package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.ComboBapNuoc;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.PopcornDrinkComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/combos")
public class PopcornDrinkComboController {
    private final PopcornDrinkComboService popCornDrinkComboService;

    @Autowired
    public PopcornDrinkComboController(PopcornDrinkComboService popCornDrinkComboService) {
        this.popCornDrinkComboService = popCornDrinkComboService;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<ComboBapNuoc> comboBapNuoc = popCornDrinkComboService.findAll();
        model.addAttribute("comboBapNuocs", comboBapNuoc);
        return "ComboPage";
    }

    // Hiển thị form insert
    @GetMapping("/new")
    public String showFormInsert(Model model) {
        //tao model de lien ket du lieu tu form
        ComboBapNuoc comboBapNuoc = new ComboBapNuoc();
        model.addAttribute("comboBapNuoc", comboBapNuoc);
        return "AddPopcornDrinkComboPage";
    }

    // Thực hiện lưu combo
    @PostMapping("/new")
    public String insertCombo(@ModelAttribute("comboBapNuoc") ComboBapNuoc comboBapNuoc, RedirectAttributes redirectAttributes) {
        ComboBapNuoc combo = popCornDrinkComboService.insert(comboBapNuoc);
        if(combo != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm combo thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm combo thất bại");
        }
        return "redirect:/combos/";
    }

    // Hiển thị form cập nhật
    @GetMapping("/update/{id}")
    public String showFormForUpdate(@PathVariable int id, Model model) {
        ComboBapNuoc comboBapNuoc = popCornDrinkComboService.findById(id);
        model.addAttribute("comboBapNuoc", comboBapNuoc);
        return "EditPopcornDrinkComboPage";
    }

    // Thực hiện cập nhật combo
    @PostMapping("/update/{id}")
    public String updateCombo(@ModelAttribute("comboBapNuoc") ComboBapNuoc comboBapNuoc, @PathVariable int id, RedirectAttributes redirectAttributes) {
        ComboBapNuoc cb = popCornDrinkComboService.update(id, comboBapNuoc);
        if(cb != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật combo thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật combo thất bại");
        }
        return "redirect:/combos/";
    }

    // Thực hiện xoá
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        popCornDrinkComboService.deleteById(id);
        return "redirect:/combos/";
    }
}
