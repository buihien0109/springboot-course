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
$('#customer-register-form').validate({
    rules: {
        name: {
            required: true
        },
        email: {
            required: true,
            email: true
        },
        password: {
            required: true
        },
        confirmPassword: {
            required: true,
            equalTo: "#password"
        }
    },
    messages: {
        name: {
            required: "Tên người dùng không được để trống",
        },
        email: {
            required: "Email không được để trống",
            email: "Email không đúng định dạng"
        },
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

// handle register form submit using axios
const registerForm = document.getElementById('customer-register-form');
registerForm.addEventListener('submit', (e) => {
    e.preventDefault();

    if (!$('#customer-register-form').valid()) {
        return;
    }

    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const data = {
        username,
        email,
        password,
        confirmPassword
    };
    console.log(data)
    axios.post('/api/v1/public/auth/register', data)
        .then((res) => {
            if (res.status === 200) {
                toastr.success('Đăng ký thành công, một email xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra email để xác nhận tài khoản');
                setTimeout(() => {
                    window.location.href = '/dang-nhap';
                }, 1500);
            } else {
                toastr.error('Đăng ký thất bại');
            }
        })
        .catch((err) => {
            console.log(err);
            toastr.error(err.response.data.message);
        });
});