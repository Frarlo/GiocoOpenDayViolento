package gov.ismonnet.game.renderer;

import dagger.MapKey;
import gov.ismonnet.util.ScaledResolution;

public interface Screen<T extends RenderContext> {

    void init(ScaledResolution scaledResolution);

    void destroy();

    void render(T ctx);

    enum Type { GAME, PAUSE }

    @MapKey
    @interface TypeKey {
        Type value();
    }
}
