package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.InvoiceRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService{
    private final InvoiceRepository invoiceRepository;
    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public HoaDon save(HoaDon hoaDon) {
        return invoiceRepository.save(hoaDon);
    }

    @Override
    public HoaDon findById(int id) {
       return invoiceRepository.findById(id).orElse(null);
    }

}
