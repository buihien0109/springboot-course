package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.TransactionService;

@Controller
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public String getTransactionPage(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        return "admin/transaction/index";
    }

    @GetMapping("/create")
    public String getTransactionCreatePage() {
        return "admin/transaction/create";
    }

    @GetMapping("{id}/detail")
    public String getTransactionPage(Model model, @PathVariable Long id) {
        model.addAttribute("transaction", transactionService.getTransactionById(id));
        return "admin/transaction/detail";
    }
}
