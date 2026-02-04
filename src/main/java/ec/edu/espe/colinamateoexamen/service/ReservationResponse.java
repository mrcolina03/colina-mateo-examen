package ec.edu.espe.colinamateoexamen.service;

public class ReservationResponse {
    private final String id;
    private final String roomCode;
    private final String reservedEmail;
    private final double hours;
    private final String status;

    public ReservationResponse(String id, String roomCode, String reservedEmail, double hours, String status) {
        this.id = id;
        this.roomCode = roomCode;
        this.reservedEmail = reservedEmail;
        this.hours = hours;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getReservedEmail() {
        return reservedEmail;
    }

    public double getHours() {
        return hours;
    }

    public String getStatus() {
        return status;
    }
}
