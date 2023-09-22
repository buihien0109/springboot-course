package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Discount;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.DiscountPublic;
import vn.techmaster.ecommecerapp.repository.DiscountRepository;

import java.util.Date;
import java.util.List;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<DiscountPublic> findAll() {
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream().map(DiscountPublic::of).toList();
    }

    public DiscountPublic findById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find discount by id " + id));
        return DiscountPublic.of(discount);
    }

    public DiscountPublic save(Discount discount) {
        Discount discountSaved = discountRepository.save(discount);
        return DiscountPublic.of(discountSaved);
    }

    public void deleteById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find discount by id " + id));
        discountRepository.deleteById(id);
    }

    // get all discount not expired
    public List<DiscountPublic> findAllByNotExpired(Date date) {
        List<Discount> discounts = discountRepository.findByEndDateAfter(new Date());
        return discounts.stream().map(DiscountPublic::of).toList();
    }
}
