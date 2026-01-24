package application;

import com.bourasenterprises.identity.core.application.UserApplicationService;
import com.bourasenterprises.identity.core.domain.UserEntity;
import com.bourasenterprises.identity.core.infrastructure.kafka.messaging.KafkaProducerService;
import com.bourasenterprises.identity.core.mapper.UserMapper;
import com.bourasenterprises.identity.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.generated.model.CreateUserRequest;
import com.example.generated.model.UserResponse;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock
    private UserService service;

    @Mock
    private UserMapper mapper;

    @Mock
    private KafkaProducerService kafkaProducer;

    @InjectMocks
    private UserApplicationService appService;

    @Test
    void createUser_shouldPublishKafkaEvent() {
        // GIVEN
        com.example.generated.model.CreateUserRequest request = new CreateUserRequest().email("test@mail.com");

        UserEntity entity = UserEntity.builder()
                .email("test@mail.com")
                .keycloakId(UUID.randomUUID())
                .build();

        com.example.generated.model.UserResponse response = new UserResponse().id(1L).email("test@mail.com");

        when(mapper.toEntity(request)).thenReturn(entity);
        when(service.createUser(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        // WHEN
        UserResponse result = appService.createUser(request);

        // THEN
        assertThat(result.getEmail()).isEqualTo("test@mail.com");
        verify(kafkaProducer).publishSubscriptionActivated(any());
    }

    @Test
    void createUser_shouldNotFailIfKafkaFails() {
        CreateUserRequest request = new CreateUserRequest().email("test@mail.com");

        UserEntity entity = UserEntity.builder()
                .email("test@mail.com")
                .keycloakId(UUID.randomUUID())
                .build();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(service.createUser(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(new UserResponse());

        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaProducer).publishSubscriptionActivated(any());

        // deve NON lanciare eccezione
        assertThatCode(() -> appService.createUser(request))
                .doesNotThrowAnyException();
    }
}

