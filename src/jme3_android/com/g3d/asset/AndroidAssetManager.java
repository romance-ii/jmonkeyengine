package com.g3d.asset;

import com.g3d.asset.plugins.AndroidLocator;
import com.g3d.export.binary.BinaryExporter;
import com.g3d.export.binary.BinaryImporter;
import com.g3d.font.BitmapFont;
import com.g3d.font.plugins.BitmapFontLoader;
import com.g3d.material.Material;
import com.g3d.material.plugins.J3MLoader;
import com.g3d.scene.Spatial;
import com.g3d.shader.Shader;
import com.g3d.shader.ShaderKey;
import com.g3d.texture.Image;
import com.g3d.texture.Texture;
import com.g3d.texture.plugins.AndroidImageLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AssetManager for Android
 *
 * @author Kirill Vainer
 */
public final class AndroidAssetManager implements AssetManager {

    private static final Logger logger = Logger.getLogger(AssetManager.class.getName());

    private final AndroidLocator locator = new AndroidLocator();
    private final AndroidImageLoader imageLoader = new AndroidImageLoader();
    private final BinaryImporter modelLoader = new BinaryImporter();
    private final BitmapFontLoader fontLoader = new BitmapFontLoader();
    private final J3MLoader j3mLoader = new J3MLoader();
    private final J3MLoader j3mdLoader = new J3MLoader();

    private final BinaryExporter exporter = new BinaryExporter();
    private final HashMap<AssetKey, Object> cache = new HashMap<AssetKey, Object>();

    public AndroidAssetManager(){
        this(false);
    }

    public AndroidAssetManager(boolean loadDefaults){
        if (loadDefaults){
//            AssetConfig cfg = new AssetConfig(this);
//            InputStream stream = AssetManager.class.getResourceAsStream("Desktop.cfg");
//            try{
//                cfg.loadText(stream);
//            }catch (IOException ex){
//                logger.log(Level.SEVERE, "Failed to load asset config", ex);
//            }finally{
//                if (stream != null)
//                    try{
//                        stream.close();
//                    }catch (IOException ex){
//                    }
//            }
        }
        logger.info("AndroidAssetManager created.");
    }

    public void registerLoader(String loaderClass, String ... extensions){
    }

    public void registerLocator(String rootPath, String locatorClass, String ... extensions){
    }

    private Object tryLoadFromHD(AssetKey key){
        if (!key.getExtension().equals("fnt"))
            return null;

        File f = new File("/sdcard/" + key.getName() + ".opt");
        if (!f.exists())
            return null;

        try {
            InputStream stream = new FileInputStream(f);
            BitmapFont font = (BitmapFont) modelLoader.load(stream, null, null);
            stream.close();
            return font;
        } catch (IOException ex){
        }

        return null;
    }

    private void tryPutToHD(AssetKey key, Object data){
        if (!key.getExtension().equals("fnt"))
            return;

        File f = new File("/sdcard/" + key.getName() + ".opt");

        try {
            BitmapFont font = (BitmapFont) data;
            OutputStream stream = new FileOutputStream(f);
            exporter.save(font, stream);
            stream.close();
        } catch (IOException ex){
        }
    }

    public Object loadContent(AssetKey key){
        Object asset;
//        Object asset = tryLoadFromHD(key);
//        if (asset != null)
//            return asset;

        if (key.shouldCache()){
            asset = cache.get(key);
            if (asset != null)
                return asset;
        }
        // find resource
        AssetInfo info = locator.locate(this, key);
        if (info == null){
            logger.log(Level.WARNING, "Cannot locate resource: "+key.getName());
            return null;
        }

        String ex = key.getExtension();
        logger.log(Level.INFO, "Loading asset: "+key.getName());
        try{
            if (ex.equals("png") || ex.equals("jpg") 
             || ex.equals("jpeg") || ex.equals("j3i")){
                Image image;
                if (ex.equals("j3i")){
                    image = (Image) modelLoader.load(info);
                }else{
                    image = (Image) imageLoader.load(info);
                }
                TextureKey tkey = (TextureKey) key;
                asset = image;
                Texture tex = (Texture) tkey.postProcess(asset);
                tex.setMagFilter(Texture.MagFilter.Nearest);
                tex.setAnisotropicFilter(0);
                if (tex.getMinFilter().usesMipMapLevels()){
                    tex.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
                }else{
                    tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                }
                asset = tex;
            }else if (ex.equals("j3o")){
                asset = modelLoader.load(info);
            }else if (ex.equals("fnt")){
                asset = fontLoader.load(info);
            }else if (ex.equals("j3md")){
                asset = j3mdLoader.load(info);
            }else if (ex.equals("j3m")){
                asset = j3mLoader.load(info);
            }else{
                logger.log(Level.WARNING, "No loader registered for type: "+ex);
                return null;
            }

            if (key.shouldCache())
                cache.put(key, asset);

//            tryPutToHD(key, asset);

            return asset;
        } catch (Exception e){
            logger.log(Level.WARNING, "Failed to load resource: "+key.getName(), e);
        }
        return null;
    }

    public Object loadContent(String name) {
        return loadContent(new AssetKey(name));
    }

    public Spatial loadModel(String name) {
        return (Spatial) loadContent(name);
    }

    public Material loadMaterial(String name) {
        return (Material) loadContent(name);
    }

    public BitmapFont loadFont(String name){
        return (BitmapFont) loadContent(name);
    }

    public Texture loadTexture(TextureKey key){
        return (Texture) loadContent(key);
    }

    public Texture loadTexture(String name){
        return loadTexture(new TextureKey(name, false));
    }

    public Shader loadShader(ShaderKey key){
        return null;
    }

}
