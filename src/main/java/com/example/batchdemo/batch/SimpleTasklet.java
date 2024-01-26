package com.example.batchdemo.batch;

import com.example.batchdemo.batch.ItemProcessor;
import com.example.batchdemo.batch.ItemReader;
import com.example.batchdemo.batch.ItemWriter;
import com.example.batchdemo.batch.Tasklet;
import org.springframework.stereotype.Component;

@Component
public class SimpleTasklet<I, O> implements Tasklet {

    private final ItemReader<I> iItemReader;
    private final ItemProcessor<I, O> ioItemProcessor;
    private final ItemWriter<O> oItemWriter;

    public SimpleTasklet(ItemReader<I> iItemReader, ItemProcessor<I, O> ioItemProcessor, ItemWriter<O> oItemWriter) {
        this.iItemReader = iItemReader;
        this.ioItemProcessor = ioItemProcessor;
        this.oItemWriter = oItemWriter;
    }


    @Override
    public void execute() {

        int pageNo = 0;
        //비즈니스 로직
        while (true) {

            //read
            final I read = iItemReader.read();
            if (read == null) break;

            //process
            final O process = ioItemProcessor.process(read);
            if (process == null) continue;

            //write
            oItemWriter.write(process);
        }
    }
}
