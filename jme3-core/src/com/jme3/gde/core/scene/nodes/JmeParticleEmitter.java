/*
 *  Copyright (c) 2009-2010 jMonkeyEngine
 *  All rights reserved.
 * 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 *  * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme3.gde.core.scene.nodes;

import com.jme3.effect.EmitterShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.gde.core.scene.nodes.properties.JmeProperty;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.awt.Image;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author normenhansen
 */
public class JmeParticleEmitter extends JmeGeometry{
    private static Image smallImage =
            ImageUtilities.loadImage("com/jme3/gde/core/scene/nodes/icons/particleemitter.gif");
    private ParticleEmitter geom;

    public JmeParticleEmitter(ParticleEmitter spatial, JmeSpatialChildFactory children) {
        super(spatial, children);
        this.geom = spatial;
        setName(spatial.getName());
    }

    @Override
    public Image getIcon(int type) {
        return smallImage;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return smallImage;
    }

    @Override
    protected Sheet createSheet() {
        //TODO: multithreading..
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setDisplayName("ParticleEmitter");
        set.setName(ParticleEmitter.class.getName());
        ParticleEmitter obj = geom;//getLookup().lookup(Spatial.class);
        if (obj == null) {
            return sheet;
        }

//        set.put(makeProperty(obj, ParticleMesh.Type.class, "getType", "type"));
        set.put(makeProperty(obj, boolean.class, "isEnabled", "setEnabled", "enabled"));
//        set.put(makeProperty(obj, int.class, "getNumParticles", "setNumParticles", "num particles"));
        set.put(makeProperty(obj, float.class, "getParticlesPerSec", "setParticlesPerSec", "particles per sec"));
        set.put(makeProperty(obj, ColorRGBA.class, "getStartColor", "setStartColor", "start color"));
        set.put(makeProperty(obj, ColorRGBA.class, "getEndColor", "setEndColor", "end color"));
        set.put(makeProperty(obj, float.class, "getStartSize", "setStartSize", "start size"));
        set.put(makeProperty(obj, float.class, "getEndSize", "setEndSize", "end size"));
        set.put(makeProperty(obj, float.class, "getHighLife", "setHighLife", "high life"));
        set.put(makeProperty(obj, float.class, "getLowLife", "setLowLife", "low life"));
        set.put(makeProperty(obj, float.class, "getGravity", "setGravity", "gravity"));
        set.put(makeProperty(obj, Vector3f.class, "getStartVel", "setStartVel", "start velocity"));
        set.put(makeProperty(obj, Vector3f.class, "getFaceNormal", "setFaceNormal", "face normal"));
        set.put(makeProperty(obj, float.class, "getVariation", "setVariation", "variation"));
        set.put(makeProperty(obj, boolean.class, "isFacingVelocity", "setFacingVelocity", "facing velocity"));
        set.put(makeProperty(obj, boolean.class, "isRandomAngle", "setRandomAngle", "random angle"));
        set.put(makeProperty(obj, float.class, "getRotateSpeed", "setRotateSpeed", "rotate speed"));
        set.put(makeProperty(obj, boolean.class, "isSelectRandomImage", "setSelectRandomImage", "select random image"));
        set.put(makeProperty(obj, int.class, "getImagesX", "setImagesX", "images x"));
        set.put(makeProperty(obj, int.class, "getImagesY", "setImagesY", "images y"));

//        set.put(makeProperty(obj, EmitterShape.class, "getShape", "setShape", "shape"));



        sheet.put(set);
        return sheet;

    }

    private Property makeProperty(ParticleEmitter obj, Class returntype, String method, String name) {
        Property prop = null;
        try {
            prop = new JmeProperty(obj, returntype, method, null);
            prop.setName(name);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        return prop;
    }

    private Property makeProperty(ParticleEmitter obj, Class returntype, String method, String setter, String name) {
        Property prop = null;
        try {
            prop = new JmeProperty(obj, returntype, method, setter);
            prop.setName(name);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        return prop;
    }
}
