package com.jme3.system.lwjgl;

import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.lwjgl.LwjglJoyInput;
import com.jme3.input.lwjgl.LwjglKeyInput;
import com.jme3.input.lwjgl.LwjglMouseInput;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.system.JmeSystem;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;


public abstract class LwjglAbstractDisplay extends LwjglContext implements Runnable {

    private static final Logger logger = Logger.getLogger(LwjglDisplay.class.getName());
    protected AtomicBoolean needClose = new AtomicBoolean(false);
    protected boolean wasActive = false;
    protected int frameRate = 0;
    protected boolean autoFlush = true;

    /**
     * @return Type.Display or Type.Canvas
     */
    public abstract Type getType();

    /**
     * Set the title if its a windowed display
     * @param title
     */
    public abstract void setTitle(String title);

    /**
     * Restart if its a windowed or full-screen display.
     */
    public abstract void restart();

    /**
     * Apply the settings, changing resolution, etc.
     * @param settings
     */
    protected abstract void createContext(AppSettings settings) throws LWJGLException;

    /**
     * Does LWJGL display initialization in the OpenGL thread
     */
    protected void initInThread(){
        

        try{
            createContext(settings);
//            String rendererStr = settings.getString("Renderer");

            logger.info("Display created.");
            logger.log(Level.FINE, "Running on thread: {0}", Thread.currentThread().getName());

            if (!JmeSystem.isLowPermissions()){
                Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread thread, Throwable thrown) {
                        listener.handleError("Uncaught exception thrown in "+thread.toString(), thrown);
                    }
                });
            }

            Keyboard.poll();
            Mouse.poll();

            String vendor = GL11.glGetString(GL11.GL_VENDOR);
            String version = GL11.glGetString(GL11.GL_VERSION);
            String renderer = GL11.glGetString(GL11.GL_RENDERER);
            String shadingLang = GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);

            logger.info("Vendor: "+vendor);
            logger.info("Renderer: "+renderer);

            logger.info("Adapter: "+Display.getAdapter());
            logger.info("Driver Version: "+Display.getVersion());

            logger.info("OpenGL Version: "+version);
            logger.info("GLSL Ver: "+shadingLang);

            created.set(true);
        } catch (LWJGLException ex){
            listener.handleError("Failed to create display", ex);
        } finally {
            // TODO: It is possible to avoid "Failed to find pixel format"
            // error here by creating a default display.

            if (!created.get()){
                if (Display.isCreated())
                    Display.destroy();
            }
        }
        super.create();
        listener.initialize();
    }

    /**
     * execute one iteration of the render loop in the OpenGL thread
     */
    protected void runLoop(){
        if (!created.get())
            throw new IllegalStateException();

        listener.update();

        // calls swap buffers, etc.
        try {
            if (autoFlush){
                Display.update();
            }else{
                Display.processMessages();
            }
        } catch (Throwable ex){
            listener.handleError("Error while swapping buffers", ex);
        }

        if (frameRate > 0)
            Display.sync(frameRate);

        if (autoFlush)
            renderer.onFrame();
    }

    /**
     * De-initialize in the OpenGL thread.
     */
    protected void deinitInThread(){
        listener.destroy();
        if (Display.isCreated()){
            renderer.cleanup();
            Display.destroy();
        }
        logger.info("Display destroyed.");
        super.destroy();
    }

    public void run(){
        logger.info("Using LWJGL "+Sys.getVersion());
        initInThread();
        while (true){
            if (Display.isCloseRequested())
                listener.requestClose(false);

            if (wasActive != Display.isActive()){
                if (!wasActive){
                    listener.gainFocus();
                    wasActive = true;
                }else{
                    listener.loseFocus();
                    wasActive = false;
                }
            }

            runLoop();

            if (needClose.get())
                break;
        }
        deinitInThread();
    }

    public JoyInput getJoyInput() {
        return new LwjglJoyInput();
    }

    public MouseInput getMouseInput() {
        return new LwjglMouseInput();
    }

    public KeyInput getKeyInput() {
        return new LwjglKeyInput();
    }

    public void setAutoFlushFrames(boolean enabled){
        this.autoFlush = enabled;
    }

    @Override
    public void destroy(){
        needClose.set(true);
    }

}
