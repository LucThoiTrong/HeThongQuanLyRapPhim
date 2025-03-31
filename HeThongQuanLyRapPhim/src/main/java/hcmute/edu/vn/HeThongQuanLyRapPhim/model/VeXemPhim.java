package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ve_xem_phim")
public class VeXemPhim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ve_xem_phim")
    private int idVeXemPhim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_suat_chieu")
    private SuatChieu suatChieu;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_ghe")
    private Ghe ghe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    public VeXemPhim() {
    }

    public VeXemPhim(SuatChieu suatChieu, Ghe ghe) {
        this.suatChieu = suatChieu;
        this.ghe = ghe;
        hoaDon = null;
    }

    public int getIdVeXemPhim() {
        return idVeXemPhim;
    }

    public void setIdVeXemPhim(int idVeXemPhim) {
        this.idVeXemPhim = idVeXemPhim;
    }

    public SuatChieu getSuatChieu() {
        return suatChieu;
    }

    public void setSuatChieu(SuatChieu suatChieu) {
        this.suatChieu = suatChieu;
    }

    public Ghe getGhe() {
        return ghe;
    }

    public void setGhe(Ghe ghe) {
        this.ghe = ghe;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }
}
