package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rap_phim")
public class RapPhim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rap_phim")
    private int idRapPhim;

    @Column(name = "ten_rap_phim", nullable = false)
    private String tenRapPhim;

    @Column(name = "dia_chi_rap_phim")
    private String diaChiRapPhim;

    @Column(name = "trang_thai_rap_phim", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrangThaiRapPhim trangThaiRapPhim;

    @OneToOne
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung nhanVien;

    @OneToMany(mappedBy = "rapPhim",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PhongChieuPhim> dsPhongChieuPhim;

    public RapPhim() {}

    public RapPhim(String tenRapPhim, String diaChiRapPhim, TrangThaiRapPhim trangThaiRapPhim) {
        this.tenRapPhim = tenRapPhim;
        this.trangThaiRapPhim = trangThaiRapPhim;
        this.diaChiRapPhim = diaChiRapPhim;
        this.nhanVien = null;
        dsPhongChieuPhim = new HashSet<>();
    }

    public int getIdRapPhim() {
        return idRapPhim;
    }

    public void setIdRapPhim(int idRapPhim) {
        this.idRapPhim = idRapPhim;
    }

    public String getTenRapPhim() {
        return tenRapPhim;
    }

    public void setTenRapPhim(String tenRapPhim) {
        this.tenRapPhim = tenRapPhim;
    }

    public TrangThaiRapPhim getTrangThaiRapPhim() {
        return trangThaiRapPhim;
    }

    public void setTrangThaiRapPhim(TrangThaiRapPhim trangThaiRapPhim) {
        this.trangThaiRapPhim = trangThaiRapPhim;
    }

    public DoiTuongSuDung getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(DoiTuongSuDung nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Set<PhongChieuPhim> getDsPhongChieuPhim() {
        return dsPhongChieuPhim;
    }

    public void setDsPhongChieuPhim(Set<PhongChieuPhim> dsPhongChieuPhim) {
        this.dsPhongChieuPhim = dsPhongChieuPhim;
    }

    public String getDiaChiRapPhim() {
        return diaChiRapPhim;
    }

    public void setDiaChiRapPhim(String diaChiRapPhim) {
        this.diaChiRapPhim = diaChiRapPhim;
    }
}
