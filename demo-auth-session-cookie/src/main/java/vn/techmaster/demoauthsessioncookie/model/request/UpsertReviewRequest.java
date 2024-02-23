package vn.techmaster.demoauthsessioncookie.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertReviewRequest {
    String content;
    Integer movieId;
}