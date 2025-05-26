package hcmute.edu.vn.HeThongQuanLyRapPhim.event;


public record PasswordRecoveryRequestedEvent(String email, int id) implements AppEvent {
}
