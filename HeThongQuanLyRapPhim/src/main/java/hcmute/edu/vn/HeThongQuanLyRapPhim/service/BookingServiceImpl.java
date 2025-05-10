package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.BookingRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HinhThucChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    @Override
    public SuatChieu findById(int id) {
        Optional<SuatChieu> result = bookingRepository.findById(id);
        SuatChieu suatChieu = null;
        suatChieu = result.get();
        return suatChieu;
    }
    @Override
    // Phương thức tìm danh sách suất chiếu theo phim, ngày chiếu và hình thức chiếu
    public List<SuatChieu> getSuatChieuByPhimNgayChieuAndHinhThuc(Phim phim, LocalDate ngayChieu, HinhThucChieu hinhThucChieu) {
        return bookingRepository.findByPhimAndNgayChieuAndHinhThucChieu(phim, ngayChieu, hinhThucChieu);
    }
}
