package com.example.batchdemo.batch;

import com.example.batchdemo.model.Customer;
import com.example.batchdemo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class DormantBatchJobTest {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Job dormantBatchJob;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
    }


    @Test
    @DisplayName("로그인 시간이 일년을 경과한 고객이 3명이고, 일년 이내에 로그인한 곡객이 다섯명이면 3명의 고객이 휴면 전환 대상이다")
    void test1() {

        //given
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);

        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);
        saveCustomer(364);

        //wehn
        JobExecution result = dormantBatchJob.execute();

        //then
        final long dormantCount = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == Customer.Status.DORMANT)
                .count();

        assertThat(dormantCount).isEqualTo(3);
        assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }



    @Test
    @DisplayName("고객이 열명이 모두 휴면 계정 대상이면  휴면 전환 대상은 10명")
    void test2() {

        //given
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);
        saveCustomer(366);

        //when
        JobExecution result = dormantBatchJob.execute();

        //then
        final long dormantCount = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == Customer.Status.DORMANT)
                .count();

        assertThat(dormantCount).isEqualTo(10);
        assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("고객이 없는 경우에도 배치는 정상 작동")
    void test3() {
        //wehn
        JobExecution result = dormantBatchJob.execute();

        //then
        final long dormantCount = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == Customer.Status.DORMANT)
                .count();

        assertThat(dormantCount).isEqualTo(0);
        assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("Batch가 실패하면 BatchSatus는 FAILED")
    void test4() {

        //given
        final Job dormantBatchJob = new TaskletJob(null);

        //when
        final JobExecution result = dormantBatchJob.execute();

        //then
        assertThat(result.getStatus()).isEqualTo(BatchStatus.FAILED);
    }

    public void saveCustomer(long loginMinusDays) {
        final String uuid = UUID.randomUUID().toString();
        final Customer test = new Customer(uuid, uuid + "@fast.com");
        test.setLoginAt(LocalDateTime.now().minusDays(loginMinusDays));
        customerRepository.save(test);
    }


}