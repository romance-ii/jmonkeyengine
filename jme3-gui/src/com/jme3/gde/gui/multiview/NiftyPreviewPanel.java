/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.gui.multiview;

import com.jme3.gde.gui.NiftyGuiDataObject;
import com.jme3.gde.gui.renderer.Java2dRenderDevice;
import com.jme3.gde.gui.renderer.Java2dSoundDevice;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.TimeProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.xml.multiview.Error;
import org.netbeans.modules.xml.multiview.ui.PanelView;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author normenhansen
 */
public class NiftyPreviewPanel extends PanelView{
    private Nifty nifty;
    private Java2dRenderDevice dev;
    private NiftyGuiDataObject niftyObject;
    private FileObject file=null;
    private Thread thread;

    public NiftyPreviewPanel(NiftyGuiDataObject niftyObject) {
        super();
        this.niftyObject=niftyObject;
        setRoot(Node.EMPTY);
    }

    public void initComponents() {
        super.initComponents();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        dev=new Java2dRenderDevice();
        add(dev);
        String name="tutorial/tutorial.xml";
        if(niftyObject!=null){
            Set<FileObject> files = niftyObject.files();
            for (Iterator<FileObject> it = files.iterator(); it.hasNext();) {
                FileObject fileObject = it.next();
                file=fileObject;//getNameExt();
            }
            name=file.getPath();
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Could not find niftyObject");
        }
        nifty = new Nifty(dev,
                    new SoundSystem(new Java2dSoundDevice()),
                    dev.getInputSystem(),
                    new TimeProvider());
        nifty.fromXml(name,"start");//
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "nify started");
    }

    public void start(){
        if(running) stop();
        thread=new Thread(run);
        thread.start();
    }

    public void stop(){
        running=false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean running=false;
    private Runnable run=new Runnable() {
        public void run() {
            running=true;
            while(running){
                if(nifty.render(true)){
                    running=false;
                }
            }
        }
    };

    @Override
    protected Error validateView() {
        return null;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showSelection(Node[] nodes) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
