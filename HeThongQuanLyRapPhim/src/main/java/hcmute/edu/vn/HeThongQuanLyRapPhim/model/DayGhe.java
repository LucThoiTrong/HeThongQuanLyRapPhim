package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "day_ghe")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idDayGhe")
public class DayGhe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_day_ghe")
    private int idDayGhe;

    @Column(name = "ten_day_ghe")
    private String tenDayGhe;

    @Column(name = "loai_ghe")
    @Enumerated(EnumType.STRING)
    private LoaiGhe loaiGhe;

    @Column(name = "gia_day_ghe")
    private int giaDayGhe;

    @ManyToOne
    @JoinColumn(name = "id_phong_chieu_phim")
    private PhongChieuPhim phongChieuPhim;

    @OneToMany(mappedBy = "dayGhe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Ghe> dsGhe;

    public DayGhe() {}

    public DayGhe(String tenDayGhe, LoaiGhe loaiGhe, int giaDayGhe) {
        this.tenDayGhe = tenDayGhe;
        this.loaiGhe = loaiGhe;
        this.giaDayGhe = giaDayGhe;
        dsGhe = new HashSet<>();
    }

    public int getIdDayGhe() {
        return idDayGhe;
    }

    public void setIdDayGhe(int idDayGhe) {
        this.idDayGhe = idDayGhe;
    }

    public String getTenDayGhe() {
        return tenDayGhe;
    }

    public void setTenDayGhe(String tenDayGhe) {
        this.tenDayGhe = tenDayGhe;
    }

    public LoaiGhe getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(LoaiGhe loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public int getGiaDayGhe() {
        return giaDayGhe;
    }

    public void setGiaDayGhe(int giaDayGhe) {
        this.giaDayGhe = giaDayGhe;
    }

    public PhongChieuPhim getPhongChieuPhim() {
        return phongChieuPhim;
    }

    public void setPhongChieuPhim(PhongChieuPhim phongChieuPhim) {
        this.phongChieuPhim = phongChieuPhim;
    }

    public Set<Ghe> getDsGhe() {
        return dsGhe;
    }

    public void setDsGhe(Set<Ghe> dsGhe) {
        this.dsGhe = dsGhe;
    }

    public Set<Ghe> generateDsGhe(LoaiGhe loaiGhe) {
        int soLuongGheToiDa;
        if(loaiGhe == LoaiGhe.DOI) {
            soLuongGheToiDa = 5;
        } else {
            soLuongGheToiDa = 10;
        }
        Set<Ghe> dsGhe = new HashSet<>();
        for (int i = 0; i <= soLuongGheToiDa; i++) {
            dsGhe.add(new Ghe(true, this));
        }
        return dsGhe;
    }
}
