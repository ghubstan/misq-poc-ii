package org.misq.web.server.handler;


import org.misq.api.CoreApi;
import org.misq.web.json.JsonTransform;
import ratpack.handling.Context;
import ratpack.handling.Handler;


public class GetVersionHandler extends AbstractHandler implements Handler {

    public GetVersionHandler(JsonTransform jsonTransform) {
        super(jsonTransform);
    }

    @Override
    public void handle(Context ctx) {
        String version = ctx.get(CoreApi.class).getVersion();
        ctx.render(toJson("version", version));
    }
}
