package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.InvoiceRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService{
    private InvoiceRepository invoiceRepository;
    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public HoaDon save(HoaDon hoaDon) {
        return invoiceRepository.save(hoaDon);
    }
    @Override
    public List<HoaDon> findByIdDoiTuongSuDung(int idDoiTuongSuDung) {
        return invoiceRepository.findByDoiTuongSuDungIdDoiTuongSuDung(idDoiTuongSuDung);
    }

    @Override
    public HoaDon findById(int id) {
        Optional<HoaDon> result = invoiceRepository.findById(id);
        HoaDon hoaDon = null;
        hoaDon = result.get();
        return hoaDon;
    }

}
