package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum HinhThucChieu {
    _2DPhuDeViet("2D Phụ Đề Việt"),
    _2DLongTiengViet("2D Lồng Tiếng Việt"),
    _4DX2DPhuDeAnh("4DX2D Phụ Đề Anh"),
    _4DX2DPhuDeViet("4DX2D Phụ Đề Việt");

    private final String moTa;

    HinhThucChieu(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}

