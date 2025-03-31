package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "combo_bap_nuoc")
public class ComboBapNuoc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_combo_bap_nuoc")
    private int idComboBapNuoc;

    @Column(name = "ten_combo")
    private String tenCombo;

    @Column(name = "mo_ta_combo")
    private String moTaCombo;

    @Column(name = "gia_tien")
    private double giaTien;

    public ComboBapNuoc() {
    }

    public ComboBapNuoc(String tenCombo, String moTaCombo, double giaTien) {
        this.tenCombo = tenCombo;
        this.moTaCombo = moTaCombo;
        this.giaTien = giaTien;
    }

    public int getIdComboBapNuoc() {
        return idComboBapNuoc;
    }

    public void setIdComboBapNuoc(int idComboBapNuoc) {
        this.idComboBapNuoc = idComboBapNuoc;
    }

    public String getTenCombo() {
        return tenCombo;
    }

    public void setTenCombo(String tenCombo) {
        this.tenCombo = tenCombo;
    }

    public String getMoTaCombo() {
        return moTaCombo;
    }

    public void setMoTaCombo(String moTaCombo) {
        this.moTaCombo = moTaCombo;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }
}
