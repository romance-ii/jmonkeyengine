/*
 * Copyright (c) 2003, jMonkeyEngine - Mojo Monkey Coding
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

package jme.geometry.primitive;

import java.util.logging.Level;

import org.lwjgl.opengl.GLU;

import jme.exception.MonkeyRuntimeException;
import jme.math.Vector;
import jme.utility.LoggingSystem;

/**
 * <code>Sphere</code> defines a spherical geometry. The sphere is defined
 * by a radius. The "quality" or tesselation of the sphere is dependant on the
 * slices and stacks value. The slices determine how many vertices along
 * equatorial line exist. The stacks determine how many vertices are along the
 * polar line. The more slices and stacks the better the sphere appears. 
 * However, frame rate will drop accordingly.
 * 
 * @author Mark Powell
 * @version $Id: Sphere.java,v 1.4 2003-09-08 20:29:27 mojomonkey Exp $
 */
public class Sphere extends Quadric {

    //attributes that define the quality of the sphere
    private double radius;
    private int slices;
    private int stacks;
    
    //glu object for the quadric sphere call.
    private GLU glu;

    /**
     * Constructor creates a new sphere. During instantiation the
     * radius, slices and stacks are defined. The radius determines the
     * size of the sphere, the slices and stacks determine how many 
     * vertices make up the sphere.
     * 
     * @param radius the distance between the center of the sphere and a
     *      point on the surface.
     * @param slices The number of subdivisions around the z-axis (similar to 
     *      lines of longitude). 
     * @param stacks The number of subdivisions along the z-axis (similar to 
     *      lines of latitude). 
     * 
     * @throws MonkeyRuntimeException if radius, slices or stacks are zero
     *      or less.
     */
    public Sphere(double radius, int slices, int stacks) {

        if (radius <= 0 || slices <= 0 || stacks <= 0) {
            throw new MonkeyRuntimeException(
                    "No sphere parameters may be less than or equal to zero.");
        }

        this.radius = radius;
        this.slices = slices;
        this.stacks = stacks;

        super.initialize();
        
        
        LoggingSystem.getLoggingSystem().getLogger().log(Level.INFO,
                "Sphere created.");
    }

    /**
     * <code>render</code> handles rendering the sphere to the view context.
     */
    public void render() {
        super.preRender();

        //glu.sphere(quadricPointer, radius, slices, stacks);

        super.clean();
    }

    /**
     * <code>setRadius</code> sets the radius for the sphere.
     * 
     * @param radius the new radius of the sphere.
     * 
     * @throws MonkeyRuntimeException if the radius is less than or equal to
     *      zero.
     */
    public void setRadius(double radius) {
        if (radius <= 0) {
            throw new MonkeyRuntimeException(
                    "Radius must be greater than zero");
        }
        this.radius = radius;
    }

    /**
     * <code>setSlices</code> sets the number of slices for the sphere.
     * 
     * @param slices the new number of slices of the sphere.
     * 
     * @throws MonkeyRuntimeException if the number of slices is less than
     *      or equal to zero.
     */
    public void setSlices(int slices) {
        if (slices <= 0) {
            throw new MonkeyRuntimeException(
                    "Number of slices must be greater than zero");
        }
        this.slices = slices;
    }

    /**
     * <code>setStacks</code> sets the number of stacks for the sphere.
     * 
     * @param stacks the new number of stacks of the sphere.
     * 
     * @throws MonkeyRuntimeException if the number of slices is less than
     *      or equal to zero.
     */
    public void setStacks(int stacks) {
        if (stacks <= 0) {
            throw new MonkeyRuntimeException(
                    "Number of stacks must be greater than zero");
        }
        this.stacks = stacks;
    }
    
    public Vector[] getPoints() {
        return null;
    }
}
