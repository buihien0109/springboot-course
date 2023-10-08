// define function to remove item from wish list in server with parameter is wish list id and using vanila javascript and axios
function removeFromWishList(wishListId) {
    // confirm before remove
    if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm khỏi danh sách yêu thích?')) {
        return;
    }

    axios.delete(`/api/v1/public/wishlist/${wishListId}`)
        .then(function (response) {
            console.log(response);
            if (response.status === 200) {
                // remove item from wish list in client
                const wishListItem = document.querySelector(`.wishlist .wishlist__item__container[data-id="${wishListId}"]`);
                wishListItem.parentNode.removeChild(wishListItem);

                // update total wish list item in client
                wishList.splice(wishList.findIndex(wishListItem => wishListItem.wishlistId === wishListId), 1);
                wishlistIcon.innerText = wishList.length;

                // show notification
                toastr.success('Xóa sản phẩm khỏi danh sách yêu thích thành công');

                // if wish list is empty then show message
                if (wishList.length === 0) {
                    const wishListContainer = document.querySelector(`.wishlist .container .row`);
                    wishListContainer.innerHTML = '<h4>Không có sản phẩm nào trong danh sách yêu thích</h4>';
                }
            } else {
                toastr.error('Xóa sản phẩm khỏi danh sách yêu thích thất bại');
            }
        })
        .catch(function (error) {
            console.log(error);
            toastr.error(error.response.data.message);
        });
}