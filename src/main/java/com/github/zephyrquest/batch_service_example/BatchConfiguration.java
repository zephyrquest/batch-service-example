package com.github.zephyrquest.batch_service_example;

import com.github.zephyrquest.batch_service_example.model.Person;
import com.github.zephyrquest.batch_service_example.processor.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

// job configuration

@Configuration
public class BatchConfiguration {

    /* creates an ItemReader. It looks for a file called sample-data.csv
    and parses each line item with enough information to turn it into a Person. */
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .targetType(Person.class)
                .build();
    }

    /* creates an instance of the PersonItemProcessor defined earlier, meant to convert the data to upper case. */
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /* creates an ItemWriter. This one is aimed at a JDBC destination
    and automatically gets a copy of the dataSource created by Spring Boot.
    It includes the SQL statement needed to insert a single Person, driven by Java record components. */
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    // defines the job
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    /* defines a single step.
    Jobs are built from steps, where each step can involve a reader, a processor, and a writer.
    chunk() is prefixed <Person,Person> because it is a generic methodd.
    Input and output types of each “chunk” of processing and lines up with ItemReader<Person> and ItemWriter<Person> */
    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<Person> reader, PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person> chunk(3, transactionManager) // writes up to three records at a time
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
