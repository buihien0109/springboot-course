package vn.techmaster.demoauthsessioncookie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.demoauthsessioncookie.service.MovieService;

@Controller
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @RequestMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movieList", movieService.getAllMovies());
        return "admin/movie/index";
    }
}
