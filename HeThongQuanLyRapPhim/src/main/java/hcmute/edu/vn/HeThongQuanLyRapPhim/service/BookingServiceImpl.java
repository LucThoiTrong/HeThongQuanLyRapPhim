package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final RowSeatRepository rowSeatRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;
    private final PopcornDrinkComboRepository popcornDrinkComboRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, MovieRepository movieRepository, UserRepository userRepository, PopcornDrinkComboRepository popcornDrinkComboRepository, CinemaRepository cinemaRepository, RowSeatRepository rowSeatRepository) {
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.popcornDrinkComboRepository = popcornDrinkComboRepository;
        this.cinemaRepository = cinemaRepository;
        this.rowSeatRepository = rowSeatRepository;
    }

    @Override
    public Phim findPhimById(int id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public List<ComboBapNuoc> findAllComboBapNuoc() {
        return popcornDrinkComboRepository.findAll();
    }

    @Override
    public SuatChieu findById(int id) {
        Optional<SuatChieu> result = bookingRepository.findById(id);
        SuatChieu suatChieu = null;
        suatChieu = result.get();
        return suatChieu;
    }

    // Thực hiện lấy danh sách suất chiếu theo từng rạp
    @Override
    public Map<String, List<SuatChieu>> getShowtimesForCinema(LocalDate date, HinhThucChieu hinhThucChieu) {
        Map<String, List<SuatChieu>> result = new HashMap<>();
        // Lấy toàn bộ rạp phim
        List<RapPhim> cinemaList = cinemaRepository.findAll();
        // Sau đó tìm từng danh sách suất chiếu của rạp đó
        for (RapPhim cinema : cinemaList) {
            List<SuatChieu> dsSuatChieu = cinema.layDanhSachSuatChieu(date, hinhThucChieu);
            if(!dsSuatChieu.isEmpty()) {
                result.put(cinema.getTenRapPhim(), dsSuatChieu);
            }
        }
        // Nếu danh sách trả về có size != 0 thì lưu rạp đó và list suất chiếu
        return result;
    }

    @Override
    public List<DayGhe> findAllDayGhe(PhongChieuPhim phongChieuPhim) {
        return rowSeatRepository.findByPhongChieuPhim(phongChieuPhim);
    }

    @Override
    public Map<Integer, Integer> extractComboSoLuong(Map<String, String> tatCaThamSo) {
        Map<Integer, Integer> comboSoLuong = new HashMap<>();
        for (Map.Entry<String, String> entry : tatCaThamSo.entrySet()) {
            String key = entry.getKey();
            if (key.matches("\\d+")) {
                try {
                    int comboId = Integer.parseInt(key);
                    int soLuong = Integer.parseInt(entry.getValue());
                    if (soLuong > 0) {
                        comboSoLuong.put(comboId, soLuong);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng số cho combo ID: " + key);
                }
            }
        }
        return comboSoLuong;
    }

    @Override
    public boolean checkMaGiamGia(double tongSoTien, MaGiamGia maGiamGia) {
        LocalDateTime now = LocalDateTime.now();
        // Trả về true
        // Mã giảm giá đã đến ngày áp dụng
        // Và tổng số tiền đủ điều kiện để dùng mã
        return !maGiamGia.getNgayBatDauApDung().isAfter(now) && !(tongSoTien < maGiamGia.getHanMucApDung());
    }

    @Override
    public double tinhTienGiam(double tongHoaDon, MaGiamGia maGiamGia) {
        double tienDuocGiam = tongHoaDon * maGiamGia.getPhanTramGiamGia() / 100;
        return Math.min(tienDuocGiam, maGiamGia.getGiaTriGiamToiDa());
    }

    @Override
    public DoiTuongSuDung findDoiTuongSuDungById(int id) {
        return userRepository.findById(id).orElse(null);
    }
}
