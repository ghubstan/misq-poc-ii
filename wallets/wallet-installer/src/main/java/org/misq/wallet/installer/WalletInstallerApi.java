package org.misq.wallet.installer;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.misq.bitcoinj.api.BitcoinjManager;
import org.misq.bitcoinj.exception.ProviderNotFoundException;
import org.misq.bitcoinj.service.BitcoinjServiceLoader;
import org.misq.bitcoinj.spi.BitcoinjServiceProvider;
import org.misq.common.classloader.MisqClassLoader;
import org.misq.common.di.annotation.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static org.misq.bitcoinj.service.BitcoinjServiceLoader.DYNAMIC_BITCOINJ_SERVICE_PROVIDER;
import static org.misq.wallet.installer.WalletInstallerApi.CONCURRENCY_FRWK.COMPLETABLE_FUTURE;

@Singleton
public class WalletInstallerApi {
    private static final Logger log = LoggerFactory.getLogger(WalletInstallerApi.class);

    public enum CONCURRENCY_FRWK {
        COMPLETABLE_FUTURE,
        RX_OBSERVABLE;
    }

    @Nullable
    private BitcoinjManager bitcoinjManager;

    private final MisqClassLoader misqClassLoader = new MisqClassLoader();

    public WalletInstallerApi() {
    }

    public void installBitcoinj(CONCURRENCY_FRWK concurrencyFramework) {
        if (concurrencyFramework.equals(COMPLETABLE_FUTURE))
            initBitcoinjInCompletableFuture();
        else
            initBitcoinjInObservableFromCallable();
    }

    private void initBitcoinjInCompletableFuture() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return getBitcoinjLoaderCallable().call();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        });
        completableFuture.thenAccept(log::info);
    }

    private void initBitcoinjInObservableFromCallable() {
        Observable<String> observable = getBitcoinjLoaderObservableCallable();
        observable.subscribeOn(Schedulers.io()).subscribe(log::info);
    }

    public Observable<String> getBitcoinjLoaderObservableCallable() {
        return Observable.fromCallable(() -> {
            initBitcoinjAndSpiImplClasses();
            return "Bitcoinj and SPI impl classes were loaded in thread " + currentThread().getName();
        });
    }

    public Callable<String> getBitcoinjLoaderCallable() {
        return () -> {
            initBitcoinjAndSpiImplClasses();
            return "Bitcoinj and SPI impl classes were loaded in thread " + currentThread().getName();
        };
    }

    private void initBitcoinjAndSpiImplClasses() {
        try {
            // Download the bitcoinj jar from jitpack and load its classes.
            BitcoinjJarInstaller bitcoinjJarInstaller = new BitcoinjJarInstaller(misqClassLoader);
            bitcoinjJarInstaller.loadRemoteJarClasses();
            // Load the Misq spi implementations classes (3 classes + META-INF/services).
            bitcoinjJarInstaller.loadBitcoinjServiceProviderClasses();
            // Now we can load the misq bitcoinj service provider.
            initDownloadedBitcoinjServiceProviderImpl();
            initWallet();
            Thread.sleep(10000);
            shutdownWallet();
        } catch (InterruptedException ignored) {
            // empty
        }
    }


    public void initWallet() {
        bitcoinjManager.printNetworkParameters();
        bitcoinjManager.syncRegtestChain();
        log.info("Wallet initialized in " + currentThread().getName());
    }

    public void shutdownWallet() {
        log.info("Shutting down wallet in " + currentThread().getName());
        bitcoinjManager.shutdownBitcoinj();
    }

    private void initDownloadedBitcoinjServiceProviderImpl() {
        String providerName = DYNAMIC_BITCOINJ_SERVICE_PROVIDER;
        log.info("Loading SPI implementation " + providerName);
        try {
            BitcoinjServiceProvider bitcoinjServiceProvider = getBitcoinjServiceProvider(providerName, misqClassLoader)
                    .orElseThrow(() -> new ProviderNotFoundException(providerName + " not found"));
            this.bitcoinjManager = bitcoinjServiceProvider.create();
        } catch (ProviderNotFoundException e) {
            throw new IllegalStateException(
                    format("Error creating dynamically loaded service provider %s.", providerName));
        }
    }

    private Optional<BitcoinjServiceProvider> getBitcoinjServiceProvider(String providerName,
                                                                         ClassLoader classLoader) {
        try {
            return Optional.of(BitcoinjServiceLoader.provider(providerName, classLoader));
        } catch (ProviderNotFoundException ex) {
            log.warn(ex.toString());
        }
        return Optional.empty();
    }
}
