package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "chi_tiet_combo_bap_nuoc")
public class ChiTietComBoBapNuoc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chi_tiet_combo_bap_nuoc")
    private int idChiTietComboBapNuoc;

    private int soLuong;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_combo_bap_nuoc")
    private ComboBapNuoc comboBapNuoc;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    public ChiTietComBoBapNuoc() {
    }

    public ChiTietComBoBapNuoc(int soLuong, ComboBapNuoc comboBapNuoc, HoaDon hoaDon) {
        this.soLuong = soLuong;
        this.comboBapNuoc = comboBapNuoc;
        this.hoaDon = hoaDon;
    }

    public int getIdChiTietComboBapNuoc() {
        return idChiTietComboBapNuoc;
    }

    public void setIdChiTietComboBapNuoc(int idChiTietComboBapNuoc) {
        this.idChiTietComboBapNuoc = idChiTietComboBapNuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public ComboBapNuoc getComboBapNuoc() {
        return comboBapNuoc;
    }

    public void setComboBapNuoc(ComboBapNuoc comboBapNuoc) {
        this.comboBapNuoc = comboBapNuoc;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }
}
