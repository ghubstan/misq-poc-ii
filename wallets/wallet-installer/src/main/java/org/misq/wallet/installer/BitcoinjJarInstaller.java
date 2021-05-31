package org.misq.wallet.installer;

import org.misq.common.classloader.MisqClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BitcoinjJarInstaller extends JarInstaller {
    private static final Logger log = LoggerFactory.getLogger(BitcoinjJarInstaller.class);

    private static final String BITCOINJ_JITPACK_URL_SPEC = "https://jitpack.io/com/github/bisq-network/bitcoinj/v0.15.8/bitcoinj-v0.15.8.jar";

    // TODO refactor (de-dup) into a config module
    private final Path defaultLocalJarPath = Paths.get("ext");

    // The bitcoinj-impl-0.0.1-SNAPSHOT.jar has to be manually extracted from the project's dist.zip to 'ext'.
    private final Path bitcoinjSpiImplJarPath;

    public BitcoinjJarInstaller(MisqClassLoader misqClassLoader) {
        super(misqClassLoader);
        this.bitcoinjSpiImplJarPath = Paths.get(defaultLocalJarPath.toString(),
                "bitcoinj-impl-0.0.1-SNAPSHOT.jar");
    }

    public void loadRemoteJarClasses() {
        super.loadRemoteJarClasses(BITCOINJ_JITPACK_URL_SPEC);
    }

    public void loadBitcoinjServiceProviderClasses() {
        super.loadLocalJarClasses(bitcoinjSpiImplJarPath.toString());
    }

    private static void printNetworkParameters(MisqClassLoader classLoader) {
        try {
            Class<?> networkParametersClass = Class.forName("org.bitcoinj.core.NetworkParameters",
                    true,
                    classLoader);
            // Get static method, invoke on null object.
            Method fromIdMethod = networkParametersClass.getDeclaredMethod("fromID", String.class);
            Object instance = fromIdMethod.invoke(null, "org.bitcoin.test");
            log.info(instance.toString());
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
