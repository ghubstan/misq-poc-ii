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

public class Peer {

    private final NodeAddress nodeAddress;
    private final long date;

    public Peer(NodeAddress nodeAddress, long date) {
        this.nodeAddress = nodeAddress;
        this.date = date;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // PROTO BUFFER
    ///////////////////////////////////////////////////////////////////////////////////////////

    public org.misq.p2p.proto.Peer toProtoMessage() {
        return org.misq.p2p.proto.Peer.newBuilder()
                .setNodeAddress(nodeAddress.toProtoMessage())
                .setDate(date)
                .build();
    }

    public static Peer fromProto(org.misq.p2p.proto.Peer proto) {
        return new Peer(NodeAddress.fromProto(proto.getNodeAddress()), proto.getDate());
    }

    @Override
    public String toString() {
        return "Peer{" +
                "nodeAddress=" + nodeAddress +
                ", date=" + date +
                '}';
    }
}
