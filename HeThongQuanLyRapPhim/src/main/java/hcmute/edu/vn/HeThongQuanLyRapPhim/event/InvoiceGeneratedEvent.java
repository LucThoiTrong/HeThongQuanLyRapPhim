package hcmute.edu.vn.HeThongQuanLyRapPhim.event;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;

public record InvoiceGeneratedEvent(String email, HoaDon hoaDon) implements AppEvent {
}
