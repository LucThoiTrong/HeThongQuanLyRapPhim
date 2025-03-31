package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

public enum LoaiDoiTuongSuDung {
    KHACH_HANG("Khách hàng"),
    NHAN_VIEN("Nhân viên"),
    CHU_RAP("Chủ rạp");

    private final String moTa;

    LoaiDoiTuongSuDung(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}

