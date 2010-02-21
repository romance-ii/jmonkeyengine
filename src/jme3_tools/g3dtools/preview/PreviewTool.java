package g3dtools.preview;

import com.g3d.animation.Model;
import com.g3d.bounding.BoundingBox;
import com.g3d.bounding.BoundingVolume;
import com.g3d.export.binary.BinaryExporter;
import com.g3d.export.xml.XMLExporter;
import com.g3d.math.Vector3f;
import com.g3d.scene.Spatial;
import com.g3d.scene.plugins.ogre.MeshLoader;
import com.g3d.system.AppSettings;
import com.g3d.system.G3DCanvasContext;
import g3dtools.converters.model.ModelConverter;
import java.awt.Canvas;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class PreviewTool extends javax.swing.JFrame {

    private G3DCanvasContext cx;
    private Canvas glCanvas;
//    private AssetManager assetManager;
    private PreviewDisplay display;

//    private JmeContext cx;
//    private LWJGLCanvas glCanvas;
//    private ModelCameraHandler handler;
    
//    private SpecialStateRenderPass render;
    
    private IAnimationHandler animHandler;
    
    private JFileChooser chooser;
    private FileNameExtensionFilter jme = new FileNameExtensionFilter("jMonkey Engine 3.0 binary model (*.j3o)","j3o");
    private FileNameExtensionFilter jmexml = new FileNameExtensionFilter("jMonkey Engine 3.0 XML model (*.xml)","xml");
    private FileNameExtensionFilter obj = new FileNameExtensionFilter("LightWave 3D Object (*.obj)","obj");
    private FileNameExtensionFilter meshxml = new FileNameExtensionFilter("Ogre3D Mesh XML (*.xml, *.meshxml)", "xml", "meshxml");
    private FileNameExtensionFilter scene = new FileNameExtensionFilter("Ogre3D dotScene (*.scene)", "scene");
    private FileNameExtensionFilter import_combo = new FileNameExtensionFilter("All supported formats (*.j3o, *.xml, *.obj, *.meshxml, *.scene)",
                                                                       "j3o", "obj", "meshxml", "xml", "scene");
    
    
    public PreviewTool() {
        //Logger.getLogger("").setLevel(Level.WARNING);
        chooser = new JFileChooser();
        
        // create canvas
        AppSettings settings = new AppSettings(true);
        settings.setSamples(4);
        display = new PreviewDisplay();
        display.setSettings(settings);
        display.createCanvas();
        cx = (G3DCanvasContext) display.getContext();
        glCanvas = cx.getCanvas();

        // set canvas on component
        initComponents();
        setLocationRelativeTo(null);

        // start canvas
        display.startCanvas();

        // show form
        setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        shadingMode = new javax.swing.ButtonGroup();
        pnlSplit = new javax.swing.JSplitPane();
        canvas = glCanvas;
        pnlButtons = new javax.swing.JPanel();
        pnlOptions = new javax.swing.JPanel();
        chkNormals = new javax.swing.JCheckBox();
        chkBounds = new javax.swing.JCheckBox();
        chkConvert = new javax.swing.JCheckBox();
        chkBones = new javax.swing.JCheckBox();
        chkBackfaces = new javax.swing.JCheckBox();
        btnWire = new javax.swing.JToggleButton();
        btnSolid = new javax.swing.JToggleButton();
        btnTextured = new javax.swing.JToggleButton();
        btnMaterial = new javax.swing.JToggleButton();
        btnLoad = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sldSpeed = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstAnims = new javax.swing.JList();
        chkAndroid = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PreviewTool");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlSplit.setDividerLocation(500);
        pnlSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        pnlSplit.setResizeWeight(0.9);
        pnlSplit.setContinuousLayout(true);
        pnlSplit.setLeftComponent(canvas);

        pnlButtons.setMinimumSize(pnlButtons.getSize());

        pnlOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("View Options"));

        chkNormals.setText("Display normals");
        chkNormals.setToolTipText("Show normals");
        chkNormals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNormalsActionPerformed(evt);
            }
        });

        chkBounds.setText("Display bounds");
        chkBounds.setToolTipText("Show bounding volumes");
        chkBounds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoundsActionPerformed(evt);
            }
        });

        chkConvert.setText("CoordSystem CAD to OGL");
        chkConvert.setToolTipText("Convert between Z-up to Y-up");
        chkConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConvertActionPerformed(evt);
            }
        });

        chkBones.setText("Display bones");
        chkBones.setToolTipText("Show bones for animated models");
        chkBones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBonesActionPerformed(evt);
            }
        });

        chkBackfaces.setText("Display backfaces");
        chkBackfaces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBackfacesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pnlOptionsLayout = new org.jdesktop.layout.GroupLayout(pnlOptions);
        pnlOptions.setLayout(pnlOptionsLayout);
        pnlOptionsLayout.setHorizontalGroup(
            pnlOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlOptionsLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(pnlOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkNormals)
                    .add(chkBounds)
                    .add(chkConvert)
                    .add(chkBones)
                    .add(chkBackfaces)))
        );
        pnlOptionsLayout.setVerticalGroup(
            pnlOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlOptionsLayout.createSequentialGroup()
                .add(chkNormals)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkBounds)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkConvert)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBones)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkBackfaces))
        );

        shadingMode.add(btnWire);
        btnWire.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/wire.gif"))); // NOI18N
        btnWire.setToolTipText("Render in wireframe");
        btnWire.setPreferredSize(new java.awt.Dimension(48, 48));
        btnWire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWireActionPerformed(evt);
            }
        });

        shadingMode.add(btnSolid);
        btnSolid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/solid.gif"))); // NOI18N
        btnSolid.setToolTipText("Render with lighting only");
        btnSolid.setPreferredSize(new java.awt.Dimension(48, 48));
        btnSolid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSolidActionPerformed(evt);
            }
        });

        shadingMode.add(btnTextured);
        btnTextured.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/textured.gif"))); // NOI18N
        btnTextured.setToolTipText("Render with texture only");
        btnTextured.setPreferredSize(new java.awt.Dimension(48, 48));
        btnTextured.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTexturedActionPerformed(evt);
            }
        });

        shadingMode.add(btnMaterial);
        btnMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/material.gif"))); // NOI18N
        btnMaterial.setSelected(true);
        btnMaterial.setToolTipText("Render with all materials applied");
        btnMaterial.setPreferredSize(new java.awt.Dimension(48, 48));
        btnMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaterialActionPerformed(evt);
            }
        });

        btnLoad.setText("Load Model..");
        btnLoad.setToolTipText("Load a model from a file");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnExport.setText("Export ..");
        btnExport.setToolTipText("Load a model from a file");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Animation"));

        jLabel1.setText("Speed: ");

        sldSpeed.setMaximum(200);
        sldSpeed.setValue(100);
        sldSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldSpeedStateChanged(evt);
            }
        });

        lstAnims.setModel(new DefaultListModel());
        lstAnims.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstAnimsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstAnims);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(4, 4, 4)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sldSpeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(sldSpeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addContainerGap())
        );

        chkAndroid.setText("Export for Android");

        org.jdesktop.layout.GroupLayout pnlButtonsLayout = new org.jdesktop.layout.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(btnWire, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnSolid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnTextured, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnMaterial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(pnlButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnExport)
                    .add(btnLoad)
                    .add(chkAndroid))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(pnlOptions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(12, 12, 12)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, btnMaterial, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, btnTextured, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, btnSolid, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, btnWire, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(pnlButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(pnlButtonsLayout.createSequentialGroup()
                            .add(btnLoad)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnExport)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                            .add(chkAndroid))
                        .add(pnlOptions, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(105, Short.MAX_VALUE))
        );

        pnlSplit.setRightComponent(pnlButtons);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlSplit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlSplit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void updateShadingMode(){
        if (btnWire.isSelected()){
            display.setMode(PreviewDisplay.MODE_WIREFRAME);
        }else if (btnSolid.isSelected()){
            display.setMode(PreviewDisplay.MODE_SOLID);
        }else if (btnTextured.isSelected()){
            display.setMode(PreviewDisplay.MODE_TEXTURED);
        }else if (btnMaterial.isSelected()){
            display.setMode(PreviewDisplay.MODE_MATERIAL);
        }
    }
    
    private void updateAnimationMode(Spatial model){
        DefaultListModel listModel = (DefaultListModel)(lstAnims.getModel());
        listModel.clear();

        if (model instanceof Model){
            Model mdl = (Model) model;
            animHandler = new ModelAnimHandler(mdl);
            animHandler.setApp(display);

            listModel.addElement("<bind>");
            for (String anim : animHandler.list()){
                listModel.addElement(anim);
            }
        }
    }
    
    private void btnWireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWireActionPerformed
        updateShadingMode();
    }//GEN-LAST:event_btnWireActionPerformed

    private void btnSolidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSolidActionPerformed
        updateShadingMode();
    }//GEN-LAST:event_btnSolidActionPerformed

    private void btnTexturedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTexturedActionPerformed
        updateShadingMode();
    }//GEN-LAST:event_btnTexturedActionPerformed

    private void btnMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaterialActionPerformed
        updateShadingMode();
    }//GEN-LAST:event_btnMaterialActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
//        display.stop();
    }//GEN-LAST:event_formWindowClosed

    private static Spatial scaleAndCenter(Spatial model, float size) {
        if (model != null) {
           model.updateGeometricState();

            BoundingVolume worldBound = model.getWorldBound();
            if (worldBound == null) {
                model.setModelBound(new BoundingBox());
                model.updateModelBound();
                model.updateGeometricState();
                worldBound = model.getWorldBound();
            }

            if (worldBound != null){ // check not still null (no geoms)
                Vector3f center = worldBound.getCenter();

                BoundingBox boundingBox = new BoundingBox(center, 0, 0, 0);
                boundingBox.mergeLocal(worldBound);

                Vector3f extent = boundingBox.getExtent( null );
                float maxExtent = Math.max( Math.max( extent.x, extent.y ), extent.z );
                float height = extent.y;
                if ( maxExtent != 0 ) {
                    model.setLocalScale( size / maxExtent );
                    Vector3f pos = center.negate().addLocal(0.0f, height / 2.0f, 0.0f); //.multLocal(model.getLocalScale().x);
                    model.setLocalTranslation(pos);
                    System.out.println("Model size: "+maxExtent);
                    System.out.println("Model position: "+center);
                }
            }
        }
        return model;
    }

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(jme);
        chooser.addChoosableFileFilter(jmexml);
        chooser.addChoosableFileFilter(obj);
        chooser.addChoosableFileFilter(meshxml);
        chooser.addChoosableFileFilter(scene);
        chooser.addChoosableFileFilter(import_combo);
        chooser.setFileFilter(import_combo);
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File selected = chooser.getSelectedFile();
            if (selected.exists()){
                String ogreMaterial = null;
                String lowerCaseName = selected.getName().toLowerCase();
                int i = lowerCaseName.lastIndexOf('.');
                if (i > 0){
                    String ext = lowerCaseName.substring(i+1);
                    if (ext.equals("xml") || ext.equals("meshxml")){
                        if (lowerCaseName.endsWith("mesh.xml") 
                         || lowerCaseName.endsWith("meshxml")){
                            ext = "mesh.xml";
                            ogreMaterial = lowerCaseName.substring(0, i) + ".material";
                        }
                    }
                    
                    try{
                        display.getAssetManager().registerLocator(selected.getParent(),
                                                                  "com.g3d.asset.plugins.FileSystemLocator",
                                                                  "*");

                        Spatial model;
                        if (ogreMaterial != null){
                            model = MeshLoader.loadModel(display.getAssetManager(), selected.getName(),
                                                                            ogreMaterial);
                        }else{
                            model = display.getAssetManager().loadModel(selected.getName());
                        }
                        if (model == null)
                            return;

//                        model = model.clone();
                        
                        // call this before scaling so access to the controllers is available
                        updateAnimationMode(model);
                        scaleAndCenter(model, 40f);
                        
//                        model = ModelLoader.scaleAndCenter(model, 40.0f);

                        display.setModel(model);
                    } catch (Throwable ex){
                        JOptionPane.showMessageDialog(this, 
                                                      "Failed to load model.\nReason: "+ex.toString(), 
                                                      "Error", 
                                                      JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void chkBonesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBonesActionPerformed
//        render.setBones(chkBones.isSelected());
    }//GEN-LAST:event_chkBonesActionPerformed

    private void chkBoundsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoundsActionPerformed
//        render.setBounds(chkBounds.isSelected());
    }//GEN-LAST:event_chkBoundsActionPerformed

    private void chkNormalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNormalsActionPerformed
//        render.setNormals(chkNormals.isSelected());
    }//GEN-LAST:event_chkNormalsActionPerformed

    private void chkBackfacesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBackfacesActionPerformed
//        backFaces.setEnabled(!chkBackfaces.isSelected());
//        rootNode.updateRenderState();
    }//GEN-LAST:event_chkBackfacesActionPerformed

    private void chkConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConvertActionPerformed
//        if (chkConvert.isSelected()){
//            Quaternion q = new Quaternion();
//            q.fromAngles(-FastMath.HALF_PI, FastMath.PI, 0);
//            rootNode.setLocalRotation(q);
//        }else{
//            rootNode.setLocalRotation(new Quaternion());
//        }
    }//GEN-LAST:event_chkConvertActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(jme);
        chooser.addChoosableFileFilter(jmexml);
        chooser.addChoosableFileFilter(obj);
        chooser.setFileFilter(jme);
        
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            String ext = "j3o";
            if (chooser.getFileFilter() == jmexml) {
                ext = "xml";
            } else if (chooser.getFileFilter() == obj) {
                ext = "obj";
            }

            if (!selected.getName().toLowerCase().endsWith(ext))
                selected = new File(selected.toString() + "." + ext);
            
            try {
                if (display.getModel() == null) {
                    return;
                }

                Spatial s = display.getModel();
                if (chkAndroid.isSelected()){
                    s = s.deepClone();
                    ModelConverter.optimize(s);
                }

                if (ext.equals("j3o")){
                    BinaryExporter exp = BinaryExporter.getInstance();
                    exp.save(s, selected);
                }else if (ext.equals("xml")){
                    XMLExporter.getInstance().save(s, selected);
                }
                //ModelLoader.saveModel(model, selected, ext);
            } catch (Throwable ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to save model.\nReason: " + ex.toString(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
}//GEN-LAST:event_btnExportActionPerformed

    private void lstAnimsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstAnimsValueChanged
        if (animHandler != null){
            String selected = (String) lstAnims.getSelectedValue();
            if (selected == null)
                return;

            if (selected.equals("<bind>")){
                // reset to bind pose
                animHandler.play("<bind>");
            }else if (animHandler.getLength(selected) > 0){
                // play anim if its defined
                animHandler.play(selected);
            }
        }
    }//GEN-LAST:event_lstAnimsValueChanged

    private void sldSpeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldSpeedStateChanged
        if (animHandler != null){
            animHandler.setSpeed(sldSpeed.getValue() / 100f);
        }
    }//GEN-LAST:event_sldSpeedStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        display.stop();
    }//GEN-LAST:event_formWindowClosing
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PreviewTool();
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnLoad;
    private javax.swing.JToggleButton btnMaterial;
    private javax.swing.JToggleButton btnSolid;
    private javax.swing.JToggleButton btnTextured;
    private javax.swing.JToggleButton btnWire;
    private java.awt.Canvas canvas;
    private javax.swing.JCheckBox chkAndroid;
    private javax.swing.JCheckBox chkBackfaces;
    private javax.swing.JCheckBox chkBones;
    private javax.swing.JCheckBox chkBounds;
    private javax.swing.JCheckBox chkConvert;
    private javax.swing.JCheckBox chkNormals;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstAnims;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JSplitPane pnlSplit;
    private javax.swing.ButtonGroup shadingMode;
    private javax.swing.JSlider sldSpeed;
    // End of variables declaration//GEN-END:variables
    
}
