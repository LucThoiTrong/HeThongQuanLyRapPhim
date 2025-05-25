package hcmute.edu.vn.HeThongQuanLyRapPhim.facade;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.specification.MovieSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieSearchFacade {
    private final MovieRepository movieRepository;

    public MovieSearchFacade(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Phim> timKiemPhim(String keyword) {
        Specification<Phim> spec = MovieSpecification.containsKeywordInAllFields(keyword);
        return movieRepository.findAll(spec);
    }
}

