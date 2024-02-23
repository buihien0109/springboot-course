// add event listener click to all btn pagination has class pagination-button
// and retain query params in url if it has
document.querySelectorAll('.pagination-button').forEach(btn => {
    btn.addEventListener('click', function () {
        let page = Number(btn.dataset.page);

        // if url has query params
        if (window.location.search) {
            // get query params in url
            const urlParams = new URLSearchParams(window.location.search);
            // add page param to query params
            urlParams.set('page', page);

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