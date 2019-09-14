package com.models.onetomany_manytoone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private long id;

    @Column(name="name")
    private String name;

    // cart_id will be FK column name inside item table and fk_cart will be a name of foreign key constraint
    // it has an attribute referencedColumnName also. This parameter declares the column in the targeted entity that will be used to the join
    // only @JoinColumn without name without name attribute, will create a foreign key column with name cart_id (table name_pk column name).
    @ManyToOne
    @JoinColumn(name="cart_id", nullable=false, foreignKey=@ForeignKey(name="fk_cart"))
    private Cart cart;

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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        //sb.append(", cart=").append(cart);
        sb.append('}');
        return sb.toString();
    }
}
