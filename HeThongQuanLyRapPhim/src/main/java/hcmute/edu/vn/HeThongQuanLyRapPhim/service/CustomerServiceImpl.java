package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.*;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(UserRepository customerRepository) {
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
    public Set<HoaDon> getHoaDonByKhachHang(int id) {
        DoiTuongSuDung customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            return customer.getDsHoaDon().stream()
                    .filter(hd -> hd.getTrangThaiHoaDon().equals(TrangThaiHoaDon.DA_THANH_TOAN))
                    .collect(Collectors.toSet());
        }
        return null;
    }

    @Override
    public Set<HoanTra> getHoanTraByKhachHang(int id) {
        DoiTuongSuDung customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            return customer.getDsHoanTra();
        }
        return null;
    }
}
