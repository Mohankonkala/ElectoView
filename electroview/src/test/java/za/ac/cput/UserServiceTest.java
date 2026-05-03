package za.ac.cput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.Enums.*;
import za.ac.cput.domain.*;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("Malesela Modiba")
                .email("malesela@cput.ac.za")
                .passwordHash("hashedpassword123")
                .role(Role.ANALYST)
                .build();
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail(testUser.getEmail()))
                .thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        User result = userService.createUser(
                testUser.getName(),
                testUser.getEmail(),
                testUser.getPasswordHash(),
                testUser.getRole()
        );

        assertNotNull(result);
        assertEquals("Malesela Modiba", result.getName());
        assertEquals(Role.ANALYST, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail(testUser.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(
                        testUser.getName(),
                        testUser.getEmail(),
                        testUser.getPasswordHash(),
                        testUser.getRole()
                )
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findById_UserExists_ReturnsUser() {
        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(testUser));

        User result = userService.findById("user-1");

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
    }

    @Test
    void findById_UserNotFound_ThrowsException() {
        when(userRepository.findById("invalid-id"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                userService.findById("invalid-id")
        );
    }

    @Test
    void activate_SetsStatusToActive() {
        testUser.setStatus(AccountStatus.PENDING);
        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        User result = userService.activate("user-1");

        assertEquals(AccountStatus.ACTIVE, result.getStatus());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void deactivate_SetsStatusToInactive() {
        testUser.setStatus(AccountStatus.ACTIVE);
        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        User result = userService.deactivate("user-1");

        assertEquals(AccountStatus.INACTIVE, result.getStatus());
    }

    @Test
    void recordFailedLogin_LocksAfterFiveAttempts() {
        testUser.activate();
        when(userRepository.findByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        for (int i = 0; i < 5; i++) {
            userService.recordFailedLogin(testUser.getEmail());
        }

        assertTrue(testUser.isLocked());
        assertEquals(AccountStatus.LOCKED, testUser.getStatus());
    }

    @Test
    void unlock_ResetsAttemptsAndActivates() {
        testUser.lockAccount();
        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        User result = userService.unlock("user-1");

        assertFalse(result.isLocked());
        assertEquals(0, result.getFailedLoginAttempts());
    }

    @Test
    void resetPassword_BlankHash_ThrowsException() {
        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () ->
                userService.resetPassword("user-1", "")
        );
    }

    @Test
    void findAll_ReturnsAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(testUser));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
    }
}