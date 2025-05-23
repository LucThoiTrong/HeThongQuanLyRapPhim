package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.LinkedHashSet;


@Entity
@Table(name = "phong_chieu_phim")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPhongChieuPhim")
public class PhongChieuPhim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong_chieu_phim")
    private Integer idPhongChieuPhim;

    @Column(name = "ten_phong_chieu_phim")
    private String tenPhongChieuPhim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rap_phim")
    private RapPhim rapPhim;

    @OneToMany(mappedBy = "phongChieuPhim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DayGhe> dsDayGhe;

    @OneToMany(mappedBy = "phongChieuPhim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<SuatChieu> dsSuatChieu;

    @Column(name = "kich_thuoc_phong")
    @Enumerated(EnumType.STRING)
    private KichThuocPhong kichThuocPhong;

    public PhongChieuPhim() {}

    public PhongChieuPhim(String tenPhongChieuPhim, KichThuocPhong kichThuocPhong, RapPhim rapPhim) {
        this.tenPhongChieuPhim = tenPhongChieuPhim;
        this.kichThuocPhong = kichThuocPhong;
        this.rapPhim = rapPhim;
        dsDayGhe = new HashSet<>();
        dsSuatChieu = new HashSet<>();
    }

    public Integer getIdPhongChieuPhim() {
        return idPhongChieuPhim;
    }

    public void setIdPhongChieuPhim(Integer idPhongChieuPhim) {
        this.idPhongChieuPhim = idPhongChieuPhim;
    }

    public KichThuocPhong getKichThuocPhong() {
        return kichThuocPhong;
    }

    public void setKichThuocPhong(KichThuocPhong kichThuocPhong) {
        this.kichThuocPhong = kichThuocPhong;
    }

    public RapPhim getRapPhim() {
        return rapPhim;
    }

    public void setRapPhim(RapPhim rapPhim) {
        this.rapPhim = rapPhim;
    }

    public Set<DayGhe> getDsDayGhe() {
        return dsDayGhe;
    }

    public List<DayGhe> getDsDayGheSorted() {
        return getDsDayGhe().stream()
                .sorted(Comparator.comparing(DayGhe::getTenDayGhe)) // Sắp xếp theo tên dãy ghế
                .peek(dayGhe -> {
                    // Sắp xếp dsGhe theo dayName + idGhe
                    Set<Ghe> sortedDsGhe = dayGhe.getDsGhe().stream()
                            .sorted(Comparator.comparing(ghe -> dayGhe.getTenDayGhe() + ghe.getIdGhe()))
                            .collect(Collectors.toCollection(LinkedHashSet::new)); // Duy trì thứ tự

                    // Tạo DayGhe mới hoặc cập nhật lại dsGhe
                    dayGhe.setDsGhe(sortedDsGhe);
                })
                .collect(Collectors.toList());
    }

    public void setDsDayGhe(Set<DayGhe> dsDayGhe) {
        this.dsDayGhe = dsDayGhe;
    }

    public String getTenPhongChieuPhim() {
        return tenPhongChieuPhim;
    }

    public void setTenPhongChieuPhim(String tenPhongChieuPhim) {
        this.tenPhongChieuPhim = tenPhongChieuPhim;
    }

    public Set<SuatChieu> getDsSuatChieu() {
        return dsSuatChieu;
    }

    public void setDsSuatChieu(Set<SuatChieu> dsSuatChieu) {
        this.dsSuatChieu = dsSuatChieu;
    }

    // Khởi tạo danh sách dãy ghế trong phòng chiếu
    public int taoDayGhe(PhongChieuPhim phongChieuPhim, List<DayGhe> list, int soLuong, LoaiGhe loai, int gia, int indexStart) {
        for (int i = 0; i < soLuong; i++) {
            DayGhe dayGhe = new DayGhe();
            dayGhe.setTenDayGhe("Dãy " + (char) ('A' + indexStart - 1));
            dayGhe.setLoaiGhe(loai);
            dayGhe.setGiaDayGhe(gia);
            dayGhe.setPhongChieuPhim(phongChieuPhim);
            dayGhe.setDsGhe(dayGhe.generateDsGhe(loai));
            list.add(dayGhe);
            indexStart++;
        }
        return indexStart;
    }

    // lấy tổng số lượng của từng dãy ghế
    public int getSoLuongDayGheTungLoai(LoaiGhe loaiGhe) {
        return (int) getDsDayGhe().stream().filter(dayGhe -> dayGhe.getLoaiGhe() == loaiGhe).count();
    }

    // Lấy số lượng dãy ghế tối đa của 1 phòng chiếu
    public int getSoLuongDayGheToiDa() {
        return switch (getKichThuocPhong()) {
            case NHO -> 10;
            case VUA -> 15;
            case LON -> 20;
        };
    }

    // Kiểm tra toàn bộ suất chiếu của rạp đó
    // Thời gian kết thúc = thời gian bắt đầu + thời lượng chiếu
    // Đối với suất chiếu mới
    // Kiểm tra xem Thời gian bắt đầu > thời gian kết thúc của các suất chiếu đã có trong rạp
    // Hoặc thời gian kết thúc < Thời gian bắt đầu của suất các suất chiếu đã có trong rạp
    // Đối với suất chiếu cập nhật => logic ko thay đổi chỉ bỏ qua lần kiểm tra nếu id trùng
    public boolean kiemTraThoiGianSuatChieu(SuatChieu suatChieuMoi) {
        LocalDateTime startMoi = suatChieuMoi.getNgayGioChieu();
        LocalDateTime endMoi = startMoi.plusMinutes(suatChieuMoi.getPhim().getThoiLuongChieu());

        return getDsSuatChieu().stream()
                .filter(sc -> !sc.getIdSuatChieu().equals(suatChieuMoi.getIdSuatChieu())) // bỏ qua chính nó nếu đang chỉnh sửa
                .anyMatch(sc -> {
                    LocalDateTime startCu = sc.getNgayGioChieu();
                    LocalDateTime endCu = startCu.plusMinutes(sc.getPhim().getThoiLuongChieu());

                    // Kiểm tra có giao nhau hay không
                    // Có giao nhau => true
                    // Không giao nhau => false
                    return !(endMoi.isBefore(startCu) || startMoi.isAfter(endCu));
                });
    }
}
