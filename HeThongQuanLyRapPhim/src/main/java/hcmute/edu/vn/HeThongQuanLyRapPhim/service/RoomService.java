package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;

import java.util.List;

public interface RoomService {
    List<PhongChieuPhim> getAllRoomsByCinemaId(int idRapPhim);
    boolean checkRoomName(String tenPhongChieuPhim, int idRapPhim);
    RapPhim getCinemaById(int idRapPhim);
    PhongChieuPhim createRoom(PhongChieuPhim phongChieuPhim);
    PhongChieuPhim getRoomById(int id);
    PhongChieuPhim updateRoom(int id, PhongChieuPhim phongChieuPhim);
    boolean deleteRoom(int id);
    PhongChieuPhim getPhongChieuPhimByName(String name);
}
