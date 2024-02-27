package com.example.loaadataspringbatch.configuration;


import com.example.loaadataspringbatch.listener.JobListener;
import com.example.loaadataspringbatch.model.CsvEntry;
import com.example.loaadataspringbatch.step.CsvEntryItemProcessor;
import com.example.loaadataspringbatch.step.CsvEntryItemReader;
import com.example.loaadataspringbatch.step.CsvEntryItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public CsvEntryItemReader csvReader(){
        return new CsvEntryItemReader();
    }

    @Bean
    public CsvEntryItemProcessor csvProcessor(){
        return new CsvEntryItemProcessor();
    }


    @Bean
    public CsvEntryItemWriter csvWriter(){
        return new CsvEntryItemWriter();
    }
    @Bean
    public Job job(JobListener jobListener){
        return jobBuilderFactory.get("job")
                .start(step())
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .build();
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("step 1")
                .<CsvEntry, CsvEntry> chunk(10000)
                .reader(csvReader())
                .processor(csvProcessor())
                .writer(csvWriter())
                .build();
    }

    }
