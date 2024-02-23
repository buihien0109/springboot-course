const reviewContent = document.querySelector("#review-content");
const authorName = document.querySelector("#author-name");
const authorEmail = document.querySelector("#author-email");
const authorPhone = document.querySelector("#author-phone");

// click button to open modal review
const btnCreateReview = document.querySelector(".btn-create-review");
btnCreateReview.addEventListener("click", () => {
    $('#modal-review').modal('show');
})

$('#modal-review').on('hidden.bs.modal', function (event) {
    // clear review content
    authorName.value = "";
    authorEmail.value = "";
    authorPhone.value = "";
    reviewContent.value = "";

    // clear rating message
    ratingMessage.textContent = "";

    // reset stars
    resetStars();
})

// validate form
$('#form-review').validate({
    rules: {
        name: {
            required: true
        },
        phone: {
            required: true,
            pattern: /^(0\d{9}|(\+84|84)[1-9]\d{8})$/
        },
        email: {
            required: true,
            email: true
        },
        content: {
            required: true
        },
    },
    messages: {
        name: {
            required: "Họ tên không được để trống",
        },
        phone: {
            required: "Số điện thoại không được để trống",
            pattern: "Số điện thoại không đúng định dạng"
        },
        email: {
            required: "Email không được để trống",
            email: "Email không đúng định dạng"
        },
        content: {
            required: "Nội dung không được để trống",
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

/*-------------------
    Create review
--------------------- */
const createReview = () => {
    if (!$('#form-review').valid()) {
        return;
    }

    if (currentRating === 0) {
        toastr.warning("Vui lòng chọn số sao");
        return;
    }

    const review = {
        authorName : authorName.value,
        authorEmail : authorEmail.value,
        authorPhone : authorPhone.value,
        productId: product.productId,
        rating: currentRating,
        comment: reviewContent.value
    }

    // Send request to server using axios
    axios.post("/api/v1/public/reviews/anonymous", review)
        .then(res => {
            if (res.status === 200) {
                toastr.success("Cảm ơn bạn đã đánh giá sản phẩm. Đánh giá của bạn sẽ được hiển thị sau khi được kiểm duyệt");
                currentRating = 0;
                currentMessage = "";

                // close modal
                $('#modal-review').modal('hide');
            } else {
                toastr.error("Đánh giá thất bại");
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error(err.response.data.message);
        })
}

const btnSendReview = document.querySelector("#btn-send-review");
btnSendReview.addEventListener("click", () => {
    createReview(); // create review
})


