package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.DashboardService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        model.addAttribute("newOrders", dashboardService.countNewOrdersInCurrentMonth());
        model.addAttribute("newUsers", dashboardService.countNewUsersInCurrentMonth());
        model.addAttribute("newBlogs", dashboardService.countNewBlogsInCurrentMonth());
        model.addAttribute("totalRevenue", dashboardService.calculateOrderRevenueByCurrentMonth());
        model.addAttribute("totalPayment", dashboardService.calculateTotalPaymentVoucherByCurrentMonth());
        model.addAttribute("latestOrders", dashboardService.getOrderLatestByCurrentMonth(5));
        model.addAttribute("latestUsers", dashboardService.getUserLatestByCurrentMonth(8));
        model.addAttribute("bestSellingProducts", dashboardService.getBestSellingProductByCurrentMonth(5));
        return "admin/dashboard/index";
    }
}
