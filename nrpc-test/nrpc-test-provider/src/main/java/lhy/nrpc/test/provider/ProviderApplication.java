package lhy.nrpc.test.provider;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lhy.nrpc.core.annotation.EnableNRpc;
@EnableNRpc(scanBasePackages={"lhy.nrpc"})
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProviderApplication.class, args);
    }
    
}

