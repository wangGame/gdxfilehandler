package com.tony.rider.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ScreenshotCreator {
    // %tF == year-month-day
    // %tH, %tM, %tS, %tL == hours, minutes, seconds, milliseconds
    // %< == reuse argument from previous placeholder
    // See https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
    private static final String FILENAME_FORMAT = "screenshot-%tF-%<tH%<tM%<tS.%<tL.png";
    private static Pixmap sPixmap;
    private static PixmapIO.PNG sPNG;

    public static String saveScreenshot() {
        Pixmap pixmap = takeScreenshot();
        FileHandle handle = generateFileHandle();
        try {
            sPNG.write(handle, pixmap);
        } catch (IOException ex) {
            throw new GdxRuntimeException("Error writing PNG: " + handle, ex);
        }
        return handle.path();
    }

    private static Pixmap takeScreenshot() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        if (sPixmap == null) {
            init();
        }
        Gdx.gl.glReadPixels(
                0, 0, width, height, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, sPixmap.getPixels());
        return sPixmap;
    }

    private static void init() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        sPixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
        // Guess at deflated size
        sPNG = new PixmapIO.PNG((int) (width * height * 1.5f));
    }

    private static FileHandle generateFileHandle() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String filename = String.format(Locale.US, FILENAME_FORMAT, calendar);
        return FileUtils.getUserWritableFile(filename);
    }
}

