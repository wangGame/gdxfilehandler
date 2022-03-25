package com.tony.rider.screen;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.tony.rider.screen.base.BaseScreen;
import com.tony.rider.tool.ScreenshotCreator;

public class LoadingScreen extends BaseScreen {

    public LoadingScreen() {
        super();
    }

    @Override
    public void show() {
        super.show();
        stage.addAction(Actions.delay(3,Actions.run(()->{
            ScreenshotCreator creator = new ScreenshotCreator();
            creator.saveScreenshot();

        })));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
