package hcmute.edu.vn.HeThongQuanLyRapPhim.exporter;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public abstract class BaseFileExporter {
    // Ghi dữ liệu
    public final void exportFile(List<Map<String, Object>> data, OutputStream outputStream) {
        List<Map<String, Object>> preparedData = prepareData(data);
        Object exportableObject = formatDataToExportableObject(preparedData);
        saveFile(exportableObject, outputStream);
    }

    // Chuẩn bị dữ liệu
    protected abstract List<Map<String, Object>> prepareData(List<Map<String, Object>> data);

    // Định dạng dữ liệu
    protected abstract Object formatDataToExportableObject(List<Map<String, Object>> preparedData);

    // Lưu đối tượng
    protected abstract void saveFile(Object exportableObject, OutputStream outputStream);
}