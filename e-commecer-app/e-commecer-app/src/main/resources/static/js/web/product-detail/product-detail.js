/*-------------------
    Quantity change
--------------------- */
let quantity = 1;
const quantityInput = document.querySelector('.pro-qty input');
const quantityBtns = document.querySelectorAll('.pro-qty .qtybtn');
quantityBtns.forEach(btn => {
    btn.addEventListener("click", () => {
        if (quantity >= product.stockQuantity) {
            alert("Số lượng sản phẩm không đủ");
            return;
        }
        if (btn.classList.contains("inc")) {
            quantity++;
        } else if (btn.classList.contains("dec") && quantity > 1) {
            quantity--;
        }
        quantityInput.value = quantity;
    })
})

/*-------------------
    Add to cart
--------------------- */
const btnAddToCart = document.querySelector('.btn-add-to-card');
// Send request to server
btnAddToCart.addEventListener('click', () => {
    addToCart(product.productId, quantity);
})

/*-------------------
    Add to wishlist
--------------------- */
const btnAddToWishlist = document.querySelector('.btn-add-to-wishlist');
// Send request to server
btnAddToWishlist.addEventListener("click", () => {
    addToWishList(product.productId);
})
