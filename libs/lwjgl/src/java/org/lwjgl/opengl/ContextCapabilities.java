/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import java.util.Set;

public class ContextCapabilities {
	final StateTracker tracker;

	public final boolean GL_ARB_color_buffer_float;
	public final boolean GL_ARB_depth_texture;
	public final boolean GL_ARB_draw_buffers;
	public final boolean GL_ARB_fragment_program;
	public final boolean GL_ARB_fragment_program_shadow;
	public final boolean GL_ARB_fragment_shader;
	public final boolean GL_ARB_half_float_pixel;
	public final boolean GL_ARB_imaging;
	public final boolean GL_ARB_matrix_palette;
	public final boolean GL_ARB_multisample;
	public final boolean GL_ARB_multitexture;
	public final boolean GL_ARB_occlusion_query;
	public final boolean GL_ARB_pixel_buffer_object;
	public final boolean GL_ARB_point_parameters;
	public final boolean GL_ARB_point_sprite;
	public final boolean GL_ARB_shader_objects;
	public final boolean GL_ARB_shading_language_100;
	public final boolean GL_ARB_shadow;
	public final boolean GL_ARB_shadow_ambient;
	public final boolean GL_ARB_texture_border_clamp;
	public final boolean GL_ARB_texture_compression;
	public final boolean GL_ARB_texture_cube_map;
	public final boolean GL_ARB_texture_env_add;
	public final boolean GL_ARB_texture_env_combine;
	public final boolean GL_ARB_texture_env_crossbar;
	public final boolean GL_ARB_texture_env_dot3;
	public final boolean GL_ARB_texture_float;
	public final boolean GL_ARB_texture_mirrored_repeat;
	public final boolean GL_ARB_texture_non_power_of_two;
	public final boolean GL_ARB_texture_rectangle;
	public final boolean GL_ARB_transpose_matrix;
	public final boolean GL_ARB_vertex_blend;
	public final boolean GL_ARB_vertex_buffer_object;
	public final boolean GL_ARB_vertex_program;
	public final boolean GL_ARB_vertex_shader;
	public final boolean GL_ARB_window_pos;
	public final boolean GL_ATI_draw_buffers;
	public final boolean GL_ATI_element_array;
	public final boolean GL_ATI_envmap_bumpmap;
	public final boolean GL_ATI_fragment_shader;
	public final boolean GL_ATI_map_object_buffer;
	public final boolean GL_ATI_pn_triangles;
	public final boolean GL_ATI_separate_stencil;
	public final boolean GL_ATI_texture_compression_3dc;
	public final boolean GL_ATI_texture_float;
	public final boolean GL_ATI_texture_mirror_once;
	public final boolean GL_ATI_vertex_array_object;
	public final boolean GL_ATI_vertex_attrib_array_object;
	public final boolean GL_ATI_vertex_streams;
	public final boolean GL_EXT_abgr;
	public final boolean GL_EXT_bgra;
	public final boolean GL_EXT_blend_equation_separate;
	public final boolean GL_EXT_blend_func_separate;
	public final boolean GL_EXT_blend_subtract;
	public final boolean GL_EXT_cg_shader;
	public final boolean GL_EXT_compiled_vertex_array;
	public final boolean GL_EXT_depth_bounds_test;
	public final boolean GL_EXT_draw_range_elements;
	public final boolean GL_EXT_fog_coord;
	public final boolean GL_EXT_framebuffer_object;
	public final boolean GL_EXT_multi_draw_arrays;
	public final boolean GL_EXT_packed_pixels;
	public final boolean GL_EXT_paletted_texture;
	public final boolean GL_EXT_pixel_buffer_object;
	public final boolean GL_EXT_point_parameters;
	public final boolean GL_EXT_rescale_normal;
	public final boolean GL_EXT_secondary_color;
	public final boolean GL_EXT_separate_specular_color;
	public final boolean GL_EXT_shadow_funcs;
	public final boolean GL_EXT_shared_texture_palette;
	public final boolean GL_EXT_stencil_two_side;
	public final boolean GL_EXT_stencil_wrap;
	public final boolean GL_EXT_texture_3d;
	public final boolean GL_EXT_texture_compression_s3tc;
	public final boolean GL_EXT_texture_env_combine;
	public final boolean GL_EXT_texture_env_dot3;
	public final boolean GL_EXT_texture_filter_anisotropic;
	public final boolean GL_EXT_texture_lod_bias;
	public final boolean GL_EXT_texture_mirror_clamp;
	public final boolean GL_EXT_texture_rectangle;
	public final boolean GL_EXT_vertex_shader;
	public final boolean GL_EXT_vertex_weighting;
	public final boolean OpenGL11;
	public final boolean OpenGL12;
	public final boolean OpenGL13;
	public final boolean OpenGL14;
	public final boolean OpenGL15;
	public final boolean OpenGL20;
	public final boolean GL_HP_occlusion_test;
	public final boolean GL_IBM_rasterpos_clip;
	public final boolean GL_NV_blend_square;
	public final boolean GL_NV_copy_depth_to_color;
	public final boolean GL_NV_depth_clamp;
	public final boolean GL_NV_evaluators;
	public final boolean GL_NV_fence;
	public final boolean GL_NV_float_buffer;
	public final boolean GL_NV_fog_distance;
	public final boolean GL_NV_fragment_program;
	public final boolean GL_NV_fragment_program2;
	public final boolean GL_NV_fragment_program_option;
	public final boolean GL_NV_half_float;
	public final boolean GL_NV_light_max_exponent;
	public final boolean GL_NV_multisample_filter_hint;
	public final boolean GL_NV_occlusion_query;
	public final boolean GL_NV_packed_depth_stencil;
	public final boolean GL_NV_pixel_data_range;
	public final boolean GL_NV_point_sprite;
	public final boolean GL_NV_primitive_restart;
	public final boolean GL_NV_register_combiners;
	public final boolean GL_NV_register_combiners2;
	public final boolean GL_NV_texgen_reflection;
	public final boolean GL_NV_texture_compression_vtc;
	public final boolean GL_NV_texture_env_combine4;
	public final boolean GL_NV_texture_expand_normal;
	public final boolean GL_NV_texture_rectangle;
	public final boolean GL_NV_texture_shader;
	public final boolean GL_NV_texture_shader2;
	public final boolean GL_NV_texture_shader3;
	public final boolean GL_NV_vertex_array_range;
	public final boolean GL_NV_vertex_array_range2;
	public final boolean GL_NV_vertex_program;
	public final boolean GL_NV_vertex_program1_1;
	public final boolean GL_NV_vertex_program2;
	public final boolean GL_NV_vertex_program2_option;
	public final boolean GL_NV_vertex_program3;
	public final boolean GL_SUN_slice_accum;

	long ARB_buffer_object_glGetBufferPointervARB_pointer;
	long ARB_buffer_object_glGetBufferParameterivARB_pointer;
	long ARB_buffer_object_glUnmapBufferARB_pointer;
	long ARB_buffer_object_glMapBufferARB_pointer;
	long ARB_buffer_object_glGetBufferSubDataARB_pointer;
	long ARB_buffer_object_glBufferSubDataARB_pointer;
	long ARB_buffer_object_glBufferDataARB_pointer;
	long ARB_buffer_object_glIsBufferARB_pointer;
	long ARB_buffer_object_glGenBuffersARB_pointer;
	long ARB_buffer_object_glDeleteBuffersARB_pointer;
	long ARB_buffer_object_glBindBufferARB_pointer;
	long ARB_color_buffer_float_glClampColorARB_pointer;
	long ARB_draw_buffers_glDrawBuffersARB_pointer;
	long ARB_imaging_glGetSeparableFilter_pointer;
	long ARB_imaging_glSeparableFilter2D_pointer;
	long ARB_imaging_glGetConvolutionParameteriv_pointer;
	long ARB_imaging_glGetConvolutionParameterfv_pointer;
	long ARB_imaging_glGetConvolutionFilter_pointer;
	long ARB_imaging_glCopyConvolutionFilter2D_pointer;
	long ARB_imaging_glCopyConvolutionFilter1D_pointer;
	long ARB_imaging_glConvolutionParameteriv_pointer;
	long ARB_imaging_glConvolutionParameteri_pointer;
	long ARB_imaging_glConvolutionParameterfv_pointer;
	long ARB_imaging_glConvolutionParameterf_pointer;
	long ARB_imaging_glConvolutionFilter2D_pointer;
	long ARB_imaging_glConvolutionFilter1D_pointer;
	long ARB_imaging_glGetMinmaxParameteriv_pointer;
	long ARB_imaging_glGetMinmaxParameterfv_pointer;
	long ARB_imaging_glGetMinmax_pointer;
	long ARB_imaging_glResetMinmax_pointer;
	long ARB_imaging_glMinmax_pointer;
	long ARB_imaging_glGetHistogramParameteriv_pointer;
	long ARB_imaging_glGetHistogramParameterfv_pointer;
	long ARB_imaging_glGetHistogram_pointer;
	long ARB_imaging_glResetHistogram_pointer;
	long ARB_imaging_glHistogram_pointer;
	long ARB_imaging_glBlendColor_pointer;
	long ARB_imaging_glBlendEquation_pointer;
	long ARB_imaging_glGetColorTableParameterfv_pointer;
	long ARB_imaging_glGetColorTableParameteriv_pointer;
	long ARB_imaging_glGetColorTable_pointer;
	long ARB_imaging_glCopyColorTable_pointer;
	long ARB_imaging_glCopyColorSubTable_pointer;
	long ARB_imaging_glColorTableParameterfv_pointer;
	long ARB_imaging_glColorTableParameteriv_pointer;
	long ARB_imaging_glColorSubTable_pointer;
	long ARB_imaging_glColorTable_pointer;
	long ARB_matrix_palette_glMatrixIndexuivARB_pointer;
	long ARB_matrix_palette_glMatrixIndexusvARB_pointer;
	long ARB_matrix_palette_glMatrixIndexubvARB_pointer;
	long ARB_matrix_palette_glMatrixIndexPointerARB_pointer;
	long ARB_matrix_palette_glCurrentPaletteMatrixARB_pointer;
	long ARB_multisample_glSampleCoverageARB_pointer;
	long ARB_multitexture_glMultiTexCoord4sARB_pointer;
	long ARB_multitexture_glMultiTexCoord4iARB_pointer;
	long ARB_multitexture_glMultiTexCoord4fARB_pointer;
	long ARB_multitexture_glMultiTexCoord3sARB_pointer;
	long ARB_multitexture_glMultiTexCoord3iARB_pointer;
	long ARB_multitexture_glMultiTexCoord3fARB_pointer;
	long ARB_multitexture_glMultiTexCoord2sARB_pointer;
	long ARB_multitexture_glMultiTexCoord2iARB_pointer;
	long ARB_multitexture_glMultiTexCoord2fARB_pointer;
	long ARB_multitexture_glMultiTexCoord1sARB_pointer;
	long ARB_multitexture_glMultiTexCoord1iARB_pointer;
	long ARB_multitexture_glMultiTexCoord1fARB_pointer;
	long ARB_multitexture_glActiveTextureARB_pointer;
	long ARB_multitexture_glClientActiveTextureARB_pointer;
	long ARB_occlusion_query_glGetQueryObjectuivARB_pointer;
	long ARB_occlusion_query_glGetQueryObjectivARB_pointer;
	long ARB_occlusion_query_glGetQueryivARB_pointer;
	long ARB_occlusion_query_glEndQueryARB_pointer;
	long ARB_occlusion_query_glBeginQueryARB_pointer;
	long ARB_occlusion_query_glIsQueryARB_pointer;
	long ARB_occlusion_query_glDeleteQueriesARB_pointer;
	long ARB_occlusion_query_glGenQueriesARB_pointer;
	long ARB_point_parameters_glPointParameterfvARB_pointer;
	long ARB_point_parameters_glPointParameterfARB_pointer;
	long ARB_program_glIsProgramARB_pointer;
	long ARB_program_glGetProgramStringARB_pointer;
	long ARB_program_glGetProgramivARB_pointer;
	long ARB_program_glGetProgramLocalParameterfvARB_pointer;
	long ARB_program_glGetProgramEnvParameterfvARB_pointer;
	long ARB_program_glProgramLocalParameter4fvARB_pointer;
	long ARB_program_glProgramLocalParameter4fARB_pointer;
	long ARB_program_glProgramEnvParameter4fvARB_pointer;
	long ARB_program_glProgramEnvParameter4fARB_pointer;
	long ARB_program_glGenProgramsARB_pointer;
	long ARB_program_glDeleteProgramsARB_pointer;
	long ARB_program_glBindProgramARB_pointer;
	long ARB_program_glProgramStringARB_pointer;
	long ARB_shader_objects_glGetShaderSourceARB_pointer;
	long ARB_shader_objects_glGetUniformivARB_pointer;
	long ARB_shader_objects_glGetUniformfvARB_pointer;
	long ARB_shader_objects_glGetActiveUniformARB_pointer;
	long ARB_shader_objects_glGetUniformLocationARB_pointer;
	long ARB_shader_objects_glGetAttachedObjectsARB_pointer;
	long ARB_shader_objects_glGetInfoLogARB_pointer;
	long ARB_shader_objects_glGetObjectParameterivARB_pointer;
	long ARB_shader_objects_glGetObjectParameterfvARB_pointer;
	long ARB_shader_objects_glUniformMatrix4fvARB_pointer;
	long ARB_shader_objects_glUniformMatrix3fvARB_pointer;
	long ARB_shader_objects_glUniformMatrix2fvARB_pointer;
	long ARB_shader_objects_glUniform4ivARB_pointer;
	long ARB_shader_objects_glUniform3ivARB_pointer;
	long ARB_shader_objects_glUniform2ivARB_pointer;
	long ARB_shader_objects_glUniform1ivARB_pointer;
	long ARB_shader_objects_glUniform4fvARB_pointer;
	long ARB_shader_objects_glUniform3fvARB_pointer;
	long ARB_shader_objects_glUniform2fvARB_pointer;
	long ARB_shader_objects_glUniform1fvARB_pointer;
	long ARB_shader_objects_glUniform4iARB_pointer;
	long ARB_shader_objects_glUniform3iARB_pointer;
	long ARB_shader_objects_glUniform2iARB_pointer;
	long ARB_shader_objects_glUniform1iARB_pointer;
	long ARB_shader_objects_glUniform4fARB_pointer;
	long ARB_shader_objects_glUniform3fARB_pointer;
	long ARB_shader_objects_glUniform2fARB_pointer;
	long ARB_shader_objects_glUniform1fARB_pointer;
	long ARB_shader_objects_glValidateProgramARB_pointer;
	long ARB_shader_objects_glUseProgramObjectARB_pointer;
	long ARB_shader_objects_glLinkProgramARB_pointer;
	long ARB_shader_objects_glAttachObjectARB_pointer;
	long ARB_shader_objects_glCreateProgramObjectARB_pointer;
	long ARB_shader_objects_glCompileShaderARB_pointer;
	long ARB_shader_objects_glShaderSourceARB_pointer;
	long ARB_shader_objects_glCreateShaderObjectARB_pointer;
	long ARB_shader_objects_glDetachObjectARB_pointer;
	long ARB_shader_objects_glGetHandleARB_pointer;
	long ARB_shader_objects_glDeleteObjectARB_pointer;
	long ARB_texture_compression_glGetCompressedTexImageARB_pointer;
	long ARB_texture_compression_glCompressedTexSubImage3DARB_pointer;
	long ARB_texture_compression_glCompressedTexSubImage2DARB_pointer;
	long ARB_texture_compression_glCompressedTexSubImage1DARB_pointer;
	long ARB_texture_compression_glCompressedTexImage3DARB_pointer;
	long ARB_texture_compression_glCompressedTexImage2DARB_pointer;
	long ARB_texture_compression_glCompressedTexImage1DARB_pointer;
	long ARB_transpose_matrix_glMultTransposeMatrixfARB_pointer;
	long ARB_transpose_matrix_glLoadTransposeMatrixfARB_pointer;
	long ARB_vertex_blend_glVertexBlendARB_pointer;
	long ARB_vertex_blend_glWeightPointerARB_pointer;
	long ARB_vertex_blend_glWeightuivARB_pointer;
	long ARB_vertex_blend_glWeightusvARB_pointer;
	long ARB_vertex_blend_glWeightubvARB_pointer;
	long ARB_vertex_blend_glWeightfvARB_pointer;
	long ARB_vertex_blend_glWeightivARB_pointer;
	long ARB_vertex_blend_glWeightsvARB_pointer;
	long ARB_vertex_blend_glWeightbvARB_pointer;
	long ARB_vertex_program_glGetVertexAttribPointervARB_pointer;
	long ARB_vertex_program_glGetVertexAttribivARB_pointer;
	long ARB_vertex_program_glGetVertexAttribfvARB_pointer;
	long ARB_vertex_program_glDisableVertexAttribArrayARB_pointer;
	long ARB_vertex_program_glEnableVertexAttribArrayARB_pointer;
	long ARB_vertex_program_glVertexAttribPointerARB_pointer;
	long ARB_vertex_program_glVertexAttrib4NubARB_pointer;
	long ARB_vertex_program_glVertexAttrib4fARB_pointer;
	long ARB_vertex_program_glVertexAttrib4sARB_pointer;
	long ARB_vertex_program_glVertexAttrib3fARB_pointer;
	long ARB_vertex_program_glVertexAttrib3sARB_pointer;
	long ARB_vertex_program_glVertexAttrib2fARB_pointer;
	long ARB_vertex_program_glVertexAttrib2sARB_pointer;
	long ARB_vertex_program_glVertexAttrib1fARB_pointer;
	long ARB_vertex_program_glVertexAttrib1sARB_pointer;
	long ARB_vertex_shader_glGetAttribLocationARB_pointer;
	long ARB_vertex_shader_glGetActiveAttribARB_pointer;
	long ARB_vertex_shader_glBindAttribLocationARB_pointer;
	long ARB_window_pos_glWindowPos3sARB_pointer;
	long ARB_window_pos_glWindowPos3iARB_pointer;
	long ARB_window_pos_glWindowPos3fARB_pointer;
	long ARB_window_pos_glWindowPos2sARB_pointer;
	long ARB_window_pos_glWindowPos2iARB_pointer;
	long ARB_window_pos_glWindowPos2fARB_pointer;
	long ATI_draw_buffers_glDrawBuffersATI_pointer;
	long ATI_element_array_glDrawRangeElementArrayATI_pointer;
	long ATI_element_array_glDrawElementArrayATI_pointer;
	long ATI_element_array_glElementPointerATI_pointer;
	long ATI_envmap_bumpmap_glGetTexBumpParameterivATI_pointer;
	long ATI_envmap_bumpmap_glGetTexBumpParameterfvATI_pointer;
	long ATI_envmap_bumpmap_glTexBumpParameterivATI_pointer;
	long ATI_envmap_bumpmap_glTexBumpParameterfvATI_pointer;
	long ATI_fragment_shader_glSetFragmentShaderConstantATI_pointer;
	long ATI_fragment_shader_glAlphaFragmentOp3ATI_pointer;
	long ATI_fragment_shader_glAlphaFragmentOp2ATI_pointer;
	long ATI_fragment_shader_glAlphaFragmentOp1ATI_pointer;
	long ATI_fragment_shader_glColorFragmentOp3ATI_pointer;
	long ATI_fragment_shader_glColorFragmentOp2ATI_pointer;
	long ATI_fragment_shader_glColorFragmentOp1ATI_pointer;
	long ATI_fragment_shader_glSampleMapATI_pointer;
	long ATI_fragment_shader_glPassTexCoordATI_pointer;
	long ATI_fragment_shader_glEndFragmentShaderATI_pointer;
	long ATI_fragment_shader_glBeginFragmentShaderATI_pointer;
	long ATI_fragment_shader_glDeleteFragmentShaderATI_pointer;
	long ATI_fragment_shader_glBindFragmentShaderATI_pointer;
	long ATI_fragment_shader_glGenFragmentShadersATI_pointer;
	long ATI_map_object_buffer_glUnmapObjectBufferATI_pointer;
	long ATI_map_object_buffer_glMapObjectBufferATI_pointer;
	long ATI_pn_triangles_glPNTrianglesiATI_pointer;
	long ATI_pn_triangles_glPNTrianglesfATI_pointer;
	long ATI_separate_stencil_glStencilFuncSeparateATI_pointer;
	long ATI_separate_stencil_glStencilOpSeparateATI_pointer;
	long ATI_vertex_array_object_glGetVariantArrayObjectivATI_pointer;
	long ATI_vertex_array_object_glGetVariantArrayObjectfvATI_pointer;
	long ATI_vertex_array_object_glVariantArrayObjectATI_pointer;
	long ATI_vertex_array_object_glGetArrayObjectivATI_pointer;
	long ATI_vertex_array_object_glGetArrayObjectfvATI_pointer;
	long ATI_vertex_array_object_glArrayObjectATI_pointer;
	long ATI_vertex_array_object_glFreeObjectBufferATI_pointer;
	long ATI_vertex_array_object_glGetObjectBufferivATI_pointer;
	long ATI_vertex_array_object_glGetObjectBufferfvATI_pointer;
	long ATI_vertex_array_object_glUpdateObjectBufferATI_pointer;
	long ATI_vertex_array_object_glIsObjectBufferATI_pointer;
	long ATI_vertex_array_object_glNewObjectBufferATI_pointer;
	long ATI_vertex_attrib_array_object_glGetVertexAttribArrayObjectivATI_pointer;
	long ATI_vertex_attrib_array_object_glGetVertexAttribArrayObjectfvATI_pointer;
	long ATI_vertex_attrib_array_object_glVertexAttribArrayObjectATI_pointer;
	long ATI_vertex_streams_glVertexBlendEnviATI_pointer;
	long ATI_vertex_streams_glVertexBlendEnvfATI_pointer;
	long ATI_vertex_streams_glClientActiveVertexStreamATI_pointer;
	long ATI_vertex_streams_glNormalStream3sATI_pointer;
	long ATI_vertex_streams_glNormalStream3iATI_pointer;
	long ATI_vertex_streams_glNormalStream3fATI_pointer;
	long ATI_vertex_streams_glNormalStream3bATI_pointer;
	long ATI_vertex_streams_glVertexStream4sATI_pointer;
	long ATI_vertex_streams_glVertexStream4iATI_pointer;
	long ATI_vertex_streams_glVertexStream4fATI_pointer;
	long ATI_vertex_streams_glVertexStream3sATI_pointer;
	long ATI_vertex_streams_glVertexStream3iATI_pointer;
	long ATI_vertex_streams_glVertexStream3fATI_pointer;
	long ATI_vertex_streams_glVertexStream2sATI_pointer;
	long ATI_vertex_streams_glVertexStream2iATI_pointer;
	long ATI_vertex_streams_glVertexStream2fATI_pointer;
	long EXT_blend_equation_separate_glBlendEquationSeparateEXT_pointer;
	long EXT_blend_func_separate_glBlendFuncSeparateEXT_pointer;
	long EXT_compiled_vertex_array_glUnlockArraysEXT_pointer;
	long EXT_compiled_vertex_array_glLockArraysEXT_pointer;
	long EXT_depth_bounds_test_glDepthBoundsEXT_pointer;
	long EXT_draw_range_elements_glDrawRangeElementsEXT_pointer;
	long EXT_fog_coord_glFogCoordPointerEXT_pointer;
	long EXT_fog_coord_glFogCoordfEXT_pointer;
	long EXT_framebuffer_object_glGenerateMipmapEXT_pointer;
	long EXT_framebuffer_object_glGetFramebufferAttachmentParameterivEXT_pointer;
	long EXT_framebuffer_object_glFramebufferRenderbufferEXT_pointer;
	long EXT_framebuffer_object_glFramebufferTexture3DEXT_pointer;
	long EXT_framebuffer_object_glFramebufferTexture2DEXT_pointer;
	long EXT_framebuffer_object_glFramebufferTexture1DEXT_pointer;
	long EXT_framebuffer_object_glCheckFramebufferStatusEXT_pointer;
	long EXT_framebuffer_object_glGenFramebuffersEXT_pointer;
	long EXT_framebuffer_object_glDeleteFramebuffersEXT_pointer;
	long EXT_framebuffer_object_glBindFramebufferEXT_pointer;
	long EXT_framebuffer_object_glIsFramebufferEXT_pointer;
	long EXT_framebuffer_object_glGetRenderbufferParameterivEXT_pointer;
	long EXT_framebuffer_object_glRenderbufferStorageEXT_pointer;
	long EXT_framebuffer_object_glGenRenderbuffersEXT_pointer;
	long EXT_framebuffer_object_glDeleteRenderbuffersEXT_pointer;
	long EXT_framebuffer_object_glBindRenderbufferEXT_pointer;
	long EXT_framebuffer_object_glIsRenderbufferEXT_pointer;
	long EXT_multi_draw_arrays_glMultiDrawArraysEXT_pointer;
	long EXT_paletted_texture_glGetColorTableParameterfvEXT_pointer;
	long EXT_paletted_texture_glGetColorTableParameterivEXT_pointer;
	long EXT_paletted_texture_glGetColorTableEXT_pointer;
	long EXT_paletted_texture_glColorSubTableEXT_pointer;
	long EXT_paletted_texture_glColorTableEXT_pointer;
	long EXT_point_parameters_glPointParameterfvEXT_pointer;
	long EXT_point_parameters_glPointParameterfEXT_pointer;
	long EXT_secondary_color_glSecondaryColorPointerEXT_pointer;
	long EXT_secondary_color_glSecondaryColor3ubEXT_pointer;
	long EXT_secondary_color_glSecondaryColor3fEXT_pointer;
	long EXT_secondary_color_glSecondaryColor3bEXT_pointer;
	long EXT_stencil_two_side_glActiveStencilFaceEXT_pointer;
	long EXT_vertex_shader_glGetLocalConstantFloatvEXT_pointer;
	long EXT_vertex_shader_glGetLocalConstantIntegervEXT_pointer;
	long EXT_vertex_shader_glGetLocalConstantBooleanvEXT_pointer;
	long EXT_vertex_shader_glGetInvariantFloatvEXT_pointer;
	long EXT_vertex_shader_glGetInvariantIntegervEXT_pointer;
	long EXT_vertex_shader_glGetInvariantBooleanvEXT_pointer;
	long EXT_vertex_shader_glGetVariantPointervEXT_pointer;
	long EXT_vertex_shader_glGetVariantFloatvEXT_pointer;
	long EXT_vertex_shader_glGetVariantIntegervEXT_pointer;
	long EXT_vertex_shader_glGetVariantBooleanvEXT_pointer;
	long EXT_vertex_shader_glIsVariantEnabledEXT_pointer;
	long EXT_vertex_shader_glBindParameterEXT_pointer;
	long EXT_vertex_shader_glBindTextureUnitParameterEXT_pointer;
	long EXT_vertex_shader_glBindTexGenParameterEXT_pointer;
	long EXT_vertex_shader_glBindMaterialParameterEXT_pointer;
	long EXT_vertex_shader_glBindLightParameterEXT_pointer;
	long EXT_vertex_shader_glDisableVariantClientStateEXT_pointer;
	long EXT_vertex_shader_glEnableVariantClientStateEXT_pointer;
	long EXT_vertex_shader_glVariantPointerEXT_pointer;
	long EXT_vertex_shader_glVariantuivEXT_pointer;
	long EXT_vertex_shader_glVariantusvEXT_pointer;
	long EXT_vertex_shader_glVariantubvEXT_pointer;
	long EXT_vertex_shader_glVariantfvEXT_pointer;
	long EXT_vertex_shader_glVariantivEXT_pointer;
	long EXT_vertex_shader_glVariantsvEXT_pointer;
	long EXT_vertex_shader_glVariantbvEXT_pointer;
	long EXT_vertex_shader_glSetLocalConstantEXT_pointer;
	long EXT_vertex_shader_glSetInvariantEXT_pointer;
	long EXT_vertex_shader_glGenSymbolsEXT_pointer;
	long EXT_vertex_shader_glExtractComponentEXT_pointer;
	long EXT_vertex_shader_glInsertComponentEXT_pointer;
	long EXT_vertex_shader_glWriteMaskEXT_pointer;
	long EXT_vertex_shader_glSwizzleEXT_pointer;
	long EXT_vertex_shader_glShaderOp3EXT_pointer;
	long EXT_vertex_shader_glShaderOp2EXT_pointer;
	long EXT_vertex_shader_glShaderOp1EXT_pointer;
	long EXT_vertex_shader_glDeleteVertexShaderEXT_pointer;
	long EXT_vertex_shader_glGenVertexShadersEXT_pointer;
	long EXT_vertex_shader_glBindVertexShaderEXT_pointer;
	long EXT_vertex_shader_glEndVertexShaderEXT_pointer;
	long EXT_vertex_shader_glBeginVertexShaderEXT_pointer;
	long EXT_vertex_weighting_glVertexWeightPointerEXT_pointer;
	long EXT_vertex_weighting_glVertexWeightfEXT_pointer;
	long GL11_glViewport_pointer;
	long GL11_glStencilMask_pointer;
	long GL11_glStencilOp_pointer;
	long GL11_glTexCoord4f_pointer;
	long GL11_glTexCoord3f_pointer;
	long GL11_glTexCoord2f_pointer;
	long GL11_glTexCoord1f_pointer;
	long GL11_glTexCoordPointer_pointer;
	long GL11_glTexEnviv_pointer;
	long GL11_glTexEnvfv_pointer;
	long GL11_glTexEnvi_pointer;
	long GL11_glTexEnvf_pointer;
	long GL11_glTexGeniv_pointer;
	long GL11_glTexGeni_pointer;
	long GL11_glTexGenfv_pointer;
	long GL11_glTexGenf_pointer;
	long GL11_glTexParameteriv_pointer;
	long GL11_glTexParameterfv_pointer;
	long GL11_glTexParameteri_pointer;
	long GL11_glTexParameterf_pointer;
	long GL11_glTexSubImage2D_pointer;
	long GL11_glTexSubImage1D_pointer;
	long GL11_glTexImage2D_pointer;
	long GL11_glTexImage1D_pointer;
	long GL11_glTranslatef_pointer;
	long GL11_glVertex4i_pointer;
	long GL11_glVertex4f_pointer;
	long GL11_glVertex3i_pointer;
	long GL11_glVertex3f_pointer;
	long GL11_glVertex2i_pointer;
	long GL11_glVertex2f_pointer;
	long GL11_glVertexPointer_pointer;
	long GL11_glStencilFunc_pointer;
	long GL11_glPopAttrib_pointer;
	long GL11_glPushAttrib_pointer;
	long GL11_glPopClientAttrib_pointer;
	long GL11_glPushClientAttrib_pointer;
	long GL11_glPopMatrix_pointer;
	long GL11_glPushMatrix_pointer;
	long GL11_glPopName_pointer;
	long GL11_glPushName_pointer;
	long GL11_glRasterPos4i_pointer;
	long GL11_glRasterPos4f_pointer;
	long GL11_glRasterPos3i_pointer;
	long GL11_glRasterPos3f_pointer;
	long GL11_glRasterPos2i_pointer;
	long GL11_glRasterPos2f_pointer;
	long GL11_glReadBuffer_pointer;
	long GL11_glReadPixels_pointer;
	long GL11_glRecti_pointer;
	long GL11_glRectf_pointer;
	long GL11_glRenderMode_pointer;
	long GL11_glRotatef_pointer;
	long GL11_glScalef_pointer;
	long GL11_glScissor_pointer;
	long GL11_glSelectBuffer_pointer;
	long GL11_glShadeModel_pointer;
	long GL11_glMultMatrixf_pointer;
	long GL11_glEndList_pointer;
	long GL11_glNewList_pointer;
	long GL11_glNormal3i_pointer;
	long GL11_glNormal3f_pointer;
	long GL11_glNormal3b_pointer;
	long GL11_glNormalPointer_pointer;
	long GL11_glOrtho_pointer;
	long GL11_glPassThrough_pointer;
	long GL11_glPixelMapusv_pointer;
	long GL11_glPixelMapuiv_pointer;
	long GL11_glPixelMapfv_pointer;
	long GL11_glPixelStorei_pointer;
	long GL11_glPixelStoref_pointer;
	long GL11_glPixelTransferi_pointer;
	long GL11_glPixelTransferf_pointer;
	long GL11_glPixelZoom_pointer;
	long GL11_glPointSize_pointer;
	long GL11_glPolygonMode_pointer;
	long GL11_glPolygonOffset_pointer;
	long GL11_glPolygonStipple_pointer;
	long GL11_glMatrixMode_pointer;
	long GL11_glIsTexture_pointer;
	long GL11_glLightiv_pointer;
	long GL11_glLightfv_pointer;
	long GL11_glLighti_pointer;
	long GL11_glLightf_pointer;
	long GL11_glLightModeliv_pointer;
	long GL11_glLightModelfv_pointer;
	long GL11_glLightModeli_pointer;
	long GL11_glLightModelf_pointer;
	long GL11_glLineStipple_pointer;
	long GL11_glLineWidth_pointer;
	long GL11_glListBase_pointer;
	long GL11_glLoadIdentity_pointer;
	long GL11_glLoadMatrixf_pointer;
	long GL11_glLoadName_pointer;
	long GL11_glLogicOp_pointer;
	long GL11_glMap1f_pointer;
	long GL11_glMap2f_pointer;
	long GL11_glMapGrid2f_pointer;
	long GL11_glMapGrid1f_pointer;
	long GL11_glMaterialiv_pointer;
	long GL11_glMaterialfv_pointer;
	long GL11_glMateriali_pointer;
	long GL11_glMaterialf_pointer;
	long GL11_glIsList_pointer;
	long GL11_glGetPolygonStipple_pointer;
	long GL11_glGetString_pointer;
	long GL11_glGetTexEnvfv_pointer;
	long GL11_glGetTexEnviv_pointer;
	long GL11_glGetTexGenfv_pointer;
	long GL11_glGetTexGeniv_pointer;
	long GL11_glGetTexImage_pointer;
	long GL11_glGetTexLevelParameteriv_pointer;
	long GL11_glGetTexLevelParameterfv_pointer;
	long GL11_glGetTexParameteriv_pointer;
	long GL11_glGetTexParameterfv_pointer;
	long GL11_glHint_pointer;
	long GL11_glInitNames_pointer;
	long GL11_glInterleavedArrays_pointer;
	long GL11_glIsEnabled_pointer;
	long GL11_glGetPointerv_pointer;
	long GL11_glFinish_pointer;
	long GL11_glFlush_pointer;
	long GL11_glFogiv_pointer;
	long GL11_glFogfv_pointer;
	long GL11_glFogi_pointer;
	long GL11_glFogf_pointer;
	long GL11_glFrontFace_pointer;
	long GL11_glFrustum_pointer;
	long GL11_glGenLists_pointer;
	long GL11_glGenTextures_pointer;
	long GL11_glGetIntegerv_pointer;
	long GL11_glGetFloatv_pointer;
	long GL11_glGetDoublev_pointer;
	long GL11_glGetBooleanv_pointer;
	long GL11_glGetClipPlane_pointer;
	long GL11_glGetError_pointer;
	long GL11_glGetLightiv_pointer;
	long GL11_glGetLightfv_pointer;
	long GL11_glGetMapiv_pointer;
	long GL11_glGetMapfv_pointer;
	long GL11_glGetMaterialiv_pointer;
	long GL11_glGetMaterialfv_pointer;
	long GL11_glGetPixelMapusv_pointer;
	long GL11_glGetPixelMapuiv_pointer;
	long GL11_glGetPixelMapfv_pointer;
	long GL11_glFeedbackBuffer_pointer;
	long GL11_glDepthFunc_pointer;
	long GL11_glDepthMask_pointer;
	long GL11_glDepthRange_pointer;
	long GL11_glDrawArrays_pointer;
	long GL11_glDrawBuffer_pointer;
	long GL11_glDrawElements_pointer;
	long GL11_glDrawPixels_pointer;
	long GL11_glEdgeFlag_pointer;
	long GL11_glEdgeFlagPointer_pointer;
	long GL11_glDisable_pointer;
	long GL11_glEnable_pointer;
	long GL11_glDisableClientState_pointer;
	long GL11_glEnableClientState_pointer;
	long GL11_glEvalCoord2f_pointer;
	long GL11_glEvalCoord1f_pointer;
	long GL11_glEvalMesh2_pointer;
	long GL11_glEvalMesh1_pointer;
	long GL11_glEvalPoint2_pointer;
	long GL11_glEvalPoint1_pointer;
	long GL11_glClearIndex_pointer;
	long GL11_glClearStencil_pointer;
	long GL11_glClipPlane_pointer;
	long GL11_glColor4ub_pointer;
	long GL11_glColor4f_pointer;
	long GL11_glColor4b_pointer;
	long GL11_glColor3ub_pointer;
	long GL11_glColor3f_pointer;
	long GL11_glColor3b_pointer;
	long GL11_glColorMask_pointer;
	long GL11_glColorMaterial_pointer;
	long GL11_glColorPointer_pointer;
	long GL11_glCopyPixels_pointer;
	long GL11_glCopyTexImage1D_pointer;
	long GL11_glCopyTexImage2D_pointer;
	long GL11_glCopyTexSubImage1D_pointer;
	long GL11_glCopyTexSubImage2D_pointer;
	long GL11_glCullFace_pointer;
	long GL11_glDeleteTextures_pointer;
	long GL11_glDeleteLists_pointer;
	long GL11_glClearDepth_pointer;
	long GL11_glArrayElement_pointer;
	long GL11_glEnd_pointer;
	long GL11_glBegin_pointer;
	long GL11_glBindTexture_pointer;
	long GL11_glBitmap_pointer;
	long GL11_glBlendFunc_pointer;
	long GL11_glCallList_pointer;
	long GL11_glCallLists_pointer;
	long GL11_glClear_pointer;
	long GL11_glClearAccum_pointer;
	long GL11_glClearColor_pointer;
	long GL11_glAlphaFunc_pointer;
	long GL11_glAccum_pointer;
	long GL12_glCopyTexSubImage3D_pointer;
	long GL12_glTexSubImage3D_pointer;
	long GL12_glTexImage3D_pointer;
	long GL12_glDrawRangeElements_pointer;
	long GL13_glSampleCoverage_pointer;
	long GL13_glMultTransposeMatrixf_pointer;
	long GL13_glLoadTransposeMatrixf_pointer;
	long GL13_glMultiTexCoord4f_pointer;
	long GL13_glMultiTexCoord3f_pointer;
	long GL13_glMultiTexCoord2f_pointer;
	long GL13_glMultiTexCoord1f_pointer;
	long GL13_glGetCompressedTexImage_pointer;
	long GL13_glCompressedTexSubImage3D_pointer;
	long GL13_glCompressedTexSubImage2D_pointer;
	long GL13_glCompressedTexSubImage1D_pointer;
	long GL13_glCompressedTexImage3D_pointer;
	long GL13_glCompressedTexImage2D_pointer;
	long GL13_glCompressedTexImage1D_pointer;
	long GL13_glClientActiveTexture_pointer;
	long GL13_glActiveTexture_pointer;
	long GL14_glWindowPos3i_pointer;
	long GL14_glWindowPos3f_pointer;
	long GL14_glWindowPos2i_pointer;
	long GL14_glWindowPos2f_pointer;
	long GL14_glBlendFuncSeparate_pointer;
	long GL14_glSecondaryColorPointer_pointer;
	long GL14_glSecondaryColor3ub_pointer;
	long GL14_glSecondaryColor3f_pointer;
	long GL14_glSecondaryColor3b_pointer;
	long GL14_glPointParameterfv_pointer;
	long GL14_glPointParameteriv_pointer;
	long GL14_glPointParameterf_pointer;
	long GL14_glPointParameteri_pointer;
	long GL14_glMultiDrawArrays_pointer;
	long GL14_glFogCoordPointer_pointer;
	long GL14_glFogCoordf_pointer;
	long GL14_glBlendColor_pointer;
	long GL14_glBlendEquation_pointer;
	long GL15_glGetQueryObjectuiv_pointer;
	long GL15_glGetQueryObjectiv_pointer;
	long GL15_glGetQueryiv_pointer;
	long GL15_glEndQuery_pointer;
	long GL15_glBeginQuery_pointer;
	long GL15_glIsQuery_pointer;
	long GL15_glDeleteQueries_pointer;
	long GL15_glGenQueries_pointer;
	long GL15_glGetBufferPointerv_pointer;
	long GL15_glGetBufferParameteriv_pointer;
	long GL15_glUnmapBuffer_pointer;
	long GL15_glMapBuffer_pointer;
	long GL15_glGetBufferSubData_pointer;
	long GL15_glBufferSubData_pointer;
	long GL15_glBufferData_pointer;
	long GL15_glIsBuffer_pointer;
	long GL15_glGenBuffers_pointer;
	long GL15_glDeleteBuffers_pointer;
	long GL15_glBindBuffer_pointer;
	long GL20_glBlendEquationSeparate_pointer;
	long GL20_glStencilMaskSeparate_pointer;
	long GL20_glStencilFuncSeparate_pointer;
	long GL20_glStencilOpSeparate_pointer;
	long GL20_glDrawBuffers_pointer;
	long GL20_glGetAttribLocation_pointer;
	long GL20_glGetActiveAttrib_pointer;
	long GL20_glBindAttribLocation_pointer;
	long GL20_glGetVertexAttribPointerv_pointer;
	long GL20_glGetVertexAttribiv_pointer;
	long GL20_glGetVertexAttribfv_pointer;
	long GL20_glDisableVertexAttribArray_pointer;
	long GL20_glEnableVertexAttribArray_pointer;
	long GL20_glVertexAttribPointer_pointer;
	long GL20_glVertexAttrib4Nub_pointer;
	long GL20_glVertexAttrib4f_pointer;
	long GL20_glVertexAttrib4s_pointer;
	long GL20_glVertexAttrib3f_pointer;
	long GL20_glVertexAttrib3s_pointer;
	long GL20_glVertexAttrib2f_pointer;
	long GL20_glVertexAttrib2s_pointer;
	long GL20_glVertexAttrib1f_pointer;
	long GL20_glVertexAttrib1s_pointer;
	long GL20_glGetShaderSource_pointer;
	long GL20_glGetUniformiv_pointer;
	long GL20_glGetUniformfv_pointer;
	long GL20_glGetActiveUniform_pointer;
	long GL20_glGetUniformLocation_pointer;
	long GL20_glGetAttachedShaders_pointer;
	long GL20_glGetProgramInfoLog_pointer;
	long GL20_glGetShaderInfoLog_pointer;
	long GL20_glGetProgramiv_pointer;
	long GL20_glGetShaderiv_pointer;
	long GL20_glUniformMatrix4fv_pointer;
	long GL20_glUniformMatrix3fv_pointer;
	long GL20_glUniformMatrix2fv_pointer;
	long GL20_glUniform4iv_pointer;
	long GL20_glUniform3iv_pointer;
	long GL20_glUniform2iv_pointer;
	long GL20_glUniform1iv_pointer;
	long GL20_glUniform4fv_pointer;
	long GL20_glUniform3fv_pointer;
	long GL20_glUniform2fv_pointer;
	long GL20_glUniform1fv_pointer;
	long GL20_glUniform4i_pointer;
	long GL20_glUniform3i_pointer;
	long GL20_glUniform2i_pointer;
	long GL20_glUniform1i_pointer;
	long GL20_glUniform4f_pointer;
	long GL20_glUniform3f_pointer;
	long GL20_glUniform2f_pointer;
	long GL20_glUniform1f_pointer;
	long GL20_glDeleteProgram_pointer;
	long GL20_glValidateProgram_pointer;
	long GL20_glUseProgram_pointer;
	long GL20_glLinkProgram_pointer;
	long GL20_glDetachShader_pointer;
	long GL20_glAttachShader_pointer;
	long GL20_glIsProgram_pointer;
	long GL20_glCreateProgram_pointer;
	long GL20_glDeleteShader_pointer;
	long GL20_glCompileShader_pointer;
	long GL20_glIsShader_pointer;
	long GL20_glCreateShader_pointer;
	long GL20_glShaderSource_pointer;
	long NV_evaluators_glEvalMapsNV_pointer;
	long NV_evaluators_glGetMapAttribParameterivNV_pointer;
	long NV_evaluators_glGetMapAttribParameterfvNV_pointer;
	long NV_evaluators_glGetMapParameterivNV_pointer;
	long NV_evaluators_glGetMapParameterfvNV_pointer;
	long NV_evaluators_glMapParameterivNV_pointer;
	long NV_evaluators_glMapParameterfvNV_pointer;
	long NV_evaluators_glMapControlPointsNV_pointer;
	long NV_evaluators_glGetMapControlPointsNV_pointer;
	long NV_fence_glGetFenceivNV_pointer;
	long NV_fence_glIsFenceNV_pointer;
	long NV_fence_glFinishFenceNV_pointer;
	long NV_fence_glTestFenceNV_pointer;
	long NV_fence_glSetFenceNV_pointer;
	long NV_fence_glDeleteFencesNV_pointer;
	long NV_fence_glGenFencesNV_pointer;
	long NV_fragment_program_glGetProgramNamedParameterfvNV_pointer;
	long NV_fragment_program_glProgramNamedParameter4fNV_pointer;
	long NV_half_float_glVertexAttribs4hvNV_pointer;
	long NV_half_float_glVertexAttribs3hvNV_pointer;
	long NV_half_float_glVertexAttribs2hvNV_pointer;
	long NV_half_float_glVertexAttribs1hvNV_pointer;
	long NV_half_float_glVertexAttrib4hNV_pointer;
	long NV_half_float_glVertexAttrib3hNV_pointer;
	long NV_half_float_glVertexAttrib2hNV_pointer;
	long NV_half_float_glVertexAttrib1hNV_pointer;
	long NV_half_float_glSecondaryColor3hNV_pointer;
	long NV_half_float_glFogCoordhNV_pointer;
	long NV_half_float_glMultiTexCoord4hNV_pointer;
	long NV_half_float_glMultiTexCoord3hNV_pointer;
	long NV_half_float_glMultiTexCoord2hNV_pointer;
	long NV_half_float_glMultiTexCoord1hNV_pointer;
	long NV_half_float_glTexCoord4hNV_pointer;
	long NV_half_float_glTexCoord3hNV_pointer;
	long NV_half_float_glTexCoord2hNV_pointer;
	long NV_half_float_glTexCoord1hNV_pointer;
	long NV_half_float_glColor4hNV_pointer;
	long NV_half_float_glColor3hNV_pointer;
	long NV_half_float_glNormal3hNV_pointer;
	long NV_half_float_glVertex4hNV_pointer;
	long NV_half_float_glVertex3hNV_pointer;
	long NV_half_float_glVertex2hNV_pointer;
	long NV_occlusion_query_glGetOcclusionQueryivNV_pointer;
	long NV_occlusion_query_glGetOcclusionQueryuivNV_pointer;
	long NV_occlusion_query_glEndOcclusionQueryNV_pointer;
	long NV_occlusion_query_glBeginOcclusionQueryNV_pointer;
	long NV_occlusion_query_glIsOcclusionQueryNV_pointer;
	long NV_occlusion_query_glDeleteOcclusionQueriesNV_pointer;
	long NV_occlusion_query_glGenOcclusionQueriesNV_pointer;
	long NV_pixel_data_range_glFlushPixelDataRangeNV_pointer;
	long NV_pixel_data_range_glPixelDataRangeNV_pointer;
	long NV_point_sprite_glPointParameterivNV_pointer;
	long NV_point_sprite_glPointParameteriNV_pointer;
	long NV_primitive_restart_glPrimitiveRestartIndexNV_pointer;
	long NV_primitive_restart_glPrimitiveRestartNV_pointer;
	long NV_program_glRequestResidentProgramsNV_pointer;
	long NV_program_glAreProgramsResidentNV_pointer;
	long NV_program_glIsProgramNV_pointer;
	long NV_program_glGetProgramStringNV_pointer;
	long NV_program_glGetProgramivNV_pointer;
	long NV_program_glGenProgramsNV_pointer;
	long NV_program_glDeleteProgramsNV_pointer;
	long NV_program_glBindProgramNV_pointer;
	long NV_program_glLoadProgramNV_pointer;
	long NV_register_combiners_glGetFinalCombinerInputParameterivNV_pointer;
	long NV_register_combiners_glGetFinalCombinerInputParameterfvNV_pointer;
	long NV_register_combiners_glGetCombinerOutputParameterivNV_pointer;
	long NV_register_combiners_glGetCombinerOutputParameterfvNV_pointer;
	long NV_register_combiners_glGetCombinerInputParameterivNV_pointer;
	long NV_register_combiners_glGetCombinerInputParameterfvNV_pointer;
	long NV_register_combiners_glFinalCombinerInputNV_pointer;
	long NV_register_combiners_glCombinerOutputNV_pointer;
	long NV_register_combiners_glCombinerInputNV_pointer;
	long NV_register_combiners_glCombinerParameterivNV_pointer;
	long NV_register_combiners_glCombinerParameteriNV_pointer;
	long NV_register_combiners_glCombinerParameterfvNV_pointer;
	long NV_register_combiners_glCombinerParameterfNV_pointer;
	long NV_register_combiners2_glGetCombinerStageParameterfvNV_pointer;
	long NV_register_combiners2_glCombinerStageParameterfvNV_pointer;
	long NV_vertex_array_range_glFreeMemoryNV_pointer;
	long NV_vertex_array_range_glAllocateMemoryNV_pointer;
	long NV_vertex_array_range_glFlushVertexArrayRangeNV_pointer;
	long NV_vertex_array_range_glVertexArrayRangeNV_pointer;
	long NV_vertex_program_glVertexAttribs4fvNV_pointer;
	long NV_vertex_program_glVertexAttribs4svNV_pointer;
	long NV_vertex_program_glVertexAttribs3fvNV_pointer;
	long NV_vertex_program_glVertexAttribs3svNV_pointer;
	long NV_vertex_program_glVertexAttribs2fvNV_pointer;
	long NV_vertex_program_glVertexAttribs2svNV_pointer;
	long NV_vertex_program_glVertexAttribs1fvNV_pointer;
	long NV_vertex_program_glVertexAttribs1svNV_pointer;
	long NV_vertex_program_glVertexAttrib4ubNV_pointer;
	long NV_vertex_program_glVertexAttrib4fNV_pointer;
	long NV_vertex_program_glVertexAttrib4sNV_pointer;
	long NV_vertex_program_glVertexAttrib3fNV_pointer;
	long NV_vertex_program_glVertexAttrib3sNV_pointer;
	long NV_vertex_program_glVertexAttrib2fNV_pointer;
	long NV_vertex_program_glVertexAttrib2sNV_pointer;
	long NV_vertex_program_glVertexAttrib1fNV_pointer;
	long NV_vertex_program_glVertexAttrib1sNV_pointer;
	long NV_vertex_program_glVertexAttribPointerNV_pointer;
	long NV_vertex_program_glTrackMatrixNV_pointer;
	long NV_vertex_program_glProgramParameters4fvNV_pointer;
	long NV_vertex_program_glProgramParameter4fNV_pointer;
	long NV_vertex_program_glGetVertexAttribPointervNV_pointer;
	long NV_vertex_program_glGetVertexAttribivNV_pointer;
	long NV_vertex_program_glGetVertexAttribfvNV_pointer;
	long NV_vertex_program_glGetTrackMatrixivNV_pointer;
	long NV_vertex_program_glGetProgramParameterfvNV_pointer;
	long NV_vertex_program_glExecuteProgramNV_pointer;

	private boolean ARB_buffer_object_initNativeFunctionAddresses() {
		return 
			(ARB_buffer_object_glGetBufferPointervARB_pointer = GLContext.getFunctionAddress("glGetBufferPointervARB")) != 0 &&
			(ARB_buffer_object_glGetBufferParameterivARB_pointer = GLContext.getFunctionAddress("glGetBufferParameterivARB")) != 0 &&
			(ARB_buffer_object_glUnmapBufferARB_pointer = GLContext.getFunctionAddress("glUnmapBufferARB")) != 0 &&
			(ARB_buffer_object_glMapBufferARB_pointer = GLContext.getFunctionAddress("glMapBufferARB")) != 0 &&
			(ARB_buffer_object_glGetBufferSubDataARB_pointer = GLContext.getFunctionAddress("glGetBufferSubDataARB")) != 0 &&
			(ARB_buffer_object_glBufferSubDataARB_pointer = GLContext.getFunctionAddress("glBufferSubDataARB")) != 0 &&
			(ARB_buffer_object_glBufferDataARB_pointer = GLContext.getFunctionAddress("glBufferDataARB")) != 0 &&
			(ARB_buffer_object_glIsBufferARB_pointer = GLContext.getFunctionAddress("glIsBufferARB")) != 0 &&
			(ARB_buffer_object_glGenBuffersARB_pointer = GLContext.getFunctionAddress("glGenBuffersARB")) != 0 &&
			(ARB_buffer_object_glDeleteBuffersARB_pointer = GLContext.getFunctionAddress("glDeleteBuffersARB")) != 0 &&
			(ARB_buffer_object_glBindBufferARB_pointer = GLContext.getFunctionAddress("glBindBufferARB")) != 0;
	}

	private boolean ARB_color_buffer_float_initNativeFunctionAddresses() {
		return 
			(ARB_color_buffer_float_glClampColorARB_pointer = GLContext.getFunctionAddress("glClampColorARB")) != 0;
	}

	private boolean ARB_draw_buffers_initNativeFunctionAddresses() {
		return 
			(ARB_draw_buffers_glDrawBuffersARB_pointer = GLContext.getFunctionAddress("glDrawBuffersARB")) != 0;
	}

	private boolean ARB_imaging_initNativeFunctionAddresses() {
		return 
			(ARB_imaging_glGetSeparableFilter_pointer = GLContext.getFunctionAddress("glGetSeparableFilter")) != 0 &&
			(ARB_imaging_glSeparableFilter2D_pointer = GLContext.getFunctionAddress("glSeparableFilter2D")) != 0 &&
			(ARB_imaging_glGetConvolutionParameteriv_pointer = GLContext.getFunctionAddress("glGetConvolutionParameteriv")) != 0 &&
			(ARB_imaging_glGetConvolutionParameterfv_pointer = GLContext.getFunctionAddress("glGetConvolutionParameterfv")) != 0 &&
			(ARB_imaging_glGetConvolutionFilter_pointer = GLContext.getFunctionAddress("glGetConvolutionFilter")) != 0 &&
			(ARB_imaging_glCopyConvolutionFilter2D_pointer = GLContext.getFunctionAddress("glCopyConvolutionFilter2D")) != 0 &&
			(ARB_imaging_glCopyConvolutionFilter1D_pointer = GLContext.getFunctionAddress("glCopyConvolutionFilter1D")) != 0 &&
			(ARB_imaging_glConvolutionParameteriv_pointer = GLContext.getFunctionAddress("glConvolutionParameteriv")) != 0 &&
			(ARB_imaging_glConvolutionParameteri_pointer = GLContext.getFunctionAddress("glConvolutionParameteri")) != 0 &&
			(ARB_imaging_glConvolutionParameterfv_pointer = GLContext.getFunctionAddress("glConvolutionParameterfv")) != 0 &&
			(ARB_imaging_glConvolutionParameterf_pointer = GLContext.getFunctionAddress("glConvolutionParameterf")) != 0 &&
			(ARB_imaging_glConvolutionFilter2D_pointer = GLContext.getFunctionAddress("glConvolutionFilter2D")) != 0 &&
			(ARB_imaging_glConvolutionFilter1D_pointer = GLContext.getFunctionAddress("glConvolutionFilter1D")) != 0 &&
			(ARB_imaging_glGetMinmaxParameteriv_pointer = GLContext.getFunctionAddress("glGetMinmaxParameteriv")) != 0 &&
			(ARB_imaging_glGetMinmaxParameterfv_pointer = GLContext.getFunctionAddress("glGetMinmaxParameterfv")) != 0 &&
			(ARB_imaging_glGetMinmax_pointer = GLContext.getFunctionAddress("glGetMinmax")) != 0 &&
			(ARB_imaging_glResetMinmax_pointer = GLContext.getFunctionAddress("glResetMinmax")) != 0 &&
			(ARB_imaging_glMinmax_pointer = GLContext.getFunctionAddress("glMinmax")) != 0 &&
			(ARB_imaging_glGetHistogramParameteriv_pointer = GLContext.getFunctionAddress("glGetHistogramParameteriv")) != 0 &&
			(ARB_imaging_glGetHistogramParameterfv_pointer = GLContext.getFunctionAddress("glGetHistogramParameterfv")) != 0 &&
			(ARB_imaging_glGetHistogram_pointer = GLContext.getFunctionAddress("glGetHistogram")) != 0 &&
			(ARB_imaging_glResetHistogram_pointer = GLContext.getFunctionAddress("glResetHistogram")) != 0 &&
			(ARB_imaging_glHistogram_pointer = GLContext.getFunctionAddress("glHistogram")) != 0 &&
			(ARB_imaging_glBlendColor_pointer = GLContext.getFunctionAddress("glBlendColor")) != 0 &&
			(ARB_imaging_glBlendEquation_pointer = GLContext.getFunctionAddress("glBlendEquation")) != 0 &&
			(ARB_imaging_glGetColorTableParameterfv_pointer = GLContext.getFunctionAddress("glGetColorTableParameterfv")) != 0 &&
			(ARB_imaging_glGetColorTableParameteriv_pointer = GLContext.getFunctionAddress("glGetColorTableParameteriv")) != 0 &&
			(ARB_imaging_glGetColorTable_pointer = GLContext.getFunctionAddress("glGetColorTable")) != 0 &&
			(ARB_imaging_glCopyColorTable_pointer = GLContext.getFunctionAddress("glCopyColorTable")) != 0 &&
			(ARB_imaging_glCopyColorSubTable_pointer = GLContext.getFunctionAddress("glCopyColorSubTable")) != 0 &&
			(ARB_imaging_glColorTableParameterfv_pointer = GLContext.getFunctionAddress("glColorTableParameterfv")) != 0 &&
			(ARB_imaging_glColorTableParameteriv_pointer = GLContext.getFunctionAddress("glColorTableParameteriv")) != 0 &&
			(ARB_imaging_glColorSubTable_pointer = GLContext.getFunctionAddress("glColorSubTable")) != 0 &&
			(ARB_imaging_glColorTable_pointer = GLContext.getFunctionAddress("glColorTable")) != 0;
	}

	private boolean ARB_matrix_palette_initNativeFunctionAddresses() {
		return 
			(ARB_matrix_palette_glMatrixIndexuivARB_pointer = GLContext.getFunctionAddress("glMatrixIndexuivARB")) != 0 &&
			(ARB_matrix_palette_glMatrixIndexusvARB_pointer = GLContext.getFunctionAddress("glMatrixIndexusvARB")) != 0 &&
			(ARB_matrix_palette_glMatrixIndexubvARB_pointer = GLContext.getFunctionAddress("glMatrixIndexubvARB")) != 0 &&
			(ARB_matrix_palette_glMatrixIndexPointerARB_pointer = GLContext.getFunctionAddress("glMatrixIndexPointerARB")) != 0 &&
			(ARB_matrix_palette_glCurrentPaletteMatrixARB_pointer = GLContext.getFunctionAddress("glCurrentPaletteMatrixARB")) != 0;
	}

	private boolean ARB_multisample_initNativeFunctionAddresses() {
		return 
			(ARB_multisample_glSampleCoverageARB_pointer = GLContext.getFunctionAddress("glSampleCoverageARB")) != 0;
	}

	private boolean ARB_multitexture_initNativeFunctionAddresses() {
		return 
			(ARB_multitexture_glMultiTexCoord4sARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord4sARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord4iARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord4iARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord4fARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord4fARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord3sARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord3sARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord3iARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord3iARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord3fARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord3fARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord2sARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord2sARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord2iARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord2iARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord2fARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord2fARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord1sARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord1sARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord1iARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord1iARB")) != 0 &&
			(ARB_multitexture_glMultiTexCoord1fARB_pointer = GLContext.getFunctionAddress("glMultiTexCoord1fARB")) != 0 &&
			(ARB_multitexture_glActiveTextureARB_pointer = GLContext.getFunctionAddress("glActiveTextureARB")) != 0 &&
			(ARB_multitexture_glClientActiveTextureARB_pointer = GLContext.getFunctionAddress("glClientActiveTextureARB")) != 0;
	}

	private boolean ARB_occlusion_query_initNativeFunctionAddresses() {
		return 
			(ARB_occlusion_query_glGetQueryObjectuivARB_pointer = GLContext.getFunctionAddress("glGetQueryObjectuivARB")) != 0 &&
			(ARB_occlusion_query_glGetQueryObjectivARB_pointer = GLContext.getFunctionAddress("glGetQueryObjectivARB")) != 0 &&
			(ARB_occlusion_query_glGetQueryivARB_pointer = GLContext.getFunctionAddress("glGetQueryivARB")) != 0 &&
			(ARB_occlusion_query_glEndQueryARB_pointer = GLContext.getFunctionAddress("glEndQueryARB")) != 0 &&
			(ARB_occlusion_query_glBeginQueryARB_pointer = GLContext.getFunctionAddress("glBeginQueryARB")) != 0 &&
			(ARB_occlusion_query_glIsQueryARB_pointer = GLContext.getFunctionAddress("glIsQueryARB")) != 0 &&
			(ARB_occlusion_query_glDeleteQueriesARB_pointer = GLContext.getFunctionAddress("glDeleteQueriesARB")) != 0 &&
			(ARB_occlusion_query_glGenQueriesARB_pointer = GLContext.getFunctionAddress("glGenQueriesARB")) != 0;
	}

	private boolean ARB_point_parameters_initNativeFunctionAddresses() {
		return 
			(ARB_point_parameters_glPointParameterfvARB_pointer = GLContext.getFunctionAddress("glPointParameterfvARB")) != 0 &&
			(ARB_point_parameters_glPointParameterfARB_pointer = GLContext.getFunctionAddress("glPointParameterfARB")) != 0;
	}

	private boolean ARB_program_initNativeFunctionAddresses() {
		return 
			(ARB_program_glIsProgramARB_pointer = GLContext.getFunctionAddress("glIsProgramARB")) != 0 &&
			(ARB_program_glGetProgramStringARB_pointer = GLContext.getFunctionAddress("glGetProgramStringARB")) != 0 &&
			(ARB_program_glGetProgramivARB_pointer = GLContext.getFunctionAddress("glGetProgramivARB")) != 0 &&
			(ARB_program_glGetProgramLocalParameterfvARB_pointer = GLContext.getFunctionAddress("glGetProgramLocalParameterfvARB")) != 0 &&
			(ARB_program_glGetProgramEnvParameterfvARB_pointer = GLContext.getFunctionAddress("glGetProgramEnvParameterfvARB")) != 0 &&
			(ARB_program_glProgramLocalParameter4fvARB_pointer = GLContext.getFunctionAddress("glProgramLocalParameter4fvARB")) != 0 &&
			(ARB_program_glProgramLocalParameter4fARB_pointer = GLContext.getFunctionAddress("glProgramLocalParameter4fARB")) != 0 &&
			(ARB_program_glProgramEnvParameter4fvARB_pointer = GLContext.getFunctionAddress("glProgramEnvParameter4fvARB")) != 0 &&
			(ARB_program_glProgramEnvParameter4fARB_pointer = GLContext.getFunctionAddress("glProgramEnvParameter4fARB")) != 0 &&
			(ARB_program_glGenProgramsARB_pointer = GLContext.getFunctionAddress("glGenProgramsARB")) != 0 &&
			(ARB_program_glDeleteProgramsARB_pointer = GLContext.getFunctionAddress("glDeleteProgramsARB")) != 0 &&
			(ARB_program_glBindProgramARB_pointer = GLContext.getFunctionAddress("glBindProgramARB")) != 0 &&
			(ARB_program_glProgramStringARB_pointer = GLContext.getFunctionAddress("glProgramStringARB")) != 0;
	}

	private boolean ARB_shader_objects_initNativeFunctionAddresses() {
		return 
			(ARB_shader_objects_glGetShaderSourceARB_pointer = GLContext.getFunctionAddress("glGetShaderSourceARB")) != 0 &&
			(ARB_shader_objects_glGetUniformivARB_pointer = GLContext.getFunctionAddress("glGetUniformivARB")) != 0 &&
			(ARB_shader_objects_glGetUniformfvARB_pointer = GLContext.getFunctionAddress("glGetUniformfvARB")) != 0 &&
			(ARB_shader_objects_glGetActiveUniformARB_pointer = GLContext.getFunctionAddress("glGetActiveUniformARB")) != 0 &&
			(ARB_shader_objects_glGetUniformLocationARB_pointer = GLContext.getFunctionAddress("glGetUniformLocationARB")) != 0 &&
			(ARB_shader_objects_glGetAttachedObjectsARB_pointer = GLContext.getFunctionAddress("glGetAttachedObjectsARB")) != 0 &&
			(ARB_shader_objects_glGetInfoLogARB_pointer = GLContext.getFunctionAddress("glGetInfoLogARB")) != 0 &&
			(ARB_shader_objects_glGetObjectParameterivARB_pointer = GLContext.getFunctionAddress("glGetObjectParameterivARB")) != 0 &&
			(ARB_shader_objects_glGetObjectParameterfvARB_pointer = GLContext.getFunctionAddress("glGetObjectParameterfvARB")) != 0 &&
			(ARB_shader_objects_glUniformMatrix4fvARB_pointer = GLContext.getFunctionAddress("glUniformMatrix4fvARB")) != 0 &&
			(ARB_shader_objects_glUniformMatrix3fvARB_pointer = GLContext.getFunctionAddress("glUniformMatrix3fvARB")) != 0 &&
			(ARB_shader_objects_glUniformMatrix2fvARB_pointer = GLContext.getFunctionAddress("glUniformMatrix2fvARB")) != 0 &&
			(ARB_shader_objects_glUniform4ivARB_pointer = GLContext.getFunctionAddress("glUniform4ivARB")) != 0 &&
			(ARB_shader_objects_glUniform3ivARB_pointer = GLContext.getFunctionAddress("glUniform3ivARB")) != 0 &&
			(ARB_shader_objects_glUniform2ivARB_pointer = GLContext.getFunctionAddress("glUniform2ivARB")) != 0 &&
			(ARB_shader_objects_glUniform1ivARB_pointer = GLContext.getFunctionAddress("glUniform1ivARB")) != 0 &&
			(ARB_shader_objects_glUniform4fvARB_pointer = GLContext.getFunctionAddress("glUniform4fvARB")) != 0 &&
			(ARB_shader_objects_glUniform3fvARB_pointer = GLContext.getFunctionAddress("glUniform3fvARB")) != 0 &&
			(ARB_shader_objects_glUniform2fvARB_pointer = GLContext.getFunctionAddress("glUniform2fvARB")) != 0 &&
			(ARB_shader_objects_glUniform1fvARB_pointer = GLContext.getFunctionAddress("glUniform1fvARB")) != 0 &&
			(ARB_shader_objects_glUniform4iARB_pointer = GLContext.getFunctionAddress("glUniform4iARB")) != 0 &&
			(ARB_shader_objects_glUniform3iARB_pointer = GLContext.getFunctionAddress("glUniform3iARB")) != 0 &&
			(ARB_shader_objects_glUniform2iARB_pointer = GLContext.getFunctionAddress("glUniform2iARB")) != 0 &&
			(ARB_shader_objects_glUniform1iARB_pointer = GLContext.getFunctionAddress("glUniform1iARB")) != 0 &&
			(ARB_shader_objects_glUniform4fARB_pointer = GLContext.getFunctionAddress("glUniform4fARB")) != 0 &&
			(ARB_shader_objects_glUniform3fARB_pointer = GLContext.getFunctionAddress("glUniform3fARB")) != 0 &&
			(ARB_shader_objects_glUniform2fARB_pointer = GLContext.getFunctionAddress("glUniform2fARB")) != 0 &&
			(ARB_shader_objects_glUniform1fARB_pointer = GLContext.getFunctionAddress("glUniform1fARB")) != 0 &&
			(ARB_shader_objects_glValidateProgramARB_pointer = GLContext.getFunctionAddress("glValidateProgramARB")) != 0 &&
			(ARB_shader_objects_glUseProgramObjectARB_pointer = GLContext.getFunctionAddress("glUseProgramObjectARB")) != 0 &&
			(ARB_shader_objects_glLinkProgramARB_pointer = GLContext.getFunctionAddress("glLinkProgramARB")) != 0 &&
			(ARB_shader_objects_glAttachObjectARB_pointer = GLContext.getFunctionAddress("glAttachObjectARB")) != 0 &&
			(ARB_shader_objects_glCreateProgramObjectARB_pointer = GLContext.getFunctionAddress("glCreateProgramObjectARB")) != 0 &&
			(ARB_shader_objects_glCompileShaderARB_pointer = GLContext.getFunctionAddress("glCompileShaderARB")) != 0 &&
			(ARB_shader_objects_glShaderSourceARB_pointer = GLContext.getFunctionAddress("glShaderSourceARB")) != 0 &&
			(ARB_shader_objects_glCreateShaderObjectARB_pointer = GLContext.getFunctionAddress("glCreateShaderObjectARB")) != 0 &&
			(ARB_shader_objects_glDetachObjectARB_pointer = GLContext.getFunctionAddress("glDetachObjectARB")) != 0 &&
			(ARB_shader_objects_glGetHandleARB_pointer = GLContext.getFunctionAddress("glGetHandleARB")) != 0 &&
			(ARB_shader_objects_glDeleteObjectARB_pointer = GLContext.getFunctionAddress("glDeleteObjectARB")) != 0;
	}

	private boolean ARB_texture_compression_initNativeFunctionAddresses() {
		return 
			(ARB_texture_compression_glGetCompressedTexImageARB_pointer = GLContext.getFunctionAddress("glGetCompressedTexImageARB")) != 0 &&
			(ARB_texture_compression_glCompressedTexSubImage3DARB_pointer = GLContext.getFunctionAddress("glCompressedTexSubImage3DARB")) != 0 &&
			(ARB_texture_compression_glCompressedTexSubImage2DARB_pointer = GLContext.getFunctionAddress("glCompressedTexSubImage2DARB")) != 0 &&
			(ARB_texture_compression_glCompressedTexSubImage1DARB_pointer = GLContext.getFunctionAddress("glCompressedTexSubImage1DARB")) != 0 &&
			(ARB_texture_compression_glCompressedTexImage3DARB_pointer = GLContext.getFunctionAddress("glCompressedTexImage3DARB")) != 0 &&
			(ARB_texture_compression_glCompressedTexImage2DARB_pointer = GLContext.getFunctionAddress("glCompressedTexImage2DARB")) != 0 &&
			(ARB_texture_compression_glCompressedTexImage1DARB_pointer = GLContext.getFunctionAddress("glCompressedTexImage1DARB")) != 0;
	}

	private boolean ARB_transpose_matrix_initNativeFunctionAddresses() {
		return 
			(ARB_transpose_matrix_glMultTransposeMatrixfARB_pointer = GLContext.getFunctionAddress("glMultTransposeMatrixfARB")) != 0 &&
			(ARB_transpose_matrix_glLoadTransposeMatrixfARB_pointer = GLContext.getFunctionAddress("glLoadTransposeMatrixfARB")) != 0;
	}

	private boolean ARB_vertex_blend_initNativeFunctionAddresses() {
		return 
			(ARB_vertex_blend_glVertexBlendARB_pointer = GLContext.getFunctionAddress("glVertexBlendARB")) != 0 &&
			(ARB_vertex_blend_glWeightPointerARB_pointer = GLContext.getFunctionAddress("glWeightPointerARB")) != 0 &&
			(ARB_vertex_blend_glWeightuivARB_pointer = GLContext.getFunctionAddress("glWeightuivARB")) != 0 &&
			(ARB_vertex_blend_glWeightusvARB_pointer = GLContext.getFunctionAddress("glWeightusvARB")) != 0 &&
			(ARB_vertex_blend_glWeightubvARB_pointer = GLContext.getFunctionAddress("glWeightubvARB")) != 0 &&
			(ARB_vertex_blend_glWeightfvARB_pointer = GLContext.getFunctionAddress("glWeightfvARB")) != 0 &&
			(ARB_vertex_blend_glWeightivARB_pointer = GLContext.getFunctionAddress("glWeightivARB")) != 0 &&
			(ARB_vertex_blend_glWeightsvARB_pointer = GLContext.getFunctionAddress("glWeightsvARB")) != 0 &&
			(ARB_vertex_blend_glWeightbvARB_pointer = GLContext.getFunctionAddress("glWeightbvARB")) != 0;
	}

	private boolean ARB_vertex_program_initNativeFunctionAddresses() {
		return 
			(ARB_vertex_program_glGetVertexAttribPointervARB_pointer = GLContext.getFunctionAddress("glGetVertexAttribPointervARB")) != 0 &&
			(ARB_vertex_program_glGetVertexAttribivARB_pointer = GLContext.getFunctionAddress("glGetVertexAttribivARB")) != 0 &&
			(ARB_vertex_program_glGetVertexAttribfvARB_pointer = GLContext.getFunctionAddress("glGetVertexAttribfvARB")) != 0 &&
			(ARB_vertex_program_glDisableVertexAttribArrayARB_pointer = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB")) != 0 &&
			(ARB_vertex_program_glEnableVertexAttribArrayARB_pointer = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB")) != 0 &&
			(ARB_vertex_program_glVertexAttribPointerARB_pointer = GLContext.getFunctionAddress("glVertexAttribPointerARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib4NubARB_pointer = GLContext.getFunctionAddress("glVertexAttrib4NubARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib4fARB_pointer = GLContext.getFunctionAddress("glVertexAttrib4fARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib4sARB_pointer = GLContext.getFunctionAddress("glVertexAttrib4sARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib3fARB_pointer = GLContext.getFunctionAddress("glVertexAttrib3fARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib3sARB_pointer = GLContext.getFunctionAddress("glVertexAttrib3sARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib2fARB_pointer = GLContext.getFunctionAddress("glVertexAttrib2fARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib2sARB_pointer = GLContext.getFunctionAddress("glVertexAttrib2sARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib1fARB_pointer = GLContext.getFunctionAddress("glVertexAttrib1fARB")) != 0 &&
			(ARB_vertex_program_glVertexAttrib1sARB_pointer = GLContext.getFunctionAddress("glVertexAttrib1sARB")) != 0;
	}

	private boolean ARB_vertex_shader_initNativeFunctionAddresses() {
		return 
			(ARB_vertex_shader_glGetAttribLocationARB_pointer = GLContext.getFunctionAddress("glGetAttribLocationARB")) != 0 &&
			(ARB_vertex_shader_glGetActiveAttribARB_pointer = GLContext.getFunctionAddress("glGetActiveAttribARB")) != 0 &&
			(ARB_vertex_shader_glBindAttribLocationARB_pointer = GLContext.getFunctionAddress("glBindAttribLocationARB")) != 0;
	}

	private boolean ARB_window_pos_initNativeFunctionAddresses() {
		return 
			(ARB_window_pos_glWindowPos3sARB_pointer = GLContext.getFunctionAddress("glWindowPos3sARB")) != 0 &&
			(ARB_window_pos_glWindowPos3iARB_pointer = GLContext.getFunctionAddress("glWindowPos3iARB")) != 0 &&
			(ARB_window_pos_glWindowPos3fARB_pointer = GLContext.getFunctionAddress("glWindowPos3fARB")) != 0 &&
			(ARB_window_pos_glWindowPos2sARB_pointer = GLContext.getFunctionAddress("glWindowPos2sARB")) != 0 &&
			(ARB_window_pos_glWindowPos2iARB_pointer = GLContext.getFunctionAddress("glWindowPos2iARB")) != 0 &&
			(ARB_window_pos_glWindowPos2fARB_pointer = GLContext.getFunctionAddress("glWindowPos2fARB")) != 0;
	}

	private boolean ATI_draw_buffers_initNativeFunctionAddresses() {
		return 
			(ATI_draw_buffers_glDrawBuffersATI_pointer = GLContext.getFunctionAddress("glDrawBuffersATI")) != 0;
	}

	private boolean ATI_element_array_initNativeFunctionAddresses() {
		return 
			(ATI_element_array_glDrawRangeElementArrayATI_pointer = GLContext.getFunctionAddress("glDrawRangeElementArrayATI")) != 0 &&
			(ATI_element_array_glDrawElementArrayATI_pointer = GLContext.getFunctionAddress("glDrawElementArrayATI")) != 0 &&
			(ATI_element_array_glElementPointerATI_pointer = GLContext.getFunctionAddress("glElementPointerATI")) != 0;
	}

	private boolean ATI_envmap_bumpmap_initNativeFunctionAddresses() {
		return 
			(ATI_envmap_bumpmap_glGetTexBumpParameterivATI_pointer = GLContext.getFunctionAddress("glGetTexBumpParameterivATI")) != 0 &&
			(ATI_envmap_bumpmap_glGetTexBumpParameterfvATI_pointer = GLContext.getFunctionAddress("glGetTexBumpParameterfvATI")) != 0 &&
			(ATI_envmap_bumpmap_glTexBumpParameterivATI_pointer = GLContext.getFunctionAddress("glTexBumpParameterivATI")) != 0 &&
			(ATI_envmap_bumpmap_glTexBumpParameterfvATI_pointer = GLContext.getFunctionAddress("glTexBumpParameterfvATI")) != 0;
	}

	private boolean ATI_fragment_shader_initNativeFunctionAddresses() {
		return 
			(ATI_fragment_shader_glSetFragmentShaderConstantATI_pointer = GLContext.getFunctionAddress("glSetFragmentShaderConstantATI")) != 0 &&
			(ATI_fragment_shader_glAlphaFragmentOp3ATI_pointer = GLContext.getFunctionAddress("glAlphaFragmentOp3ATI")) != 0 &&
			(ATI_fragment_shader_glAlphaFragmentOp2ATI_pointer = GLContext.getFunctionAddress("glAlphaFragmentOp2ATI")) != 0 &&
			(ATI_fragment_shader_glAlphaFragmentOp1ATI_pointer = GLContext.getFunctionAddress("glAlphaFragmentOp1ATI")) != 0 &&
			(ATI_fragment_shader_glColorFragmentOp3ATI_pointer = GLContext.getFunctionAddress("glColorFragmentOp3ATI")) != 0 &&
			(ATI_fragment_shader_glColorFragmentOp2ATI_pointer = GLContext.getFunctionAddress("glColorFragmentOp2ATI")) != 0 &&
			(ATI_fragment_shader_glColorFragmentOp1ATI_pointer = GLContext.getFunctionAddress("glColorFragmentOp1ATI")) != 0 &&
			(ATI_fragment_shader_glSampleMapATI_pointer = GLContext.getFunctionAddress("glSampleMapATI")) != 0 &&
			(ATI_fragment_shader_glPassTexCoordATI_pointer = GLContext.getFunctionAddress("glPassTexCoordATI")) != 0 &&
			(ATI_fragment_shader_glEndFragmentShaderATI_pointer = GLContext.getFunctionAddress("glEndFragmentShaderATI")) != 0 &&
			(ATI_fragment_shader_glBeginFragmentShaderATI_pointer = GLContext.getFunctionAddress("glBeginFragmentShaderATI")) != 0 &&
			(ATI_fragment_shader_glDeleteFragmentShaderATI_pointer = GLContext.getFunctionAddress("glDeleteFragmentShaderATI")) != 0 &&
			(ATI_fragment_shader_glBindFragmentShaderATI_pointer = GLContext.getFunctionAddress("glBindFragmentShaderATI")) != 0 &&
			(ATI_fragment_shader_glGenFragmentShadersATI_pointer = GLContext.getFunctionAddress("glGenFragmentShadersATI")) != 0;
	}

	private boolean ATI_map_object_buffer_initNativeFunctionAddresses() {
		return 
			(ATI_map_object_buffer_glUnmapObjectBufferATI_pointer = GLContext.getFunctionAddress("glUnmapObjectBufferATI")) != 0 &&
			(ATI_map_object_buffer_glMapObjectBufferATI_pointer = GLContext.getFunctionAddress("glMapObjectBufferATI")) != 0;
	}

	private boolean ATI_pn_triangles_initNativeFunctionAddresses() {
		return 
			(ATI_pn_triangles_glPNTrianglesiATI_pointer = GLContext.getFunctionAddress("glPNTrianglesiATI")) != 0 &&
			(ATI_pn_triangles_glPNTrianglesfATI_pointer = GLContext.getFunctionAddress("glPNTrianglesfATI")) != 0;
	}

	private boolean ATI_separate_stencil_initNativeFunctionAddresses() {
		return 
			(ATI_separate_stencil_glStencilFuncSeparateATI_pointer = GLContext.getFunctionAddress("glStencilFuncSeparateATI")) != 0 &&
			(ATI_separate_stencil_glStencilOpSeparateATI_pointer = GLContext.getFunctionAddress("glStencilOpSeparateATI")) != 0;
	}

	private boolean ATI_vertex_array_object_initNativeFunctionAddresses() {
		return 
			(ATI_vertex_array_object_glGetVariantArrayObjectivATI_pointer = GLContext.getFunctionAddress("glGetVariantArrayObjectivATI")) != 0 &&
			(ATI_vertex_array_object_glGetVariantArrayObjectfvATI_pointer = GLContext.getFunctionAddress("glGetVariantArrayObjectfvATI")) != 0 &&
			(ATI_vertex_array_object_glVariantArrayObjectATI_pointer = GLContext.getFunctionAddress("glVariantArrayObjectATI")) != 0 &&
			(ATI_vertex_array_object_glGetArrayObjectivATI_pointer = GLContext.getFunctionAddress("glGetArrayObjectivATI")) != 0 &&
			(ATI_vertex_array_object_glGetArrayObjectfvATI_pointer = GLContext.getFunctionAddress("glGetArrayObjectfvATI")) != 0 &&
			(ATI_vertex_array_object_glArrayObjectATI_pointer = GLContext.getFunctionAddress("glArrayObjectATI")) != 0 &&
			(ATI_vertex_array_object_glFreeObjectBufferATI_pointer = GLContext.getFunctionAddress("glFreeObjectBufferATI")) != 0 &&
			(ATI_vertex_array_object_glGetObjectBufferivATI_pointer = GLContext.getFunctionAddress("glGetObjectBufferivATI")) != 0 &&
			(ATI_vertex_array_object_glGetObjectBufferfvATI_pointer = GLContext.getFunctionAddress("glGetObjectBufferfvATI")) != 0 &&
			(ATI_vertex_array_object_glUpdateObjectBufferATI_pointer = GLContext.getFunctionAddress("glUpdateObjectBufferATI")) != 0 &&
			(ATI_vertex_array_object_glIsObjectBufferATI_pointer = GLContext.getFunctionAddress("glIsObjectBufferATI")) != 0 &&
			(ATI_vertex_array_object_glNewObjectBufferATI_pointer = GLContext.getFunctionAddress("glNewObjectBufferATI")) != 0;
	}

	private boolean ATI_vertex_attrib_array_object_initNativeFunctionAddresses() {
		return 
			(ATI_vertex_attrib_array_object_glGetVertexAttribArrayObjectivATI_pointer = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectivATI")) != 0 &&
			(ATI_vertex_attrib_array_object_glGetVertexAttribArrayObjectfvATI_pointer = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectfvATI")) != 0 &&
			(ATI_vertex_attrib_array_object_glVertexAttribArrayObjectATI_pointer = GLContext.getFunctionAddress("glVertexAttribArrayObjectATI")) != 0;
	}

	private boolean ATI_vertex_streams_initNativeFunctionAddresses() {
		return 
			(ATI_vertex_streams_glVertexBlendEnviATI_pointer = GLContext.getFunctionAddress("glVertexBlendEnviATI")) != 0 &&
			(ATI_vertex_streams_glVertexBlendEnvfATI_pointer = GLContext.getFunctionAddress("glVertexBlendEnvfATI")) != 0 &&
			(ATI_vertex_streams_glClientActiveVertexStreamATI_pointer = GLContext.getFunctionAddress("glClientActiveVertexStreamATI")) != 0 &&
			(ATI_vertex_streams_glNormalStream3sATI_pointer = GLContext.getFunctionAddress("glNormalStream3sATI")) != 0 &&
			(ATI_vertex_streams_glNormalStream3iATI_pointer = GLContext.getFunctionAddress("glNormalStream3iATI")) != 0 &&
			(ATI_vertex_streams_glNormalStream3fATI_pointer = GLContext.getFunctionAddress("glNormalStream3fATI")) != 0 &&
			(ATI_vertex_streams_glNormalStream3bATI_pointer = GLContext.getFunctionAddress("glNormalStream3bATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream4sATI_pointer = GLContext.getFunctionAddress("glVertexStream4sATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream4iATI_pointer = GLContext.getFunctionAddress("glVertexStream4iATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream4fATI_pointer = GLContext.getFunctionAddress("glVertexStream4fATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream3sATI_pointer = GLContext.getFunctionAddress("glVertexStream3sATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream3iATI_pointer = GLContext.getFunctionAddress("glVertexStream3iATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream3fATI_pointer = GLContext.getFunctionAddress("glVertexStream3fATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream2sATI_pointer = GLContext.getFunctionAddress("glVertexStream2sATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream2iATI_pointer = GLContext.getFunctionAddress("glVertexStream2iATI")) != 0 &&
			(ATI_vertex_streams_glVertexStream2fATI_pointer = GLContext.getFunctionAddress("glVertexStream2fATI")) != 0;
	}

	private boolean EXT_blend_equation_separate_initNativeFunctionAddresses() {
		return 
			(EXT_blend_equation_separate_glBlendEquationSeparateEXT_pointer = GLContext.getFunctionAddress("glBlendEquationSeparateEXT")) != 0;
	}

	private boolean EXT_blend_func_separate_initNativeFunctionAddresses() {
		return 
			(EXT_blend_func_separate_glBlendFuncSeparateEXT_pointer = GLContext.getFunctionAddress("glBlendFuncSeparateEXT")) != 0;
	}

	private boolean EXT_compiled_vertex_array_initNativeFunctionAddresses() {
		return 
			(EXT_compiled_vertex_array_glUnlockArraysEXT_pointer = GLContext.getFunctionAddress("glUnlockArraysEXT")) != 0 &&
			(EXT_compiled_vertex_array_glLockArraysEXT_pointer = GLContext.getFunctionAddress("glLockArraysEXT")) != 0;
	}

	private boolean EXT_depth_bounds_test_initNativeFunctionAddresses() {
		return 
			(EXT_depth_bounds_test_glDepthBoundsEXT_pointer = GLContext.getFunctionAddress("glDepthBoundsEXT")) != 0;
	}

	private boolean EXT_draw_range_elements_initNativeFunctionAddresses() {
		return 
			(EXT_draw_range_elements_glDrawRangeElementsEXT_pointer = GLContext.getFunctionAddress("glDrawRangeElementsEXT")) != 0;
	}

	private boolean EXT_fog_coord_initNativeFunctionAddresses() {
		return 
			(EXT_fog_coord_glFogCoordPointerEXT_pointer = GLContext.getFunctionAddress("glFogCoordPointerEXT")) != 0 &&
			(EXT_fog_coord_glFogCoordfEXT_pointer = GLContext.getFunctionAddress("glFogCoordfEXT")) != 0;
	}

	private boolean EXT_framebuffer_object_initNativeFunctionAddresses() {
		return 
			(EXT_framebuffer_object_glGenerateMipmapEXT_pointer = GLContext.getFunctionAddress("glGenerateMipmapEXT")) != 0 &&
			(EXT_framebuffer_object_glGetFramebufferAttachmentParameterivEXT_pointer = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameterivEXT")) != 0 &&
			(EXT_framebuffer_object_glFramebufferRenderbufferEXT_pointer = GLContext.getFunctionAddress("glFramebufferRenderbufferEXT")) != 0 &&
			(EXT_framebuffer_object_glFramebufferTexture3DEXT_pointer = GLContext.getFunctionAddress("glFramebufferTexture3DEXT")) != 0 &&
			(EXT_framebuffer_object_glFramebufferTexture2DEXT_pointer = GLContext.getFunctionAddress("glFramebufferTexture2DEXT")) != 0 &&
			(EXT_framebuffer_object_glFramebufferTexture1DEXT_pointer = GLContext.getFunctionAddress("glFramebufferTexture1DEXT")) != 0 &&
			(EXT_framebuffer_object_glCheckFramebufferStatusEXT_pointer = GLContext.getFunctionAddress("glCheckFramebufferStatusEXT")) != 0 &&
			(EXT_framebuffer_object_glGenFramebuffersEXT_pointer = GLContext.getFunctionAddress("glGenFramebuffersEXT")) != 0 &&
			(EXT_framebuffer_object_glDeleteFramebuffersEXT_pointer = GLContext.getFunctionAddress("glDeleteFramebuffersEXT")) != 0 &&
			(EXT_framebuffer_object_glBindFramebufferEXT_pointer = GLContext.getFunctionAddress("glBindFramebufferEXT")) != 0 &&
			(EXT_framebuffer_object_glIsFramebufferEXT_pointer = GLContext.getFunctionAddress("glIsFramebufferEXT")) != 0 &&
			(EXT_framebuffer_object_glGetRenderbufferParameterivEXT_pointer = GLContext.getFunctionAddress("glGetRenderbufferParameterivEXT")) != 0 &&
			(EXT_framebuffer_object_glRenderbufferStorageEXT_pointer = GLContext.getFunctionAddress("glRenderbufferStorageEXT")) != 0 &&
			(EXT_framebuffer_object_glGenRenderbuffersEXT_pointer = GLContext.getFunctionAddress("glGenRenderbuffersEXT")) != 0 &&
			(EXT_framebuffer_object_glDeleteRenderbuffersEXT_pointer = GLContext.getFunctionAddress("glDeleteRenderbuffersEXT")) != 0 &&
			(EXT_framebuffer_object_glBindRenderbufferEXT_pointer = GLContext.getFunctionAddress("glBindRenderbufferEXT")) != 0 &&
			(EXT_framebuffer_object_glIsRenderbufferEXT_pointer = GLContext.getFunctionAddress("glIsRenderbufferEXT")) != 0;
	}

	private boolean EXT_multi_draw_arrays_initNativeFunctionAddresses() {
		return 
			(EXT_multi_draw_arrays_glMultiDrawArraysEXT_pointer = GLContext.getFunctionAddress("glMultiDrawArraysEXT")) != 0;
	}

	private boolean EXT_paletted_texture_initNativeFunctionAddresses() {
		return 
			(EXT_paletted_texture_glGetColorTableParameterfvEXT_pointer = GLContext.getFunctionAddress("glGetColorTableParameterfvEXT")) != 0 &&
			(EXT_paletted_texture_glGetColorTableParameterivEXT_pointer = GLContext.getFunctionAddress("glGetColorTableParameterivEXT")) != 0 &&
			(EXT_paletted_texture_glGetColorTableEXT_pointer = GLContext.getFunctionAddress("glGetColorTableEXT")) != 0 &&
			(EXT_paletted_texture_glColorSubTableEXT_pointer = GLContext.getFunctionAddress("glColorSubTableEXT")) != 0 &&
			(EXT_paletted_texture_glColorTableEXT_pointer = GLContext.getFunctionAddress("glColorTableEXT")) != 0;
	}

	private boolean EXT_point_parameters_initNativeFunctionAddresses() {
		return 
			(EXT_point_parameters_glPointParameterfvEXT_pointer = GLContext.getFunctionAddress("glPointParameterfvEXT")) != 0 &&
			(EXT_point_parameters_glPointParameterfEXT_pointer = GLContext.getFunctionAddress("glPointParameterfEXT")) != 0;
	}

	private boolean EXT_secondary_color_initNativeFunctionAddresses() {
		return 
			(EXT_secondary_color_glSecondaryColorPointerEXT_pointer = GLContext.getFunctionAddress("glSecondaryColorPointerEXT")) != 0 &&
			(EXT_secondary_color_glSecondaryColor3ubEXT_pointer = GLContext.getFunctionAddress("glSecondaryColor3ubEXT")) != 0 &&
			(EXT_secondary_color_glSecondaryColor3fEXT_pointer = GLContext.getFunctionAddress("glSecondaryColor3fEXT")) != 0 &&
			(EXT_secondary_color_glSecondaryColor3bEXT_pointer = GLContext.getFunctionAddress("glSecondaryColor3bEXT")) != 0;
	}

	private boolean EXT_stencil_two_side_initNativeFunctionAddresses() {
		return 
			(EXT_stencil_two_side_glActiveStencilFaceEXT_pointer = GLContext.getFunctionAddress("glActiveStencilFaceEXT")) != 0;
	}

	private boolean EXT_vertex_shader_initNativeFunctionAddresses() {
		return 
			(EXT_vertex_shader_glGetLocalConstantFloatvEXT_pointer = GLContext.getFunctionAddress("glGetLocalConstantFloatvEXT")) != 0 &&
			(EXT_vertex_shader_glGetLocalConstantIntegervEXT_pointer = GLContext.getFunctionAddress("glGetLocalConstantIntegervEXT")) != 0 &&
			(EXT_vertex_shader_glGetLocalConstantBooleanvEXT_pointer = GLContext.getFunctionAddress("glGetLocalConstantBooleanvEXT")) != 0 &&
			(EXT_vertex_shader_glGetInvariantFloatvEXT_pointer = GLContext.getFunctionAddress("glGetInvariantFloatvEXT")) != 0 &&
			(EXT_vertex_shader_glGetInvariantIntegervEXT_pointer = GLContext.getFunctionAddress("glGetInvariantIntegervEXT")) != 0 &&
			(EXT_vertex_shader_glGetInvariantBooleanvEXT_pointer = GLContext.getFunctionAddress("glGetInvariantBooleanvEXT")) != 0 &&
			(EXT_vertex_shader_glGetVariantPointervEXT_pointer = GLContext.getFunctionAddress("glGetVariantPointervEXT")) != 0 &&
			(EXT_vertex_shader_glGetVariantFloatvEXT_pointer = GLContext.getFunctionAddress("glGetVariantFloatvEXT")) != 0 &&
			(EXT_vertex_shader_glGetVariantIntegervEXT_pointer = GLContext.getFunctionAddress("glGetVariantIntegervEXT")) != 0 &&
			(EXT_vertex_shader_glGetVariantBooleanvEXT_pointer = GLContext.getFunctionAddress("glGetVariantBooleanvEXT")) != 0 &&
			(EXT_vertex_shader_glIsVariantEnabledEXT_pointer = GLContext.getFunctionAddress("glIsVariantEnabledEXT")) != 0 &&
			(EXT_vertex_shader_glBindParameterEXT_pointer = GLContext.getFunctionAddress("glBindParameterEXT")) != 0 &&
			(EXT_vertex_shader_glBindTextureUnitParameterEXT_pointer = GLContext.getFunctionAddress("glBindTextureUnitParameterEXT")) != 0 &&
			(EXT_vertex_shader_glBindTexGenParameterEXT_pointer = GLContext.getFunctionAddress("glBindTexGenParameterEXT")) != 0 &&
			(EXT_vertex_shader_glBindMaterialParameterEXT_pointer = GLContext.getFunctionAddress("glBindMaterialParameterEXT")) != 0 &&
			(EXT_vertex_shader_glBindLightParameterEXT_pointer = GLContext.getFunctionAddress("glBindLightParameterEXT")) != 0 &&
			(EXT_vertex_shader_glDisableVariantClientStateEXT_pointer = GLContext.getFunctionAddress("glDisableVariantClientStateEXT")) != 0 &&
			(EXT_vertex_shader_glEnableVariantClientStateEXT_pointer = GLContext.getFunctionAddress("glEnableVariantClientStateEXT")) != 0 &&
			(EXT_vertex_shader_glVariantPointerEXT_pointer = GLContext.getFunctionAddress("glVariantPointerEXT")) != 0 &&
			(EXT_vertex_shader_glVariantuivEXT_pointer = GLContext.getFunctionAddress("glVariantuivEXT")) != 0 &&
			(EXT_vertex_shader_glVariantusvEXT_pointer = GLContext.getFunctionAddress("glVariantusvEXT")) != 0 &&
			(EXT_vertex_shader_glVariantubvEXT_pointer = GLContext.getFunctionAddress("glVariantubvEXT")) != 0 &&
			(EXT_vertex_shader_glVariantfvEXT_pointer = GLContext.getFunctionAddress("glVariantfvEXT")) != 0 &&
			(EXT_vertex_shader_glVariantivEXT_pointer = GLContext.getFunctionAddress("glVariantivEXT")) != 0 &&
			(EXT_vertex_shader_glVariantsvEXT_pointer = GLContext.getFunctionAddress("glVariantsvEXT")) != 0 &&
			(EXT_vertex_shader_glVariantbvEXT_pointer = GLContext.getFunctionAddress("glVariantbvEXT")) != 0 &&
			(EXT_vertex_shader_glSetLocalConstantEXT_pointer = GLContext.getFunctionAddress("glSetLocalConstantEXT")) != 0 &&
			(EXT_vertex_shader_glSetInvariantEXT_pointer = GLContext.getFunctionAddress("glSetInvariantEXT")) != 0 &&
			(EXT_vertex_shader_glGenSymbolsEXT_pointer = GLContext.getFunctionAddress("glGenSymbolsEXT")) != 0 &&
			(EXT_vertex_shader_glExtractComponentEXT_pointer = GLContext.getFunctionAddress("glExtractComponentEXT")) != 0 &&
			(EXT_vertex_shader_glInsertComponentEXT_pointer = GLContext.getFunctionAddress("glInsertComponentEXT")) != 0 &&
			(EXT_vertex_shader_glWriteMaskEXT_pointer = GLContext.getFunctionAddress("glWriteMaskEXT")) != 0 &&
			(EXT_vertex_shader_glSwizzleEXT_pointer = GLContext.getFunctionAddress("glSwizzleEXT")) != 0 &&
			(EXT_vertex_shader_glShaderOp3EXT_pointer = GLContext.getFunctionAddress("glShaderOp3EXT")) != 0 &&
			(EXT_vertex_shader_glShaderOp2EXT_pointer = GLContext.getFunctionAddress("glShaderOp2EXT")) != 0 &&
			(EXT_vertex_shader_glShaderOp1EXT_pointer = GLContext.getFunctionAddress("glShaderOp1EXT")) != 0 &&
			(EXT_vertex_shader_glDeleteVertexShaderEXT_pointer = GLContext.getFunctionAddress("glDeleteVertexShaderEXT")) != 0 &&
			(EXT_vertex_shader_glGenVertexShadersEXT_pointer = GLContext.getFunctionAddress("glGenVertexShadersEXT")) != 0 &&
			(EXT_vertex_shader_glBindVertexShaderEXT_pointer = GLContext.getFunctionAddress("glBindVertexShaderEXT")) != 0 &&
			(EXT_vertex_shader_glEndVertexShaderEXT_pointer = GLContext.getFunctionAddress("glEndVertexShaderEXT")) != 0 &&
			(EXT_vertex_shader_glBeginVertexShaderEXT_pointer = GLContext.getFunctionAddress("glBeginVertexShaderEXT")) != 0;
	}

	private boolean EXT_vertex_weighting_initNativeFunctionAddresses() {
		return 
			(EXT_vertex_weighting_glVertexWeightPointerEXT_pointer = GLContext.getFunctionAddress("glVertexWeightPointerEXT")) != 0 &&
			(EXT_vertex_weighting_glVertexWeightfEXT_pointer = GLContext.getFunctionAddress("glVertexWeightfEXT")) != 0;
	}

	private boolean GL11_initNativeFunctionAddresses() {
		return 
			(GL11_glViewport_pointer = GLContext.getFunctionAddress("glViewport")) != 0 &&
			(GL11_glStencilMask_pointer = GLContext.getFunctionAddress("glStencilMask")) != 0 &&
			(GL11_glStencilOp_pointer = GLContext.getFunctionAddress("glStencilOp")) != 0 &&
			(GL11_glTexCoord4f_pointer = GLContext.getFunctionAddress("glTexCoord4f")) != 0 &&
			(GL11_glTexCoord3f_pointer = GLContext.getFunctionAddress("glTexCoord3f")) != 0 &&
			(GL11_glTexCoord2f_pointer = GLContext.getFunctionAddress("glTexCoord2f")) != 0 &&
			(GL11_glTexCoord1f_pointer = GLContext.getFunctionAddress("glTexCoord1f")) != 0 &&
			(GL11_glTexCoordPointer_pointer = GLContext.getFunctionAddress("glTexCoordPointer")) != 0 &&
			(GL11_glTexEnviv_pointer = GLContext.getFunctionAddress("glTexEnviv")) != 0 &&
			(GL11_glTexEnvfv_pointer = GLContext.getFunctionAddress("glTexEnvfv")) != 0 &&
			(GL11_glTexEnvi_pointer = GLContext.getFunctionAddress("glTexEnvi")) != 0 &&
			(GL11_glTexEnvf_pointer = GLContext.getFunctionAddress("glTexEnvf")) != 0 &&
			(GL11_glTexGeniv_pointer = GLContext.getFunctionAddress("glTexGeniv")) != 0 &&
			(GL11_glTexGeni_pointer = GLContext.getFunctionAddress("glTexGeni")) != 0 &&
			(GL11_glTexGenfv_pointer = GLContext.getFunctionAddress("glTexGenfv")) != 0 &&
			(GL11_glTexGenf_pointer = GLContext.getFunctionAddress("glTexGenf")) != 0 &&
			(GL11_glTexParameteriv_pointer = GLContext.getFunctionAddress("glTexParameteriv")) != 0 &&
			(GL11_glTexParameterfv_pointer = GLContext.getFunctionAddress("glTexParameterfv")) != 0 &&
			(GL11_glTexParameteri_pointer = GLContext.getFunctionAddress("glTexParameteri")) != 0 &&
			(GL11_glTexParameterf_pointer = GLContext.getFunctionAddress("glTexParameterf")) != 0 &&
			(GL11_glTexSubImage2D_pointer = GLContext.getFunctionAddress("glTexSubImage2D")) != 0 &&
			(GL11_glTexSubImage1D_pointer = GLContext.getFunctionAddress("glTexSubImage1D")) != 0 &&
			(GL11_glTexImage2D_pointer = GLContext.getFunctionAddress("glTexImage2D")) != 0 &&
			(GL11_glTexImage1D_pointer = GLContext.getFunctionAddress("glTexImage1D")) != 0 &&
			(GL11_glTranslatef_pointer = GLContext.getFunctionAddress("glTranslatef")) != 0 &&
			(GL11_glVertex4i_pointer = GLContext.getFunctionAddress("glVertex4i")) != 0 &&
			(GL11_glVertex4f_pointer = GLContext.getFunctionAddress("glVertex4f")) != 0 &&
			(GL11_glVertex3i_pointer = GLContext.getFunctionAddress("glVertex3i")) != 0 &&
			(GL11_glVertex3f_pointer = GLContext.getFunctionAddress("glVertex3f")) != 0 &&
			(GL11_glVertex2i_pointer = GLContext.getFunctionAddress("glVertex2i")) != 0 &&
			(GL11_glVertex2f_pointer = GLContext.getFunctionAddress("glVertex2f")) != 0 &&
			(GL11_glVertexPointer_pointer = GLContext.getFunctionAddress("glVertexPointer")) != 0 &&
			(GL11_glStencilFunc_pointer = GLContext.getFunctionAddress("glStencilFunc")) != 0 &&
			(GL11_glPopAttrib_pointer = GLContext.getFunctionAddress("glPopAttrib")) != 0 &&
			(GL11_glPushAttrib_pointer = GLContext.getFunctionAddress("glPushAttrib")) != 0 &&
			(GL11_glPopClientAttrib_pointer = GLContext.getFunctionAddress("glPopClientAttrib")) != 0 &&
			(GL11_glPushClientAttrib_pointer = GLContext.getFunctionAddress("glPushClientAttrib")) != 0 &&
			(GL11_glPopMatrix_pointer = GLContext.getFunctionAddress("glPopMatrix")) != 0 &&
			(GL11_glPushMatrix_pointer = GLContext.getFunctionAddress("glPushMatrix")) != 0 &&
			(GL11_glPopName_pointer = GLContext.getFunctionAddress("glPopName")) != 0 &&
			(GL11_glPushName_pointer = GLContext.getFunctionAddress("glPushName")) != 0 &&
			(GL11_glRasterPos4i_pointer = GLContext.getFunctionAddress("glRasterPos4i")) != 0 &&
			(GL11_glRasterPos4f_pointer = GLContext.getFunctionAddress("glRasterPos4f")) != 0 &&
			(GL11_glRasterPos3i_pointer = GLContext.getFunctionAddress("glRasterPos3i")) != 0 &&
			(GL11_glRasterPos3f_pointer = GLContext.getFunctionAddress("glRasterPos3f")) != 0 &&
			(GL11_glRasterPos2i_pointer = GLContext.getFunctionAddress("glRasterPos2i")) != 0 &&
			(GL11_glRasterPos2f_pointer = GLContext.getFunctionAddress("glRasterPos2f")) != 0 &&
			(GL11_glReadBuffer_pointer = GLContext.getFunctionAddress("glReadBuffer")) != 0 &&
			(GL11_glReadPixels_pointer = GLContext.getFunctionAddress("glReadPixels")) != 0 &&
			(GL11_glRecti_pointer = GLContext.getFunctionAddress("glRecti")) != 0 &&
			(GL11_glRectf_pointer = GLContext.getFunctionAddress("glRectf")) != 0 &&
			(GL11_glRenderMode_pointer = GLContext.getFunctionAddress("glRenderMode")) != 0 &&
			(GL11_glRotatef_pointer = GLContext.getFunctionAddress("glRotatef")) != 0 &&
			(GL11_glScalef_pointer = GLContext.getFunctionAddress("glScalef")) != 0 &&
			(GL11_glScissor_pointer = GLContext.getFunctionAddress("glScissor")) != 0 &&
			(GL11_glSelectBuffer_pointer = GLContext.getFunctionAddress("glSelectBuffer")) != 0 &&
			(GL11_glShadeModel_pointer = GLContext.getFunctionAddress("glShadeModel")) != 0 &&
			(GL11_glMultMatrixf_pointer = GLContext.getFunctionAddress("glMultMatrixf")) != 0 &&
			(GL11_glEndList_pointer = GLContext.getFunctionAddress("glEndList")) != 0 &&
			(GL11_glNewList_pointer = GLContext.getFunctionAddress("glNewList")) != 0 &&
			(GL11_glNormal3i_pointer = GLContext.getFunctionAddress("glNormal3i")) != 0 &&
			(GL11_glNormal3f_pointer = GLContext.getFunctionAddress("glNormal3f")) != 0 &&
			(GL11_glNormal3b_pointer = GLContext.getFunctionAddress("glNormal3b")) != 0 &&
			(GL11_glNormalPointer_pointer = GLContext.getFunctionAddress("glNormalPointer")) != 0 &&
			(GL11_glOrtho_pointer = GLContext.getFunctionAddress("glOrtho")) != 0 &&
			(GL11_glPassThrough_pointer = GLContext.getFunctionAddress("glPassThrough")) != 0 &&
			(GL11_glPixelMapusv_pointer = GLContext.getFunctionAddress("glPixelMapusv")) != 0 &&
			(GL11_glPixelMapuiv_pointer = GLContext.getFunctionAddress("glPixelMapuiv")) != 0 &&
			(GL11_glPixelMapfv_pointer = GLContext.getFunctionAddress("glPixelMapfv")) != 0 &&
			(GL11_glPixelStorei_pointer = GLContext.getFunctionAddress("glPixelStorei")) != 0 &&
			(GL11_glPixelStoref_pointer = GLContext.getFunctionAddress("glPixelStoref")) != 0 &&
			(GL11_glPixelTransferi_pointer = GLContext.getFunctionAddress("glPixelTransferi")) != 0 &&
			(GL11_glPixelTransferf_pointer = GLContext.getFunctionAddress("glPixelTransferf")) != 0 &&
			(GL11_glPixelZoom_pointer = GLContext.getFunctionAddress("glPixelZoom")) != 0 &&
			(GL11_glPointSize_pointer = GLContext.getFunctionAddress("glPointSize")) != 0 &&
			(GL11_glPolygonMode_pointer = GLContext.getFunctionAddress("glPolygonMode")) != 0 &&
			(GL11_glPolygonOffset_pointer = GLContext.getFunctionAddress("glPolygonOffset")) != 0 &&
			(GL11_glPolygonStipple_pointer = GLContext.getFunctionAddress("glPolygonStipple")) != 0 &&
			(GL11_glMatrixMode_pointer = GLContext.getFunctionAddress("glMatrixMode")) != 0 &&
			(GL11_glIsTexture_pointer = GLContext.getFunctionAddress("glIsTexture")) != 0 &&
			(GL11_glLightiv_pointer = GLContext.getFunctionAddress("glLightiv")) != 0 &&
			(GL11_glLightfv_pointer = GLContext.getFunctionAddress("glLightfv")) != 0 &&
			(GL11_glLighti_pointer = GLContext.getFunctionAddress("glLighti")) != 0 &&
			(GL11_glLightf_pointer = GLContext.getFunctionAddress("glLightf")) != 0 &&
			(GL11_glLightModeliv_pointer = GLContext.getFunctionAddress("glLightModeliv")) != 0 &&
			(GL11_glLightModelfv_pointer = GLContext.getFunctionAddress("glLightModelfv")) != 0 &&
			(GL11_glLightModeli_pointer = GLContext.getFunctionAddress("glLightModeli")) != 0 &&
			(GL11_glLightModelf_pointer = GLContext.getFunctionAddress("glLightModelf")) != 0 &&
			(GL11_glLineStipple_pointer = GLContext.getFunctionAddress("glLineStipple")) != 0 &&
			(GL11_glLineWidth_pointer = GLContext.getFunctionAddress("glLineWidth")) != 0 &&
			(GL11_glListBase_pointer = GLContext.getFunctionAddress("glListBase")) != 0 &&
			(GL11_glLoadIdentity_pointer = GLContext.getFunctionAddress("glLoadIdentity")) != 0 &&
			(GL11_glLoadMatrixf_pointer = GLContext.getFunctionAddress("glLoadMatrixf")) != 0 &&
			(GL11_glLoadName_pointer = GLContext.getFunctionAddress("glLoadName")) != 0 &&
			(GL11_glLogicOp_pointer = GLContext.getFunctionAddress("glLogicOp")) != 0 &&
			(GL11_glMap1f_pointer = GLContext.getFunctionAddress("glMap1f")) != 0 &&
			(GL11_glMap2f_pointer = GLContext.getFunctionAddress("glMap2f")) != 0 &&
			(GL11_glMapGrid2f_pointer = GLContext.getFunctionAddress("glMapGrid2f")) != 0 &&
			(GL11_glMapGrid1f_pointer = GLContext.getFunctionAddress("glMapGrid1f")) != 0 &&
			(GL11_glMaterialiv_pointer = GLContext.getFunctionAddress("glMaterialiv")) != 0 &&
			(GL11_glMaterialfv_pointer = GLContext.getFunctionAddress("glMaterialfv")) != 0 &&
			(GL11_glMateriali_pointer = GLContext.getFunctionAddress("glMateriali")) != 0 &&
			(GL11_glMaterialf_pointer = GLContext.getFunctionAddress("glMaterialf")) != 0 &&
			(GL11_glIsList_pointer = GLContext.getFunctionAddress("glIsList")) != 0 &&
			(GL11_glGetPolygonStipple_pointer = GLContext.getFunctionAddress("glGetPolygonStipple")) != 0 &&
			(GL11_glGetString_pointer = GLContext.getFunctionAddress("glGetString")) != 0 &&
			(GL11_glGetTexEnvfv_pointer = GLContext.getFunctionAddress("glGetTexEnvfv")) != 0 &&
			(GL11_glGetTexEnviv_pointer = GLContext.getFunctionAddress("glGetTexEnviv")) != 0 &&
			(GL11_glGetTexGenfv_pointer = GLContext.getFunctionAddress("glGetTexGenfv")) != 0 &&
			(GL11_glGetTexGeniv_pointer = GLContext.getFunctionAddress("glGetTexGeniv")) != 0 &&
			(GL11_glGetTexImage_pointer = GLContext.getFunctionAddress("glGetTexImage")) != 0 &&
			(GL11_glGetTexLevelParameteriv_pointer = GLContext.getFunctionAddress("glGetTexLevelParameteriv")) != 0 &&
			(GL11_glGetTexLevelParameterfv_pointer = GLContext.getFunctionAddress("glGetTexLevelParameterfv")) != 0 &&
			(GL11_glGetTexParameteriv_pointer = GLContext.getFunctionAddress("glGetTexParameteriv")) != 0 &&
			(GL11_glGetTexParameterfv_pointer = GLContext.getFunctionAddress("glGetTexParameterfv")) != 0 &&
			(GL11_glHint_pointer = GLContext.getFunctionAddress("glHint")) != 0 &&
			(GL11_glInitNames_pointer = GLContext.getFunctionAddress("glInitNames")) != 0 &&
			(GL11_glInterleavedArrays_pointer = GLContext.getFunctionAddress("glInterleavedArrays")) != 0 &&
			(GL11_glIsEnabled_pointer = GLContext.getFunctionAddress("glIsEnabled")) != 0 &&
			(GL11_glGetPointerv_pointer = GLContext.getFunctionAddress("glGetPointerv")) != 0 &&
			(GL11_glFinish_pointer = GLContext.getFunctionAddress("glFinish")) != 0 &&
			(GL11_glFlush_pointer = GLContext.getFunctionAddress("glFlush")) != 0 &&
			(GL11_glFogiv_pointer = GLContext.getFunctionAddress("glFogiv")) != 0 &&
			(GL11_glFogfv_pointer = GLContext.getFunctionAddress("glFogfv")) != 0 &&
			(GL11_glFogi_pointer = GLContext.getFunctionAddress("glFogi")) != 0 &&
			(GL11_glFogf_pointer = GLContext.getFunctionAddress("glFogf")) != 0 &&
			(GL11_glFrontFace_pointer = GLContext.getFunctionAddress("glFrontFace")) != 0 &&
			(GL11_glFrustum_pointer = GLContext.getFunctionAddress("glFrustum")) != 0 &&
			(GL11_glGenLists_pointer = GLContext.getFunctionAddress("glGenLists")) != 0 &&
			(GL11_glGenTextures_pointer = GLContext.getFunctionAddress("glGenTextures")) != 0 &&
			(GL11_glGetIntegerv_pointer = GLContext.getFunctionAddress("glGetIntegerv")) != 0 &&
			(GL11_glGetFloatv_pointer = GLContext.getFunctionAddress("glGetFloatv")) != 0 &&
			(GL11_glGetDoublev_pointer = GLContext.getFunctionAddress("glGetDoublev")) != 0 &&
			(GL11_glGetBooleanv_pointer = GLContext.getFunctionAddress("glGetBooleanv")) != 0 &&
			(GL11_glGetClipPlane_pointer = GLContext.getFunctionAddress("glGetClipPlane")) != 0 &&
			(GL11_glGetError_pointer = GLContext.getFunctionAddress("glGetError")) != 0 &&
			(GL11_glGetLightiv_pointer = GLContext.getFunctionAddress("glGetLightiv")) != 0 &&
			(GL11_glGetLightfv_pointer = GLContext.getFunctionAddress("glGetLightfv")) != 0 &&
			(GL11_glGetMapiv_pointer = GLContext.getFunctionAddress("glGetMapiv")) != 0 &&
			(GL11_glGetMapfv_pointer = GLContext.getFunctionAddress("glGetMapfv")) != 0 &&
			(GL11_glGetMaterialiv_pointer = GLContext.getFunctionAddress("glGetMaterialiv")) != 0 &&
			(GL11_glGetMaterialfv_pointer = GLContext.getFunctionAddress("glGetMaterialfv")) != 0 &&
			(GL11_glGetPixelMapusv_pointer = GLContext.getFunctionAddress("glGetPixelMapusv")) != 0 &&
			(GL11_glGetPixelMapuiv_pointer = GLContext.getFunctionAddress("glGetPixelMapuiv")) != 0 &&
			(GL11_glGetPixelMapfv_pointer = GLContext.getFunctionAddress("glGetPixelMapfv")) != 0 &&
			(GL11_glFeedbackBuffer_pointer = GLContext.getFunctionAddress("glFeedbackBuffer")) != 0 &&
			(GL11_glDepthFunc_pointer = GLContext.getFunctionAddress("glDepthFunc")) != 0 &&
			(GL11_glDepthMask_pointer = GLContext.getFunctionAddress("glDepthMask")) != 0 &&
			(GL11_glDepthRange_pointer = GLContext.getFunctionAddress("glDepthRange")) != 0 &&
			(GL11_glDrawArrays_pointer = GLContext.getFunctionAddress("glDrawArrays")) != 0 &&
			(GL11_glDrawBuffer_pointer = GLContext.getFunctionAddress("glDrawBuffer")) != 0 &&
			(GL11_glDrawElements_pointer = GLContext.getFunctionAddress("glDrawElements")) != 0 &&
			(GL11_glDrawPixels_pointer = GLContext.getFunctionAddress("glDrawPixels")) != 0 &&
			(GL11_glEdgeFlag_pointer = GLContext.getFunctionAddress("glEdgeFlag")) != 0 &&
			(GL11_glEdgeFlagPointer_pointer = GLContext.getFunctionAddress("glEdgeFlagPointer")) != 0 &&
			(GL11_glDisable_pointer = GLContext.getFunctionAddress("glDisable")) != 0 &&
			(GL11_glEnable_pointer = GLContext.getFunctionAddress("glEnable")) != 0 &&
			(GL11_glDisableClientState_pointer = GLContext.getFunctionAddress("glDisableClientState")) != 0 &&
			(GL11_glEnableClientState_pointer = GLContext.getFunctionAddress("glEnableClientState")) != 0 &&
			(GL11_glEvalCoord2f_pointer = GLContext.getFunctionAddress("glEvalCoord2f")) != 0 &&
			(GL11_glEvalCoord1f_pointer = GLContext.getFunctionAddress("glEvalCoord1f")) != 0 &&
			(GL11_glEvalMesh2_pointer = GLContext.getFunctionAddress("glEvalMesh2")) != 0 &&
			(GL11_glEvalMesh1_pointer = GLContext.getFunctionAddress("glEvalMesh1")) != 0 &&
			(GL11_glEvalPoint2_pointer = GLContext.getFunctionAddress("glEvalPoint2")) != 0 &&
			(GL11_glEvalPoint1_pointer = GLContext.getFunctionAddress("glEvalPoint1")) != 0 &&
			(GL11_glClearIndex_pointer = GLContext.getFunctionAddress("glClearIndex")) != 0 &&
			(GL11_glClearStencil_pointer = GLContext.getFunctionAddress("glClearStencil")) != 0 &&
			(GL11_glClipPlane_pointer = GLContext.getFunctionAddress("glClipPlane")) != 0 &&
			(GL11_glColor4ub_pointer = GLContext.getFunctionAddress("glColor4ub")) != 0 &&
			(GL11_glColor4f_pointer = GLContext.getFunctionAddress("glColor4f")) != 0 &&
			(GL11_glColor4b_pointer = GLContext.getFunctionAddress("glColor4b")) != 0 &&
			(GL11_glColor3ub_pointer = GLContext.getFunctionAddress("glColor3ub")) != 0 &&
			(GL11_glColor3f_pointer = GLContext.getFunctionAddress("glColor3f")) != 0 &&
			(GL11_glColor3b_pointer = GLContext.getFunctionAddress("glColor3b")) != 0 &&
			(GL11_glColorMask_pointer = GLContext.getFunctionAddress("glColorMask")) != 0 &&
			(GL11_glColorMaterial_pointer = GLContext.getFunctionAddress("glColorMaterial")) != 0 &&
			(GL11_glColorPointer_pointer = GLContext.getFunctionAddress("glColorPointer")) != 0 &&
			(GL11_glCopyPixels_pointer = GLContext.getFunctionAddress("glCopyPixels")) != 0 &&
			(GL11_glCopyTexImage1D_pointer = GLContext.getFunctionAddress("glCopyTexImage1D")) != 0 &&
			(GL11_glCopyTexImage2D_pointer = GLContext.getFunctionAddress("glCopyTexImage2D")) != 0 &&
			(GL11_glCopyTexSubImage1D_pointer = GLContext.getFunctionAddress("glCopyTexSubImage1D")) != 0 &&
			(GL11_glCopyTexSubImage2D_pointer = GLContext.getFunctionAddress("glCopyTexSubImage2D")) != 0 &&
			(GL11_glCullFace_pointer = GLContext.getFunctionAddress("glCullFace")) != 0 &&
			(GL11_glDeleteTextures_pointer = GLContext.getFunctionAddress("glDeleteTextures")) != 0 &&
			(GL11_glDeleteLists_pointer = GLContext.getFunctionAddress("glDeleteLists")) != 0 &&
			(GL11_glClearDepth_pointer = GLContext.getFunctionAddress("glClearDepth")) != 0 &&
			(GL11_glArrayElement_pointer = GLContext.getFunctionAddress("glArrayElement")) != 0 &&
			(GL11_glEnd_pointer = GLContext.getFunctionAddress("glEnd")) != 0 &&
			(GL11_glBegin_pointer = GLContext.getFunctionAddress("glBegin")) != 0 &&
			(GL11_glBindTexture_pointer = GLContext.getFunctionAddress("glBindTexture")) != 0 &&
			(GL11_glBitmap_pointer = GLContext.getFunctionAddress("glBitmap")) != 0 &&
			(GL11_glBlendFunc_pointer = GLContext.getFunctionAddress("glBlendFunc")) != 0 &&
			(GL11_glCallList_pointer = GLContext.getFunctionAddress("glCallList")) != 0 &&
			(GL11_glCallLists_pointer = GLContext.getFunctionAddress("glCallLists")) != 0 &&
			(GL11_glClear_pointer = GLContext.getFunctionAddress("glClear")) != 0 &&
			(GL11_glClearAccum_pointer = GLContext.getFunctionAddress("glClearAccum")) != 0 &&
			(GL11_glClearColor_pointer = GLContext.getFunctionAddress("glClearColor")) != 0 &&
			(GL11_glAlphaFunc_pointer = GLContext.getFunctionAddress("glAlphaFunc")) != 0 &&
			(GL11_glAccum_pointer = GLContext.getFunctionAddress("glAccum")) != 0;
	}

	private boolean GL12_initNativeFunctionAddresses() {
		return 
			(GL12_glCopyTexSubImage3D_pointer = GLContext.getFunctionAddress("glCopyTexSubImage3D")) != 0 &&
			(GL12_glTexSubImage3D_pointer = GLContext.getFunctionAddress("glTexSubImage3D")) != 0 &&
			(GL12_glTexImage3D_pointer = GLContext.getFunctionAddress("glTexImage3D")) != 0 &&
			(GL12_glDrawRangeElements_pointer = GLContext.getFunctionAddress("glDrawRangeElements")) != 0;
	}

	private boolean GL13_initNativeFunctionAddresses() {
		return 
			(GL13_glSampleCoverage_pointer = GLContext.getFunctionAddress("glSampleCoverage")) != 0 &&
			(GL13_glMultTransposeMatrixf_pointer = GLContext.getFunctionAddress("glMultTransposeMatrixf")) != 0 &&
			(GL13_glLoadTransposeMatrixf_pointer = GLContext.getFunctionAddress("glLoadTransposeMatrixf")) != 0 &&
			(GL13_glMultiTexCoord4f_pointer = GLContext.getFunctionAddress("glMultiTexCoord4f")) != 0 &&
			(GL13_glMultiTexCoord3f_pointer = GLContext.getFunctionAddress("glMultiTexCoord3f")) != 0 &&
			(GL13_glMultiTexCoord2f_pointer = GLContext.getFunctionAddress("glMultiTexCoord2f")) != 0 &&
			(GL13_glMultiTexCoord1f_pointer = GLContext.getFunctionAddress("glMultiTexCoord1f")) != 0 &&
			(GL13_glGetCompressedTexImage_pointer = GLContext.getFunctionAddress("glGetCompressedTexImage")) != 0 &&
			(GL13_glCompressedTexSubImage3D_pointer = GLContext.getFunctionAddress("glCompressedTexSubImage3D")) != 0 &&
			(GL13_glCompressedTexSubImage2D_pointer = GLContext.getFunctionAddress("glCompressedTexSubImage2D")) != 0 &&
			(GL13_glCompressedTexSubImage1D_pointer = GLContext.getFunctionAddress("glCompressedTexSubImage1D")) != 0 &&
			(GL13_glCompressedTexImage3D_pointer = GLContext.getFunctionAddress("glCompressedTexImage3D")) != 0 &&
			(GL13_glCompressedTexImage2D_pointer = GLContext.getFunctionAddress("glCompressedTexImage2D")) != 0 &&
			(GL13_glCompressedTexImage1D_pointer = GLContext.getFunctionAddress("glCompressedTexImage1D")) != 0 &&
			(GL13_glClientActiveTexture_pointer = GLContext.getFunctionAddress("glClientActiveTexture")) != 0 &&
			(GL13_glActiveTexture_pointer = GLContext.getFunctionAddress("glActiveTexture")) != 0;
	}

	private boolean GL14_initNativeFunctionAddresses() {
		return 
			(GL14_glWindowPos3i_pointer = GLContext.getFunctionAddress("glWindowPos3i")) != 0 &&
			(GL14_glWindowPos3f_pointer = GLContext.getFunctionAddress("glWindowPos3f")) != 0 &&
			(GL14_glWindowPos2i_pointer = GLContext.getFunctionAddress("glWindowPos2i")) != 0 &&
			(GL14_glWindowPos2f_pointer = GLContext.getFunctionAddress("glWindowPos2f")) != 0 &&
			(GL14_glBlendFuncSeparate_pointer = GLContext.getFunctionAddress("glBlendFuncSeparate")) != 0 &&
			(GL14_glSecondaryColorPointer_pointer = GLContext.getFunctionAddress("glSecondaryColorPointer")) != 0 &&
			(GL14_glSecondaryColor3ub_pointer = GLContext.getFunctionAddress("glSecondaryColor3ub")) != 0 &&
			(GL14_glSecondaryColor3f_pointer = GLContext.getFunctionAddress("glSecondaryColor3f")) != 0 &&
			(GL14_glSecondaryColor3b_pointer = GLContext.getFunctionAddress("glSecondaryColor3b")) != 0 &&
			(GL14_glPointParameterfv_pointer = GLContext.getFunctionAddress("glPointParameterfv")) != 0 &&
			(GL14_glPointParameteriv_pointer = GLContext.getFunctionAddress("glPointParameteriv")) != 0 &&
			(GL14_glPointParameterf_pointer = GLContext.getFunctionAddress("glPointParameterf")) != 0 &&
			(GL14_glPointParameteri_pointer = GLContext.getFunctionAddress("glPointParameteri")) != 0 &&
			(GL14_glMultiDrawArrays_pointer = GLContext.getFunctionAddress("glMultiDrawArrays")) != 0 &&
			(GL14_glFogCoordPointer_pointer = GLContext.getFunctionAddress("glFogCoordPointer")) != 0 &&
			(GL14_glFogCoordf_pointer = GLContext.getFunctionAddress("glFogCoordf")) != 0 &&
			(GL14_glBlendColor_pointer = GLContext.getFunctionAddress("glBlendColor")) != 0 &&
			(GL14_glBlendEquation_pointer = GLContext.getFunctionAddress("glBlendEquation")) != 0;
	}

	private boolean GL15_initNativeFunctionAddresses() {
		return 
			(GL15_glGetQueryObjectuiv_pointer = GLContext.getFunctionAddress("glGetQueryObjectuiv")) != 0 &&
			(GL15_glGetQueryObjectiv_pointer = GLContext.getFunctionAddress("glGetQueryObjectiv")) != 0 &&
			(GL15_glGetQueryiv_pointer = GLContext.getFunctionAddress("glGetQueryiv")) != 0 &&
			(GL15_glEndQuery_pointer = GLContext.getFunctionAddress("glEndQuery")) != 0 &&
			(GL15_glBeginQuery_pointer = GLContext.getFunctionAddress("glBeginQuery")) != 0 &&
			(GL15_glIsQuery_pointer = GLContext.getFunctionAddress("glIsQuery")) != 0 &&
			(GL15_glDeleteQueries_pointer = GLContext.getFunctionAddress("glDeleteQueries")) != 0 &&
			(GL15_glGenQueries_pointer = GLContext.getFunctionAddress("glGenQueries")) != 0 &&
			(GL15_glGetBufferPointerv_pointer = GLContext.getFunctionAddress("glGetBufferPointerv")) != 0 &&
			(GL15_glGetBufferParameteriv_pointer = GLContext.getFunctionAddress("glGetBufferParameteriv")) != 0 &&
			(GL15_glUnmapBuffer_pointer = GLContext.getFunctionAddress("glUnmapBuffer")) != 0 &&
			(GL15_glMapBuffer_pointer = GLContext.getFunctionAddress("glMapBuffer")) != 0 &&
			(GL15_glGetBufferSubData_pointer = GLContext.getFunctionAddress("glGetBufferSubData")) != 0 &&
			(GL15_glBufferSubData_pointer = GLContext.getFunctionAddress("glBufferSubData")) != 0 &&
			(GL15_glBufferData_pointer = GLContext.getFunctionAddress("glBufferData")) != 0 &&
			(GL15_glIsBuffer_pointer = GLContext.getFunctionAddress("glIsBuffer")) != 0 &&
			(GL15_glGenBuffers_pointer = GLContext.getFunctionAddress("glGenBuffers")) != 0 &&
			(GL15_glDeleteBuffers_pointer = GLContext.getFunctionAddress("glDeleteBuffers")) != 0 &&
			(GL15_glBindBuffer_pointer = GLContext.getFunctionAddress("glBindBuffer")) != 0;
	}

	private boolean GL20_initNativeFunctionAddresses() {
		return 
			(GL20_glBlendEquationSeparate_pointer = GLContext.getFunctionAddress("glBlendEquationSeparate")) != 0 &&
			(GL20_glStencilMaskSeparate_pointer = GLContext.getFunctionAddress("glStencilMaskSeparate")) != 0 &&
			(GL20_glStencilFuncSeparate_pointer = GLContext.getFunctionAddress("glStencilFuncSeparate")) != 0 &&
			(GL20_glStencilOpSeparate_pointer = GLContext.getFunctionAddress("glStencilOpSeparate")) != 0 &&
			(GL20_glDrawBuffers_pointer = GLContext.getFunctionAddress("glDrawBuffers")) != 0 &&
			(GL20_glGetAttribLocation_pointer = GLContext.getFunctionAddress("glGetAttribLocation")) != 0 &&
			(GL20_glGetActiveAttrib_pointer = GLContext.getFunctionAddress("glGetActiveAttrib")) != 0 &&
			(GL20_glBindAttribLocation_pointer = GLContext.getFunctionAddress("glBindAttribLocation")) != 0 &&
			(GL20_glGetVertexAttribPointerv_pointer = GLContext.getFunctionAddress("glGetVertexAttribPointerv")) != 0 &&
			(GL20_glGetVertexAttribiv_pointer = GLContext.getFunctionAddress("glGetVertexAttribiv")) != 0 &&
			(GL20_glGetVertexAttribfv_pointer = GLContext.getFunctionAddress("glGetVertexAttribfv")) != 0 &&
			(GL20_glDisableVertexAttribArray_pointer = GLContext.getFunctionAddress("glDisableVertexAttribArray")) != 0 &&
			(GL20_glEnableVertexAttribArray_pointer = GLContext.getFunctionAddress("glEnableVertexAttribArray")) != 0 &&
			(GL20_glVertexAttribPointer_pointer = GLContext.getFunctionAddress("glVertexAttribPointer")) != 0 &&
			(GL20_glVertexAttrib4Nub_pointer = GLContext.getFunctionAddress("glVertexAttrib4Nub")) != 0 &&
			(GL20_glVertexAttrib4f_pointer = GLContext.getFunctionAddress("glVertexAttrib4f")) != 0 &&
			(GL20_glVertexAttrib4s_pointer = GLContext.getFunctionAddress("glVertexAttrib4s")) != 0 &&
			(GL20_glVertexAttrib3f_pointer = GLContext.getFunctionAddress("glVertexAttrib3f")) != 0 &&
			(GL20_glVertexAttrib3s_pointer = GLContext.getFunctionAddress("glVertexAttrib3s")) != 0 &&
			(GL20_glVertexAttrib2f_pointer = GLContext.getFunctionAddress("glVertexAttrib2f")) != 0 &&
			(GL20_glVertexAttrib2s_pointer = GLContext.getFunctionAddress("glVertexAttrib2s")) != 0 &&
			(GL20_glVertexAttrib1f_pointer = GLContext.getFunctionAddress("glVertexAttrib1f")) != 0 &&
			(GL20_glVertexAttrib1s_pointer = GLContext.getFunctionAddress("glVertexAttrib1s")) != 0 &&
			(GL20_glGetShaderSource_pointer = GLContext.getFunctionAddress("glGetShaderSource")) != 0 &&
			(GL20_glGetUniformiv_pointer = GLContext.getFunctionAddress("glGetUniformiv")) != 0 &&
			(GL20_glGetUniformfv_pointer = GLContext.getFunctionAddress("glGetUniformfv")) != 0 &&
			(GL20_glGetActiveUniform_pointer = GLContext.getFunctionAddress("glGetActiveUniform")) != 0 &&
			(GL20_glGetUniformLocation_pointer = GLContext.getFunctionAddress("glGetUniformLocation")) != 0 &&
			(GL20_glGetAttachedShaders_pointer = GLContext.getFunctionAddress("glGetAttachedShaders")) != 0 &&
			(GL20_glGetProgramInfoLog_pointer = GLContext.getFunctionAddress("glGetProgramInfoLog")) != 0 &&
			(GL20_glGetShaderInfoLog_pointer = GLContext.getFunctionAddress("glGetShaderInfoLog")) != 0 &&
			(GL20_glGetProgramiv_pointer = GLContext.getFunctionAddress("glGetProgramiv")) != 0 &&
			(GL20_glGetShaderiv_pointer = GLContext.getFunctionAddress("glGetShaderiv")) != 0 &&
			(GL20_glUniformMatrix4fv_pointer = GLContext.getFunctionAddress("glUniformMatrix4fv")) != 0 &&
			(GL20_glUniformMatrix3fv_pointer = GLContext.getFunctionAddress("glUniformMatrix3fv")) != 0 &&
			(GL20_glUniformMatrix2fv_pointer = GLContext.getFunctionAddress("glUniformMatrix2fv")) != 0 &&
			(GL20_glUniform4iv_pointer = GLContext.getFunctionAddress("glUniform4iv")) != 0 &&
			(GL20_glUniform3iv_pointer = GLContext.getFunctionAddress("glUniform3iv")) != 0 &&
			(GL20_glUniform2iv_pointer = GLContext.getFunctionAddress("glUniform2iv")) != 0 &&
			(GL20_glUniform1iv_pointer = GLContext.getFunctionAddress("glUniform1iv")) != 0 &&
			(GL20_glUniform4fv_pointer = GLContext.getFunctionAddress("glUniform4fv")) != 0 &&
			(GL20_glUniform3fv_pointer = GLContext.getFunctionAddress("glUniform3fv")) != 0 &&
			(GL20_glUniform2fv_pointer = GLContext.getFunctionAddress("glUniform2fv")) != 0 &&
			(GL20_glUniform1fv_pointer = GLContext.getFunctionAddress("glUniform1fv")) != 0 &&
			(GL20_glUniform4i_pointer = GLContext.getFunctionAddress("glUniform4i")) != 0 &&
			(GL20_glUniform3i_pointer = GLContext.getFunctionAddress("glUniform3i")) != 0 &&
			(GL20_glUniform2i_pointer = GLContext.getFunctionAddress("glUniform2i")) != 0 &&
			(GL20_glUniform1i_pointer = GLContext.getFunctionAddress("glUniform1i")) != 0 &&
			(GL20_glUniform4f_pointer = GLContext.getFunctionAddress("glUniform4f")) != 0 &&
			(GL20_glUniform3f_pointer = GLContext.getFunctionAddress("glUniform3f")) != 0 &&
			(GL20_glUniform2f_pointer = GLContext.getFunctionAddress("glUniform2f")) != 0 &&
			(GL20_glUniform1f_pointer = GLContext.getFunctionAddress("glUniform1f")) != 0 &&
			(GL20_glDeleteProgram_pointer = GLContext.getFunctionAddress("glDeleteProgram")) != 0 &&
			(GL20_glValidateProgram_pointer = GLContext.getFunctionAddress("glValidateProgram")) != 0 &&
			(GL20_glUseProgram_pointer = GLContext.getFunctionAddress("glUseProgram")) != 0 &&
			(GL20_glLinkProgram_pointer = GLContext.getFunctionAddress("glLinkProgram")) != 0 &&
			(GL20_glDetachShader_pointer = GLContext.getFunctionAddress("glDetachShader")) != 0 &&
			(GL20_glAttachShader_pointer = GLContext.getFunctionAddress("glAttachShader")) != 0 &&
			(GL20_glIsProgram_pointer = GLContext.getFunctionAddress("glIsProgram")) != 0 &&
			(GL20_glCreateProgram_pointer = GLContext.getFunctionAddress("glCreateProgram")) != 0 &&
			(GL20_glDeleteShader_pointer = GLContext.getFunctionAddress("glDeleteShader")) != 0 &&
			(GL20_glCompileShader_pointer = GLContext.getFunctionAddress("glCompileShader")) != 0 &&
			(GL20_glIsShader_pointer = GLContext.getFunctionAddress("glIsShader")) != 0 &&
			(GL20_glCreateShader_pointer = GLContext.getFunctionAddress("glCreateShader")) != 0 &&
			(GL20_glShaderSource_pointer = GLContext.getFunctionAddress("glShaderSource")) != 0;
	}

	private boolean NV_evaluators_initNativeFunctionAddresses() {
		return 
			(NV_evaluators_glEvalMapsNV_pointer = GLContext.getFunctionAddress("glEvalMapsNV")) != 0 &&
			(NV_evaluators_glGetMapAttribParameterivNV_pointer = GLContext.getFunctionAddress("glGetMapAttribParameterivNV")) != 0 &&
			(NV_evaluators_glGetMapAttribParameterfvNV_pointer = GLContext.getFunctionAddress("glGetMapAttribParameterfvNV")) != 0 &&
			(NV_evaluators_glGetMapParameterivNV_pointer = GLContext.getFunctionAddress("glGetMapParameterivNV")) != 0 &&
			(NV_evaluators_glGetMapParameterfvNV_pointer = GLContext.getFunctionAddress("glGetMapParameterfvNV")) != 0 &&
			(NV_evaluators_glMapParameterivNV_pointer = GLContext.getFunctionAddress("glMapParameterivNV")) != 0 &&
			(NV_evaluators_glMapParameterfvNV_pointer = GLContext.getFunctionAddress("glMapParameterfvNV")) != 0 &&
			(NV_evaluators_glMapControlPointsNV_pointer = GLContext.getFunctionAddress("glMapControlPointsNV")) != 0 &&
			(NV_evaluators_glGetMapControlPointsNV_pointer = GLContext.getFunctionAddress("glGetMapControlPointsNV")) != 0;
	}

	private boolean NV_fence_initNativeFunctionAddresses() {
		return 
			(NV_fence_glGetFenceivNV_pointer = GLContext.getFunctionAddress("glGetFenceivNV")) != 0 &&
			(NV_fence_glIsFenceNV_pointer = GLContext.getFunctionAddress("glIsFenceNV")) != 0 &&
			(NV_fence_glFinishFenceNV_pointer = GLContext.getFunctionAddress("glFinishFenceNV")) != 0 &&
			(NV_fence_glTestFenceNV_pointer = GLContext.getFunctionAddress("glTestFenceNV")) != 0 &&
			(NV_fence_glSetFenceNV_pointer = GLContext.getFunctionAddress("glSetFenceNV")) != 0 &&
			(NV_fence_glDeleteFencesNV_pointer = GLContext.getFunctionAddress("glDeleteFencesNV")) != 0 &&
			(NV_fence_glGenFencesNV_pointer = GLContext.getFunctionAddress("glGenFencesNV")) != 0;
	}

	private boolean NV_fragment_program_initNativeFunctionAddresses() {
		return 
			(NV_fragment_program_glGetProgramNamedParameterfvNV_pointer = GLContext.getFunctionAddress("glGetProgramNamedParameterfvNV")) != 0 &&
			(NV_fragment_program_glProgramNamedParameter4fNV_pointer = GLContext.getFunctionAddress("glProgramNamedParameter4fNV")) != 0;
	}

	private boolean NV_half_float_initNativeFunctionAddresses() {
		return 
			(NV_half_float_glVertexAttribs4hvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs4hvNV")) != 0 &&
			(NV_half_float_glVertexAttribs3hvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs3hvNV")) != 0 &&
			(NV_half_float_glVertexAttribs2hvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs2hvNV")) != 0 &&
			(NV_half_float_glVertexAttribs1hvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs1hvNV")) != 0 &&
			(NV_half_float_glVertexAttrib4hNV_pointer = GLContext.getFunctionAddress("glVertexAttrib4hNV")) != 0 &&
			(NV_half_float_glVertexAttrib3hNV_pointer = GLContext.getFunctionAddress("glVertexAttrib3hNV")) != 0 &&
			(NV_half_float_glVertexAttrib2hNV_pointer = GLContext.getFunctionAddress("glVertexAttrib2hNV")) != 0 &&
			(NV_half_float_glVertexAttrib1hNV_pointer = GLContext.getFunctionAddress("glVertexAttrib1hNV")) != 0 &&
			(NV_half_float_glSecondaryColor3hNV_pointer = GLContext.getFunctionAddress("glSecondaryColor3hNV")) != 0 &&
			(NV_half_float_glFogCoordhNV_pointer = GLContext.getFunctionAddress("glFogCoordhNV")) != 0 &&
			(NV_half_float_glMultiTexCoord4hNV_pointer = GLContext.getFunctionAddress("glMultiTexCoord4hNV")) != 0 &&
			(NV_half_float_glMultiTexCoord3hNV_pointer = GLContext.getFunctionAddress("glMultiTexCoord3hNV")) != 0 &&
			(NV_half_float_glMultiTexCoord2hNV_pointer = GLContext.getFunctionAddress("glMultiTexCoord2hNV")) != 0 &&
			(NV_half_float_glMultiTexCoord1hNV_pointer = GLContext.getFunctionAddress("glMultiTexCoord1hNV")) != 0 &&
			(NV_half_float_glTexCoord4hNV_pointer = GLContext.getFunctionAddress("glTexCoord4hNV")) != 0 &&
			(NV_half_float_glTexCoord3hNV_pointer = GLContext.getFunctionAddress("glTexCoord3hNV")) != 0 &&
			(NV_half_float_glTexCoord2hNV_pointer = GLContext.getFunctionAddress("glTexCoord2hNV")) != 0 &&
			(NV_half_float_glTexCoord1hNV_pointer = GLContext.getFunctionAddress("glTexCoord1hNV")) != 0 &&
			(NV_half_float_glColor4hNV_pointer = GLContext.getFunctionAddress("glColor4hNV")) != 0 &&
			(NV_half_float_glColor3hNV_pointer = GLContext.getFunctionAddress("glColor3hNV")) != 0 &&
			(NV_half_float_glNormal3hNV_pointer = GLContext.getFunctionAddress("glNormal3hNV")) != 0 &&
			(NV_half_float_glVertex4hNV_pointer = GLContext.getFunctionAddress("glVertex4hNV")) != 0 &&
			(NV_half_float_glVertex3hNV_pointer = GLContext.getFunctionAddress("glVertex3hNV")) != 0 &&
			(NV_half_float_glVertex2hNV_pointer = GLContext.getFunctionAddress("glVertex2hNV")) != 0;
	}

	private boolean NV_occlusion_query_initNativeFunctionAddresses() {
		return 
			(NV_occlusion_query_glGetOcclusionQueryivNV_pointer = GLContext.getFunctionAddress("glGetOcclusionQueryivNV")) != 0 &&
			(NV_occlusion_query_glGetOcclusionQueryuivNV_pointer = GLContext.getFunctionAddress("glGetOcclusionQueryuivNV")) != 0 &&
			(NV_occlusion_query_glEndOcclusionQueryNV_pointer = GLContext.getFunctionAddress("glEndOcclusionQueryNV")) != 0 &&
			(NV_occlusion_query_glBeginOcclusionQueryNV_pointer = GLContext.getFunctionAddress("glBeginOcclusionQueryNV")) != 0 &&
			(NV_occlusion_query_glIsOcclusionQueryNV_pointer = GLContext.getFunctionAddress("glIsOcclusionQueryNV")) != 0 &&
			(NV_occlusion_query_glDeleteOcclusionQueriesNV_pointer = GLContext.getFunctionAddress("glDeleteOcclusionQueriesNV")) != 0 &&
			(NV_occlusion_query_glGenOcclusionQueriesNV_pointer = GLContext.getFunctionAddress("glGenOcclusionQueriesNV")) != 0;
	}

	private boolean NV_pixel_data_range_initNativeFunctionAddresses() {
		return 
			(NV_pixel_data_range_glFlushPixelDataRangeNV_pointer = GLContext.getFunctionAddress("glFlushPixelDataRangeNV")) != 0 &&
			(NV_pixel_data_range_glPixelDataRangeNV_pointer = GLContext.getFunctionAddress("glPixelDataRangeNV")) != 0;
	}

	private boolean NV_point_sprite_initNativeFunctionAddresses() {
		return 
			(NV_point_sprite_glPointParameterivNV_pointer = GLContext.getFunctionAddress("glPointParameterivNV")) != 0 &&
			(NV_point_sprite_glPointParameteriNV_pointer = GLContext.getFunctionAddress("glPointParameteriNV")) != 0;
	}

	private boolean NV_primitive_restart_initNativeFunctionAddresses() {
		return 
			(NV_primitive_restart_glPrimitiveRestartIndexNV_pointer = GLContext.getFunctionAddress("glPrimitiveRestartIndexNV")) != 0 &&
			(NV_primitive_restart_glPrimitiveRestartNV_pointer = GLContext.getFunctionAddress("glPrimitiveRestartNV")) != 0;
	}

	private boolean NV_program_initNativeFunctionAddresses() {
		return 
			(NV_program_glRequestResidentProgramsNV_pointer = GLContext.getFunctionAddress("glRequestResidentProgramsNV")) != 0 &&
			(NV_program_glAreProgramsResidentNV_pointer = GLContext.getFunctionAddress("glAreProgramsResidentNV")) != 0 &&
			(NV_program_glIsProgramNV_pointer = GLContext.getFunctionAddress("glIsProgramNV")) != 0 &&
			(NV_program_glGetProgramStringNV_pointer = GLContext.getFunctionAddress("glGetProgramStringNV")) != 0 &&
			(NV_program_glGetProgramivNV_pointer = GLContext.getFunctionAddress("glGetProgramivNV")) != 0 &&
			(NV_program_glGenProgramsNV_pointer = GLContext.getFunctionAddress("glGenProgramsNV")) != 0 &&
			(NV_program_glDeleteProgramsNV_pointer = GLContext.getFunctionAddress("glDeleteProgramsNV")) != 0 &&
			(NV_program_glBindProgramNV_pointer = GLContext.getFunctionAddress("glBindProgramNV")) != 0 &&
			(NV_program_glLoadProgramNV_pointer = GLContext.getFunctionAddress("glLoadProgramNV")) != 0;
	}

	private boolean NV_register_combiners_initNativeFunctionAddresses() {
		return 
			(NV_register_combiners_glGetFinalCombinerInputParameterivNV_pointer = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterivNV")) != 0 &&
			(NV_register_combiners_glGetFinalCombinerInputParameterfvNV_pointer = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterfvNV")) != 0 &&
			(NV_register_combiners_glGetCombinerOutputParameterivNV_pointer = GLContext.getFunctionAddress("glGetCombinerOutputParameterivNV")) != 0 &&
			(NV_register_combiners_glGetCombinerOutputParameterfvNV_pointer = GLContext.getFunctionAddress("glGetCombinerOutputParameterfvNV")) != 0 &&
			(NV_register_combiners_glGetCombinerInputParameterivNV_pointer = GLContext.getFunctionAddress("glGetCombinerInputParameterivNV")) != 0 &&
			(NV_register_combiners_glGetCombinerInputParameterfvNV_pointer = GLContext.getFunctionAddress("glGetCombinerInputParameterfvNV")) != 0 &&
			(NV_register_combiners_glFinalCombinerInputNV_pointer = GLContext.getFunctionAddress("glFinalCombinerInputNV")) != 0 &&
			(NV_register_combiners_glCombinerOutputNV_pointer = GLContext.getFunctionAddress("glCombinerOutputNV")) != 0 &&
			(NV_register_combiners_glCombinerInputNV_pointer = GLContext.getFunctionAddress("glCombinerInputNV")) != 0 &&
			(NV_register_combiners_glCombinerParameterivNV_pointer = GLContext.getFunctionAddress("glCombinerParameterivNV")) != 0 &&
			(NV_register_combiners_glCombinerParameteriNV_pointer = GLContext.getFunctionAddress("glCombinerParameteriNV")) != 0 &&
			(NV_register_combiners_glCombinerParameterfvNV_pointer = GLContext.getFunctionAddress("glCombinerParameterfvNV")) != 0 &&
			(NV_register_combiners_glCombinerParameterfNV_pointer = GLContext.getFunctionAddress("glCombinerParameterfNV")) != 0;
	}

	private boolean NV_register_combiners2_initNativeFunctionAddresses() {
		return 
			(NV_register_combiners2_glGetCombinerStageParameterfvNV_pointer = GLContext.getFunctionAddress("glGetCombinerStageParameterfvNV")) != 0 &&
			(NV_register_combiners2_glCombinerStageParameterfvNV_pointer = GLContext.getFunctionAddress("glCombinerStageParameterfvNV")) != 0;
	}

	private boolean NV_vertex_array_range_initNativeFunctionAddresses() {
		return 
			(NV_vertex_array_range_glFreeMemoryNV_pointer = GLContext.getPlatformSpecificFunctionAddress("gl", new String[]{"Windows", "Linux"}, new String[]{"wgl", "glX"}, "glFreeMemoryNV")) != 0 &&
			(NV_vertex_array_range_glAllocateMemoryNV_pointer = GLContext.getPlatformSpecificFunctionAddress("gl", new String[]{"Windows", "Linux"}, new String[]{"wgl", "glX"}, "glAllocateMemoryNV")) != 0 &&
			(NV_vertex_array_range_glFlushVertexArrayRangeNV_pointer = GLContext.getFunctionAddress("glFlushVertexArrayRangeNV")) != 0 &&
			(NV_vertex_array_range_glVertexArrayRangeNV_pointer = GLContext.getFunctionAddress("glVertexArrayRangeNV")) != 0;
	}

	private boolean NV_vertex_program_initNativeFunctionAddresses() {
		return 
			(NV_vertex_program_glVertexAttribs4fvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs4fvNV")) != 0 &&
			(NV_vertex_program_glVertexAttribs4svNV_pointer = GLContext.getFunctionAddress("glVertexAttribs4svNV")) != 0 &&
			(NV_vertex_program_glVertexAttribs3fvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs3fvNV")) != 0 &&
			(NV_vertex_program_glVertexAttribs3svNV_pointer = GLContext.getFunctionAddress("glVertexAttribs3svNV")) != 0 &&
			(NV_vertex_program_glVertexAttribs2fvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs2fvNV")) != 0 &&
			(NV_vertex_program_glVertexAttribs2svNV_pointer = GLContext.getFunctionAddress("glVertexAttribs2svNV")) != 0 &&
			(NV_vertex_program_glVertexAttribs1fvNV_pointer = GLContext.getFunctionAddress("glVertexAttribs1fvNV")) != 0 &&
			(NV_vertex_program_glVertexAttribs1svNV_pointer = GLContext.getFunctionAddress("glVertexAttribs1svNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib4ubNV_pointer = GLContext.getFunctionAddress("glVertexAttrib4ubNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib4fNV_pointer = GLContext.getFunctionAddress("glVertexAttrib4fNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib4sNV_pointer = GLContext.getFunctionAddress("glVertexAttrib4sNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib3fNV_pointer = GLContext.getFunctionAddress("glVertexAttrib3fNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib3sNV_pointer = GLContext.getFunctionAddress("glVertexAttrib3sNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib2fNV_pointer = GLContext.getFunctionAddress("glVertexAttrib2fNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib2sNV_pointer = GLContext.getFunctionAddress("glVertexAttrib2sNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib1fNV_pointer = GLContext.getFunctionAddress("glVertexAttrib1fNV")) != 0 &&
			(NV_vertex_program_glVertexAttrib1sNV_pointer = GLContext.getFunctionAddress("glVertexAttrib1sNV")) != 0 &&
			(NV_vertex_program_glVertexAttribPointerNV_pointer = GLContext.getFunctionAddress("glVertexAttribPointerNV")) != 0 &&
			(NV_vertex_program_glTrackMatrixNV_pointer = GLContext.getFunctionAddress("glTrackMatrixNV")) != 0 &&
			(NV_vertex_program_glProgramParameters4fvNV_pointer = GLContext.getFunctionAddress("glProgramParameters4fvNV")) != 0 &&
			(NV_vertex_program_glProgramParameter4fNV_pointer = GLContext.getFunctionAddress("glProgramParameter4fNV")) != 0 &&
			(NV_vertex_program_glGetVertexAttribPointervNV_pointer = GLContext.getFunctionAddress("glGetVertexAttribPointervNV")) != 0 &&
			(NV_vertex_program_glGetVertexAttribivNV_pointer = GLContext.getFunctionAddress("glGetVertexAttribivNV")) != 0 &&
			(NV_vertex_program_glGetVertexAttribfvNV_pointer = GLContext.getFunctionAddress("glGetVertexAttribfvNV")) != 0 &&
			(NV_vertex_program_glGetTrackMatrixivNV_pointer = GLContext.getFunctionAddress("glGetTrackMatrixivNV")) != 0 &&
			(NV_vertex_program_glGetProgramParameterfvNV_pointer = GLContext.getFunctionAddress("glGetProgramParameterfvNV")) != 0 &&
			(NV_vertex_program_glExecuteProgramNV_pointer = GLContext.getFunctionAddress("glExecuteProgramNV")) != 0;
	}


	private Set initAllStubs() throws LWJGLException {
		if (!GL11_initNativeFunctionAddresses())
			throw new LWJGLException("GL11 not supported");
		GLContext.setCapabilities(this);
		Set supported_extensions = GLContext.getSupportedExtensions();
		supported_extensions.add("GL_ARB_buffer_object");
		supported_extensions.add("GL_ARB_program");
		supported_extensions.add("GL_NV_program");
		if (supported_extensions.contains("GL_ARB_buffer_object") && !ARB_buffer_object_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_buffer_object");
		if (supported_extensions.contains("GL_ARB_color_buffer_float") && !ARB_color_buffer_float_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_color_buffer_float");
		if (supported_extensions.contains("GL_ARB_draw_buffers") && !ARB_draw_buffers_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_draw_buffers");
		if (supported_extensions.contains("GL_ARB_imaging") && !ARB_imaging_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_imaging");
		if (supported_extensions.contains("GL_ARB_matrix_palette") && !ARB_matrix_palette_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_matrix_palette");
		if (supported_extensions.contains("GL_ARB_multisample") && !ARB_multisample_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_multisample");
		if (supported_extensions.contains("GL_ARB_multitexture") && !ARB_multitexture_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_multitexture");
		if (supported_extensions.contains("GL_ARB_occlusion_query") && !ARB_occlusion_query_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_occlusion_query");
		if (supported_extensions.contains("GL_ARB_point_parameters") && !ARB_point_parameters_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_point_parameters");
		if (supported_extensions.contains("GL_ARB_program") && !ARB_program_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_program");
		if (supported_extensions.contains("GL_ARB_shader_objects") && !ARB_shader_objects_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_shader_objects");
		if (supported_extensions.contains("GL_ARB_texture_compression") && !ARB_texture_compression_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_texture_compression");
		if (supported_extensions.contains("GL_ARB_transpose_matrix") && !ARB_transpose_matrix_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_transpose_matrix");
		if (supported_extensions.contains("GL_ARB_vertex_blend") && !ARB_vertex_blend_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_vertex_blend");
		if (supported_extensions.contains("GL_ARB_vertex_program") && !ARB_vertex_program_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_vertex_program");
		if (supported_extensions.contains("GL_ARB_vertex_shader") && !ARB_vertex_shader_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_vertex_shader");
		if (supported_extensions.contains("GL_ARB_window_pos") && !ARB_window_pos_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ARB_window_pos");
		if (supported_extensions.contains("GL_ATI_draw_buffers") && !ATI_draw_buffers_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_draw_buffers");
		if (supported_extensions.contains("GL_ATI_element_array") && !ATI_element_array_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_element_array");
		if (supported_extensions.contains("GL_ATI_envmap_bumpmap") && !ATI_envmap_bumpmap_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_envmap_bumpmap");
		if (supported_extensions.contains("GL_ATI_fragment_shader") && !ATI_fragment_shader_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_fragment_shader");
		if (supported_extensions.contains("GL_ATI_map_object_buffer") && !ATI_map_object_buffer_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_map_object_buffer");
		if (supported_extensions.contains("GL_ATI_pn_triangles") && !ATI_pn_triangles_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_pn_triangles");
		if (supported_extensions.contains("GL_ATI_separate_stencil") && !ATI_separate_stencil_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_separate_stencil");
		if (supported_extensions.contains("GL_ATI_vertex_array_object") && !ATI_vertex_array_object_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_vertex_array_object");
		if (supported_extensions.contains("GL_ATI_vertex_attrib_array_object") && !ATI_vertex_attrib_array_object_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_vertex_attrib_array_object");
		if (supported_extensions.contains("GL_ATI_vertex_streams") && !ATI_vertex_streams_initNativeFunctionAddresses())
			supported_extensions.remove("GL_ATI_vertex_streams");
		if (supported_extensions.contains("GL_EXT_blend_equation_separate") && !EXT_blend_equation_separate_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_blend_equation_separate");
		if (supported_extensions.contains("GL_EXT_blend_func_separate") && !EXT_blend_func_separate_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_blend_func_separate");
		if (supported_extensions.contains("GL_EXT_compiled_vertex_array") && !EXT_compiled_vertex_array_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_compiled_vertex_array");
		if (supported_extensions.contains("GL_EXT_depth_bounds_test") && !EXT_depth_bounds_test_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_depth_bounds_test");
		if (supported_extensions.contains("GL_EXT_draw_range_elements") && !EXT_draw_range_elements_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_draw_range_elements");
		if (supported_extensions.contains("GL_EXT_fog_coord") && !EXT_fog_coord_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_fog_coord");
		if (supported_extensions.contains("GL_EXT_framebuffer_object") && !EXT_framebuffer_object_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_framebuffer_object");
		if (supported_extensions.contains("GL_EXT_multi_draw_arrays") && !EXT_multi_draw_arrays_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_multi_draw_arrays");
		if (supported_extensions.contains("GL_EXT_paletted_texture") && !EXT_paletted_texture_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_paletted_texture");
		if (supported_extensions.contains("GL_EXT_point_parameters") && !EXT_point_parameters_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_point_parameters");
		if (supported_extensions.contains("GL_EXT_secondary_color") && !EXT_secondary_color_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_secondary_color");
		if (supported_extensions.contains("GL_EXT_stencil_two_side") && !EXT_stencil_two_side_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_stencil_two_side");
		if (supported_extensions.contains("GL_EXT_vertex_shader") && !EXT_vertex_shader_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_vertex_shader");
		if (supported_extensions.contains("GL_EXT_vertex_weighting") && !EXT_vertex_weighting_initNativeFunctionAddresses())
			supported_extensions.remove("GL_EXT_vertex_weighting");
		if (supported_extensions.contains("OpenGL12") && !GL12_initNativeFunctionAddresses())
			supported_extensions.remove("OpenGL12");
		if (supported_extensions.contains("OpenGL13") && !GL13_initNativeFunctionAddresses())
			supported_extensions.remove("OpenGL13");
		if (supported_extensions.contains("OpenGL14") && !GL14_initNativeFunctionAddresses())
			supported_extensions.remove("OpenGL14");
		if (supported_extensions.contains("OpenGL15") && !GL15_initNativeFunctionAddresses())
			supported_extensions.remove("OpenGL15");
		if (supported_extensions.contains("OpenGL20") && !GL20_initNativeFunctionAddresses())
			supported_extensions.remove("OpenGL20");
		if (supported_extensions.contains("GL_NV_evaluators") && !NV_evaluators_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_evaluators");
		if (supported_extensions.contains("GL_NV_fence") && !NV_fence_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_fence");
		if (supported_extensions.contains("GL_NV_fragment_program") && !NV_fragment_program_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_fragment_program");
		if (supported_extensions.contains("GL_NV_half_float") && !NV_half_float_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_half_float");
		if (supported_extensions.contains("GL_NV_occlusion_query") && !NV_occlusion_query_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_occlusion_query");
		if (supported_extensions.contains("GL_NV_pixel_data_range") && !NV_pixel_data_range_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_pixel_data_range");
		if (supported_extensions.contains("GL_NV_point_sprite") && !NV_point_sprite_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_point_sprite");
		if (supported_extensions.contains("GL_NV_primitive_restart") && !NV_primitive_restart_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_primitive_restart");
		if (supported_extensions.contains("GL_NV_program") && !NV_program_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_program");
		if (supported_extensions.contains("GL_NV_register_combiners") && !NV_register_combiners_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_register_combiners");
		if (supported_extensions.contains("GL_NV_register_combiners2") && !NV_register_combiners2_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_register_combiners2");
		if (supported_extensions.contains("GL_NV_vertex_array_range") && !NV_vertex_array_range_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_vertex_array_range");
		if (supported_extensions.contains("GL_NV_vertex_program") && !NV_vertex_program_initNativeFunctionAddresses())
			supported_extensions.remove("GL_NV_vertex_program");
		return supported_extensions;
	}

	static void unloadAllStubs() {
	}

	ContextCapabilities() throws LWJGLException {
		Set supported_extensions = initAllStubs();
		tracker = new StateTracker();
		this.GL_ARB_color_buffer_float = supported_extensions.contains("GL_ARB_color_buffer_float");
		this.GL_ARB_depth_texture = supported_extensions.contains("GL_ARB_depth_texture");
		this.GL_ARB_draw_buffers = supported_extensions.contains("GL_ARB_draw_buffers");
		this.GL_ARB_fragment_program = supported_extensions.contains("GL_ARB_fragment_program")
			&& supported_extensions.contains("GL_ARB_program");
		this.GL_ARB_fragment_program_shadow = supported_extensions.contains("GL_ARB_fragment_program_shadow");
		this.GL_ARB_fragment_shader = supported_extensions.contains("GL_ARB_fragment_shader");
		this.GL_ARB_half_float_pixel = supported_extensions.contains("GL_ARB_half_float_pixel");
		this.GL_ARB_imaging = supported_extensions.contains("GL_ARB_imaging");
		this.GL_ARB_matrix_palette = supported_extensions.contains("GL_ARB_matrix_palette");
		this.GL_ARB_multisample = supported_extensions.contains("GL_ARB_multisample");
		this.GL_ARB_multitexture = supported_extensions.contains("GL_ARB_multitexture");
		this.GL_ARB_occlusion_query = supported_extensions.contains("GL_ARB_occlusion_query");
		this.GL_ARB_pixel_buffer_object = supported_extensions.contains("GL_ARB_pixel_buffer_object")
			&& supported_extensions.contains("GL_ARB_buffer_object");
		this.GL_ARB_point_parameters = supported_extensions.contains("GL_ARB_point_parameters");
		this.GL_ARB_point_sprite = supported_extensions.contains("GL_ARB_point_sprite");
		this.GL_ARB_shader_objects = supported_extensions.contains("GL_ARB_shader_objects");
		this.GL_ARB_shading_language_100 = supported_extensions.contains("GL_ARB_shading_language_100");
		this.GL_ARB_shadow = supported_extensions.contains("GL_ARB_shadow");
		this.GL_ARB_shadow_ambient = supported_extensions.contains("GL_ARB_shadow_ambient");
		this.GL_ARB_texture_border_clamp = supported_extensions.contains("GL_ARB_texture_border_clamp");
		this.GL_ARB_texture_compression = supported_extensions.contains("GL_ARB_texture_compression");
		this.GL_ARB_texture_cube_map = supported_extensions.contains("GL_ARB_texture_cube_map");
		this.GL_ARB_texture_env_add = supported_extensions.contains("GL_ARB_texture_env_add");
		this.GL_ARB_texture_env_combine = supported_extensions.contains("GL_ARB_texture_env_combine");
		this.GL_ARB_texture_env_crossbar = supported_extensions.contains("GL_ARB_texture_env_crossbar");
		this.GL_ARB_texture_env_dot3 = supported_extensions.contains("GL_ARB_texture_env_dot3");
		this.GL_ARB_texture_float = supported_extensions.contains("GL_ARB_texture_float");
		this.GL_ARB_texture_mirrored_repeat = supported_extensions.contains("GL_ARB_texture_mirrored_repeat");
		this.GL_ARB_texture_non_power_of_two = supported_extensions.contains("GL_ARB_texture_non_power_of_two");
		this.GL_ARB_texture_rectangle = supported_extensions.contains("GL_ARB_texture_rectangle");
		this.GL_ARB_transpose_matrix = supported_extensions.contains("GL_ARB_transpose_matrix");
		this.GL_ARB_vertex_blend = supported_extensions.contains("GL_ARB_vertex_blend");
		this.GL_ARB_vertex_buffer_object = supported_extensions.contains("GL_ARB_vertex_buffer_object")
			&& supported_extensions.contains("GL_ARB_buffer_object");
		this.GL_ARB_vertex_program = supported_extensions.contains("GL_ARB_vertex_program")
			&& supported_extensions.contains("GL_ARB_program");
		this.GL_ARB_vertex_shader = supported_extensions.contains("GL_ARB_vertex_shader");
		this.GL_ARB_window_pos = supported_extensions.contains("GL_ARB_window_pos");
		this.GL_ATI_draw_buffers = supported_extensions.contains("GL_ATI_draw_buffers");
		this.GL_ATI_element_array = supported_extensions.contains("GL_ATI_element_array");
		this.GL_ATI_envmap_bumpmap = supported_extensions.contains("GL_ATI_envmap_bumpmap");
		this.GL_ATI_fragment_shader = supported_extensions.contains("GL_ATI_fragment_shader");
		this.GL_ATI_map_object_buffer = supported_extensions.contains("GL_ATI_map_object_buffer");
		this.GL_ATI_pn_triangles = supported_extensions.contains("GL_ATI_pn_triangles");
		this.GL_ATI_separate_stencil = supported_extensions.contains("GL_ATI_separate_stencil");
		this.GL_ATI_texture_compression_3dc = supported_extensions.contains("GL_ATI_texture_compression_3dc");
		this.GL_ATI_texture_float = supported_extensions.contains("GL_ATI_texture_float");
		this.GL_ATI_texture_mirror_once = supported_extensions.contains("GL_ATI_texture_mirror_once");
		this.GL_ATI_vertex_array_object = supported_extensions.contains("GL_ATI_vertex_array_object");
		this.GL_ATI_vertex_attrib_array_object = supported_extensions.contains("GL_ATI_vertex_attrib_array_object");
		this.GL_ATI_vertex_streams = supported_extensions.contains("GL_ATI_vertex_streams");
		this.GL_EXT_abgr = supported_extensions.contains("GL_EXT_abgr");
		this.GL_EXT_bgra = supported_extensions.contains("GL_EXT_bgra");
		this.GL_EXT_blend_equation_separate = supported_extensions.contains("GL_EXT_blend_equation_separate");
		this.GL_EXT_blend_func_separate = supported_extensions.contains("GL_EXT_blend_func_separate");
		this.GL_EXT_blend_subtract = supported_extensions.contains("GL_EXT_blend_subtract");
		this.GL_EXT_cg_shader = supported_extensions.contains("GL_EXT_cg_shader");
		this.GL_EXT_compiled_vertex_array = supported_extensions.contains("GL_EXT_compiled_vertex_array");
		this.GL_EXT_depth_bounds_test = supported_extensions.contains("GL_EXT_depth_bounds_test");
		this.GL_EXT_draw_range_elements = supported_extensions.contains("GL_EXT_draw_range_elements");
		this.GL_EXT_fog_coord = supported_extensions.contains("GL_EXT_fog_coord");
		this.GL_EXT_framebuffer_object = supported_extensions.contains("GL_EXT_framebuffer_object");
		this.GL_EXT_multi_draw_arrays = supported_extensions.contains("GL_EXT_multi_draw_arrays");
		this.GL_EXT_packed_pixels = supported_extensions.contains("GL_EXT_packed_pixels");
		this.GL_EXT_paletted_texture = supported_extensions.contains("GL_EXT_paletted_texture");
		this.GL_EXT_pixel_buffer_object = supported_extensions.contains("GL_EXT_pixel_buffer_object")
			&& supported_extensions.contains("GL_ARB_buffer_object");
		this.GL_EXT_point_parameters = supported_extensions.contains("GL_EXT_point_parameters");
		this.GL_EXT_rescale_normal = supported_extensions.contains("GL_EXT_rescale_normal");
		this.GL_EXT_secondary_color = supported_extensions.contains("GL_EXT_secondary_color");
		this.GL_EXT_separate_specular_color = supported_extensions.contains("GL_EXT_separate_specular_color");
		this.GL_EXT_shadow_funcs = supported_extensions.contains("GL_EXT_shadow_funcs");
		this.GL_EXT_shared_texture_palette = supported_extensions.contains("GL_EXT_shared_texture_palette");
		this.GL_EXT_stencil_two_side = supported_extensions.contains("GL_EXT_stencil_two_side");
		this.GL_EXT_stencil_wrap = supported_extensions.contains("GL_EXT_stencil_wrap");
		this.GL_EXT_texture_3d = supported_extensions.contains("GL_EXT_texture_3d");
		this.GL_EXT_texture_compression_s3tc = supported_extensions.contains("GL_EXT_texture_compression_s3tc");
		this.GL_EXT_texture_env_combine = supported_extensions.contains("GL_EXT_texture_env_combine");
		this.GL_EXT_texture_env_dot3 = supported_extensions.contains("GL_EXT_texture_env_dot3");
		this.GL_EXT_texture_filter_anisotropic = supported_extensions.contains("GL_EXT_texture_filter_anisotropic");
		this.GL_EXT_texture_lod_bias = supported_extensions.contains("GL_EXT_texture_lod_bias");
		this.GL_EXT_texture_mirror_clamp = supported_extensions.contains("GL_EXT_texture_mirror_clamp");
		this.GL_EXT_texture_rectangle = supported_extensions.contains("GL_EXT_texture_rectangle");
		this.GL_EXT_vertex_shader = supported_extensions.contains("GL_EXT_vertex_shader");
		this.GL_EXT_vertex_weighting = supported_extensions.contains("GL_EXT_vertex_weighting");
		this.OpenGL11 = supported_extensions.contains("OpenGL11");
		this.OpenGL12 = supported_extensions.contains("OpenGL12");
		this.OpenGL13 = supported_extensions.contains("OpenGL13");
		this.OpenGL14 = supported_extensions.contains("OpenGL14");
		this.OpenGL15 = supported_extensions.contains("OpenGL15");
		this.OpenGL20 = supported_extensions.contains("OpenGL20");
		this.GL_HP_occlusion_test = supported_extensions.contains("GL_HP_occlusion_test");
		this.GL_IBM_rasterpos_clip = supported_extensions.contains("GL_IBM_rasterpos_clip");
		this.GL_NV_blend_square = supported_extensions.contains("GL_NV_blend_square");
		this.GL_NV_copy_depth_to_color = supported_extensions.contains("GL_NV_copy_depth_to_color");
		this.GL_NV_depth_clamp = supported_extensions.contains("GL_NV_depth_clamp");
		this.GL_NV_evaluators = supported_extensions.contains("GL_NV_evaluators");
		this.GL_NV_fence = supported_extensions.contains("GL_NV_fence");
		this.GL_NV_float_buffer = supported_extensions.contains("GL_NV_float_buffer");
		this.GL_NV_fog_distance = supported_extensions.contains("GL_NV_fog_distance");
		this.GL_NV_fragment_program = supported_extensions.contains("GL_NV_fragment_program")
			&& supported_extensions.contains("GL_NV_program");
		this.GL_NV_fragment_program2 = supported_extensions.contains("GL_NV_fragment_program2");
		this.GL_NV_fragment_program_option = supported_extensions.contains("GL_NV_fragment_program_option");
		this.GL_NV_half_float = supported_extensions.contains("GL_NV_half_float");
		this.GL_NV_light_max_exponent = supported_extensions.contains("GL_NV_light_max_exponent");
		this.GL_NV_multisample_filter_hint = supported_extensions.contains("GL_NV_multisample_filter_hint");
		this.GL_NV_occlusion_query = supported_extensions.contains("GL_NV_occlusion_query");
		this.GL_NV_packed_depth_stencil = supported_extensions.contains("GL_NV_packed_depth_stencil");
		this.GL_NV_pixel_data_range = supported_extensions.contains("GL_NV_pixel_data_range");
		this.GL_NV_point_sprite = supported_extensions.contains("GL_NV_point_sprite");
		this.GL_NV_primitive_restart = supported_extensions.contains("GL_NV_primitive_restart");
		this.GL_NV_register_combiners = supported_extensions.contains("GL_NV_register_combiners");
		this.GL_NV_register_combiners2 = supported_extensions.contains("GL_NV_register_combiners2");
		this.GL_NV_texgen_reflection = supported_extensions.contains("GL_NV_texgen_reflection");
		this.GL_NV_texture_compression_vtc = supported_extensions.contains("GL_NV_texture_compression_vtc");
		this.GL_NV_texture_env_combine4 = supported_extensions.contains("GL_NV_texture_env_combine4");
		this.GL_NV_texture_expand_normal = supported_extensions.contains("GL_NV_texture_expand_normal");
		this.GL_NV_texture_rectangle = supported_extensions.contains("GL_NV_texture_rectangle");
		this.GL_NV_texture_shader = supported_extensions.contains("GL_NV_texture_shader");
		this.GL_NV_texture_shader2 = supported_extensions.contains("GL_NV_texture_shader2");
		this.GL_NV_texture_shader3 = supported_extensions.contains("GL_NV_texture_shader3");
		this.GL_NV_vertex_array_range = supported_extensions.contains("GL_NV_vertex_array_range");
		this.GL_NV_vertex_array_range2 = supported_extensions.contains("GL_NV_vertex_array_range2");
		this.GL_NV_vertex_program = supported_extensions.contains("GL_NV_vertex_program")
			&& supported_extensions.contains("GL_NV_program");
		this.GL_NV_vertex_program1_1 = supported_extensions.contains("GL_NV_vertex_program1_1");
		this.GL_NV_vertex_program2 = supported_extensions.contains("GL_NV_vertex_program2");
		this.GL_NV_vertex_program2_option = supported_extensions.contains("GL_NV_vertex_program2_option");
		this.GL_NV_vertex_program3 = supported_extensions.contains("GL_NV_vertex_program3");
		this.GL_SUN_slice_accum = supported_extensions.contains("GL_SUN_slice_accum");
	}
}
