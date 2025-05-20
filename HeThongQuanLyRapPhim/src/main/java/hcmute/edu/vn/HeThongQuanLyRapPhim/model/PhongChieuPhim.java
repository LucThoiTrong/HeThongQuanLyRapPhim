package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "phong_chieu_phim")
public class PhongChieuPhim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong_chieu_phim")
    private Integer idPhongChieuPhim;

    @Column(name = "ten_phong_chieu_phim")
    private String tenPhongChieuPhim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rap_phim")
    private RapPhim rapPhim;

    @OneToMany(mappedBy = "phongChieuPhim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DayGhe> dsDayGhe;

    @OneToMany(mappedBy = "phongChieuPhim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<SuatChieu> dsSuatChieu;

    @Column(name = "kich_thuoc_phong")
    @Enumerated(EnumType.STRING)
    private KichThuocPhong kichThuocPhong;

    public PhongChieuPhim() {}

    public PhongChieuPhim(String tenPhongChieuPhim, KichThuocPhong kichThuocPhong, RapPhim rapPhim) {
        this.tenPhongChieuPhim = tenPhongChieuPhim;
        this.kichThuocPhong = kichThuocPhong;
        this.rapPhim = rapPhim;
        dsDayGhe = new HashSet<>();
        dsSuatChieu = new HashSet<>();
    }

    public Integer getIdPhongChieuPhim() {
        return idPhongChieuPhim;
    }

    public void setIdPhongChieuPhim(Integer idPhongChieuPhim) {
        this.idPhongChieuPhim = idPhongChieuPhim;
    }

    public KichThuocPhong getKichThuocPhong() {
        return kichThuocPhong;
    }

    public void setKichThuocPhong(KichThuocPhong kichThuocPhong) {
        this.kichThuocPhong = kichThuocPhong;
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

    // Khởi tạo danh sách dãy ghế trong phòng chiếu
    public int taoDayGhe(PhongChieuPhim phongChieuPhim, List<DayGhe> list, int soLuong, LoaiGhe loai, int gia, int indexStart) {
        for (int i = 0; i < soLuong; i++) {
            DayGhe dayGhe = new DayGhe();
            dayGhe.setTenDayGhe("Dãy " + (char) ('A' + indexStart - 1));
            dayGhe.setLoaiGhe(loai);
            dayGhe.setGiaDayGhe(gia);
            dayGhe.setPhongChieuPhim(phongChieuPhim);
            dayGhe.setDsGhe(dayGhe.generateDsGhe(loai));
            list.add(dayGhe);
            indexStart++;
        }
        return indexStart;
    }

    // lấy tổng số lượng của từng dãy ghế
    public int getSoLuongDayGheTungLoai(LoaiGhe loaiGhe) {
        return (int) getDsDayGhe().stream().filter(dayGhe -> dayGhe.getLoaiGhe() == loaiGhe).count();
    }

    // Lấy số lượng dãy ghế tối đa của 1 phòng chiếu
    public int getSoLuongDayGheToiDa() {
        return switch (getKichThuocPhong()) {
            case NHO -> 10;
            case VUA -> 15;
            case LON -> 20;
        };
    }
}
