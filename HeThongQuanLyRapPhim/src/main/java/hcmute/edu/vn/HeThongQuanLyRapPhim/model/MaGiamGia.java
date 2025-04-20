package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ma_giam_gia")
public class MaGiamGia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ma_giam_gia")
    private int idMaGiamGia;

    @Column(name = "ma_giam_gia", nullable = false)
    private String tenMaGiamGia;

    @Column(name = "phan_tram_giam_gia", nullable = false)
    private int phanTramGiamGia;

    @Column(name = "han_muc_ap_dung", nullable = false)
    private double hanMucApDung;

    @Column(name = "gia_tri_giam_toi_Da", nullable = false)
    private int giaTriGiamToiDa;

    @Column(name = "ngay_bat_dau_ap_dung", nullable = false)
    private LocalDateTime ngayBatDauApDung;

    @Column(name = "ngay_ket_thuc_ap_dung", nullable = false)
    private LocalDateTime ngayKetThucApDung;

    @Column(name = "trang_thai_su_dung", nullable = false)
    private boolean trangThaiSuDung;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_chien_dich_giam_gia")
    private ChienDichGiamGia chienDichGiamGia;

    @ManyToOne
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung doiTuongSuDung;

    public MaGiamGia() {}

    public MaGiamGia(String tenMaGiamGia, int phanTramGiamGia, double hanMucApDung, LocalDateTime ngayBatDauApDung, LocalDateTime ngayKetThucApDung, boolean trangThaiSuDung, ChienDichGiamGia chienDichGiamGia, int giaTriGiamToiDa) {
        this.tenMaGiamGia = tenMaGiamGia;
        this.phanTramGiamGia = phanTramGiamGia;
        this.hanMucApDung = hanMucApDung;
        this.ngayBatDauApDung = ngayBatDauApDung;
        this.ngayKetThucApDung = ngayKetThucApDung;
        this.trangThaiSuDung = trangThaiSuDung;
        this.chienDichGiamGia = chienDichGiamGia;
        this.giaTriGiamToiDa = giaTriGiamToiDa;
        this.doiTuongSuDung = null;
    }

    public int getIdMaGiamGia() {
        return idMaGiamGia;
    }

    public void setIdMaGiamGia(int idMaGiamGia) {
        this.idMaGiamGia = idMaGiamGia;
    }

    public String getTenMaGiamGia() {
        return tenMaGiamGia;
    }

    public void setTenMaGiamGia(String tenMaGiamGia) {
        this.tenMaGiamGia = tenMaGiamGia;
    }

    public int getPhanTramGiamGia() {
        return phanTramGiamGia;
    }

    public void setPhanTramGiamGia(int phanTramGiamGia) {
        this.phanTramGiamGia = phanTramGiamGia;
    }

    public double getHanMucApDung() {
        return hanMucApDung;
    }

    public void setHanMucApDung(double hanMucApDung) {
        this.hanMucApDung = hanMucApDung;
    }

    public int getGiaTriGiamToiDa() {
        return giaTriGiamToiDa;
    }

    public void setGiaTriGiamToiDa(int giaTriGiamToiDa) {
        this.giaTriGiamToiDa = giaTriGiamToiDa;
    }

    public LocalDateTime getNgayBatDauApDung() {
        return ngayBatDauApDung;
    }

    public void setNgayBatDauApDung(LocalDateTime ngayBatDauApDung) {
        this.ngayBatDauApDung = ngayBatDauApDung;
    }

    public LocalDateTime getNgayKetThucApDung() {
        return ngayKetThucApDung;
    }

    public void setNgayKetThucApDung(LocalDateTime ngayKetThucApDung) {
        this.ngayKetThucApDung = ngayKetThucApDung;
    }

    public boolean isTrangThaiSuDung() {
        return trangThaiSuDung;
    }

    public void setTrangThaiSuDung(boolean trangThaiSuDung) {
        this.trangThaiSuDung = trangThaiSuDung;
    }

    public ChienDichGiamGia getChienDichGiamGia() {
        return chienDichGiamGia;
    }

    public void setChienDichGiamGia(ChienDichGiamGia chienDichGiamGia) {
        this.chienDichGiamGia = chienDichGiamGia;
    }

    public DoiTuongSuDung getDoiTuongSuDung() {
        return doiTuongSuDung;
    }

    public void setDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung) {
        this.doiTuongSuDung = doiTuongSuDung;
    }
}