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

import org.misq.common.di.annotation.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;

import static java.net.InetAddress.getLoopbackAddress;

@SuppressWarnings({"rawtypes", "unchecked"})
@Singleton
public class P2PService {
    private static final Logger log = LoggerFactory.getLogger(P2PService.class);

    public PeersResponse getPeers() {
        NodeAddress nodeAddress = new NodeAddress(getLoopbackAddress().getHostName(), 10_0000);
        PeersRequest peersRequest = new PeersRequest(nodeAddress);
        log.info("Sending peers request from {}", peersRequest);
        HashSet<Peer> reportedPeers = new HashSet();
        reportedPeers.add(new Peer(new NodeAddress("abbey.onion", 10_000), new Date().getTime()));
        reportedPeers.add(new Peer(new NodeAddress("bubba.onion", 10_000), new Date().getTime()));
        reportedPeers.add(new Peer(new NodeAddress("cecil.onion", 10_000), new Date().getTime()));
        reportedPeers.add(new Peer(new NodeAddress("dane.onion", 10_000), new Date().getTime()));
        return new PeersResponse(reportedPeers);
    }
}
