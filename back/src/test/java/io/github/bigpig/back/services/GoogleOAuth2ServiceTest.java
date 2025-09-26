package io.github.bigpig.back.services;

import io.github.bigpig.back.models.Role;
import io.github.bigpig.back.models.User;
import io.github.bigpig.back.repositories.RoleRepository;
import io.github.bigpig.back.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleOAuth2ServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RestTemplate restTemplate;

    private GoogleOAuth2Service googleOAuth2Service;

    private final String CLIENT_ID = "test-client-id";
    private final String CODE = "test-code";
    private final String CODE_VERIFIER = "test-code-verifier";
    private final String REDIRECT_URI = "http://localhost:8080/callback";
    private final String ACCESS_TOKEN = "test-access-token";
    private final String EMAIL = "test@example.com";
    private final String NAME = "Test User";
    private final String JWT_TOKEN = "test-jwt-token";
    private final String USER_ROLE = "ROLE_USER";

    @BeforeEach
    void setUp() {
        // Используем рефлексию для установки значений полей
        googleOAuth2Service = new GoogleOAuth2Service(userService, jwtUtils, roleRepository);

        // Устанавливаем значения через рефлексию
        setField(googleOAuth2Service, "clientId", CLIENT_ID);
        String CLIENT_SECRET = "test-client-secret";
        setField(googleOAuth2Service, "clientSecret", CLIENT_SECRET);

        // Заменяем RestTemplate на мок
        setField(googleOAuth2Service, "restTemplate", restTemplate);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }

    @Test
    @DisplayName("exchangeCodeForToken should return access token when API call succeeds")
    void exchangeCodeForToken_Success() {
        // Arrange
        String tokenUrl = "https://oauth2.googleapis.com/token";
        Map<String, Object> tokenResponse = Map.of("access_token", ACCESS_TOKEN);

        when(restTemplate.postForObject(eq(tokenUrl), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(tokenResponse);

        // Act
        String result = googleOAuth2Service.exchangeCodeForToken(CODE, CODE_VERIFIER, REDIRECT_URI);

        // Assert
        assertEquals(ACCESS_TOKEN, result);
        verify(restTemplate).postForObject(eq(tokenUrl), argThat(entity -> {
            HttpEntity<?> httpEntity = (HttpEntity<?>) entity;
            return httpEntity.getHeaders().getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED) &&
                    httpEntity.getBody().toString().contains("grant_type=authorization_code") &&
                    httpEntity.getBody().toString().contains("code=" + CODE) &&
                    httpEntity.getBody().toString().contains("client_id=" + CLIENT_ID);
        }), eq(Map.class));
    }

    @Test
    @DisplayName("exchangeCodeForToken should handle null response")
    void exchangeCodeForToken_NullResponse_ThrowsException() {
        // Arrange
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> googleOAuth2Service.exchangeCodeForToken(CODE, CODE_VERIFIER, REDIRECT_URI));
    }

    @Test
    @DisplayName("getUserInfo should return user info when API call succeeds")
    void getUserInfo_Success() {
        // Arrange
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        Map<String, Object> userInfo = Map.of("email", EMAIL, "name", NAME);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(userInfo, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(userInfoUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(responseEntity);

        // Act
        Map<String, Object> result = googleOAuth2Service.getUserInfo(ACCESS_TOKEN);

        // Assert
        assertNotNull(result);
        assertEquals(EMAIL, result.get("email"));
        assertEquals(NAME, result.get("name"));
        verify(restTemplate).exchange(
                eq(userInfoUrl),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = ((HttpEntity<?>) entity).getHeaders();
                    return headers.getFirst(HttpHeaders.AUTHORIZATION).equals("Bearer " + ACCESS_TOKEN);
                }),
                eq(Map.class)
        );
    }

    @Test
    @DisplayName("handleGoogleLogin should create new user and return JWT when user not exists")
    void handleGoogleLogin_NewUser_Success() {
        // Arrange
        Map<String, Object> tokenResponse = Map.of("access_token", ACCESS_TOKEN);
        Map<String, Object> userInfo = Map.of("email", EMAIL, "name", NAME);
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(userInfo, HttpStatus.OK);
        Role userRole = new Role();
        userRole.setName(USER_ROLE);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(tokenResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(userInfoResponse);
        when(userService.findByUsername(NAME)).thenReturn(Optional.empty());
        when(roleRepository.findByName(USER_ROLE)).thenReturn(Optional.of(userRole));
        when(jwtUtils.generateJwtToken(any(UserDetails.class))).thenReturn(JWT_TOKEN);

        // Act
        String result = googleOAuth2Service.handleGoogleLogin(CODE, CODE_VERIFIER, REDIRECT_URI);

        // Assert
        assertEquals(JWT_TOKEN, result);
        verify(userService).findByUsername(NAME);
        verify(roleRepository).findByName(USER_ROLE);
        verify(jwtUtils).generateJwtToken(argThat(userDetails ->
                userDetails.getUsername().equals(NAME) &&
                        userDetails.getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().equals(USER_ROLE))
        ));
    }

    @Test
    @DisplayName("handleGoogleLogin should use existing user and return JWT when user exists")
    void handleGoogleLogin_ExistingUser_Success() {
        // Arrange
        Map<String, Object> tokenResponse = Map.of("access_token", ACCESS_TOKEN);
        Map<String, Object> userInfo = Map.of("email", EMAIL, "name", NAME);
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(userInfo, HttpStatus.OK);

        User existingUser = new User();
        existingUser.setUsername(NAME);
        existingUser.setPassword("encoded-password");
        Role userRole = new Role();
        userRole.setName(USER_ROLE);
        existingUser.setRoles(List.of(userRole));

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(tokenResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(userInfoResponse);
        when(userService.findByUsername(NAME)).thenReturn(Optional.of(existingUser));
        when(jwtUtils.generateJwtToken(any(UserDetails.class))).thenReturn(JWT_TOKEN);

        // Act
        String result = googleOAuth2Service.handleGoogleLogin(CODE, CODE_VERIFIER, REDIRECT_URI);

        // Assert
        assertEquals(JWT_TOKEN, result);
        verify(userService).findByUsername(NAME);
        verify(roleRepository, never()).findByName(anyString());
        verify(jwtUtils).generateJwtToken(argThat(userDetails ->
                userDetails.getUsername().equals(NAME) &&
                        userDetails.getPassword().equals("encoded-password") &&
                        userDetails.getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().equals(USER_ROLE))
        ));
    }

    @Test
    @DisplayName("handleGoogleLogin should throw exception when role not found")
    void handleGoogleLogin_RoleNotFound_ThrowsException() {
        // Arrange
        Map<String, Object> tokenResponse = Map.of("access_token", ACCESS_TOKEN);
        Map<String, Object> userInfo = Map.of("email", EMAIL, "name", NAME);
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(userInfo, HttpStatus.OK);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(tokenResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(userInfoResponse);
        when(userService.findByUsername(NAME)).thenReturn(Optional.empty());
        when(roleRepository.findByName(USER_ROLE)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> googleOAuth2Service.handleGoogleLogin(CODE, CODE_VERIFIER, REDIRECT_URI));

        assertEquals("Role ROLE_USER not found", exception.getMessage());
    }

    @Test
    @DisplayName("handleGoogleLogin should handle missing email in user info")
    void handleGoogleLogin_MissingEmail_ThrowsException() {
        // Arrange
        Map<String, Object> tokenResponse = Map.of("access_token", ACCESS_TOKEN);
        Map<String, Object> userInfo = Map.of("name", NAME); // email missing
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(userInfo, HttpStatus.OK);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(tokenResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(userInfoResponse);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> googleOAuth2Service.handleGoogleLogin(CODE, CODE_VERIFIER, REDIRECT_URI));
    }

    @Test
    @DisplayName("handleGoogleLogin should handle missing name in user info")
    void handleGoogleLogin_MissingName_ThrowsException() {
        // Arrange
        Map<String, Object> tokenResponse = Map.of("access_token", ACCESS_TOKEN);
        Map<String, Object> userInfo = Map.of("email", EMAIL); // name missing
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(userInfo, HttpStatus.OK);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(tokenResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(userInfoResponse);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> googleOAuth2Service.handleGoogleLogin(CODE, CODE_VERIFIER, REDIRECT_URI));
    }

    @Test
    @DisplayName("handleGoogleLogin should handle null user info")
    void handleGoogleLogin_NullUserInfo_ThrowsException() {
        // Arrange
        Map<String, Object> tokenResponse = Map.of("access_token", ACCESS_TOKEN);
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(tokenResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(userInfoResponse);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> googleOAuth2Service.handleGoogleLogin(CODE, CODE_VERIFIER, REDIRECT_URI));
    }

    @Test
    @DisplayName("handleGoogleLogin should propagate RestTemplate exceptions")
    void handleGoogleLogin_RestTemplateException_PropagatesException() {
        // Arrange
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Network error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> googleOAuth2Service.handleGoogleLogin(CODE, CODE_VERIFIER, REDIRECT_URI));

        assertEquals("Network error", exception.getMessage());
    }
}