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
const resetForm = document.getElementById('customer-reset');
resetForm.addEventListener('submit', function (e) {
    e.preventDefault();

    if (!$('#customer-reset-form').valid()) {
        return;
    }

    const email = document.getElementById('email').value;
    const data = {
        email: email
    };
    axios.post('/api/v1/auth/reset-password', data)
        .then(function (response) {
            console.log(response);
            toastr.success('Vui lòng kiểm tra email để lấy lại mật khẩu');
        })
        .catch(function (error) {
            console.log(error);
            toastr.error('Email không tồn tại');
        });
});
