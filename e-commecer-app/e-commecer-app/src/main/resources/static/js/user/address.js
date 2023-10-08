let isUpdate = false;
let idUpdate = null;

const addressInfo = document.querySelector('.address-info');
const renderAddress = () => {
    addressInfo.innerHTML = '';
    addressList.sort((a, b) => {
        return b.isDefault - a.isDefault;
    })
    let html = '';
    addressList.forEach((address, index) => {
        html += `
                    <div class="d-flex justify-content-between align-items-center">
                        <p class="address-detail">
                            ${index + 1}. ${address.detail}, ${address.ward}, ${address.district}, ${address.province}
                            ${
            address.isDefault
                ? '<span class="address-default">(Mặc định)</span>'
                : `<span class="address-sub" onclick="setDefaultAddress(${address.id})">(Đặt làm địa chỉ mặc định)</span>`
        }
                        </p>
                        <div class="btn-action-list">
                            ${
            address.isDefault
                ? `<button class="btn-action" onclick="openModalUpdate(${address.id})"><i class="fa fa-pencil" aria-hidden="true"></i></button>`
                : `
                                    <button class="btn-action" onclick="openModalUpdate(${address.id})"><i class="fa fa-pencil" aria-hidden="true"></i></button>
                                    <button class="btn-action" onclick="deleteAddress(${address.id})"><i class="fa fa-trash" aria-hidden="true"></i></button>
                                    `
        }
                        </div>
                    </div>
                `
    })
    addressInfo.innerHTML = html;
}

// handle set default address, call api using axios with valina js
function setDefaultAddress(id) {
    axios.put(`/api/v1/users/address/${id}/set-default`)
        .then(res => {
            if (res.status === 200) {
                addressList.forEach(address => {
                    address.isDefault = address.id === id;
                })
                renderAddress();

                // show toast success
                toastr.success('Đặt làm địa chỉ mặc định thành công');
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error('Đặt làm địa chỉ mặc định thất bại');
        })
}

// handle delete address, call api using axios with valina js
function deleteAddress(id) {
    const isDelete = confirm('Bạn có chắc chắn muốn xóa địa chỉ này?');
    if (!isDelete) return;
    axios.delete(`/api/v1/users/address/${id}`)
        .then(res => {
            if (res.status === 200) {
                addressList.forEach((address, index) => {
                    if (address.id === id) {
                        addressList.splice(index, 1);
                    }
                })
                renderAddress();

                // show toast success
                toastr.success('Xóa địa chỉ thành công');
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error('Xóa địa chỉ thất bại');
        })
}

// Hiển thị danh sách province vào select id="province"
const provinceSelect = document.getElementById('province');
const districtSelect = document.getElementById('district');
const wardSelect = document.getElementById('ward');

const renderProvince = (provinces) => {
    provinceSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Tỉnh/Thành phố</option>';
    districtSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Quận/Huyện</option>';
    wardSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Xã/Phường</option>';

    provinces.forEach(province => {
        const {ProvinceID, ProvinceName} = province;
        const option = document.createElement('option');
        option.value = ProvinceID;
        option.innerText = ProvinceName;
        option.setAttribute('data-province-name', ProvinceName);
        provinceSelect.appendChild(option);
    });
}

const renderDistrict = (districts) => {
    districtSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Quận/Huyện</option>';
    districts.forEach(district => {
        const {DistrictID, DistrictName} = district;
        const option = document.createElement('option');
        option.value = DistrictID;
        option.innerText = DistrictName;
        option.setAttribute('data-district-name', DistrictName);
        districtSelect.appendChild(option);
    });
}

const renderWard = (wards) => {
    wardSelect.innerHTML = '<option hidden="hidden" value="">-- Chọn Xã/Phường</option>';
    wards.forEach(ward => {
        const {WardCode, WardName} = ward;
        const option = document.createElement('option');
        option.value = WardCode;
        option.innerText = WardName;
        option.setAttribute('data-ward-name', WardName);
        wardSelect.appendChild(option);
    });
}

const getProvinces = async () => {
    try {
        const response = await axios.get('/api/v1/public/address/provinces');
        if (response.status === 200) {
            const {data} = response.data;
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
    getDistricts(provinceCode);
    districtSelect.disabled = false;
    wardSelect.disabled = true;
    wardSelect.innerHTML = '<option hidden="hidden">-- Chọn Xã/Phường</option>';
});

const getDistricts = async (provinceId) => {
    try {
        const response = await axios.get(`/api/v1/public/address/districts?province_id=${provinceId}`);
        if (response.status === 200) {
            const {data} = response.data;
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
        const response = await axios.get(`/api/v1/public/address/wards?district_id=${districtId}`);
        if (response.status === 200) {
            const {data} = response.data;
            renderWard(data);
        }
    } catch (error) {
        console.log(error);
        toastr.error('Không thể lấy danh sách xã/phường');
    }
}
getProvinces();

// open modal create address
const btnCreateAddress = document.querySelector('.btn-create-address');
btnCreateAddress.addEventListener('click', () => {
    $('#modal-address').modal('show');
    // Change title modal
    document.querySelector('#modal-address .modal-title').innerText = 'Tạo địa chỉ';

    // changle label button
    document.querySelector('#modal-address .modal-footer .btn-handle-address').innerText = 'Tạo';
})

// handle create address, call api using axios with valina js with modal
function createAddress() {
    const provinceSelected = provinceSelect.options[provinceSelect.selectedIndex];
    const provinceName = provinceSelected.getAttribute('data-province-name');

    const districtSelected = districtSelect.options[districtSelect.selectedIndex];
    const districtName = districtSelected.getAttribute('data-district-name');

    const wardSelected = wardSelect.options[wardSelect.selectedIndex];
    const wardName = wardSelected.getAttribute('data-ward-name');

    const address = document.getElementById('address').value;
    const isDefault = document.getElementById('set-address-default').checked;

    const data = {
        province: provinceName,
        district: districtName,
        ward: wardName,
        detail: address,
        isDefault: isDefault
    }

    axios.post('/api/v1/users/address', data)
        .then(res => {
            if (res.status === 200) {
                if (res.data.isDefault) {
                    addressList.forEach(address => {
                        address.isDefault = false;
                    })
                }
                addressList.push(res.data);
                renderAddress();
                $('#modal-address').modal('hide');

                // show toast success
                toastr.success('Thêm địa chỉ thành công');
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error('Thêm địa chỉ thất bại');
        })
}

$('#modal-address').on('hidden.bs.modal', function (e) {
    getProvinces();
    document.getElementById('address').value = '';
    document.getElementById('set-address-default').checked = false;
    document.getElementById('set-address-default').disabled = false;
})

// open modal update address
async function openModalUpdate(id) {
    $('#modal-address').modal('show');
    // Change title modal
    document.querySelector('#modal-address .modal-title').innerText = 'Cập nhật địa chỉ';

    // changle label button
    document.querySelector('#modal-address .modal-footer .btn-handle-address').innerText = 'Cập nhật';

    // set data for modal
    const address = addressList.find(address => address.id === id);
    document.getElementById('address').value = address.detail;
    document.getElementById('set-address-default').checked = address.isDefault;
    if (address.isDefault) {
        document.getElementById('set-address-default').disabled = true;
    }

    // set province if province same province in address
    const provinceOptions = provinceSelect.options;
    for (let i = 0; i < provinceOptions.length; i++) {
        const provinceOption = provinceOptions[i];
        if (provinceOption.getAttribute('data-province-name') === address.province) {
            provinceSelect.selectedIndex = i;
            break;
        }
    }

    // get district by province id
    console.log("provinceId", provinceSelect.value);
    await getDistricts(provinceSelect.value);

    // set district if district same district in address
    const districtOptions = districtSelect.options;
    for (let i = 0; i < districtOptions.length; i++) {
        const districtOption = districtOptions[i];
        if (districtOption.getAttribute('data-district-name') === address.district) {
            districtSelect.selectedIndex = i;
            break;
        }
    }

    // get ward by district id
    console.log("districtId", districtSelect.value);
    await getWards(districtSelect.value);

    // set ward if ward same ward in address
    const wardOptions = wardSelect.options;
    for (let i = 0; i < wardOptions.length; i++) {
        const wardOption = wardOptions[i];
        if (wardOption.getAttribute('data-ward-name') === address.ward) {
            wardSelect.selectedIndex = i;
            break;
        }
    }

    // enable disabled select, wardSelect when update
    districtSelect.disabled = false;
    wardSelect.disabled = false;

    isUpdate = true;
    idUpdate = id;
}

function updateAddress() {
    const provinceSelected = provinceSelect.options[provinceSelect.selectedIndex];
    const provinceName = provinceSelected.getAttribute('data-province-name');

    const districtSelected = districtSelect.options[districtSelect.selectedIndex];
    const districtName = districtSelected.getAttribute('data-district-name');

    const wardSelected = wardSelect.options[wardSelect.selectedIndex];
    const wardName = wardSelected.getAttribute('data-ward-name');

    const address = document.getElementById('address').value;
    const isDefault = document.getElementById('set-address-default').checked;

    const data = {
        province: provinceName,
        district: districtName,
        ward: wardName,
        detail: address,
        isDefault: isDefault
    }

    axios.put(`/api/v1/users/address/${idUpdate}`, data)
        .then(res => {
            if (res.status === 200) {
                if (res.data.isDefault) {
                    addressList.forEach(address => {
                        address.isDefault = false;
                    })
                }
                addressList.forEach(address => {
                    if (address.id === idUpdate) {
                        address.province = res.data.province;
                        address.district = res.data.district;
                        address.ward = res.data.ward;
                        address.detail = res.data.detail;
                        address.isDefault = res.data.isDefault;
                    }
                })
                renderAddress();
                $('#modal-address').modal('hide');

                // show toast success
                toastr.success('Cập nhật địa chỉ thành công');

                isUpdate = false;
                idUpdate = null;
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error('Cập nhật địa chỉ thất bại');
        })
}

// validation form
$('#form-address').validate({
    rules: {
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

function handleAddress() {
    if (!$('#form-address').valid()) return;

    if (isUpdate) {
        updateAddress();
    } else {
        createAddress();
    }
}