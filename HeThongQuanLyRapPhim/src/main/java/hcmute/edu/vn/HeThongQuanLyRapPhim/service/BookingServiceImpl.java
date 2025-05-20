package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;
    private final PopcornDrinkComboRepository popcornDrinkComboRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, MovieRepository movieRepository, UserRepository userRepository, PopcornDrinkComboRepository popcornDrinkComboRepository, CinemaRepository cinemaRepository) {
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.popcornDrinkComboRepository = popcornDrinkComboRepository;
        this.cinemaRepository = cinemaRepository;
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
    @Override
    // Phương thức tìm danh sách suất chiếu theo phim, ngày chiếu và hình thức chiếu
    public List<SuatChieu> getSuatChieuByPhimNgayChieuAndHinhThuc(Phim phim, LocalDate ngayChieu, HinhThucChieu hinhThucChieu) {
        return bookingRepository.findByPhimAndNgayChieuAndHinhThucChieu(phim, ngayChieu, hinhThucChieu);
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
}
