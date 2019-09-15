package com.inheritance.joined;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/*
                    Animal table
                    ------
    animal_id                   species


                    Pet table
                    ---
    animal_id                       name
    (PK, FK from Animal table)

    animal_id is a PK for Pet table. It is a FK referenced from Animal table's primary key animal_id.

    To have different column name (pet_id), you can use @PrimaryKeyJoinColumn("pet_id")

 */

@Entity
@Table(name = "pet")
// @JoinColumn vs @PrimaryKeyJoinColumn
// @JoinColumn is used where primary key (or any other referencing column) of one table is foreign key in another table.
// @PrimaryKeyJoinColumn annotation is used for associated entities sharing the same primary key. It can be used with @oneToOne
// where primary key of two related tables are same, you should use this annotation instead of @JoinColumn.
// http://www.techferry.com/articles/hibernate-jpa-annotations.html#PrimaryKeyJoinColumn
// https://stackoverflow.com/questions/3417097/jpa-difference-between-joincolumn-and-primarykeyjoincolumn
@PrimaryKeyJoinColumn(name = "pet_id")
public class Pet extends Animal {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pet{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
