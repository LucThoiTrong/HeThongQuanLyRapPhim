package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;

import java.util.List;

public interface EmployeeService {
    DoiTuongSuDung getEmployeeById(int id);
    List<DoiTuongSuDung> getAllEmployee();
    boolean deleteEmployee(int id);
    DoiTuongSuDung updateEmployee(int id, DoiTuongSuDung newEmployee, int rapPhimId);
    DoiTuongSuDung createEmployee(DoiTuongSuDung newEmployee);
}
