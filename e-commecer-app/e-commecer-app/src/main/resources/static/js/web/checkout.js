let couponCode = null;
let couponDiscount = null;

// Hiển thị danh sách province vào select id="province"
const provinceSelect = document.getElementById('province');
const districtSelect = document.getElementById('district');
const wardSelect = document.getElementById('ward');

const renderProvince = (provinces) => {
    console.log(provinces)
    provinceSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Tỉnh/Thành phố</option>';
    districtSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Quận/Huyện</option>';
    wardSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Xã/Phường</option>';

    provinces.forEach(province => {
        const {code, name} = province;
        const option = document.createElement('option');
        option.value = code;
        option.innerText = name;
        option.setAttribute('data-province-name', name);
        provinceSelect.appendChild(option);
    });
}

const renderDistrict = (districts) => {
    districtSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Quận/Huyện</option>';
    districts.forEach(district => {
        const {code, name} = district;
        const option = document.createElement('option');
        option.value = code;
        option.innerText = name;
        option.setAttribute('data-district-name', name);
        districtSelect.appendChild(option);
    });
}

const renderWard = (wards) => {
    wardSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Xã/Phường</option>';
    wards.forEach(ward => {
        const {code, name} = ward;
        const option = document.createElement('option');
        option.value = code;
        option.innerText = name;
        option.setAttribute('data-ward-name', name);
        wardSelect.appendChild(option);
    });
}

const getProvinces = async () => {
    try {
        const response = await axios.get('/api/v2/public/address/provinces');
        if (response.status === 200) {
            const {data} = response;
            renderProvince(data);
            districtSelect.disabled = true;
            wardSelect.disabled = true;
        }
    } catch (error) {
        console.log(error);
        toastr.error('Không thể lấy danh sách tỉnh/thành phố');
    }
}

provinceSelect.addEventListener('change', (event) => {
    const provinceCode = event.target.value;
    console.log(provinceCode)
    getDistricts(provinceCode);
    districtSelect.disabled = false;
    wardSelect.disabled = true;
    wardSelect.innerHTML = '<option hidden="hidden">-- Chọn Xã/Phường</option>';
});

const getDistricts = async (provinceId) => {
    try {
        const response = await axios.get(`/api/v2/public/address/districts?provinceCode=${provinceId}`);
        if (response.status === 200) {
            const {data} = response;
            renderDistrict(data);
        }
    } catch (error) {
        console.log(error);
        toastr.error('Không thể lấy danh sách quận/huyện');
    }
}

districtSelect.addEventListener('change', (event) => {
    const districtId = event.target.value;
    getWards(districtId);
    wardSelect.disabled = false;
});

const getWards = async (districtId) => {
    try {
        const response = await axios.get(`/api/v2/public/address/wards?districtCode=${districtId}`);
        if (response.status === 200) {
            const {data} = response;
            renderWard(data);
        }
    } catch (error) {
        console.log(error);
        toastr.error('Không thể lấy danh sách xã/phường');
    }
}

// chose address
const fillAddress = async (address) => {
    document.getElementById("address").value = address.detail;

    provinceSelect.querySelector(`option[data-province-name="${address.province}"]`).selected = true;
    await getDistricts(provinceSelect.value);

    districtSelect.querySelector(`option[data-district-name="${address.district}"]`).selected = true;
    await getWards(districtSelect.value);

    wardSelect.querySelector(`option[data-ward-name="${address.ward}"]`).selected = true;

    // enable district and ward
    districtSelect.disabled = false;
    wardSelect.disabled = false;
}

const chooseAddress = async (index) => {
    const address = addressList[index];
    await fillAddress(address);
    $('#modal-address').modal('hide');
}

// get TemporaryAmount
const getTemporaryAmount = () => {
    let cartItems = cart.cartItems;
    let temporaryAmountValue = 0;
    cartItems.forEach(ct => {
        if (ct.product.discountPrice != null) {
            temporaryAmountValue += ct.product.discountPrice * ct.quantity;
        } else {
            temporaryAmountValue += ct.product.price * ct.quantity;
        }
    })
    return temporaryAmountValue;
}

// get DiscountAmount
const getDiscountAmount = () => {
    if (couponDiscount == null) {
        return 0;
    }
    return getTemporaryAmount() * couponDiscount / 100;
}

// get TotalAmount = TemporaryAmount - DiscountAmount
const getTotalAmount = () => {
    return getTemporaryAmount() - getDiscountAmount();
}

// Tính thành tiền, tổng tiền
const displayTotalPrice = () => {
    const temporaryAmount = document.querySelector('.temporary-amount');
    const discountAmount = document.querySelector('.discount-amount');
    const totalAmount = document.querySelector('.total-amount');

    // display to UI
    temporaryAmount.innerText = formatCurrency(getTemporaryAmount());
    discountAmount.innerText = formatCurrency(getDiscountAmount());
    totalAmount.innerText = formatCurrency(getTotalAmount());
}

// check coupon
const couponInput = document.getElementById('coupon-input');
const btnApplyCoupon = document.querySelector('.btn-apply-coupon');
btnApplyCoupon.addEventListener('click', async () => {
    const couponCodeInput = couponInput.value;
    if (couponCodeInput === '') {
        toastr.error('Vui lòng nhập mã giảm giá');
        return;
    }
    try {
        const response = await axios.get(`/api/v1/public/coupons/check?couponCode=${couponCodeInput}`);
        if (response.status === 200) {
            const {data} = response;
            couponCode = data.code;
            couponDiscount = data.discount;
            toastr.success('Áp dụng mã giảm giá thành công');
        } else {
            toastr.error('Mã giảm giá không hợp lệ');
            couponCode = null;
            couponDiscount = null;
        }
    } catch (error) {
        console.log(error);
        toastr.error('Mã giảm giá không hợp lệ');
        couponCode = null;
        couponDiscount = null;
    } finally {
        couponInput.value = '';
        displayTotalPrice();
    }
})

// validate form checkout
$('#form-checkout').validate({
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
        province: {
            required: true
        },
        district: {
            required: true
        },
        ward: {
            required: true
        },
        address: {
            required: true
        }
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
        province: {
            required: "Tỉnh/Thành phố không được để trống",
        },
        district: {
            required: "Quận/Huyện không được để trống",
        },
        ward: {
            required: "Xã/Phường không được để trống",
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

// handle submit order
const btnSubmitOrder = document.getElementById('btn-submit-order');
btnSubmitOrder.addEventListener("click", () => {
    if (!$('#form-checkout').valid()) {
        return;
    }

    const username = document.getElementById('username').value;
    const phone = document.getElementById('phone').value;
    const email = document.getElementById('email').value;
    const address = document.getElementById('address').value;
    const note = document.getElementById('note').value;
    const shippingMethod = document.querySelector('#shipping-method input[type="radio"]:checked').value;
    const paymentMethod = document.querySelector('#payment-method input[type="radio"]:checked').value;
    const items = cart.cartItems.map(cartItem => {
        return {
            productId: cartItem.product.productId,
            quantity: cartItem.quantity,
            price: cartItem.product.discountPrice != null ? cartItem.product.discountPrice : cartItem.product.price
        }
    })

    // get province selected and get province name by attribute data-province-name
    const provinceSelected = provinceSelect.options[provinceSelect.selectedIndex];
    const provinceName = provinceSelected.getAttribute('data-province-name');

    // get district selected and get district name by attribute data-district-name
    const districtSelected = districtSelect.options[districtSelect.selectedIndex];
    const districtName = districtSelected.getAttribute('data-district-name');

    // get ward selected and get ward name by attribute data-ward-name
    const wardSelected = wardSelect.options[wardSelect.selectedIndex];
    const wardName = wardSelected.getAttribute('data-ward-name');

    const order = {
        username,
        phone,
        email,
        province: provinceName,
        district: districtName,
        ward: wardName,
        address,
        note,
        shippingMethod,
        paymentMethod,
        couponCode,
        couponDiscount,
        items,
    }
    console.log({order})

    axios.post('/api/v1/public/orders', order)
        .then(response => {
            if (response.status === 200) {
                toastr.success('Đặt hàng thành công');
                cart.cartItems = [];
                setTimeout(() => {
                    window.location.href = `/xac-nhan-don-hang/${response.data}`;
                }, 1500)
            } else {
                toastr.error('Đặt hàng thất bại');
            }
        })
        .catch(error => {
            console.log(error);
            toastr.error(error.response.data.message);
        })
})

const init = async () => {
    await getProvinces();
    if(isLogin && addressList.length > 0) {
        await fillAddress(addressList[0]);
    }
    displayTotalPrice();
}
init()