package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CinemaRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.PopcornDrinkComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RevenueServiceImpl implements RevenueService {
    private final CinemaRepository cinemaRepository;
    private final PopcornDrinkComboRepository popcornDrinkComboRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public RevenueServiceImpl(CinemaRepository cinemaRepository, PopcornDrinkComboRepository popcornDrinkComboRepository, MovieRepository movieRepository) {
        this.cinemaRepository = cinemaRepository;
        this.popcornDrinkComboRepository = popcornDrinkComboRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public RapPhim getRapPhimById(int id) {
        return cinemaRepository.findById(id).orElse(null);
    }

    @Override
    public List<RapPhim> getAllRapPhim() {
        return cinemaRepository.findAll();
    }

    @Override
    public Map<String, Double> getMonthlyRevenueByYearAndCinema(int year, RapPhim rapPhim) {
        Map<String, Double> tongDoanhThu = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            double tongTien = rapPhim.tongDoanhThuTungThang(i, year);
            tongDoanhThu.put("Tháng " + i, tongTien);
        }

        double maxDoanhThuThang = 0.0;
        if (!tongDoanhThu.isEmpty()) {
            for (Double doanhThu : tongDoanhThu.values()) {
                if (doanhThu != null && doanhThu > maxDoanhThuThang) {
                    maxDoanhThuThang = doanhThu;
                }
            }
        }

        return tongDoanhThu;
    }

    @Override
    public Map<String, Double> getMovieRevenueByMonthYearAndCinema(int year, int month, RapPhim cinema) {
        Map<String, Double> tongDoanhThuTungPhim = new HashMap<>();
        List<Phim> danhSachPhim = movieRepository.getAllByTrangThaiPhimIsNot(TrangThaiPhim.SAP_CHIEU);
        for (Phim phim : danhSachPhim) {
            double tongTien = cinema.tongDoanhThuTungPhim(month, year, phim);
            tongDoanhThuTungPhim.put(phim.getTenPhim(), tongTien);
        }
        return tongDoanhThuTungPhim;
    }

    @Override
    public Map<String, Integer> getComboQuantityByMonthYearAndCinema(int month, int year, RapPhim cinema) {
        Map<String, Integer> soLuongMuaTungCombo = new HashMap<>();
        List<ComboBapNuoc> danhSachCombo = popcornDrinkComboRepository.findAll();
        for (ComboBapNuoc comboBapNuoc : danhSachCombo) {
            int soLuongBanRa = cinema.tongSoLuongTungComboBanRa(year, month, comboBapNuoc);
            soLuongMuaTungCombo.put(comboBapNuoc.getTenCombo(), soLuongBanRa);
        }
        return soLuongMuaTungCombo;
    }
}

