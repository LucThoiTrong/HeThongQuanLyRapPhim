package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public SuatChieu createShowTime(SuatChieu showTime) {
        try {
            return showTimeRepository.save(showTime);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public SuatChieu updateShowTime(int id, SuatChieu showTime) {
        SuatChieu existingShowTime = getShowTimeById(id);
        if (existingShowTime == null) {
            return null;
        }
        showTime.setIdSuatChieu(id);
        try {
            return showTimeRepository.save(showTime);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteShowTimeById(int id) {
        if (showTimeRepository.existsById(id)) {
            try {
                showTimeRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}