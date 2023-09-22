package vn.techmaster.ecommecerapp.model.request;


import jakarta.validation.constraints.Email;
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
public class LoginRequest {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    @NotNull(message = "Email should not be null")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @NotNull(message = "Password should not be null")
    private String password;
}
