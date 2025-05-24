package hcmute.edu.vn.HeThongQuanLyRapPhim.factory;

import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.MomoStrategy;
import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.PaymentStrategy;
import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.VnpayStrategy;
import org.springframework.stereotype.Component;

//lop nay lay doi tuong phu hop voi dieu kiện dau vao
@Component
public class PaymentFactory {
    private final MomoStrategy momoStrategy;
    private final VnpayStrategy vnpayStrategy;

    public PaymentFactory(MomoStrategy momo, VnpayStrategy vnpay) {
        this.momoStrategy = momo;
        this.vnpayStrategy = vnpay;
    }

    public PaymentStrategy getPaymentStrategy(String method) {
        return switch (method.toLowerCase()) {
            case "momo" -> momoStrategy;
            case "vnpay" -> vnpayStrategy;
            default -> throw new IllegalArgumentException(method);
        };
    }
}
