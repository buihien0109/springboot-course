package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.security.SecurityUtils;
import vn.techmaster.ecommecerapp.service.PaymentVoucherService;

@Controller
@RequestMapping("/admin/payment_vouchers")
@RequiredArgsConstructor
public class PaymentVoucherController {
    private final PaymentVoucherService paymentVoucherService;

    @GetMapping
    public String getPaymentVouchers(Model model) {
        model.addAttribute("paymentVouchers", paymentVoucherService.getAllPaymentVouchers());
        return "admin/payment-voucher/index";
    }

    @GetMapping("/create")
    public String getCreatePaymentVoucher(Model model) {
        model.addAttribute("user", SecurityUtils.getCurrentUserLogin());
        return "admin/payment-voucher/create";
    }

    @GetMapping("/{id}/detail")
    public String getEditPaymentVoucher(@PathVariable Long id, Model model) {
        model.addAttribute("paymentVoucher", paymentVoucherService.getPaymentVoucherById(id));
        model.addAttribute("user", SecurityUtils.getCurrentUserLogin());
        return "admin/payment-voucher/detail";
    }
}
