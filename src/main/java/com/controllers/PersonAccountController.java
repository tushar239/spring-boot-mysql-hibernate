package com.controllers;

import com.daos.PersonAccountDao;
import com.models.onetoone.Account;
import com.models.onetoone.AccountType;
import com.models.onetoone.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/personaccount")
public class PersonAccountController {

    @Autowired
    private PersonAccountDao personAccountDao;

    @RequestMapping(value = "/savePerson")
    @ResponseBody
    public String create() {
        try {
            Person person = createPerson();

            personAccountDao.save(person);

        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Stores and Products succesfully saved!";
    }

    private Person createPerson() {
        Person person = new Person();
        person.setName("T");

        Account account = new Account();
        account.setAccountType(AccountType.CHECKING);

        person.setAccount(account);

        account.setPerson(person);

        return person;
    }
}
