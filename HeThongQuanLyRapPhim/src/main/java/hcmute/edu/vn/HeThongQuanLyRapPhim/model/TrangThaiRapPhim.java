package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum TrangThaiRapPhim {
    DANG_HOAT_DONG("Đang hoạt động"),
    NGUNG_HOAT_DONG("Ngừng hoạt động"),
    BAO_TRI("Bảo trì");

    private final String moTa;

    TrangThaiRapPhim(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}