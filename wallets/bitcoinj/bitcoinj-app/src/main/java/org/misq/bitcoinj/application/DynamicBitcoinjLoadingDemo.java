package org.misq.bitcoinj.application;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.misq.bitcoinj.api.BitcoinjManager;
import org.misq.bitcoinj.exception.ProviderNotFoundException;
import org.misq.bitcoinj.service.BitcoinjServiceLoader;
import org.misq.bitcoinj.spi.BitcoinjServiceProvider;
import org.misq.common.classloader.MisqClassLoader;
import org.misq.wallet.installer.BitcoinjJarInstaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.misq.bitcoinj.service.BitcoinjServiceLoader.DUMMY_BITCOINJ_SERVICE_PROVIDER;
import static org.misq.bitcoinj.service.BitcoinjServiceLoader.DYNAMIC_BITCOINJ_SERVICE_PROVIDER;

// TODO STUDY
// https://www.fatalerrors.org/index.php/a/analysis-of-java-spi-mechanism.html

public class DynamicBitcoinjLoadingDemo {
    private static final Logger log = LoggerFactory.getLogger(DynamicBitcoinjLoadingDemo.class);

    private final Predicate<String> shouldDownloadServiceProvider = (providerName) ->
            providerName.equals(DYNAMIC_BITCOINJ_SERVICE_PROVIDER);

    private BitcoinjServiceProvider bitcoinjServiceProvider;
    private BitcoinjManager bitcoinjManager;

    public DynamicBitcoinjLoadingDemo() {
    }

    public void init(String providerName) {
        checkWhichProvidersAreAvailable();

        if (shouldDownloadServiceProvider.test(providerName))
            downloadAndInitRemoteServiceProvider(providerName);
        else
            initDummyServiceProvider(providerName);

        bitcoinjManager = createBitcoinjManager(bitcoinjServiceProvider);
    }

    private void initDummyServiceProvider(String providerName) {
        try {
            this.bitcoinjServiceProvider =
                    getBitcoinjServiceProvider(providerName).orElseThrow(() ->
                            new ProviderNotFoundException(providerName + " not found"));
        } catch (ProviderNotFoundException e) {
            throw new IllegalStateException(
                    format("Error creating dynamically loaded service provider %s.", providerName));
        }
    }

    private void downloadAndInitRemoteServiceProvider(String providerName) {
        try {
            // Download the bitcoinj jar from jitpack and load its classes.
            BitcoinjJarInstaller bitcoinjJarInstaller = new BitcoinjJarInstaller(new MisqClassLoader());
            bitcoinjJarInstaller.loadRemoteJarClasses();
            // Load the Misq spi implementations classes (3 classes + META-INF/services).
            bitcoinjJarInstaller.loadBitcoinjServiceProviderClasses();
            // Now we can load the misq bitcoinj service provider.
            this.bitcoinjServiceProvider =
                    getBitcoinjServiceProvider(providerName, bitcoinjJarInstaller.getClassLoader())
                            .orElseThrow(() -> new ProviderNotFoundException(providerName + " not found"));
        } catch (ProviderNotFoundException e) {
            throw new IllegalStateException(
                    format("Error creating dynamically loaded service provider %s.", providerName));
        }
    }

    public void start() {
        bitcoinjManager.printNetworkParameters();
        bitcoinjManager.syncRegtestChain();
    }

    public void stop() {
        bitcoinjManager.shutdownBitcoinj();
    }

    private BitcoinjManager createBitcoinjManager(BitcoinjServiceProvider provider) {
        return provider.create();
    }

    private Optional<BitcoinjServiceProvider> getBitcoinjServiceProvider(String providerName, ClassLoader classLoader) {
        try {
            return Optional.of(BitcoinjServiceLoader.provider(providerName, classLoader));
        } catch (ProviderNotFoundException ex) {
            log.warn(ex.toString());
        }
        return Optional.empty();
    }

    private Optional<BitcoinjServiceProvider> getBitcoinjServiceProvider(String providerName) {
        try {
            return Optional.of(BitcoinjServiceLoader.provider(providerName));
        } catch (ProviderNotFoundException ex) {
            log.warn(ex.toString());
        }
        return Optional.empty();
    }

    private void checkWhichProvidersAreAvailable() {
        Optional<BitcoinjServiceProvider> dummyProvider = getBitcoinjServiceProvider(DUMMY_BITCOINJ_SERVICE_PROVIDER);
        if (dummyProvider.isEmpty())
            log.warn("Service provider {} is not available.", DUMMY_BITCOINJ_SERVICE_PROVIDER);
        else
            log.info("Service provider {} is available.", dummyProvider.get());

        Optional<BitcoinjServiceProvider> dynamicProvider = getBitcoinjServiceProvider(DYNAMIC_BITCOINJ_SERVICE_PROVIDER);
        if (dynamicProvider.isEmpty())
            log.warn("Service provider {} is not available.", DYNAMIC_BITCOINJ_SERVICE_PROVIDER);
        else
            log.info("Service provider {} is available.", dynamicProvider.get());
    }

    public static void main(String[] args) {
        useCurrentThread();
        // useCompletableFuture();
        // useObservableFromCallable();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ignored) {
            // empty
        }
        System.exit(0);
    }

    // Just downloads jar, does not load spi.
    private static void useCompletableFuture() {
        BitcoinjLoadingCallable bitcoinjLoadingCallable = new BitcoinjLoadingCallable(new MisqClassLoader());
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return bitcoinjLoadingCallable.getBitcoinjLoaderCallable().call();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        });
        completableFuture.thenAccept((status) -> log.info(status));
    }

    // Just downloads jar, does not load spi.
    private static void useObservableFromCallable() {
        BitcoinjLoadingCallable bitcoinjLoadingCallable = new BitcoinjLoadingCallable(new MisqClassLoader());
        Observable<String> observable = bitcoinjLoadingCallable.getBitcoinjLoaderObservableCallable();
        observable.subscribeOn(Schedulers.io()).subscribe(s -> log.info(s));
    }

    private static void useCurrentThread() {
        try {
            DynamicBitcoinjLoadingDemo main = new DynamicBitcoinjLoadingDemo();
            // main.init(DUMMY_BITCOINJ_SERVICE_PROVIDER);
            main.init(DYNAMIC_BITCOINJ_SERVICE_PROVIDER);
            main.start();
            SECONDS.sleep(15);
            main.stop();
        } catch (InterruptedException ignored) {
            // empty
        }
    }
}
