package hcmute.edu.vn.HeThongQuanLyRapPhim.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FactoryManager {
    //FactoryManager là lớp giúp bạn quản lý các PaymentFactory khác nhau như VNPayFactory, MomoFactory, ...
    //Khi gọi getFactory("momo"), nó sẽ trả về đúng PaymentFactory tương ứng.
    //Lưu trữ tất cả các PaymentFactory dưới dạng Map với key là tên loại thanh toán ("momo", "vnpay")
    //và value là đối tượng Factory tương ứng.
    private final Map<String, PaymentFactory> factoryMap;

    @Autowired
    public FactoryManager(List<PaymentFactory> factories) {
        factoryMap = new HashMap<>();
        for (PaymentFactory factory : factories) {
            String key = factory.getClass().getAnnotation(Component.class).value(); // ví dụ: "momoFactory"
            factoryMap.put(key.replace("Factory", "").toLowerCase(), factory); // lưu là "momo", "vnpay"
        }
    }

    public PaymentFactory getFactory(String paymentType) {
        PaymentFactory factory = factoryMap.get(paymentType.toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("Không tìm thấy factory cho loại thanh toán: " + paymentType);
        }
        return factory;
    }
}

