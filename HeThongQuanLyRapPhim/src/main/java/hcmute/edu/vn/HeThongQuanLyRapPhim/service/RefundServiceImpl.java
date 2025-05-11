package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.RefundRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoanTra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundServiceImpl implements RefundService {
    private RefundRepository refundRepository;
    @Autowired
    public RefundServiceImpl(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    @Override
    public HoanTra save(HoanTra hoanTra) {
        return refundRepository.save(hoanTra);
    }
}
