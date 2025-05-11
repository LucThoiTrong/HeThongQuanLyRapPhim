package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import java.io.UnsupportedEncodingException;

public interface VNPayService {
    String createPayment(int amount) throws UnsupportedEncodingException;
    String getPaymentMessage(String responseCode);
}
