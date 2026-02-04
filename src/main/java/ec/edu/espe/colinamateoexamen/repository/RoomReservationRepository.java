package ec.edu.espe.colinamateoexamen.repository;

import ec.edu.espe.colinamateoexamen.model.RoomReservation;

import java.util.Optional;

public interface RoomReservationRepository {
    RoomReservation save(RoomReservation roomReservation);
    Optional<RoomReservation> findById(String id);
    boolean existsByReservationCode(String reservationCode);
}
