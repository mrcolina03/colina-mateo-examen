package ec.edu.espe.colinamateoexamen.service;

import ec.edu.espe.colinamateoexamen.model.RoomReservation;
import ec.edu.espe.colinamateoexamen.repository.RoomReservationRepository;

import java.util.Objects;
import java.util.regex.Pattern;

public class RoomReservationService {

    private final RoomReservationRepository repository;
    private final UserPolicyClient userPolicyClient;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public RoomReservationService(RoomReservationRepository repository, UserPolicyClient userPolicyClient) {
        this.repository = Objects.requireNonNull(repository);
        this.userPolicyClient = Objects.requireNonNull(userPolicyClient);
    }

    public ReservationResponse createReservation(String roomCode, String email, int hours) {
        // Validaciones locales primero
        if (roomCode == null || roomCode.trim().isEmpty()) {
            throw new IllegalArgumentException("roomCode no puede ser nulo o vacío");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("email inválido");
        }
        if (hours <= 0 || hours > 8) {
            throw new IllegalArgumentException("hours debe ser mayor a 0 y menor o igual a 8");
        }

        // Verificar si la sala ya está reservada (dependencia externa)
        boolean exists = repository.existsByReservationCode(roomCode);
        if (exists) {
            throw new IllegalStateException("La sala ya se encuentra reservada");
        }

        // Verificar política de usuario (dependencia externa)
        boolean blocked = userPolicyClient.isBlocked(email);
        if (blocked) {
            throw new IllegalStateException("El usuario está bloqueado por políticas institucionales");
        }

        RoomReservation reservation = new RoomReservation(roomCode, email, hours, "CREATED");
        RoomReservation saved = repository.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getRoomCode(), saved.getReservedEmail(), saved.getHours(), saved.getStatus());
    }
}