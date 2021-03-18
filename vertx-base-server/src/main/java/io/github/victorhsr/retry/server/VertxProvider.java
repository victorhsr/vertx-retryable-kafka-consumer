package io.github.victorhsr.retry.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.util.List;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public class VertxProvider {

    /**
     * prove uma instancia {@link Vertx}
     */
    public static Vertx getVertxInstance() {
        final Vertx vertx = Vertx.vertx();
        return vertx;
    }

    /**
     * Prove uma instancia clusterizada do {@link Vertx}.
     * O cluster eh gerenciado pelo {@link HazelcastClusterManager}
     * na porta 57701
     *
     * @param networkMembers membros que farao parte do scan de nos do cluster
     */
    public static Vertx getClusteredVertxInstance(final List<String> networkMembers) {

        final Config config = new Config();
        final NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701).setPortCount(5);
        network.setPortAutoIncrement(true);

        final JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        final TcpIpConfig tcpIpConfig = join.getTcpIpConfig();

        networkMembers.forEach(tcpIpConfig::addMember);
        tcpIpConfig.setEnabled(true);

        final ClusterManager clusterManager = new HazelcastClusterManager(config);
        final VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

        final Single<Vertx> vertxSingle = Single.create(emitter -> {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    emitter.onSuccess(res.result());
                } else {
                    emitter.onError(res.cause());
                }
            });
        });

        final Vertx clusteredVertx = vertxSingle.blockingGet();
        return clusteredVertx;
    }

}
