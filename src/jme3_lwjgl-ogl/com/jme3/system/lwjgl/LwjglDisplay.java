package com.jme3.system.lwjgl;

import com.jme3.system.JmeContext.Type;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LwjglDisplay extends LwjglAbstractDisplay {

    private static final Logger logger = Logger.getLogger(LwjglDisplay.class.getName());

    protected DisplayMode getFullscreenDisplayMode(int width, int height, int bpp, int freq){
        try {
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            for (DisplayMode mode : modes){
                if (mode.getWidth() == width && mode.getHeight() == height
                 && mode.getBitsPerPixel() == bpp && mode.getFrequency() == freq){
                    return mode;
                }
            }
        } catch (LWJGLException ex) {
            logger.log(Level.SEVERE, "Failed to acquire fullscreen display mode!", ex);
        }
        return null;
    }

    protected void applySettings(AppSettings settings) throws LWJGLException{
        DisplayMode displayMode = null;
        if (settings.getWidth() <= 0 || settings.getHeight() <= 0){
//            displayMode = org.lwjgl.opengl.Display.getDesktopDisplayMode();
//            settings.setResolution(displayMode.getWidth(), displayMode.getHeight());
        }else if (settings.isFullscreen()){
            displayMode = getFullscreenDisplayMode(settings.getWidth(), settings.getHeight(),
                                                   settings.getBitsPerPixel(), settings.getFrequency());
            if (displayMode == null)
                throw new RuntimeException("Unable to find fullscreen display mode matching settings");
        }else{
            displayMode = new DisplayMode(settings.getWidth(), settings.getHeight());
        }

        frameRate = settings.getFrameRate();
        logger.info("Selected display mode: "+displayMode);
        
        Display.setTitle(settings.getTitle());
        if (displayMode != null)
            Display.setDisplayMode(displayMode);

        Display.setFullscreen(settings.isFullscreen());
        Display.setVSyncEnabled(settings.isVSync());
    }

    @Override
    public void restart() {
        if (created.get()){
            try{
                applySettings(settings);
            }catch (LWJGLException ex){
                logger.log(Level.SEVERE, "Failed to set display settings!", ex);
            }
            listener.reshape(settings.getWidth(), settings.getHeight());
            logger.info("Display restarted.");
        }else{
            logger.warning("Display is not created, cannot restart window.");
        }
    }

    public Type getType() {
        return Type.Display;
    }

    public void setTitle(String title){
        if (created.get())
            Display.setTitle(title);
    }

}
