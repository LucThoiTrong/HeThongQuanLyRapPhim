package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum GioiTinh {
    NAM("Nam"),
    NU("Nữ"),
    KHAC("Khác");

    private final String moTa;

    GioiTinh(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}