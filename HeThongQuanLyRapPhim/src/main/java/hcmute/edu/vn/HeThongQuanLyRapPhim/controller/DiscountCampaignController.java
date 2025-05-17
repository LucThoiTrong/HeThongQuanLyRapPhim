package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ChienDichGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.MaGiamGia;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.DiscountCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/discount-campaign")
public class DiscountCampaignController {
    private final DiscountCampaignService chienDichGiamGiaService;

    @Autowired
    public DiscountCampaignController(DiscountCampaignService discountCampaignService) {
        this.chienDichGiamGiaService = discountCampaignService;
    }

    // Lấy danh sách chiến dịch giảm giá
    @GetMapping("/")
    public String list(Model model) {
        List<ChienDichGiamGia> chienDichGiamGiaList = chienDichGiamGiaService.findAll();
        model.addAttribute("chienDichGiamGiaList", chienDichGiamGiaList);
        return "DiscountCampaignPage";
    }

    // Thêm chiến dịch giảm giá
    @GetMapping("/themChienDich")
    public String themChienDich(Model model) {
        //tao model de lien ket du lieu tu form
        ChienDichGiamGia chienDichGiamGia = new ChienDichGiamGia();
        model.addAttribute("chienDichGiamGia", chienDichGiamGia);
        return "AddDiscountCampaignPage";
    }


    @GetMapping("/themDanhSachMaGiamGia")
    public String themDanhSachMaGiamGia(@ModelAttribute("chienDichGiamGia") ChienDichGiamGia chienDichGiamGia,
                                        Model model)
    {
        model.addAttribute("chienDichGiamGia", chienDichGiamGia);
        System.out.println(chienDichGiamGia.getTenChienDich());
        System.out.println(chienDichGiamGia.getNgayBatDauChienDich());
        MaGiamGia maGiamGia = new MaGiamGia();
        model.addAttribute("maGiamGia", maGiamGia);
        model.addAttribute("soLuongMaGiamGia", 0);
        return "AddDiscountWithCampaignPage";
    }

    @PostMapping("/save")
    public String save(@RequestParam("tenChienDich") String tenChienDich,
                       @RequestParam("ngayBatDauChienDich") LocalDateTime ngayBatDau,
                       @RequestParam("ngayKetThucChienDich") LocalDateTime ngayKetThuc,
                       @ModelAttribute("maGiamGia") MaGiamGia maGiamGia,
                       @RequestParam("soLuongMaGiamGia") int soLuongMaGiamGia) {
        //add danh sach ma giam gia vao chien dich
        ChienDichGiamGia chienDichGiamGia=new ChienDichGiamGia();
        chienDichGiamGia.setTenChienDich(tenChienDich);
        chienDichGiamGia.setNgayBatDauChienDich(ngayBatDau);
        chienDichGiamGia.setNgayKetThucChienDich(ngayKetThuc);
        // Tạo danh sách mã giảm giá de add vao ds mgg trong chien dich
        for (int i = 0; i < soLuongMaGiamGia; i++) {
            MaGiamGia newMaGiamGia = new MaGiamGia();
            newMaGiamGia.setTenMaGiamGia(maGiamGia.getTenMaGiamGia());
            newMaGiamGia.setPhanTramGiamGia(maGiamGia.getPhanTramGiamGia());
            newMaGiamGia.setHanMucApDung(maGiamGia.getHanMucApDung());
            newMaGiamGia.setGiaTriGiamToiDa(maGiamGia.getGiaTriGiamToiDa());
            newMaGiamGia.setNgayBatDauApDung(maGiamGia.getNgayBatDauApDung());
            newMaGiamGia.setNgayKetThucApDung(maGiamGia.getNgayKetThucApDung());
            newMaGiamGia.setTrangThaiSuDung(maGiamGia.isTrangThaiSuDung());

            chienDichGiamGia.addMaGiamGia(newMaGiamGia);
        }
        chienDichGiamGiaService.save(chienDichGiamGia);
        return "redirect:/discount-campaign/";
    }


    @GetMapping("/capNhat")
    public String save(@RequestParam("idChienDichGiamGia") int id,
            @RequestParam("tenChienDich") String tenChienDich,
                       @RequestParam("ngayBatDauChienDich") LocalDateTime ngayBatDau,
                       @RequestParam("ngayKetThucChienDich") LocalDateTime ngayKetThuc) {
        ChienDichGiamGia chienDichGiamGia;
        chienDichGiamGia = chienDichGiamGiaService.findById(id);
        // Gán giá trị mới
        chienDichGiamGia.setTenChienDich(tenChienDich);
        chienDichGiamGia.setNgayBatDauChienDich(ngayBatDau);
        chienDichGiamGia.setNgayKetThucChienDich(ngayKetThuc);
        chienDichGiamGiaService.save(chienDichGiamGia);
        return "redirect:/discount-campaign/";
    }

    @GetMapping("/HienFormDeCapNhat")
    public String showFormForUpdate(@RequestParam("idChienDichGiamGia") int theId, Model model) {
        ChienDichGiamGia chienDichGiamGia = chienDichGiamGiaService.findById(theId);
        model.addAttribute("chienDichGiamGia", chienDichGiamGia);
        System.out.println(chienDichGiamGia.getNgayBatDauChienDich());
        System.out.println(chienDichGiamGia.getNgayKetThucChienDich());
        return "EditDiscountCampaignPage";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("idChienDichGiamGia") int theId) {
        chienDichGiamGiaService.deleteById(theId);
        return "redirect:/discount-campaign/";
    }
}
