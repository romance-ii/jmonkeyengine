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

import com.jme3.bounding.BoundingVolume;
import com.jme3.gde.core.scene.nodes.properties.JmeProperty;
import com.jme3.light.LightList;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author normenhansen
 */
public class JmeSpatial extends AbstractNode{
    private Spatial spatial;

    public JmeSpatial(Spatial spatial, Children children) {
        super(children, Lookups.singleton(spatial));
        this.spatial=spatial;
        setName(spatial.getName());
    }

    @Override
    protected Sheet createSheet() {
        //TODO: multithreading..
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        Spatial obj = getLookup().lookup(Spatial.class);
        if(obj==null) return sheet;

        set.put(makeProperty(obj, Integer.class,"getVertexCount","vertexes"));
        set.put(makeProperty(obj, Integer.class,"getTriangleCount","triangles"));

//        set.put(makeProperty(obj, Transform.class,"getWorldTransform","world transform"));
        set.put(makeProperty(obj, Vector3f.class,"getWorldTranslation","world translation"));
        set.put(makeProperty(obj, Quaternion.class,"getWorldRotation","world rotation"));
        set.put(makeProperty(obj, Vector3f.class,"getWorldScale","world scale"));

        set.put(makeProperty(obj, Vector3f.class,"getLocalTranslation","setLocalTranslation","local translation"));
        set.put(makeProperty(obj, Quaternion.class,"getLocalRotation","local rotation"));
        set.put(makeProperty(obj, Vector3f.class,"getLocalScale","setLocalScale","local scale"));

        set.put(makeProperty(obj, BoundingVolume.class,"getWorldBound","world bound"));

        set.put(makeProperty(obj, CullHint.class,"getCullHint","setCullHint","cull hint"));
        set.put(makeProperty(obj, CullHint.class,"getLocalCullHint","local cull hint"));
        set.put(makeProperty(obj, ShadowMode.class,"getShadowMode","setShadowMode","shadow mode"));
        set.put(makeProperty(obj, ShadowMode.class,"getLocalShadowMode","local shadow mode"));
        set.put(makeProperty(obj, LightList.class,"getWorldLightList","world light list"));

        sheet.put(set);
        return sheet;

    }

    private Property makeProperty(Spatial obj, Class returntype, String method, String name){
        Property prop=null;
        try {
            prop = new JmeProperty(obj, returntype, method, null);
            prop.setName(name);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        return prop;
    }

    private Property makeProperty(Spatial obj, Class returntype, String method, String setter, String name){
        Property prop=null;
        try {
            prop = new JmeProperty(obj, returntype, method, setter);
            prop.setName(name);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        return prop;
    }
}
