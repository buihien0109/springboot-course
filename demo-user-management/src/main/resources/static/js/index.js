const API_URL = "/api/v1/users";

// Định nghĩa API lấy danh sách user
const userAPI = {
    getAllUsers(term) {
        return axios.get(API_URL, {params: {search: term}});
    },
    deleteUser(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
}

// Gọi API và hiển thị ra dữ liệu
async function getUsers(term) {
    try {
        let res = await userAPI.getAllUsers(term);
        if (res.status === 200) {
            users = res.data;
            renderUsers(users);
        } else {
            toastr.error("Lỗi khi lấy dữ liệu");
        }
    } catch (error) {
        console.log(error);
        toastr.error(error.response.data.message);
    }
}

// Truy cập vào các thành phần
const tableEl = document.querySelector("table");
const messageEl = document.querySelector(".message");
const tableBodyEl = document.querySelector("tbody");

// Hiển thi user ra ngoài giao diện
function renderUsers(arr) {
    // Kiểm tra nếu arr rỗng
    if (arr.length === 0) {
        messageEl.classList.remove("d-none");
        tableEl.classList.add("d-none");

        messageEl.innerHTML = "Danh sách user trống!"
        return;
    }

    // Nếu arr có phần tử
    messageEl.classList.add("d-none");
    tableEl.classList.remove("d-none");

    // Sử dụng vòng lặp để hiển thị dữ liệu
    tableBodyEl.innerHTML = "";
    for (let i = 0; i < arr.length; i++) {
        const u = arr[i];
        tableBodyEl.innerHTML += `
            <tr>
                <td>${i + 1}</td>
                <td>${u.name}</td>
                <td>${u.email}</td>
                <td>${u.phone}</td>
                <td>${u.address}</td>
                <td>
                    <a href="/users/${u.id}" class="btn btn-success">Xem chi tiết</a>
                    <button class="btn btn-danger" onclick="deleteUser(${u.id})">Xóa</button>
                </td>
            </tr>
        `;
    }
}

const deleteUser = async (id) => {
    try {
        const isConfirm = confirm("Bạn có muốn xóa không");
        if (!isConfirm) return;

        let res = await userAPI.deleteUser(id);
        if (res.status === 200) {
            users = users.filter(user => user.id !== id);
            renderUsers(users)
            toastr.success("Xóa thành công");
        } else {
            toastr.error("Xóa thất bại");
        }
    } catch (error) {
        console.log(error)
        toastr.error(error.response.data.message);
    }
};

const searchEl = document.getElementById("search");
searchEl.addEventListener("keydown", function (e) {
    if (e.keyCode === 13) {
        let term = e.target.value;
        getUsers(term);
    }
});