/*
 * Copyright (c) 2003-2009 jMonkeyEngine
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

package com.g3d.export.binary;

import com.g3d.asset.AssetInfo;
import com.g3d.asset.AssetManager;
import com.g3d.export.G3DImporter;
import com.g3d.export.ReadListener;
import com.g3d.export.Savable;
import com.g3d.math.FastMath;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joshua Slack
 */
public final class BinaryImporter implements G3DImporter {
    private static final Logger logger = Logger.getLogger(BinaryImporter.class
            .getName());

    //TODO: Provide better cleanup and reuse of this class -- Good for now.
    private AssetManager assetManager;

    //Key - alias, object - bco
    private HashMap<String, BinaryClassObject> classes
             = new HashMap<String, BinaryClassObject>();
    //Key - id, object - the savable
    private HashMap<Integer, Savable> contentTable
            = new HashMap<Integer, Savable>();
    //Key - savable, object - capsule
    private IdentityHashMap<Savable, BinaryInputCapsule> capsuleTable
             = new IdentityHashMap<Savable, BinaryInputCapsule>();
    //Key - id, opject - location in the file
    private HashMap<Integer, Integer> locationTable
             = new HashMap<Integer, Integer>();

    public static boolean debug = false;

    private byte[] dataArray;
    private int aliasWidth;

    private static final boolean fastRead = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;

    public BinaryImporter() {
    }

    public static boolean canUseFastBuffers(){
        return fastRead;
    }

    public static BinaryImporter getInstance() {
        return new BinaryImporter();
    }

    public AssetManager getAssetManager(){
        return assetManager;
    }

    public Object load(AssetInfo info){
        assetManager = info.getManager();

        try{
            InputStream is = info.openStream();
            Savable s = load(is);
            is.close();
            return s;
        }catch (IOException ex){
            logger.log(Level.SEVERE, "An error occured while loading jME binary object", ex);
        }
        return null;
    }

    public Savable load(InputStream is) throws IOException {
        return load(is, null, null);
    }

    public Savable load(InputStream is, ReadListener listener) throws IOException {
        return load(is, listener, null);
    }

    public Savable load(InputStream is, ReadListener listener, ByteArrayOutputStream baos) throws IOException {
        contentTable.clear();
        BufferedInputStream bis = new BufferedInputStream(is);
        int numClasses = ByteUtils.readInt(bis);
        int bytes = 4;
        aliasWidth = ((int)FastMath.log(numClasses, 256) + 1);

        classes.clear();
        for(int i = 0; i < numClasses; i++) {
            String alias = readString(bis, aliasWidth);

            int classLength = ByteUtils.readInt(bis);
            String className = readString(bis, classLength);
            BinaryClassObject bco = new BinaryClassObject();
            bco.alias = alias.getBytes();
            bco.className = className;

            int fields = ByteUtils.readInt(bis);
            bytes += (8 + aliasWidth + classLength);

            bco.nameFields = new HashMap<String, BinaryClassField>(fields);
            bco.aliasFields = new HashMap<Byte, BinaryClassField>(fields);
            for (int x = 0; x < fields; x++) {
                byte fieldAlias = (byte)bis.read();
                byte fieldType = (byte)bis.read();

                int fieldNameLength = ByteUtils.readInt(bis);
                String fieldName = readString(bis, fieldNameLength);
                BinaryClassField bcf = new BinaryClassField(fieldName, fieldAlias, fieldType);
                bco.nameFields.put(fieldName, bcf);
                bco.aliasFields.put(fieldAlias, bcf);
                bytes += (6 + fieldNameLength);
            }
            classes.put(alias, bco);
        }
        if (listener != null) listener.readBytes(bytes);

        int numLocs = ByteUtils.readInt(bis);
        bytes = 4;

        capsuleTable.clear();
        locationTable.clear();
        for(int i = 0; i < numLocs; i++) {
            int id = ByteUtils.readInt(bis);
            int loc = ByteUtils.readInt(bis);
            locationTable.put(id, loc);
            bytes += 8;
        }

        @SuppressWarnings("unused")
        int numbIDs = ByteUtils.readInt(bis); // XXX: NOT CURRENTLY USED
        int id = ByteUtils.readInt(bis);
        bytes += 8;
        if (listener != null) listener.readBytes(bytes);

        if (baos == null) {
                baos = new ByteArrayOutputStream(bytes);
        } else {
                baos.reset();
        }
        int size = -1;
        byte[] cache = new byte[4096];
        while((size = bis.read(cache)) != -1) {
            baos.write(cache, 0, size);
            if (listener != null) listener.readBytes(size);
        }
        bis = null;

        dataArray = baos.toByteArray();
        baos = null;

        Savable rVal = readObject(id);
        if (debug) {
            logger.info("Importer Stats: ");
            logger.info("Tags: "+numClasses);
            logger.info("Objects: "+numLocs);
            logger.info("Data Size: "+dataArray.length);
        }
        dataArray = null;
        return rVal;
    }

    public Savable load(URL f) throws IOException {
        return load(f, null);
    }

    public Savable load(URL f, ReadListener listener) throws IOException {
        InputStream is = f.openStream();
        Savable rVal = load(is, listener);
        is.close();
        return rVal;
    }

    public Savable load(File f) throws IOException {
        return load(f, null);
    }

    public Savable load(File f, ReadListener listener) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        Savable rVal = load(fis, listener);
        fis.close();
        return rVal;
    }

    public Savable load(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Savable rVal = load(bais);
        bais.close();
        return rVal;
    }

    public BinaryInputCapsule getCapsule(Savable id) {
        return capsuleTable.get(id);
    }

    protected String readString(InputStream f, int length) throws IOException {
        byte[] data = new byte[length];
        for(int j = 0; j < length; j++) {
            data[j] = (byte)f.read();
        }

        return new String(data);
    }

    protected String readString(int length, int offset) throws IOException {
        byte[] data = new byte[length];
        for(int j = 0; j < length; j++) {
            data[j] = dataArray[j+offset];
        }

        return new String(data);
    }

    public Savable readObject(int id) {

        if(contentTable.get(id) != null) {
            return contentTable.get(id);
        }

        try {
            int loc = locationTable.get(id);

            String alias = readString(aliasWidth, loc);
            loc+=aliasWidth;

            BinaryClassObject bco = classes.get(alias);

            if(bco == null) {
                logger.logp(Level.SEVERE, this.getClass().toString(), "readObject(int id)", "NULL class object: " + alias);
                return null;
            }

            int dataLength = ByteUtils.convertIntFromBytes(dataArray, loc);
            loc+=4;

            BinaryInputCapsule cap = new BinaryInputCapsule(this, bco);
            cap.setContent(dataArray, loc, loc+dataLength);

            Savable out = BinaryClassLoader.fromName(bco.className, cap);

            capsuleTable.put(out, cap);
            contentTable.put(id, out);

            out.read(this);

            capsuleTable.remove(out);

            return out;

        } catch (IOException e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "readObject(int id)", "Exception", e);
            return null;
        } catch (ClassNotFoundException e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "readObject(int id)", "Exception", e);
            return null;
        } catch (InstantiationException e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "readObject(int id)", "Exception", e);
            return null;
        } catch (IllegalAccessException e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "readObject(int id)", "Exception", e);
            return null;
        }
    }
}