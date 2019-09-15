package com.daos;

import com.inheritance.joined.Animal;
import com.inheritance.joined.Pet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalDao {

    @Autowired
    private SessionFactory _sessionFactory;


    public void save(Animal animal) {
        Session session = _sessionFactory.openSession();
        try {

            Transaction transaction = session.beginTransaction();

            // insert into animal (species) values (?)
            // insert into pet (name, animal_id) values (?, ?)
            Long animalId = (Long) session.save(animal);
            System.out.println("saved animal:" + animalId); // save returns saved object with id
            transaction.commit();

        } catch (Exception e) {
            System.out.println("transaction will be rolled back");
            throw e;
        } finally {
            session.close();
        }
    }

    public void get(Long animalId) {
        Session session = _sessionFactory.openSession();
        Query query = session.createQuery("from Animal where id=:animalId");
        query.setParameter("animalId", animalId);
        /*
            select animal0_.animal_id as animal_i1_1_,
                   animal0_.species as species2_1_,

                   animal0_1_.name as name1_5_,
                   case when animal0_1_.pet_id is not null then 1
                        when animal0_2_.wild_id is not null then 2
                        when animal0_.animal_id is not null then 0 end as clazz_

                   from animal animal0_
                        left outer join pet animal0_1_ on animal0_.animal_id=animal0_1_.pet_id
                        left outer join wild animal0_2_ on animal0_.animal_id=animal0_2_.wild_id

                   where animal0_.animal_id=?

         */
        Animal animal = (Animal) query.uniqueResult();// hibernate will automatically identify that animal_id=1 is in Pet table.

        if (animal instanceof Pet) {
            System.out.println("it's a pet: " + animal);
        }

        session.close();
    }
}
