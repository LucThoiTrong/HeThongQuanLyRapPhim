package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "danh_gia")
public class DanhGia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_gia")
    private int idDanhGia;

    @Column(name = "noi_dung_danh_gia")
    private String noiDungDanhGia;

    @Column(name = "diem_danh_gia")
    private int diemDanhGia;

    @Column(name = "thoi_gian_danh_gia")
    private Date thoiGianDanhGia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_phim")
    private Phim phim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung doiTuongSuDung;

    @OneToMany(mappedBy = "danhGia", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<PhanHoi> dsPhanHoi;

    public DanhGia() {
    }

    public DanhGia(String noiDungDanhGia, int diemDanhGia, Date thoiGianDanhGia, Phim phim, DoiTuongSuDung doiTuongSuDung) {
        this.noiDungDanhGia = noiDungDanhGia;
        this.diemDanhGia = diemDanhGia;
        this.thoiGianDanhGia = thoiGianDanhGia;
        this.phim = phim;
        this.doiTuongSuDung = doiTuongSuDung;
        dsPhanHoi = new HashSet<>();
    }

    public int getIdDanhGia() {
        return idDanhGia;
    }

    public void setIdDanhGia(int idDanhGia) {
        this.idDanhGia = idDanhGia;
    }

    public String getNoiDungDanhGia() {
        return noiDungDanhGia;
    }

    public void setNoiDungDanhGia(String noiDungDanhGia) {
        this.noiDungDanhGia = noiDungDanhGia;
    }

    public int getDiemDanhGia() {
        return diemDanhGia;
    }

    public void setDiemDanhGia(int diemDanhGia) {
        this.diemDanhGia = diemDanhGia;
    }

    public Date getThoiGianDanhGia() {
        return thoiGianDanhGia;
    }

    public void setThoiGianDanhGia(Date thoiGianDanhGia) {
        this.thoiGianDanhGia = thoiGianDanhGia;
    }

    public Phim getPhim() {
        return phim;
    }

    public void setPhim(Phim phim) {
        this.phim = phim;
    }

    public DoiTuongSuDung getDoiTuongSuDung() {
        return doiTuongSuDung;
    }

    public void setDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung) {
        this.doiTuongSuDung = doiTuongSuDung;
    }

    public Set<PhanHoi> getDsPhanHoi() {
        return dsPhanHoi;
    }

    public void setDsPhanHoi(Set<PhanHoi> dsPhanHoi) {
        this.dsPhanHoi = dsPhanHoi;
    }

    public void themPhanHoi(PhanHoi phanHoi) {
        dsPhanHoi.add(phanHoi);
    }
}
