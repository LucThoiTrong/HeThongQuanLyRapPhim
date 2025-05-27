package hcmute.edu.vn.HeThongQuanLyRapPhim.factory;

import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.MomoStrategy;
import hcmute.edu.vn.HeThongQuanLyRapPhim.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("momo")
public class ConcreteCreatorMomo implements PaymentFactory{
    private final MomoStrategy momoStrategy;

    @Autowired
    public ConcreteCreatorMomo(MomoStrategy momoStrategy) {
        this.momoStrategy = momoStrategy;
    }

    @Override
    public PaymentStrategy createPayment() {
        return momoStrategy;
    }
}
