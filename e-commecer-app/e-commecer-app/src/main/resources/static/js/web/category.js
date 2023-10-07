// add event listener click to all btn pagination has class pagination-button
// and retain query params in url if it has
document.querySelectorAll('.pagination-button').forEach(btn => {
    btn.addEventListener('click', function () {
        let page = btn.dataset.page;
        console.log(page)

        // if url has query params
        if (window.location.search) {
            console.log(1, window.location.search)
            // get query params in url
            const urlParams = new URLSearchParams(window.location.search);
            // add page param to query params
            urlParams.set('page', page);

            // TODO: if page = 1 => remove page param from query params

            // set query params to url
            window.location.search = urlParams.toString();
        } else {
            // if url has not query params
            // add query params to url
            window.location.search = `?page=${page}`;
        }
    })
})