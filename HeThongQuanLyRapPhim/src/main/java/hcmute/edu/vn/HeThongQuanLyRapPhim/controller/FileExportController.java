package hcmute.edu.vn.HeThongQuanLyRapPhim.controller;

import hcmute.edu.vn.HeThongQuanLyRapPhim.service.FileExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/export")
public class FileExportController {
    private final FileExportService fileExportService;

    @Autowired
    public FileExportController(FileExportService fileExportService) {
        this.fileExportService = fileExportService;
    }

    @GetMapping("/pdf")
    public void exportPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";

        // Đặt tên file động với timestamp
        String headerValue = "attachment; filename=danh_sach_phim_" + getCurrentDateTimeString() + ".pdf";
        response.setHeader(headerKey, headerValue);

        // 2. Gọi service để thực hiện việc xuất file
        fileExportService.exportToPDF(response.getOutputStream());
    }

    @GetMapping("/excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";

        // Đặt tên file động với timestamp
        String headerValue = "attachment; filename=danh_sach_phim_" + getCurrentDateTimeString() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        // Gọi service để thực hiện việc xuất file
        fileExportService.exportToExcel(response.getOutputStream());
    }

    private String getCurrentDateTimeString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
