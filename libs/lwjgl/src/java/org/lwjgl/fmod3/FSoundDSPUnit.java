/* 
 * Copyright (c) 2002-2004 LWJGL Project
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
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
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
package org.lwjgl.fmod3;

import java.nio.ByteBuffer;

/**
 * This class is a representation of a DSPUnit in FMod.
 * $Id: FSoundDSPUnit.java,v 1.3 2004/08/12 21:40:35 matzon Exp $
 * <br>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 1.3 $
 */
public class FSoundDSPUnit {
  /** Opaque handle to dsp unit */
  ByteBuffer dspHandle;
  
  /** DSP id, used for tracking which dsp needs to call which 
   * object (when entering from native side) */
  ByteBuffer dspTrackingID;
  
  /** ID for next dsp unit */
  static long nextDspTrackingID;
  
  /**
   * Creates a new FSoundDSPUnit
   * 
   * @param dspHandle handle to dsp unit
   */
  FSoundDSPUnit(ByteBuffer dspHandle) {
   this.dspHandle = dspHandle;
  }
  
  /**
   * Creates a new FSoundDSPUnit
   * 
   * @param dspHandle handle to dsp unit
   */
  FSoundDSPUnit(ByteBuffer dspHandle, ByteBuffer dspTrackingID) {
   this.dspHandle         = dspHandle;
   this.dspTrackingID     = dspTrackingID;
  }
  
  /**
   * @return Next dsp id
   */
  static long getNextId() {
    // To infinity and beyond!... well almost
    if(nextDspTrackingID == Long.MAX_VALUE) {
    	nextDspTrackingID = 0;
    }
  	return nextDspTrackingID++;
  }
}
