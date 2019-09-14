package com.daos;

import com.models.onetomany_manytoone.Cart;
import com.models.onetomany_manytoone.Item;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/*
Hibernate In Action
Chapter 7.3 Joining Associations

HQL:
Item has OneToMany relation with Bid. Item has Set<Bid>

 from Item item
   join item.bids bid
       where item.description like '%gc%'
       and bid.amount > 100

NOTE: There is no need of 'ON' clause

The resulting SQL is as follows:

   select I.DESCRIPTION, I.CREATED, I.SUCCESSFUL_BID,
          B.BID_ID, B.AMOUNT, B.ITEM_ID, B.CREATED
   from ITEM I
   inner join BID B on I.ITEM_ID = B.ITEM_ID

   where I.DESCRIPTION like '%gc%'
        and B.AMOUNT > 100


   Query q = session.createQuery("from Item item join item.bids bid");
   Iterator pairs = q.list().iterator();

    while ( pairs.hasNext() ) {
          Object[] pair = (Object[]) pairs.next();
          Item item = (Item) pair[0];
          Bid bid = (Bid) pair[1];
    }

    Instead of a List of Items, this query returns a List of Object[] arrays. At index 0 is the Item, and at index 1 is the Bid.
    A particular Item may appear multiple times, once for each associated Bid.

    This is all different from the case of a query with an eager "fetch join". The query with the fetch join returned a List of Items, with initialized bids collections.


    Implicit Joins

    from Bid bid where bid.item.description like '%gc%'

    This results in an implicit join on the many-to-one associations from Bid to Item. Implicit joins are always directed along many-to-one or one-to-one associations, never through a collection-valued association (you can’t write item.bids.amount).
*/


@Repository
//@Transactional // HibernateTransactionManager is added in spring context. So, this will be hibernate transaction
public class CartItemsDao {

    @Autowired
    private SessionFactory _sessionFactory;

    /*private Session getSession() {
        return _sessionFactory.getCurrentSession();
    }*/

    public void save(Cart cart) {

        Session session = _sessionFactory.openSession();
        try {

            Transaction transaction = session.beginTransaction();
            Long savedCartId = (Long) session.save(cart);// without Cascade on OneToMany, items will not be inserted
            System.out.println("savedCart:" + savedCartId); // save returns saved object with id
            session.flush();
            transaction.commit();

            // If session is cleared, Cart object will not be inside session object. So, below session.get will fire a select query.
            // Otherwise, session.get will retrieve Cart object from session only. It won't fire a select query.
            //session.clear();

            //Cart cart1 = (Cart) session.get(Cart.class, new Long(1));
            //System.out.println(cart1.getId());
            //System.out.println(cart1.getItems());
        } catch (Exception e) {
            System.out.println("transaction will be rolled back");
            throw e;
        } finally {
            session.close();
        }
    }

    public void save_different(Cart cart) {
        {
            Session session = _sessionFactory.openSession();

            try {
                Transaction transaction1 = session.beginTransaction();
                Long savedCartId = (Long) session.save(cart);// without Cascade on OneToMany, items will not be inserted
                System.out.println("savedCart:" + savedCartId); // save returns saved object with id
                transaction1.commit();
            } catch (Exception e) {
                System.out.println("transaction will be rolled back");
                throw e;
            } finally {
                session.close();
            }

        }

        // New Session doesn't have Cart object in it. So, select query will be fired to retrieve it from DB.
        {
            Session session = _sessionFactory.openSession();
            try {
                Cart cart1 = (Cart) session.get(Cart.class, new Long(1));// retrieved from session only. No DB query
                System.out.println(cart1.getId());
                System.out.println(cart1.getItems());
            } finally {
                session.close();
            }

        }
    }

    public void save_flush_update(Cart cart) {
        // save in one session
        {
            Session session = _sessionFactory.openSession();

            try {
                Transaction trans = session.beginTransaction();
                Long savedCartId = (Long) session.save(cart);// without Cascade on OneToMany, items will not be inserted
                System.out.println("savedCart:" + savedCartId); // save returns saved object with id
                trans.commit();
            } catch (Exception e) {
                System.out.println("transaction will be rolled back");
                throw e;
            } finally {
                session.close();
            }

        }

        // retrieve and flush in another session
        {
            Session session = _sessionFactory.openSession();
            //session.setFlushMode(FlushMode.COMMIT); // IMPORTANT: session.flush() is automatically called before any Query execution/tx.commit(). To do flush only on Commit, you can change flush mode to FlushMode.COMMIT.
            try {
                Cart cart1 = (Cart) session.get(Cart.class, new Long(1));// retrieved from session only. No DB query

                Transaction trans = session.beginTransaction();
                Set<Item> items = cart1.getItems();
                for (Item item : items) {
                    item.setName(item.getName()+"testing flush");
                }
                session.saveOrUpdate(cart1); // IMPORTANT: update/saveOrUpdate doesn't fire update queries. It waited for session.flush()/trans.commit() to execute.

                /*
                    flush will be called automatically when trans.commit() is called. But calling flush() explicitly will sync in-memory changes to objects in session with database. flush() doesn't do commit though.
                    update item set cart_id=?, name=? where item_id=?
                    update item set cart_id=?, name=? where item_id=?
                 */
                session.flush();
                trans.commit();// IMPORTANT: session.flush() is automatically called before any Query execution/tx.commit(). To do flush only on Commit, you can change flush mode to FlushMode.COMMIT.
            } finally {
                session.close();
            }

        }
    }

    public Object fetch(Long cartId) {
        Session session = _sessionFactory.openSession();
        try {

            {
            /*
            select cart0_.id as id1_0_0_,
                    items1_.id as id1_1_1_,
                    items1_.cart_id as cart_id3_1_1_,
                    items1_.name as name2_1_1_
            from cart cart0_ inner join item items1_ on cart0_.id=items1_.cart_id
            where cart0_.id=1

            select items0_.cart_id as cart_id3_0_0_,
                    items0_.id as id1_1_0_,
                    items0_.id as id1_1_1_,
                    items0_.cart_id as cart_id3_1_1_,
                    items0_.name as name2_1_1_
            from item items0_
            where items0_.cart_id=?

             */

                // JOIN/LEFT JOIN returns Object[]. Object[0] will be Cart and Object[1] will be Item
                // e.g. Object[0] = cart_id 1, Object[1] = item_id 1
                // e.g. Object[1] = cart_id 1, Object[1] = item_id 2
                Query query = session.createQuery(/*select c.items*/"from Cart c JOIN c.items i WHERE c.id=:cartId");
                query.setParameter("cartId", cartId);
                List list = query.list();
                // https://www.concretepage.com/hibernate/hibernate-hql-associations-and-inner-join-left-outer-join-right-outer-join-cross-join-example
                for (int i = 0; i < list.size(); i++) {
                    Object[] row = (Object[]) list.get(i);
                    Cart cart = (Cart) row[0];
                    Item item = (Item) row[1];

                    System.out.println(cart.getId() + ":::" + cart.getItems());
                    System.out.println(item);
                }
            }

            // JOIN FETCH/LEFT JOIN FETCH returns cart object having items in it

            /*
                select cart0_.cart_id as cart_id1_0_0_,
                        items1_.item_id as item_id1_1_1_,
                        items1_.cart_id as cart_id3_1_1_,
                        items1_.name as name2_1_1_,
                        items1_.cart_id as cart_id3_0_0__,
                        items1_.item_id as item_id1_1_0__
                from cart cart0_ inner join item items1_ on cart0_.cart_id=items1_.cart_id
                where
                cart0_.cart_id=?

                Just one query (which is better)
             */
            {
                Query query = session.createQuery("from Cart c JOIN FETCH c.items i WHERE c.id=:cartId");
                query.setParameter("cartId", cartId);
                List list = query.list();
                // https://www.concretepage.com/hibernate/hibernate-hql-associations-and-inner-join-left-outer-join-right-outer-join-cross-join-example
                for (int i = 0; i < list.size(); i++) {
                    Cart cart = (Cart) list.get(i);

                    System.out.println(cart.getId() + ":::" + cart.getItems());
                }
            }
            /*{
                // Native SQL Query
                Query query = session.createSQLQuery("select c.cart_id, i.item_id, i.name from myschema.cart as c " +
                        "JOIN myschema.item as i ON c.cart_id=i.cart_id " +
                        "WHERE " +
                        "c.cart_id=" + cartId);
                List list = query.list();
                for (int i = 0; i < list.size(); i++) {
                    Object[] row = (Object[]) list.get(i);
                    BigInteger cId = (BigInteger) row[0];
                    BigInteger iId = (BigInteger) row[1];
                    String iName = (String) row[2];

                    System.out.println(cId + ", " + iId + ", " + iName);
                }
            }*/

        } finally {
            session.close();
        }
        return null;
    }
}
