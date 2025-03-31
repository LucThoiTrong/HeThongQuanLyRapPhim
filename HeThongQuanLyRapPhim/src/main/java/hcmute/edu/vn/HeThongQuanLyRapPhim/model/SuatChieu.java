package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "suat_chieu")
public class SuatChieu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_suat_chieu")
    private int idSuatChieu;

    @Column(name = "ngay_gio_chieu")
    private LocalDateTime ngayGioChieu;

    @Column(name = "hinh_thuc_chieu")
    @Enumerated(EnumType.STRING)
    private HinhThucChieu hinhThucChieu;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_phong_chieu_phim")
    private PhongChieuPhim phongChieuPhim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_phim")
    private Phim phim;

    @OneToMany(mappedBy = "suatChieu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<VeXemPhim> dsVeXemPhim;

    public SuatChieu() {
    }

    public SuatChieu(LocalDateTime ngayGioChieu, HinhThucChieu hinhThucChieu, PhongChieuPhim phongChieuPhim, Phim phim) {
        this.ngayGioChieu = ngayGioChieu;
        this.hinhThucChieu = hinhThucChieu;
        this.phongChieuPhim = phongChieuPhim;
        this.phim = phim;
    }

    public int getIdSuatChieu() {
        return idSuatChieu;
    }

    public void setIdSuatChieu(int idSuatChieu) {
        this.idSuatChieu = idSuatChieu;
    }

    public LocalDateTime getNgayGioChieu() {
        return ngayGioChieu;
    }

    public void setNgayGioChieu(LocalDateTime ngayGioChieu) {
        this.ngayGioChieu = ngayGioChieu;
    }

    public HinhThucChieu getHinhThucChieu() {
        return hinhThucChieu;
    }

    public void setHinhThucChieu(HinhThucChieu hinhThucChieu) {
        this.hinhThucChieu = hinhThucChieu;
    }

    public PhongChieuPhim getPhongChieuPhim() {
        return phongChieuPhim;
    }

    public void setPhongChieuPhim(PhongChieuPhim phongChieuPhim) {
        this.phongChieuPhim = phongChieuPhim;
    }

    public Phim getPhim() {
        return phim;
    }

    public void setPhim(Phim phim) {
        this.phim = phim;
    }

    public Set<VeXemPhim> getDsVeXemPhim() {
        return dsVeXemPhim;
    }

    public void setDsVeXemPhim(Set<VeXemPhim> dsVeXemPhim) {
        this.dsVeXemPhim = dsVeXemPhim;
    }
}
