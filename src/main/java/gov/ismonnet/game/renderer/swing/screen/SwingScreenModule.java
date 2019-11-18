package gov.ismonnet.game.renderer.swing.screen;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import gov.ismonnet.game.GameSession;
import gov.ismonnet.game.renderer.Screen;
import gov.ismonnet.game.renderer.swing.SwingScreen;

@Module
public abstract class SwingScreenModule {

    @Binds @GameSession
    abstract @GameScreen.Qualifier SwingScreen gameScreen0(GameScreen gameScreen);

    @Binds @GameSession
    @IntoMap @Screen.TypeKey(Screen.Type.GAME)
    abstract SwingScreen gameScreen(@GameScreen.Qualifier SwingScreen gameScreen);

    @Binds @GameSession
    @IntoMap @Screen.TypeKey(Screen.Type.PAUSE)
    abstract SwingScreen pauseScreen(PauseScreen gameScreen);
}
