package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RowSeatRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DayGhe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Ghe;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RowSeatServiceImpl implements RowSeatService {

    private RowSeatRepository gheRepository;
    @Autowired
    public RowSeatServiceImpl(RowSeatRepository gheRepository) {
        this.gheRepository = gheRepository;
    }
    @Override
    public List<DayGhe> findByPhongChieuPhim(PhongChieuPhim phongChieuPhim) {
        List <DayGhe> dayGheList = gheRepository.findByPhongChieuPhim(phongChieuPhim);
        for (DayGhe dayGhe : dayGheList) {
            List<Ghe> sortedList = new ArrayList<>(dayGhe.getDsGhe());
            sortedList.sort(Comparator.comparingInt(Ghe::getIdGhe));
            Set<Ghe> sortedSet = new LinkedHashSet<>(sortedList);
            dayGhe.setDsGhe(sortedSet);
        }
        return dayGheList;
    }
}
