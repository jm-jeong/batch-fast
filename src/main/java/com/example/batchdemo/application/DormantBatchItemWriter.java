package com.example.batchdemo.application;

import com.example.batchdemo.batch.ItemWriter;
import com.example.batchdemo.model.Customer;
import com.example.batchdemo.repository.CustomerRepository;
import com.example.batchdemo.service.EmailProvider;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchItemWriter implements ItemWriter<Customer> {
    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchItemWriter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }


    @Override
    public void write(Customer item) {
        //3 휴면계정 상태 변경
        customerRepository.save(item);
        //4 메일을 보낸다
        emailProvider.send(item.getEmail(), "휴면 전환 메일입니다.", "내용");
    }
}
