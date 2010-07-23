package com.jme3.renderer;

import com.jme3.math.ColorRGBA;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.texture.FrameBuffer;
import java.util.ArrayList;
import java.util.List;

public class ViewPort {

    protected final String name;
    protected final Camera cam;
    protected final RenderQueue queue = new RenderQueue();
    protected final ArrayList<Spatial> sceneList = new ArrayList<Spatial>();
    protected final ArrayList<SceneProcessor> processors = new ArrayList<SceneProcessor>();
    protected FrameBuffer out = null;

    protected final ColorRGBA backColor = new ColorRGBA(0,0,0,0);
    protected boolean clearEnabled = true;

    public ViewPort(String name, Camera cam) {
        this.name = name;
        this.cam = cam;
    }

    public String getName() {
        return name;
    }

    public List<SceneProcessor> getProcessors(){
        return processors;
    }

    public void addProcessor(SceneProcessor processor){
        processors.add(processor);
    }

    public void removeProcessor(SceneProcessor processor){
        processors.remove(processor);
        processor.cleanup();
    }

    public boolean isClearEnabled() {
        return clearEnabled;
    }

    public void setClearEnabled(boolean clearEnabled) {
        this.clearEnabled = clearEnabled;
    }

    public FrameBuffer getOutputFrameBuffer() {
        return out;
    }

    public void setOutputFrameBuffer(FrameBuffer out) {
        this.out = out;
    }

    public Camera getCamera() {
        return cam;
    }

    public RenderQueue getQueue() {
        return queue;
    }

    public void attachScene(Spatial scene){
        sceneList.add(scene);
    }

    public void detachScene(Spatial scene){
        sceneList.remove(scene);
    }

    public void clearScenes() {
        sceneList.clear();
    }

    public List<Spatial> getScenes(){
        return sceneList;
    }

    public void setBackgroundColor(ColorRGBA background){
        backColor.set(background);
    }

    public ColorRGBA getBackgroundColor(){
        return backColor;
    }

}
