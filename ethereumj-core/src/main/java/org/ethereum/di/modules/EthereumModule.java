package org.ethereum.di.modules;

import android.content.Context;

import org.ethereum.core.BlockchainImpl;
import org.ethereum.core.Wallet;
import org.ethereum.datasource.KeyValueDataSource;
import org.ethereum.datasource.LevelDbDataSource;
import org.ethereum.db.BlockStore;
import org.ethereum.db.InMemoryBlockStore;
import org.ethereum.db.RepositoryImpl;
import org.ethereum.facade.Blockchain;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumImpl;
import org.ethereum.facade.Repository;
import org.ethereum.listener.CompositeEthereumListener;
import org.ethereum.listener.EthereumListener;
import org.ethereum.manager.AdminInfo;
import org.ethereum.manager.BlockLoader;
import org.ethereum.manager.WorldManager;
import org.ethereum.net.BlockQueue;
import org.ethereum.net.MessageQueue;
import org.ethereum.net.client.PeerClient;
import org.ethereum.net.eth.EthHandler;
import org.ethereum.net.p2p.P2pHandler;
import org.ethereum.net.peerdiscovery.PeerDiscovery;
import org.ethereum.net.server.ChannelManager;
import org.ethereum.net.server.EthereumChannelInitializer;
import org.ethereum.net.shh.ShhHandler;
import org.ethereum.net.wire.MessageCodec;
import org.ethereum.vm.ProgramInvokeFactory;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EthereumModule {

    private Context context;

    public EthereumModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Ethereum provideEthereum(WorldManager worldManager, AdminInfo adminInfo, ChannelManager channelManager,
                             BlockLoader blockLoader, Provider<PeerClient> peerClientProvider) {
        return new EthereumImpl(worldManager, adminInfo, channelManager, blockLoader, peerClientProvider);
    }

    @Provides
    @Singleton
    WorldManager provideWorldManager(Blockchain blockchain, Repository repository, Wallet wallet, PeerDiscovery peerDiscovery
            ,BlockStore blockStore, ChannelManager channelManager, EthereumListener listener) {
        return new WorldManager(blockchain, repository, wallet, peerDiscovery, blockStore, channelManager, listener);
    }

    @Provides
    @Singleton
    Blockchain provideBlockchain(BlockStore blockStore, Repository repository,
                                 Wallet wallet, AdminInfo adminInfo,
                                 EthereumListener listener, ChannelManager channelManager) {
        return new BlockchainImpl(blockStore, repository, wallet, adminInfo, listener, channelManager);
    }

    @Provides
    @Singleton
    BlockStore provideBlockStore() {
        return new InMemoryBlockStore();
    }

    @Provides
    @Singleton
    Repository provideRepository() {
        LevelDbDataSource detailsDS = new LevelDbDataSource();
        detailsDS.setContext(context);
        LevelDbDataSource stateDS = new LevelDbDataSource();
        stateDS.setContext(context);
        return new RepositoryImpl(detailsDS, stateDS);
    }

    @Provides
    @Singleton
    AdminInfo provideAdminInfo() {
        return new AdminInfo();
    }

    @Provides
    @Singleton
    EthereumListener provideEthereumListener() {
        return new CompositeEthereumListener();
    }

    @Provides
    @Singleton
    PeerDiscovery providePeerDiscovery() {
        return new PeerDiscovery();
    }

    @Provides
    @Singleton
    ChannelManager provideChannelManager() {
        return new ChannelManager();
    }

    @Provides
    @Singleton
    BlockLoader provideBlockLoader() {
        return new BlockLoader();
    }

    @Provides
    EthHandler provideEthHandler() {
        return new EthHandler();
    }

    @Provides
    ShhHandler provideShhHandler() {
        return new ShhHandler();
    }

    @Provides
    P2pHandler provideP2pHandler() {
        return new P2pHandler();
    }

    @Provides
    MessageCodec provideMessageCodec() {
        return new MessageCodec();
    }

    @Provides
    PeerClient providePeerClient(EthereumListener listener, ChannelManager channelManager,
                                 Provider<EthereumChannelInitializer> ethereumChannelInitializerProvider) {
        return new PeerClient(listener, channelManager, ethereumChannelInitializerProvider);
    }

    @Provides
    MessageQueue provideMessageQueue() {
        return new MessageQueue();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }



    @Provides
    String provideRemoteId() {
        return "bf01b54b6bc7faa203286dfb8359ce11d7b1fe822968fb4991f508d6f5a36ab7d9ae8af9b0d61c0467fb08567e0fb71cfb9925a370b69f9ede97927db473d1f5";
    }


}