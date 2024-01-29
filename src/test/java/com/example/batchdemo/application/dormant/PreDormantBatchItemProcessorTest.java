package com.example.batchdemo.application.dormant;

import com.example.batchdemo.customer.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PreDormantBatchItemProcessorTest {

    private PreDormantBatchItemProcessor preDormantBatchItemProcessor;

    @BeforeEach
    void setup() {
        preDormantBatchItemProcessor = new PreDormantBatchItemProcessor();
    }

    @Test
    @DisplayName("로그인 날짜가 오늘부터 358일전이면 customer 를 반환해야 함")
    void test1() {
        //given
        final Customer customer = new Customer("karina", "karina@mail.com");

        customer.setLoginAt(LocalDateTime.now().minusDays(365).plusDays(7));

        //when
        final Customer result = preDormantBatchItemProcessor.process(customer);

        //then
        Assertions.assertThat(result).isEqualTo(customer);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("로그인_날짜가_오늘로부터_358일전이_아니면_null을_반환해야한다.")
    void test2() {

        // given
        final Customer customer = new Customer("minsoo", "minsoo@fastcampus.com");

        // when
        final Customer result = preDormantBatchItemProcessor.process(customer);

        // then
        Assertions.assertThat(result).isNull();

    }
}