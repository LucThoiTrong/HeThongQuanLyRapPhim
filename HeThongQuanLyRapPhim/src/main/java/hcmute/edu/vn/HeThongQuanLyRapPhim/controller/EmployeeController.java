package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.LoaiDoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.AuthService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CinemaService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.EmployeeService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AuthService authService;
    private final CinemaService cinemaService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, AuthService authService, CinemaService cinemaService) {
        this.employeeService = employeeService;
        this.authService = authService;
        this.cinemaService = cinemaService;
    }

    @GetMapping("/")
    public String getAllEmployees(Model model) {
        List<DoiTuongSuDung> dsNhanVien = employeeService.getAllEmployee();
        model.addAttribute("danhSachNhanVien", dsNhanVien);
        return "EmployeePage";
    }
    // Show Form Insert
    @GetMapping("/new/")
    public String showFormInsert(Model model) {
        model.addAttribute("nhanVien", new DoiTuongSuDung());
        return "AddEmployee";
    }

    // Thực hiện Insert
    @PostMapping("/new/")
    public String insertEmployee(@ModelAttribute DoiTuongSuDung nhanVien, RedirectAttributes redirectAttributes) {
        nhanVien.setLoaiDoiTuongSuDung(LoaiDoiTuongSuDung.NHAN_VIEN);
        DoiTuongSuDung nv = employeeService.createEmployee(nhanVien);
        if(nv != null) {
            String tenDangNhap = StringUtils.normalizeString(nhanVien.getHoTen());
            try {
                authService.register(nv, tenDangNhap, "password123", "password123");
                redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thành công");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thất bại");
        }
        return "redirect:/employees/";
    }
    // Show Form Update
    @GetMapping("/update/{id}")
    public String showUpdateForm(Model model,@PathVariable int id, RedirectAttributes redirectAttributes) {
        DoiTuongSuDung nhanVien = employeeService.getEmployeeById(id);
        if(nhanVien != null) {
            model.addAttribute("nhanVien", nhanVien);
            List<RapPhim> dsRapPhim = new ArrayList<>(cinemaService.isCinemaWithoutManager());
            if (nhanVien.getRapPhim() != null) {
                dsRapPhim.add(nhanVien.getRapPhim()); // Không còn lỗi nữa
            }

            model.addAttribute("dsCinema", dsRapPhim);
            return "EditEmployee";
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy nhân viên");
            return "redirect:/employees/";
        }
    }

    // Thực hiện update
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable int id, @ModelAttribute DoiTuongSuDung nhanVien, @RequestParam("rapPhim") Integer rapPhimId, RedirectAttributes redirectAttributes) {
        DoiTuongSuDung nv = employeeService.updateEmployee(id, nhanVien, rapPhimId);
        if(nv != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thất bại");
        }
        return "redirect:/employees/";
    }

    // Thực hiện delete
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id, RedirectAttributes redirectAttributes) {
        boolean ketQua = employeeService.deleteEmployee(id);
        if (ketQua) {
            redirectAttributes.addFlashAttribute("message", "Xoá nhân viên thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xoá nhân viên thất bại");
        }
        return "redirect:/employees/";
    }
}
