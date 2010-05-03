/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.gde.scenecomposer;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.effect.EmitterSphereShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.gde.core.scene.PreviewRequest;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.gde.core.scene.SceneListener;
import com.jme3.gde.core.scene.SceneRequest;
import com.jme3.gde.core.scene.nodes.JmeNode;
import com.jme3.gde.core.scene.nodes.JmeSpatial;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.palette.PaletteController;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 * TODO: some threading stuff
 */
@ConvertAsProperties(dtd = "-//com.jme3.gde.scenecomposer//SceneComposer//EN",
autostore = false)
public final class SceneComposerTopComponent extends TopComponent implements SceneListener, LookupListener, AppState {

    private static SceneComposerTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "com/jme3/gde/scenecomposer/jme-logo24.png";
    private static final String PREFERRED_ID = "SceneComposerTopComponent";
    private SceneRequest currentRequest;
    private FileObject currentFileObject;
    private final Result<JmeSpatial> result;
    private JmeSpatial selectedSpat;
    private Spatial selected;
    ComposerCameraController camController;
    //palette
    private PaletteController palette = null;
    private JmeNode paletteRoot;

    public SceneComposerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SceneComposerTopComponent.class, "CTL_SceneComposerTopComponent"));
        setToolTipText(NbBundle.getMessage(SceneComposerTopComponent.class, "HINT_SceneComposerTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        result = Utilities.actionsGlobalContext().lookupResult(JmeSpatial.class);
        result.addLookupListener(this);
        SceneApplication.getApplication().addSceneListener(this);
//        SceneApplication.getApplication().get
//        preparePalette();
//        associateLookup(Lookups.fixed(new Object[]{getPalette()}));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        sceneNameLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        addObjectButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.openide.awt.Mnemonics.setLocalizedText(saveButton, org.openide.util.NbBundle.getMessage(SceneComposerTopComponent.class, "SceneComposerTopComponent.saveButton.text")); // NOI18N
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(sceneNameLabel, org.openide.util.NbBundle.getMessage(SceneComposerTopComponent.class, "SceneComposerTopComponent.sceneNameLabel.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .add(saveButton))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, sceneNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(sceneNameLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 94, Short.MAX_VALUE)
                .add(saveButton))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SceneComposerTopComponent.class, "SceneComposerTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(addObjectButton, org.openide.util.NbBundle.getMessage(SceneComposerTopComponent.class, "SceneComposerTopComponent.addObjectButton.text")); // NOI18N
        addObjectButton.setEnabled(false);
        addObjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addObjectButtonActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Node", "Particle Emitter", "Audio Node", "Picture" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
            .add(jPanel2Layout.createSequentialGroup()
                .add(2, 2, 2)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(addObjectButton)
                        .addContainerGap(99, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addObjectButton))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 282, Short.MAX_VALUE)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addObjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addObjectButtonActionPerformed
        if (jList1.getSelectedValue() != null) {
            addSpatial(jList1.getSelectedValue().toString());
        }

    }//GEN-LAST:event_addObjectButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveRequest();
    }//GEN-LAST:event_saveButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addObjectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel sceneNameLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized SceneComposerTopComponent getDefault() {
        if (instance == null) {
            instance = new SceneComposerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SceneComposerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SceneComposerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(SceneComposerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SceneComposerTopComponent) {
            return (SceneComposerTopComponent) win;
        }
        Logger.getLogger(SceneComposerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    private void refreshSelected() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                if (selectedSpat != null) {
                    selectedSpat.refresh(false);
                }
            }
        });

    }
//    private Node paletteNode;
//
//    private void preparePalette() {
//        paletteNode = new Node("Palette Root");
//        //NODE
//        paletteNode.attachChild(new Node("Node"));
//        //PARTICLE EMITTER
//        ParticleEmitter emit = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 200);
//        emit.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
//        emit.setName("Particle Emitter");
//        emit.setGravity(0);
//        emit.setLowLife(5);
//        emit.setHighLife(10);
//        emit.setStartVel(new Vector3f(0, 0, 0));
//        emit.setImagesX(15);
//        Material mat = null;
//        try {
//            mat = SceneApplication.getApplication().enqueue(new Callable<Material>() {
//
//                public Material call() throws Exception {
//                    Material mat = new Material(SceneApplication.getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
//                    mat.setTexture("m_Texture", SceneApplication.getApplication().getAssetManager().loadTexture("Effects/Smoke/Smoke.png"));
//                    return mat;
//                }
//            }).get();
//        } catch (InterruptedException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (ExecutionException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        if (mat != null) {
//            emit.setMaterial(mat);
//        }
//        paletteNode.attachChild(emit);
//    }
//
//    private PaletteController getPalette() {
//        if (null == palette) {
//            paletteRoot = NodeUtility.createNode(paletteNode);
//            paletteRoot.setName("Palette Root");
//
//            palette = PaletteFactory.createPalette(paletteRoot,
//                    new MyPaletteActions(), null, new MyDragAndDropHandler());
//        }
//        return palette;
//    }
//    public static final DataFlavor MyCustomDataFlavor = new DataFlavor(ClipboardSpatial.class, "Spatial");
//
//    private static class MyDragAndDropHandler extends DragAndDropHandler {
//
//        public void customize(ExTransferable exTransferable, Lookup lookup) {
//            final Spatial item = (Spatial) lookup.lookup(Spatial.class);
//            if (null != item) {
//                exTransferable.put(new ExTransferable.Single(MyCustomDataFlavor) {
//
//                    protected Object getData() throws IOException, UnsupportedFlavorException {
//                        return new ClipboardSpatial(item);
//                    }
//                });
//            }
//        }
//    }
//
//    private static class MyPaletteActions extends PaletteActions {
//
//        public Action[] getImportActions() {
//            return null;
//        }
//
//        public Action[] getCustomPaletteActions() {
//            return null;
//        }
//
//        public Action[] getCustomCategoryActions(Lookup lookup) {
//            return null;
//        }
//
//        public Action[] getCustomItemActions(Lookup lookup) {
//            return null;
//        }
//
//        public Action getPreferredAction(Lookup lookup) {
//            return null;
//        }
//    }

    private void addSpatial(final String name) {
        if (currentRequest != null && currentRequest.isDisplayed() && selected instanceof Node) {
            try {
                SceneApplication.getApplication().enqueue(new Callable() {

                    public Object call() throws Exception {
                        if ("Node".equals(name)) {
                            ((Node) selected).attachChild(new Node("Node"));
                        } else if ("Particle Emitter".equals(name)) {
                            ParticleEmitter emit = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 200);
                            emit.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
                            emit.setGravity(0);
                            emit.setLowLife(5);
                            emit.setHighLife(10);
                            emit.setStartVel(new Vector3f(0, 0, 0));
                            emit.setImagesX(15);
                            Material mat = new Material(SceneApplication.getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
                            //                    mat.setTexture("m_Texture", SceneApplication.getApplication().getAssetManager().loadTexture("Effects/Smoke/Smoke.png"));
                            emit.setMaterial(mat);
                            ((Node) selected).attachChild(emit);
                        } else if ("Audio Node".equals(name)) {
                            AudioNode node = new AudioNode();
                            node.setName("Audio Node");
                            ((Node) selected).attachChild(node);
                        } else if ("Picture".equals(name)) {
                            Picture pic = new Picture("Picture");
                            Material mat = new Material(SceneApplication.getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
                            pic.setMaterial(mat);
                            ((Node) selected).attachChild(pic);
                        }
                        refreshSelected();
                        return null;

                    }
                }).get();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /*
     * methods for external access
     */
    public void addModel(final AssetManager manager, final String assetName) {

        if (currentRequest != null && currentRequest.isDisplayed() && selected != null && selected instanceof Node) {
            SceneApplication.getApplication().enqueue(new Callable() {

                public Object call() throws Exception {
                    ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Importing Model..");
                    progressHandle.start();
                    ((DesktopAssetManager) manager).clearCache();
                    Spatial spat = manager.loadModel(assetName);
                    ((Node) selected).attachChild(spat);
                    refreshSelected();
                    progressHandle.finish();
                    return null;
                }
            });
        }
    }

    public void loadRequest(SceneRequest request, FileObject file) {
        //TODO: handle request change
        this.currentRequest = request;
        this.currentFileObject = file;
        request.setWindowTitle("SceneComposer - " + request.getRootNode().getName());
        SceneApplication.getApplication().requestScene(request);
    }

    public void saveRequest() {
        if (currentFileObject != null && currentRequest != null && currentRequest.isDisplayed()) {
            SceneApplication.getApplication().enqueue(new Callable() {

                public Object call() throws Exception {
                    ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Saving File..");
                    progressHandle.start();
                    Node node = currentRequest.getRootNode().getLookup().lookup(Node.class);
                    BinaryExporter exp = BinaryExporter.getInstance();
                    try {
                        exp.save(node, FileUtil.toFile(currentFileObject));
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    progressHandle.finish();
                    StatusDisplayer.getDefault().setStatusText("Saved file " + currentFileObject.getNameExt());
                    //try make NetBeans update the tree.. :/
                    return null;
                }
            });
        }
    }

    /**
     * method to set the state of the ui items
     */
    private void setLoadedState(final String name, final boolean active) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                sceneNameLabel.setText(name);
                if (!active) {
                    saveButton.setEnabled(false);
                    addObjectButton.setEnabled(false);
                    close();
                } else {
                    saveButton.setEnabled(true);
                    addObjectButton.setEnabled(true);
                    open();
                    requestActive();
                }
            }
        });
    }

    /**
     * listener for node selection changes
     */
    public void resultChanged(LookupEvent ev) {
        Collection<JmeSpatial> items = (Collection<JmeSpatial>) result.allInstances();
        for (JmeSpatial spatial : items) {
            selectedSpat = spatial;
            selected = spatial.getLookup().lookup(Spatial.class);
        }
    }

    /*
     * SceneListener
     */
    public void sceneRequested(SceneRequest request) {
        if (request.equals(currentRequest)) {
            setLoadedState(currentRequest.getRootNode().getName(), true);
            if (camController != null) {
                SceneApplication.getApplication().getInputManager().removeRawInputListener(camController);
                SceneApplication.getApplication().getInputManager().removeBindingListener(camController);
            }
            camController = new ComposerCameraController(SceneApplication.getApplication().getCamera(), request.getRootNode().getLookup().lookup(Node.class));
            SceneApplication.getApplication().getInputManager().addRawInputListener(camController);
            SceneApplication.getApplication().getInputManager().addBindingListener(camController);
        } else {
            if (camController != null) {
                SceneApplication.getApplication().getInputManager().removeRawInputListener(camController);
                SceneApplication.getApplication().getInputManager().removeBindingListener(camController);
                camController = null;
            }
            setLoadedState("no scene loaded", false);
        }
    }

    public void nodeSelected(JmeSpatial spatial) {
    }

    public void previewRequested(PreviewRequest request) {
    }

    /*
     * AppState
     */
    public void initialize(AppStateManager asm, Application aplctn) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isInitialized() {
        return true;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stateAttached(AppStateManager asm) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stateDetached(AppStateManager asm) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(float f) {
    }

    public void render(RenderManager rm) {
//        if (camController != null) {
//            Geometry geom = camController.checkClick();
//            if (geom != null) {
//                StatusDisplayer.getDefault().setStatusText("Clicked Geometry: " + geom.toString());
//            }
//        }
    }

    public void cleanup() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
