package com.models.onetomany_manytoone;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart") // you can use uniqueConstraints, indexes attributes also.
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private long id;

    // You can have only @ManyToOne also for unidirectional access. It will create a foreign key in Item table.
    // For Bidirectional access, you need @OneToMany with mappedBy.
    // without mappedBy, it creates an association table with columns item_id,cart_id
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    // default FetchMode.SELECT - on retrieval of Cart object from db, it first fires select * from cart and then using cart ids, it fires another select query to find Items.

    // FetchMode.JOIN fires single query to fetch cart and items in one joined query. As it has to do it in one query, it disables FetchType.LAZY that is default for @OneToMany and @ManyToMany.
    //  select cart0_.id as id1_0_0_, items1_.cart_id as cart_id3_0_1_, items1_.id as id1_1_1_, items1_.id as id1_1_2_, items1_.cart_id as cart_id3_1_2_, items1_.name as name2_1_2_ from cart cart0_ left outer join item items1_ on cart0_.id=items1_.cart_id where cart0_.id=?
    @Fetch(FetchMode.JOIN)
    private Set<Item> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        item.setCart(this);//important

        if (items == null) {
            items = new HashSet<>();
        }
        items.add(item);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cart{");
        sb.append("id=").append(id);
        //sb.append(", items=").append(items);
        sb.append('}');
        return sb.toString();
    }
}
