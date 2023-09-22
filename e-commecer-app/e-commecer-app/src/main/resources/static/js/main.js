/*------------------
   toastr config
   --------------------*/
toastr.options = {
    "closeButton": false,
    "debug": false,
    "newestOnTop": false,
    "progressBar": false,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "2000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
}

// add to cart
const shoppingCartIcon = document.querySelector('.shopping-cart-icon span');
const addToCart = (productId, quantity) => {
    axios.post('/api/v1/cart/add', {
        productId: productId,
        quantity: quantity
    }).then(res => {
        console.log(res);
        if (res.status === 200) {
            // update cart data
            cart = res.data;
            shoppingCartIcon.innerText = cart.cartItems.length;
            toastr.success("Thêm vào giỏ hàng thành công");
        } else {
            toastr.error("Thêm vào giỏ hàng thất bại");
        }
    }).catch(err => {
        console.log(err);
        toastr.error(err.response.data.message)
    })
};

// add to wishlist
const wishlistIcon = document.querySelector('.wishlist-icon span');
const addToWishList = (productId) => {
    axios.post('/api/v1/wishlist', {
        productId: productId
    }).then(res => {
        console.log(res);
        if (res.status === 200) {
            // check if product in wishlist or not
            const wishListItem = wishList.find(wl => wl.product.productId === productId);
            if (!wishListItem) {
                wishList.push(res.data);
                wishlistIcon.innerText = wishList.length;
            }
            toastr.success("Thêm vào danh sách yêu thích thành công");
        } else {
            toastr.error("Thêm vào danh sách yêu thích thất bại");
        }
    }).catch(err => {
        console.log(err);
        toastr.error(err.response.data.message)
    })
}