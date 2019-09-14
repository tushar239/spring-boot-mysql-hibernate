package com.models.onetoone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private long id;

    // Without mappedBy, it will create person_id column in account table. NOTE: It won't create an association table like OneToMany and ManyToMany.
    @OneToOne(mappedBy = "account")
    //@JoinColumn(name = "person_id")
    private Person person;

    @Enumerated(EnumType.STRING) // to save enum as a string. Without this, it will be saved as an Integer (0,1 etc)
    private AccountType accountType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        //person.setAccount(this);
        this.person = person;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("id=").append(id);
        sb.append(", accountType=").append(accountType);
        sb.append('}');
        return sb.toString();
    }
}
