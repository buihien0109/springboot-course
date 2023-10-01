// --------- Product Info ---------
// render sub category
const renderSubCategory = (parentId) => {
    const subCategoryList = categoryList.find(category => category.mainCategory.categoryId == parentId).subCategories;
    subCategory.innerHTML = ""
    let html = '<option hidden="hidden">Chọn danh mục con</option>';
    subCategoryList.forEach(subCategory => {
        html += `<option value="${subCategory.categoryId}">${subCategory.name}</option>`
    });
    subCategory.innerHTML = html;
}

// triger value parent category, sub category
const parentCategory = document.getElementById('parent-category');
const subCategory = document.getElementById('sub-category');
parentCategory.value = product.category.parentCategoryId;
renderSubCategory(product.category.parentCategoryId);
subCategory.value = product.category.categoryId;

// format price
const price = document.getElementById('price');
price.value = product.price.toString().replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");

// Handle event change parent category and load sub category using vanilla js
parentCategory.addEventListener('change', function (event) {
    const categoryId = event.target.value;
    renderSubCategory(Number(categoryId));
});

// format price when user input
price.addEventListener('keyup', function (event) {
    const value = event.target.value;
    event.target.value = value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
});

// convert price to number when user submit
const formatPrice = (price) => {
    return parseInt(price.replace(/,/g, ""));
}

// handle event submit form
const btnUpdate = document.getElementById('btn-update');
btnUpdate.addEventListener(("click"), () => {
    // send request to server
    const name = document.getElementById('name').value;
    const price = formatPrice(document.getElementById('price').value);
    const description = document.getElementById('description').value;
    const status = document.getElementById('status').value;
    const categoryId = Number(document.getElementById('sub-category').value);
    const attributes = product.attributes;

    // create product object
    const data = {
        name, price, description, status, categoryId, attributes
    }

    axios.put(`/api/v1/admin/products/${product.productId}`, data)
        .then(response => {
            if (response.status === 200) {
                toastr.success("Cập nhật sản phẩm thành công");
            } else {
                toastr.error("Cập nhật sản phẩm thất bại");
            }
        })
        .catch(error => {
            console.log(error);
            toastr.error(error.response.data.message);
        })
})