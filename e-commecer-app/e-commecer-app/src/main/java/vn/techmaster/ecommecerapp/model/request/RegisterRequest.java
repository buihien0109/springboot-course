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
public class RegisterRequest {
    @NotNull(message = "Name should not be null")
    @NotEmpty(message = "Name should not be empty")
    private String username;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email should not be null")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @NotNull(message = "Password should not be null")
    @NotEmpty(message = "Password should not be empty")
    private String password;
}
