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
package com.jme3.bullet.nodes;

import com.bulletphysics.dynamics.RigidBody;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.bullet.collision.CollisionObject;
import com.jme3.bullet.util.Converter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.scene.Node;
import java.io.IOException;

/**
 * Stores info about one wheel of a PhysicsVehicleNode
 * @author normenhansen
 */
public class PhysicsVehicleWheel extends Node{
    private com.bulletphysics.dynamics.vehicle.WheelInfo wheelInfo;
    private Spatial spatial;
    private boolean frontWheel;
    private Vector3f location=new Vector3f();
    private Vector3f direction=new Vector3f();
    private Vector3f axle=new Vector3f();

    private float suspensionStiffness = 20.0f;
    private float wheelsDampingRelaxation = 2.3f;
    private float wheelsDampingCompression = 4.4f;
    private float frictionSlip = 10.5f;
    private float rollInfluence = 1.0f;
    private float maxSuspensionTravelCm = 500f;

    private float radius=0.5f;
    private float restLength=1f;

    private Vector3f wheelWorldLocation=new Vector3f();
    private Quaternion wheelWorldRotation=new Quaternion();

    private com.jme3.math.Quaternion tempRotation=new com.jme3.math.Quaternion();
    private com.jme3.math.Matrix3f tempMatrix=new com.jme3.math.Matrix3f();
    
    public PhysicsVehicleWheel(Spatial spat, Vector3f location, Vector3f direction, Vector3f axle,
            float restLength, float radius, boolean frontWheel) {
        this.attachChild(spat);
        this.spatial = spat;
        this.location.set(location);
        this.direction.set(direction);
        this.axle.set(axle);
        this.frontWheel=frontWheel;
        this.restLength=restLength;
        this.radius=radius;
    }

    @Override
    public synchronized void updateGeometricState(){
        if ((refreshFlags & RF_LIGHTLIST) != 0){
            updateWorldLightList();
        }

        getLocalTranslation().set( wheelWorldLocation ).subtractLocal( parent.getWorldTranslation() );
        getLocalTranslation().divideLocal( parent.getWorldScale() );
        tempRotation.set( parent.getWorldRotation()).inverseLocal().multLocal( getLocalTranslation() );

        tempRotation.set(parent.getWorldRotation()).inverseLocal().mult(wheelWorldRotation,getLocalRotation());

        updateWorldTransforms();

        // the important part- make sure child geometric state is refreshed
        // first before updating own world bound. This saves
        // a round-trip later on.
        // NOTE 9/19/09
        // Although it does save a round trip,
        for (int i = 0, cSize = children.size(); i < cSize; i++) {
            Spatial child = children.get(i);
            child.updateGeometricState();
        }

        if ((refreshFlags & RF_BOUND) != 0){
            updateWorldBound();
        }
        
    }

    public synchronized void updatePhysicsState(){
        Converter.convert(wheelInfo.worldTransform.origin,wheelWorldLocation);
        Converter.convert(wheelInfo.worldTransform.basis,tempMatrix);
        wheelWorldRotation.fromRotationMatrix(tempMatrix);
    }

    public void setParent(PhysicsVehicleNode parent) {
        this.parent = parent;
    }

    public com.bulletphysics.dynamics.vehicle.WheelInfo getWheelInfo() {
        return wheelInfo;
    }

    public void setWheelInfo(com.bulletphysics.dynamics.vehicle.WheelInfo wheelInfo) {
        this.wheelInfo = wheelInfo;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spat) {
        this.spatial = spat;
    }

    public boolean isFrontWheel() {
        return frontWheel;
    }

    public void setFrontWheel(boolean frontWheel) {
        this.frontWheel = frontWheel;
        applyInfo();
    }

    public Vector3f getLocation() {
        return location;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public Vector3f getAxle() {
        return axle;
    }

    public float getSuspensionStiffness() {
        return suspensionStiffness;
    }

    /**
     * the stiffness constant for the suspension.  10.0 - Offroad buggy, 50.0 - Sports car, 200.0 - F1 Car
     * @param suspensionStiffness
     */
    public void setSuspensionStiffness(float suspensionStiffness) {
        this.suspensionStiffness = suspensionStiffness;
        applyInfo();
    }

    public float getWheelsDampingRelaxation() {
        return wheelsDampingRelaxation;
    }

    /**
     * the damping coefficient for when the suspension is expanding.
     * See the comments for setWheelsDampingCompression for how to set k.
     * @param wheelsDampingRelaxation
     */
    public void setWheelsDampingRelaxation(float wheelsDampingRelaxation) {
        this.wheelsDampingRelaxation = wheelsDampingRelaxation;
        applyInfo();
    }

    public float getWheelsDampingCompression() {
        return wheelsDampingCompression;
    }

    /**
     * the damping coefficient for when the suspension is compressed.
     * Set to k * 2.0 * FastMath.sqrt(m_suspensionStiffness) so k is proportional to critical damping.<br>
     * k = 0.0 undamped & bouncy, k = 1.0 critical damping<br>
     * 0.1 to 0.3 are good values
     * @param wheelsDampingCompression
     */
    public void setWheelsDampingCompression(float wheelsDampingCompression) {
        this.wheelsDampingCompression = wheelsDampingCompression;
        applyInfo();
    }

    public float getFrictionSlip() {
        return frictionSlip;
    }

    /**
     * the coefficient of friction between the tyre and the ground.
     * Should be about 0.8 for realistic cars, but can increased for better handling.
     * Set large (10000.0) for kart racers
     * @param frictionSlip
     */
    public void setFrictionSlip(float frictionSlip) {
        this.frictionSlip = frictionSlip;
        applyInfo();
    }

    public float getRollInfluence() {
        return rollInfluence;
    }

    /**
     * reduces the rolling torque applied from the wheels that cause the vehicle to roll over.
     * This is a bit of a hack, but it's quite effective. 0.0 = no roll, 1.0 = physical behaviour.
     * If m_frictionSlip is too high, you'll need to reduce this to stop the vehicle rolling over.
     * You should also try lowering the vehicle's centre of mass
     * @param rollInfluence the rollInfluence to set
     */
    public void setRollInfluence(float rollInfluence) {
        this.rollInfluence = rollInfluence;
        applyInfo();
    }

    public float getMaxSuspensionTravelCm() {
        return maxSuspensionTravelCm;
    }

    /**
     * the maximum distance the suspension can be compressed (centimetres)
     * @param maxSuspensionTravelCm
     */
    public void setMaxSuspensionTravelCm(float maxSuspensionTravelCm) {
        this.maxSuspensionTravelCm = maxSuspensionTravelCm;
        applyInfo();
    }

    public void applyInfo(){
        wheelInfo.suspensionStiffness = suspensionStiffness;
        wheelInfo.wheelsDampingRelaxation = wheelsDampingRelaxation;
        wheelInfo.wheelsDampingCompression = wheelsDampingCompression;
        wheelInfo.frictionSlip = frictionSlip;
        wheelInfo.rollInfluence = rollInfluence;
        wheelInfo.maxSuspensionTravelCm = maxSuspensionTravelCm;
        wheelInfo.wheelsRadius = radius;
        wheelInfo.bIsFrontWheel = frontWheel;
        wheelInfo.suspensionRestLength1=restLength;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        applyInfo();
    }

    public float getRestLength() {
        return restLength;
    }

    public void setRestLength(float restLength) {
        this.restLength = restLength;
        applyInfo();
    }

    /**
     * returns the object this wheel is in contact with or null if no contact
     * @return the CollisionObject (PhysicsNode, PhysicsGhostNode)
     */
    public CollisionObject getGroundObject(){
        if(wheelInfo.raycastInfo.groundObject == null){
            return null;
        }
        else if(wheelInfo.raycastInfo.groundObject instanceof RigidBody){
            System.out.println("RigidBody");
            return (PhysicsNode)((RigidBody)wheelInfo.raycastInfo.groundObject).getUserPointer();
        }
        else
            return null;
    }

    /**
     * returns the location where the wheel collides with the ground
     */
    public Vector3f getCollisionLocation(Vector3f vec){
        Converter.convert(wheelInfo.raycastInfo.contactPointWS,vec);
        return vec;
    }

    /**
     * returns the location where the wheel collides with the ground
     */
    public Vector3f getCollisionLocation(){
        return Converter.convert(wheelInfo.raycastInfo.contactPointWS);
    }

    /**
     * returns the normal where the wheel collides with the ground
     */
    public Vector3f getCollisionNormal(Vector3f vec){
        Converter.convert(wheelInfo.raycastInfo.contactNormalWS,vec);
        return vec;
    }

    /**
     * returns the normal where the wheel collides with the ground
     */
    public Vector3f getCollisionNormal(){
        return Converter.convert(wheelInfo.raycastInfo.contactNormalWS);
    }

    /**
     * returns how much the wheel skids on the ground (for skid sounds/smoke etc.)<br>
     * 0.0 = wheels are sliding, 1.0 = wheels have traction.
     */
    public float getSkidInfo(){
        return wheelInfo.skidInfo;
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        //TODO
//        throw new UnsupportedOperationException("vehicle saving not working yet");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        //TODO
//        throw new UnsupportedOperationException("vehicle saving not working yet");
    }

}
