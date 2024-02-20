package vn.techmaster.demoauthsessioncookie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.demoauthsessioncookie.entity.Movie;
import vn.techmaster.demoauthsessioncookie.repository.MovieRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
    }
}
