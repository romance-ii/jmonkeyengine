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

/**
 * Author: Normen Hansen
 */

#include "com_jme3_bullet_objects_PhysicsCharacter.h"
#include "jmeBulletUtil.h"
#include "BulletCollision/CollisionDispatch/btGhostObject.h"
#include "BulletDynamics/Character/btKinematicCharacterController.h"

#ifdef __cplusplus
extern "C" {
#endif

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    createGhostObject
     * Signature: ()J
     */
    JNIEXPORT jlong JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_createGhostObject
    (JNIEnv * env, jobject object) {
        btPairCachingGhostObject* ghost = new btPairCachingGhostObject();
        return (long) ghost;
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setCharacterFlags
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setCharacterFlags
    (JNIEnv *env, jobject object, jlong ghostId) {
        btPairCachingGhostObject* ghost = (btPairCachingGhostObject*) ghostId;
        ghost->setCollisionFlags(ghost->getCollisionFlags() | btCollisionObject::CF_CHARACTER_OBJECT);
        ghost->setCollisionFlags(ghost->getCollisionFlags() & ~btCollisionObject::CF_NO_CONTACT_RESPONSE);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    createCharacterObject
     * Signature: (JJF)J
     */
    JNIEXPORT jlong JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_createCharacterObject
    (JNIEnv *env, jobject object, jlong objectId, jlong shapeId, jfloat stepHeight){
        btPairCachingGhostObject* ghost = (btPairCachingGhostObject*) objectId;
        //TODO: check convexshape!
        btConvexShape* shape = (btConvexShape*) shapeId;
        btKinematicCharacterController* character = new btKinematicCharacterController(ghost, shape, stepHeight);
        return (long)character;
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    warp
     * Signature: (JLcom/jme3/math/Vector3f;)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_warp
    (JNIEnv *env, jobject object, jlong objectId, jobject vector){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        btVector3* vec = new btVector3();
        jmeBulletUtil::convert(env, vector, vec);
        character->warp(*vec);
        delete(vec);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setWalkDirection
     * Signature: (JLcom/jme3/math/Vector3f;)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setWalkDirection
    (JNIEnv *env, jobject object, jlong objectId, jobject vector){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        btVector3* vec = new btVector3();
        jmeBulletUtil::convert(env, vector, vec);
        character->setWalkDirection(*vec);
        delete(vec);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setUpAxis
     * Signature: (JI)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setUpAxis
    (JNIEnv *env, jobject object, jlong objectId, jint value){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        character->setUpAxis(value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setFallSpeed
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setFallSpeed
    (JNIEnv *env, jobject object, jlong objectId, jfloat value){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        character->setFallSpeed(value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setJumpSpeed
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setJumpSpeed
    (JNIEnv *env, jobject object, jlong objectId, jfloat value){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        character->setJumpSpeed(value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setGravity
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setGravity
    (JNIEnv *env, jobject object, jlong objectId, jfloat value){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        character->setGravity(value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    getGravity
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_getGravity
    (JNIEnv *env, jobject object, jlong objectId){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        return character->getGravity();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setMaxSlope
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setMaxSlope
    (JNIEnv *env, jobject object, jlong objectId, jfloat value){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        character->setMaxSlope(value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    getMaxSlope
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_getMaxSlope
    (JNIEnv *env, jobject object, jlong objectId){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        return character->getMaxSlope();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    onGround
     * Signature: (J)Z
     */
    JNIEXPORT jboolean JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_onGround
    (JNIEnv *env, jobject object, jlong objectId){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        return character->onGround();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    jump
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_jump
    (JNIEnv *env, jobject object, jlong objectId){
        btKinematicCharacterController* character = (btKinematicCharacterController*)objectId;
        character->jump();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    getPhysicsLocation
     * Signature: (JLcom/jme3/math/Vector3f;)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_getPhysicsLocation
    (JNIEnv *env, jobject object, jlong objectId, jobject value){
        btGhostObject* character = (btGhostObject*)objectId;
        jmeBulletUtil::convert(env, &character->getWorldTransform().getOrigin(), value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setCcdSweptSphereRadius
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setCcdSweptSphereRadius
    (JNIEnv *env, jobject object, jlong objectId, jfloat value){
        btGhostObject* character = (btGhostObject*)objectId;
        character->setCcdSweptSphereRadius(value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    setCcdMotionThreshold
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_setCcdMotionThreshold
    (JNIEnv *env, jobject object, jlong objectId, jfloat value){
        btGhostObject* character = (btGhostObject*)objectId;
        character->setCcdMotionThreshold(value);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    getCcdSweptSphereRadius
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_getCcdSweptSphereRadius
    (JNIEnv *env, jobject object, jlong objectId){
        btGhostObject* character = (btGhostObject*)objectId;
        return character->getCcdSweptSphereRadius();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    getCcdMotionThreshold
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_getCcdMotionThreshold
    (JNIEnv *env, jobject object, jlong objectId){
        btGhostObject* character = (btGhostObject*)objectId;
        return character->getCcdMotionThreshold();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsCharacter
     * Method:    getCcdSquareMotionThreshold
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_PhysicsCharacter_getCcdSquareMotionThreshold
    (JNIEnv *env, jobject object, jlong objectId){
        btGhostObject* character = (btGhostObject*)objectId;
        return character->getCcdSquareMotionThreshold();
    }

#ifdef __cplusplus
}
#endif
