/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package org.misq.grpc;

import io.grpc.stub.StreamObserver;
import org.misq.grpc.proto.InstallWalletReply;
import org.misq.grpc.proto.InstallWalletRequest;
import org.misq.wallet.installer.WalletInstallerApi;
import org.misq.wallet.installer.proto.WalletType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static org.misq.grpc.proto.WalletInstallerGrpc.WalletInstallerImplBase;
import static org.misq.wallet.installer.WalletInstallerApi.CONCURRENCY_FRWK.COMPLETABLE_FUTURE;

// $ grpcurl --plaintext   localhost:9999 grpc.proto.WalletInstaller/InstallWallet

public class GrpcWalletInstallerService extends WalletInstallerImplBase {
    private static final Logger log = LoggerFactory.getLogger(GrpcWalletInstallerService.class);

    private final WalletInstallerApi walletInstallerApi;

    public GrpcWalletInstallerService(WalletInstallerApi walletInstallerApi) {
        this.walletInstallerApi = walletInstallerApi;
    }

    @Override
    public void installWallet(InstallWalletRequest req,
                              StreamObserver<InstallWalletReply> responseObserver) {
        try {
            var walletType = WalletType.BITCOINJ;   // Ignore: req.getWalletType();
            try {
                walletInstallerApi.installBitcoinj(COMPLETABLE_FUTURE);
            } catch (Throwable t) {
                log.error("", t);
            }
            var reply = InstallWalletReply.newBuilder()
                    .setStatus("Downloading Bitcoinj jar, loading classes, initializing and shutting down wallet.")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } catch (Throwable cause) {
            new GrpcExceptionHandler().handleException(log, cause, responseObserver);
        }
    }
}
