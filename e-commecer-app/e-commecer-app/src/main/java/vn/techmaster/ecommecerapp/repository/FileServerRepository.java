package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.FileServer;

import java.util.List;

public interface FileServerRepository extends JpaRepository<FileServer, Long> {
    List<FileServer> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}