/*
 * Copyright (c) 2003-2004, jMonkeyEngine - Mojo Monkey Coding
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the Mojo Monkey Coding, jME, jMonkey Engine, nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.jme.input.action;

import com.jme.math.Matrix3f;
import com.jme.scene.Spatial;

/**
 * <code>KeyNodeLookDownAction</code> defines an action to tilt the node
 * towards the worlds negative y-axis. The rotation is along the node's left
 * vector (the first column of it's rotation matrix).
 * @author Mark Powell
 * @version $Id: KeyNodeLookDownAction.java,v 1.8 2004-05-08 02:48:27 renanse Exp $
 */
public class KeyNodeLookDownAction extends AbstractInputAction {
    private Matrix3f incr;
    private Spatial node;

    /**
     * Constructor instatiates a new <code>KeyNodeLookDownAction</code> object
     * using the supplied node and speed for it's attributes.
     * @param node the node that will be affected by this action.
     * @param speed the speed at which the node can move.
     */
    public KeyNodeLookDownAction(Spatial node, float speed) {
        incr = new Matrix3f();
        this.node = node;
        this.speed = speed;
    }

    /**
     * <code>performAction</code> rotates the node towards the world's negative
     * y-axis at a speed of movement speed * time. Where time is
     * the time between frames and 1 corresponds to 1 second.
     * @see com.jme.input.action.InputAction#performAction(float)
     */
    public void performAction(float time) {
        incr.loadIdentity();
        incr.fromAxisAngle(node.getLocalRotation().getRotationColumn(0), speed * time);
        node.getLocalRotation().fromRotationMatrix(incr.mult(node.getLocalRotation().toRotationMatrix()));
        node.getLocalRotation().normalize();
        node.updateWorldData(0);
    }
}
