package org.example.RPC.server;

import org.example.RPC.API.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServerHandler {
    private final int port;

    private final ConcurrentHashMap<String, Object> serviceMap = new ConcurrentHashMap<>();

    public RpcServerHandler(int port) {
        this.port = port;
    }

    public void addService(String serviceName, Object serviceImpl) {
        serviceMap.put(serviceName, serviceImpl);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> handleRequest(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleRequest(Socket socket) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Request request = (Request) inputStream.readObject();

            Object service = serviceMap.get(request.getService());

            Class<?> clazz = Class.forName(request.getService());
            String[] paramsType = request.getType();

            Method[] methods = clazz.getMethods();
            Method realMethod = null;
            for (var method : methods) {
                if (method.getParameterCount() != paramsType.length) continue;
                boolean foundMethod = true;
                for (int i = 0; i < method.getParameterCount(); i++) {
                    if (!method.getParameterTypes()[i].getName().equals(paramsType[i])) {
                        foundMethod = false;
                        break;
                    }
                }
                if (foundMethod) {
                    realMethod = method;
                    break;
                }
            }
            assert realMethod != null;
            Object result = realMethod.invoke(service, request.getParams());

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
