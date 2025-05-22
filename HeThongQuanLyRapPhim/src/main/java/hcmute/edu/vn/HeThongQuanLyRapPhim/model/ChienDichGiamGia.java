package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chien_dich_giam_gia")
public class ChienDichGiamGia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chien_dich_giam_gia")
    private int idChienDichGiamGia;

    @Column(name = "ten_chien_dich", nullable = false)
    private String tenChienDich;

    @Column(name = "ngay_bat_dau_chien_dich", nullable = false)
    private LocalDateTime ngayBatDauChienDich;

    @Column(name = "ngay_ket_thuc_chien_dich", nullable = false)
    private LocalDateTime ngayKetThucChienDich;

    @OneToMany(mappedBy = "chienDichGiamGia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MaGiamGia> dsMaGiamGia;

    public ChienDichGiamGia() {}

    public ChienDichGiamGia(String tenChienDich, LocalDateTime ngayBatDauChienDich, LocalDateTime ngayKetThucChienDich) {
        this.tenChienDich = tenChienDich;
        this.ngayBatDauChienDich = ngayBatDauChienDich;
        this.ngayKetThucChienDich = ngayKetThucChienDich;
        dsMaGiamGia = new HashSet<>();
    }

    public int getIdChienDichGiamGia() {
        return idChienDichGiamGia;
    }

    public void setIdChienDichGiamGia(int idChienDichGiamGia) {
        this.idChienDichGiamGia = idChienDichGiamGia;
    }

    public String getTenChienDich() {
        return tenChienDich;
    }

    public void setTenChienDich(String tenChienDich) {
        this.tenChienDich = tenChienDich;
    }

    public LocalDateTime getNgayBatDauChienDich() {
        return ngayBatDauChienDich;
    }

    public void setNgayBatDauChienDich(LocalDateTime ngayBatDauChienDich) {
        this.ngayBatDauChienDich = ngayBatDauChienDich;
    }

    public LocalDateTime getNgayKetThucChienDich() {
        return ngayKetThucChienDich;
    }

    public void setNgayKetThucChienDich(LocalDateTime ngayKetThucChienDich) {
        this.ngayKetThucChienDich = ngayKetThucChienDich;
    }

    public Set<MaGiamGia> getDsMaGiamGia() {
        return dsMaGiamGia;
    }

    public void setDsMaGiamGia(Set<MaGiamGia> dsMaGiamGia) {
        this.dsMaGiamGia = dsMaGiamGia;
    }

    public void addMaGiamGia(MaGiamGia maGiamGia, int soLuongMaGiamGia, ChienDichGiamGia chienDichGiamGia) {
        if(dsMaGiamGia == null) {
            dsMaGiamGia = new HashSet<>();
        }
        for(int i = 0; i < soLuongMaGiamGia; i++) {
            MaGiamGia newMaGiamGia = new MaGiamGia();
            newMaGiamGia.setTenMaGiamGia(maGiamGia.getTenMaGiamGia());
            newMaGiamGia.setPhanTramGiamGia(maGiamGia.getPhanTramGiamGia());
            newMaGiamGia.setHanMucApDung(maGiamGia.getHanMucApDung());
            newMaGiamGia.setGiaTriGiamToiDa(maGiamGia.getGiaTriGiamToiDa());
            newMaGiamGia.setNgayBatDauApDung(maGiamGia.getNgayBatDauApDung());
            newMaGiamGia.setNgayKetThucApDung(maGiamGia.getNgayKetThucApDung());
            newMaGiamGia.setTrangThaiSuDung(maGiamGia.isTrangThaiSuDung());
            newMaGiamGia.setChienDichGiamGia(chienDichGiamGia);
            dsMaGiamGia.add(newMaGiamGia);
        }
        setDsMaGiamGia(dsMaGiamGia);
    }

}
