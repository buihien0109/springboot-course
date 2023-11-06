const API_URL = "/api/v1/users";

$('.select2').select2({
    placeholder: 'Chọn địa chỉ'
})

// Validate dữ liệu
$('#form-create-user').validate({
    rules: {
        name: {
            required: true
        },
        email: {
            required: true,
            email: true,
        },
        phone: {
            required: true,
            pattern: /^(0\d{9}|(\+84|84)[1-9]\d{8})$/
        },
        address: {
            required: true,
        },
        password: {
            required: true,
            minlength: 3
        }
    },
    messages: {
        name: {
            required: "Tên user không được để trống"
        },
        email: {
            required: "Email không được để trống",
            email: "Email không đúng định dạng"
        },
        phone: {
            required: "Số điện thoại không được để trống",
            pattern: "Số điện thoại không đúng định dạng",

        },
        address: {
            required: "Địa chỉ không được để trống",
        },
        password: {
            required: "Mật khẩu không được để trống",
            minlength: "Mật khẩu phải có ít nhất 3 ký tự"
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

// Xử lý thêm user
const nameEl = document.getElementById("name");
const emailEl = document.getElementById("email");
const phoneEl = document.getElementById("phone");
const addressEl = document.getElementById("address");
const passwordEl = document.getElementById("password");
const formCreateUserEl = document.getElementById("form-create-user");
formCreateUserEl.addEventListener("submit", async function (e) {
    e.preventDefault();
    if (!$('#form-create-user').valid()) return false;
    try {
        // Tạo object với dữ liệu đã được cập nhật
        let data = {
            name: nameEl.value,
            phone: phoneEl.value,
            email: emailEl.value,
            address: addressEl.value,
            password: passwordEl.value
        };

        // Gọi API để tạo
        let res = await axios.post(API_URL, data);
        if (res.status === 201) {
            toastr.success("Tạo user thành công!");
            setTimeout(() => {
                window.location.href = "/users";
            }, 1500)
        } else {
            toastr.error("Tạo user thất bại!");
        }
    } catch (error) {
        console.log(error)
        toastr.error(error.response.data.message);
    }
});

async function getProvince() {
    try {
        // Gọi API lấy danh sách tỉnh thành phố
        let res = await axios.get("https://provinces.open-api.vn/api/p/");

        // Render tỉnh thành phố
        renderProvince(res.data);
    } catch (error) {
        console.log(error);
    }
}

function renderProvince(arr) {
    for (let i = 0; i < arr.length; i++) {
        const p = arr[i];
        addressEl.innerHTML += `<option value="${p.name}">${p.name}</option>`
    }
}

getProvince();
