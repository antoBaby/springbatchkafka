package com.anto.springbatchkafka.config;

import com.anto.springbatchkafka.batch.JobCompletionListener;
import com.anto.springbatchkafka.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Value("${batch.delimiter}")
    private String lineDelimiter;

    @Value("${batch.skip.line}")
    private int lineSkip;

    @Value("${batch.line.chuck}")
    private int batchChuck;

    @Value("${batch.file.row.name}")
    private String fileColumnName;


    /**
     * Batch Job
     *
     * @param jobBuilderFactory
     * @param stepBuilderFactory
     * @param itemReader
     * @param itemProcessor
     * @param itemWriter
     * @return
     */
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                   ItemReader<Object> itemReader,
                   ItemProcessor<Object, Object> itemProcessor,
                   ItemWriter<Object> itemWriter) {
        Step step = stepBuilderFactory.get("file-step")
                .chunk(batchChuck)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("file-job").incrementer(new RunIdIncrementer())
                .listener(listener())
                .start(step)
                .build();
    }

    /**
     * Item Reader
     *
     * @return
     */
    @Bean
    public FlatFileItemReader<Object> itemReader() {

        FlatFileItemReader itemReader = new FlatFileItemReader();
        itemReader.setResource(new FileSystemResource("src/main/resources/User.csv"));
        itemReader.setName("file-reader");
        itemReader.setLinesToSkip(lineSkip);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    /**
     * Mapping file items into object
     *
     * @return
     */
    @Bean
    public LineMapper<Object> lineMapper() {
        DefaultLineMapper<Object> defaultLineMapper = new DefaultLineMapper();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(lineDelimiter);
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(fileColumnName);

        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper();
        mapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(mapper);
        return defaultLineMapper;
    }

    @Bean
    private JobExecutionListener listener() {
        return new JobCompletionListener();
    }

}
