package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RoomRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RowChairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RowChairServiceImplement implements RowChairService {
    private final RowChairRepository rowChairRepository;

    private final RoomRepository roomRepository;

    @Autowired
    public RowChairServiceImplement(RowChairRepository rowChairRepository, RoomRepository roomRepository) {
        this.rowChairRepository = rowChairRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<DayGhe> getAllRowChairByIdRoom(int idPhongChieuPhim) {
        return rowChairRepository.findAllRowChairByIdRoom(idPhongChieuPhim);
    }


    @Override
    public void updateRowChair(PhongChieuPhim phongChieuPhim, int soLuongDoi, int soLuongVip, int soLuongThuong) {
        phongChieuPhim.getDsDayGhe().clear();
        List<DayGhe> newDayGheList = new ArrayList<>();
        int index = 1;

        index = phongChieuPhim.taoDayGhe(phongChieuPhim, newDayGheList, soLuongThuong, LoaiGhe.THUONG, 100000, index);
        index = phongChieuPhim.taoDayGhe(phongChieuPhim, newDayGheList, soLuongVip, LoaiGhe.VIP, 120000, index);
        phongChieuPhim.taoDayGhe(phongChieuPhim, newDayGheList, soLuongDoi, LoaiGhe.DOI, 240000, index);

        rowChairRepository.saveAll(newDayGheList);
    }


    @Override
    public PhongChieuPhim getRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }
}