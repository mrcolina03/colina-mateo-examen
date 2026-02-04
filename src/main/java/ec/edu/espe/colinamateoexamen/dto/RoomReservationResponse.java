package ec.edu.espe.colinamateoexamen.dto;

public class RoomReservationResponse {
    private final String walletId;
    private final double balance;
    public RoomReservationResponse(String walletId, double balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public String getWalletId() {
        return walletId;
    }

    public double getBalance() {
        return balance;
    }

}
