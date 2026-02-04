package ec.edu.espe.colinamateoexamen.repository;

import ec.edu.espe.buildtestci.model.Wallet;

import java.util.Optional;

public interface RoomReservationRepository {
    Wallet save(Wallet wallet);
    Optional<Wallet> findById(String id);
    boolean existsByOwnerEmail(String ownerEmail);
}
