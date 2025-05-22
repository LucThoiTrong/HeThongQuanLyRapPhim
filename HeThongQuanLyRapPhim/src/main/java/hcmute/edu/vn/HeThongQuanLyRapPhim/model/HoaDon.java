package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "hoa_don")
public class HoaDon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hoa_don")
    private int idHoaDon;

    @Column(name = "tong_gia_tien")
    private double tongGiaTien;

    @Column(name = "ngay_thanh_toan")
    private LocalDateTime ngayThanhToan;

    @Column(name = "trang_thai_hoa_don")
    @Enumerated(EnumType.STRING)
    private TrangThaiHoaDon trangThaiHoaDon;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung doiTuongSuDung;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_suat_chieu")
    private SuatChieu suatChieu;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<VeXemPhim> dsVeXemPhimDaMua;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ChiTietComBoBapNuoc> dsComBoDaMua;

    public HoaDon() {
    }

    public HoaDon(double tongGiaTien, LocalDateTime ngayThanhToan, TrangThaiHoaDon trangThaiHoaDon, DoiTuongSuDung doiTuongSuDung, SuatChieu suatChieu, Set<VeXemPhim> dsVeXemPhimDaMua, Set<ChiTietComBoBapNuoc> dsChiTietComBoDaMua) {
        this.tongGiaTien = tongGiaTien;
        this.ngayThanhToan = ngayThanhToan;
        this.trangThaiHoaDon = trangThaiHoaDon;
        this.doiTuongSuDung = doiTuongSuDung;
        this.suatChieu = suatChieu;
        this.dsVeXemPhimDaMua = dsVeXemPhimDaMua;
        this.dsComBoDaMua = dsChiTietComBoDaMua;
    }

    public int getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(int idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public double getTongGiaTien() {
        return tongGiaTien;
    }

    public void setTongGiaTien(double tongGiaTien) {
        this.tongGiaTien = tongGiaTien;
    }

    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public TrangThaiHoaDon getTrangThaiHoaDon() {
        return trangThaiHoaDon;
    }

    public void setTrangThaiHoaDon(TrangThaiHoaDon trangThaiHoaDon) {
        this.trangThaiHoaDon = trangThaiHoaDon;
    }

    public DoiTuongSuDung getDoiTuongSuDung() {
        return doiTuongSuDung;
    }

    public void setDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung) {
        this.doiTuongSuDung = doiTuongSuDung;
    }

    public SuatChieu getSuatChieu() {
        return suatChieu;
    }

    public void setSuatChieu(SuatChieu suatChieu) {
        this.suatChieu = suatChieu;
    }

    public Set<VeXemPhim> getDsVeXemPhimDaMua() {
        return dsVeXemPhimDaMua;
    }

    public void setDsVeXemPhimDaMua(Set<VeXemPhim> dsVeXemPhimDaMua) {
        this.dsVeXemPhimDaMua = dsVeXemPhimDaMua;
    }

    public Set<ChiTietComBoBapNuoc> getDsComBoDaMua() {
        return dsComBoDaMua;
    }

    public void setDsComBoDaMua(Set<ChiTietComBoBapNuoc> dsComBoDaMua) {
        this.dsComBoDaMua = dsComBoDaMua;
    }

    public int soLuongVeXemPhimDaMua() {
        return dsVeXemPhimDaMua.size();
    }
}
