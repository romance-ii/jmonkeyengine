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
package com.jme.scene.state;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL;

import com.jme.light.DirectionalLight;
import com.jme.light.Light;
import com.jme.light.PointLight;
import com.jme.light.SpotLight;

/**
 * <code>LWJGLLightState</code> subclasses the Light class using the LWJGL
 * API to access OpenGL for light processing.
 * @author Mark Powell
 * @version $Id: LWJGLLightState.java,v 1.1 2003-10-13 18:30:09 mojomonkey Exp $
 */
public class LWJGLLightState extends LightState {
    //buffer for light colors.
    private FloatBuffer buffer;

    /**
     * Constructor instantiates a new <code>LWJGLLightState</code>.
     *
     */
    public LWJGLLightState() {
        super();
        buffer =
            ByteBuffer
                .allocateDirect(16)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }

    /**
     * <code>set</code> iterates over the light queue and processes each 
     * individual light.
     * @see com.jme.scene.state.RenderState#set()
     */
    public void set() {
        int quantity = getQuantity();

        if (quantity > 0) {
            GL.glEnable(GL.GL_LIGHTING);

            float[] ambient = { 0.0f, 0.0f, 0.0f, 1.0f };
            float[] color = new float[4];
            color[3] = 1.0f;

            for (int i = 0; i < quantity; i++) {
                Light light = get(i);
                if (light.isEnabled()) {
                    int index = GL.GL_LIGHT0 + i;
                    GL.glEnable(index);

                    color[0] = light.getAmbient().r;
                    color[1] = light.getAmbient().g;
                    color[2] = light.getAmbient().b;

                    buffer.clear();
                    buffer.put(color);
                    buffer.flip();

                    GL.glLightfv(index, GL.GL_AMBIENT, buffer);

                    color[0] = light.getDiffuse().r;
                    color[1] = light.getDiffuse().g;
                    color[2] = light.getDiffuse().b;

                    buffer.clear();
                    buffer.put(color);
                    buffer.flip();

                    GL.glLightfv(index, GL.GL_DIFFUSE, buffer);

                    color[0] = light.getSpecular().r;
                    color[1] = light.getSpecular().g;
                    color[2] = light.getSpecular().b;

                    buffer.clear();
                    buffer.put(color);
                    buffer.flip();

                    GL.glLightfv(index, GL.GL_SPECULAR, buffer);

                    if (light.isAttenuate()) {
                        GL.glLightf(
                            index,
                            GL.GL_CONSTANT_ATTENUATION,
                            light.getConstant());
                        GL.glLightf(
                            index,
                            GL.GL_LINEAR_ATTENUATION,
                            light.getLinear());
                        GL.glLightf(
                            index,
                            GL.GL_QUADRATIC_ATTENUATION,
                            light.getQuadratic());
                    } else {
                        GL.glLightf(index, GL.GL_CONSTANT_ATTENUATION, 1.0f);
                        GL.glLightf(index, GL.GL_LINEAR_ATTENUATION, 0.0f);
                        GL.glLightf(index, GL.GL_QUADRATIC_ATTENUATION, 0.0f);
                    }

                    if (light.getType() == Light.LT_AMBIENT) {
                        ambient[0] += light.getAmbient().r;
                        ambient[1] += light.getAmbient().g;
                        ambient[2] += light.getAmbient().b;
                    }

                    float[] posParam = new float[4];
                    switch (light.getType()) {
                        case Light.LT_DIRECTIONAL :
                            {
                                DirectionalLight pkDL =
                                    (DirectionalLight) light;
                                posParam[0] = -pkDL.getDirection().x;
                                posParam[1] = -pkDL.getDirection().y;
                                posParam[2] = -pkDL.getDirection().z;
                                posParam[3] = 0.0f;

                                buffer.clear();
                                buffer.put(posParam);
                                buffer.flip();
                                GL.glLightfv(index, GL.GL_POSITION, buffer);
                                break;
                            }
                        case Light.LT_POINT :
                        case Light.LT_SPOT :
                            {
                                PointLight pkPL = (PointLight) light;
                                posParam[0] = pkPL.getLocation().x;
                                posParam[1] = pkPL.getLocation().y;
                                posParam[2] = pkPL.getLocation().z;
                                posParam[3] = 1.0f;
                                break;
                            }
                    }

                    if (light.getType() == Light.LT_SPOT) {
                        SpotLight spot = (SpotLight) light;
                        GL.glLightf(
                            index,
                            GL.GL_SPOT_CUTOFF,
                            180.0f * spot.getAngle() / (float) Math.PI);
                        buffer.clear();
                        float[] spotDir =
                            {
                                spot.getDirection().x,
                                spot.getDirection().y,
                                spot.getDirection().z };
                        buffer.put(spotDir);
                        buffer.flip();
                        GL.glLightfv(index, GL.GL_SPOT_DIRECTION, buffer);
                        GL.glLightf(
                            index,
                            GL.GL_SPOT_EXPONENT,
                            spot.getExponent());
                    } else {
                        float[] afDefaultDirection = { 0.0f, 0.0f, -1.0f };
                        GL.glLightf(index, GL.GL_SPOT_CUTOFF, 180.0f);
                        buffer.clear();
                        buffer.put(afDefaultDirection);
                        buffer.flip();
                        GL.glLightfv(index, GL.GL_SPOT_DIRECTION, buffer);
                        GL.glLightf(index, GL.GL_SPOT_EXPONENT, 0.0f);
                    }
                }
            }

            buffer.clear();
            buffer.put(ambient);
            buffer.flip();
            GL.glLightModel(GL.GL_LIGHT_MODEL_AMBIENT, buffer);

            for (int i = quantity; i < MAX_LIGHTS_ALLOWED; i++)
                GL.glDisable((GL.GL_LIGHT0 + i));
        } else {
            GL.glDisable(GL.GL_LIGHTING);
        }

    }

    /**
     * <code>unset</code> turns off lighting.
     * @see com.jme.scene.state.RenderState#unset()
     */
    public void unset() {
        if (getQuantity() > 0) {
            GL.glDisable(GL.GL_LIGHTING);
        }
    }

}
