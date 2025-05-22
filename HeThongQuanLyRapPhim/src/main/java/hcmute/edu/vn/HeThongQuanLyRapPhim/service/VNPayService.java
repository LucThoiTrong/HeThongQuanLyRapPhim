package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;

import java.util.Map;

public interface VNPayService {
    String createPayment(int amount, String clientIp);
    String getPaymentMessage(String responseCode);
    void saveMaGiamGiaApDung(MaGiamGia maGiamGia);
    void createInvoice(String danhSachGhe, Map<Integer, Integer> dsChiTietComBoBapNuoc, SuatChieu suatChieu, DoiTuongSuDung customer, double tongTienSauGiam);
}
