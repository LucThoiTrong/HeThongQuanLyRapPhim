package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum KichThuocPhong {
    NHO("Nhỏ"),
    VUA("Vừa"),
    LON("Lớn");

    private final String moTa;

    KichThuocPhong(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
