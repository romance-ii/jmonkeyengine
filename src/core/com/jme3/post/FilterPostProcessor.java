/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme3.post;

import com.jme3.renderer.Renderer;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilterPostProcessor implements SceneProcessor {

    private RenderManager renderManager;
    private Renderer renderer;
    private ViewPort viewPort;
    private FrameBuffer renderFrameBuffer;
    private Texture2D filterTexture;
    private Texture2D depthTexture; 
    private List<Filter> filters = new ArrayList<Filter>();
    private AssetManager assetManager;
    private Camera filterCam = new Camera(1, 1);
    private Picture fsQuad;
    private boolean computeDepth=false;

    public FilterPostProcessor(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
        if (isInitialized()) {
            filter.init(assetManager,viewPort.getCamera().getWidth(), viewPort.getCamera().getHeight());
        }
    }

    public void removeFilter(Filter filter) {
        for (Iterator<Filter> it = filters.iterator(); it.hasNext();) {
            if (it.next() == filter) {
                it.remove();
            }
        }
    }

    public void initialize(RenderManager rm, ViewPort vp) {
        renderManager = rm;
        renderer = rm.getRenderer();
        viewPort = vp;
        fsQuad = new Picture("filter full screen quad");

        int w = vp.getCamera().getWidth();
        int h = vp.getCamera().getHeight();

        reshape(vp, w, h);
    }

    private void renderProcessing(Renderer r, FrameBuffer buff, Material mat) {
        if (buff == null) {
            fsQuad.setWidth(renderFrameBuffer.getWidth());
            fsQuad.setHeight(renderFrameBuffer.getHeight());
            filterCam.resize(renderFrameBuffer.getWidth(), renderFrameBuffer.getHeight(), true);
        } else {
            fsQuad.setWidth(buff.getWidth());
            fsQuad.setHeight(buff.getHeight());
            filterCam.resize(buff.getWidth(), buff.getHeight(), true);
        }
        fsQuad.setMaterial(mat);
        fsQuad.updateGeometricState();

        renderManager.setCamera(filterCam, true);
        r.setFrameBuffer(buff);
        r.clearBuffers(true, true, true);
        renderManager.renderGeometry(fsQuad);
    }

    public boolean isInitialized() {
        return viewPort != null;
    }

    public void postQueue(RenderQueue rq) {
        for (Iterator<Filter> it = filters.iterator(); it.hasNext();) {
            Filter filter = it.next();
            filter.preRender(renderManager, viewPort);
        }
    }

    public void renderFilterChain(Renderer r) {
        Texture2D tex = filterTexture;
        for (Iterator<Filter> it = filters.iterator(); it.hasNext();) {
            Filter filter = it.next();

            if(filter.getPostRenderPasses()!=null){
                for (Iterator<Filter.Pass> it1 = filter.getPostRenderPasses().iterator(); it1.hasNext();) {
                    Filter.Pass pass = it1.next();
                    pass.getPassMaterial().setTexture("m_Texture", tex);
                    renderProcessing(r, pass.getRenderFrameBuffer(), pass.getPassMaterial());
                }
            }
            
            Material mat = filter.getMaterial();
            if(computeDepth && filter.isRequiresDepthTexture()){
                mat.setTexture("m_DepthTexture", depthTexture);
            }
            
            mat.setTexture("m_Texture", tex);
            FrameBuffer buff = null;
            if (it.hasNext()) {
                buff = filter.getRenderFrameBuffer();
                tex = filter.getRenderedTexture();
            }
            renderProcessing(r, buff, mat);
        }
    }

    public void postFrame(FrameBuffer out) {
        renderFilterChain(renderer);
        renderManager.setCamera(viewPort.getCamera(), false);
    }

    public void preFrame(float tpf) {
        if (filters.size() == 0) {
            viewPort.setOutputFrameBuffer(null);
        }
    }

    public void cleanup() {

        if (viewPort != null) {
            viewPort.setOutputFrameBuffer(null);
            viewPort = null;
        }

        for (Iterator<Filter> it = filters.iterator(); it.hasNext();) {
            Filter filter = it.next();
            filter.cleanup(renderer);
        }
    }

    public void reshape(ViewPort vp, int w, int h) {

        for (Iterator<Filter> it = filters.iterator(); it.hasNext();) {
            Filter filter = it.next();
            filter.init(assetManager,w, h);
            computeDepth=filter.isRequiresDepthTexture();
            
        }
     

        if (renderFrameBuffer == null) {

            renderFrameBuffer = new FrameBuffer(w, h, 0);
            renderFrameBuffer.setDepthBuffer(Format.Depth);
            filterTexture = new Texture2D(w, h, Format.RGB32F);
            renderFrameBuffer.setColorTexture(filterTexture);

            if(computeDepth){
                 depthTexture = new Texture2D(w, h, Format.Depth);
                renderFrameBuffer.setDepthTexture(depthTexture);
               
            }
           

        }
        viewPort.setOutputFrameBuffer(renderFrameBuffer);
    }
}



