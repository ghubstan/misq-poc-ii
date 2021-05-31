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

package org.misq.p2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PeersResponse {

    private static final Logger log = LoggerFactory.getLogger(PeersResponse.class);

    private final Set<Peer> reportedPeers;

    public PeersResponse(Set<Peer> reportedPeers) {
        this.reportedPeers = reportedPeers;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // PROTO BUFFER
    ///////////////////////////////////////////////////////////////////////////////////////////

    public org.misq.p2p.proto.PeersResponse toProtoMessage() {
        Set<Peer> clone = new HashSet<>(reportedPeers);
        return org.misq.p2p.proto.PeersResponse.newBuilder()
                .addAllReportedPeers(clone.stream()
                        .map(Peer::toProtoMessage)
                        .collect(Collectors.toList()))
                .build();
    }

    public static PeersResponse fromProto(org.misq.p2p.proto.PeersResponse proto) {
        HashSet<Peer> reportedPeers = proto.getReportedPeersList()
                .stream()
                .map(peer -> {
                    NodeAddress nodeAddress = new NodeAddress(peer.getNodeAddress().getHostName(), peer.getNodeAddress().getPort());
                    return new Peer(nodeAddress, new Date().getTime());
                })
                .collect(Collectors.toCollection(HashSet::new));
        return new PeersResponse(reportedPeers);
    }
}
