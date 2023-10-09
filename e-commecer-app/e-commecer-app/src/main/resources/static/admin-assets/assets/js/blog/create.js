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

// TODO : Chưa validate được nội dung của EasyMDE
easyMDE.codemirror.on("change", () => {
    document.getElementById("content").innerHTML = easyMDE.value();
});

$.validator.addMethod(
    "validateEasyMDE",
    function (value, element) {
        let text = easyMDE.value();
        return text !== "";
    },
    "Nội dung không được để trống"
);

$('#form-create-blog').validate({
    rules: {
        title: {
            required: true
        },
        editor: {
            validateEasyMDE: true
        },
        description: {
            required: true
        }
    },
    messages: {
        title: {
            required: "Tiêu đề không được để trống"
        },
        editor: {
            validateEasyMDE: "Nội dung không được để trống"
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
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

// Truy cập
const titleEl = document.getElementById("title");
const descriptionEl = document.getElementById("description");
const statusEl = document.getElementById("status");
const tagEl = $("#tag");
const btnCreate = document.getElementById("btn-create");

// create blog using axios and promise
btnCreate.addEventListener("click", () => {
    if (!$('#form-create-blog').valid()) {
        return;
    }

    const data = {
        title: titleEl.value,
        content: easyMDE.value(),
        description: descriptionEl.value,
        status: statusEl.value === "true",
        tagIds: tagEl.val().map(id => Number(id))
    };
    console.log(data)

    axios.post("/api/v1/admin/blogs", data)
        .then((res) => {
            toastr.success("Tạo bài viết thành công");
            setTimeout(() => {
                window.location.href = `/admin/blogs/${res.data.id}/detail`
            }, 1500);
        })
        .catch((err) => {
            console.log(err);
            toastr.error(err.response.data.message);
        });
});