package org.misq.wallet.installer;

import org.misq.common.classloader.MisqClassLoader;
import org.misq.common.classloader.MisqJarClassLoader;
import org.misq.common.classloader.MisqJarDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;

public class JarInstaller {
    private static final Logger log = LoggerFactory.getLogger(JarInstaller.class);

    protected final MisqClassLoader misqClassLoader;

    public JarInstaller(MisqClassLoader misqClassLoader) {
        this.misqClassLoader = misqClassLoader;
    }

    /**
     * Downloads jar and loads its classes into the MisqClassLoader.
     *
     * @param jarUrlSpec usually a jitpack url, e.g.,
     *                   https://jitpack.io/com/github/bisq-network/bitcoinj/v0.15.8/bitcoinj-v0.15.8.jar
     */
    public void loadRemoteJarClasses(String jarUrlSpec) {
        try {
            MisqJarDownloader misqJarDownloader = new MisqJarDownloader(misqClassLoader, jarUrlSpec);
            misqJarDownloader.downloadJar();
            misqJarDownloader.loadClassesFromJar();
        } catch (MalformedURLException ex) {
            throw new IllegalStateException("", ex);
        }
    }

    /**
     * Loads a local jar's classes into the MisqClassLoader.
     *
     * @param extensionJarUrlSpec a local jar url spec, e.g,
     *                            ext/bitcoinj-impl-0.0.1-SNAPSHOT.jar
     */
    public void loadLocalJarClasses(String extensionJarUrlSpec) {
        File localJar = new File(extensionJarUrlSpec);
        MisqJarClassLoader misqJarClassloader = new MisqJarClassLoader(misqClassLoader, localJar);
        misqJarClassloader.loadClasses();
    }

    // Note: downloaded SPI providers are loaded by the MisqClassLoader, not the AppClassLoader.
    // You must use this MisqClassLoader to lookup and instantiate any dynamically class-loaded spi provider.
    public MisqClassLoader getClassLoader() {
        return misqClassLoader;
    }
}
