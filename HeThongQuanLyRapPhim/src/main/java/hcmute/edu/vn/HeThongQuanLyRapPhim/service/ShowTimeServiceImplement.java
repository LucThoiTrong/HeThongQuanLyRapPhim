package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShowTimeServiceImplement implements ShowTimeService {

    private final ShowTimeRepository showTimeRepository;

    @Autowired
    public ShowTimeServiceImplement(ShowTimeRepository showTimeRepository) {
        this.showTimeRepository = showTimeRepository;
    }

    @Override
    public List<SuatChieu> getAllShowTimes() {
        return showTimeRepository.findAll();
    }

    @Override
    public SuatChieu getShowTimeById(int id) {
        Optional<SuatChieu> optional = showTimeRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public SuatChieu createShowTime(SuatChieu newShowTime) {
        if (newShowTime == null || newShowTime.getPhim() == null || newShowTime.getPhongChieuPhim() == null || newShowTime.getNgayGioChieu() == null) {
            throw new IllegalArgumentException("Dữ liệu suất chiếu không đầy đủ.");
        }
        if (isShowTimeConflict(newShowTime)) {
            throw new IllegalArgumentException("Suất chiếu bị trùng thời gian với suất chiếu khác trong cùng phòng.");
        }
        return showTimeRepository.save(newShowTime);
    }

    @Override
    public SuatChieu updateShowTime(int id, SuatChieu newShowTime) {
        if (newShowTime == null || newShowTime.getPhim() == null || newShowTime.getPhim().getThoiLuongChieu() == null ||
                newShowTime.getPhongChieuPhim() == null || newShowTime.getNgayGioChieu() == null) {
            throw new IllegalArgumentException("Dữ liệu suất chiếu hoặc thời lượng chiếu của phim không đầy đủ.");
        }
        Optional<SuatChieu> optional = showTimeRepository.findById(id);
        if (optional.isPresent()) {
            if (isShowTimeConflict(newShowTime, id)) {
                throw new IllegalArgumentException("Suất chiếu bị trùng thời gian với suất chiếu khác trong cùng phòng.");
            }
            SuatChieu suatChieu = optional.get();
            suatChieu.setPhim(newShowTime.getPhim());
            suatChieu.setPhongChieuPhim(newShowTime.getPhongChieuPhim());
            suatChieu.setNgayGioChieu(newShowTime.getNgayGioChieu());
            suatChieu.setHinhThucChieu(newShowTime.getHinhThucChieu());
            return showTimeRepository.save(suatChieu);
        }
        return null;
    }

    @Override
    public boolean deleteShowTimeById(int id) {
        if (showTimeRepository.existsById(id)) {
            showTimeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private boolean isShowTimeConflict(SuatChieu newShowTime) {
        return isShowTimeConflict(newShowTime, -1);
    }

    private boolean isShowTimeConflict(SuatChieu newShowTime, int excludeId) {
        Integer thoiLuongChieu = newShowTime.getPhim() != null ? newShowTime.getPhim().getThoiLuongChieu() : null;
        if (thoiLuongChieu == null) {
            throw new IllegalArgumentException("Thời lượng chiếu của phim không được để trống.");
        }

        LocalDateTime startTime = newShowTime.getNgayGioChieu();
        LocalDateTime endTime = startTime.plusMinutes(thoiLuongChieu);

        List<SuatChieu> existingShowTimes = showTimeRepository.findByIdPhongChieuPhim(
                newShowTime.getPhongChieuPhim().getIdPhongChieuPhim());

        for (SuatChieu existing : existingShowTimes) {
            if (existing.getIdSuatChieu() == excludeId) {
                continue;
            }

            Integer existingThoiLuongChieu = existing.getPhim() != null ? existing.getPhim().getThoiLuongChieu() : null;
            if (existingThoiLuongChieu == null) {
                throw new IllegalArgumentException("Thời lượng chiếu của suất chiếu hiện có (ID: " + existing.getIdSuatChieu() + ") không được để trống.");
            }

            LocalDateTime existingStartTime = existing.getNgayGioChieu();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(existingThoiLuongChieu);

            boolean isOverlapping = !(endTime.isBefore(existingStartTime) || startTime.isAfter(existingEndTime));
            if (isOverlapping) {
                return true;
            }
        }
        return false;
    }
}