package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RoomRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RowChairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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
        // Xoá tất cả dãy ghế thuộc phòng chiếu phim
        phongChieuPhim.getDsDayGhe().clear();

        // Tạo danh sách dãy ghế mới
        List<DayGhe> newDayGheList = new ArrayList<>();
        int index = 1;

        // Thêm dãy ghế Đôi
        for (int i = 0; i < soLuongDoi; i++) {
            DayGhe dayGhe = new DayGhe();
            dayGhe.setTenDayGhe("Dãy " + (char) ('A' + index++ - 1));
            dayGhe.setLoaiGhe(LoaiGhe.DOI);
            dayGhe.setGiaDayGhe(240000);
            dayGhe.setPhongChieuPhim(phongChieuPhim);
            dayGhe.setDsGhe(dayGhe.generateDsGhe(LoaiGhe.DOI));
            newDayGheList.add(dayGhe);
        }

        // Thêm dãy ghế VIP
        for (int i = 0; i < soLuongVip; i++) {
            DayGhe dayGhe = new DayGhe();
            dayGhe.setTenDayGhe("Dãy " + (char) ('A' + index++ - 1));
            dayGhe.setLoaiGhe(LoaiGhe.VIP);
            dayGhe.setGiaDayGhe(120000);
            dayGhe.setPhongChieuPhim(phongChieuPhim);
            dayGhe.setDsGhe(dayGhe.generateDsGhe(LoaiGhe.VIP));
            newDayGheList.add(dayGhe);
        }

        // Thêm dãy ghế Thường
        for (int i = 0; i < soLuongThuong; i++) {
            DayGhe dayGhe = new DayGhe();
            dayGhe.setTenDayGhe("Dãy " + (char) ('A' + index++ - 1));
            dayGhe.setLoaiGhe(LoaiGhe.THUONG);
            dayGhe.setGiaDayGhe(100000);
            dayGhe.setPhongChieuPhim(phongChieuPhim);
            dayGhe.setDsGhe(dayGhe.generateDsGhe(LoaiGhe.THUONG));
            newDayGheList.add(dayGhe);
        }

        // Lưu tất cả dãy ghế mới (cùng với ghế nhờ cascade)
        rowChairRepository.saveAll(newDayGheList);
    }

    @Override
    public PhongChieuPhim getRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }
}