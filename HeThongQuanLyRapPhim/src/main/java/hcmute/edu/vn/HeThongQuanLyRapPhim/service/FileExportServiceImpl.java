package hcmute.edu.vn.HeThongQuanLyRapPhim.service;

import hcmute.edu.vn.HeThongQuanLyRapPhim.exporter.BaseFileExporter;
import hcmute.edu.vn.HeThongQuanLyRapPhim.exporter.ExcelExporter;
import hcmute.edu.vn.HeThongQuanLyRapPhim.exporter.PdfExporter;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileExportServiceImpl implements FileExportService {
    private final MovieRepository movieRepository;

    @Autowired
    public FileExportServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void exportToPDF(OutputStream outputStream) {
        List<Phim> movies = movieRepository.findAll();
        List<Map<String, Object>> data = convertToMapList(movies);

        BaseFileExporter pdfExporter = new PdfExporter();
        pdfExporter.exportFile(data, outputStream);
    }

    @Override
    public void exportToExcel(OutputStream outputStream) {
        List<Phim> movies = movieRepository.findAll();
        List<Map<String, Object>> data = convertToMapList(movies);

        BaseFileExporter excelExporter = new ExcelExporter();
        excelExporter.exportFile(data, outputStream);
    }

    private List<Map<String, Object>> convertToMapList(List<Phim> phimList) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        if (phimList == null) {
            return dataList;
        }

        for (Phim phim : phimList) {
            Map<String, Object> dataMap = new HashMap<>();

            dataMap.put("idPhim", phim.getIdPhim());
            dataMap.put("tenPhim", phim.getTenPhim());
            dataMap.put("daoDien", phim.getDaoDien());

            dataMap.put("doTuoi", phim.getDoTuoi());
            dataMap.put("trangThaiPhim", phim.getTrangThaiPhim().getMoTa());;
            dataMap.put("ngonNgu", phim.getNgonNgu());

            dataList.add(dataMap);
        }
        return dataList;
    }
}
