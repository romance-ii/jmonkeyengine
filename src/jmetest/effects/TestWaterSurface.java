/*
 * Copyright (c) 2003-2004, jMonkeyEngine - Mojo Monkey Coding
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
package jmetest.effects;

import com.jme.app.SimpleGame;
import com.jme.effects.water.WaterSurface;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

/**
 * @author Joshua Slack
 * @version $Id: TestWaterSurface.java,v 1.7 2005-02-10 21:48:27 renanse Exp $
 */
public class TestWaterSurface extends SimpleGame {

  private WaterSurface water;

  public static void main(String[] args) {
    TestWaterSurface app = new TestWaterSurface();
    app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);
    app.start();
  }

  protected void simpleUpdate() {
  }

  protected void simpleInitGame() {
    KeyBindingManager.getKeyBindingManager().set(
        "drip",
        KeyInput.KEY_Q);

    display.setTitle("Water Surface");
    water = new WaterSurface("water", 64, 64, .1f);
    water.copyTextureCoords(0,1);
    rootNode.attachChild(water);
    lightState.setEnabled(false);

    TextureState ts = display.getRenderer().createTextureState();
    ts.setEnabled(true);
    Texture t1 = TextureManager.loadTexture(
        TestWaterSurface.class.getClassLoader().getResource(
        "jmetest/data/texture/water.png"),
        Texture.MM_LINEAR_LINEAR,
        Texture.FM_LINEAR);
    //Environmental Map (reflection of clouds)
    Texture t = TextureManager.loadTexture(
            TestWaterSurface.class.getClassLoader().getResource(
            "jmetest/data/texture/clouds.png"),
        Texture.MM_LINEAR_LINEAR,
        Texture.FM_LINEAR);
    t.setApply(Texture.AM_DECAL);
    t.setEnvironmentalMapMode(Texture.EM_SPHERE);
    ts.setTexture(t1);
    ts.setTexture(t, 1);
    rootNode.setRenderState(ts);
  }
}
