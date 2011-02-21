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

package com.jme3.font;

import java.io.IOException;
import java.util.Arrays;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

/**
 * Represents a font within jME that is generated with the AngelCode Bitmap Font Generator
 * @author dhdd
 */
public class BitmapFont implements Savable {

    public enum Align {
        Left, Center, Right
    }
    public enum VAlign {
        Top, Center, Bottom
    }

    private BitmapCharacterSet charSet;
    private Material[] pages;

    public BitmapFont() {
    }

    public BitmapText createLabel(String content){
        BitmapText label = new BitmapText(this);
        label.setSize(getCharSet().getRenderedSize());
        label.setText(content);
        return label;
    }

    public float getPreferredSize(){
        return getCharSet().getRenderedSize();
    }

    public void setCharSet(BitmapCharacterSet charSet) {
        this.charSet = charSet;
    }

    public void setPages(Material[] pages) {
        this.pages = pages;
        charSet.setPageSize(pages.length);
    }

    public Material getPage(int index) {
        return pages[index];
    }

    public int getPageSize() {
        return pages.length;
    }

    public BitmapCharacterSet getCharSet() {
        return charSet;
    }
    
    /**
     * Gets the line height of a StringBlock.
     * @param sb
     * @return
     */
    public float getLineHeight(StringBlock sb) {
        return charSet.getLineHeight() * (sb.getSize() / charSet.getRenderedSize());
    }

    public float getCharacterAdvance(char curChar, char nextChar, float size){
        BitmapCharacter c = charSet.getCharacter(curChar);
        if (c == null)
            return 0f;

        int kerning = c.getKerning(nextChar);
        float advance = size * c.getXAdvance();
        if (kerning != -1){
            advance += kerning * size;
        }
        return advance;
    }

    private int findKerningAmount(int newLineLastChar, int nextChar) {
        BitmapCharacter c = charSet.getCharacter(newLineLastChar);
        if (c == null)
            return 0;
        return c.getKerning(nextChar);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(charSet, "charSet", null);
        oc.write(pages, "pages", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        charSet = (BitmapCharacterSet) ic.readSavable("charSet", null);
        Savable[] pagesSavable = ic.readSavableArray("pages", null);
        pages = new Material[pagesSavable.length];
        System.arraycopy(pagesSavable, 0, pages, 0, pages.length);
    }

    public float getLineWidth(CharSequence text){
        float lineWidth = 0f;
        float maxLineWidth = 0f;
        char lastChar = 0;
        boolean firstCharOfLine = true;
//        float sizeScale = (float) block.getSize() / charSet.getRenderedSize();
        float sizeScale = 1f;
        for (int i = 0; i < text.length(); i++){
            char theChar = text.charAt(i);
            if (theChar == '\n'){
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                lineWidth = 0f;
            }
            BitmapCharacter c = charSet.getCharacter((int) theChar);
            if (c != null){
                if (theChar == '\\' && i<text.length()-1 && text.charAt(i+1)=='#'){
                    if (i+5<text.length() && text.charAt(i+5)=='#'){
                        i+=5;
                        continue;
                    }else if (i+8<text.length() && text.charAt(i+8)=='#'){
                        i+=8;
                        continue;
                    }
                }
                if (!firstCharOfLine){
                    int amount = findKerningAmount(lastChar, theChar);
                    if (amount != -1){
                        lineWidth += amount * sizeScale;
                    }
                }
                float xAdvance = c.getXAdvance() * sizeScale;
                lineWidth += xAdvance;
            }
        }
        return Math.max(maxLineWidth, lineWidth);
    }


    /**
     * Merge two fonts.
     * If two font have the same style, merge will fail.
     * @param styleSet Style must be assigned to this.
     * @author Yonghoon
     */
    public void merge(BitmapFont newFont) {
        charSet.merge(newFont.charSet);
        final int size1 = this.pages.length;
        final int size2 = newFont.pages.length;
        this.pages = Arrays.copyOf(this.pages, size1+size2);
        System.arraycopy(newFont.pages, 0, this.pages, size1, size2);
    }

    public void setStyle(int style) {
        charSet.setStyle(style);
    }

}