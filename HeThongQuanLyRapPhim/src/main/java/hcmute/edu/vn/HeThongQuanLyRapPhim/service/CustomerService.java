package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoanTra;

import java.util.List;

public interface CustomerService {
    List<DoiTuongSuDung> getAllCustomer();
    DoiTuongSuDung getCustomerById(int id);
    boolean deleteCutomer(int id);
    DoiTuongSuDung updateCustomer(int id, DoiTuongSuDung newCustomer);
    List<HoaDon> getHoaDonOfCustomer(int id);
    List<HoanTra> getHoanTraOfCustomer(int id);
}
