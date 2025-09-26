package io.github.bigpig.back.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private final String JWT_SECRET = "mySuperSecretKeyThatIsLongEnoughForHS256Algorithm";
    private final int JWT_EXPIRATION_MS = 3600000; // 1 hour

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        // Устанавливаем значения через рефлексию
        setField(jwtUtils, "jwtSecret", JWT_SECRET);
        setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private UserDetails createUserDetails(String username, String... roles) {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        // Создаем коллекцию GrantedAuthority
        Collection<GrantedAuthority> authorities = List.of(roles).stream()
                .map(SimpleGrantedAuthority::new)
                .map(authority -> (GrantedAuthority) authority)
                .toList();

        // Правильное использование thenReturn с Collection<? extends GrantedAuthority>
        when(userDetails.getAuthorities()).thenAnswer(invocation -> authorities);

        return userDetails;
    }

    @Test
    @DisplayName("generateJwtToken should generate valid JWT token")
    void generateJwtToken_ShouldGenerateValidToken() {
        // Arrange
        UserDetails userDetails = createUserDetails("testuser", "ROLE_USER", "ROLE_ADMIN");

        // Act
        String token = jwtUtils.generateJwtToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verify token structure
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length); // Header, Payload, Signature
    }

    @Test
    @DisplayName("generateJwtToken should include username in subject")
    void generateJwtToken_ShouldIncludeUsername() {
        // Arrange
        String username = "john.doe";
        UserDetails userDetails = createUserDetails(username, "ROLE_USER");

        // Act
        String token = jwtUtils.generateJwtToken(userDetails);
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("generateJwtToken should include roles in claims")
    void generateJwtToken_ShouldIncludeRoles() {
        // Arrange
        UserDetails userDetails = createUserDetails("testuser", "ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR");

        // Act
        String token = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = jwtUtils.getRolesFromToken(token);

        // Assert
        assertNotNull(roles);
        assertEquals(3, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
        assertTrue(roles.contains("ROLE_MODERATOR"));
    }

    @Test
    @DisplayName("generateJwtToken should handle user without roles")
    void generateJwtToken_UserWithoutRoles() {
        // Arrange
        UserDetails userDetails = createUserDetails("testuser");

        // Act
        String token = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = jwtUtils.getRolesFromToken(token);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    @DisplayName("getUserNameFromJwtToken should extract username from valid token")
    void getUserNameFromJwtToken_ValidToken() {
        // Arrange
        String username = "alice";
        UserDetails userDetails = createUserDetails(username, "ROLE_USER");
        String token = jwtUtils.generateJwtToken(userDetails);

        // Act
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("getRolesFromToken should extract roles from valid token")
    void getRolesFromToken_ValidToken() {
        // Arrange
        UserDetails userDetails = createUserDetails("bob", "ROLE_USER", "ROLE_ADMIN");
        String token = jwtUtils.generateJwtToken(userDetails);

        // Act
        List<String> roles = jwtUtils.getRolesFromToken(token);

        // Assert
        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("getUserNameFromJwtToken should throw exception for expired token")
    void getUserNameFromJwtToken_ExpiredToken() throws Exception {
        // Arrange - создаем утилиту с очень коротким временем жизни
        JwtUtils shortLivedJwtUtils = new JwtUtils();
        setField(shortLivedJwtUtils, "jwtSecret", JWT_SECRET);
        setField(shortLivedJwtUtils, "jwtExpirationMs", 1); // 1 ms

        UserDetails userDetails = createUserDetails("testuser", "ROLE_USER");
        String token = shortLivedJwtUtils.generateJwtToken(userDetails);

        // Ждем истечения срока действия
        Thread.sleep(10);

        // Act & Assert
        assertThrows(ExpiredJwtException.class, () -> shortLivedJwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    @DisplayName("getUserNameFromJwtToken should throw exception for tampered token")
    void getUserNameFromJwtToken_TamperedToken() {
        // Arrange
        UserDetails userDetails = createUserDetails("testuser", "ROLE_USER");
        String validToken = jwtUtils.generateJwtToken(userDetails);

        // Подделываем токен - меняем часть подписи
        String[] parts = validToken.split("\\.");
        String tamperedToken = parts[0] + "." + parts[1] + ".tamperedSignature";

        // Act & Assert
        assertThrows(SignatureException.class, () -> jwtUtils.getUserNameFromJwtToken(tamperedToken));
    }

    @Test
    @DisplayName("getUserNameFromJwtToken should throw exception for malformed token")
    void getUserNameFromJwtToken_MalformedToken() {
        // Arrange
        String malformedToken = "not.a.valid.jwt.token";

        // Act & Assert
        assertThrows(MalformedJwtException.class, () -> jwtUtils.getUserNameFromJwtToken(malformedToken));
    }
}
