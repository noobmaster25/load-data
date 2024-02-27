package com.example.loaadataspringbatch.step;

import com.example.loaadataspringbatch.model.CsvEntry;
import com.example.loaadataspringbatch.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class CsvEntryItemWriter implements ItemWriter<CsvEntry> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //event_time, event_type, product_id, category_id, brand, price, user_id, user_session
    private final String insertQuery = "INSERT INTO evento_usuario VALUES (?,?,?,?,?,?,?,?)";
    @Override
    public void write(List<? extends CsvEntry> list) throws Exception {
        for (CsvEntry entry : list){
            jdbcTemplate.update(insertQuery,
                    DateTimeUtil.dateTimeFormat(entry.getEventTime()),
                    entry.getEventType(),
                    entry.getProductId(),
                    entry.getCategoryId(),
                    entry.getBrand(),
                    entry.getPrice(),
                    entry.getUserId(),
                    entry.getUserSession());
        }
    }
}
