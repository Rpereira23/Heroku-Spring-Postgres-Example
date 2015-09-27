package com.example.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * This Database works and you can now RESTfully interact with things in the database.
 *
 * Once all the initial setup is complete this is how you prep Heroku to deal with this project
 *
 * Go to CMD and do the following
 *
 * Create a heroku project if you don't already have one
 * $ heroku create --stack cedar
 *
 * IMPORTANT: Set the environment variables
 * $ heroku config:set SPRING_CLOUD_APP_NAME={Heroku Given App Name}
 * $ heroku config:set SPRING_PROFILES_ACTIVE={Your Profile Name you Decided On For This Project eg: 'cloud'}
 *
 * Create a Postgres DB if you don't already have one
 * $ heroku addons:add heroku-postgresql:hobby-dev
 *
 * Deploy project
 * $ git push heroku master
 *
 */
//@SpringBootApplication
@Component
@RestController
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories
public class App implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Autowired
    PersonRepo repo;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // save a couple of persons
        repo.save(new Person("Anthony"));
        repo.save(new Person("Karissa"));
        repo.save(new Person("Toni"));
        repo.save(new Person("Richelle"));
        repo.save(new Person("Dom"));

        // fetch all persons
        log.info("Persons found with findAll():");
        log.info("-------------------------------");
        for (Person person : repo.findAll()) {
            log.info(person.toString());
        }
        System.out.println();

        // fetch an individual person by ID
        Person person = repo.findOne(1L);
        log.info("Person found with findOne(1L):");
        log.info("--------------------------------");
        log.info(person.toString());

        // fetch persons by last name
        log.info("Person found with findByLastName('Karissa'):");
        log.info("--------------------------------------------");
        for (Person bauer : repo.findByNameIgnoreCase("Karissa")) {
            log.info(bauer.toString());
        }
    }

    @RequestMapping("/test")
    public Person testJSON() {
        return new Person("Text Value");
    }

    @RequestMapping("/people")
    public ArrayList<Person> getPeopleListJSON() {
        ArrayList<Person> peopleList = new ArrayList<>();

        for (Person person : repo.findAll()) {
            peopleList.add(person);
        }

        return peopleList;
    }

    @RequestMapping("/add/{person}")
    public ArrayList<Person> addPersonToDB(@PathVariable String person) {
        repo.save(new Person(person));
        ArrayList<Person> result = new ArrayList<>();
        for (Person ans : repo.findByNameIgnoreCase(person)) {
            result.add(ans);
        }
        return result;
    }

    @RequestMapping("/get/{person}")
    public ArrayList<Person> getPersonFromDB(@PathVariable String person) {
        ArrayList<Person> result = new ArrayList<>();
        for (Person ans : repo.findByNameIgnoreCase(person)) {
            result.add(ans);
        }
        return result;
    }

}