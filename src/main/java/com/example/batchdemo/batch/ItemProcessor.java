package com.example.batchdemo.batch;

public interface ItemProcessor<I, O> {
    O process(I item);
}
