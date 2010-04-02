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
package com.jme3.gde.core.sceneviewer;

import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.system.JmeCanvasContext;
import com.jme3.system.SystemListener;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//com.jme3.gde.core.sceneviewer//SceneViewer//EN",
autostore = false)
public final class SceneViewerTopComponent extends TopComponent implements SystemListener{

    private static SceneViewerTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "com/jme3/gde/core/sceneviewer/jme-logo.png";
    private static final String PREFERRED_ID = "SceneViewerTopComponent";

    private JmeCanvasContext ctx;
    private SceneApplication app;

    public SceneViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SceneViewerTopComponent.class, "CTL_SceneViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(SceneViewerTopComponent.class, "HINT_SceneViewerTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
//        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
//        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);
//        app=Lookup.getDefault().lookup(SceneViewerApplication.class);
        app=SceneApplication.getApplication();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        enableCamLight = new javax.swing.JToggleButton();
        oGLPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        enableCamLight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jme3/gde/core/sceneviewer/icons/lightbulb.gif"))); // NOI18N
        enableCamLight.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(enableCamLight, org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableCamLight.text")); // NOI18N
        enableCamLight.setToolTipText(org.openide.util.NbBundle.getMessage(SceneViewerTopComponent.class, "SceneViewerTopComponent.enableCamLight.toolTipText")); // NOI18N
        enableCamLight.setFocusable(false);
        enableCamLight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        enableCamLight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        enableCamLight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableCamLightActionPerformed(evt);
            }
        });
        jToolBar1.add(enableCamLight);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        oGLPanel.setPreferredSize(new java.awt.Dimension(100, 100));
        oGLPanel.setLayout(new java.awt.GridLayout(1, 0));
        add(oGLPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void enableCamLightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableCamLightActionPerformed
        app.enableCamLight(enableCamLight.isSelected());
    }//GEN-LAST:event_enableCamLightActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton enableCamLight;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel oGLPanel;
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized SceneViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new SceneViewerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SceneViewerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SceneViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(SceneViewerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SceneViewerTopComponent) {
            return (SceneViewerTopComponent) win;
        }
        Logger.getLogger(SceneViewerTopComponent.class.getName()).warning(
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
        super.componentOpened();
        oGLPanel.add(((JmeCanvasContext)app.getContext()).getCanvas());
    }

    @Override
    protected void componentShowing() {
        super.componentShowing();
    }

    @Override
    protected void componentHidden() {
        super.componentHidden();
    }


    @Override
    public void componentClosed() {
        super.componentClosed();
        oGLPanel.removeAll();
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

    /*
     * SystemListener
     */

    public void initialize() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reshape(int i, int i1) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void requestClose(boolean bln) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void gainFocus() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void loseFocus() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void handleError(String string, Throwable thrwbl) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void destroy() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
