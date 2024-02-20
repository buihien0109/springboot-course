document.querySelectorAll('.pagination-button').forEach(btn => {
    btn.addEventListener('click', function () {
        let page = Number(btn.dataset.page);

        // if url has query params
        if (window.location.search) {
            // get query params in url
            const urlParams = new URLSearchParams(window.location.search);

            // add page param to query params
            urlParams.set('page', page.toString());

            if (page === 1) {
                urlParams.delete('page');
            }

            // set query params to url
            window.location.search = urlParams.toString();
        } else {
            // if url has not query params
            // add query params to url
            window.location.search = `?page=${page}`;
        }
    })
})

// Xu lý tìm kiếm blog
const searchForm = document.getElementById('blog-form-search');
const searchInput = document.getElementById('search-input');
searchForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const searchValue = searchInput.value;
    if(searchValue.trim() === '') {
        alert('Vui lòng nhập từ khóa tìm kiếm');
        return;
    }

    // if url has query params
    if (window.location.search) {
        // get query params in url
        const urlParams = new URLSearchParams(window.location.search);

        // add search param to query params
        urlParams.set('search', searchValue);

        // remove page param
        urlParams.delete('page');

        // set query params to url
        window.location.search = urlParams.toString();
    } else {
        // if url has not query params
        // add query params to url
        window.location.search = `?search=${searchValue}`;
    }
})