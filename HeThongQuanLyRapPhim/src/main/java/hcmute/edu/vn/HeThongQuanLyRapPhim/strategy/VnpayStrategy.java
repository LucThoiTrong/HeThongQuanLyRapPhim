package hcmute.edu.vn.HeThongQuanLyRapPhim.strategy;

import hcmute.edu.vn.HeThongQuanLyRapPhim.config.VNPayConfig;
import hcmute.edu.vn.HeThongQuanLyRapPhim.event.InvoiceGeneratedEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.observer.AppEventManager;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ChairRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.DiscountRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.InvoiceRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.PopcornDrinkComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
@Service
public class VnpayStrategy implements PaymentStrategy{
    private final InvoiceRepository invoiceRepository;
    private final ChairRepository chairRepository;
    private final PopcornDrinkComboRepository popcornDrinkComboRepository;
    private final DiscountRepository discountRepository;
    private final AppEventManager appEventManager;
    private final VNPayConfig vnPayConfig;
    @Autowired
    public VnpayStrategy(InvoiceRepository invoiceRepository,
                         ChairRepository chairRepository,
                         PopcornDrinkComboRepository popcornDrinkComboRepository,
                         DiscountRepository discountRepository, VNPayConfig vnPayConfig,
                         AppEventManager appEventManager) {
        this.invoiceRepository = invoiceRepository;
        this.chairRepository = chairRepository;
        this.popcornDrinkComboRepository = popcornDrinkComboRepository;
        this.appEventManager = appEventManager;
        this.discountRepository = discountRepository;
        this.vnPayConfig = vnPayConfig;
    }
    @Override
    public String createPayment(String amount) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        int amountInt = Integer.parseInt(amount);

        String bankCode = "";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = vnPayConfig.getTmnCode();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInt*100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
//        vnp_Params.put("vnp_IpAddr", clientIp);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.HOUR, 8);  // Thêm 7 giờ để chuyển từ UTC sang giờ Việt Nam

        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append('&');
                hashData.append('&');
            }
        }

        if (!query.isEmpty())
            query.setLength(query.length() - 1);
        if (!hashData.isEmpty())
            hashData.setLength(hashData.length() - 1);

        String vnp_SecureHash = VNPayConfig.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        return vnPayConfig.getVnpayUrl() + "?" + query;
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
}
