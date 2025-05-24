package hcmute.edu.vn.HeThongQuanLyRapPhim.event;

import org.springframework.context.ApplicationEvent;

public class PasswordRecoveryRequestedEvent extends ApplicationEvent {
    private final String email;
    private final int id;
    public PasswordRecoveryRequestedEvent(Object source, String email, int id) {
        super(source);
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
