package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCardMapper;
import com.sky.result.Result;
import com.sky.service.ShoppingCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCardServiceImpl implements ShoppingCardService {

    @Autowired
    private ShoppingCardMapper shoppingCardMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入购物车的商品是否已经存在
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        List<ShoppingCart> list = shoppingCardMapper.list(shoppingCart);
        //如果存在，数量加1
        if (list != null && list.size() > 0) {
            ShoppingCart existShoppingCart = list.get(0);
            existShoppingCart.setNumber(existShoppingCart.getNumber() + 1);
            shoppingCardMapper.update(existShoppingCart);
        } else {
            //如果不存在，插入购物车数据
            Long dishId=shoppingCartDTO.getDishId();
            Long setmealId=shoppingCartDTO.getSetmealId();
            //根据dishId或setmealId进行判断加入购物车的是套餐还是菜品
            if (dishId != null) {
                Dish byId = dishMapper.getById(dishId);
                shoppingCart.setName(byId.getName());
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());
            } else {
                if (setmealId == null) {
                    return;
                }
                Setmeal byId = setmealMapper.getById(setmealId);
                shoppingCart.setName(byId.getName());
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCardMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart build = ShoppingCart.builder()
                .userId(currentId)
                .build();
        List<ShoppingCart> list = shoppingCardMapper.list(build);
        return list;
    }


}
