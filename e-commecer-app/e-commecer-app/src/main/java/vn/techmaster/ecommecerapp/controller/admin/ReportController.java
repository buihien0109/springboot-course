package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.ecommecerapp.service.DashboardService;
import vn.techmaster.ecommecerapp.service.ReportService;

@Slf4j
@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final DashboardService dashboardService;

    @GetMapping
    public String getReports(Model model,
                             @RequestParam(required = false) String start,
                             @RequestParam(required = false) String end) {
        model.addAttribute("totalRevenue", reportService.calculateOrderRevenueAmount(start, end));
        model.addAttribute("totalPayment", reportService.calculateTotalPaymentVoucherAmount(start, end));
        model.addAttribute("orders", reportService.getAllOrders(start, end));
        model.addAttribute("paymentVouchers", reportService.getAllPaymentVouchers(start, end));
        return "admin/report/index";
    }
}
