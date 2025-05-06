package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RoomRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RowChairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class RowChairServiceImplement implements RowChairService {

    @Autowired
    private RowChairRepository rowChairRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DayGhe> getAllRowChairByIdRoom(int idPhongChieuPhim) {
        return rowChairRepository.findAllRowChairByIdRoom(idPhongChieuPhim);
    }

    @Override
    @Transactional(readOnly = true)
    public DayGhe getRowChairById(int id) {
        return rowChairRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateRowChair(PhongChieuPhim phongChieuPhim, int soLuongDoi, int soLuongVip, int soLuongThuong) {
        // Xóa tất cả dãy ghế cũ của phòng
        rowChairRepository.deleteAll(rowChairRepository.findAllRowChairByIdRoom(phongChieuPhim.getIdPhongChieuPhim()));

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
            dayGhe.setDsGhe(new HashSet<>(generateChairs(dayGhe, LoaiGhe.DOI)));
            newDayGheList.add(dayGhe);
        }

        // Thêm dãy ghế VIP
        for (int i = 0; i < soLuongVip; i++) {
            DayGhe dayGhe = new DayGhe();
            dayGhe.setTenDayGhe("Dãy " + (char) ('A' + index++ - 1));
            dayGhe.setLoaiGhe(LoaiGhe.VIP);
            dayGhe.setGiaDayGhe(120000);
            dayGhe.setPhongChieuPhim(phongChieuPhim);
            dayGhe.setDsGhe(new HashSet<>(generateChairs(dayGhe, LoaiGhe.VIP)));
            newDayGheList.add(dayGhe);
        }

        // Thêm dãy ghế Thường
        for (int i = 0; i < soLuongThuong; i++) {
            DayGhe dayGhe = new DayGhe();
            dayGhe.setTenDayGhe("Dãy " + (char) ('A' + index++ - 1));
            dayGhe.setLoaiGhe(LoaiGhe.THUONG);
            dayGhe.setGiaDayGhe(100000);
            dayGhe.setPhongChieuPhim(phongChieuPhim);
            dayGhe.setDsGhe(new HashSet<>(generateChairs(dayGhe, LoaiGhe.THUONG)));
            newDayGheList.add(dayGhe);
        }

        // Lưu tất cả dãy ghế mới (cùng với ghế nhờ cascade)
        rowChairRepository.saveAll(newDayGheList);
    }

    @Override
    @Transactional(readOnly = true)
    public PhongChieuPhim getRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    private List<Ghe> generateChairs(DayGhe dayGhe, LoaiGhe loaiGhe) {
        List<Ghe> ghes = new ArrayList<>();
        int soLuongGhe = loaiGhe == LoaiGhe.DOI ? 5 : 10; // 5 ghế cho DOI, 10 ghế cho VIP/THUONG

        for (int i = 0; i < soLuongGhe; i++) {
            Ghe ghe = new Ghe();
            ghe.setTrangThaiGhe(true); // Mặc định ghế có sẵn
            ghe.setDayGhe(dayGhe); // Liên kết với dãy ghế
            ghes.add(ghe);
        }

        return ghes;
    }
}