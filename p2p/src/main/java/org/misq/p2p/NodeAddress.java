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

public class NodeAddress {

    private final String hostName;
    private final int port;

    public NodeAddress(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // PROTO BUFFER
    ///////////////////////////////////////////////////////////////////////////////////////////

    public org.misq.p2p.proto.NodeAddress toProtoMessage() {
        return org.misq.p2p.proto.NodeAddress.newBuilder().setHostName(hostName).setPort(port).build();
    }

    public static NodeAddress fromProto(org.misq.p2p.proto.NodeAddress proto) {
        return new NodeAddress(proto.getHostName(), proto.getPort());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////////////////////

    public String getFullAddress() {
        return hostName + ":" + port;
    }

    @Override
    public String toString() {
        return getFullAddress();
    }
}
