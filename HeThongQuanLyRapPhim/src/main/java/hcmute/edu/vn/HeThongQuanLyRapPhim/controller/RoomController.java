package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.PhongChieuPhim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{id}")
    public String getAll(Model model, @PathVariable("id") int idRapPhim) {
        List<PhongChieuPhim> dsPhongChieuPhim = roomService.getAllRoomsByCinemaId(idRapPhim);
        model.addAttribute("dsPhongChieuPhim", dsPhongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        return "RoomPage";
    }

    // Thực hiện thêm phòng chiếu
    // Show form thêm phòng chiếu
    @GetMapping("/new")
    public String newPhongChieuPhim(Model model, @RequestParam("idRapPhim") int idRapPhim) {
        // Khởi tạo đối tượng phòng chiếu
        PhongChieuPhim phongChieuPhim = new PhongChieuPhim();

        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        return "AddRoomPage";
    }

    // Thực hiện thêm
    @PostMapping("/new")
    public String insertPhongChieuPhim(@ModelAttribute("phongChieuPhim") PhongChieuPhim phongChieuPhim,
                                       @RequestParam("idRapPhim") int idRapPhim,
                                       RedirectAttributes redirectAttributes) {
        // Kiểm tra tên phong chiếu đã tồn tại trong rạp đó hay chưa
        boolean existingRoom = roomService.checkRoomName(phongChieuPhim.getTenPhongChieuPhim(), idRapPhim);

        if (existingRoom) {
            redirectAttributes.addFlashAttribute("tenPhongChieuPhimError", "Tên phòng chiếu đã tồn tại trong rạp này!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return MessageFormat.format("redirect:/rooms/new?idRapPhim={0}", idRapPhim);
        }

        // Thực hiện lưu phòng chiếu
        phongChieuPhim.setRapPhim(roomService.getCinemaById(idRapPhim));
        PhongChieuPhim result = roomService.createRoom(phongChieuPhim);

        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm phòng chiếu thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Thêm phòng chiếu thất bại");
        }

        return "redirect:/rooms/" + idRapPhim;
    }

    // Thực hiện update phòng chiếu
    // Show form update
    @GetMapping("/update/{id}")
    public String updatePhongChieuPhim(Model model, @PathVariable("id") int id,
                                       @RequestParam("idRapPhim") int idRapPhim) {
        PhongChieuPhim phongChieuPhim = roomService.getRoomById(id);

        model.addAttribute("phongChieuPhim", phongChieuPhim);
        model.addAttribute("idRapPhim", idRapPhim);
        return "EditRoomPage";
    }

    // Thực hiện update
    @PostMapping("/update/{id}")
    public String updatePhongChieuPhim(@PathVariable("id") int idPhongChieuPhim,
                                       @ModelAttribute("phongChieuPhim") PhongChieuPhim phongChieuPhim,
                                       @RequestParam("idRapPhim") int idRapPhim,
                                       RedirectAttributes redirectAttributes) {
        // Kiểm tra tên phong chiếu đã tồn tại trong rạp đó hay chưa
        PhongChieuPhim existingRoom = roomService.getPhongChieuPhimByName(phongChieuPhim.getTenPhongChieuPhim());

        if (existingRoom != null && !existingRoom.getIdPhongChieuPhim().equals(phongChieuPhim.getIdPhongChieuPhim())) {
            redirectAttributes.addFlashAttribute("tenPhongChieuPhimError", "Tên phòng chiếu đã tồn tại trong rạp này!");
            redirectAttributes.addFlashAttribute("phongChieuPhim", phongChieuPhim);
            return MessageFormat.format("redirect:/rooms/update/{0}?idRapPhim={1}", idPhongChieuPhim, idRapPhim);
        }

        PhongChieuPhim result = roomService.updateRoom(idPhongChieuPhim, phongChieuPhim);
        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phòng chiếu thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Cập nhật phòng chiếu thất bại");
        }

        return "redirect:/rooms/" + idRapPhim;
    }

    // Thực hiện delete
    @PostMapping("/delete/{id}")
    public String deletePhongChieuPhim(@PathVariable("id") int idPhongChieuPhim,
                                       @RequestParam("idRapPhim") int idRapPhim,
                                       RedirectAttributes redirectAttributes) {

        boolean result = roomService.deleteRoom(idPhongChieuPhim);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "Xóa phòng chiếu thành công");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xóa phòng chiếu thất bại");
        }

        return "redirect:/rooms/" + idRapPhim;
    }
}