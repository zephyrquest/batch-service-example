package com.github.zephyrquest.batch_service_example.processor;

import com.github.zephyrquest.batch_service_example.model.Person;
import org.springframework.batch.item.ItemProcessor;

/* A common paradigm in batch processing is to ingest data, transform it, and then pipe it out somewhere else.
Here, PersonItemProcessor is a simple transformer that converts the names to uppercase */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) {
        final String firstName = person.firstName().toUpperCase();
        final String lastName = person.lastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        return transformedPerson;
    }
}
