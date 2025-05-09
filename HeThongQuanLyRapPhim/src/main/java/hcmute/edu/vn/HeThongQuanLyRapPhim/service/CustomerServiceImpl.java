package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoaDon;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.HoanTra;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public List<DoiTuongSuDung> getAllCustomer() {
        return customerRepository.findAllByLoaiDoiTuongSuDung(LoaiDoiTuongSuDung.KHACH_HANG);
    }

    @Override
    public DoiTuongSuDung getCustomerById(int id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteCutomer(int id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public DoiTuongSuDung updateCustomer(int id, DoiTuongSuDung newCustomer) {
        DoiTuongSuDung customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setHoTen(newCustomer.getHoTen());
            customer.setSoDienThoai(newCustomer.getSoDienThoai());
            customer.setEmail(newCustomer.getEmail());
            customer.getTkDoiTuongSuDung().setTrangThaiTaiKhoan(newCustomer.getTkDoiTuongSuDung().isTrangThaiTaiKhoan());
            return customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public List<HoaDon> getHoaDonOfCustomer(int id) {
        DoiTuongSuDung doiTuongSuDung = customerRepository.findById(id).orElse(null);
        assert doiTuongSuDung != null;
        return doiTuongSuDung.getDsHoaDon().stream().toList();
    }

    @Override
    public List<HoanTra> getHoanTraOfCustomer(int id) {
        DoiTuongSuDung doiTuongSuDung = customerRepository.findById(id).orElse(null);
        assert doiTuongSuDung != null;
        return doiTuongSuDung.getDsHoanTra().stream().toList();
    }
}
