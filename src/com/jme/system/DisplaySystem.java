/*
 * Copyright (c) 2003, jMonkeyEngine - Mojo Monkey Coding
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. 
 * 
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution. 
 * 
 * Neither the name of the Mojo Monkey Coding, jME, jMonkey Engine, nor the 
 * names of its contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.jme.system;

import com.jme.renderer.Renderer;

/**
 * <code>DisplaySystem</code> defines an interface for system creation.
 * Specifically, any implementing class will create a window for rendering. 
 * It also should create the appropriate <code>Renderer</code> object that
 * allows the client to render to this window. 
 * 
 * Implmenting classes should check for the appropriate libraries to insure 
 * these libraries are indeed installed on the system. This will allow users 
 * to cleanly exit if an improper library was chosen for rendering. 
 * 
 * Example usage:
 * 
 * <code>
 * DisplaySystem ds = DisplaySystem.getDisplaySystem("LWJGL");<br>
 * ds.createWindow(640,480,32,60,true);<br>
 * Renderer r = ds.getRenderer();<br>
 * </code>
 * 
 * @see com.jme.renderer.Renderer
 * 
 * @author Mark Powell
 * @version $Id: DisplaySystem.java,v 1.3 2003-10-23 21:24:18 mojomonkey Exp $
 */
public abstract class DisplaySystem {
    protected int width, height;
    
    /**
     * The list of current implemented rendering APIs that subclass Display.
     */
    public static final String[] rendererNames = {"LWJGL"};
    
    /**
     * 
     * <code>getDisplaySystem</code> is a factory method that creates the
     * appropriate display system specified by the key parameter. If the
     * key given is not a valid identifier for a specific display system,
     * null is returned. For valid display systems see the
     * <code>rendererNames</code> array.
     * @param key the display system to use.
     * @return the appropriate display system specified by the key.
     */
    public static DisplaySystem getDisplaySystem(String key) {
        if("LWJGL".equalsIgnoreCase(key)) {
            return new LWJGLDisplaySystem();
        }
        
        return null;
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    /**
     * <code>createWindow</code> creates a window with the desired settings. 
     * The width and height defined by w and h define the size of the window
     * if fullscreen is false, otherwise it defines the resolution of the
     * fullscreen display. The color depth is defined by bpp. The 
     * implementing class should only allow 16, 24, and 32. The monitor 
     * frequency is defined by the frq parameter and should not exceed the
     * capabilities of the connected hardware, the implementing class should
     * attempt to assure this does not happen. Lastly, the boolean flag fs 
     * determines if the display should be windowed or fullscreen. If false,
     * windowed is chosen. This window will be placed in the center of the
     * screen initially. If true fullscreen mode will be entered with the
     * appropriate settings.
     * @param w the width/horizontal resolution of the display.
     * @param h the height/vertical resolution of the display.
     * @param bpp the color depth of the display.
     * @param frq the frequency of refresh of the display.
     * @param fs flag determining if fullscreen is to be used or not. True will
     *      use fullscreen, false will use windowed mode.
     */
    public abstract void createWindow(int w, int h, int bpp, int frq, boolean fs);
    
    /**
     * <code>getRenderer</code> returns the <code>Renderer</code> implementation
     * that is compatible with the chosen <code>DisplaySystem</code>. For 
     * example, if <code>LWJGLDisplaySystem</code> is used, the returned 
     * <code>Renderer</code> will be </code>LWJGLRenderer</code>.
     * @see com.jme.renderer.Renderer
     * @return the appropriate <code>Renderer</code> implementation that is
     *      compatible with the used <code>DisplaySystem</code>.
     */
    public abstract Renderer getRenderer();
    
    /**
     * <code>isCreated</code> returns the current status of the display
     * system. If the window and renderer are created, true is returned,
     * otherwise false.
     * 
     * @return whether the display system is created.
     */
    public abstract boolean isCreated();
    
    /**
     * <code>isClosing</code> notifies if the window is currently closing.
     * This could be caused via the application itself or external interrupts
     * such as alt-f4 etc.
     * @return true if the window is closing, false otherwise.
     */
    public abstract boolean isClosing();
    
    /**
     * <code>reset</code> cleans up the display system for closing or restarting.
     *
     */
    public abstract void reset();
    
}
