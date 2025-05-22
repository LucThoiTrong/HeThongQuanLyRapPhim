package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.CinemaRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CinemaRepository cinemaRepository;
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CinemaRepository cinemaRepository) {
        this.employeeRepository = employeeRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public DoiTuongSuDung getEmployeeById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<DoiTuongSuDung> getAllEmployee() {
        return employeeRepository.findAllByLoaiDoiTuongSuDung(LoaiDoiTuongSuDung.NHAN_VIEN);
    }

    @Override
    public boolean deleteEmployee(int id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public DoiTuongSuDung updateEmployee(int id, DoiTuongSuDung newEmployee, int rapPhimId) {
        DoiTuongSuDung employee = employeeRepository.findById(id).orElse(null);
        RapPhim rapPhim = cinemaRepository.findById(rapPhimId).orElse(null);
        if (employee != null) {
            employee.setHoTen(newEmployee.getHoTen());
            employee.setSoDienThoai(newEmployee.getSoDienThoai());
            employee.setEmail(newEmployee.getEmail());

            if(employee.getRapPhim() != null) {
                employee.getRapPhim().setNhanVien(null);
                cinemaRepository.save(employee.getRapPhim());
            }

            if(rapPhim != null) {
                rapPhim.setNhanVien(employee);
                cinemaRepository.save(rapPhim);
            }

            return employeeRepository.save(employee);
        }
        return null;
    }

    @Override
    public DoiTuongSuDung createEmployee(DoiTuongSuDung newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    @Override
    public List<RapPhim> isCinemaWithoutManager() {
        return cinemaRepository.findAll().stream().filter(x -> x.getNhanVien() == null).toList();
    }
}
