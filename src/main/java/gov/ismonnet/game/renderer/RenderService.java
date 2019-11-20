package gov.ismonnet.game.renderer;

import gov.ismonnet.util.ScaledResolution;

public interface RenderService<T extends Screen> {

    void setScreen(Screen.Type screen);

    Screen.Type getScreenType();

    T getScreen(Screen.Type type);

    T getCurrentScreen();

    ScaledResolution getScaledResolution();

    Side getSide();

    void switchSide();

    enum Side { LEFT, RIGHT }
}
