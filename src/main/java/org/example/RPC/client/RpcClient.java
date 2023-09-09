package org.example.RPC.client;

import org.example.RPC.API.CalculatorService;

public class RpcClient {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy("localhost:2181");
        CalculatorService calculatorService = (CalculatorService) rpcClientProxy.getProxy(CalculatorService.class);
        System.out.println("RPC result: " + calculatorService.add(1, 2));
    }
}
