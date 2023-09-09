package org.example.RPC.client;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.example.RPC.API.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcClientProxy {
    private static final String ZK_REGISTRY_PATH = "/rpc_registry";
    ZooKeeper zk;

    public RpcClientProxy(String address) {
        try {
            zk = new ZooKeeper(address, 100000, null);
            System.out.println(zk.getState());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Object getProxy(Class<?> o) {
        String service = ZK_REGISTRY_PATH + "/" + o.getName();
        String serviceAddress;
        try {
            if (zk.exists(service, null) == null) return null;
            serviceAddress = new String(zk.getData(service, false, null));
            System.out.println("Found Service in: " + serviceAddress);
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        try (Socket socket = new Socket(serviceAddress.split(":")[0], Integer.parseInt(serviceAddress.split(":")[1]));) {
            synchronized (this) {
                this.wait(100);
            }

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            Request request = Request.builder().service(o.getName()).build();
            outputStream.writeObject(request);
            outputStream.flush();

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object object = inputStream.readObject();

            System.out.println(object);
            return object;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
