package hcmute.edu.vn.HeThongQuanLyRapPhim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rap_phim")
public class RapPhim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rap_phim")
    private int idRapPhim;

    @Column(name = "ten_rap_phim", nullable = false)
    private String tenRapPhim;

    @Column(name = "dia_chi_rap_phim")
    private String diaChiRapPhim;

    @Column(name = "trang_thai_rap_phim", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrangThaiRapPhim trangThaiRapPhim;

    @OneToOne
    @JoinColumn(name = "id_doi_tuong_su_dung")
    private DoiTuongSuDung nhanVien;

    @OneToMany(mappedBy = "rapPhim",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PhongChieuPhim> dsPhongChieuPhim;

    public RapPhim() {}

    public RapPhim(String tenRapPhim, String diaChiRapPhim, TrangThaiRapPhim trangThaiRapPhim) {
        this.tenRapPhim = tenRapPhim;
        this.trangThaiRapPhim = trangThaiRapPhim;
        this.diaChiRapPhim = diaChiRapPhim;
        this.nhanVien = null;
        dsPhongChieuPhim = new HashSet<>();
    }

    public int getIdRapPhim() {
        return idRapPhim;
    }

    public void setIdRapPhim(int idRapPhim) {
        this.idRapPhim = idRapPhim;
    }

    public String getTenRapPhim() {
        return tenRapPhim;
    }

    public void setTenRapPhim(String tenRapPhim) {
        this.tenRapPhim = tenRapPhim;
    }

    public TrangThaiRapPhim getTrangThaiRapPhim() {
        return trangThaiRapPhim;
    }

    public void setTrangThaiRapPhim(TrangThaiRapPhim trangThaiRapPhim) {
        this.trangThaiRapPhim = trangThaiRapPhim;
    }

    public DoiTuongSuDung getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(DoiTuongSuDung nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Set<PhongChieuPhim> getDsPhongChieuPhim() {
        return dsPhongChieuPhim;
    }

    public void setDsPhongChieuPhim(Set<PhongChieuPhim> dsPhongChieuPhim) {
        this.dsPhongChieuPhim = dsPhongChieuPhim;
    }

    public String getDiaChiRapPhim() {
        return diaChiRapPhim;
    }

    public void setDiaChiRapPhim(String diaChiRapPhim) {
        this.diaChiRapPhim = diaChiRapPhim;
    }

    // Thực hiện kiểm tra tên phòng chiếu phim
    public boolean kiemTraTenRapPhim(String tenPhongChieuPhim) {
        return getDsPhongChieuPhim().stream()
                .anyMatch(pc -> pc.getTenPhongChieuPhim().equals(tenPhongChieuPhim));
    }

    // Lấy danh sách suất chiếu của 1 rạp phim dựa trên ngày chiếu và hình thức chiếu
    public List<SuatChieu> layDanhSachSuatChieu(LocalDate ngayChieu, HinhThucChieu hinhThucChieu, Phim phim) {
        return getDsPhongChieuPhim().stream()
                .flatMap(pc -> pc.getDsSuatChieu().stream())
                .filter(sc -> sc.getNgayGioChieu().toLocalDate().equals(ngayChieu) &&
                        sc.getHinhThucChieu().equals(hinhThucChieu) &&
                        sc.getNgayGioChieu().isAfter(LocalDateTime.now()) &&
                        sc.getPhim().equals(phim))
                .toList();
    }

    // Tính tổng doanh thu từng tháng
    public double tongDoanhThuTungThang(int thang, int nam) {
        // Lấy ngày đầu tháng và ngày cuối tháng
        LocalDateTime startOfMonth = LocalDate.of(nam, thang, 1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.of(nam, thang, 1)
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(LocalTime.MAX);

        return  getDsPhongChieuPhim().stream()
                .flatMap(pc -> pc.getDsSuatChieu().stream())
                .flatMap(sc -> sc.getDsHoaDon().stream())
                .filter(hd -> {
                    LocalDateTime ngayThanhToan = hd.getNgayThanhToan();
                    return (ngayThanhToan.isEqual(startOfMonth) || ngayThanhToan.isAfter(startOfMonth)) &&
                            (ngayThanhToan.isEqual(endOfMonth) || ngayThanhToan.isBefore(endOfMonth)) &&
                            hd.getTrangThaiHoaDon().equals(TrangThaiHoaDon.DA_THANH_TOAN);
                })
                .mapToDouble(HoaDon::getTongGiaTien).sum();
    }

    // Tính doanh thu của từng bộ phim trong 1 tháng
    public double tongDoanhThuTungPhim(int thang, int nam, Phim phim) {
        // Lấy ngày đầu tháng và ngày cuối tháng
        LocalDateTime startOfMonth = LocalDate.of(nam, thang, 1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.of(nam, thang, 1)
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(LocalTime.MAX);

        return getDsPhongChieuPhim().stream()
                .flatMap(pc -> pc.getDsSuatChieu().stream())
                .filter(sc -> sc.getPhim().equals(phim))
                .flatMap(sc -> sc.getDsHoaDon().stream())
                .filter(hd -> {
                    LocalDateTime ngayThanhToan = hd.getNgayThanhToan();
                    return (ngayThanhToan.isEqual(startOfMonth) || ngayThanhToan.isAfter(startOfMonth)) &&
                            (ngayThanhToan.isEqual(endOfMonth) || ngayThanhToan.isBefore(endOfMonth)) &&
                            hd.getTrangThaiHoaDon().equals(TrangThaiHoaDon.DA_THANH_TOAN);
                })
                .mapToDouble(HoaDon::getTongGiaTien)
                .sum();
    }

    // Tính số lượng từng combo bán ra trong 1 tháng
    public int tongSoLuongTungComboBanRa(int thang, int nam, ComboBapNuoc comboBapNuoc) {
        // Lấy ngày đầu tháng và ngày cuối tháng
        LocalDateTime startOfMonth = LocalDate.of(nam, thang, 1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.of(nam, thang, 1)
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(LocalTime.MAX);

        return getDsPhongChieuPhim().stream()
                .flatMap(pc -> pc.getDsSuatChieu().stream())
                .flatMap(sc -> sc.getDsHoaDon().stream())
                .filter(hd -> {
                    LocalDateTime ngayThanhToan = hd.getNgayThanhToan();
                    return (ngayThanhToan.isEqual(startOfMonth) || ngayThanhToan.isAfter(startOfMonth)) &&
                            (ngayThanhToan.isEqual(endOfMonth) || ngayThanhToan.isBefore(endOfMonth));
                })
                .flatMap(hd -> hd.getDsComBoDaMua().stream())
                .filter(chiTietComBoBapNuoc -> chiTietComBoBapNuoc.getComboBapNuoc().equals(comboBapNuoc))
                .mapToInt(ChiTietComBoBapNuoc::getSoLuong)
                .sum();
    }
}