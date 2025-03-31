package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "phan_hoi")
public class PhanHoi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phan_hoi")
    private int idPhanHoi;

    @Column(name = "noi_dung_phan_hoi")
    private String noiDungPhanHoi;

    @Column(name = "thoi_gian_phan_hoi")
    private LocalDateTime thoiGianPhanHoi;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_danh_gia")
    private DanhGia danhGia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung doiTuongSuDung;

    public PhanHoi() {
    }

    public PhanHoi(String noiDungPhanHoi, LocalDateTime thoiGianPhanHoi, DanhGia danhGia, DoiTuongSuDung doiTuongSuDung) {
        this.noiDungPhanHoi = noiDungPhanHoi;
        this.thoiGianPhanHoi = thoiGianPhanHoi;
        this.danhGia = danhGia;
        this.doiTuongSuDung = doiTuongSuDung;
    }

    public int getIdPhanHoi() {
        return idPhanHoi;
    }

    public void setIdPhanHoi(int idPhanHoi) {
        this.idPhanHoi = idPhanHoi;
    }

    public String getNoiDungPhanHoi() {
        return noiDungPhanHoi;
    }

    public void setNoiDungPhanHoi(String noiDungPhanHoi) {
        this.noiDungPhanHoi = noiDungPhanHoi;
    }

    public LocalDateTime getThoiGianPhanHoi() {
        return thoiGianPhanHoi;
    }

    public void setThoiGianPhanHoi(LocalDateTime thoiGianPhanHoi) {
        this.thoiGianPhanHoi = thoiGianPhanHoi;
    }

    public DanhGia getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(DanhGia danhGia) {
        this.danhGia = danhGia;
    }

    public DoiTuongSuDung getDoiTuongSuDung() {
        return doiTuongSuDung;
    }

    public void setDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung) {
        this.doiTuongSuDung = doiTuongSuDung;
    }
}
