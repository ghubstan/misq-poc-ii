package org.misq.web.server.handler;


import org.misq.api.CoreApi;
import org.misq.web.json.JsonTransform;
import ratpack.handling.Context;
import ratpack.handling.Handler;


public class GetBalanceHandler extends AbstractHandler implements Handler {

    public GetBalanceHandler(JsonTransform jsonTransform) {
        super(jsonTransform);
    }

    @Override
    public void handle(Context ctx) {
        long balance = ctx.get(CoreApi.class).getBalance();
        ctx.render(toJson("balance", balance));
    }
}
