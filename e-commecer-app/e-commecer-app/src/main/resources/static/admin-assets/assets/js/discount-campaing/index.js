
// Generate status discount
// Compare 2 date string
const compareDate = (date1, date2) => {
    const d1 = new Date(date1);
    const d2 = new Date(date2);
    return d1.getTime() - d2.getTime();
}

// Generate status discount
const generateStatus = (validFrom, validTo) => {
    const now = new Date();
    const from = new Date(validFrom);
    const to = new Date(validTo);
    if (compareDate(now, from) < 0) {
        return `<span class="badge badge-warning">Chưa kích hoạt</span>`;
    } else if (compareDate(now, to) > 0) {
        return `<span class="badge badge-secondary">Đã hết hạn</span>`;
    } else {
        return `<span class="badge badge-success">Đang kích hoạt</span>`;
    }
}

// render discount value by discount type
const renderDiscountValue = (discountType, discountValue) => {
    switch (discountType) {
        case "PERCENT":
            return `${discountValue}%`;
        case "AMOUNT":
        case"SAME_PRICE":
            return `${formatCurrency(discountValue)}`;
        default:
            return `Unknown`;
    }
}

// Render tag
const tableContent = document.querySelector("table tbody")
const renderDiscountCampaings = (discountCampaingList) => {
    tableContent.innerHTML = "";
    let html = "";
    discountCampaingList.forEach((discountCampaing) => {
        html += `
                <tr>
                    <td>
                        <a href="/admin/discount-campaigns/${discountCampaing.campaignId}/detail">
                            ${discountCampaing.name}
                        </a>
                    </td>
                    <td>${generateStatus(discountCampaing.startDate, discountCampaing.endDate)}</td>
                    <td>${discountCampaing.discountType}</td>
                    <td>${renderDiscountValue(discountCampaing.discountType, discountCampaing.discountValue)}</td>
                    <td>${formatDate(discountCampaing.startDate)} - ${formatDate(discountCampaing.endDate)}</td>
                    <td>${discountCampaing.productCount}</td>
                </tr>
                `
    })
    tableContent.innerHTML = html;
}

// render pagination using pagination.js
const renderPagination = () => {
    $('#pagination').pagination({
        dataSource: discountCampaigns,
        className: 'paginationjs-theme-blue paginationjs-big',
        callback: function (data, pagination) {
            renderDiscountCampaings(data);
        },
        hideOnlyOnePage: true
    })
}

renderPagination();