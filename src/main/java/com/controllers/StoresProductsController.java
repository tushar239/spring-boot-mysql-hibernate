package com.controllers;

import com.daos.StoresProductDao;
import com.models.manytomany.Product;
import com.models.manytomany.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
    @ManyToMany unidirectional and bidirectional
        @JointTable annotation

    @OneToMany and @ManyToMany have default FetchType=LAZY.

    mappedBy - what happens if mappedBy is not there. What happens if you try to save an object which has mappedBy.

    Detached object.
    Fetching objects lazily.
    Reattaching detached object to new session.
*/

@Controller
@RequestMapping(value = "/storesproducts")
public class StoresProductsController {

    @Autowired
    private StoresProductDao storesProductDao;

    @RequestMapping(value = "/save")
    @ResponseBody
    public String create() {
        try {

            List<Store> stores = createStores();

            for (Store store : stores) {
                storesProductDao.save(store);
            }

            List<Product> products = createProducts();

            for (Product product : products) {
                storesProductDao.save(product);
            }

        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Stores and Products succesfully saved!";
    }

    private List<Store> createStores() {
        Product product1 = new Product();
        product1.setName("p1");
        product1.setBarcode("" + new Random().nextLong());

        Product product2 = new Product();
        product2.setName("p2");
        product2.setBarcode("" + new Random().nextLong());

        Store store1 = new Store();
        store1.setName("walmart");
        store1.addProduct(product1);
        store1.addProduct(product2);

        Store store2 = new Store();
        store2.setName("target");
        store2.addProduct(product1);
        store2.addProduct(product2);

        List<Store> stores = new LinkedList<>();
        stores.add(store1);
        stores.add(store2);

        return stores;
    }

    private List<Product> createProducts() {
        Store store3 = new Store();
        store3.setName("fred mayer");

        Product product3 = new Product();
        product3.setName("p3");
        product3.setBarcode("" + new Random().nextLong());
        product3.addStore(store3);

        List<Product> products = new LinkedList<>();
        products.add(product3);

        return products;
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public String get() {
        try {
            create();
            storesProductDao.get(1L);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Stores and Products retrieved successfully!";
    }
}
