package hcmute.edu.vn.HeThongQuanLyRapPhim.facade;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.repository.MovieRepository;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieServiceImpl;
import hcmute.edu.vn.HeThongQuanLyRapPhim.specification.MovieSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieSearchFacade {
    private final MovieService movieService;

    @Autowired
    public MovieSearchFacade(MovieServiceImpl movieService) {
        this.movieService = movieService;

    }

    public List<Phim> timKiemPhim(String keyword) {
        return movieService.searchMovies(keyword);
    }
}