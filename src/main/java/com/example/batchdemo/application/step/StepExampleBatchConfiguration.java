package com.example.batchdemo.application.step;

import com.example.batchdemo.batch.Job;
import com.example.batchdemo.batch.Step;
import com.example.batchdemo.batch.StepJob;
import com.example.batchdemo.batch.StepJobBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class StepExampleBatchConfiguration {

    @Bean
    public Job stepExampleBatchJob(
            Step step1,
            Step step2,
            Step step3
    ) {

        return new StepJobBuilder()
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Step step1() {
        return new Step(() -> System.out.println("step1"));
    }
    @Bean
    public Step step2() {
        return new Step(() -> System.out.println("step2"));
    }
    @Bean
    public Step step3() {
        return new Step(() -> System.out.println("step3"));
    }
}
