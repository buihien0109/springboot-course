// write function to render cart from cart object
const tableBody = document.querySelector('.shoping__cart__table tbody');
const renderCart = (cartItems) => {
    tableBody.innerHTML = '';
    let html = '';
    cartItems.forEach(ct => {
        html += `
                    <tr>
                        <td class="shoping__cart__item">
                            <img src="${ct.product.images.mainImage != null ? ct.product.images.mainImage.imageUrl : '/img/image-placeholder.png'}" alt="">
                            <h5>
                                <a href="/san-pham/${ct.product.productId}">${ct.product.name}</a>
                            </h5>
                        </td>
                        <td class="shoping__cart__price ${ct.product.discountPrice != null ? 'has-discount' : ''}">
                            ${ct.product.discountPrice != null ? `<span class="discount-price">${formatCurrency(ct.product.discountPrice)}</span>
                                    <span class="original-price">${formatCurrency(ct.product.price)}</span>` : `<span class="original-price">${formatCurrency(ct.product.price)}</span>`}
                        </td>
                        <td class="shoping__cart__quantity">
                            <div class="quantity">
                                <div class="pro-qty">
                                    <span class="dec qtybtn" onclick="decrementQuantity(${ct.cartItemId})">-</span>
                                    <input type="text" value="${ct.quantity}" readonly="">
                                    <span class="inc qtybtn" onclick="incrementQuantity(${ct.cartItemId})">+</span>
                                </div>
                            </div>
                        </td>
                        <td class="shoping__cart__total">${ct.product.discountPrice != null ? formatCurrency(ct.product.discountPrice * ct.quantity) : formatCurrency(ct.product.price * ct.quantity)}</td>
                        <td class="shoping__cart__item__close">
                            <span class="icon_close" onclick="deleteCartItem(${ct.cartItemId})"></span>
                        </td>
                    </tr>
                `
    })
    tableBody.innerHTML = html;

    displayTotalPrice(cartItems);
}

// Tính thành tiền, tổng tiền
const displayTotalPrice = (cartItems) => {
    const temporaryAmount = document.querySelector('.temporary-amount');
    const totalAmount = document.querySelector('.total-amount');

    let temporaryAmountValue = 0;
    let totalAmountValue = 0;

    cartItems.forEach(ct => {
        if (ct.product.discountPrice != null) {
            temporaryAmountValue += ct.product.discountPrice * ct.quantity;
        } else {
            temporaryAmountValue += ct.product.price * ct.quantity;
        }
    })

    totalAmountValue = temporaryAmountValue;

    // display to UI
    temporaryAmount.innerText = formatCurrency(temporaryAmountValue);
    totalAmount.innerText = formatCurrency(totalAmountValue);
}

// Increment quantity
const incrementQuantity = (cartItemId) => {
    const cartItem = cart.cartItems.find(ct => ct.cartItemId === cartItemId);

    // check if quantity is greater than quantity in stock
    if (cartItem.quantity >= cartItem.product.stockQuantity) {
        toastr.error('Số lượng sản phẩm trong kho không đủ!');
        return;
    }

    // call api to update cart item using axios
    axios.put(`/api/v1/cart-items/${cartItemId}`, {
        quantity: 1
    })
        .then(res => {
            if (res.status === 200) {
                // update cart in UI
                cartItem.quantity++;
                renderCart(cart.cartItems);
            } else {
                toastr.error('Cập nhật số lượng thất bại!')
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error(err.response.data.message);
        })
}

// Decrement quantity
const decrementQuantity = (cartItemId) => {
    // check if quantity is less than 1
    const cartItem = cart.cartItems.find(ct => ct.cartItemId === cartItemId);
    if (cartItem.quantity <= 1) {
        return;
    }

    // call api to update cart item using axios
    axios.put(`/api/v1/cart-items/${cartItemId}`, {
        quantity: -1
    })
        .then(res => {
            if (res.status === 200) {
                // update cart in UI
                if (cartItem.quantity > 1) {
                    cartItem.quantity--;
                    renderCart(cart.cartItems);
                }
            } else {
                toastr.error('Cập nhật số lượng thất bại!')
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error(err.response.data.message);
        })
}

// delete cart item
const deleteCartItem = (cartItemId) => {
    const isConfirm = confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?');
    if (!isConfirm) {
        return;
    }

    // call api to delete cart item using axios
    axios.delete(`/api/v1/cart-items/${cartItemId}`)
        .then(res => {
            console.log(res);
            if (res.status === 200) {
                // update cart in UI
                const cartItem = cart.cartItems.find(ct => ct.cartItemId === cartItemId);
                const index = cart.cartItems.indexOf(cartItem);
                cart.cartItems.splice(index, 1);
                renderCart(cart.cartItems);

                // update cart icon
                shoppingCartIcon.innerText = cart.cartItems.length;

                // show toastr
                toastr.success('Xóa sản phẩm khỏi giỏ hàng thành công!')

                // check if cart is empty to display message
                if (cart.cartItems.length === 0) {
                    const html = `
                                <div class="d-flex flex-column">
                                    <h4 class="mb-3">Không có sản phẩm nào trong giỏ hàng</h4>
                                    <div class="shoping__cart__btns">
                                        <a href="/" class="primary-btn cart-btn" style="background: #7fad39; color: #fff;">Tiếp tục mua hàng</a>
                                    </div>
                                </div>
                            `
                    const shopingCartProducts = document.querySelector('.shoping-cart-products');
                    shopingCartProducts.innerHTML = html;

                    // hide shoping-cart-info
                    const shopingCartInfo = document.querySelector('.shoping-cart-info');
                    shopingCartInfo.parentElement.removeChild(shopingCartInfo);
                }
            } else {
                toastr.error('Xóa sản phẩm khỏi giỏ hàng thất bại!')
            }
        })
        .catch(err => {
            console.log(err);
            toastr.error(err.response.data.message);
        })
}

// call display total price function if cart is not empty
if (cart.cartItems.length > 0) {
    displayTotalPrice(cart.cartItems);
}