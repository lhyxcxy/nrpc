package lhy.nrpc.test.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lhy.nrpc.core.annotation.EnableNRpc;
@EnableNRpc
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}

