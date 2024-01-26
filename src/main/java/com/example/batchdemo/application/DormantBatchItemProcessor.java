package com.example.batchdemo.application;

import com.example.batchdemo.batch.ItemProcessor;
import com.example.batchdemo.model.Customer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DormantBatchItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer item) {
        //2 휴면계정 대상 추출 및 변환
        //     로그인 날짜      / 365일 전 / 오늘
        final boolean isDormantTarget = LocalDateTime.now()
                .minusDays(365)
                .isAfter(item.getLoginAt());

        if (isDormantTarget) {
            item.setStatus(Customer.Status.DORMANT);
            return item;
        } else {
            return null;
        }

    }
}
