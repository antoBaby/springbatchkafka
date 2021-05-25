package com.anto.springbatchkafka.batch;

import com.anto.springbatchkafka.exception.TransException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.util.List;

public class KafkaWriter implements ItemWriter<Object> {


    @Autowired
    KafkaTemplate kafkaTemplate;

    @Retryable(value = TransException.class, maxAttemptsExpression = "${batch.retry.count}", backoff = @Backoff(delayExpression = "${batch.retry.delay}"))
    @Override
    public void write(List<?> recordList) throws Exception {

    }


    @Recover
    public void ExRecover(TransException exception) {

    }
}
