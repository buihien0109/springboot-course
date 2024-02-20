package vn.techmaster.demoauthsessioncookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.demoauthsessioncookie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}