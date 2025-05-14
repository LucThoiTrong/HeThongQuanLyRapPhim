package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import java.io.UnsupportedEncodingException;

public interface VNPayService {
    String createPayment(int amount, String clientIp) throws UnsupportedEncodingException;
    String getPaymentMessage(String responseCode);
}
