package com.example.loaadataspringbatch.step;

import com.example.loaadataspringbatch.model.CsvEntry;

import com.example.loaadataspringbatch.util.DateTimeUtil;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;



@Component
public class CsvEntryItemProcessor implements ItemProcessor<CsvEntry, CsvEntry> {

    @Override
    public CsvEntry process(CsvEntry csvEntry) throws Exception {

        return csvEntry;
    }

}
