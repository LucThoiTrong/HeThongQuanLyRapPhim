package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.ComboBapNuocService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/combo")
public class ComboBapNuocController {

    private ComboBapNuocService comboBapNuocService;
    public ComboBapNuocController(ComboBapNuocService comboBapNuocService) {
        this.comboBapNuocService = comboBapNuocService;
    }
    @GetMapping("/list")
    public String list(Model model) {
        List<ComboBapNuoc> comboBapNuoc = comboBapNuocService.findAll();
        model.addAttribute("comboBapNuocs", comboBapNuoc);
        return "DanhSachCombo";
    }

    @GetMapping("/themComboBapNuoc")
    public String themComboBapNuoc(Model model) {
        //tao model de lien ket du lieu tu form
        ComboBapNuoc comboBapNuoc = new ComboBapNuoc();
        model.addAttribute("comboBapNuoc", comboBapNuoc);
        return "ThemComboBapNuoc";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("comboBapNuoc") ComboBapNuoc comboBapNuoc) {
        comboBapNuocService.save(comboBapNuoc);
        return "redirect:/combo/list";
    }
    @GetMapping("/HienFormDeCapNhat")
    public String showFormForUpdate(@RequestParam("idComboBapNuoc") int theId, Model model) {
        //@RequestParam("idComboBapNuoc") int theId - lay du lieu tu tham so idComboBapNuoc gan vao theId
        ComboBapNuoc comboBapNuoc = comboBapNuocService.findById(theId);
        model.addAttribute("comboBapNuoc", comboBapNuoc);
        return "ThemComboBapNuoc";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam("idComboBapNuoc") int theId) {
        comboBapNuocService.deleteById(theId);
        return "redirect:/combo/list";
    }
}
