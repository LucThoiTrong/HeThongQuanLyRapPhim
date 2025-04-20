package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ChienDichMaGiamGiaService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MaGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/discount")
public class MaGiamGiaController {
    private final MaGiamGiaService maGiamGiaService;
    private final ChienDichMaGiamGiaService chienDichMaGiamGiaService;

    @Autowired
    public MaGiamGiaController(MaGiamGiaService maGiamGiaService, ChienDichMaGiamGiaService chienDichMaGiamGiaService) {
        this.maGiamGiaService = maGiamGiaService;
        this.chienDichMaGiamGiaService = chienDichMaGiamGiaService;
    }

    @GetMapping("/list")
    public String getDiscountList(Model model) {
        List<MaGiamGia> maGiamGiaList = maGiamGiaService.findAll();
        model.addAttribute("maGiamGiaList", maGiamGiaList);
        return "DanhSachMaGiamGia";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = chienDichMaGiamGiaService.findAll();
        model.addAttribute("listChienDichGiamGia", chienDichGiamGiaList);
        model.addAttribute("maGiamGia", new MaGiamGia());
        return "ThemMaGiamGia"; // Tên file form tạo mới: resources/templates/discount/create.html
    }

    @PostMapping("/save")
    public String saveMaGiamGia(@ModelAttribute MaGiamGia maGiamGia) {
        maGiamGiaService.save(maGiamGia);
        return "redirect:/discount/list"; // Sau khi lưu thì chuyển về trang danh sách
    }

    @GetMapping("/HienFormDeCapNhat")
    public String showEditForm(@RequestParam("idMaGiamGia") int id, Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = chienDichMaGiamGiaService.findAll();
        model.addAttribute("listChienDichGiamGia", chienDichGiamGiaList);
        MaGiamGia maGiamGia = maGiamGiaService.findById(id);
        model.addAttribute("maGiamGia", maGiamGia);
        return "ThemMaGiamGia"; // Tên file form chỉnh sửa: resources/templates/discount/edit.html
    }

    @GetMapping("/delete")
    public String deleteMaGiamGia(@RequestParam("idMaGiamGia") int id) {
        maGiamGiaService.deleteById(id);
        return "redirect:/discount/list";
    }
}
