package org.example.RPC.server;

import org.example.RPC.API.CalculatorService;

import java.io.Serializable;

public class CalculatorServiceImpl implements CalculatorService, Serializable {
    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }
}
