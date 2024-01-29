package com.example.batchdemo.application.dormant;

import com.example.batchdemo.batch.ItemWriter;
import com.example.batchdemo.customer.Customer;
import com.example.batchdemo.EmailProvider;
import org.springframework.stereotype.Component;

@Component
public class PreDormantBatchItemWriter implements ItemWriter<Customer> {
    private final EmailProvider emailProvider;

    public PreDormantBatchItemWriter() {
        this.emailProvider = new EmailProvider.Fake();
    }

    public PreDormantBatchItemWriter(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }


    @Override
    public void write(Customer customer) {
        emailProvider.send(
                customer.getEmail(),
                "곧 휴면계정으로 전환 됩니다.",
                "휴면계정으로 사용되기를 원치 않으시다면, 1주일 내에 로그인해주세요."
        );

    }
}
