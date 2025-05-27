package hcmute.edu.vn.HeThongQuanLyRapPhim.specification;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.DoTuoi;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.model.TrangThaiPhim;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieSpecification {

    // Hàm tạo điều kiện tìm kiếm phim dựa trên từ khóa nhập vào
    public static Specification<Phim> containsKeywordInAllFields(String keyword) {
        return (Root<Phim> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // Kiểm tra nếu từ khóa rỗng hoặc null, trả về điều kiện trung lập (không lọc)
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            // Thêm ký tự % vào trước và sau từ khóa để tìm kiếm gần đúng (LIKE)
            String pattern = "%" + keyword.toLowerCase() + "%";

            // Tạo danh sách các điều kiện tìm kiếm
            List<Predicate> predicates = new ArrayList<>();

            // Tìm theo ID phim (nếu từ khóa là số)
            try {
                Integer id = Integer.parseInt(keyword);
                predicates.add(cb.equal(root.get("idPhim"), id));
            } catch (NumberFormatException ignored) {
                // Bỏ qua nếu từ khóa không phải số
            }

            // Tìm kiếm gần đúng theo tên phim, đạo diễn, thể loại
            predicates.add(cb.like(cb.lower(root.get("tenPhim")), pattern)); // Tên phim
            predicates.add(cb.like(cb.lower(root.get("daoDien")), pattern)); // Đạo diễn
            predicates.add(cb.like(cb.lower(root.get("theLoai")), pattern)); // Thể loại

            // Tìm theo thời lượng phim (nếu từ khóa là số)
            try {
                Integer duration = Integer.parseInt(keyword);
                predicates.add(cb.equal(root.get("thoiLuongChieu"), duration));
            } catch (NumberFormatException ignored) {
                // Bỏ qua nếu từ khóa không phải số
            }

            // Tìm theo ngày khởi chiếu (nếu từ khóa là ngày hợp lệ)
            Date date = tryParseDate(keyword);
            if (date != null) {
                predicates.add(cb.equal(root.get("thoiGianKhoiChieu"), date));
            }

            // Tìm theo độ tuổi (enum DoTuoi)
            DoTuoi doTuoi = tryParseDoTuoi(keyword);
            if (doTuoi != null) {
                predicates.add(cb.equal(root.get("doTuoi"), doTuoi));
            }

            // Tìm theo trạng thái phim (enum TrangThaiPhim)
            TrangThaiPhim trangThaiPhim = tryParseTrangThaiPhim(keyword);
            if (trangThaiPhim != null) {
                predicates.add(cb.equal(root.get("trangThaiPhim"), trangThaiPhim));
            }

            // Kết hợp tất cả điều kiện với toán tử OR
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    // Hàm thử phân tích từ khóa thành định dạng ngày
    private static Date tryParseDate(String keyword) {
        // Danh sách các định dạng ngày được hỗ trợ
        List<String> formats = List.of("yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy");
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false); // Không cho phép phân tích ngày không hợp lệ
                return sdf.parse(keyword); // Trả về ngày nếu phân tích thành công
            } catch (ParseException ignored) {
                // Bỏ qua nếu định dạng không khớp
            }
        }
        return null; // Trả về null nếu không phân tích được
    }

    // Hàm thử phân tích từ khóa thành enum DoTuoi
    private static DoTuoi tryParseDoTuoi(String keyword) {
        try {
            return DoTuoi.valueOf(keyword.toUpperCase()); // Chuyển từ khóa thành chữ hoa và thử khớp với enum
        } catch (IllegalArgumentException e) {
            return null; // Trả về null nếu không khớp
        }
    }

    // Hàm thử phân tích từ khóa thành enum TrangThaiPhim
    private static TrangThaiPhim tryParseTrangThaiPhim(String keyword) {
        try {
            return TrangThaiPhim.valueOf(keyword.toUpperCase()); // Chuyển từ khóa thành chữ hoa và thử khớp với enum
        } catch (IllegalArgumentException e) {
            return null; // Trả về null nếu không khớp
        }
    }
}