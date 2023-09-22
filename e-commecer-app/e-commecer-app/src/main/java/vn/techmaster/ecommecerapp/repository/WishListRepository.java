package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.WishList;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
}