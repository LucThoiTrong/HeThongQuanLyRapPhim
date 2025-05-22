package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;

import java.util.List;

public interface InvoiceService {
    //tao hoa don moi
    HoaDon save(HoaDon hoaDon);
    HoaDon findById(int id);
}
