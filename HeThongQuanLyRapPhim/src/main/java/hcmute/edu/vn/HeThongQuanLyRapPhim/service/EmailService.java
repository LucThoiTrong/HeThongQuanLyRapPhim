package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import jakarta.mail.MessagingException;

public interface EmailService {
    void guiHoaDonQuaEmail(String to, HoaDon hoaDon) throws MessagingException;
}
