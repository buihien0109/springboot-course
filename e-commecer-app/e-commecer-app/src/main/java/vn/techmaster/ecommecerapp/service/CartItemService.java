package vn.techmaster.ecommecerapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.CartItem;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.request.UpdateCartItemRequest;
import vn.techmaster.ecommecerapp.repository.CartItemRepository;

import java.util.List;

@Service
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    // create method update cart item by id and quantity
    public void updateCartItemById(UpdateCartItemRequest request, Long id) {
        log.info("Update cart item by id: " + id);
        log.info("Update cart item quantity: " + request.getQuantity());
        // find cart item by id, if cart item not found, throw exception
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cart item not found"));

        // check if product quantity is enough
        if (cartItem.getProduct().getStockQuantity() < cartItem.getQuantity() + request.getQuantity()) {
            throw new BadRequestException("Product quantity is not enough");
        }

        // update cart item quantity
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());

        // save cart item
        cartItemRepository.save(cartItem);
    }

    // create method delete cart item by id
    public void deleteCartItemById(Long id) {
        // find cart item by id, if cart item not found, throw exception
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cart item not found"));

        // delete cart item
        cartItemRepository.delete(cartItem);
    }
}
