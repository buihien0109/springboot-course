package vn.techmaster.ecommecerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_server")
@Entity
public class FileServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String type;

    @Lob
    @Column(name = "data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] data;

    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
