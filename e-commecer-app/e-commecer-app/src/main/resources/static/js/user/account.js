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

// add event listener to avatar input change
const avatarInput = document.getElementById('avatar');
const avatarPreview = document.getElementById('avatar-preview');
avatarInput.addEventListener('change', (e) => {
    const file = e.target.files[0];

    // create form data with key file and value is file in input
    const formData = new FormData();
    formData.append('file', file);

    // Send post request to url /api/v1/users/update-avatar, then check request status and set src for avatar preview
    axios.post('/api/v1/users/update-avatar', formData)
        .then(res => {
            if (res.status === 200) {
                avatarPreview.src = res.data;
                toastr.success('Cập nhật avatar thành công');
            }
        })
        .catch(err => {
            console.log(err)
            toastr.error('Cập nhật avatar thất bại');
        });
});

// validation form update profile
$('#form-update-profile').validate({
    rules: {
        username: {
            required: true
        },
        phone: {
            required: true,
            pattern: /^(0\d{9}|(\+84|84)[1-9]\d{8})$/
        },
        email: {
            required: true,
            email: true
        }
    },
    messages: {
        username: {
            required: "Họ tên không được để trống",
        },
        phone: {
            required: "Số điện thoại không được để trống",
            pattern: "Số điện thoại không đúng định dạng"
        },
        email: {
            required: "Email không được để trống",
            email: "Email không đúng định dạng"
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

// add event listener to update profile form
const formUpdateProfile = document.getElementById('form-update-profile');
formUpdateProfile.addEventListener('submit', (e) => {
    e.preventDefault();

    if (!$('#form-update-profile').valid()) {
        return;
    }

    const username = document.querySelector('#username').value;
    const phone = document.querySelector('#phone').value;

    // create data with key username and value is username in input
    const data = {
        username: username,
        phone: phone
    };

    // Send post request to url /api/v1/users/update-profile, then check request status and set src for avatar preview
    axios.put('/api/v1/users/update-profile', data)
        .then(res => {
            if (res.status === 200) {
                toastr.success('Cập nhật thông tin thành công');
            }
        })
        .catch(err => {
            console.log(err)
            toastr.error('Cập nhật thông tin thất bại');
        });
});

// validation form update password
$('#form-update-password').validate({
    rules: {
        oldPassword: {
            required: true
        },
        newPassword: {
            required: true
        },
        confirmPassword: {
            required: true,
            equalTo: "#newPassword"
        }
    },
    messages: {
        oldPassword: {
            required: "Mật khẩu cũ không được để trống"
        },
        newPassword: {
            required: "Mật khẩu mới không được để trống"
        },
        confirmPassword: {
            required: "Mật khẩu xác nhận không được để trống",
            equalTo: "Mật khẩu xác nhận không khớp"

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

// add event listener to update password form
const formUpdatePassword = document.getElementById('form-update-password');
formUpdatePassword.addEventListener('click', (e) => {
    e.preventDefault();

    if (!$('#form-update-password').valid()) {
        return;
    }

    const oldPassword = document.querySelector('#old-password').value;
    const newPassword = document.querySelector('#new-password').value;
    const confirmPassword = document.querySelector('#confirm-password').value;

    // create data with key username and value is username in input
    const data = {
        oldPassword: oldPassword,
        newPassword: newPassword,
        confirmPassword: confirmPassword
    };

    // Send post request to url /api/v1/users/update-password, then check request status and set src for avatar preview
    axios.put('/api/v1/users/update-password', data)
        .then(res => {
            if (res.status === 200) {
                toastr.success('Cập nhật mật khẩu thành công');

                // reset all password input
                document.querySelector('#old-password').value = '';
                document.querySelector('#new-password').value = '';
                document.querySelector('#confirm-password').value = '';
            }
        })
        .catch(err => {
            console.log(err)
            showError(err.response.data.message);
        });
});