package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "phim")
public class Phim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phim")
    private Integer idPhim;

    @Column(name = "ten_phim")
    private String tenPhim;

    @Column(name = "dao_dien")
    private String daoDien;

    @Column(name = "dien_vien")
    private String dienVien;

    @Column(name = "mo_ta_phim", length = 1000)
    private String moTaPhim;

    @Column(name = "the_loai")
    private String theLoai;

    @Column(name = "thoiGianKhoiChieu")
    @Temporal(TemporalType.DATE) // Chỉ định kiểu dữ liệu DATE cho JPA
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Định dạng ngày từ biểu mẫu
    private Date thoiGianKhoiChieu;

    @Column(name = "thoi_luong_chieu")
    private Integer thoiLuongChieu;

    @Column(name = "link_trailer")
    private String linkTrailer;

    @Column(name = "link_anh")
    private String linkAnh;

    @Column(name = "do_tuoi")
    @Enumerated(EnumType.STRING)
    private DoTuoi doTuoi;

    @Column(name = "trang_thai_phim")
    @Enumerated(EnumType.STRING)
    private TrangThaiPhim trangThaiPhim;

    @Column(name = "hinh_thuc_chieu")
    @Enumerated(EnumType.STRING)
    private HinhThucChieu hinhThucChieu;

    @Column(name = "ngon_ngu")
    private String ngonNgu;

    @OneToMany(mappedBy = "phim", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DanhGia> dsDanhGia;

    @OneToMany(mappedBy = "phim", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<SuatChieu> dsSuatChieu;

    public Phim() {
    }

    public Phim(String tenPhim, String daoDien, String dienVien, String moTaPhim, String theLoai, Date thoiGianKhoiChieu, Integer thoiLuongChieu, String linkTrailer,String linkAnh, DoTuoi doTuoi, TrangThaiPhim trangThaiPhim, HinhThucChieu hinhThucChieu, String ngonNgu) {
        this.tenPhim = tenPhim;
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.moTaPhim = moTaPhim;
        this.theLoai = theLoai;
        this.thoiGianKhoiChieu = thoiGianKhoiChieu;
        this.thoiLuongChieu = thoiLuongChieu;
        this.linkTrailer = linkTrailer;
        this.linkAnh = linkAnh;
        this.doTuoi = doTuoi;
        this.trangThaiPhim = trangThaiPhim;
        this.hinhThucChieu = hinhThucChieu;
        this.ngonNgu = ngonNgu;
        dsDanhGia = new HashSet<>();
        dsSuatChieu = new HashSet<>();
    }

    public Integer getIdPhim() {
        return idPhim;
    }

    public void setIdPhim(Integer idPhim) {
        this.idPhim = idPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public String getDienVien() {
        return dienVien;
    }

    public void setDienVien(String dienVien) {
        this.dienVien = dienVien;
    }

    public String getMoTaPhim() {
        return moTaPhim;
    }

    public void setMoTaPhim(String moTaPhim) {
        this.moTaPhim = moTaPhim;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public Date getThoiGianKhoiChieu() {
        return thoiGianKhoiChieu;
    }

    public void setThoiGianKhoiChieu(Date thoiGianKhoiChieu) {
        this.thoiGianKhoiChieu = thoiGianKhoiChieu;
    }

    public Integer getThoiLuongChieu() {
        return thoiLuongChieu;
    }

    public void setThoiLuongChieu(Integer thoiLuongChieu) {
        this.thoiLuongChieu = thoiLuongChieu;
    }

    public String getLinkTrailer() {
        return linkTrailer;
    }

    public void setLinkTrailer(String linkTrailer) {
        this.linkTrailer = linkTrailer;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public DoTuoi getDoTuoi() {
        return doTuoi;
    }

    public void setDoTuoi(DoTuoi doTuoi) {
        this.doTuoi = doTuoi;
    }

    public TrangThaiPhim getTrangThaiPhim() {
        return trangThaiPhim;
    }

    public void setTrangThaiPhim(TrangThaiPhim trangThaiPhim) {
        this.trangThaiPhim = trangThaiPhim;
    }

    public HinhThucChieu getHinhThucChieu() {
        return hinhThucChieu;
    }

    public void setHinhThucChieu(HinhThucChieu hinhThucChieu) {
        this.hinhThucChieu = hinhThucChieu;
    }

    public String getNgonNgu() {
        return ngonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        this.ngonNgu = ngonNgu;
    }

    public Set<DanhGia> getDsDanhGia() {
        return dsDanhGia;
    }

    public void setDsDanhGia(Set<DanhGia> dsDanhGia) {
        this.dsDanhGia = dsDanhGia;
    }

    public Set<SuatChieu> getDsSuatChieu() {
        return dsSuatChieu;
    }

    public void setDsSuatChieu(Set<SuatChieu> dsSuatChieu) {
        this.dsSuatChieu = dsSuatChieu;
    }
}
