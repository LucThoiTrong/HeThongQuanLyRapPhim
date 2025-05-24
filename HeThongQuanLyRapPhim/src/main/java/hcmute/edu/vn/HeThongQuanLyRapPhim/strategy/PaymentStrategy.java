package hcmute.edu.vn.HeThongQuanLyRapPhim.strategy;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;

import java.util.Map;

public interface PaymentStrategy {
    String createPayment(String amount) throws Exception;
    void createInvoice(String danhSachGhe, Map<Integer, Integer> dsChiTietComBoBapNuoc, SuatChieu suatChieu, DoiTuongSuDung customer, double tongTienSauGiam);
    void saveMaGiamGiaApDung(MaGiamGia maGiamGia);
}
