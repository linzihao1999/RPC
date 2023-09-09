package org.example.RPC.server;

import org.example.RPC.API.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            String serviceName = request.getService();
            System.out.println(serviceName);

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(serviceMap.get(serviceName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
