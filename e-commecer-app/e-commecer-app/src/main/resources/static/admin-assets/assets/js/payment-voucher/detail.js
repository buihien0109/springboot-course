// convert price to number when user submit
const formatPrice = (price) => {
    return parseInt(price.replace(/,/g, ""));
}

// Validate form
$.validator.addMethod(
    "validateMinValue",
    function (value, element) {
        // get value of type
        return formatPrice(value) > 0
    },
    "Giá trị phải lớn hơn 0"
);

$.validator.addMethod(
    "validateInteger",
    function (value, element) {
        // check if value is number
        return Number.isInteger(Number(formatPrice(value)))
    },
    "Giá trị phải là số nguyên"
);

$('#form-update-payment-voucher').validate({
    rules: {
        purpose: {
            required: true
        },
        amount: {
            required: true,
            validateInteger: true,
            validateMinValue: true,
        },
        user: {
            required: true
        }
    },
    messages: {
        purpose: {
            required: "Mục đích chi không được để trống"
        },
        amount: {
            required: "Số tiền không được để trống",
            validateInteger: "Số tiền phải là số nguyên",
            validateMinValue: "Số tiền phải lớn hơn 0",
        },
        user: {
            required: "Người tạo không được để trống"
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

// Truy cập vào các thành phần
const purposeEl = document.getElementById("purpose");
const amountEl = document.getElementById("amount");
const noteEl = document.getElementById("note");
const userEl = document.getElementById("user");
const btnUpdate = document.getElementById("btn-update")

// format amount when user input
amountEl.addEventListener('keyup', function (event) {
    const value = event.target.value;
    event.target.value = value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
});

// convert price to number when user submit
amountEl.value = paymentVoucher.amount.toString()
    .replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");

btnUpdate.addEventListener("click", async () => {
    if ($('#form-update-payment-voucher').valid() === false) return

    const data = {
        purpose: purposeEl.value,
        amount: formatPrice(amountEl.value),
        note: noteEl.value,
        userId: Number(userEl.value)
    }

    try {
        let res = await axios.put(`/api/v1/admin/payment_vouchers/${paymentVoucher.id}`, data)

        if (res.status === 200) {
            toastr.success("Cập nhật phiếu chi thành công");
        } else {
            toastr.error("Cập nhật phiếu chi thất bại");
        }
    } catch (e) {
        console.log(e);
        toastr.error(e.response.data.message);
    }
})

const btnDelete = document.getElementById("btn-delete");
btnDelete.addEventListener("click", () => {
    if (window.confirm("Bạn có chắc chắn muốn xóa phiếu chi này?")) {
        axios.delete(`/api/v1/admin/payment_vouchers/${paymentVoucher.id}`)
            .then(res => {
                toastr.success("Xóa thành công");
                setTimeout(() => {
                    window.location.href = "/admin/payment_vouchers";
                }, 1500);
            })
            .catch(err => {
                console.log(err);
                toastr.error(err.response.data.message);
            });
    }
});