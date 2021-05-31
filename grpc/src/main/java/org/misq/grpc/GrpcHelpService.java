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
import org.misq.grpc.proto.GetMethodHelpReply;
import org.misq.grpc.proto.GetMethodHelpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.misq.grpc.proto.HelpGrpc.HelpImplBase;


public class GrpcHelpService extends HelpImplBase {
    private static final Logger log = LoggerFactory.getLogger(GrpcHelpService.class);

    private final CoreApi coreApi;

    public GrpcHelpService(CoreApi coreApi) {
        this.coreApi = coreApi;
    }

    @Override
    public void getMethodHelp(GetMethodHelpRequest req,
                              StreamObserver<GetMethodHelpReply> responseObserver) {
        try {
            String helpText = coreApi.getHelp();
            var reply = GetMethodHelpReply.newBuilder().setMethodHelp(helpText).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } catch (Throwable cause) {
            new GrpcExceptionHandler().handleException(log, cause, responseObserver);
        }
    }
}
