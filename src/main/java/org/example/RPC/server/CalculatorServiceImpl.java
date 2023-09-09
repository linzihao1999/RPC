package org.example.RPC.server;

import org.example.RPC.API.CalculatorService;

import java.io.Serializable;

public class CalculatorServiceImpl implements CalculatorService, Serializable {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
