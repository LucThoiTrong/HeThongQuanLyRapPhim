package hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.pagination;

import hcmute.edu.vn.HeThongQuanLyRapPhim.model.Phim;
import hcmute.edu.vn.HeThongQuanLyRapPhim.service.MovieService;
import hcmute.edu.vn.HeThongQuanLyRapPhim.decorator.MovieServiceDecorator;

import java.util.Collections;
import java.util.List;

public class PaginatedMovieService extends MovieServiceDecorator {
    private final int page;
    private final int size;

    public PaginatedMovieService(MovieService decorated, int page, int size) {
        super(decorated);
        this.page = page;
        this.size = size;
    }

    @Override
    public List<Phim> getAllMovies() {
        List<Phim> all = super.getAllMovies();
        int fromIndex = page * size;
        if (fromIndex >= all.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(fromIndex + size, all.size());
        return all.subList(fromIndex, toIndex);
    }

    @Override
    public List<Phim> searchMovies(String keyword) {
        List<Phim> fullList = super.searchMovies(keyword);
        int fromIndex = page * size;
        if (fromIndex >= fullList.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(fromIndex + size, fullList.size());
        return fullList.subList(fromIndex, toIndex);
    }
}
