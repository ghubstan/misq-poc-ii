package org.misq.web.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.misq.p2p.PeersResponse;

import java.util.Map;

public class JsonTransform {

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    public String toJson(Map<String, String> map) {
        return gson.toJson(map, map.getClass());
    }

    public String toJson(PeersResponse peersResponse) {
        return gson.toJson(peersResponse, peersResponse.getClass());
    }
}
