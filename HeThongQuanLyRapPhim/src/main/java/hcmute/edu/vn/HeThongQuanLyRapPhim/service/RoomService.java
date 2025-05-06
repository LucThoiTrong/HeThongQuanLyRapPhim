package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;

import java.util.List;

public interface RoomService {
    List<PhongChieuPhim> getAllRoomsByCinemaId(int idRapPhim);
    PhongChieuPhim getRoomById(int id);
    PhongChieuPhim createRoom(PhongChieuPhim phongChieuPhim);
    PhongChieuPhim updateRoom(int id, PhongChieuPhim phongChieuPhim);
    boolean deleteRoom(int id);
}
