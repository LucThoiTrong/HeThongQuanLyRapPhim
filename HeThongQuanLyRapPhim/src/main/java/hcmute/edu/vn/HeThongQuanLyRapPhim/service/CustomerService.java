package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoanTra;

import java.util.List;
import java.util.Set;

public interface CustomerService {
    List<DoiTuongSuDung> getAllCustomer();
    DoiTuongSuDung getCustomerById(int id);
    boolean deleteCutomer(int id);
    DoiTuongSuDung updateCustomer(int id, DoiTuongSuDung newCustomer);
    Set<HoaDon> getHoaDonByKhachHang(int id);
    Set<HoanTra> getHoanTraByKhachHang(int id);
}
