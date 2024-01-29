package com.example.batchdemo.application.dormant;

import com.example.batchdemo.EmailProvider;
import com.example.batchdemo.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreDormantBatchItemWriterTest {

    private PreDormantBatchItemWriter preDormantBatchItemWriter;

    @Test
    @DisplayName("1주일 뒤에 휴면 계정 전환 예정자라고 이메일을 전송해야함")
    void test1() {

        //given
        final EmailProvider mockEmailProvider = mock(EmailProvider.class);
        this.preDormantBatchItemWriter = new PreDormantBatchItemWriter(mockEmailProvider);

        final Customer customer = new Customer("karina", "karina@mail.com");

        //when
        preDormantBatchItemWriter.write(customer);

        //then
        verify(mockEmailProvider, atLeastOnce()).send(any(), any(), any());
    }
}