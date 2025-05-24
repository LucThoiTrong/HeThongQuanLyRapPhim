package hcmute.edu.vn.HeThongQuanLyRapPhim.event;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import org.springframework.context.ApplicationEvent;

public class InvoiceGeneratedEvent extends ApplicationEvent {
    private final String email;
    private final HoaDon hoaDon;
    public InvoiceGeneratedEvent(Object source, String email, HoaDon hoaDon) {
        super(source);
        this.email = email;
        this.hoaDon = hoaDon;
    }

    public String getEmail() {
        return email;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }
}
