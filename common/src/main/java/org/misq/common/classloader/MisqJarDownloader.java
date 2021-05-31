package org.misq.common.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static java.lang.String.format;

public final class MisqJarDownloader {
    private static final Logger log = LoggerFactory.getLogger(MisqJarDownloader.class);

    // TODO refactor (de-dup) into a config module.
    private final Path defaultLocalJarPath = Paths.get("ext");

    private final MisqClassLoader classLoader;
    private final URL jarDownloadUrl;
    private final File jarFile;

    public MisqJarDownloader(MisqClassLoader classLoader, String jarDownloadUrlSpec) throws MalformedURLException {
        this.classLoader = classLoader;
        this.jarDownloadUrl = specToUrl.apply(jarDownloadUrlSpec);
        this.jarFile = specToFile.apply(jarDownloadUrlSpec);
    }

    public Path getDefaultLocalJarPath() {
        return defaultLocalJarPath;
    }

    public void downloadJar() {
        try {
            log.info("Downloading {}", jarDownloadUrl.toString());
            ReadableByteChannel readableByteChannel = Channels.newChannel(jarDownloadUrl.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(jarFile);
            FileChannel fileChannel = fileOutputStream.getChannel();
            // TODO find source file size, track progress
            long bytesDownloaded = fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileChannel.close();
            fileOutputStream.close();
            readableByteChannel.close();
            log.info("Downloaded {} bytes to {}/{}.",
                    bytesDownloaded,
                    defaultLocalJarPath,
                    jarFile.getName());
        } catch (IOException ex) {
            throw new IllegalStateException(format("Could not download %s.", jarDownloadUrl), ex);
        }
    }

    public void loadClassesFromJar() {
        MisqJarClassLoader misqJarClassloader = new MisqJarClassLoader(classLoader, jarFile);
        misqJarClassloader.loadClasses();
    }

    private final Function<String, URL> specToUrl = (s) -> {
        try {
            URL url = new URL(s);
            validateJarUrl(url);
            return url;
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(format("%s is not a valid URL.", s), ex);
        }
    };

    private final Function<String, String> specToFilename = (s) -> s.substring(s.lastIndexOf('/') + 1);
    private final Function<String, File> specToFile = (s) ->
            Paths.get(defaultLocalJarPath.toAbsolutePath().toString(), specToFilename.apply(s)).toFile();

    private void validateJarUrl(URL url) {
        if (!url.toString().endsWith(".jar")) {
            throw new IllegalStateException(format("s is not a valid url to a jar file.", url));
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            new URI(url.toString()).parseServerAuthority();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(
                    format("%s could not be parsed into user-information, host, and port components.", url), ex);
        }
        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(2000);
            conn.connect();
        } catch (IOException ex) {
            throw new IllegalStateException(format("Could not open url connection to %s.", url), ex);
        }
    }

    /////////////////////////////////////////////////////////////
    // TODO Move these two methods to a bitcoinj module (project).
    /////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        // Test bitcoinj download.
        try {
            MisqClassLoader classLoader = new MisqClassLoader();
            MisqJarDownloader jarLoader = new MisqJarDownloader(classLoader,
                    "https://jitpack.io/com/github/bisq-network/bitcoinj/v0.15.8/bitcoinj-v0.15.8.jar");
            jarLoader.downloadJar();
            jarLoader.loadClassesFromJar();

            printNetworkParameters(classLoader);

            log.info("done");
        } catch (MalformedURLException ex) {
            log.error("", ex);
        }
    }

    @Deprecated
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
