package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum TrangThaiPhim {
    DANG_CHIEU("Đang chiếu"),
    NGUNG_CHIEU("Ngừng chiếu"),
    SAP_CHIEU("Sắp chiếu");

    private final String moTa;

    TrangThaiPhim(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
