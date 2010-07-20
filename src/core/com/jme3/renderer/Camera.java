/*
 * Copyright (c) 2003-2008 jMonkeyEngine
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

package com.jme3.renderer;

import com.jme3.bounding.BoundingVolume;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * <code>Camera</code> is a standalone, purely mathematical class for doing
 * camera-related computations.
 *
 * <p>
 * Given input data such as location, orientation (direction, left, up),
 * and viewport settings, it can compute data neccessary to render objects
 * with the graphics library. Two matrices are generated, the view matrix
 * transforms objects from world space into eye space, while the projection
 * matrix transforms objects from eye space into clip space.
 * </p>
 * <p>Another purpose of the camera class is to do frustum culling operations,
 * defined by six planes which define a 3D frustum shape, it is possible to
 * test if an object bounded by a mathematically defined volume is inside
 * the camera frustum, and thus to avoid rendering objects that are outside
 * the frustum
 * </p>
 *
 * @author Mark Powell
 * @author Joshua Slack
 */
public class Camera implements Savable, Cloneable {

    private static final Logger logger = Logger.getLogger(Camera.class
            .getName());

    public enum FrustumIntersect {
        /**
         * defines a constant assigned to spatials that are completely outside
         * of this camera's view frustum.
         */
        Outside,
        /**
         * defines a constant assigned to spatials that are completely inside
         * the camera's view frustum.
         */
        Inside,
        /**
         * defines a constant assigned to spatials that are intersecting one of
         * the six planes that define the view frustum.
         */
        Intersects;
    }

    //planes of the frustum
    /**
     * LEFT_PLANE represents the left plane of the camera frustum.
     */
    public static final int LEFT_PLANE = 0;

    /**
     * RIGHT_PLANE represents the right plane of the camera frustum.
     */
    public static final int RIGHT_PLANE = 1;

    /**
     * BOTTOM_PLANE represents the bottom plane of the camera frustum.
     */
    public static final int BOTTOM_PLANE = 2;

    /**
     * TOP_PLANE represents the top plane of the camera frustum.
     */
    public static final int TOP_PLANE = 3;

    /**
     * FAR_PLANE represents the far plane of the camera frustum.
     */
    public static final int FAR_PLANE = 4;

    /**
     * NEAR_PLANE represents the near plane of the camera frustum.
     */
    public static final int NEAR_PLANE = 5;

    /**
     * FRUSTUM_PLANES represents the number of planes of the camera frustum.
     */
    public static final int FRUSTUM_PLANES = 6;

    /**
     * MAX_WORLD_PLANES holds the maximum planes allowed by the system.
     */
    public static final int MAX_WORLD_PLANES = 32;

    //the location and orientation of the camera.
    /**
     * Camera's location
     */
    protected Vector3f location;

    /**
     * The orientation of the camera.
     */
    protected Quaternion rotation;

    /**
     * Distance from camera to near frustum plane.
     */
    protected float frustumNear;

    /**
     * Distance from camera to far frustum plane.
     */
    protected float frustumFar;

    /**
     * Distance from camera to left frustum plane.
     */
    protected float frustumLeft;

    /**
     * Distance from camera to right frustum plane.
     */
    protected float frustumRight;

    /**
     * Distance from camera to top frustum plane.
     */
    protected float frustumTop;

    /**
     * Distance from camera to bottom frustum plane.
     */
    protected float frustumBottom;

    //Temporary values computed in onFrustumChange that are needed if a
    //call is made to onFrameChange.
    protected float coeffLeft[];

    protected float coeffRight[];

    protected float coeffBottom[];

    protected float coeffTop[];

    //view port coordinates
    /**
     * Percent value on display where horizontal viewing starts for this camera.
     * Default is 0.
     */
    protected float viewPortLeft;

    /**
     * Percent value on display where horizontal viewing ends for this camera.
     * Default is 1.
     */
    protected float viewPortRight;

    /**
     * Percent value on display where vertical viewing ends for this camera.
     * Default is 1.
     */
    protected float viewPortTop;

    /**
     * Percent value on display where vertical viewing begins for this camera.
     * Default is 0.
     */
    protected float viewPortBottom;

    /**
     * Array holding the planes that this camera will check for culling.
     */
    protected Plane[] worldPlane;

    /**
     * A mask value set during contains() that allows fast culling of a Node's
     * children.
     */
    private int planeState;
    
    protected int width;
    protected int height;
    protected boolean viewportChanged = true;

    /**
     * store the value for field parallelProjection
     */
    private boolean parallelProjection;

    protected Matrix4f projectionMatrixOverride;
    protected Matrix4f viewMatrix = new Matrix4f();
    protected Matrix4f projectionMatrix = new Matrix4f();
    protected Matrix4f viewProjectionMatrix = new Matrix4f();

    /**
     * Constructor instantiates a new <code>Camera</code> object. All
     * values of the camera are set to default.
     */
    public Camera(int width, int height) {
        location = new Vector3f();
        rotation = new Quaternion();

        frustumNear = 1.0f;
        frustumFar = 2.0f;
        frustumLeft = -0.5f;
        frustumRight = 0.5f;
        frustumTop = 0.5f;
        frustumBottom = -0.5f;

        coeffLeft = new float[2];
        coeffRight = new float[2];
        coeffBottom = new float[2];
        coeffTop = new float[2];

        viewPortLeft = 0.0f;
        viewPortRight = 1.0f;
        viewPortTop = 1.0f;
        viewPortBottom = 0.0f;

        worldPlane = new Plane[MAX_WORLD_PLANES];
        for ( int i = 0; i < MAX_WORLD_PLANES; i++ ) {
            worldPlane[i] = new Plane();
        }

        this.width = width;
        this.height = height;

        onFrustumChange();
        onViewPortChange();
        onFrameChange();

        logger.info("Camera created (W: "+width+", H: "+height+")");
    }

    @Override
    public Camera clone(){
        try {
            Camera cam = (Camera) super.clone();
            cam.viewportChanged = true;
            cam.planeState = 0;

            for (int i = 0; i < worldPlane.length; i++){
                cam.worldPlane[i] = worldPlane[i].clone();
            }

            cam.coeffLeft = new float[2];
            cam.coeffRight = new float[2];
            cam.coeffBottom = new float[2];
            cam.coeffTop = new float[2];

            cam.location = location.clone();
            cam.rotation = rotation.clone();

            if (projectionMatrixOverride != null)
                cam.projectionMatrixOverride = projectionMatrixOverride.clone();

            cam.viewMatrix = viewMatrix.clone();
            cam.projectionMatrix = projectionMatrix.clone();
            cam.viewProjectionMatrix = cam.viewProjectionMatrix.clone();

            return cam;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    /**
     * Resizes this camera's view with the given width and height. This is
     * similar to constructing a new camera, but reusing the same Object. This
     * method is called by an associated renderer to notify the camera of
     * changes in the display dimensions.
     *
     * @param width
     *            the view width
     * @param height
     *            the view height
     */
    public void resize(int width, int height, boolean fixAspect) {
        this.width = width;
        this.height = height;
        onViewPortChange();

        if (fixAspect && !parallelProjection){
            frustumRight = frustumTop * ((float)width / height);
            frustumLeft = -frustumRight;
            onFrustumChange();
        }
    }

    /**
     * <code>getFrustumBottom</code> returns the value of the bottom frustum
     * plane.
     *
     * @return the value of the bottom frustum plane.
     */
    public float getFrustumBottom() {
        return frustumBottom;
    }

    /**
     * <code>setFrustumBottom</code> sets the value of the bottom frustum
     * plane.
     *
     * @param frustumBottom the value of the bottom frustum plane.
     */
    public void setFrustumBottom( float frustumBottom ) {
        this.frustumBottom = frustumBottom;
        onFrustumChange();
    }

    /**
     * <code>getFrustumFar</code> gets the value of the far frustum plane.
     *
     * @return the value of the far frustum plane.
     */
    public float getFrustumFar() {
        return frustumFar;
    }

    /**
     * <code>setFrustumFar</code> sets the value of the far frustum plane.
     *
     * @param frustumFar the value of the far frustum plane.
     */
    public void setFrustumFar( float frustumFar ) {
        this.frustumFar = frustumFar;
        onFrustumChange();
    }

    /**
     * <code>getFrustumLeft</code> gets the value of the left frustum plane.
     *
     * @return the value of the left frustum plane.
     */
    public float getFrustumLeft() {
        return frustumLeft;
    }

    /**
     * <code>setFrustumLeft</code> sets the value of the left frustum plane.
     *
     * @param frustumLeft the value of the left frustum plane.
     */
    public void setFrustumLeft( float frustumLeft ) {
        this.frustumLeft = frustumLeft;
        onFrustumChange();
    }

    /**
     * <code>getFrustumNear</code> gets the value of the near frustum plane.
     *
     * @return the value of the near frustum plane.
     */
    public float getFrustumNear() {
        return frustumNear;
    }

    /**
     * <code>setFrustumNear</code> sets the value of the near frustum plane.
     *
     * @param frustumNear the value of the near frustum plane.
     */
    public void setFrustumNear( float frustumNear ) {
        this.frustumNear = frustumNear;
        onFrustumChange();
    }

    /**
     * <code>getFrustumRight</code> gets the value of the right frustum plane.
     *
     * @return frustumRight the value of the right frustum plane.
     */
    public float getFrustumRight() {
        return frustumRight;
    }

    /**
     * <code>setFrustumRight</code> sets the value of the right frustum plane.
     *
     * @param frustumRight the value of the right frustum plane.
     */
    public void setFrustumRight( float frustumRight ) {
        this.frustumRight = frustumRight;
        onFrustumChange();
    }

    /**
     * <code>getFrustumTop</code> gets the value of the top frustum plane.
     *
     * @return the value of the top frustum plane.
     */
    public float getFrustumTop() {
        return frustumTop;
    }

    /**
     * <code>setFrustumTop</code> sets the value of the top frustum plane.
     *
     * @param frustumTop the value of the top frustum plane.
     */
    public void setFrustumTop( float frustumTop ) {
        this.frustumTop = frustumTop;
        onFrustumChange();
    }

    /**
     * <code>getLocation</code> retrieves the location vector of the camera.
     *
     * @return the position of the camera.
     * @see Camera#getLocation()
     */
    public Vector3f getLocation() {
        return location;
    }

    /**
     * <code>getRotation</code> retrieves the rotation quaternion of the camera.
     *
     * @return the rotation of the camera.
     */
    public Quaternion getRotation(){
        return rotation;
    }

    /**
     * <code>getDirection</code> retrieves the direction vector the camera is
     * facing.
     *
     * @return the direction the camera is facing.
     * @see Camera#getDirection()
     */
    public Vector3f getDirection() {
        return rotation.getRotationColumn(2);
    }

    /**
     * <code>getLeft</code> retrieves the left axis of the camera.
     *
     * @return the left axis of the camera.
     * @see Camera#getLeft()
     */
    public Vector3f getLeft() {
        return rotation.getRotationColumn(0);
    }

    /**
     * <code>getUp</code> retrieves the up axis of the camera.
     *
     * @return the up axis of the camera.
     * @see Camera#getUp()
     */
    public Vector3f getUp() {
        return rotation.getRotationColumn(1);
    }

    /**
     * <code>getDirection</code> retrieves the direction vector the camera is
     * facing.
     *
     * @return the direction the camera is facing.
     * @see Camera#getDirection()
     */
    public Vector3f getDirection(Vector3f store) {
        return rotation.getRotationColumn(2, store);
    }

    /**
     * <code>getLeft</code> retrieves the left axis of the camera.
     *
     * @return the left axis of the camera.
     * @see Camera#getLeft()
     */
    public Vector3f getLeft(Vector3f store) {
        return rotation.getRotationColumn(0, store);
    }

    /**
     * <code>getUp</code> retrieves the up axis of the camera.
     *
     * @return the up axis of the camera.
     * @see Camera#getUp()
     */
    public Vector3f getUp(Vector3f store) {
        return rotation.getRotationColumn(1, store);
    }

    /**
     * <code>setLocation</code> sets the position of the camera.
     *
     * @param location the position of the camera.
     * @see Camera#setLocation(com.jme.math.Vector3f)
     */
    public void setLocation(Vector3f location) {
        this.location.set(location);
        onFrameChange();
    }

    /**
     * <code>setRotation</code> sets the orientation of this camera. 
     * This will be equivelant to setting each of the axes:
     * <code><br>
     * cam.setLeft(rotation.getRotationColumn(0));<br>
     * cam.setUp(rotation.getRotationColumn(1));<br>
     * cam.setDirection(rotation.getRotationColumn(2));<br>
     * </code>
     *
     * @param rotation the rotation of this camera
     */
    public void setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
        onFrameChange();
    }

    /**
     * <code>setDirection</code> sets the direction this camera is facing. In
     * most cases, this changes the up and left vectors of the camera. If your
     * left or up vectors change, you must updates those as well for correct
     * culling.
     *
     * @param direction the direction this camera is facing.
     * @see Camera#setDirection(com.jme.math.Vector3f)
     */
    public void setDirection(Vector3f direction) {
        //this.rotation.lookAt(direction, getUp());
        Vector3f left = getLeft();
        Vector3f up = getUp();
        this.rotation.fromAxes(left, up, direction);
        onFrameChange();
    }

    /**
     * <code>setAxes</code> sets the axes (left, up and direction) for this
     * camera.
     *
     * @param left      the left axis of the camera.
     * @param up        the up axis of the camera.
     * @param direction the direction the camera is facing.
     * @see Camera#setAxes(com.jme.math.Vector3f,com.jme.math.Vector3f,com.jme.math.Vector3f)
     */
    public void setAxes( Vector3f left, Vector3f up, Vector3f direction ) {
        this.rotation.fromAxes(left, up, direction);
        onFrameChange();
    }

    /**
     * <code>setAxes</code> uses a rotational matrix to set the axes of the
     * camera.
     *
     * @param axes the matrix that defines the orientation of the camera.
     */
    public void setAxes(Quaternion axes) {
        this.rotation.set(axes);
        onFrameChange();
    }

    /**
     * normalize normalizes the camera vectors.
     */
    public void normalize() {
        this.rotation.normalize();
        onFrameChange();
    }

    /**
     * <code>setFrustum</code> sets the frustum of this camera object.
     *
     * @param near   the near plane.
     * @param far    the far plane.
     * @param left   the left plane.
     * @param right  the right plane.
     * @param top    the top plane.
     * @param bottom the bottom plane.
     * @see Camera#setFrustum(float, float, float, float,
     *      float, float)
     */
    public void setFrustum( float near, float far, float left, float right,
                            float top, float bottom ) {

        frustumNear = near;
        frustumFar = far;
        frustumLeft = left;
        frustumRight = right;
        frustumTop = top;
        frustumBottom = bottom;
        onFrustumChange();
    }

    /**
     * <code>setFrustumPerspective</code> defines the frustum for the camera.  This
     * frustum is defined by a viewing angle, aspect ratio, and near/far planes
     *
     * @param fovY   Frame of view angle along the Y in degrees.
     * @param aspect Width:Height ratio
     * @param near   Near view plane distance
     * @param far    Far view plane distance
     */
    public void setFrustumPerspective( float fovY, float aspect, float near,
                                       float far ) {
        if (Float.isNaN(aspect) || Float.isInfinite(aspect)) {
            // ignore.
            logger.warning("Invalid aspect given to setFrustumPerspective: "
                    + aspect);
            return;
        }

        float h = FastMath.tan( fovY * FastMath.DEG_TO_RAD * .5f) * near;
        float w = h * aspect;
        frustumLeft = -w;
        frustumRight = w;
        frustumBottom = -h;
        frustumTop = h;
        frustumNear = near;
        frustumFar = far;

        onFrustumChange();
    }

    /**
     * <code>setFrame</code> sets the orientation and location of the camera.
     *
     * @param location  the point position of the camera.
     * @param left      the left axis of the camera.
     * @param up        the up axis of the camera.
     * @param direction the facing of the camera.
     * @see Camera#setFrame(com.jme.math.Vector3f,
     *      com.jme.math.Vector3f, com.jme.math.Vector3f, com.jme.math.Vector3f)
     */
    public void setFrame( Vector3f location, Vector3f left, Vector3f up,
                          Vector3f direction ) {

        this.location = location;
        this.rotation.fromAxes(left, up, direction);
        onFrameChange();
    }

    /**
     * <code>lookAt</code> is a convienence method for auto-setting the frame
     * based on a world position the user desires the camera to look at. It
     * repoints the camera towards the given position using the difference
     * between the position and the current camera location as a direction
     * vector and the worldUpVector to compute up and left camera vectors.
     *
     * @param pos           where to look at in terms of world coordinates
     * @param worldUpVector a normalized vector indicating the up direction of the world.
     *                      (typically {0, 1, 0} in jME.)
     */
    public void lookAt(Vector3f pos, Vector3f worldUpVector) {
        TempVars vars = TempVars.get();
        assert vars.lock();
        Vector3f newDirection = TempVars.get().vect1;
        Vector3f newUp = TempVars.get().vect2;
        Vector3f newLeft = TempVars.get().vect3;

        newDirection.set(pos).subtractLocal(location).normalizeLocal();

        newUp.set(worldUpVector).normalizeLocal();
        if (newUp.equals(Vector3f.ZERO))
            newUp.set(Vector3f.UNIT_Y);

        newLeft.set(newUp).crossLocal(newDirection).normalizeLocal();
        if (newLeft.equals(Vector3f.ZERO)){
            if (newDirection.x != 0) {
                newLeft.set(newDirection.y, -newDirection.x, 0f);
            } else {
                newLeft.set(0f, newDirection.z, -newDirection.y);
            }
        }

        newUp.set(newDirection).crossLocal(newLeft).normalizeLocal();

        this.rotation.fromAxes(newLeft, newUp, newDirection);
        this.rotation.normalize();
        assert vars.unlock();

        onFrameChange();
    }

    /**
     * <code>setFrame</code> sets the orientation and location of the camera.
     * 
     * @param location
     *            the point position of the camera.
     * @param axes
     *            the orientation of the camera.
     */
    public void setFrame( Vector3f location, Quaternion axes ) {
        this.location = location;
        this.rotation.set(axes);
        onFrameChange();
    }

    /**
     * <code>update</code> updates the camera parameters by calling
     * <code>onFrustumChange</code>,<code>onViewPortChange</code> and
     * <code>onFrameChange</code>.
     *
     * @see Camera#update()
     */
    public void update() {
        onFrustumChange();
        onViewPortChange();
        onFrameChange();
    }

    /**
     * <code>getPlaneState</code> returns the state of the frustum planes. So
     * checks can be made as to which frustum plane has been examined for
     * culling thus far.
     *
     * @return the current plane state int.
     */
    public int getPlaneState() {
        return planeState;
    }

    /**
     * <code>setPlaneState</code> sets the state to keep track of tested
     * planes for culling.
     *
     * @param planeState the updated state.
     */
    public void setPlaneState(int planeState) {
        this.planeState = planeState;
    }

    /**
     * <code>getViewPortLeft</code> gets the left boundary of the viewport
     *
     * @return the left boundary of the viewport
     */
    public float getViewPortLeft() {
        return viewPortLeft;
    }

    /**
     * <code>setViewPortLeft</code> sets the left boundary of the viewport
     *
     * @param left the left boundary of the viewport
     */
    public void setViewPortLeft( float left ) {
        viewPortLeft = left;
        onViewPortChange();
    }

    /**
     * <code>getViewPortRight</code> gets the right boundary of the viewport
     *
     * @return the right boundary of the viewport
     */
    public float getViewPortRight() {
        return viewPortRight;
    }

    /**
     * <code>setViewPortRight</code> sets the right boundary of the viewport
     *
     * @param right the right boundary of the viewport
     */
    public void setViewPortRight( float right ) {
        viewPortRight = right;
        onViewPortChange();
    }

    /**
     * <code>getViewPortTop</code> gets the top boundary of the viewport
     *
     * @return the top boundary of the viewport
     */
    public float getViewPortTop() {
        return viewPortTop;
    }

    /**
     * <code>setViewPortTop</code> sets the top boundary of the viewport
     *
     * @param top the top boundary of the viewport
     */
    public void setViewPortTop( float top ) {
        viewPortTop = top;
        onViewPortChange();
    }

    /**
     * <code>getViewPortBottom</code> gets the bottom boundary of the viewport
     *
     * @return the bottom boundary of the viewport
     */
    public float getViewPortBottom() {
        return viewPortBottom;
    }

    /**
     * <code>setViewPortBottom</code> sets the bottom boundary of the viewport
     *
     * @param bottom the bottom boundary of the viewport
     */
    public void setViewPortBottom( float bottom ) {
        viewPortBottom = bottom;
        onViewPortChange();
    }

    /**
     * <code>setViewPort</code> sets the boundaries of the viewport
     *
     * @param left   the left boundary of the viewport (default: 0)
     * @param right  the right boundary of the viewport (default: 1)
     * @param bottom the bottom boundary of the viewport (default: 0)
     * @param top    the top boundary of the viewport (default: 1)
     */
    public void setViewPort( float left, float right, float bottom, float top ) {
        setViewPortLeft( left );
        setViewPortRight( right );
        setViewPortBottom( bottom );
        setViewPortTop( top );
    }

    /**
     * Returns the pseudo distance from the given position to the near
     * plane of the camera. This is used for render queue sorting.
     * @param pos The position to compute a distance to.
     * @return Distance from the far plane to the point.
     */
    public float distanceToNearPlane(Vector3f pos){
        return worldPlane[NEAR_PLANE].pseudoDistance(pos);
    }

    /**
     * <code>contains</code> tests a bounding volume against the planes of the
     * camera's frustum. The frustums planes are set such that the normals all
     * face in towards the viewable scene. Therefore, if the bounding volume is
     * on the negative side of the plane is can be culled out.
     *
     * NOTE: This method is used internally for culling, for public usage,
     * the plane state of the bounding volume must be saved and restored, e.g:
     * <code>BoundingVolume bv;<br/>
     * Camera c;<br/>
     * int planeState = bv.getPlaneState();<br/>
     * bv.setPlaneState(0);<br/>
     * c.contains(bv);<br/>
     * bv.setPlaneState(plateState);<br/>
     * </code>
     *
     * @param bound the bound to check for culling
     * @return See enums in <code>FrustumIntersect</code>
     */
    public FrustumIntersect contains(BoundingVolume bound){
        if (bound == null) {
            return FrustumIntersect.Inside;
        }

        int mask;
        FrustumIntersect rVal = FrustumIntersect.Inside;

        for (int planeCounter = FRUSTUM_PLANES; planeCounter >= 0; planeCounter--) {
            if ( planeCounter == bound.getCheckPlane() ) {
                continue; // we have already checked this plane at first iteration
            }
            int planeId = ( planeCounter == FRUSTUM_PLANES ) ? bound.getCheckPlane() : planeCounter;
//            int planeId = planeCounter;

            mask = 1 << ( planeId );
            if ( ( planeState & mask ) == 0 ) {
                Plane.Side side = bound.whichSide( worldPlane[planeId] );

                if ( side == Plane.Side.Negative ) {
                    //object is outside of frustum
                    bound.setCheckPlane( planeId );
                    return FrustumIntersect.Outside;
                } else if ( side == Plane.Side.Positive ) {
                    //object is visible on *this* plane, so mark this plane
                    //so that we don't check it for sub nodes.
                    planeState |= mask;
                } else {
                    rVal = FrustumIntersect.Intersects;
                }
            }
        }

        return rVal;
    }

    /**
     * @return the view matrix of the camera.
     * The view matrix transforms world space into eye space.
     * This matrix is usually defined by the position and
     * orientation of the camera.
     */
    public Matrix4f getViewMatrix(){
        return viewMatrix;
    }

    /**
     * Overrides the projection matrix used by the camera. Will
     * use the matrix for computing the view projection matrix as well.
     * Use null argument to return to normal functionality.
     *
     * @param projMatrix
     */
    public void setProjectionMatrix(Matrix4f projMatrix){
        projectionMatrixOverride = projMatrix;
        updateViewProjection();
    }

    /**
     * @return the projection matrix of the camera.
     * The view matrix transforms eye space into clip space.
     * This matrix is usually defined by the viewport and perspective settings
     * of the camera.
     */
    public Matrix4f getProjectionMatrix(){
        if (projectionMatrixOverride != null)
            return projectionMatrixOverride;

        return projectionMatrix;
    }

    /**
     * Updates the view projection matrix.
     */
    public void updateViewProjection(){
        if (projectionMatrixOverride != null){
            viewProjectionMatrix.set(projectionMatrixOverride).multLocal(viewMatrix);
        }else{
            //viewProjectionMatrix.set(viewMatrix).multLocal(projectionMatrix);
            viewProjectionMatrix.set(projectionMatrix).multLocal(viewMatrix);
        }
    }

    /**
     * @return The result of multiplying the projection matrix by the view
     * matrix. This matrix is required for rendering an object. It is
     * precomputed so as to not compute it every time an object is rendered.
     */
    public Matrix4f getViewProjectionMatrix(){
        return viewProjectionMatrix;
    }

    /**
     * @return True if the viewport (width, height, left, right, bottom, up)
     * has been changed. This is needed in the renderer so that the proper
     * viewport can be set-up.
     */
    public boolean isViewportChanged(){
        return viewportChanged;
    }

    /**
     * Clears the viewport changed flag once it has been updated inside
     * the renderer.
     */
    public void clearViewportChanged(){
        viewportChanged = false;
    }

    /**
     * Called when the viewport has been changed.
     */
    public void onViewPortChange(){
        viewportChanged = true;
    }

    /**
     * <code>onFrustumChange</code> updates the frustum to reflect any changes
     * made to the planes. The new frustum values are kept in a temporary
     * location for use when calculating the new frame. The projection
     * matrix is updated to reflect the current values of the frustum.
     */
    public void onFrustumChange() {
        if ( !isParallelProjection() ) {
            float nearSquared = frustumNear * frustumNear;
            float leftSquared = frustumLeft * frustumLeft;
            float rightSquared = frustumRight * frustumRight;
            float bottomSquared = frustumBottom * frustumBottom;
            float topSquared = frustumTop * frustumTop;

            float inverseLength = FastMath.invSqrt( nearSquared + leftSquared );
            coeffLeft[0] = frustumNear * inverseLength;
            coeffLeft[1] = -frustumLeft * inverseLength;

            inverseLength = FastMath.invSqrt( nearSquared + rightSquared );
            coeffRight[0] = -frustumNear * inverseLength;
            coeffRight[1] = frustumRight * inverseLength;

            inverseLength = FastMath.invSqrt( nearSquared + bottomSquared );
            coeffBottom[0] = frustumNear * inverseLength;
            coeffBottom[1] = -frustumBottom * inverseLength;

            inverseLength = FastMath.invSqrt( nearSquared + topSquared );
            coeffTop[0] = -frustumNear * inverseLength;
            coeffTop[1] = frustumTop * inverseLength;
        }
        else {
            coeffLeft[0] = 1;
            coeffLeft[1] = 0;

            coeffRight[0] = -1;
            coeffRight[1] = 0;

            coeffBottom[0] = 1;
            coeffBottom[1] = 0;

            coeffTop[0] = -1;
            coeffTop[1] = 0;
        }

        projectionMatrix.fromFrustum(frustumNear, frustumFar, frustumLeft, frustumRight, frustumTop, frustumBottom, parallelProjection);
//        projectionMatrix.transposeLocal();

        // The frame is effected by the frustum values
        // update it as well
        onFrameChange();
    }

    /**
     * <code>onFrameChange</code> updates the view frame of the camera.
     */
    public void onFrameChange() {
        Vector3f left = getLeft();
        Vector3f direction = getDirection();
        Vector3f up = getUp();

        float dirDotLocation = direction.dot( location );

        // left plane
        Vector3f leftPlaneNormal = worldPlane[LEFT_PLANE].getNormal();
        leftPlaneNormal.set(left).multLocal(coeffLeft[0]);
//        leftPlaneNormal.x = left.x * coeffLeft[0];
//        leftPlaneNormal.y = left.y * coeffLeft[0];
//        leftPlaneNormal.z = left.z * coeffLeft[0];
        leftPlaneNormal.addLocal( direction.x * coeffLeft[1], direction.y
                * coeffLeft[1], direction.z * coeffLeft[1] );
        worldPlane[LEFT_PLANE].setConstant( location.dot( leftPlaneNormal ) );

        // right plane
        Vector3f rightPlaneNormal = worldPlane[RIGHT_PLANE].getNormal();
        rightPlaneNormal.set(left).multLocal(coeffRight[0]);
//        rightPlaneNormal.x = left.x * coeffRight[0];
//        rightPlaneNormal.y = left.y * coeffRight[0];
//        rightPlaneNormal.z = left.z * coeffRight[0];
        rightPlaneNormal.addLocal( direction.x * coeffRight[1], direction.y
                * coeffRight[1], direction.z * coeffRight[1] );
        worldPlane[RIGHT_PLANE].setConstant( location.dot( rightPlaneNormal ) );

        // bottom plane
        Vector3f bottomPlaneNormal = worldPlane[BOTTOM_PLANE].getNormal();
        bottomPlaneNormal.set(up).multLocal(coeffBottom[0]);
//        bottomPlaneNormal.x = up.x * coeffBottom[0];
//        bottomPlaneNormal.y = up.y * coeffBottom[0];
//        bottomPlaneNormal.z = up.z * coeffBottom[0];
        bottomPlaneNormal.addLocal( direction.x * coeffBottom[1], direction.y
                * coeffBottom[1], direction.z * coeffBottom[1] );
        worldPlane[BOTTOM_PLANE].setConstant( location.dot( bottomPlaneNormal ) );

        // top plane
        Vector3f topPlaneNormal = worldPlane[TOP_PLANE].getNormal();
        topPlaneNormal.set(up).multLocal(coeffTop[0]);
//        topPlaneNormal.x = up.x * coeffTop[0];
//        topPlaneNormal.y = up.y * coeffTop[0];
//        topPlaneNormal.z = up.z * coeffTop[0];
        topPlaneNormal.addLocal( direction.x * coeffTop[1], direction.y
                * coeffTop[1], direction.z * coeffTop[1] );
        worldPlane[TOP_PLANE].setConstant( location.dot( topPlaneNormal ) );

        if ( isParallelProjection() ) {
            worldPlane[LEFT_PLANE].setConstant( worldPlane[LEFT_PLANE].getConstant() + frustumLeft );
            worldPlane[RIGHT_PLANE].setConstant( worldPlane[RIGHT_PLANE].getConstant() - frustumRight );
            worldPlane[TOP_PLANE].setConstant( worldPlane[TOP_PLANE].getConstant() + frustumTop );
            worldPlane[BOTTOM_PLANE].setConstant( worldPlane[BOTTOM_PLANE].getConstant() - frustumBottom );
        }

        // far plane
        worldPlane[FAR_PLANE].setNormal(left);
        worldPlane[FAR_PLANE].setNormal( -direction.x, -direction.y, -direction.z );
        worldPlane[FAR_PLANE].setConstant( -( dirDotLocation + frustumFar ) );

        // near plane
        worldPlane[NEAR_PLANE].setNormal( direction.x, direction.y, direction.z );
        worldPlane[NEAR_PLANE].setConstant( dirDotLocation + frustumNear );

        viewMatrix.fromFrame(location, direction, up, left);
//        viewMatrix.transposeLocal();
        updateViewProjection();
    }

    /**
     * @return true if parallel projection is enable, false if in normal perspective mode
     * @see #setParallelProjection(boolean)
     */
    public boolean isParallelProjection() {
        return this.parallelProjection;
    }

    /**
     * Enable/disable parallel projection.
     *
     * @param value true to set up this camera for parallel projection is enable, false to enter normal perspective mode
     */
    public void setParallelProjection( final boolean value ) {
        this.parallelProjection = value;
    }

    /**
     * @see Camera#getWorldCoordinates
     */
    public Vector3f getWorldCoordinates( Vector2f screenPos, float zPos ) {
        return getWorldCoordinates(screenPos, zPos, null );
    }
    

    /**
     * @see Camera#getWorldCoordinates
     */
    public Vector3f getWorldCoordinates( Vector2f screenPosition,
                                         float zPos, Vector3f store ) {
        if ( store == null ) {
            store = new Vector3f();
        }

        Matrix4f inverseMat = new Matrix4f(viewProjectionMatrix);
        inverseMat.invertLocal();

        store.set(
                ( screenPosition.x / getWidth() - viewPortLeft ) / ( viewPortRight - viewPortLeft ) * 2 - 1,
                ( screenPosition.y / getHeight() - viewPortBottom ) / ( viewPortTop - viewPortBottom ) * 2 - 1,
                zPos * 2 - 1);
        
        float w = inverseMat.multProj(store, store);
        store.multLocal(1f / w);

        return store;
    }

    /**
     * @see Camera#getScreenCoordinates
     */
    public Vector3f getScreenCoordinates( Vector3f worldPos ) {
        return getScreenCoordinates( worldPos, null );
    }


    /**
     * Implementation contributed by Zbyl.
     *
     * @see Camera#getScreenCoordinates(Vector3f, Vector3f)
     */
    public Vector3f getScreenCoordinates( Vector3f worldPosition, Vector3f store ) {
        if ( store == null ) {
            store = new Vector3f();
        }

//        assert TempVars.get().lock();
//        Quaternion tmp_quat = TempVars.get().quat1;
//        tmp_quat.set( worldPosition.x, worldPosition.y, worldPosition.z, 1 );
//        viewProjectionMatrix.mult(tmp_quat, tmp_quat);
//        tmp_quat.multLocal( 1.0f / tmp_quat.getW() );
//        store.x = ( ( tmp_quat.getX() + 1 ) * ( viewPortRight - viewPortLeft ) / 2 + viewPortLeft ) * getWidth();
//        store.y = ( ( tmp_quat.getY() + 1 ) * ( viewPortTop - viewPortBottom ) / 2 + viewPortBottom ) * getHeight();
//        store.z = ( tmp_quat.getZ() + 1 ) / 2;
//        assert TempVars.get().unlock();

        float w = viewProjectionMatrix.multProj(worldPosition, store);
        store.divideLocal(w);

        store.x = ( ( store.x + 1f ) * ( viewPortRight - viewPortLeft ) / 2f + viewPortLeft ) * getWidth();
        store.y = ( ( store.y + 1f ) * ( viewPortTop - viewPortBottom ) / 2f + viewPortBottom ) * getHeight();
        store.z = ( store.z + 1f ) / 2f;

        return store;
    }

    /**
     * @return the width/resolution of the display.
     */
    public int getWidth(){
        return width;
    }

    /**
     * @return the height/resolution of the display.
     */
    public int getHeight(){
        return height;
    }
    
    public void write(JmeExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(location, "location", Vector3f.ZERO);
        capsule.write(rotation, "rotation", Quaternion.DIRECTION_Z);
        capsule.write(frustumNear, "frustumNear", 1);
        capsule.write(frustumFar, "frustumFar", 2);
        capsule.write(frustumLeft, "frustumLeft", -0.5f);
        capsule.write(frustumRight, "frustumRight", 0.5f);
        capsule.write(frustumTop, "frustumTop", 0.5f);
        capsule.write(frustumBottom, "frustumBottom", -0.5f);
        capsule.write(coeffLeft, "coeffLeft", new float[2]);
        capsule.write(coeffRight, "coeffRight", new float[2]);
        capsule.write(coeffBottom, "coeffBottom", new float[2]);
        capsule.write(coeffTop, "coeffTop", new float[2]);
        capsule.write(viewPortLeft, "viewPortLeft", 0);
        capsule.write(viewPortRight, "viewPortRight", 1);
        capsule.write(viewPortTop, "viewPortTop", 1);
        capsule.write(viewPortBottom, "viewPortBottom", 0);
        capsule.write(width, "width", 0);
        capsule.write(height, "height", 0);
    }

    public void read(JmeImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        location = (Vector3f)capsule.readSavable("location", Vector3f.ZERO.clone());
        rotation = (Quaternion) capsule.readSavable("rotation", Quaternion.DIRECTION_Z);
        frustumNear = capsule.readFloat("frustumNear", 1);
        frustumFar = capsule.readFloat("frustumFar", 2);
        frustumLeft = capsule.readFloat("frustumLeft", -0.5f);
        frustumRight = capsule.readFloat("frustumRight", 0.5f);
        frustumTop = capsule.readFloat("frustumTop", 0.5f);
        frustumBottom = capsule.readFloat("frustumBottom", -0.5f);
        coeffLeft = capsule.readFloatArray("coeffLeft", new float[2]);
        coeffRight = capsule.readFloatArray("coeffRight", new float[2]);
        coeffBottom = capsule.readFloatArray("coeffBottom", new float[2]);
        coeffTop = capsule.readFloatArray("coeffTop", new float[2]);
        viewPortLeft = capsule.readFloat("viewPortLeft", 0);
        viewPortRight = capsule.readFloat("viewPortRight", 1);
        viewPortTop = capsule.readFloat("viewPortTop", 1);
        viewPortBottom = capsule.readFloat("viewPortBottom", 0);
        width = capsule.readInt("width", 0);
        height = capsule.readInt("height", 0);
    }
    
//    public Class<AbstractCamera> getClassTag() {
//        return AbstractCamera.class;
//    }
//
}

