package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.SuatChieu;

import java.util.List;

public interface ShowTimeService {
    List<SuatChieu> getAllShowTimes();
    SuatChieu getShowTimeById(int id);
    SuatChieu createShowTime(SuatChieu showTime);
    SuatChieu updateShowTime(int id, SuatChieu showTime);
    boolean deleteShowTimeById(int id);
}
