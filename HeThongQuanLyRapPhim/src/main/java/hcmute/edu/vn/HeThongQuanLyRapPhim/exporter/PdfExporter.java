package hcmute.edu.vn.HeThongQuanLyRapPhim.exporter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PdfExporter extends BaseFileExporter {
    private final PDType0Font font;

    private final String[] columnKeys = {
            "idPhim",
            "tenPhim",
            "daoDien",
            "doTuoi",
            "trangThaiPhim",
            "ngonNgu"
    };

    private final String[] headers = {
            "ID",
            "Tên Phim",
            "Đạo Diễn",
            "Độ Tuổi",
            "Trạng Thái",
            "Ngôn Ngữ"
    };

    private final float[] columnWidths = {
            30f,  // idPhim
            200f, // tenPhim (có thể rộng hơn nữa)
            100f, // daoDien (rộng hơn)
            50f,  // doTuoi
            80f,  // trangThaiPhim (rộng hơn)
            60f   // ngonNgu
    };

    public PdfExporter() {
        PDDocument document = new PDDocument();
        try (InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/arial.ttf")) {
            font = PDType0Font.load(document, fontStream, false);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải font: " + e.getMessage(), e);
        }
    }

    @Override
    protected List<Map<String, Object>> prepareData(List<Map<String, Object>> data) {
        if (data == null) return List.of();
        return data.stream()
                .filter(map -> map != null && map.get("idPhim") instanceof Integer)
                .sorted(Comparator.comparing(map -> (Integer) map.get("idPhim")))
                .collect(Collectors.toList());
    }

    @Override
    protected Object formatDataToExportableObject(List<Map<String, Object>> preparedData) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        float margin = 40;
        float yStart = page.getMediaBox().getHeight() - margin;
        float yPosition = yStart;
        float leading = 15f;
        float fontSize = 10f;

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, fontSize);

            yPosition = drawHeader(contentStream, margin, yPosition, headers, columnWidths, leading);

            for (Map<String, Object> row : preparedData) {
                if (yPosition < margin) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, fontSize);
                    yPosition = yStart;
                    yPosition = drawHeader(contentStream, margin, yPosition, headers, columnWidths, leading);
                }

                yPosition -= drawRow(contentStream, margin, yPosition, columnKeys, columnWidths, row, font, fontSize, leading);
            }

            contentStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo nội dung PDF: " + e.getMessage(), e);
        }

        return document;
    }

    private float drawHeader(PDPageContentStream stream, float xStart, float y, String[] headers, float[] widths, float leading) throws IOException {
        stream.beginText();
        stream.newLineAtOffset(xStart, y);
        for (int i = 0; i < headers.length; i++) {
            stream.showText(headers[i]);
            if (i < headers.length - 1)
                stream.newLineAtOffset(widths[i] + 5, 0);
        }
        stream.endText();
        return y - (leading * 1.5f);
    }

    private float drawRow(PDPageContentStream stream, float xStart, float y, String[] keys, float[] widths, Map<String, Object> row,
                          PDType0Font font, float fontSize, float leading) throws IOException {
        stream.beginText();
        stream.newLineAtOffset(xStart, y);

        for (int i = 0; i < keys.length; i++) {
            Object val = row.getOrDefault(keys[i], "");
            String text;

            if ("trangThaiPhim".equals(keys[i]) && val != null && val.getClass().isEnum()) {
                try {
                    text = val.getClass().getMethod("getMoTa").invoke(val).toString();
                } catch (Exception e) {
                    text = val.toString();
                }
            } else {
                text = (val != null) ? val.toString() : "";
            }

            // Rút gọn nếu quá dài
            float actualWidth = font.getStringWidth(text) / 1000 * fontSize;
            if (actualWidth > widths[i]) {
                int charsFit = (int) ((widths[i] / actualWidth) * text.length());
                if (charsFit > 3 && charsFit < text.length())
                    text = text.substring(0, charsFit - 3) + "...";
                else if (charsFit <= 3 && text.length() > 3)
                    text = text.substring(0, 3) + "...";
            }

            stream.showText(text);
            if (i < keys.length - 1)
                stream.newLineAtOffset(widths[i] + 5, 0);
        }

        stream.endText();
        return leading;
    }

    @Override
    protected void saveFile(Object exportableObject, OutputStream outputStream) {
        if (!(exportableObject instanceof PDDocument document)) {
            throw new IllegalArgumentException("Đối tượng không phải là PDDocument.");
        }
        try {
            document.save(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu PDF: " + e.getMessage(), e);
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng tài liệu PDF: " + e.getMessage());
            }
        }
        System.out.println("PdfExporter: Đã lưu file PDF.");
    }
}