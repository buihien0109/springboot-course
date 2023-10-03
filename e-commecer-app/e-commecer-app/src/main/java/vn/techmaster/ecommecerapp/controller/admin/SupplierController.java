package vn.techmaster.ecommecerapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.ecommecerapp.service.SupplierService;

@Controller
@RequestMapping("/admin/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

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
        return "admin/supplier/detail";
    }
}
