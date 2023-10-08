// write function to covert date string to date with format dd/MM/yyyy
function convertDate(dateString) {
    const date = new Date(dateString);
    const day = `0${date.getDate()}`.slice(-2);
    const month = `0${date.getMonth() + 1}`.slice(-2);
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

// handle cancel order, add event listener to button, then call API to cancel order using axios
function cancelOrder(orderId) {
    axios.put(`/api/v1/users/orders/${orderId}/cancel`)
        .then(function (response) {
            console.log(response);
            if (response.status === 200) {
                // delete tr in table by data-id
                const tr = document.querySelector(`.order-history-table tbody tr[data-id="${orderId}"]`);
                tr.parentNode.removeChild(tr);

                toastr.success('Hủy đơn hàng thành công');
            } else {
                toastr.error('Hủy đơn hàng thất bại');
            }
        })
        .catch(function (error) {
            console.log(error);
            showError(error.response.data.message)
        });
}