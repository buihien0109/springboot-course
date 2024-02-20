package vn.techmaster.ecommecerapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.SupplierService;
import vn.techmaster.ecommecerapp.service.TransactionService;

@Controller
@RequestMapping("/admin/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final TransactionService transactionService;

    @GetMapping
    public String getSupplierPage(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "admin/supplier/index";
    }

    @GetMapping("/create")
    public String getSupplierCreatePage() {
        return "admin/supplier/create";
    }

    @GetMapping("{id}/detail")
    public String getSupplierPage(Model model, @PathVariable Long id) {
        model.addAttribute("supplier", supplierService.getSupplierById(id));
        model.addAttribute("transactions", transactionService.getTransactionsBySupplierId(id)); // done
        return "admin/supplier/detail";
    }
}
