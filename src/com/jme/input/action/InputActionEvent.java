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

package com.jme.input.action;

import java.util.ArrayList;

import com.jme.input.KeyInput;

/**
 * <code>InputActionEvent</code> defines an event that generates the
 * processing of a given InputAction. This event contains information about the
 * triggers that caused the event to take places as well as the list of names of
 * the other Actions that were to be processed at the same time.
 * 
 * @author Mark Powell
 * @version $Id: InputActionEvent.java,v 1.7 2006-01-13 19:39:39 renanse Exp $
 */
public class InputActionEvent {

    //the time of the event.
    private float time;

    /**
     * instantiates a default InputActionEvent object. The keys, eventList and
     * time are set to null or 0.
     *  
     */
    public InputActionEvent() {
    }

    /**
     * returns the time the event occured.
     * 
     * @return Returns the time.
     */
    public float getTime() {
        return time;
    }

    /**
     * sets the time the event occured.
     * 
     * @param time
     *            The time to set.
     */
    public void setTime(float time) {
        this.time = time;
    }


    /**
     * Usually triggerName is set to a button/axis name or command.
     *
     * @return current value of field triggerName
     */
    public String getTriggerName() {
        return this.triggerName;
    }

    /**
     * @see #getTriggerName
     */
    private String triggerName;

    /**
     * @param value new value
     */
    public void setTriggerName( final String value ) {
        this.triggerName = value;
    }

    /**
     * This list used to contain the keys of all actions invoked in one call of InputHandler.update.
     * @deprecated as it consumes time and memory to build the event list and it is of questionalbe use. Contact Irrisor
     *             if you used it (and to probably bring it back).
     */
    public ArrayList getEventList() {
        throw new UnsupportedOperationException( "As it consumes time and memory to build the event list and " +
                "it is of questionalbe use the event list was removed. Contact Irrisor\n" +
                "if you used it (and to probably bring it back)." );
    }

    /**
     * @deprecated use KeyInput.get().isKeyDown(key) instead
     */
    public boolean isKeyDown(int key) {
        //todo: remove this method in .11
        return KeyInput.get().isKeyDown(key);
    }

    /**
     * @return some character data associated with the event / button name, '\0' if not applicable.
     *         <br>example: typed keyboard character
     */
    public char getTriggerCharacter() {
        return this.triggerCharacter;
    }

    /**
     * @see #getTriggerCharacter
     */
    private char triggerCharacter;

    /**
     * @see #getTriggerCharacter
     *
     * @param value new value
     */
    public void setTriggerCharacter( final char value ) {
        this.triggerCharacter = value;
    }


    /**
     * name of the device that triggered this event, null if not applicable
     *
     * @return current value of field axisName
     */
    public String getTriggerDevice() {
        return this.triggerDevice;
    }

    /**
     * @see #getTriggerDevice()
     */
    private String triggerDevice;

    /**
     * @see #getTriggerDevice()
     *
     * @param value new value
     */
    public void setTriggerDevice( final String value ) {
        this.triggerDevice = value;
    }

    /**
     * @return index of the device part that caused the event, -1 if not applicable
     *         <br>example: mouse button index, joystick axis index
     */
    public int getTriggerIndex() {
        return this.triggerIndex;
    }

    /**
     * @see #getTriggerIndex
     */
    private int triggerIndex;

    /**
     * @see #getTriggerIndex
     *
     * @param value new value
     */
    public void setTriggerIndex( final int value ) {
        this.triggerIndex = value;
    }


    /**
     * @return new position of the device part that caused the event, default 0, range [-1;1]
     *         <br>example: joystick axis position
     */
    public float getTriggerPosition() {
        return this.triggerPosition;
    }

    /**
     * @see #getTriggerPosition
     */
    private float triggerPosition;

    /**
     * @see #getTriggerPosition
     *
     * @param value new value
     */
    public void setTriggerPosition( final float value ) {
        this.triggerPosition = value;
    }


    /**
     * @return position delta of the device part that caused the event, default 0, range [-1;1]
     *         <br>example: joystick axis delta
     */
    public float getTriggerDelta() {
        return this.triggerDelta;
    }

    /**
     * @see #getTriggerDelta
     */
    private float triggerDelta;

    /**
     * @see #getTriggerDelta
     *
     * @param value new value
     */
    public void setTriggerDelta( final float value ) {
        this.triggerDelta = value;
    }


    /**
     * @return true if a button was pressed, false if released, default: false
     *         <br>example: true if joystick button is pressed, false if joystick button is released
     */
    public boolean getTriggerPressed() {
        return this.triggerPressed;
    }

    /**
     * @see #getTriggerPressed
     */
    private boolean triggerPressed;

    /**
     * @see #getTriggerPressed
     *
     * @param value new value
     */
    public void setTriggerPressed( final boolean value ) {
        this.triggerPressed = value;
    }


    /**
     * @return true if the trigger that caused the event allows repeats
     * @see com.jme.input.InputHandler#addAction(InputAction, String, int, int, boolean)
     */
    public boolean getTriggerAllowsRepeats() {
        return this.triggerAllowsRepeats;
    }

    /**
     * @see #getTriggerAllowsRepeats()
     */
    private boolean triggerAllowsRepeats;

    /**
     * @see #getTriggerAllowsRepeats()
     *
     * @param value new value
     */
    public void setTriggerAllowsRepeats( final boolean value ) {
            this.triggerAllowsRepeats = value;
    }
}