package hcmute.edu.vn.HeThongQuanLyRapPhim.strategy;

import hcmute.edu.vn.HeThongQuanLyRapPhim.factory.FactoryManager;
import hcmute.edu.vn.HeThongQuanLyRapPhim.factory.PaymentFactory;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {
    private final FactoryManager factoryManager;

    @Autowired
    public PaymentService(FactoryManager factoryManager) {
        this.factoryManager = factoryManager;
    }

    public String processPayment(String paymentType, double amount) {
        PaymentFactory factory = factoryManager.getFactory(paymentType);
        PaymentStrategy paymentStrategy = factory.createPayment();
        try {
            String paymentUrl = paymentStrategy.createPayment(String.valueOf((int)amount));
            return paymentUrl;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
    public void createInvoice(String method, String danhSachGhe, Map<Integer, Integer> dsChiTietComBoBapNuoc, SuatChieu suatChieu, DoiTuongSuDung customer, double tongTienSauGiam)
    {
        PaymentFactory factory = factoryManager.getFactory(method);
        PaymentStrategy paymentStrategy = factory.createPayment();
        paymentStrategy.createInvoice(danhSachGhe, dsChiTietComBoBapNuoc, suatChieu, customer, tongTienSauGiam);
    }
    public void saveMaGiamGiaApDung(String method, MaGiamGia maGiamGia)
    {
        PaymentFactory factory = factoryManager.getFactory(method);
        PaymentStrategy paymentStrategy = factory.createPayment();
        paymentStrategy.saveMaGiamGiaApDung(maGiamGia);
    }
}

