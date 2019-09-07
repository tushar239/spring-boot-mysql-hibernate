package netgloo.controllers;

import netgloo.models.Cart;
import netgloo.daos.CartItemsDao;
import netgloo.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequestMapping(value = "/cartitems")
public class CartItemsController {

    @Autowired
    private CartItemsDao cartItemsDao;

    @RequestMapping(value = "/save")
    @ResponseBody
    public String create() {
        try {

            Cart cart = createCart();

            cartItemsDao.save(cart);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Cart succesfully saved!";
    }

    private Cart createCart() {
        Cart cart = new Cart();

        Item item1 = new Item();
        item1.setName("table" + UUID.randomUUID());
        //item1.setCart(cart);// this is done in Cart's addItem
        cart.addItem(item1);

        Item item2 = new Item();
        item2.setName("chair" + UUID.randomUUID());
        //item1.setCart(cart);// this is done in Cart's addItem
        cart.addItem(item2);
        return cart;
    }

    @RequestMapping(value = "/fetch")
    @ResponseBody
    public void fetch(Long id) {
        Cart cart = createCart();
        cartItemsDao.save(cart);
        cartItemsDao.fetch(id);
    }

    @RequestMapping(value = "/save_flush_update")
    @ResponseBody
    public void save_flush_update() {
        Cart cart = createCart();
        cartItemsDao.save_flush_update(cart);
    }

}
