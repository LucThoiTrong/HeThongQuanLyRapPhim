package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiHoaDon;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.InvoiceRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RefundRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoanTra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final InvoiceRepository invoiceRepository;
    @Autowired
    public RefundServiceImpl(RefundRepository refundRepository, InvoiceRepository invoiceRepository) {
        this.refundRepository = refundRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public HoaDon getHoaDon(int id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    @Override
    public HoanTra save(HoaDon hoaDon, DoiTuongSuDung customer, String lyDoHoanTra) {
        HoanTra hoanTra = new HoanTra();
        hoanTra.setDoiTuongSuDung(customer);
        hoanTra.setHoaDon(hoaDon);
        hoanTra.setNgayHoanTra(LocalDateTime.now());
        hoanTra.setLyDoHoanTra(lyDoHoanTra);

        hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_HOAN_TRA);
        return refundRepository.save(hoanTra);
    }

    @Override
    public String checkHoaDon(HoaDon hoaDon) {
        LocalDateTime ngayGioChieu = hoaDon.getSuatChieu().getNgayGioChieu();
        LocalDateTime now = LocalDateTime.now();
        // phim da bat dau chieu
        if (now.isAfter(ngayGioChieu)) {
            return "Vé không thể hoàn trả do phim đã bắt đầu chiếu.";
        }
        // con it hon 45p
        Duration duration = Duration.between(now, ngayGioChieu);
        if (duration.toMinutes() < 45) {
            return  "Vé không thể hoàn trả do đã quá thời gian cho phép.";
        }
        return "";
    }
}
