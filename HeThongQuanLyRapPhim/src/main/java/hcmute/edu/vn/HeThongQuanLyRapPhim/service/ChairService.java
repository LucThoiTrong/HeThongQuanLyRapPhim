package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;


public interface ChairService {
    Ghe updateChair(int id, Ghe ghe);
    PhongChieuPhim getPhongChieuPhimById(int idPhongChieuPhim);
}
