package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import java.io.IOException;
import java.io.OutputStream;

public interface FileExportService {
    void exportToPDF(OutputStream outputStream);
    void exportToExcel(OutputStream outputStream);
}
