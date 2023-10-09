// validate form
$('#customer-reset-form').validate({
    rules: {
        email: {
            required: true,
            email: true
        }
    },
    messages: {
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

// handle reset form submit using axios using valina javascript
const resetForm = document.getElementById('customer-reset-form');
resetForm.addEventListener('submit', function (e) {
    e.preventDefault();

    if (!$('#customer-reset-form').valid()) {
        return;
    }

    const email = document.getElementById('email').value;
    axios.post(`/api/v1/public/auth/reset-password?email=${email}`)
        .then((response) => {
            if (response.status === 200) {
                toastr.success('Một email đã được gửi đến email của bạn. Vui lòng kiểm tra email để đặt lại mật khẩu');
            } else {
                toastr.error('Đã có lỗi xảy ra. Vui lòng thử lại sau');
            }
        })
        .catch((error) => {
            console.log(error);
            toastr.error(error.response.data.message);
        });
});
