package com.example.loaadataspringbatch.step;

import com.example.loaadataspringbatch.model.CsvEntry;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CsvEntryItemReader extends FlatFileItemReader<CsvEntry> {
    public CsvEntryItemReader() {
        setName("personReader");
        setResource(new ClassPathResource("data.csv"));
        setLinesToSkip(1);
        setLineMapper(getLineMapper());
    }

    private LineMapper<CsvEntry> getLineMapper() {
        DefaultLineMapper<CsvEntry> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        String[] columnsToBeInserted = new String[]{"event_time",
                "event_type",
                "product_id",
                "category_id",
                "category_code",
                "brand","price",
                "user_id",
                "user_session"};

        tokenizer.setNames(columnsToBeInserted);

        BeanWrapperFieldSetMapper<CsvEntry> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CsvEntry.class);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        tokenizer.setDelimiter(",");
        return lineMapper;
    }

}
