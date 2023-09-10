package org.example.RPC.client;

import org.example.RPC.API.CalculatorService;

import java.lang.reflect.Method;

public class RpcClient {
    public static void main(String[] args) throws NoSuchMethodException {
        RpcClientProxy rpcClientProxy = new RpcClientProxy("localhost:2181");
        Class<?> service = CalculatorService.class;
        Method method = CalculatorService.class.getMethod("add", Integer.class, Integer.class);
        Object[] params = new Object[]{1, 2};
        Object result = (Object) rpcClientProxy.getProxy(service, method, params);

        System.out.println("RPC result: " + result.toString());
    }
}
