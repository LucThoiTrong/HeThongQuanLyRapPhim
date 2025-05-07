package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.KichThuocPhong;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.RapPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/{id}")
    public String getAll(Model model, @PathVariable("id") int idRapPhim) {
        List<PhongChieuPhim> dsPhongChieuPhim = roomService.getAllRoomsByCinemaId(idRapPhim);
        model.addAttribute("dsPhongChieuPhim", dsPhongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        return "RoomListPage";
    }

    @GetMapping("/new")
    public String newPhongChieuPhim(Model model, @RequestParam("idRapPhim") int idRapPhim) {
        PhongChieuPhim phongChieuPhim = new PhongChieuPhim();
        RapPhim rapPhim = new RapPhim();
        rapPhim.setIdRapPhim(idRapPhim);
        phongChieuPhim.setRapPhim(rapPhim);
        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        model.addAttribute("kichThuocPhongList", Arrays.asList(KichThuocPhong.values()));
        return "AddEditRoom";
    }

    @PostMapping("/new")
    public String insertPhongChieuPhim(@Valid @ModelAttribute("phongChieuPhim") PhongChieuPhim phongChieuPhim,
                                       BindingResult result,
                                       @RequestParam("idRapPhim") int idRapPhim,
                                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("tenPhongChieuPhimError", "Tên phòng chiếu không hợp lệ!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return "redirect:/rooms/new?idRapPhim=" + idRapPhim;
        }
        if (phongChieuPhim.getKichThuocPhong() == null) {
            redirectAttributes.addFlashAttribute("kichThuocPhongError", "Vui lòng chọn kích thước phòng!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return "redirect:/rooms/new?idRapPhim=" + idRapPhim;
        }
        RapPhim rapPhim = new RapPhim();
        rapPhim.setIdRapPhim(idRapPhim);
        phongChieuPhim.setRapPhim(rapPhim);
        PhongChieuPhim existingRoom = roomService.findRoomByNameAndCinemaId(phongChieuPhim.getTenPhongChieuPhim(), idRapPhim);
        if (existingRoom != null) {
            redirectAttributes.addFlashAttribute("tenPhongChieuPhimError", "Tên phòng chiếu đã tồn tại trong rạp này!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return "redirect:/rooms/new?idRapPhim=" + idRapPhim;
        }
        try {
            roomService.createRoom(phongChieuPhim);
            redirectAttributes.addFlashAttribute("message", "Thêm phòng chiếu thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Thêm phòng chiếu thất bại: " + e.getMessage());
        }
        return "redirect:/rooms/" + idRapPhim;
    }

    @GetMapping("/update/{id}")
    public String updatePhongChieuPhim(Model model, @PathVariable("id") int id,
                                       @RequestParam("idRapPhim") int idRapPhim) {
        PhongChieuPhim phongChieuPhim = roomService.getRoomById(id);
        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        model.addAttribute("kichThuocPhongList", Arrays.asList(KichThuocPhong.values()));
        return "AddEditRoom";
    }

    @PostMapping("/update/{id}")
    public String updatePhongChieuPhim(@PathVariable("id") int id,
                                       @Valid @ModelAttribute("phongChieuPhim") PhongChieuPhim phongChieuPhim,
                                       BindingResult result,
                                       @RequestParam("idRapPhim") int idRapPhim,
                                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("tenPhongChieuPhimError", "Tên phòng chiếu không hợp lệ!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return "redirect:/rooms/update/" + id + "?idRapPhim=" + idRapPhim;
        }
        if (phongChieuPhim.getKichThuocPhong() == null) {
            redirectAttributes.addFlashAttribute("kichThuocPhongError", "Vui lòng chọn kích thước phòng!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return "redirect:/rooms/update/" + id + "?idRapPhim=" + idRapPhim;
        }
        RapPhim rapPhim = new RapPhim();
        rapPhim.setIdRapPhim(idRapPhim);
        phongChieuPhim.setRapPhim(rapPhim);
        PhongChieuPhim existingRoom = roomService.findRoomByNameAndCinemaId(phongChieuPhim.getTenPhongChieuPhim(), idRapPhim);
        if (existingRoom != null && existingRoom.getIdPhongChieuPhim() != id) {
            redirectAttributes.addFlashAttribute("tenPhongChieuPhimError", "Tên phòng chiếu đã tồn tại trong rạp này!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return "redirect:/rooms/update/" + id + "?idRapPhim=" + idRapPhim;
        }
        try {
            roomService.updateRoom(id, phongChieuPhim);
            redirectAttributes.addFlashAttribute("message", "Cập nhật phòng chiếu thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phòng chiếu thất bại: " + e.getMessage());
        }
        return "redirect:/rooms/" + idRapPhim;
    }

    @PostMapping("/delete/{id}")
    public String deletePhongChieuPhim(@PathVariable("id") int id,
                                       @RequestParam("idRapPhim") int idRapPhim,
                                       RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("message", "Xóa phòng chiếu thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Xóa phòng chiếu thất bại: " + e.getMessage());
        }
        return "redirect:/rooms/" + idRapPhim;
    }
}