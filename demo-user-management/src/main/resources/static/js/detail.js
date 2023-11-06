const API_URL = "/api/v1/users";

$('.select2').select2({
    placeholder: 'Chọn địa chỉ'
})

// Validate form cập nhật thông tin user
$('#form-update-user').validate({
    rules: {
        name: {
            required: true
        },
        phone: {
            required: true,
            pattern: /^(0\d{9}|(\+84|84)[1-9]\d{8})$/
        },
        address: {
            required: true,
        }
    },
    messages: {
        name: {
            required: "Tên user không được để trống"
        },
        phone: {
            required: "Số điện thoại không được để trống",
            pattern: "Số điện thoại không đúng định dạng",

        },
        address: {
            required: "Địa chỉ không được để trống",
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

// validate form đổi mật khẩu
$('#form-change-password').validate({
    rules: {
        oldPassword: {
            required: true,
            minlength: 3
        },
        newPassword: {
            required: true,
            minlength: 3
        }
    },
    messages: {
        oldPassword: {
            required: "Mật khẩu cũ không được để trống",
            minlength: "Mật khẩu cũ phải có ít nhất 3 ký tự"
        },
        newPassword: {
            required: "Mật khẩu mới không được để trống",
            minlength: "Mật khẩu mới phải có ít nhất 3 ký tự"
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

// Xử lý cập nhật thông tin user
const nameEl = document.getElementById("name");
const phoneEl = document.getElementById("phone");
const addressEl = document.getElementById("address");
const imageEl = document.getElementById("image");
const formUpdateUserEl = document.getElementById("form-update-user");
formUpdateUserEl.addEventListener("submit", async function (e) {
    e.preventDefault();
    if (!$('#form-update-user').valid()) return false;
    try {
        // Tạo object với dữ liệu đã được cập nhật
        let data = {
            name: nameEl.value,
            phone: phoneEl.value,
            address: addressEl.value,
            avatar: imageEl.src !== "https://via.placeholder.com/200" ? imageEl.src : null
        };

        // Gọi API để cập nhật
        let res = await axios.put(`${API_URL}/${user.id}`, data);
        if (res.status === 200) {
            toastr.success("Cập nhật thông tin thành công!");
        } else {
            toastr.error("Cập nhật thông tin thất bại!");
        }
    } catch (error) {
        console.log(error)
        toastr.error(error.response.data.message);
    }
});

// Đổi mật khẩu
const oldPasswordEl = document.getElementById("old-password");
const newPasswordEl = document.getElementById("new-password");
const btnChangePassword = document.getElementById("btn-change-password");

const modalChangePasswordEl = document.getElementById('modal-change-password');
const modalChangePasswordConfig = new bootstrap.Modal(modalChangePasswordEl, {
    keyboard: false
})

// Thay đổi mật khẩu
btnChangePassword.addEventListener("click", async function (e) {
    e.preventDefault();
    if (!$('#form-change-password').valid()) return false;
    try {
        let oldPasswordValue = oldPasswordEl.value;
        let newPasswordValue = newPasswordEl.value;

        let data = {
            oldPassword: oldPasswordValue,
            newPassword: newPasswordValue,
        };

        let res = await axios.put(`${API_URL}/${user.id}/update-password`, data);
        if (res.status === 204) {
            modalChangePasswordConfig.hide();
            oldPasswordEl.value = "";
            newPasswordEl.value = ""
            toastr.success("Đổi mật khẩu thành công!");
        } else {
            toastr.error("Đổi mật khẩu thất bại!");
        }
    } catch (error) {
        console.log(error)
        toastr.error(error.response.data.message);
    }
});

// Quên mật khẩu
const btnForgotPassword = document.getElementById("btn-forgot-password");
btnForgotPassword.addEventListener("click", async function () {
    try {
        let res = await axios.post(`${API_URL}/${user.id}/forgot-password`)
        if (res.status === 200) {
            toastr.success(res.data);
        } else {
            toastr.error("Có lỗi xảy ra!");
        }
    } catch (error) {
        console.log(error)
        toastr.error(error.response.data.message);
    }
})

// API lấy danh sách tỉnh - thành phố
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

// Hiển thị thông tin tỉnh - thành phố lên trên giao diện
function renderProvince(arr) {
    for (let i = 0; i < arr.length; i++) {
        const p = arr[i];
        addressEl.innerHTML += `<option value="${p.name}">${p.name}</option>`
    }
}

const btnModalImage = document.getElementById("btn-modal-image");
const btnChoseImage = document.getElementById("btn-chose-image");
const inputImageEl = document.getElementById("input-image");
const btnDeleteImage = document.getElementById("btn-delete-image");
const imageContainerEl = document.querySelector(".image-container");

let imageList = [];

const modalImageEl = document.getElementById('modal-image')
modalImageEl.addEventListener('hidden.bs.modal', function (event) {
    btnChoseImage.disabled = true;
    btnDeleteImage.disabled = true;
})

const modalImageElConfig = new bootstrap.Modal(modalImageEl, {
    keyboard: false
})

btnModalImage.addEventListener("click", async () => {
    try {
        // Goi API
        const res = await axios.get(`${API_URL}/${user.id}/files`);

        // Lưu lại kết quả trả về từ server
        imageList = res.data;

        // Hien thi hinh anh
        renderPagination(imageList);
    } catch (err) {
        console.log(err)
    }
})

// Hiển thị danh sách image
function renderImages(imageList) {
    // Xóa hết nd đang có trước khi render
    imageContainerEl.innerHTML = "";

    // Tạo nd
    let html = ""
    imageList.forEach(image => {
        html += `
            <div class="image-item" onclick="choseImage(this)" data-url="${image}">
                <img src="${image}" alt="">
            </div>
        `
    })

    // Insert nd
    imageContainerEl.innerHTML = html;
    btnChoseImage.disabled = true;
    btnDeleteImage.disabled = true;
}

// Hiển thị phần phân trang
function renderPagination(imageList) {
    $('.image-pagination-container').pagination({
        dataSource: imageList,
        pageSize: 12,
        callback: function (data) {
            renderImages(data);
        }
    })
}

// Chọn ảnh
function choseImage(ele) {
    // Xóa hết selected
    const imageSelected = document.querySelector(".image-item.selected");
    if (imageSelected) {
        imageSelected.classList.remove("selected")
    }

    // Thêm selected
    ele.classList.add("selected");

    // Active 2 nút chức năng
    btnChoseImage.disabled = false;
    btnDeleteImage.disabled = false;
}

// Xóa ảnh
btnDeleteImage.addEventListener("click", async () => {
    try {
        const imageSelected = document.querySelector(".image-item.selected");
        const url = imageSelected.dataset.url

        // Xóa trên server
        await axios.delete(`${url}`)

        // Xóa trong mảng ban đầu
        imageList = imageList.filter(imageUrl => imageUrl !== url);
        renderPagination(imageList);

        // Disable 2 nút chức năng
        btnChoseImage.disabled = true;
        btnDeleteImage.disabled = true;
    } catch (e) {
        console.log(e)
    }
})

// Chọn ảnh Preview
btnChoseImage.addEventListener("click", () => {
    // Preview ảnh
    const imageSelected = document.querySelector(".image-item.selected");
    const url = imageSelected.querySelector("img").getAttribute("src");
    imageEl.src = url;

    // Đóng modal
    modalImageElConfig.hide();

    // Disable 2 nút chức năng
    btnChoseImage.disabled = true;
    btnDeleteImage.disabled = true;
})

// Upload file
inputImageEl.addEventListener("change", (event) => {
    const file = event.target.files[0]

    // Tạo đối tượng formData
    const formData = new FormData();
    formData.append("file", file)

    // Gọi API
    axios.post(`${API_URL}/${user.id}/upload-file`, formData)
        .then(res => {
            imageList.unshift(res.data);
            renderPagination(imageList);
        })
        .catch(err => {
            console.log(err)
            toastr.error(err.response.data.message)
        })
})

// Hiển thị thông tin user lên trên giao diện
const renderUser = (user) => {
    $("#address").val(user.address).trigger("change");

    // Nếu user không có avatar thì lấy avatar mặc định
    if (user.avatar) {
        imageEl.src = user.avatar;
    } else {
        imageEl.src = "https://via.placeholder.com/200"
    }
};


async function init() {
    await getProvince();
}

init().then(() => renderUser(user));