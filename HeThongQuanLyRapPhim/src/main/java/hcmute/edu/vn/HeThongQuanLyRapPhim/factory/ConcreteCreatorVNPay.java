package hcmute.edu.vn.HeThongQuanLyRapPhim.factory;

import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.PaymentStrategy;
import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.VnpayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("vnpay")
public class ConcreteCreatorVNPay implements PaymentFactory{
    private final VnpayStrategy vnpayStrategy;

    @Autowired
    public ConcreteCreatorVNPay(VnpayStrategy vnpayStrategy) {
        this.vnpayStrategy = vnpayStrategy;
    }

    @Override
    public PaymentStrategy createPayment() {
        return vnpayStrategy;
    }
}
