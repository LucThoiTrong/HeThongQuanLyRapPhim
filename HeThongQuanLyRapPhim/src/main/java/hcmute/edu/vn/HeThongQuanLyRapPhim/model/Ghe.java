package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ghe")
public class Ghe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ghe")
    private int idGhe;

    @Column(name = "trang_thai_ghe")
    private boolean trangThaiGhe;

    @ManyToOne
    @JoinColumn(name = "id_day_ghe")
    private DayGhe dayGhe;

    public Ghe() {
    }

    public Ghe(boolean trangThaiGhe, DayGhe dayGhe) {
        this.trangThaiGhe = trangThaiGhe;
        this.dayGhe = dayGhe;
    }

    public int getIdGhe() {
        return idGhe;
    }

    public void setIdGhe(int idGhe) {
        this.idGhe = idGhe;
    }

    public boolean isTrangThaiGhe() {
        return trangThaiGhe;
    }

    public void setTrangThaiGhe(boolean trangThaiGhe) {
        this.trangThaiGhe = trangThaiGhe;
    }

    public DayGhe getDayGhe() {
        return dayGhe;
    }

    public void setDayGhe(DayGhe dayGhe) {
        this.dayGhe = dayGhe;
    }
}
