package org.example.RPC.server;

import lombok.Data;
import org.apache.zookeeper.*;

import java.io.IOException;

@Data
public class ServiceRegistry {
    private static final String ZK_REGISTRY_PATH = "/rpc_registry";

    private final ZooKeeper zk;

    public ServiceRegistry(String zkAddress) {
        try {
            zk = new ZooKeeper(zkAddress, 100000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        createRegistryNode();
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createRegistryNode() {
        try {
            if (zk.exists(ZK_REGISTRY_PATH, false) == null) {
                zk.create(ZK_REGISTRY_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerService(String serviceName, String serviceAddress) {
        String servicePath = ZK_REGISTRY_PATH + "/" + serviceName;
        try {
            if (zk.exists(servicePath, false) == null) {
                zk.create(servicePath, serviceAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
