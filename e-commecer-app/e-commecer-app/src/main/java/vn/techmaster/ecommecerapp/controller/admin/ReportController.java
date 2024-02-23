package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.ecommecerapp.model.dto.PaymentVoucherDto;
import vn.techmaster.ecommecerapp.service.DashboardService;
import vn.techmaster.ecommecerapp.service.ReportService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public String getReports(Model model,
                             @RequestParam(required = false) String start,
                             @RequestParam(required = false) String end) {
        List<PaymentVoucherDto> paymentVouchers = reportService.getAllPaymentVouchers(start, end);
        model.addAttribute("totalRevenue", reportService.calculateOrderRevenueAmount(start, end));
        model.addAttribute("totalPayment", reportService.calculateTotalPaymentVoucherAmount(paymentVouchers));
        model.addAttribute("orders", reportService.getAllOrders(start, end)); // done
        model.addAttribute("paymentVouchers", paymentVouchers); // done
        model.addAttribute("startDate", reportService.getStartDate(start));
        model.addAttribute("endDate", reportService.getEndDate(end));
        return "admin/report/index";
    }
}
