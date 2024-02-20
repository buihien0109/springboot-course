function logout() {
    // call logout api using axios
    axios.post('/api/auth/logout')
        .then(res => {
            if (res.status === 200) {
                alert("Đăng xuất thành công")
                window.location.href = '/';
            } else {
                alert('Đăng xuất thất bại')
            }
        })
        .catch(err => {
            console.log(err);
        })
}