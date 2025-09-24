package io.github.bigpig.back.services;

import io.github.bigpig.back.exceptions.EntitySaveException;
import io.github.bigpig.back.exceptions.UserAlreadyExistsException;
import io.github.bigpig.back.models.Role;
import io.github.bigpig.back.models.User;
import io.github.bigpig.back.repositories.RoleRepository;
import io.github.bigpig.back.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByUsernameTest() {
        User user = new User();
        user.setUsername("Ivan");
        Optional<User> expUser = Optional.of(user);
        when(userRepository.findByUsername("Ivan")).thenReturn(expUser);

        Optional<User> result = userService.findByUsername("Ivan");

        assertEquals(expUser, result);

        Mockito.verify(userRepository, only()).findByUsername("Ivan");
    }

    @Test
    void loadUserByUsernameTest() {

        String username = "Ivan";
        Role testRole = new Role();
        testRole.setName("ROLE_USER");
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("Pass");
        testUser.setRoles(List.of(testRole));

        Optional<User> expUser = Optional.of(testUser);
        when(userRepository.findByUsername(username)).thenReturn(expUser);

        UserDetails result = userService.loadUserByUsername("Ivan");

        assertAll(
                "UserDetails properties",
                () -> assertEquals(username, result.getUsername()),
                () -> assertEquals("Pass", result.getPassword()),
                () -> assertEquals(1, result.getAuthorities().size()),
                () -> assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")))
        );

        Mockito.verify(userRepository, only()).findByUsername(username);
    }

    @Test
    void loadUserByUsernameThrowExceptionTest() {

        String username = "Ivan";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(username)
        );

        assertEquals("User Ivan not found", exception.getMessage());
        Mockito.verify(userRepository, only()).findByUsername("Ivan");
    }

    @Test
    void createNewUserThrowUserAlreadyExistsExceptionTest() {

        String username = "Ivan";
        Role testRole = new Role();
        testRole.setName("ROLE_USER");
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("Pass");
        testUser.setRoles(List.of(testRole));

        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> userService.createNewUser(testUser)
        );

        assertEquals("User with username '" + username + "' already exists", exception.getMessage());
        Mockito.verify(userRepository, only()).findByUsername(username);
    }

    @Test
    void createNewUserThrowIllegalStateExceptionTest() {

        Role testRole = new Role();
        String roleName = "ROLE_USER";

        String username = "Ivan";

        testRole.setName(roleName);
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("Pass");
        testUser.setRoles(List.of(testRole));

        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> userService.createNewUser(testUser)
        );

        assertEquals(String.format("Role %s not found", roleName), exception.getMessage());
        Mockito.verify(userRepository, only()).findByUsername(username);
        Mockito.verify(roleRepository, only()).findByName(roleName);
    }

    @Test
    void createNewUserThrowEntitySaveExceptionTest() {

        Role testRole = new Role();
        String roleName = "ROLE_USER";

        String username = "Ivan";

        testRole.setName(roleName);
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("Pass");
        testUser.setRoles(List.of(testRole));

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(testRole));
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(testUser)).thenThrow(new RuntimeException("Simulated DB failure"));

        EntitySaveException exception = assertThrows(EntitySaveException.class,
                () -> userService.createNewUser(testUser)
        );

        assertEquals("User create exception", exception.getMessage());
        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(roleRepository).findByName(roleName);
    }

    @Test
    void createNewUserCorrectTest() {

        Role testRole = new Role();
        String roleName = "ROLE_USER";

        String username = "Ivan";

        testRole.setName(roleName);
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("Pass");
        testUser.setRoles(List.of(testRole));

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(testRole));
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        userService.createNewUser(testUser);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(roleRepository).findByName(roleName);
        Mockito.verify(userRepository).save(testUser);
    }
}
