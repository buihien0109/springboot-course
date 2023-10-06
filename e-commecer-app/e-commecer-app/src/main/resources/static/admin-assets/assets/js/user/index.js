
// render users from users to body of table
const tableBody = document.querySelector('table tbody');
const renderUsers = (userList) => {
    let html = '';
    userList.forEach(user => {
        html += `
                  <tr>
                      <td>
                          <a href="/admin/users/${user.userId}/detail">${user.username}</a>
                      </td>
                      <td>
                            ${user.email}
                      </td>
                      <td>
                            ${user.phone}
                      </td>
                      <td>Active</td>
                      <td>
                            ${user.roles.map(role => `<span class="badge bg-info mr-1">${role.name}</span>`).join('')}
                        </td>
                  </tr>
                `;
    })
    tableBody.innerHTML = html;
}

// render pagination using pagination.js
const renderPagination = (userList) => {
    $('#pagination').pagination({
        dataSource: userList,
        className: 'paginationjs-theme-blue paginationjs-big',
        callback: function (data, pagination) {
            renderUsers(data);
        },
        hideOnlyOnePage : true
    })
}
renderPagination(users);