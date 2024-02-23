package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.PaymentVoucher}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentVoucherDto implements Serializable {
    Long id;
    String purpose;
    String note;
    Integer amount;
    UserDto user;
    Date createdAt;
    Date updatedAt;

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.User}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserDto implements Serializable {
        Long userId;
        String username;
    }

    public PaymentVoucherDto(Long id, String purpose, String note, Integer amount, String user, String createdAt, String updatedAt) {
        this.id = id;
        this.purpose = purpose;
        this.note = note;
        this.amount = amount;
        this.createdAt = DateUtils.parseDate(createdAt);
        this.updatedAt = DateUtils.parseDate(updatedAt);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.user = objectMapper.readValue((String) user, PaymentVoucherDto.UserDto.class);
        } catch (IOException e) {
            this.user = null;
        }
    }
}