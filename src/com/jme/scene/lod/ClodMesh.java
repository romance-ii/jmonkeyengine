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

package com.jme.scene.lod;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.TriMesh;

/**
 * <code>ClodMesh</code>
 * originally ported from David Eberly's c++, modifications and
 * enhancements made from there. <br><br>
 * This class is an extention of TriMesh that allows the option of rendering
 * a trimesh at various degrees of accuracy.
 * @author Joshua Slack
 * @author Jack Lindamood (javadoc only)
 * @version $Id: ClodMesh.java,v 1.18 2005-01-10 20:30:45 renanse Exp $
 */
public class ClodMesh extends TriMesh {
  private static final long serialVersionUID = 1L;
int currentRecord, targetRecord;
  CollapseRecord[] records;

  /**
   * Empty Constructor to be used internally only.
   */
  public ClodMesh() {}


  /**
   * Creates a new ClodMesh without any information.  It is assumed a call to reconstruct and
   * create will be called before this ClodMesh is used.
   * @param name The name of the ClodMesh.
   * @see #reconstruct(com.jme.math.Vector3f[], com.jme.math.Vector3f[], com.jme.renderer.ColorRGBA[], com.jme.math.Vector2f[], int[])
   * @see #create(com.jme.scene.lod.CollapseRecord[])
   */
  public ClodMesh(String name) {
      super(name);
  }

  /**
   * Creates a ClodMesh that is a duplicate of the given TriMesh's geometric information.  RenderState
   * and Controller information is not used and should be set manually if those are to be copied as
   * well.  The records parameter defines how the ClodMesh should collapse vertices.  If null is
   * given, records are generated by the ClodMesh automatically.
   * @param name The name of the ClodMesh.
   * @param data The TriMesh to copy information into for this mesh.
   * @param records The collapse record(s) this ClodMesh should use.  These modify how the ClodMesh
   * collapses vertexes.
   */
  public ClodMesh(
      String name,
      TriMesh data,
      CollapseRecord[] records) {

    this(name, data.getVertices(), data.getNormals(), data.getColors(),
         data.getTextures(), data.getIndices(), records);

  }

  /**
   * Creates a clod mesh with the given information.  A null for records causes the ClodMesh to
   * generate its own records information.
   * @param name The name of the ClodMesh.
   * @param vertices The vertex information of this clod mesh.
   * @param normal The per vertex normal information of this clod mesh.
   * @param color The per vertex color information of this clod mesh.
   * @param texture The per vertex texture information of this clod mesh.
   * @param indices The index array of this TriMesh's triangles.
   * @param records The collapse record(s) this ClodMesh should use.  These modify how the ClodMesh
   * collapses vertexes.
   */
  public ClodMesh(
      String name,
      Vector3f[] vertices,
      Vector3f[] normal,
      ColorRGBA[] color,
      Vector2f[] texture,
      int[] indices, CollapseRecord[] records) {

    super(name, vertices, normal, color, texture, indices);

    create(records);
  }

  /**
   * This function sets the records information for this ClodMesh.  If non-null is passed, then
   * the ClodMesh uses the passed CollapseRecord(s) to determine how the TriMesh's triangles should
   * collapse.  If null is passed, then the collapse records are created automatically with ClodCreator.
   * In most cases, null will be passed.
   * @param records The records for this ClodMesh to use, or null if it should generate its own.
   * @see ClodCreator
   */
  public void create(CollapseRecord[] records) {

      targetRecord = 0;
      currentRecord = 0;

      if (records != null && records.length > 0) {
        this.records = records;
      } else {
        ClodCreator creator = new ClodCreator(this.getVertices(), this.getNormals(), this.getColors(), this.getTextures(),
                                  this.getIndices());
        this.records = creator.getRecords();
        creator.removeAllTriangles();
        creator = null;
      }
      triangleQuantity = this.records[0].numbTriangles;
      vertQuantity = this.records[0].numbVerts;

      updateColorBuffer();
      updateNormalBuffer();
      updateVertexBuffer();
      updateTextureBuffer();
      updateIndexBuffer();
      updateModelBound();

  }

  private void selectLevelOfDetail(Renderer r) {
    // Get target record.  The function may be overridden by a derived
    // class to obtain a desired automated change in the target.

    int iTargetRecord = chooseTargetRecord(r);
    if (iTargetRecord == currentRecord) {
      return;
    }

    // collapse mesh (if necessary)
    int i, iC;
    while (currentRecord < iTargetRecord) {
      currentRecord++;

      // replace indices in connectivity array
      CollapseRecord rkRecord = records[currentRecord];
      for (i = 0; i < rkRecord.numbIndices; i++) {
        iC = rkRecord.indices[i];
//        if (! (indices[iC] == rkRecord.vertToThrow))throw new AssertionError();
        indices[iC] = rkRecord.vertToKeep;
      }

      // reduce vertex count (vertices are properly ordered)
      vertQuantity = rkRecord.numbVerts;

      // reduce triangle count (triangles are properly ordered)
      triangleQuantity = rkRecord.numbTriangles;
    }

    // expand mesh (if necessary)
    while (currentRecord > iTargetRecord) {
      // restore indices in connectivity array
      CollapseRecord rkRecord = records[currentRecord];
      for (i = 0; i < rkRecord.numbIndices; i++) {
        iC = rkRecord.indices[i];
//        if (! (indices[iC] == rkRecord.vertToKeep))throw new AssertionError();
        indices[iC] = rkRecord.vertToThrow;
      }

      currentRecord--;
      CollapseRecord rkPrevRecord = records[currentRecord];

      // increase vertex count (vertices are properly ordered)
      vertQuantity = rkPrevRecord.numbVerts;

      // increase triangle count (triangles are properly ordered)
      triangleQuantity = rkPrevRecord.numbTriangles;
    }

    updateVertexBuffer();
    updateIndexBuffer();
  }

  /**
   * Called during rendering.  Should not be called directly.
   * @param r The renderer to draw this TriMesh with.
   */
  public void draw(Renderer r) {
    selectLevelOfDetail(r);
    super.draw(r);
  }

  /**
   * Returns the number of records this ClodMesh currently uses.
   * @return The current length of the records array.
   */
  public int getRecordQuantity() {
    return records.length;
  }

  /**
   * This function should not be called manually.  It selects the correct target record based on
   * the renderer used.  For a basic ClodMesh, simply the currently set target record is used.  The
   * target record signals how to collapse this mesh's triangle information.
   * @param r A renderer which aids in choosing the correct target record.
   * @return The integer position in the records array for the target collapse record.
   */
  public int chooseTargetRecord(Renderer r) {
    return targetRecord;
  }

  /**
   * Returns the currently set target record.
   * @return The currently set target record.
   */
  public int getTargetRecord() {
    return targetRecord;
  }

  /**
   * Returns the current collapse records array.  This array is created in create()
   * @return The current collapse records array.
   * @see #create(com.jme.scene.lod.CollapseRecord[])
   */
  public CollapseRecord[] getRecords() {
    return records;
  }

  /**
   * Sets the current target record to the target value.  If less than 0 is passed,
   * 0 is used.  If a value greater than the current records length is passed, then the maximum
   * records length value is used.
   * @param target The new target record to use.
   */
  public void setTargetRecord(int target) {
    targetRecord = target;
    if (targetRecord < 0)
      targetRecord = 0;
    else if (targetRecord > records.length - 1)
      targetRecord = records.length - 1;
  }

  /**
   * Sets the records this ClodMesh will use to collapse triangles.
   * @param records The new records information of this ClodMesh.
   */
  public void setRecords(CollapseRecord[] records) {
    this.records = records;
  }
}
