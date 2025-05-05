package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;

import java.util.List;

public interface ChairService {
    List<Ghe> getAllChair();
    Ghe getChairById(int id);
    Ghe createChair(Ghe ghe);
    Ghe updateChair(int id, Ghe ghe);
    PhongChieuPhim getPhongChieuPhimById(int idPhongChieuPhim);
}
