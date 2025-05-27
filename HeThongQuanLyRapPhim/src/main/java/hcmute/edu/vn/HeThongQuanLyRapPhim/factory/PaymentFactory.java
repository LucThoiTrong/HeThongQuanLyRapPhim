package hcmute.edu.vn.HeThongQuanLyRapPhim.factory;

import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.PaymentStrategy;

public interface PaymentFactory {
    PaymentStrategy createPayment();
}
