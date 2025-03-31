package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum LoaiGhe {
    VIP("Ghế VIP"),
    THUONG("Ghế Thường"),
    DOI("Ghế Đôi");

    private final String moTa;

    LoaiGhe(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}

