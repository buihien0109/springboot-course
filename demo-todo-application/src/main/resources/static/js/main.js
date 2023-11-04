// Truy cập vào các thành phần
const todoListEl = document.querySelector(".todo-list");
const inputEl = document.getElementById("todo-input");
const btnAdd = document.getElementById("btn-add");
const todoOptionEls = document.querySelectorAll(".todo-option-item input");

const API_URL = "/api/v1/todos"

// Khai báo biến
let idUpdate = null;

// ============== API ===============
// API lấy danh sách todo
function getTodosAPI(status) {
    switch (status) {
        case "all": {
            return axios.get(`${API_URL}`);
        }
        case "active": {
            return axios.get(`${API_URL}?status=true`);
        }
        case "unactive": {
            return axios.get(`${API_URL}?status=false`);
        }
        default: {
            return axios.get(`${API_URL}`);
        }
    }
}

// API thêm công việc
function createTodoAPI(title) {
    return axios.post(`${API_URL}`, {
        title: title
    });
}

// API xóa công việc
function deleteTodoAPI(id) {
    return axios.delete(`${API_URL}/${id}`)
}

// API cập nhật công việc
function updateTodoAPI(todo) {
    return axios.put(`${API_URL}/${todo.id}`, {
        title: todo.title,
        status: todo.status
    });
}


// Render UI - Hiển thị danh sách todo ra ngoài giao diện
function renderTodos(arr) {
    todoListEl.innerHTML = "";

    // Kiểm tra mảng rỗng
    if (arr.length === 0) {
        todoListEl.innerHTML = "<p class='todos-empty'>Không có công việc nào trong danh sách</p>";
        return;
    }

    // Trường hợp có công việc
    for (let i = 0; i < arr.length; i++) {
        const t = arr[i];
        todoListEl.innerHTML += `
            <div class="todo-item ${t.status ? "active-todo" : ""}">
                <div class="todo-item-title">
                    <input 
                        type="checkbox" 
                        ${t.status ? "checked" : ""}
                        onclick="toggleStatus(${t.id})"
                    />
                    <p>${t.title}</p>
                </div>
                <div class="option">
                    <button class="btn btn-update" onclick="updateTitle(${t.id})">
                        <img src="./img/pencil.svg" alt="icon" />
                    </button>
                    <button class="btn btn-delete" onclick=deleteTodo(${t.id})>
                        <img src="./img/remove.svg" alt="icon" />
                    </button>
                </div>
            </div>
        `;
    }
}

// ============= Hàm xử lý =============
// Lấy danh sách todo
async function getTodos(status) {
    try {
        const res = await getTodosAPI(status);
        todos = res.data;

        // Render ra ngoài giao diện
        renderTodos(todos);
    } catch (error) {
        console.log(error);
    }
}

// Hàm xử lý việc thêm
async function createTodo(title) {
    try {
        const res = await createTodoAPI(title);
        todos.push(res.data)

        // Render ra ngoài giao diện
        renderTodos(todos);
    } catch (error) {
        console.log(error);
    }
}

// Hàm xử lý việc xóa
async function deleteTodo(id) {
    try {
        await deleteTodoAPI(id)
        todos = todos.filter((todo) => todo.id !== id);
        renderTodos(todos)

    } catch (error) {
        console.log(error);
    }
}

// Hàm xử lý thay đổi trạng thái công việc
async function toggleStatus(id) {
    try {
        let todo = todos.find((todo) => todo.id === id);
        todo.status = !todo.status;
        let res = await updateTodoAPI(todo);
        renderTodos(todos);
    } catch (error) {
        console.log(error);
    }
}

// Hàm xử lý thay đổi tiêu đề công việc
async function updateTodo(todoUpdate) {
    try {
        let res = await updateTodoAPI(todoUpdate);

        todos.forEach((todo, index) => {
            if (todo.id === todoUpdate.id) {
                todos[index] = res.data;
            }
        });

        btnAdd.innerText = "Thêm";
        idUpdate = null;

        renderTodos(todos);
    } catch (error) {
        console.log(error);
    }
}

// Lấy giá trị trong 1 ô input radio
function getOptionSelected() {
    for (let i = 0; i < todoOptionEls.length; i++) {
        if (todoOptionEls[i].checked) {
            return todoOptionEls[i].value;
        }
    }
}

todoOptionEls.forEach((btn) => {
    btn.addEventListener("change", function () {
        let optionSelected = getOptionSelected();
        getTodos(optionSelected);
    });
});

// Thêm công việc và cập nhật tiêu đề công việc
btnAdd.addEventListener("click", function () {
    let todoTitle = inputEl.value;
    if (todoTitle === "") {
        alert("Nội dung không được để trống!");
        return;
    }
    if (idUpdate) {
        let todo = todos.find((todo) => todo.id === idUpdate);
        todo.title = todoTitle;

        updateTodo(todo);
    } else {
        createTodo(todoTitle);
    }

    inputEl.value = "";
});

// Cập nhật tiêu đề todo
function updateTitle(id) {
    let title;
    todos.forEach((todo) => {
        if (todo.id === id) {
            title = todo.title;
        }
    });

    btnAdd.innerText = "CẬP NHẬT";

    inputEl.value = title;
    inputEl.focus();

    idUpdate = id;
}