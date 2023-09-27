package vn.techmaster.ecommecerapp.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @NotNull(message = "Mật khẩu cũ không được để trống")
    @NotEmpty(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotNull(message = "Mật khẩu mới không được để trống")
    @NotEmpty(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    @NotNull(message = "Xác nhận mật khẩu không được để trống")
    @NotEmpty(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
}
