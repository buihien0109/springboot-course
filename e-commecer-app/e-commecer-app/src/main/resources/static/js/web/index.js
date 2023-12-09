// slider banner
$(".banners__pic__slider").owlCarousel({
    margin: 20,
    items: 2,
    dots: false,
    smartSpeed: 1200,
    autoHeight: false,
    autoplay: true,
    loop: true,
});

// load more product by category parent slug
function loadMoreProduct(categorySlug) {
    // get info in data
    const dataItem = data.find(d => d.slug === categorySlug);

    // call api
    axios.get(`/api/v1/public/products/load-more?categorySlug=${categorySlug}&page=${dataItem.currentPage + 1}`).then(res => {
        console.log(res);
        if (res.status === 200) {
            // update data
            dataItem.products = [...dataItem.products, ...res.data.products];
            dataItem.totalElements = res.data.totalElements;
            dataItem.remain = res.data.remain;
            dataItem.currentPage = res.data.currentPage;

            const categoryContainer = document.querySelector(`[data-name="${dataItem.name}"]`);
            const loadMoreRow = categoryContainer.querySelector(`.load-more-row`);
            const loadMoreBtn = loadMoreRow.querySelector(`.loadmore__btn button`);
            // if remain = 0 then remove load more button
            if (dataItem.remain === 0) {
                loadMoreRow.parentNode.removeChild(loadMoreRow);
            } else {
                // update remain product in load more button
                loadMoreBtn.querySelector(`.remain-count`).innerText = dataItem.remain;
            }
            renderLoadMore(categorySlug);
        }
    }).catch(err => {
        console.log(err);
    })
}

const renderLoadMore = (categorySlug) => {
    const dataItem = data.find(d => d.slug === categorySlug);
    let html = '';
    dataItem.products.forEach(product => {
        html += `
                    <div class="col-lg-3 col-md-6 col-sm-6">
                        <div class="product__item">
                            <div class="product__item__pic set-bg" style="background-image: url(${product.imageUrl != null ? product.imageUrl : '/img/image-placeholder.png'})">
                                <ul class="product__item__pic__hover">
                                    <li><a href="javascript:void(0)" onclick="addToWishList(${product.productId})"><i class="fa fa-heart"></i></a></li>
                                    <li><a href="javascript:void(0)" onclick="addToCart(${product.productId}, 1)"><i class="fa fa-shopping-cart"></i></a></li>
                                </ul>
                            </div>
                            <div class="product__item__text">
                                <h6><a href="/san-pham/${product.productId}">${product.name}</a></h6>
                                <div class="${product.discountPrice != null ? 'product-item-price' : ''}">
                                    ${
            product.discountPrice != null ?
                `<h5 class="discount-price currency">${formatCurrency(product.discountPrice)}</h5>
                                            <h5 class="original-price currency">${formatCurrency(product.price)}</h5>`
                :
                `<h5 class="currency">${formatCurrency(product.price)}</h5>`
        }
                                </div>
                            </div>
                        </div>
                    </div>
                `
    })
    const productContainer = document.querySelector(`[data-name="${dataItem.name}"] .product-list`);
    productContainer.innerHTML = html;
}