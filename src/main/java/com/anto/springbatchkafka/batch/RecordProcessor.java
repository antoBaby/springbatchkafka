package com.anto.springbatchkafka.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class RecordProcessor implements ItemProcessor {

    @Override
    public Object process(Object record) throws Exception {
        return null;
    }
}
