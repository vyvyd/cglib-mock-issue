package com.demo;

import java.math.BigDecimal;

public class Resource {

    private Service service;

    public Resource(
            Service service
    ) {
        this.service = service;
    }

    public void execute() {
        this.service.execute(BigDecimal.ZERO);
    }
}
