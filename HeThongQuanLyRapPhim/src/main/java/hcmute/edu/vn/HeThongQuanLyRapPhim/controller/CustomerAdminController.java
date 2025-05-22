package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoiTuongSuDung;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerAdminController {
    private final CustomerService customerService;

    @Autowired
    public CustomerAdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String showCustomerPage(Model model) {
        List<DoiTuongSuDung> customers = customerService.getAllCustomer();
        model.addAttribute("danhSachKhachHang", customers);
        return "CustomerPage";
    }

    // show form update
    @GetMapping("/update/{id}")
    public String showUpdateForm(Model model, @PathVariable("id") int id) {
        DoiTuongSuDung customer = customerService.getCustomerById(id);
        if (customer != null) {
            model.addAttribute("customer", customer);
        }
        return "EditCustomerPage";
    }

    // Thực hiện update
    @PostMapping("/update/{id}")
    public String updateCustomer(RedirectAttributes redirectAttributes, @ModelAttribute DoiTuongSuDung customer, @PathVariable("id") int id) {
        DoiTuongSuDung cs = customerService.updateCustomer(id, customer);
        if (cs != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin khách hàng thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin khách hàng thất ba");
        }
        return "redirect:/customers/";
    }

    // Thực hiện chức năng xoá
    @PostMapping("/delete/{id}")
    public String deleteCustomer(RedirectAttributes redirectAttributes, @PathVariable("id") int id ) {
        // Thực hiện chức năng xoá
        boolean ketQua = customerService.deleteCutomer(id);
        if (ketQua) {
            redirectAttributes.addFlashAttribute("message", "Xoá khách hàng thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xoá khách hàng không thành công");
        }
        return "redirect:/customers/";
    }

    @GetMapping("/{id}/history-fragment")
    @ResponseBody
    public Object getCustomerHistoryFragment(@PathVariable("id") int customerId,
                                             @RequestParam("type") String type) {
        if ("purchase".equals(type)) {
            return customerService.getHoaDonByKhachHang(customerId);
        } else {
            return customerService.getHoanTraByKhachHang(customerId);
        }
    }
}
