package hcmute.edu.vn.HeThongQuanLyRapPhim.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.edu.vn.HeThongQuanLyRapPhim.config.MomoConfig;
import hcmute.edu.vn.HeThongQuanLyRapPhim.event.InvoiceGeneratedEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.observer.AppEventManager;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ChairRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.InvoiceRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.PopcornDrinkComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MomoStrategy implements PaymentStrategy{
    private final InvoiceRepository invoiceRepository;
    private final ChairRepository chairRepository;
    private final PopcornDrinkComboRepository popcornDrinkComboRepository;
    private final DiscountRepository discountRepository;
    private final AppEventManager appEventManager;
    private final MomoConfig momoConfig;
    @Autowired
    public MomoStrategy(InvoiceRepository invoiceRepository,
                        ChairRepository chairRepository,
                        PopcornDrinkComboRepository popcornDrinkComboRepository,
                        DiscountRepository discountRepository, MomoConfig momoConfig,
                        AppEventManager appEventManager) {
        this.invoiceRepository = invoiceRepository;
        this.chairRepository = chairRepository;
        this.popcornDrinkComboRepository = popcornDrinkComboRepository;
        this.discountRepository = discountRepository;
        this.appEventManager = appEventManager;
        this.momoConfig = momoConfig;
    }
    public String createPayment(String amount) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();
        String extraData = "";
        String orderInfo="Thanh toán đơn hàng";
        String rawSignature = "accessKey=" + momoConfig.getAccessKey()
                + "&amount=" + amount
                + "&extraData=" + extraData
                + "&ipnUrl=" + momoConfig.getIpnUrl()
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + momoConfig.getPartnerCode()
                + "&redirectUrl=" + momoConfig.getRedirectUrl()
                + "&requestId=" + requestId
                + "&requestType=captureWallet";


        String signature = hmacSHA256(rawSignature, momoConfig.getSecretKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", momoConfig.getPartnerCode());
        requestBody.put("accessKey", momoConfig.getAccessKey());
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("redirectUrl", momoConfig.getRedirectUrl());
        requestBody.put("ipnUrl", momoConfig.getIpnUrl());
        requestBody.put("extraData", extraData);
        requestBody.put("requestType", "captureWallet");
        requestBody.put("signature", signature);
        requestBody.put("lang", "vi");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(momoConfig.getEndpoint()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBody)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.body()).get("payUrl").asText();  //trả về url de chuyen huong sang trang cua momo
    }

    @Override
    public void saveMaGiamGiaApDung(MaGiamGia maGiamGia) {
        discountRepository.save(maGiamGia);
    }
    @Override
    public void createInvoice(String danhSachGhe, Map<Integer, Integer> dsChiTietComBoBapNuoc, SuatChieu suatChieu, DoiTuongSuDung customer, double tongTienSauGiam) {
        HoaDon hoaDon = new HoaDon();
        // Khởi tạo vé
        Set<VeXemPhim> dsVeXemPhim = createTicket(danhSachGhe, hoaDon, suatChieu);
        // Khởi tạo danh sách combo đã mua
        Set<ChiTietComBoBapNuoc> dsChiTietComBoBapNuocDaMua = createDetailCombo(dsChiTietComBoBapNuoc, hoaDon);

        // Set các thông tin liên quan
        hoaDon.setDoiTuongSuDung(customer);
        hoaDon.setSuatChieu(suatChieu);
        hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDon.setDsVeXemPhimDaMua(dsVeXemPhim);
        hoaDon.setTongGiaTien(tongTienSauGiam);
        hoaDon.setDsComBoDaMua(dsChiTietComBoBapNuocDaMua);
        hoaDon.setNgayThanhToan(LocalDateTime.now());

        invoiceRepository.save(hoaDon);

        // Tiến hành gửi mail
        InvoiceGeneratedEvent invoiceGeneratedEvent = new InvoiceGeneratedEvent(customer.getEmail(), hoaDon);
        appEventManager.notify(invoiceGeneratedEvent);
    }

    private Set<ChiTietComBoBapNuoc> createDetailCombo(Map<Integer, Integer> dsChiTietComBoBapNuoc, HoaDon hoaDon) {
        Set<ChiTietComBoBapNuoc> dsChiTietComBoBapNuocDaMua = new HashSet<>();
        if (dsChiTietComBoBapNuoc != null) {
            for (Map.Entry<Integer, Integer> entry : dsChiTietComBoBapNuoc.entrySet()) {
                Integer idCombo = entry.getKey();
                Integer soLuong = entry.getValue();
                ComboBapNuoc comboBapNuoc = popcornDrinkComboRepository.findById(idCombo).orElse(null);
                ChiTietComBoBapNuoc chiTietComBoBapNuoc = new ChiTietComBoBapNuoc();
                chiTietComBoBapNuoc.setComboBapNuoc(comboBapNuoc);
                chiTietComBoBapNuoc.setSoLuong(soLuong);
                chiTietComBoBapNuoc.setHoaDon(hoaDon);
                dsChiTietComBoBapNuocDaMua.add(chiTietComBoBapNuoc);
            }
        }
        return dsChiTietComBoBapNuocDaMua;
    }

    private Set<VeXemPhim> createTicket(String danhSachGhe, HoaDon hoaDon, SuatChieu suatChieu) {
        Set<VeXemPhim> dsVeXemPhim = new HashSet<>();
        if (danhSachGhe != null && !danhSachGhe.isEmpty()) {
            String[] gheArray = danhSachGhe.split(",");
            for (String id : gheArray) {
                // Xoá tất cả ký tự không phải là số
                String soGhe = id.replaceAll("[^0-9]", "");  // giữ lại chỉ chữ số
                if (!soGhe.isEmpty()) {
                    Ghe ghe = chairRepository.findById(Integer.parseInt(soGhe)).orElse(null);
                    VeXemPhim veXemPhim = new VeXemPhim();
                    veXemPhim.setGhe(ghe);
                    veXemPhim.setSuatChieu(suatChieu);
                    veXemPhim.setHoaDon(hoaDon);
                    dsVeXemPhim.add(veXemPhim);
                }
            }
        }
        return dsVeXemPhim;
    }
    private String hmacSHA256(String data, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hmacData) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
