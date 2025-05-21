package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;

import java.io.UnsupportedEncodingException;

public interface VNPayService {
    String createPayment(int amount, String clientIp) throws UnsupportedEncodingException;
    String getPaymentMessage(String responseCode);
    void saveMaGiamGiaApDung(MaGiamGia maGiamGia);
}
