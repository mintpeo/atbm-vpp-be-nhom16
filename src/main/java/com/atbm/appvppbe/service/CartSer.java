package com.atbm.appvppbe.service;

import com.atbm.appvppbe.dto.entity.Cart;
import com.atbm.appvppbe.dto.entity.CartItem;
import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.AddToCartReq;
import com.atbm.appvppbe.dto.request.ShowCartReq;
import com.atbm.appvppbe.repository.CartItemRep;
import com.atbm.appvppbe.repository.CartRep;
import com.atbm.appvppbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartSer {
    private final CartRep rep;
    private final CartItemRep cartItemRep;
    private final UserRep userRep;

    // Show cart item with id
    public List<CartItem> showSelected(long[] selectedItems) {
        List<CartItem> list = new ArrayList<>();
        for (long i : selectedItems) {
            CartItem cartItem = cartItemRep.findById(i).orElse(null);
            list.add(cartItem);
        }
        return list;
    }

    // Check user have cart?
    private Cart checkUserCart(long userId) {
        Cart cart = rep.findCartByUserId(userId).orElse(null);
        if (cart == null) {
            User user = userRep.findById(userId).orElse(null);

            // Create new cart
            cart = new Cart();
            cart.setUser(user);
            cart = rep.save(cart);
        }
        return cart;
    }

    // Show cart
    public List<CartItem> showCart(ShowCartReq req) {
        Cart cart = checkUserCart(req.getUserId());
        return cartItemRep.findByCartId(cart.getId());
    }

    public void addToCart(AddToCartReq req) {
        Cart cart = checkUserCart(req.getUserId());

        // Save item
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProductId(req.getProductId());
        item.setType(req.getType());
        item.setImage(req.getImage());
        item.setQuantity(req.getQuantity());
        cartItemRep.save(item);
    }
}
