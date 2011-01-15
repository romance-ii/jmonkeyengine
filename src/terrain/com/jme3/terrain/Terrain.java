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

package com.jme3.terrain;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 * Terrain can be one or many meshes comprising of a, probably large, piece of land.
 * Terrain is Y-up in the grid axis, meaning gravity acts in the -Y direction.
 * Level of Detail (LOD) is supported and expected as terrains can get very large. LOD can
 * also be disabled if you so desire, however some terrain implementations can choose to ignore
 * useLOD(boolean).
 * Terrain implementations should extend Node, or at least Spatial.
 *
 * @author bowens
 */
public interface Terrain {

	/**
	 * Get the real-world height of the terrain at the specified X-Z coorindate.
	 * @param xz the X-Z world coordinate
	 * @return the height at the given point
	 */
	public float getHeight(Vector2f xz);

    /**
     * Get the heightmap height at the specified X-Z coordinate. This does not
     * count scaling and snaps the XZ coordinate to the nearest (rounded) heightmap grid point.
     * @param xz world coordinate
     * @return the height, unscaled and uninterpolated
     */
    public float getHeightmapHeight(Vector2f xz);
	
	/**
	 * Set the height at the specified X-Z coordinate.
     * To set the height of the terrain and see it, you will have
     * to unlock the terrain meshes by calling terrain.setLocked(false) before
     * you call setHeight().
	 * @param xzCoordinate coordinate to set the height
	 * @param height that will be set at the coordinate
	 */
	public void setHeight(Vector2f xzCoordinate, float height);

    /**
     * Raise/lower the height in one call (instead of getHeight then setHeight).
     * @param xzCoordinate world coordinate to adjust the terrain height
     * @param delta +- value to adjust the height by
     */
    public void adjustHeight(Vector2f xzCoordinate, float delta);
	
	/**
	 * Get the heightmap of the entire terrain.
	 * This can return null if that terrain object does not store the height data.
     * Infinite or "paged" terrains will not be able to support this, so use with caution.
	 */
	public float[] getHeightMap();

	/**
	 * Tell the terrain system to use/not use Level of Detail algorithms.
     * This is allowed to be ignored if a particular implementation cannot support it.
	 */
	public void useLOD(boolean useLod);

    /**
     * Check if the terrain is using LOD techniques.
     * If a terrain system only supports enabled LOD, then this
     * should always return true.
     */
	public boolean isUsingLOD();
	
	/**
	 * This is calculated by the specific LOD algorithm.
	 * A value of one means that the terrain is showing full detail.
	 * The higher the value, the more the terrain has been generalized 
	 * and the less detailed it will be.
	 */
	public int getMaxLod();
	
	/**
	 * Called in the update (pre or post, up to you) method of your game.
	 * Calculates the level of detail of the terrain and adjusts its geometry.
	 * This is where the Terrain's LOD algorithm will change the detail of 
	 * the terrain based on how far away this position is from the particular
	 * terrain patch.
	 * @param location often the Camera's location
	 */
	public void update(List<Vector3f> location);
	
    /**
     * Get the spatial instance of this Terrain. Right now just used in the 
     * terrain editor in JMP.
     */
    public Spatial getSpatial();

    /**
     * Lock or unlock the meshes of this terrain.
     * Locked meshes are uneditable but have better performance.
     * This should call the underlying getMesh().setStatic()/setDynamic() methods.
     * @param locked or unlocked
     */
    public void setLocked(boolean locked);
}
