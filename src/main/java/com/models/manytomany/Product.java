package com.models.manytomany;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private long id;

    @Column(unique = true)
    private String barcode;

    // IMP - Without mappedBy, it will create another association table product_store with columns (product_product_id, stores_store_id)
    // IMP - If the association is bidirectional, one side has to be the owner and one side has to be the inverse end (ie. it will be ignored when updating the relationship values in the association table):
    // So, the side which has the mappedBy attribute is the inverse side. The side which doesn't have the mappedBy attribute is the owner.
    // Here Store is the owner. So, when you save store, entries in store_product table will be added.
    // But when you save product, no entry will be added in store_product.
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products")
    private List<Store> stores;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public void addStore(Store store) {
        if(stores == null) {
            stores = new LinkedList<>();
        }
        //store.addProduct(this);
        stores.add(store);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", barcode='").append(barcode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
