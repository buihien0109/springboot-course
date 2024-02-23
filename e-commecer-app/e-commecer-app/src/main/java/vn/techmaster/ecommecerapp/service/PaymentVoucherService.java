package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.dto.PaymentVoucherDto;
import vn.techmaster.ecommecerapp.model.projection.PaymentVoucherPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertPaymentVoucherRequest;
import vn.techmaster.ecommecerapp.repository.PaymentVoucherRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentVoucherService {
    private final PaymentVoucherRepository paymentVoucherRepository;
    private final UserRepository userRepository;

    public List<PaymentVoucherDto> getAllPaymentVouchers() {
        return paymentVoucherRepository.getAllPaymentVouchersDto();
    }

    public PaymentVoucherDto getPaymentVoucherById(Long id) {
        return paymentVoucherRepository.getPaymentVoucherDtoById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy phiếu chi với id: " + id));
    }

    public PaymentVoucherPublic createPaymentVoucher(UpsertPaymentVoucherRequest request) {
        // get current user
        User currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin == null) {
            throw new ResouceNotFoundException("Không tìm thấy người dùng hiện tại");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy người dùng với id: " + request.getUserId()));

        // create new payment voucher
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setPurpose(request.getPurpose());
        paymentVoucher.setNote(request.getNote());
        paymentVoucher.setAmount(request.getAmount());
        paymentVoucher.setUser(user);

        // save to database
        paymentVoucherRepository.save(paymentVoucher);

        return PaymentVoucherPublic.of(paymentVoucher);
    }

    public PaymentVoucherPublic updatePaymentVoucher(Long id, UpsertPaymentVoucherRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy người dùng với id: " + request.getUserId()));

        // find payment voucher by id
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy phiếu chi với id: " + id));

        // update payment voucher
        paymentVoucher.setPurpose(request.getPurpose());
        paymentVoucher.setNote(request.getNote());
        paymentVoucher.setAmount(request.getAmount());
        paymentVoucher.setUser(user);

        // save to database
        paymentVoucherRepository.save(paymentVoucher);

        return PaymentVoucherPublic.of(paymentVoucher);
    }

    public void deletePaymentVoucherById(Long id) {
        PaymentVoucher paymentVoucher = paymentVoucherRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy phiếu chi với id: " + id));
        paymentVoucherRepository.delete(paymentVoucher);
    }
}
