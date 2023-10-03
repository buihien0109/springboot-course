package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.techmaster.ecommecerapp.service.TransactionService;

@RestController
@RequestMapping("/api/v1/admin/transactions")
@RequiredArgsConstructor
public class TransactionResources {
    private final TransactionService transactionService;


}
