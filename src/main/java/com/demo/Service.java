package com.demo;

import java.math.BigDecimal;

public class Service {

    private final String apiToken;

    public Service(
            String apiToken
    ) {
        this.apiToken = apiToken;
    }

    public void execute(BigDecimal number) {
        System.out.println("Service got " + number.toString() + "units");
    }
}
