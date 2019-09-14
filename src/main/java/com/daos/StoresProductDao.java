package com.daos;

import com.models.manytomany.Product;
import com.models.manytomany.Store;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("Duplicates")
@Repository
public class StoresProductDao {
    @Autowired
    private SessionFactory _sessionFactory;

    public void save(Store store) {
        Session session = _sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            // insert into store (name) values (?)
            // insert into product (barcode, name) values (?, ?)
            // insert into product (barcode, name) values (?, ?)
            Long storeId = (Long) session.save(store);
            System.out.println("saved store:" + storeId); // save returns saved object with id
            // insert into store_product (fk_store, fk_product) values (?, ?)
            // insert into store_product (fk_store, fk_product) values (?, ?)
            transaction.commit();
        } catch (Exception e) {
            System.out.println("transaction will be rolled back");
            throw e;
        } finally {
            session.close();
        }
    }


    public void save(Product product) {
        Session session = _sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            // insert into product (barcode, name) values (?, ?)
            // insert into store (name) values (?)
            Long productId = (Long) session.save(product);
            System.out.println("saved product:" + productId); // save returns saved object with id
            // This will not happen ------ insert into store_product (fk_store, fk_product) values (?, ?)
            // because product has mappedBy. From mappedBy (inverse) side, entry in association table will not be made.
            transaction.commit();
        } catch (Exception e) {
            System.out.println("transaction will be rolled back");
            throw e;
        } finally {
            session.close();
        }

    }

    public void get(Long id) {
        Session session = _sessionFactory.openSession();
        Store store = (Store)session.get(Store.class, id); //  select store0_.store_id as store_id1_3_0_, store0_.name as name2_3_0_ from store store0_ where store0_.store_id=?

        // @OneToMany and @ManyToMany is has default FetchTYPE.LAZY. so, unless you do store.getProducts(), it won't fetch products from db.
        // when store is retrieved, proxy objects are injected into its products. These proxy objects retrieve actual products from db when whey are accessed as follows.
        //List<Product> products = store.getProducts(); //  select products0_.fk_store as fk_store1_3_0_, products0_.fk_product as fk_produ2_4_0_, product1_.product_id as product_1_2_1_, product1_.barcode as barcode2_2_1_, product1_.name as name3_2_1_ from store_product products0_ inner join product product1_ on products0_.fk_product=product1_.product_id where products0_.fk_store=?

        session.close();

        // store object became detached object as session got closed before.
        // Lazy objects can be retrieved only inside the session.
        //List<Product> products = store.getProducts(); //  select products0_.fk_store as fk_store1_3_0_, products0_.fk_product as fk_produ2_4_0_, product1_.product_id as product_1_2_1_, product1_.barcode as barcode2_2_1_, product1_.name as name3_2_1_ from store_product products0_ inner join product product1_ on products0_.fk_product=product1_.product_id where products0_.fk_store=?

        Session session2 = _sessionFactory.openSession();
        session2.lock(store, LockMode.NONE);// attaching detached object to a new session
        List<Product> products = store.getProducts();//  select products0_.fk_store as fk_store1_3_0_, products0_.fk_product as fk_produ2_4_0_, product1_.product_id as product_1_2_1_, product1_.barcode as barcode2_2_1_, product1_.name as name3_2_1_ from store_product products0_ inner join product product1_ on products0_.fk_product=product1_.product_id where products0_.fk_store=?
        System.out.println(products);
        session2.close();
    }


}
