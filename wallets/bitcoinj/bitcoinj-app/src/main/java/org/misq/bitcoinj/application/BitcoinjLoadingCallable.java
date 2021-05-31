package org.misq.bitcoinj.application;

import io.reactivex.Observable;
import org.misq.common.classloader.MisqClassLoader;
import org.misq.wallet.installer.BitcoinjJarInstaller;

import java.util.concurrent.Callable;

public class BitcoinjLoadingCallable {

    private final MisqClassLoader misqClassLoader;

    public BitcoinjLoadingCallable(MisqClassLoader misqClassLoader) {
        this.misqClassLoader = misqClassLoader;
    }

    public Observable<String> getBitcoinjLoaderObservableCallable() {
        return Observable.fromCallable(() -> {
            load();
            return "done (rx-java)";
        });
    }

    public Callable<String> getBitcoinjLoaderCallable() {
        return () -> {
            load();
            return "done (completable future)";
        };
    }

    private void load() {
        // Download the bitcoinj jar from jitpack and load its classes.
        BitcoinjJarInstaller bitcoinjJarInstaller = new BitcoinjJarInstaller(misqClassLoader);
        bitcoinjJarInstaller.loadRemoteJarClasses();
        // Load the Misq spi implementations classes (3 classes + META-INF/services).
        bitcoinjJarInstaller.loadBitcoinjServiceProviderClasses();
        // Now we can load the misq bitcoinj service provider.
    }
}
