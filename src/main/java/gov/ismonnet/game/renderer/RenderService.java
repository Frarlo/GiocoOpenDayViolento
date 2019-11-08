package gov.ismonnet.game.renderer;

public interface RenderService {

    Side getSide();

    void switchSide();

    enum Side { LEFT, RIGHT }
}
