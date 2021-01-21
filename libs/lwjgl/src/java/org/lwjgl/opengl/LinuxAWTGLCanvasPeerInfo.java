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
package org.lwjgl.opengl;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;

/**
 * $Id: LinuxAWTGLCanvasPeerInfo.java,v 1.3 2005/05/04 20:59:36 cix_foo Exp $
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 1.3 $
 */
final class LinuxAWTGLCanvasPeerInfo extends LinuxPeerInfo {
	private final AWTGLCanvas canvas;
	private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();

	public LinuxAWTGLCanvasPeerInfo(AWTGLCanvas canvas) {
		this.canvas = canvas;
	}
	
	protected void doLockAndInitHandle() throws LWJGLException {
		int screen = LinuxCanvasImplementation.getScreenFromDevice(canvas.getGraphicsConfiguration().getDevice());
		nInitHandle(screen, awt_surface.lockAndGetHandle(canvas), getHandle());
	}
	private static native void nInitHandle(int screen, ByteBuffer surface_buffer, ByteBuffer peer_info_handle) throws LWJGLException;

	protected void doUnlock() throws LWJGLException {
		awt_surface.unlock();
	}
}
