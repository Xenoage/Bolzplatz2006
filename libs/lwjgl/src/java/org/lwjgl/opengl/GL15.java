/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import java.nio.*;

public final class GL15 {
	public static final int GL_QUERY_RESULT_AVAILABLE = 0x8867;
	public static final int GL_QUERY_RESULT = 0x8866;
	public static final int GL_CURRENT_QUERY = 0x8865;
	public static final int GL_QUERY_COUNTER_BITS = 0x8864;
	public static final int GL_SAMPLES_PASSED = 0x8914;
	public static final int GL_BUFFER_MAP_POINTER = 0x88bd;
	public static final int GL_BUFFER_MAPPED = 0x88bc;
	public static final int GL_BUFFER_ACCESS = 0x88bb;
	public static final int GL_BUFFER_USAGE = 0x8765;
	public static final int GL_BUFFER_SIZE = 0x8764;
	public static final int GL_READ_WRITE = 0x88ba;
	public static final int GL_WRITE_ONLY = 0x88b9;
	public static final int GL_READ_ONLY = 0x88b8;
	public static final int GL_DYNAMIC_COPY = 0x88ea;
	public static final int GL_DYNAMIC_READ = 0x88e9;
	public static final int GL_DYNAMIC_DRAW = 0x88e8;
	public static final int GL_STATIC_COPY = 0x88e6;
	public static final int GL_STATIC_READ = 0x88e5;
	public static final int GL_STATIC_DRAW = 0x88e4;
	public static final int GL_STREAM_COPY = 0x88e2;
	public static final int GL_STREAM_READ = 0x88e1;
	public static final int GL_STREAM_DRAW = 0x88e0;
	public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889f;
	public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING = 0x889e;
	public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 0x889d;
	public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 0x889c;
	public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 0x889b;
	public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 0x889a;
	public static final int GL_INDEX_ARRAY_BUFFER_BINDING = 0x8899;
	public static final int GL_COLOR_ARRAY_BUFFER_BINDING = 0x8898;
	public static final int GL_NORMAL_ARRAY_BUFFER_BINDING = 0x8897;
	public static final int GL_VERTEX_ARRAY_BUFFER_BINDING = 0x8896;
	public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
	public static final int GL_ARRAY_BUFFER_BINDING = 0x8894;
	public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;
	public static final int GL_ARRAY_BUFFER = 0x8892;

	private GL15() {
	}


	public static void glGetQueryObjectu(int id, int pname, IntBuffer params) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetQueryObjectuiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetQueryObjectuiv(id, pname, params, params.position(), function_pointer);
	}
	private static native void nglGetQueryObjectuiv(int id, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetQueryObject(int id, int pname, IntBuffer params) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetQueryObjectiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetQueryObjectiv(id, pname, params, params.position(), function_pointer);
	}
	private static native void nglGetQueryObjectiv(int id, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetQuery(int target, int pname, IntBuffer params) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetQueryiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetQueryiv(target, pname, params, params.position(), function_pointer);
	}
	private static native void nglGetQueryiv(int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glEndQuery(int target) {
		long function_pointer = GLContext.getCapabilities().GL15_glEndQuery_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndQuery(target, function_pointer);
	}
	private static native void nglEndQuery(int target, long function_pointer);

	public static void glBeginQuery(int target, int id) {
		long function_pointer = GLContext.getCapabilities().GL15_glBeginQuery_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginQuery(target, id, function_pointer);
	}
	private static native void nglBeginQuery(int target, int id, long function_pointer);

	public static boolean glIsQuery(int id) {
		long function_pointer = GLContext.getCapabilities().GL15_glIsQuery_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsQuery(id, function_pointer);
		return __result;
	}
	private static native boolean nglIsQuery(int id, long function_pointer);

	public static void glDeleteQueries(IntBuffer ids) {
		long function_pointer = GLContext.getCapabilities().GL15_glDeleteQueries_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglDeleteQueries((ids.remaining()), ids, ids.position(), function_pointer);
	}
	private static native void nglDeleteQueries(int n, IntBuffer ids, int ids_position, long function_pointer);

	public static void glGenQueries(IntBuffer ids) {
		long function_pointer = GLContext.getCapabilities().GL15_glGenQueries_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglGenQueries((ids.remaining()), ids, ids.position(), function_pointer);
	}
	private static native void nglGenQueries(int n, IntBuffer ids, int ids_position, long function_pointer);

	public static java.nio.ByteBuffer glGetBufferPointer(int target, int pname, int result_size) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetBufferPointerv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		java.nio.ByteBuffer __result = nglGetBufferPointerv(target, pname, result_size, function_pointer);
		return __result;
	}
	private static native java.nio.ByteBuffer nglGetBufferPointerv(int target, int pname, int result_size, long function_pointer);

	public static void glGetBufferParameter(int target, int pname, IntBuffer params) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetBufferParameteriv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetBufferParameteriv(target, pname, params, params.position(), function_pointer);
	}
	private static native void nglGetBufferParameteriv(int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static boolean glUnmapBuffer(int target) {
		long function_pointer = GLContext.getCapabilities().GL15_glUnmapBuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglUnmapBuffer(target, function_pointer);
		return __result;
	}
	private static native boolean nglUnmapBuffer(int target, long function_pointer);

	/**
	 * glMapBuffer maps a gl vertex buffer buffer to a ByteBuffer. The oldBuffer argument can be null, in which case a new
	 * ByteBuffer will be created, pointing to the returned memory. If oldBuffer is non-null, it will be returned if it points to
	 * the same mapped memory, otherwise a new ByteBuffer is created. That way, an application will normally use glMapBuffer like
	 * this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapBuffer(..., ..., ..., null); ... // Another map on the same buffer
	 * mapped_buffer = glMapBuffer(..., ..., ..., mapped_buffer);
	 * @param result_size	The size of the buffer area.
	 * @param old_buffer	A ByteBuffer. If this argument points to the same address as the new mapping, it will be returned and no
	 *                  new buffer will be created. In that case, size is ignored.
	 * @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static java.nio.ByteBuffer glMapBuffer(int target, int access, int result_size, java.nio.ByteBuffer old_buffer) {
		long function_pointer = GLContext.getCapabilities().GL15_glMapBuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		java.nio.ByteBuffer __result = nglMapBuffer(target, access, result_size, old_buffer, function_pointer);
		return __result;
	}
	private static native java.nio.ByteBuffer nglMapBuffer(int target, int access, int result_size, java.nio.ByteBuffer old_buffer, long function_pointer);

	public static void glGetBufferSubData(int target, int offset, ByteBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining()), data, data.position(), function_pointer);
	}
	public static void glGetBufferSubData(int target, int offset, FloatBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
	}
	public static void glGetBufferSubData(int target, int offset, IntBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
	}
	public static void glGetBufferSubData(int target, int offset, ShortBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glGetBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining() << 1), data, data.position() << 1, function_pointer);
	}
	private static native void nglGetBufferSubData(int target, int offset, int size, Buffer data, int data_position, long function_pointer);

	public static void glBufferSubData(int target, int offset, ByteBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining()), data, data.position(), function_pointer);
	}
	public static void glBufferSubData(int target, int offset, FloatBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
	}
	public static void glBufferSubData(int target, int offset, IntBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
	}
	public static void glBufferSubData(int target, int offset, ShortBuffer data) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferSubData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 1), data, data.position() << 1, function_pointer);
	}
	private static native void nglBufferSubData(int target, int offset, int size, Buffer data, int data_position, long function_pointer);

	public static void glBufferData(int target, int size, int usage) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBufferData(target, size, null, 0, usage, function_pointer);
	}
	public static void glBufferData(int target, ByteBuffer data, int usage) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining()), data, data.position(), usage, function_pointer);
	}
	public static void glBufferData(int target, FloatBuffer data, int usage) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 2), data, data.position() << 2, usage, function_pointer);
	}
	public static void glBufferData(int target, IntBuffer data, int usage) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 2), data, data.position() << 2, usage, function_pointer);
	}
	public static void glBufferData(int target, ShortBuffer data, int usage) {
		long function_pointer = GLContext.getCapabilities().GL15_glBufferData_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 1), data, data.position() << 1, usage, function_pointer);
	}
	private static native void nglBufferData(int target, int size, Buffer data, int data_position, int usage, long function_pointer);

	public static boolean glIsBuffer(int buffer) {
		long function_pointer = GLContext.getCapabilities().GL15_glIsBuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsBuffer(buffer, function_pointer);
		return __result;
	}
	private static native boolean nglIsBuffer(int buffer, long function_pointer);

	public static void glGenBuffers(IntBuffer buffers) {
		long function_pointer = GLContext.getCapabilities().GL15_glGenBuffers_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglGenBuffers((buffers.remaining()), buffers, buffers.position(), function_pointer);
	}
	private static native void nglGenBuffers(int n, IntBuffer buffers, int buffers_position, long function_pointer);

	public static void glDeleteBuffers(IntBuffer buffers) {
		StateTracker.deleteBuffers(buffers);
		long function_pointer = GLContext.getCapabilities().GL15_glDeleteBuffers_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglDeleteBuffers((buffers.remaining()), buffers, buffers.position(), function_pointer);
	}
	private static native void nglDeleteBuffers(int n, IntBuffer buffers, int buffers_position, long function_pointer);

	public static void glBindBuffer(int target, int buffer) {
		StateTracker.bindBuffer(target, buffer);
		long function_pointer = GLContext.getCapabilities().GL15_glBindBuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBuffer(target, buffer, function_pointer);
	}
	private static native void nglBindBuffer(int target, int buffer, long function_pointer);
}
