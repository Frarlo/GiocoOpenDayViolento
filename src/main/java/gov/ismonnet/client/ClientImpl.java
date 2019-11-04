package gov.ismonnet.client;

import gov.ismonnet.client.renderer.RenderServiceFactory;

import javax.inject.Inject;

public class ClientImpl implements Client {

    @Inject ClientImpl(RenderServiceFactory factory) {
        factory.create(this::onTick);
    }

    private void onTick() {
    }
}
