package hcmute.edu.vn.HeThongQuanLyRapPhim.exporter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelExporter extends BaseFileExporter {

    private final String[] columnKeys = {
            "idPhim", "tenPhim", "daoDien",
            "doTuoi", "trangThaiPhim", "ngonNgu"
    };

    private final String[] headers = {
            "ID", "Tên Phim", "Đạo Diễn",
            "Độ Tuổi", "Trạng Thái", "Ngôn Ngữ"
    };

    @Override
    protected List<Map<String, Object>> prepareData(List<Map<String, Object>> data) {
        System.out.println("ExcelExporter: Chuẩn bị dữ liệu...");
        if (data == null) return List.of();
        return data.stream()
                .filter(map -> map != null && map.get("idPhim") instanceof Integer)
                .sorted(Comparator.comparing(map -> (Integer) map.get("idPhim")))
                .collect(Collectors.toList());
    }

    @Override
    protected Object formatDataToExportableObject(List<Map<String, Object>> preparedData) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh Sách Phim");

        // Tạo style header
        CellStyle headerStyle = createHeaderCellStyle(workbook);
        createHeaderRow(sheet, headerStyle);

        // Ghi dữ liệu
        int rowNum = 1;
        for (Map<String, Object> rowData : preparedData) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < columnKeys.length; i++) {
                Cell cell = row.createCell(i);
                Object value = rowData.get(columnKeys[i]);

                if (value instanceof String str) {
                    cell.setCellValue(str);
                } else if (value instanceof Number num) {
                    cell.setCellValue(num.doubleValue());
                } else if (value instanceof Boolean bool) {
                    cell.setCellValue(bool);
                } else if (value != null && isEnumField(columnKeys[i])) {
                    cell.setCellValue(resolveEnumValue(value));
                } else {
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }
        }

        // Tự điều chỉnh kích thước cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    @Override
    protected void saveFile(Object exportableObject, OutputStream outputStream) {
        if (!(exportableObject instanceof Workbook workbook)) {
            throw new IllegalArgumentException("Đối tượng cung cấp cho saveFile không phải là Workbook.");
        }
        try (workbook) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ExcelExporter: Đã lưu file Excel.");
    }

    // ======== HÀM PHỤ TRỢ ========

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        CellStyle style = workbook.createCellStyle();
        style.setFont(headerFont);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createHeaderRow(Sheet sheet, CellStyle style) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private boolean isEnumField(String key) {
        return key.equals("doTuoi") || key.equals("trangThaiPhim") || key.equals("hinhThucChieu");
    }

    private String resolveEnumValue(Object enumValue) {
        try {
            Method method = enumValue.getClass().getMethod("getMoTa");
            return method.invoke(enumValue).toString();
        } catch (NoSuchMethodException e1) {
            try {
                return enumValue.getClass().getMethod("name").invoke(enumValue).toString();
            } catch (Exception e2) {
                return enumValue.toString();
            }
        } catch (Exception e) {
            return enumValue.toString();
        }
    }
}
