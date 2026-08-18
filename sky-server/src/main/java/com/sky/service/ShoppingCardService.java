package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import org.springframework.stereotype.Service;


public interface ShoppingCardService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);
}
