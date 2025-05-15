package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;

import java.util.List;
import java.util.Map;

public interface RevenueService {
    RapPhim getRapPhimById(int id);
    List<RapPhim> getAllRapPhim();
    Map<String, Double> getMonthlyRevenueByYearAndCinema(int year, RapPhim cinema);
    Map<String, Double> getMovieRevenueByMonthYearAndCinema(int year, int month, RapPhim cinema);
    Map<String, Integer> getComboQuantityByMonthYearAndCinema(int month, int year, RapPhim cinema);
}