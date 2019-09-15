package com.controllers;

import com.daos.AnimalDao;
import com.inheritance.joined.Animal;
import com.inheritance.joined.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/animal")
public class AnimalController {

    @Autowired
    private AnimalDao animalDao;

    @RequestMapping(value = "/savePet")
    @ResponseBody
    public String savePet() {
        try {
            Animal animal = createPet();
            animalDao.save(animal);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Pet successfully saved!";
    }

    private Animal createPet() {
        Pet pet = new Pet();
        pet.setName("dog");
        pet.setSpecies("s1");
        return pet;
    }

    @RequestMapping(value = "/getAnimal")
    @ResponseBody
    public String getAnimal() {
        try {
            savePet();
            animalDao.get(1L);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Pet successfully saved!";
    }

}
