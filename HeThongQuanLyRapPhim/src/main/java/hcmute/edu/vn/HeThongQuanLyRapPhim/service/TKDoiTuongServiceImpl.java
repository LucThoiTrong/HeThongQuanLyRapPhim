package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TKDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.TKDoiTuongSuDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TKDoiTuongServiceImpl implements TKDoiTuongService {
    private final TKDoiTuongSuDungRepository tkDoiTuongSuDungRepository;

    @Autowired
    public TKDoiTuongServiceImpl(TKDoiTuongSuDungRepository tkDoiTuongSuDungRepository) {
        this.tkDoiTuongSuDungRepository = tkDoiTuongSuDungRepository;
    }

    @Override
    public TKDoiTuongSuDung updatePassword(TKDoiTuongSuDung tkDoiTuongSuDung) {
        return tkDoiTuongSuDungRepository.save(tkDoiTuongSuDung);
    }
}
