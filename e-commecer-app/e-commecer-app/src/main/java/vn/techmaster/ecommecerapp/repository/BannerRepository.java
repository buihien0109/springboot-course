package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Banner;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    List<Banner> findByStatusTrueOrderByDisplayOrderAsc();

    List<Banner> findAllByStatus(Boolean status);

    List<Banner> findAllByStatusOrderByDisplayOrderAsc(Boolean status);
}