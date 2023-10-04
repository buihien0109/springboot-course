package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.ProductService;
import vn.techmaster.ecommecerapp.service.SupplierService;
import vn.techmaster.ecommecerapp.service.TransactionService;

@Controller
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final SupplierService supplierService;
    private final ProductService productService;

    @GetMapping
    public String getTransactionPage(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        return "admin/transaction/index";
    }

    @GetMapping("/create")
    public String getTransactionCreatePage(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("products", productService.findAll());
        return "admin/transaction/create";
    }

    @GetMapping("{id}/detail")
    public String getTransactionPage(Model model, @PathVariable Long id) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("transaction", transactionService.getTransactionById(id));
        model.addAttribute("products", productService.findAll());
        return "admin/transaction/detail";
    }
}
