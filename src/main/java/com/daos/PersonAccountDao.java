package com.daos;

import com.models.onetoone.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
