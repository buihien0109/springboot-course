package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class PaymentVoucherDto implements Serializable {
    private Long id;
    private String purpose;
    private String note;
    private Integer amount;
    private UserDto user;
    private Date createdAt;
    private Date updatedAt;

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.User}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserDto implements Serializable {
        private Long userId;
        private String username;
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