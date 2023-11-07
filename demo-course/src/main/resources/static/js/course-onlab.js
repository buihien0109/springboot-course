const API_URL = "/api/v1";
let courseList = [];
let itemPerPage = 9;

const getParams = () => {
    let params = {
        name: null,
        topic: null
    }

    const searchParams = new URLSearchParams(window.location.search);
    if (!searchParams) {
        return params;
    }

    for (const key in params) {
        if (searchParams.has(key)) {
            params[key] = searchParams.get(key)
        } else {
            params[key] = null;
        }
    }

    return params
}
let params = getParams();

// Danh sách function API
const getAllCourseApi = (params) => {
    console.log("call")
    console.log({ ...params, type: "onlab" })
    return axios.get(`${API_URL}/courses`, {params : { ...params, type: "onlab" }})
}

const getAllTopicApi = () => {
    return axios.get(`${API_URL}/topics`)
}

// Gọi API lấy ds khóa học và hiển thị
const getAllCourse = async (params) => {
    try {
        let res = await getAllCourseApi(params);
        if (res.status === 200) {
            courseList = res.data;
            renderPagination(courseList, itemPerPage);
            changeURL(params);
        }
    } catch (error) {
        console.log(error);
    }
}

// Hiển thị ds khóa học
const courseListEl = document.querySelector(".course-list");
const renderCourses = (courseList) => {
    if (courseList.length === 0) {
        courseListEl.innerHTML = `
            <h3 class="mt-3">Không có khóa học!</h3>
        `;
        return
    }
    let html = "";
    courseList.forEach(course => {
        html += `
            <div class="col-md-4">
                <a href="/khoa-hoc/${course.id}">
                    <div class="course-item shadow-sm rounded mb-4">
                        <div class="course-item-image">
                            <img src="${course.thumbnail}"
                                alt="${course.name}">
                        </div>
                        <div class="course-item-info p-3">
                            <h2 class="fs-5 mb-3 text-dark">${course.name}</h2>
                            <p class="type fw-light text-black-50">${course.type === "online" ? "Trực tuyến" : "Phòng lab"}</p>
                        </div>
                    </div>
                </a>
            </div>
        `
    });
    courseListEl.innerHTML = html;
}

// Hiển thị phần phân trang
function renderPagination(courseList, pageSize) {
    $('#pagination').pagination({
        dataSource: courseList,
        pageSize: pageSize,
        className: 'paginationjs-big',
        callback: function (data) {
            renderCourses(data);
        },
        hideOnlyOnePage: true
    })
}

// Gọi API lấy ds topic và hiển thị
const getAllTopic = async () => {
    try {
        let res = await getAllTopicApi();
        renderTopics(res.data);
    } catch (error) {
        console.log(error);
    }
}

// Hiển thị danh sách topic
const topicListEl = document.querySelector(".topic-list");
const renderTopics = (topicList) => {
    let html = "";
    topicList.forEach(topic => {
        html += `
            <div class="topic-item input-group d-flex align-items-center mb-1">
                <input type="radio" value="${topic}" id="${topic}" name="topic">
                <label for="${topic}" class="ms-2 fs-5">${topic}</label>
            </div>
        `
    });
    topicListEl.innerHTML = html;
}

// Xử lý tìm kiếm khóa học theo tiêu đề
const searchEl = document.querySelector(".seach-form-input");
searchEl.addEventListener("search", (e) => {
    if (!e.target.value.length) {
        params = { ...params, name: null };
    } else {
        let searchTerm = e.target.value;
        params = { ...params, name: searchTerm };
    }
    getAllCourse(params);
})

getAllCourse(params);
getAllTopic().then(() => {
    // Xử lý lọc theo topic
    const topicItemEls = document.querySelectorAll(".topic-item");
    topicItemEls.forEach(el => {
        el.addEventListener("change", (e) => {
            btnUnFilter.classList.remove("d-none");
            let value = e.target.value;
            params = { ...params, topic: value };
            getAllCourse(params);
        })
    })

    // Xử lý bỏ lọc
    const btnUnFilter = document.querySelector(".btn-unfilter");
    btnUnFilter.addEventListener("click", () => {
        topicItemEls.forEach(el => el.querySelector("input").checked = false);
        btnUnFilter.classList.add("d-none");

        params = { ...params, topic: null };
        getAllCourse(params);
    })

    // Trigger các ô topic, search ban đầu
    const triggerData = (params) => {
        if (params["topic"]) {
            const currentTopicEl = document.getElementById(`${params["topic"]}`);
            currentTopicEl.checked = true;
            btnUnFilter.classList.remove("d-none");
        }
        if (params["name"]) {
            searchEl.value = params["name"];
        }
    }
    triggerData(params);
});

// Thay đổi URL nếu người dùng tìm kiếm theo topic hay name
const changeURL = (params) => {
    const url = new URL(location);
    for (const key in params) {
        if (params[key]) {
            url.searchParams.set(key, params[key]);
        } else {
            url.searchParams.delete(key);
        }
    }

    history.pushState({}, "", url);
}

// Hiển thị số lượng item trên 1 trang
const itemPerPageEl = document.querySelector(".item-per-page");
itemPerPageEl.addEventListener("change", (e) => {
    const value = e.target.value;
    if (Number(value)) {
        itemPerPage = Number(value);
    } else {
        itemPerPage = courseList.length;
    }
    renderPagination(courseList, itemPerPage)
})

