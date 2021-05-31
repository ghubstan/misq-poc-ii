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
import org.misq.api.CoreApi;
import org.misq.grpc.proto.GetBalanceReply;
import org.misq.grpc.proto.GetBalanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.misq.grpc.proto.WalletsGrpc.WalletsImplBase;

public class GrpcWalletsService extends WalletsImplBase {
    private static final Logger log = LoggerFactory.getLogger(GrpcWalletsService.class);

    private final CoreApi coreApi;

    public GrpcWalletsService(CoreApi coreApi) {
        this.coreApi = coreApi;
    }

    @Override
    public void getBalance(GetBalanceRequest req,
                           StreamObserver<GetBalanceReply> responseObserver) {
        try {
            long balance = coreApi.getBalance();
            var reply = GetBalanceReply.newBuilder().setBalance(balance).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } catch (Throwable cause) {
            new GrpcExceptionHandler().handleException(log, cause, responseObserver);
        }
    }
}
