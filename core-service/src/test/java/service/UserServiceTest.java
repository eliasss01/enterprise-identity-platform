package service;

import com.bourasenterprises.identity.core.domain.UserEntity;
import com.bourasenterprises.identity.core.domain.exception.BusinessException;
import com.bourasenterprises.identity.core.repository.UserRepository;
import com.bourasenterprises.identity.core.service.UserService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldCreateUserWhenEmailNotExists() {
        // GIVEN
        UserEntity input = UserEntity.builder()
                .email("test@mail.com")
                .fullName("Mario Rossi")
                .build();

        when(repository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        when(keycloak.realm("enterprise-platform")).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        Response response = Response.status(201)
                .header("Location", "http://localhost/users/123e4567-e89b-12d3-a456-426614174000")
                .build();

        when(usersResource.create(any())).thenReturn(response);

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        UserEntity result = userService.createUser(input);

        // THEN
        assertThat(result.getEmail()).isEqualTo("test@mail.com");
        assertThat(result.getKeycloakId()).isNotNull();
        verify(repository).save(any());
    }

    @Test
    void createUser_shouldThrowExceptionIfEmailExists() {
        // GIVEN
        UserEntity input = UserEntity.builder()
                .email("test@mail.com")
                .build();

        when(repository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(new UserEntity()));

        // WHEN + THEN
        assertThatThrownBy(() -> userService.createUser(input))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email già presente");

        verify(repository, never()).save(any());
    }

    @Test
    void getUser_shouldReturnUserIfExists() {
        UserEntity user = UserEntity.builder().email("a@a.com").build();

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity result = userService.getUser(1L);

        assertThat(result).isNotNull();
    }

    @Test
    void getUser_shouldThrowExceptionIfNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(99L))
                .isInstanceOf(BusinessException.class);
    }
}
