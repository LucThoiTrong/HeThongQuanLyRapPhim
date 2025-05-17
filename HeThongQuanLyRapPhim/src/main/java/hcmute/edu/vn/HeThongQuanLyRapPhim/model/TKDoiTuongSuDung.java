package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tk_doi_tuong_su_dung")
public class TKDoiTuongSuDung implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tk_doi_tuong_su_dung")
    private int idTKDoiTuongSuDung;

    @Column(name = "ten_dang_nhap", nullable = false)
    private String tenDangNhap;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "trang_thai_tai_khoan", nullable = false)
    private boolean trangThaiTaiKhoan;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung doiTuongSuDung;

    public TKDoiTuongSuDung(String tenDangNhap, String matKhau, boolean trangThaiTaiKhoan, DoiTuongSuDung doiTuongSuDung) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.trangThaiTaiKhoan = trangThaiTaiKhoan;
        this.doiTuongSuDung = doiTuongSuDung;
    }

    public TKDoiTuongSuDung() {

    }

    public int getIdTKDoiTuongSuDung() {
        return idTKDoiTuongSuDung;
    }

    public void setIdTKDoiTuongSuDung(int idTKDoiTuongSuDung) {
        this.idTKDoiTuongSuDung = idTKDoiTuongSuDung;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public boolean isTrangThaiTaiKhoan() {
        return trangThaiTaiKhoan;
    }

    public void setTrangThaiTaiKhoan(boolean trangThaiTaiKhoan) {
        this.trangThaiTaiKhoan = trangThaiTaiKhoan;
    }

    public DoiTuongSuDung getDoiTuongSuDung() {
        return doiTuongSuDung;
    }

    public void setDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung) {
        this.doiTuongSuDung = doiTuongSuDung;
    }

}
