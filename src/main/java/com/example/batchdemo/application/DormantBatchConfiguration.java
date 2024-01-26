package com.example.batchdemo.application;

import com.example.batchdemo.batch.Job;
import com.example.batchdemo.batch.SimpleTasklet;
import com.example.batchdemo.model.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DormantBatchConfiguration {

    @Bean
    public Job dormantBatchJob(
            DormantBatchItemReader itemReader,
            DormantBatchItemProcessor itemProcessor,
            DormantBatchItemWriter itemWriter,
            DormantBatchJobExecutionListener listener

    ) {
        return Job.builder()
                .itemReader(itemReader)
                .itemProcessor(itemProcessor)
                .itemWriter(itemWriter)
                .jobExecutionListener(listener)
                .build();
    }
}
