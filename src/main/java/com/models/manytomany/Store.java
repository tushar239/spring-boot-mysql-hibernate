package com.models.manytomany;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id")
    private long id;

    private String name;

    // Unidirectional access: Just have @ManyToMany on one side. It will create an association table (store_product with store_id,product_id columns)
    // Default FetchType is LAZY for @OneToMany and @ManyToMany
    @ManyToMany(cascade = CascadeType.ALL)
    // You can customize the column names and columns that you want in association table using @JoinTable
    @JoinTable(name = "store_product",
            joinColumns = {@JoinColumn(name = "fk_store", referencedColumnName = "store_id", foreignKey = @ForeignKey(name = "fk_store"))},
            inverseJoinColumns = {@JoinColumn(name = "fk_product", referencedColumnName = "product_id", foreignKey = @ForeignKey(name = "fk_product"))
                    //, @JoinColumn(name = "fk_barcode", referencedColumnName = "barcode", foreignKey = @ForeignKey(name = "fk_barcode"))
            }// foreignKey helps to give a name to fk constraint that is created in db. without that hibernate will give any random name like FK_lxywdkek etc

            //, foreignKey = @ForeignKey(name = "fk_store")
            //, inverseForeignKey = @ForeignKey(name = "fk_product")
    )
    private List<Product> products;

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (products == null) {
            products = new LinkedList<>();
        }
        products.add(product);
        //product.addStore(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Store{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
