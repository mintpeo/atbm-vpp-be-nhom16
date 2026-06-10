package com.atbm.appvppbe.controller;

import com.atbm.appvppbe.dto.entity.CartItem;
import com.atbm.appvppbe.dto.request.AddToCartReq;
import com.atbm.appvppbe.dto.request.ShowCartReq;
import com.atbm.appvppbe.service.CartSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class CartCon {
    private final CartSer ser;

    @PostMapping("/add")
    public void addToCart(@RequestBody AddToCartReq req) {
        ser.addToCart(req);
    }

    @PostMapping("/show")
    public List<CartItem> showCart(@RequestBody ShowCartReq req) {
        return ser.showCart(req);
    }
}
