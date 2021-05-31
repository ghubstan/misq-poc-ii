# Misq POC II - Proposed Misq project structure

## Module Organization

### Gradle `buildSrc`

The buildSrc directory is treated as an included build. Gradle automatically compiles buildSrc/build.gradle and puts it
in the classpath of Misq project build scripts. There can be only one buildSrc directory, located in the root project
directory.

This buildSrc/build.gradle file is used to tailor gradle tasks run by other Misq build scripts in one place; they can be
overridden.

Common but not ubiquitous tasks can be defined in buildSrc as well. For example, many but not all modules will generate
protobuf sources. Module build files can share the `protobuf` task as needed by including the line

```asciidoc
apply from: '../buildSrc/gen-protos.gradle'
```

### Gradle `Platforms`

- Define recommended dependency versions.  In this POC, single, specific version are defined in the platforms,
  but platforms can specify dependency ranges to.
  
- Describe modules which are published together (and for example, share the same version).

- Define and share a set of dependency versions between projects.

When a gradle project (module) includes a platform, none of the platform's dependencies are included in the
module until the module explicitly declares a dependency described in the platform.  
P2P module build file example:
```asciidoc
// Get recommended versions from the common-platform project.
api platform(project(':platforms:common-platform'))

// Declare dependencies with versions recommended by platform.
implementation 'org.slf4j:slf4j-api'
implementation 'org.slf4j:slf4j-api'
implementation 'ch.qos.logback:logback-core'
implementation 'ch.qos.logback:logback-classic'
```

See https://docs.gradle.org/current/userguide/java_platform_plugin.html

### Gradle Projects (a.k.a Modules)

## Remote Module Download / Dynamic Class Loading

An example of the Java SPI api is shown in the `org.misq.common.classloader` package.

Prerequisite:  copy `bitcoinj-impl-0.0.1-SNAPSHOT.jar` to extension dir `ext`:
  `$ ./gradlew installBitcoinjImplJar`

The bitcoinj spi poc:
- Downloads `https://jitpack.io/com/github/bisq-network/bitcoinj/v0.15.8/bitcoinj-v0.15.8.jar` to the Misq extensions 
  directory `ext`
- Dynamically loads the `bitcoinj-v0.15.8.jar`'s classes
- Dynamically loads a Misq spi implementation (the interface to bitcoinj classes)
- Initializes a regtest wallet
- Shuts down wallet.

## Application

`MisqAppMain` wires up some simple services in a `CoreApi`, a `WalletInstallerApi`, and passes them
to both a webapp and grpc server.

The main application's webapp and grpc service endpoints can be called as described below.  

### Web App

#### Web URLS

- http://localhost:5050
- http://localhost:5050/balance
- http://localhost:5050/version
- http://localhost:5050/peers


#### Dynamically Load Bitcoinj

- Create bitcoin-core regtest data dir: 
`$ mkdir /tmp/regtest`


- Start bitcoind in regest mode:
`bitcoind -datadir=/tmp/regtest -regtest=1 -server=1 -txindex=1 -peerbloomfilters=1 -debug=net`


- Dynamically bootstrap Bitcoinj:
`http://localhost:5050/install/wallet`

_Note: The ratpack `InstallWalletHandler` uses the JavaRX `Observable` api to load bitcoinj's service provider._

### Grpc Server

Grpc end points can be inspected and called from the command line, using `grpcurl`.
  
See https://github.com/fullstorydev/grpcurl
See https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md
See https://offensivedefence.co.uk/posts/grpc-attack-surface

#### Examples
List Endpoints
```asciidoc
    $ grpcurl --plaintext   localhost:9999 list
         grpc.reflection.v1alpha.ServerReflection
         io.misq.protobuffer.GetVersion
         io.misq.protobuffer.Help
         io.misq.protobuffer.Wallets
```
Call Endpoints
```asciidoc
    $ grpcurl --plaintext   localhost:9999 grpc.proto.Wallets/GetBalance
    $ grpcurl --plaintext   localhost:9999 grpc.proto.Help/GetMethodHelp
    $ grpcurl --plaintext   localhost:9999 grpc.proto.P2P/GetPeers
    $ grpcurl --plaintext   localhost:9999 grpc.proto.GetVersion/GetVersion
    $ grpcurl --plaintext   localhost:9999 grpc.proto.WalletInstaller/InstallWallet
```

Describe Services
```asciidoc
    $ grpcurl --plaintext   localhost:9999 describe grpc.proto.P2P
    $ grpcurl --plaintext   localhost:9999 describe grpc.proto.Wallets
    $ grpcurl --plaintext   localhost:9999 describe grpc.proto.WalletInstaller
```


#### Dynamically Load Bitcoinj

- Create bitcoin-core regtest data dir:
  `$ mkdir /tmp/regtest`


- Start bitcoind in regest mode:
  `bitcoind -datadir=/tmp/regtest -regtest=1 -server=1 -txindex=1 -peerbloomfilters=1 -debug=net`


- Dynamically bootstrap Bitcoinj:
  `$ grpcurl --plaintext   localhost:9999 grpc.proto.WalletInstaller/InstallWallet`

_Note: The grpc module's `GrpcWalletInstallerService` uses the `CompletableFuture` api to load bitcoinj's service provider._

## GraalVM

POC was developed on GraalVM 16, but Graal's `native-image` compiler could not build the application 
distribution's jars into a native app.  POC III will be built on OpenJDK.

## Dependency Injection

- Does not use google guice.
- Does use home-made reflection based dependency injector, which will be discarded in POC III.  We will take
care to cleanly wire up services into api instances without any reflection based DI.