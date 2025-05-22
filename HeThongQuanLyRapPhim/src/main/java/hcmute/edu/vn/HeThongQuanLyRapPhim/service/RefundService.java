package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoanTra;

public interface RefundService {
    HoaDon getHoaDon(int id);
    HoanTra save(HoaDon hoaDon, DoiTuongSuDung customer, String lyDoHoanTra);
    String checkHoaDon(HoaDon hoaDon);
}
