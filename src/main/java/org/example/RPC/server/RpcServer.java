package org.example.RPC.server;

import org.example.RPC.API.CalculatorService;

public class RpcServer {
    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new ServiceRegistry("localhost:2181");
        serviceRegistry.registerService(CalculatorService.class.getName(), "localhost:1234");

        CalculatorService calculatorService = new CalculatorServiceImpl();

        RpcServerHandler serverHandler = new RpcServerHandler(1234);
        serverHandler.addService(CalculatorService.class.getName(), calculatorService);
        serverHandler.start();
    }
}
