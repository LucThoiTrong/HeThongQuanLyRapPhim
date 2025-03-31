package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "phong_chieu_phim")
public class PhongChieuPhim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong_chieu_phim")
    private int idPhongChieuPhim;

    @Column(name = "ten_phong_chieu_phim")
    private String tenPhongChieuPhim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rap_phim")
    private RapPhim rapPhim;

    @OneToMany(mappedBy = "phongChieuPhim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DayGhe> dsDayGhe;

    @OneToMany(mappedBy = "phongChieuPhim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<SuatChieu> dsSuatChieu;

    public PhongChieuPhim() {}

    public PhongChieuPhim(String tenPhongChieuPhim, RapPhim rapPhim) {
        this.tenPhongChieuPhim = tenPhongChieuPhim;
        this.rapPhim = rapPhim;
        dsDayGhe = new HashSet<>();
        dsSuatChieu = new HashSet<>();
    }

    public int getIdPhongChieuPhim() {
        return idPhongChieuPhim;
    }

    public void setIdPhongChieuPhim(int idPhongChieuPhim) {
        this.idPhongChieuPhim = idPhongChieuPhim;
    }

    public RapPhim getRapPhim() {
        return rapPhim;
    }

    public void setRapPhim(RapPhim rapPhim) {
        this.rapPhim = rapPhim;
    }

    public Set<DayGhe> getDsDayGhe() {
        return dsDayGhe;
    }

    public void setDsDayGhe(Set<DayGhe> dsDayGhe) {
        this.dsDayGhe = dsDayGhe;
    }

    public String getTenPhongChieuPhim() {
        return tenPhongChieuPhim;
    }

    public void setTenPhongChieuPhim(String tenPhongChieuPhim) {
        this.tenPhongChieuPhim = tenPhongChieuPhim;
    }

    public Set<SuatChieu> getDsSuatChieu() {
        return dsSuatChieu;
    }

    public void setDsSuatChieu(Set<SuatChieu> dsSuatChieu) {
        this.dsSuatChieu = dsSuatChieu;
    }
}
