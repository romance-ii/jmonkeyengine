/*
 * Copyright (c) 2003-2006 jMonkeyEngine
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

package com.jmex.model.XMLparser.Converters.TDSChunkingFiles;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.system.DisplaySystem;

import java.io.DataInput;
import java.io.IOException;

/**
 * Started Date: Jul 2, 2004<br><br>
 *
 *
 * type == afff == MAT_BLOCK<br>
 * parent == 3d3d == EDIT_3DS<br>
 *
 * @author Jack Lindamood
 */
class MaterialBlock extends ChunkerClass {

    String name;
    MaterialState myMatState;
    TextureState myTexState;
    WireframeState myWireState;


    public MaterialBlock(DataInput myIn, ChunkHeader i) throws IOException {
        super (myIn,i);
    }

    protected void initializeVariables(){
        myMatState=DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        myMatState.setEnabled(false);
        myWireState=DisplaySystem.getDisplaySystem().getRenderer().createWireframeState();
        myWireState.setEnabled(false);
        myTexState=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        myTexState.setEnabled(false);
    }

    protected boolean processChildChunk(ChunkHeader i) throws IOException {
        switch (i.type){
            case MAT_NAME:
                readMatName();
                return true;

            case MAT_AMB_COLOR:
                myMatState.setAmbient(new ColorChunk(myIn,i).getBestColor());
                myMatState.setEnabled(true);
                if (DEBUG || DEBUG_LIGHT) System.out.println("Ambient color:" + myMatState.getAmbient());
                return true;

            case MAT_DIF_COLOR:
                myMatState.setDiffuse(new ColorChunk(myIn,i).getBestColor());
                myMatState.setEnabled(true);
                if (DEBUG || DEBUG_LIGHT) System.out.println("Diffuse color:" + myMatState.getDiffuse());
                return true;

            case MAT_SPEC_CLR:
                myMatState.setSpecular(new ColorChunk(myIn,i).getBestColor());
                myMatState.setEnabled(true);
                if (DEBUG || DEBUG_LIGHT) System.out.println("Diffuse color:" + myMatState.getSpecular());
                return true;
            case MAT_SHINE:
                myMatState.setShininess(128*new PercentChunk(myIn,i).percent);
                myMatState.setEnabled(true);
                if (DEBUG || DEBUG_LIGHT) System.out.println("Shinniness:" + myMatState.getShininess());
                return true;
            case MAT_SHINE_STR:
                new PercentChunk(myIn,i);   // ignored / Unknown use
                return true;
            case MAT_ALPHA:
                float alpha = 1 - new PercentChunk(myIn, i).percent;
                myMatState.setAlpha(alpha);
                myMatState.getDiffuse().a = alpha;
                myMatState.getEmissive().a = alpha;
                myMatState.getAmbient().a = alpha;
                myMatState.setEnabled(true);
                if (DEBUG || DEBUG_LIGHT) System.out.println("Alpha:" + myMatState.getAlpha());
                return true;
            case MAT_ALPHA_FAL:
                new PercentChunk(myIn,i);   // ignored / Unknown use
                return true;
            case MAT_REF_BLUR:
                new PercentChunk(myIn,i);   // Reflective ignored
                return true;
            case MAT_SHADING:
                myIn.readShort();           // Shading ignored
                return true;
            case MAT_SELF_ILUM:
                new PercentChunk(myIn,i);   // Self illumination ignored
                return true;
            case MAT_WIRE_SIZE:
                myWireState.setLineWidth(myIn.readFloat());
                if (DEBUG || DEBUG_LIGHT) System.out.println("Wireframe size:" + myWireState.getLineWidth());
                return true;
            case IN_TRANC_FLAG:
                return true;    //  Unknown use for this flag
            case TEXMAP_ONE:
                readTextureMapOne(i);
                return true;
            case MAT_TEX_BUMPMAP:
                readTextureBumpMap(i);
                return true;
            case MAT_SOFTEN:
                //if (DEBUG) System.out.println("Material soften is true");
                return true;    // Unknown flag
            case MAT_SXP_TEXT_DATA:
                myIn.readFully(new byte[i.length]);   // unknown
                return true;
            case MAT_REFL_BLUR:
                if (DEBUG) System.out.println("Material blur present");
                // Unused Flag
                return true;
            case MAT_WIRE_ABS:
                if (DEBUG) System.out.println("Using absolute wire in units");
                // Unknown Flag
                return true;
            case MAT_REFLECT_MAP:
                readReflectMap(i);
                return true;
            case MAT_SXP_BUMP_DATA:
                myIn.readFully(new byte[i.length]);   // unknown
                return true;
            case MAT_TWO_SIDED:
                if (DEBUG) System.out.println("Material two sided indicated");
                // On by default
                return true;
            case MAT_FALLOFF:
                if (DEBUG) System.out.println("Using material falloff");
                // Unknown flag
                return true;
            case MAT_WIREFRAME_ON:
                if (DEBUG) System.out.println("Material wireframe is active");
                myWireState.setEnabled(true);
                return true;
            case MAT_TEX2MAP:
                readTextureMapTwo(i);
                return true;
            default:
                return false;
        }
    }

    private void readTextureMapTwo(ChunkHeader i) throws IOException {
        TextureChunk tc=new TextureChunk(myIn,i);
        // TexureMap 2 ignored
    }

    private void readReflectMap(ChunkHeader i) throws IOException {
        TextureChunk tc=new TextureChunk(myIn,i);
        // Reflective Map ignored
    }

    private void readTextureBumpMap(ChunkHeader i) throws IOException {
        TextureChunk tc=new TextureChunk(myIn,i);
        // BumpMap ignored

    }

    private void readTextureMapOne(ChunkHeader i) throws IOException {
        TextureChunk tc=new TextureChunk(myIn,i);
//        myTexState.setTexture(TextureManager.loadTexture(tc.texName,Texture.MM_LINEAR,Texture.FM_LINEAR,false));
        Texture t=new Texture();
        t.setImageLocation("file:/"+tc.texName);
        t.setWrap(Texture.WM_WRAP_S_WRAP_T);
        float vScale = tc.vScale;
        float uScale = tc.uScale;
        if ( uScale == 0 ) {
            uScale = 1;
        }
        if ( vScale == 0 ) {
            vScale = 1;
        }
        t.setScale( new Vector3f( uScale, vScale, 1 ) );
        myTexState.setTexture(t);
        myTexState.setEnabled(true);
    }

    private void readMatName() throws IOException{
        name=readcStr();
        if (DEBUG || DEBUG_LIGHT) System.out.println("read material name:" + name);
    }
}
