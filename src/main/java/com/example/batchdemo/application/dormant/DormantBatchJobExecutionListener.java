package com.example.batchdemo.application.dormant;

import com.example.batchdemo.batch.JobExecution;
import com.example.batchdemo.batch.JobExecutionListener;
import com.example.batchdemo.EmailProvider;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchJobExecutionListener implements JobExecutionListener {
    private final EmailProvider emailProvider;

    public DormantBatchJobExecutionListener() {
        this.emailProvider = new EmailProvider.Fake();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        //  no-op

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        //비즈니스 로직
        emailProvider.send(
                "admin@fast.com",
                "배치 완료 알림",
                "BatchDormantJob이 수행되었습니다. Status : " + jobExecution.getStatus()
        );


    }
}
