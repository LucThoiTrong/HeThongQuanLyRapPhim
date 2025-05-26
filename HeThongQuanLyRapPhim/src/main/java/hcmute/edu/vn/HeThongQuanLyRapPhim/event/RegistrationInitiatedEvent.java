package hcmute.edu.vn.HeThongQuanLyRapPhim.event;

public record RegistrationInitiatedEvent(String email, int id) implements AppEvent {
}
