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

    public static Specification<Phim> containsKeywordInAllFields(String keyword) {
        return (Root<Phim> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();  // Không lọc nếu từ khóa rỗng
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            List<Predicate> predicates = new ArrayList<>();

            // Tìm theo ID (nếu keyword là số)
            try {
                Integer id = Integer.parseInt(keyword);
                predicates.add(cb.equal(root.get("idPhim"), id));
            } catch (NumberFormatException ignored) {
            }

            // Tìm theo tên phim, đạo diễn, thể loại (LIKE)
            predicates.add(cb.like(cb.lower(root.get("tenPhim")), pattern));
            predicates.add(cb.like(cb.lower(root.get("daoDien")), pattern));
            predicates.add(cb.like(cb.lower(root.get("theLoai")), pattern));

            // Tìm theo thời lượng (nếu keyword là số)
            try {
                Integer duration = Integer.parseInt(keyword);
                predicates.add(cb.equal(root.get("thoiLuongChieu"), duration));
            } catch (NumberFormatException ignored) {
            }

            // Tìm theo ngày khởi chiếu (nếu keyword đúng định dạng ngày)
            Date date = tryParseDate(keyword);
            if (date != null) {
                predicates.add(cb.equal(root.get("thoiGianKhoiChieu"), date));
            }

            // Tìm theo enum Độ tuổi
            DoTuoi doTuoi = tryParseDoTuoi(keyword);
            if (doTuoi != null) {
                predicates.add(cb.equal(root.get("doTuoi"), doTuoi));
            }

            // Tìm theo enum Trạng thái phim
            TrangThaiPhim trangThaiPhim = tryParseTrangThaiPhim(keyword);
            if (trangThaiPhim != null) {
                predicates.add(cb.equal(root.get("trangThaiPhim"), trangThaiPhim));
            }

            // Kết hợp tất cả điều kiện với OR
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static Date tryParseDate(String keyword) {
        List<String> formats = List.of("yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy");
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                return sdf.parse(keyword);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    private static DoTuoi tryParseDoTuoi(String keyword) {
        try {
            return DoTuoi.valueOf(keyword.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static TrangThaiPhim tryParseTrangThaiPhim(String keyword) {
        try {
            return TrangThaiPhim.valueOf(keyword.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
