package com.tony.rider.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import java.io.IOException;

/** Utility methods to deal with the platform */
public class PlatformUtils {
    private enum UiType {
        BUTTONS,
        TOUCH
    }

    private static UiType sUiType;

    public static boolean isTouchUi() {
        init();
        return sUiType == UiType.TOUCH;
    }

    public static boolean isDesktop() {
        switch (Gdx.app.getType()) {
            case Desktop:
            case HeadlessDesktop:
            case Applet:
            case WebGL:
                return true;
            default:
                return false;
        }
    }

    /** An implementation of Gdx.net.openURI which works on Linux */
    public static void openURI(String uri) {
        if (Gdx.net.openURI(uri)) {
            return;
        }
//        NLog.i("Gdx.net.openURI() failed");
        String command = null;
        if (SharedLibraryLoader.isLinux) {
            command = "xdg-open";
        } else if (SharedLibraryLoader.isWindows) {
            command = "start";
        } else if (SharedLibraryLoader.isMac) {
            command = "open";
        }
        if (command == null) {
//            NLog.e("Don't know how to open url %s on this OS", uri);
            return;
        }
        try {
//            NLog.i("Trying with '%s %s'", command, uri);
            new ProcessBuilder().command(command, uri).start();
        } catch (IOException e) {
//            NLog.e("Command failed: %s", e);
        }
    }

    private static void init() {
        if (sUiType != null) {
            return;
        }
        String envValue = System.getenv("AGC_UI_TYPE");
        if (envValue == null) {
            sUiType = isDesktop() ? UiType.BUTTONS : UiType.TOUCH;
        } else {
            sUiType = UiType.valueOf(envValue);
//            NLog.d("Forcing UI type to %s", sUiType);
        }
//        NLog.i("UI type: %s", sUiType);
    }
}
