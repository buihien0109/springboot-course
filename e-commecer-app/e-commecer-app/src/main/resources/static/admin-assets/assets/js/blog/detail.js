//Initialize Select2 Elements
$(".select2").select2({
    placeholder: "Chọn danh mục",
});

function uploadFileIDE(file, onSuccess, onError) {
    console.log(file)
    const formData = new FormData();
    formData.append("file", file);

    axios.post("/api/v1/files", formData).then((res) => {
        if (res.status === 200) {
            const imageUrl = `/api/v1/files/${res.data.id}`; // URL của hình ảnh đã tải lên
            const cursorPosition = easyMDE.codemirror.getCursor(); // Lấy vị trí con trỏ
            const imageMarkdown = '!'; // Tạo đoạn Markdown cho hình ảnh
            easyMDE.codemirror.replaceRange(imageMarkdown, cursorPosition); // Chèn đoạn Markdown vào trình soạn thảo
            onSuccess(imageUrl);
        } else {
            onError("Lỗi khi tải lên hình ảnh");
        }
    }).catch((err) => {
        onError(err.response.data.message);
    });
}

// Initialize editor
const easyMDE = new EasyMDE({
    element: document.getElementById("content"),
    spellChecker: false,
    maxHeight: "350px",
    renderingConfig: {
        codeSyntaxHighlighting: true,
    },
    uploadImage: true,
    imageUploadFunction: uploadFileIDE
});

// Initialize data
const initData = (data) => {
    $("#title").val(data.title);
    easyMDE.value(data.content);
    $("#description").html(data.description);
    $("#status").val(String(data.status));
    $("#tag").val(data.tags.map(e => e.id)).trigger("change");
    $("#image").attr("src", data.thumbnail ? data.thumbnail : "https://placehold.co/1000x600");
};
initData(blog);

// TODO : Chưa validate được nội dung của EasyMDE
$.validator.addMethod(
    "easymdeContent",
    function (value, element) {
        // Check if the EasyMDE content is not empty
        // Lấy giá trị của textarea tương ứng với EasyMDE
        let textareaValue = easyMDE.value();

        // Kiểm tra xem nội dung của textarea có rỗng không
        return textareaValue.trim() !== '';
    },
    "Nội dung không được để trống"
);

$('#form-update-blog').validate({
    rules: {
        title: {
            required: true
        },
        content: {
            required: true,
            easymdeContent: true
        },
        description: {
            required: true
        }
    },
    messages: {
        title: {
            required: "Tiêu đề không được để trống"
        },
        content: {
            required: "Nội dung không được để trống",
            easymdeContent: "Nội dung không được để trống"
        },
        description: {
            required: "Mô tả không được để trống"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
        console.log(element)
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

// Handle event update blog and send request to server using axios
const btnUpdate = document.getElementById("btn-update");
btnUpdate.addEventListener("click", () => {
    if (!$('#form-update-blog').valid()) {
        return;
    }

    const title = $("#title").val();
    const content = easyMDE.value();
    const description = $("#description").val();
    const status = $("#status").val();
    const tagIds = $("#tag").val();
    const thumbnail = $("#image").attr("src");

    const data = {
        title,
        content,
        description,
        status: status === "true",
        tagIds: tagIds.map(e => Number(e)),
        thumbnail,
    };
    console.log(data)

    axios.put(`/api/v1/admin/blogs/${blog.id}`, data)
        .then(res => {
            toastr.success("Cập nhật thành công");
        })
        .catch(err => {
            console.log(err);
            toastr.error("Cập nhật thất bại");
        });
});

// Handle event delete blog and send request to server using axios and confirm before delete using window.confirm
const btnDelete = document.getElementById("btn-delete");
btnDelete.addEventListener("click", () => {
    if (window.confirm("Bạn có chắc chắn muốn xóa bài viết này?")) {
        axios.delete(`/api/v1/admin/blogs/${blog.id}`)
            .then(res => {
                toastr.success("Xóa thành công");
                setTimeout(() => {
                    window.location.href = "/admin/blogs/own-blogs";
                }, 1500);
            })
            .catch(err => {
                console.log(err);
                toastr.error("Xóa thất bại");
            });
    }
});