package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum TrangThaiHoaDon {
    DA_THANH_TOAN("Đã thanh toán"),   // Đã thanh toán
    DA_HOAN_TRA("Đã hoàn trả");        // Đã hoàn trả

    private final String moTa;

    // Constructor cho enum để khởi tạo mô tả
    TrangThaiHoaDon(String moTa) {
        this.moTa = moTa;
    }

    // Phương thức để lấy mô tả của trạng thái
    public String getMoTa() {
        return this.moTa;
    }
}