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

import java.util.ArrayList;

class QuadList {

    private ArrayList<FontQuad> quads = new ArrayList<FontQuad>();
    private int actualSize = 0;

    FontQuad getQuad(int index){
        return quads.get(index);
    }

    int getQuantity(){
        return quads.size();
    }

    void clear() {
        actualSize = 0;
    }
    
    void removeAllQuads() {
        quads.clear();
        actualSize = 0;
    }

    int getActualSize() {
        return actualSize;
    }

    /**
     * Sets the size of any remaining quads to 0, 0
     */
    void cleanTail(){
        if (quads.size() > actualSize){
            for (int i = actualSize; i < quads.size(); i++){
                quads.get(i).setSize(0, 0);
            }
        }
    }

    FontQuad newQuad() {
        FontQuad q = null;
        if (actualSize == quads.size()) {
            q = new FontQuad();
            quads.add(q);
        } else {
            q = quads.get(actualSize);
            q.setSize(0, 0);
        }
        actualSize++;
        return q;
    }

    QuadList getPage(int page, QuadList store) {
        store.removeAllQuads();
        for (int i = 0; i < actualSize; i++) {
            FontQuad q = quads.get(i);
            if (q.getBitmapChar().getPage() == page) {
                store.quads.add(q);
            }
        }
        store.actualSize = store.quads.size();
        return store;
    }
}