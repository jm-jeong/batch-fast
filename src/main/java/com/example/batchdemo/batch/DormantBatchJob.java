package com.example.batchdemo.batch;

import com.example.batchdemo.model.Customer;
import com.example.batchdemo.repository.CustomerRepository;
import com.example.batchdemo.service.EmailProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DormantBatchJob {

    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    public JobExecution execute() {
        final JobExecution jobExecution = new JobExecution();
        jobExecution.setStatus(BatchStatus.STARTING);
        jobExecution.setStartTime(LocalDateTime.now());
        int pageNo = 0;

        try {
            while (true) {
                //1 유저를 조회

                final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending());
                final Page<Customer> page = customerRepository.findAll(pageRequest);

                final Customer customer;
                if (page.isEmpty()) {
                    break;
                } else {
                    pageNo++;
                    customer = page.getContent().get(0);
                }

                //2 휴면계정 대상 추출 및 변환
                //     로그인 날짜      / 365일 전 / 오늘
                final boolean isDormantTarget = LocalDateTime.now()
                        .minusDays(365)
                        .isAfter(customer.getLoginAt());

                if (isDormantTarget) {
                    customer.setStatus(Customer.Status.DORMANT);
                } else {
                    continue;
                }

                //3 휴면계정 상태 변경

                customerRepository.save(customer);

                //4 메일을 보낸다
                emailProvider.send(customer.getEmail(), "휴면 전환 메일입니다.", "내용");

            }
            jobExecution.setStatus(BatchStatus.COMPLETED);
        } catch (Exception e) {
            jobExecution.setStatus(BatchStatus.FAILED);
        }

        jobExecution.setEndTime(LocalDateTime.now());


        emailProvider.send(
                "admin@fast.com",
                "배치 완료 알림",
                "BatchDormantJob이 수행되었습니다. Status : " + jobExecution.getStatus()
        );

        return jobExecution;
    }
}
