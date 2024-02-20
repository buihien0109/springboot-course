package vn.techmaster.demoauthsessioncookie.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.techmaster.demoauthsessioncookie.auth.AuthUtils;
import vn.techmaster.demoauthsessioncookie.entity.User;
import vn.techmaster.demoauthsessioncookie.service.MovieService;
import vn.techmaster.demoauthsessioncookie.service.ReviewService;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final AuthUtils authUtils;
    private final HttpServletRequest request;

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("movieList", movieService.getAllMovies());
        return "web/index";
    }

    @GetMapping("/phim/{id}")
    public String getChiTietPhim(Model model, @PathVariable Integer id) {
        model.addAttribute("movie", movieService.getMovieById(id));
        model.addAttribute("reviewList", reviewService.getReviewsOfMovie(id));
        return "web/chi-tiet-phim";
    }

    @GetMapping("/dang-nhap")
    public String getLogin() {
        User user = authUtils.getLoginedUser(request);
        if (user != null) {
            return "redirect:/";
        }
        return "web/dang-nhap";
    }
}
