package com.jme3.system;

import com.jme3.system.JmeSystem.Platform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for extracting the natives (dll, so) from the jars.
 * This class should only be used internally.
 */
class Natives {

    private static final Logger logger = Logger.getLogger(Natives.class.getName());
    private static final byte[] buf = new byte[1024];

    public static void extractNativeLib(String sysName, String name, boolean load) throws IOException{
        String fullname = System.mapLibraryName(name);

        InputStream in = Natives.class.getResourceAsStream("/native/"+sysName+"/" + fullname);
        if (in == null) {
            logger.log(Level.WARNING, "Cannot locate native library: {0}/{1}", 
                    new String[]{ sysName, fullname} );
            return;
        }
        File targetFile = new File(fullname);
        OutputStream out = new FileOutputStream(targetFile);
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        if (load){
            System.loadLibrary(name);
        }

        logger.log(Level.FINE, "Copied {0} to {1}", new Object[]{fullname, targetFile});
    }

    public static void extractNativeLibs(Platform platform, AppSettings settings) throws IOException{
        String renderer = settings.getRenderer();
        String audioRenderer = settings.getAudioRenderer();
        boolean needLWJGL = false;
        boolean needGG = false;
        boolean needJOGL = false;
        boolean needJOAL = false;
        boolean needOAL = false;
        boolean needJInput = false;
        if (renderer != null){
            if (renderer.startsWith("LWJGL")){
                needLWJGL = true;
            }else if (renderer.startsWith("JOGL")){
                needGG = true;
                needJOGL = true;
            }
        }
        if (audioRenderer != null){
            if (audioRenderer.equals("LWJGL")){
                needLWJGL = true;
                needOAL = true;
            }else if (audioRenderer.startsWith("JOAL")){
                needJOAL = true;
                needGG = true;
                needOAL = true;
            }
        }
        needJInput = settings.useJoysticks();

        switch (platform){
            case Windows64:
                if (needLWJGL)
                    extractNativeLib("windows", "lwjgl64", true);
                else if (needJOGL){
                    extractNativeLib("win64", "jogl_awt", false);
                    extractNativeLib("win64", "jogl", true);
                }

                if (needJOAL)
                    extractNativeLib("win64", "joal_native", true);

                if (needGG)
                    extractNativeLib("win64", "gluegen-rt", false);

                if (needOAL)
                    extractNativeLib("windows", "OpenAL64", true);
                
                if (needJInput){
                    extractNativeLib("windows", "jinput-dx8_64", true);
                    extractNativeLib("windows", "jinput-raw_64", true);
                }
                break;
            case Windows32:
                if (needLWJGL)
                    extractNativeLib("windows", "lwjgl", true);
                else if (needJOGL){
                    extractNativeLib("win32", "jogl_awt", false);
                    extractNativeLib("win32", "jogl", true);
                }

                if (needJOAL)
                    extractNativeLib("win32", "joal_native", true);

                if (needGG)
                    extractNativeLib("win32", "gluegen-rt", false);

                if (needOAL)
                    extractNativeLib("windows", "OpenAL32", true);

                if (needJInput){
                    extractNativeLib("windows", "jinput-dx8", true);
                    extractNativeLib("windows", "jinput-raw", true);
                }
                break;
            case Linux64:
                if (needLWJGL)
                    extractNativeLib("linux", "lwjgl64", true);
                else if (needJOGL){
                    extractNativeLib("linux64", "jogl_awt", false);
                    extractNativeLib("linux64", "jogl", true);
                }

                if (needJOAL)
                    extractNativeLib("linux64", "joal_native", true);

                if (needGG)
                    extractNativeLib("linux64", "gluegen-rt", false);

                if (needJInput)
                    extractNativeLib("linux", "jinput-linux64", true);

                break;
            case Linux32:
                if (needLWJGL)
                    extractNativeLib("linux", "lwjgl", true);
                else if (needJOGL){
                    extractNativeLib("linux32", "jogl_awt", false);
                    extractNativeLib("linux32", "jogl", true);
                }

                if (needJOAL)
                    extractNativeLib("linux32", "joal_native", true);

                if (needGG)
                    extractNativeLib("linux32", "gluegen-rt", false);

                if (needJInput)
                    extractNativeLib("linux", "jinput-linux", true);

                break;
            case MacOSX_PPC32:
                if (needLWJGL)
                    extractNativeLib("macosx", "lwjgl", true);
                else if (needJOGL){
                    extractNativeLib("macosx_ppc", "jogl_awt", false);
                    extractNativeLib("macosx_ppc", "jogl", true);
                }

                if (needJOAL)
                    throw new UnsupportedOperationException("JOAL not available on Mac OS PPC");

                if (needGG)
                    extractNativeLib("macosx_ppc", "gluegen-rt", false);

                if (needOAL)
                    extractNativeLib("macosx", "openal", true);

                if (needJInput)
                    extractNativeLib("macosx", "jinput-osx", true);

                break;
            case MacOSX32:
                if (needLWJGL)
                    extractNativeLib("macosx", "lwjgl", true);
                else if (needJOGL){
                    extractNativeLib("macosx_universal", "jogl_awt", false);
                    extractNativeLib("macosx_universal", "jogl", true);
                }

                if (needJOAL)
                    extractNativeLib("macosx_universal", "joal_native", true);

                if (needGG)
                    extractNativeLib("macosx_universal", "gluegen-rt", false);

                if (needOAL)
                    extractNativeLib("macosx", "openal", true);

                if (needJInput)
                    extractNativeLib("macosx", "jinput-osx", true);

                break;
            case MacOSX_PPC64:
                if (needLWJGL)
                    extractNativeLib("macosx", "lwjgl", true);
                else if (needJOGL){
                    extractNativeLib("macosx_ppc", "jogl_awt", false);
                    extractNativeLib("macosx_ppc", "jogl", true);
                }

                if (needJOAL)
                    throw new UnsupportedOperationException("JOAL not available on Mac OS 64 bit");

                if (needGG)
                    extractNativeLib("macosx_ppc", "gluegen-rt", false);

                if (needOAL)
                    extractNativeLib("macosx", "openal", true);

                if (needJInput)
                    extractNativeLib("macosx", "jinput-osx", true);

                break;
            case MacOSX64:
                if (needLWJGL)
                    extractNativeLib("macosx", "lwjgl", true);
                else if (needJOGL){
                    extractNativeLib("macosx_universal", "jogl_awt", false);
                    extractNativeLib("macosx_universal", "jogl", true);
                }

                if (needJOAL)
                    throw new UnsupportedOperationException("JOAL not available on Mac OS 64 bit");

                if (needGG)
                    extractNativeLib("macosx_universal", "gluegen-rt", false);

                if (needOAL)
                    extractNativeLib("macosx", "openal", true);

                if (needJInput)
                    extractNativeLib("macosx", "jinput-osx", true);

                break;
            
                
        }

//            System.setProperty("org.lwjgl.librarypath", workingDir.toString());
    }

}
