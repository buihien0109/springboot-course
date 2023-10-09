// hide/show password with icon in inputs using valina javascript
const togglePasswordBtns = document.querySelectorAll('.icon-toggle-password');
const passwordInputs = document.querySelectorAll('input[type="password"]');
togglePasswordBtns.forEach((btn, index) => {
    btn.addEventListener('click', () => {
        if (passwordInputs[index].type === 'password') {
            passwordInputs[index].type = 'text';
            btn.innerHTML = '<i class="fa fa-eye-slash" aria-hidden="true"></i>';
        } else {
            passwordInputs[index].type = 'password';
            btn.innerHTML = '<i class="fa fa-eye" aria-hidden="true"></i>';
        }
    });
});

// validation form
$('#customer-reset-password-form').validate({
    rules: {
        password: {
            required: true
        },
        confirmPassword: {
            required: true,
            equalTo: "#password"
        }
    },
    messages: {
        password: {
            required: "Mật khẩu không được để trống",
        },
        confirmPassword: {
            required: "Nhập lại mật khẩu không được để trống",
            equalTo: "Nhập lại mật khẩu không khớp"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

// handle change password form submit using axios
const resetPasswordForm = document.getElementById('customer-reset-password-form');
resetPasswordForm.addEventListener('submit', (e) => {
    e.preventDefault();

    if (!$('#customer-reset-password-form').valid()) {
        return;
    }

    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const request = {
        token: data.token,
        newPassword: password,
        confirmPassword
    };
    console.log(request)
    axios.post('/api/v1/public/auth/change-password', request)
        .then((res) => {
            if (res.status === 200) {
                toastr.success('Thay đổi mật khẩu thành công');

                setTimeout(() => {
                    window.location.href = '/dang-nhap';
                }, 1500);
            } else {
                toastr.error('Thay đổi mật khẩu thất bại');
            }
        })
        .catch((err) => {
            console.log(err);
            toastr.error(err.response.data.message);
        });
});