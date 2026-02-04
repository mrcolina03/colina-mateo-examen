package ec.edu.espe.colinamateoexamen;

import ec.edu.espe.colinamateoexamen.model.RoomReservation;
import ec.edu.espe.colinamateoexamen.repository.RoomReservationRepository;
import ec.edu.espe.colinamateoexamen.service.ReservationResponse;
import ec.edu.espe.colinamateoexamen.service.RoomReservationService;
import ec.edu.espe.colinamateoexamen.service.UserPolicyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomReservationServiceTest {

    private RoomReservationRepository repository;
    private UserPolicyClient userPolicyClient;
    private RoomReservationService service;

    @BeforeEach
    void setUp() {
        repository = mock(RoomReservationRepository.class);
        userPolicyClient = mock(UserPolicyClient.class);
        service = new RoomReservationService(repository, userPolicyClient);
    }

    @Test
    void createReservation_correcto() {
        // Arrange
        String roomCode = "1";
        String email = "mrcolina@espe.edu.ec";
        int hours = 3;

        when(repository.existsByReservationCode(roomCode)).thenReturn(false);
        when(userPolicyClient.isBlocked(email)).thenReturn(false);
        // Simular que el repositorio devuelve la misma reserva (con id generado)
        when(repository.save(any(RoomReservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ReservationResponse response = service.createReservation(roomCode, email, hours);

        // Assert
        assertNotNull(response.getId());
        assertEquals(roomCode, response.getRoomCode());
        assertEquals(email, response.getReservedEmail());
        assertEquals(hours, (int) response.getHours());
        assertEquals("CREATED", response.getStatus());

        verify(repository).existsByReservationCode(roomCode);
        verify(userPolicyClient).isBlocked(email);
        ArgumentCaptor<RoomReservation> captor = ArgumentCaptor.forClass(RoomReservation.class);
        verify(repository).save(captor.capture());

        RoomReservation saved = captor.getValue();
        assertEquals(roomCode, saved.getRoomCode());
        assertEquals(email, saved.getReservedEmail());
        assertEquals(hours, (int) saved.getHours());
    }

    @Test
    void createReservation_invalidEmail_throws() {
        // Arrange
        String roomCode = "1";
        String email = "mrcolina-no-valido";
        int hours = 2;

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createReservation(roomCode, email, hours));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));

        verifyNoInteractions(repository);
        verifyNoInteractions(userPolicyClient);
    }

    @Test
    void createReservation_hoursOutOfRange_throws() {
        // Arrange
        String roomCode = "1";
        String email = "mrcolina@espe.edu.ec";
        int hours = 10;

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createReservation(roomCode, email, hours));
        assertTrue(ex.getMessage().toLowerCase().contains("hours"));

        verifyNoInteractions(repository);
        verifyNoInteractions(userPolicyClient);
    }

    @Test
    void createReservation_roomAlreadyReserved_throws() {
        // Arrange
        String roomCode = "1";
        String email = "mrcolina@espe.edu.ec";
        int hours = 2;

        when(repository.existsByReservationCode(roomCode)).thenReturn(true);


        //cambios

        //cabm++bmios

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.createReservation(roomCode, email, hours));
        assertTrue(ex.getMessage().toLowerCase().contains("reservada") || ex.getMessage().toLowerCase().contains("reserv"));

        verify(repository).existsByReservationCode(roomCode);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(userPolicyClient);
    }

}
