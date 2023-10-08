let idUpdate = null;
let isUpdate = false;

/*---------------------------------
    Product Details Pic Slider
----------------------------------*/
$(".product__details__pic__slider").owlCarousel({
    margin: 20, items: 4, dots: true, smartSpeed: 1200, autoHeight: false, autoplay: true, center: true, loop: true,
});

/*------------------
    Single Product
--------------------*/
$('.product__details__pic__slider img').on('click', function () {
    var imgurl = $(this).data('imgbigurl');
    var bigImg = $('.product__details__pic__item--large').attr('src');
    if (imgurl != bigImg) {
        $('.product__details__pic__item--large').attr({
            src: imgurl
        });
    }
});

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

// click button to open modal review
const btnCreateReview = document.querySelector(".btn-create-review");
btnCreateReview.addEventListener("click", () => {
    $('#modal-review').modal('show');
})

/*-------------------
    Rating change
--------------------- */
const stars = document.querySelectorAll(".review-product-rating-item");
const ratingMessage = document.querySelector(".review-product-rating-result");
let currentRating = 0;
let currentMessage = "";

stars.forEach((star) => {
    star.addEventListener("mouseover", () => {
        resetStars();
        const rating = parseInt(star.getAttribute("data-rating"));
        highlightStars(rating);
    });

    star.addEventListener("mouseout", () => {
        resetStars();
        highlightStars(currentRating);
    });

    star.addEventListener("click", () => {
        currentRating = parseInt(star.getAttribute("data-rating"));
        currentMessage = star.getAttribute("data-message");
        ratingMessage.textContent = `Bạn đã đánh giá ${currentRating} sao. (${currentMessage})`;
        highlightStars(currentRating);
    });
});

function resetStars() {
    stars.forEach((star) => {
        star.classList.remove("active");
    });
}

function highlightStars(rating) {
    stars.forEach((star) => {
        const starRating = parseInt(star.getAttribute("data-rating"));
        if (starRating <= rating) {
            star.classList.add("active");
        }
    });
}

function reviewMessage(rating) {
    switch (rating) {
        case 1:
            return "Không thích";
        case 2:
            return "Tạm được";
        case 3:
            return "Bình thường";
        case 4:
            return "Hài lòng";
        case 5:
            return "Tuyệt vời";
        default:
            return "";
    }
}

const formatDate = dateString => {
    const date = new Date(dateString);
    const day = `0${date.getDate()}`.slice(-2);
    const month = `0${date.getMonth() + 1}`.slice(-2);
    const year = date.getFullYear();
    const hour = `0${date.getHours()}`.slice(-2);
    const minute = `0${date.getMinutes()}`.slice(-2);
    const second = `0${date.getSeconds()}`.slice(-2);
    return `${hour}:${minute}:${second} - ${day}/${month}/${year}`;
}

/*-------------------
    render review
--------------------- */
const renderReview = review => {
    let ratingHtml = "";
    for (let i = 0; i < 5; i++) {
        if (i < review.rating) {
            ratingHtml += `<span class="active"><i class="fa fa-star"></i></span>`
        } else {
            ratingHtml += `<span class="unactive"><i class="fa fa-star"></i></span>`
        }
    }
    return `
            <div class="review-item">
                <div class="review-avatar">
                    <img src="${review.authorAvatar}" alt="">
                </div>
                <div class="review-info">
                    <div class="d-flex align-items-center mb-2">
                        <p class="author mr-2 my-0">${review.authorName}</p> <span class="time">${formatDate(review.updatedAt)}</span>
                    </div>
                    <div class="rating mb-2 d-flex align-items-center">
                        ${ratingHtml}
                    </div>
                    <p class="content">${review.comment}</p>
                    ${isLogin && currentUser.userId === review.authorId ? `
                            <div>
                                <button class="border-0 bg-transparent btn-edit-review text-primary" onclick="openModalToUpdateReview(${review.reviewId})">Sửa</button>
                                <button class="border-0 bg-transparent btn-delete-review text-primary" onclick="deleteReview(${review.reviewId})">Xóa</button>
                            </div>
                            ` : ""}
                </div>
            </div>
            `
}

const renderReviews = (reviewList) => {
    const prevBtn = document.querySelector("#review-pagination .paginationjs-prev a");
    const nextBtn = document.querySelector("#review-pagination .paginationjs-next a");
    if (prevBtn) {
        prevBtn.innerHTML = `<i class="fa fa-long-arrow-left"></i>`;
    }
    if (nextBtn) {
        nextBtn.innerHTML = `<i class="fa fa-long-arrow-right"></i>`;
    }
    const reviewListEl = document.querySelector(".review-list");
    reviewListEl.innerHTML = "";
    let html = "";
    reviewList.forEach(review => {
        html += renderReview(review);
    })
    reviewListEl.innerHTML = html;

    renderOverview();
}

const renderOverview = () => {
    const reviewCount = document.querySelectorAll(".review-count");
    reviewCount.forEach(el => {
        el.innerText = reviews.length;
    })

    renderOverviewAvg();
    renderOverviewProgress();
}

const renderOverviewAvg = () => {
    // calcaulate avg rating
    let avgRating = 0;
    reviews.forEach(review => {
        avgRating += review.rating;
    })
    avgRating = reviews.length > 0 ? Math.round(avgRating / reviews.length) : 0

    // render star based on rating number
    const ratingHtmlEl = document.querySelector(".overview-avg .rating");
    const productDetailRatingHtmlEl = document.querySelector(".product__details__rating .rating");
    ratingHtmlEl.innerHTML = "";
    productDetailRatingHtmlEl.innerHTML = "";
    let ratingHtml = "";
    for (let i = 0; i < 5; i++) {
        if (i < avgRating) {
            ratingHtml += `<span class="active"><i class="fa fa-star"></i></span>`
        } else {
            ratingHtml += `<span class="unactive"><i class="fa fa-star"></i></span>`
        }
    }
    ratingHtmlEl.innerHTML = ratingHtml;
    productDetailRatingHtmlEl.innerHTML = ratingHtml;

    // render avg rating
    const avgRatingEl = document.querySelector(".overview-avg h2");
    avgRatingEl.innerText = `${avgRating}/5`;

    // render count rating
    const countRatingEl = document.querySelector(".overview-avg .review-count");
    countRatingEl.innerText = reviews.length;
}

const renderOverviewProgress = () => {
    const ratingArr = [5, 4, 3, 2, 1];
    const ratingCount = [0, 0, 0, 0, 0];

    ratingArr.forEach((el, index) => {
        reviews.forEach(review => {
            if (review.rating === el) {
                ratingCount[index]++;
            }
        })
    })

    const progressBlockEl = document.querySelector(".overview-progress .progress-block");
    progressBlockEl.innerHTML = "";
    let html = "";
    ratingArr.forEach((el, index) => {
        html += `
                    <div class="progress-item d-flex align-items-center w-100 mb-1">
                        <span class="mr-1">${el}</span>
                        <span class="mr-1 rating"><i class="fa fa-star"></i></span>
                        <div class="flex-grow-1 progress progress-success progress-sm progress-line">
                            <div class="progress-bar" style="width: ${ratingCount[index] * 100 / reviews.length}%;"></div>
                        </div>
                        <span class="ml-1">${ratingCount[index]}</span>
                    </div>
                `
    })
    progressBlockEl.innerHTML = html;
}

const renderPagination = () => {
    $('#review-pagination').pagination({
        dataSource: reviews,
        pageSize: 5,
        autoHidePrevious: true,
        autoHideNext: true,
        callback: function (data, pagination) {
            console.log(pagination)
            renderReviews(data);
        },
        hideOnlyOnePage: true,
    })
}

$('#modal-review').on('hidden.bs.modal', function (event) {
    // clear review content
    reviewContent.value = "";

    // clear rating message
    ratingMessage.textContent = "";

    // reset stars
    resetStars();

    idUpdate = null;
    isUpdate = false;
})

function openModalToUpdateReview(reviewId) {
    // find review by id
    const review = reviews.find(review => review.reviewId === reviewId);

    // set rating
    currentRating = review.rating;
    currentMessage = reviewMessage(currentRating);
    ratingMessage.textContent = `Bạn đã đánh giá ${currentRating} sao. (${currentMessage})`;
    highlightStars(currentRating);

    // set review content
    reviewContent.value = review.comment;

    // set id update
    idUpdate = reviewId;
    isUpdate = true;

    // open modal
    $('#modal-review').modal('show');
}

/*-------------------
    Handle review (create/update)
--------------------- */
const btnSendReview = document.querySelector("#btn-send-review");
const reviewContent = document.querySelector("#review-content");
btnSendReview.addEventListener("click", () => {
    if (isUpdate) {
        updateReview(idUpdate);
    } else {
        createReview();
    }
})

/*-------------------
    Create review
--------------------- */
const createReview = () => {
    const review = {
        productId: product.productId, rating: currentRating, comment: reviewContent.value
    }

    // Send request to server using axios
    axios.post("/api/v1/public/reviews", review)
        .then(res => {
            // add review to reviews array
            reviews.unshift(res.data);
            renderPagination();

            toastr.success("Đánh giá thành công");
            currentRating = 0;
            currentMessage = "";

            // close modal
            $('#modal-review').modal('hide');
        })
        .catch(err => {
            console.log(err);
            toastr.error("Đánh giá thất bại");
        })
}

/*-------------------
    Delete review
--------------------- */
const deleteReview = id => {
    const isDelete = confirm("Bạn có chắc chắn muốn xóa đánh giá này?");
    if (!isDelete) {
        return;
    }
    // Send request to server using axios
    axios.delete(`/api/v1/public/reviews/${id}`)
        .then(res => {
            // delete review in reviews array
            const index = reviews.findIndex(review => review.reviewId === id);
            reviews.splice(index, 1);
            renderPagination();

            toastr.success("Xóa đánh giá thành công");
        })
        .catch(err => {
            console.log(err);
            toastr.error("Xóa đánh giá thất bại");
        })
}


/*-------------------
    Update review
--------------------- */
const updateReview = id => {
    const review = {
        rating: currentRating, comment: reviewContent.value
    }

    // Send request to server using axios
    axios.put(`/api/v1/public/reviews/${id}`, review)
        .then(res => {
            // update review in reviews array
            const index = reviews.findIndex(review => review.reviewId === id);
            reviews[index] = res.data;
            renderPagination();

            toastr.success("Cập nhật đánh giá thành công");
            currentRating = 0;
            currentMessage = "";

            // close modal
            $('#modal-review').modal('hide');
        })
        .catch(err => {
            console.log(err);
            toastr.error("Cập nhật đánh giá thất bại");
        })
}

renderPagination();