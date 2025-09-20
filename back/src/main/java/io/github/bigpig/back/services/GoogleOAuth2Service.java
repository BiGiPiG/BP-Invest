package io.github.bigpig.back.services;

import io.github.bigpig.back.models.User;
import io.github.bigpig.back.repositories.RoleRepository;
import io.github.bigpig.back.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String exchangeCodeForToken(String code, String codeVerifier, String redirectUri) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code" +
                "&code=" + code +
                "&redirect_uri=" + redirectUri +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code_verifier=" + codeVerifier;

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        Map<String, Object> response = restTemplate.postForObject(tokenUrl, request, Map.class);
        return (String) response.get("access_token");
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        return restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class).getBody();
    }

    public String handleGoogleLogin(String code, String codeVerifier, String redirectUri) {
        String accessToken = exchangeCodeForToken(code, codeVerifier, redirectUri);

        Map<String, Object> userInfo = getUserInfo(accessToken);
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String userRole = "ROLE_USER";

        Optional<User> user = userService.findByUsername(name);
        UserDetails userDetails;
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setRoles(List.of(roleRepository.findByName(userRole).orElseThrow(
                    () -> new IllegalStateException(String.format("Role %s not found", userRole))
            )));
            userDetails = new org.springframework.security.core.userdetails.User(
                    name,
                   "",
                    newUser.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
            );
        } else {
            userDetails = new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    user.get().getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
            );
        }

        return jwtUtils.generateJwtToken(userDetails);
    }
}