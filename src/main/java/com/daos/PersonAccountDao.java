package com.daos;

import com.models.onetoone.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
/*
    save() vs persist() and saveOrUpdate()
    https://javarevisited.blogspot.com/2012/09/difference-hibernate-save-vs-persist-and-saveOrUpdate.html

    - save() returns id. persist() returns void.

    - persist() method doesn't guarantee that the identifier value will be assigned to the persistent instance immediately, the assignment might happen at flush time.

    - persist() method guarantees that it will not execute an INSERT statement if it is called outside of transaction boundaries. save() method does not guarantee the same, it returns an identifier, and if an INSERT has to be executed to get the identifier (e.g. "identity" generator), this INSERT happens immediately, no matter if you are inside or outside of a transaction.

    - Fourth difference between save and persist method in Hibernate is related to previous difference on save vs persist. Because of its above behavior of persist method outside transaction boundary, its useful in long-running conversations with an extended Session context. On the other hand save method is not good in a long-running conversation with an extended Session context.

 */
@SuppressWarnings("Duplicates")
@Repository
public class PersonAccountDao {

    @Autowired
    private SessionFactory _sessionFactory;

    public void save(Person person) {
        Session session = _sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();

            // insert into account (accountType) values (?)
            // insert into person (account_id, name) values (?, ?)
            Long personId = (Long) session.save(person);
            System.out.println("saved person:" + personId); // save returns saved object with id

            transaction.commit();
        } catch (Exception e) {
            System.out.println("transaction will be rolled back");
            throw e;
        } finally {
            session.close();
        }

    }
}
