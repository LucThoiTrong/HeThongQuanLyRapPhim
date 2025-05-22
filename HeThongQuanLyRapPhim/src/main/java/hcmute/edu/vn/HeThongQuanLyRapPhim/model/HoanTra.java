package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "hoan_tra")
public class HoanTra implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hoan_tra")
    private int idHoanTra;

    @Column(name = "ngay_hoan_tra")
    private LocalDateTime ngayHoanTra;

    @Column(name = "ly_do_hoan_tra")
    private String lyDoHoanTra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung doiTuongSuDung;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    public HoanTra() {
    }

    public HoanTra(LocalDateTime ngayHoanTra, String lyDoHoanTra, DoiTuongSuDung doiTuongSuDung, HoaDon hoaDon) {
        this.ngayHoanTra = ngayHoanTra;
        this.lyDoHoanTra = lyDoHoanTra;
        this.doiTuongSuDung = doiTuongSuDung;
        this.hoaDon = hoaDon;
    }

    public int getIdHoanTra() {
        return idHoanTra;
    }

    public void setIdHoanTra(int idHoanTra) {
        this.idHoanTra = idHoanTra;
    }

    public LocalDateTime getNgayHoanTra() {
        return ngayHoanTra;
    }

    public void setNgayHoanTra(LocalDateTime ngayHoanTra) {
        this.ngayHoanTra = ngayHoanTra;
    }

    public String getLyDoHoanTra() {
        return lyDoHoanTra;
    }

    public void setLyDoHoanTra(String lyDoHoanTra) {
        this.lyDoHoanTra = lyDoHoanTra;
    }

    public DoiTuongSuDung getDoiTuongSuDung() {
        return doiTuongSuDung;
    }

    public void setDoiTuongSuDung(DoiTuongSuDung doiTuongSuDung) {
        this.doiTuongSuDung = doiTuongSuDung;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }
}
