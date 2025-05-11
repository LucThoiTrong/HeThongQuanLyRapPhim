package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doi_tuong_su_dung")
public class DoiTuongSuDung implements Serializable {
    @Id
    @Column(name = "id_doi_tuong_su_dung")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDoiTuongSuDung;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "ngay_sinh")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySinh;

    @Column(name = "gioi_tinh")
    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name = "loai_doi_tuong", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoaiDoiTuongSuDung loaiDoiTuongSuDung;

    @OneToMany(mappedBy = "doiTuongSuDung", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<MaGiamGia> dsMaGiamGia;

    @OneToMany(mappedBy = "doiTuongSuDung", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DanhGia> dsDanhGia;

    @OneToMany(mappedBy = "doiTuongSuDung", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PhanHoi> dsPhanHoi;

    @OneToMany(mappedBy = "doiTuongSuDung", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<HoaDon> dsHoaDon;

    @OneToMany(mappedBy = "doiTuongSuDung", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<HoanTra> dsHoanTra;

    @OneToOne(mappedBy = "doiTuongSuDung", cascade = CascadeType.ALL, orphanRemoval = true)
    private TKDoiTuongSuDung tkDoiTuongSuDung;

    @OneToOne(mappedBy = "nhanVien", cascade = CascadeType.ALL, orphanRemoval = true)
    private RapPhim rapPhim;

    public DoiTuongSuDung() {}

    public DoiTuongSuDung(String hoTen, String email, LocalDateTime ngaySinh, Object gioiTinh, LoaiDoiTuongSuDung khachHang, String soDienThoai) {}

    public DoiTuongSuDung(String hoTen, String email, Date ngaySinh, GioiTinh gioiTinh, LoaiDoiTuongSuDung loaiDoiTuongSuDung, String soDienThoai) {
        this.hoTen = hoTen;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.loaiDoiTuongSuDung = loaiDoiTuongSuDung;
        this.soDienThoai = soDienThoai;
        if(loaiDoiTuongSuDung == LoaiDoiTuongSuDung.KHACH_HANG) {
            dsMaGiamGia = new HashSet<>();
            dsDanhGia = new HashSet<>();
            dsPhanHoi = new HashSet<>();
            dsHoaDon = new HashSet<>();
        }
    }

    public int getIdDoiTuongSuDung() {
        return idDoiTuongSuDung;
    }

    public void setIdDoiTuongSuDung(int idDoiTuongSuDung) {
        this.idDoiTuongSuDung = idDoiTuongSuDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public GioiTinh getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(GioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LoaiDoiTuongSuDung getLoaiDoiTuongSuDung() {
        return loaiDoiTuongSuDung;
    }

    public void setLoaiDoiTuongSuDung(LoaiDoiTuongSuDung loaiDoiTuongSuDung) {
        this.loaiDoiTuongSuDung = loaiDoiTuongSuDung;
    }

    public Set<MaGiamGia> getDsMaGiamGia() {
        return dsMaGiamGia;
    }

    public void setDsMaGiamGia(Set<MaGiamGia> dsMaGiamGia) {
        this.dsMaGiamGia = dsMaGiamGia;
    }

    public Set<DanhGia> getDsDanhGia() {
        return dsDanhGia;
    }

    public void setDsDanhGia(Set<DanhGia> dsDanhGia) {
        this.dsDanhGia = dsDanhGia;
    }

    public Set<PhanHoi> getDsPhanHoi() {
        return dsPhanHoi;
    }

    public void setDsPhanHoi(Set<PhanHoi> dsPhanHoi) {
        this.dsPhanHoi = dsPhanHoi;
    }

    public Set<HoaDon> getDsHoaDon() {
        return dsHoaDon;
    }

    public void setDsHoaDon(Set<HoaDon> dsHoaDon) {
        this.dsHoaDon = dsHoaDon;
    }

    public Set<HoanTra> getDsHoanTra() {
        return dsHoanTra;
    }

    public void setDsHoanTra(Set<HoanTra> dsHoanTra) {
        this.dsHoanTra = dsHoanTra;
    }

    public TKDoiTuongSuDung getTkDoiTuongSuDung() {
        return tkDoiTuongSuDung;
    }

    public void setTkDoiTuongSuDung(TKDoiTuongSuDung tkDoiTuongSuDung) {
        this.tkDoiTuongSuDung = tkDoiTuongSuDung;
    }

    public RapPhim getRapPhim() {
        return rapPhim;
    }

    public void setRapPhim(RapPhim rapPhim) {
        this.rapPhim = rapPhim;
    }
}