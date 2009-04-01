package com.g3d.renderer.lwjgl;

import com.g3d.light.DirectionalLight;
import com.g3d.light.Light;
import com.g3d.light.LightList;
import com.g3d.light.PointLight;
import com.g3d.math.ColorRGBA;
import com.g3d.math.Matrix4f;
import com.g3d.math.Transform;
import com.g3d.math.Vector2f;
import com.g3d.math.Vector3f;
import com.g3d.renderer.Camera;
import com.g3d.renderer.GLObjectManager;
import com.g3d.renderer.Renderer;
import com.g3d.scene.Mesh;
import com.g3d.scene.VertexBuffer;
import com.g3d.scene.VertexBuffer.Format;
import com.g3d.scene.VertexBuffer.Type;
import com.g3d.scene.VertexBuffer.Usage;
import com.g3d.renderer.RenderContext;
import com.g3d.scene.Geometry;
import com.g3d.shader.Shader;
import com.g3d.shader.Shader.ShaderSource;
import com.g3d.shader.Shader.ShaderType;
import com.g3d.shader.Uniform;
import com.g3d.texture.Image;
import com.g3d.texture.Texture;
import com.g3d.texture.Texture.WrapAxis;
import com.g3d.util.BufferUtils;
import com.g3d.util.TempVars;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.logging.Logger;

import org.lwjgl.opengl.ARBGeometryShader4;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.ARBDrawInstanced.*;

public class LwjglRenderer implements Renderer {

    private static final Logger logger = Logger.getLogger(LwjglRenderer.class.getName());

    private ByteBuffer nameBuf = BufferUtils.createByteBuffer(250);
    private StringBuilder stringBuf = new StringBuilder(250);

    private RenderContext context = new RenderContext();
    
    private final Matrix4f worldViewProjectionMatrix = new Matrix4f();
    private final Matrix4f worldMatrix = new Matrix4f();
//    private final FloatBuffer floatBuf16 = BufferUtils.createFloatBuffer(16);

    private Camera camera;
    private Shader boundShader;

    protected void updateNameBuffer(){
        int len = stringBuf.length();

        nameBuf.position(0);
        nameBuf.limit(len);
        for (int i = 0; i < len; i++)
            nameBuf.put((byte)stringBuf.charAt(i));

        nameBuf.rewind();
    }

    /*********************************************************************\
    |* Render State                                                      *|
    \*********************************************************************/

    @Override
    public void clearBuffers(boolean color, boolean depth, boolean stencil){
        int bits = 0;
        if (color) bits = GL_COLOR_BUFFER_BIT;
        if (depth) bits |= GL_DEPTH_BUFFER_BIT;
        if (stencil) bits |= GL_STENCIL_BUFFER_BIT;
        if (bits != 0) glClear(bits);
    }

    public void setDepthTest(boolean enabled) {
        if (enabled && !context.depthTestEnabled){
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);
//            glColorMask(false, false, false, false);
//            glDepthMask(false);
//            glStencilMask(0x00);
            context.depthTestEnabled = true;
        }else if (!enabled && context.depthTestEnabled){
            glDisable(GL_DEPTH_TEST);
            context.depthTestEnabled = false;
        }
    }

    public void setBackfaceCulling(boolean enabled) {
        if (enabled && !context.cullingEnabled){
            glCullFace(GL_BACK);
            context.cullingEnabled = true;
        }else if (!enabled && context.cullingEnabled){
            glCullFace(GL_NONE);
            context.cullingEnabled = false;
        }
    }

    /*********************************************************************\
    |* Camera and World transforms                                       *|
    \*********************************************************************/

    public void setCamera(Camera cam) {
        this.camera = cam;
    }

    public Camera getCamera(){
        return camera;
    }

    public void onFrame(){
        if (camera.isViewportChanged()){
            int x = (int) (camera.getViewPortLeft() * camera.getWidth());
            int y = (int) (camera.getViewPortBottom() * camera.getHeight());
            int w = (int) ((camera.getViewPortRight() - camera.getViewPortLeft()) * camera.getWidth());
            int h = (int) ((camera.getViewPortTop() - camera.getViewPortBottom()) * camera.getHeight());
            glViewport(x, y, w, h);

            camera.clearViewportChanged();
        }
    }

    @Override
    public void setTransform(Transform transform) {
        worldMatrix.loadIdentity();
        
        worldMatrix.setRotationQuaternion(transform.getRotation());
        worldMatrix.setTranslation(transform.getTranslation());

        Matrix4f scaleMat = TempVars.get().tempMat4;
        scaleMat.loadIdentity();
        scaleMat.scale(transform.getScale());
        worldMatrix.multLocal(scaleMat);
    }

    /*********************************************************************\
    |* Shaders                                                           *|
    \*********************************************************************/

    protected void updateUniform(Shader shader, Uniform uniform){
        if (uniform.getName() == null)
            throw new IllegalArgumentException("Uniform must have a name!");

        int shaderId = shader.getId();
        if (shaderId == -1)
            throw new IllegalArgumentException("Shader has not been uploaded yet.");

        if (context.boundShaderProgram != shaderId){
            glUseProgram(shaderId);
            context.boundShaderProgram = shaderId;
        }

        int loc = uniform.getLocation();
        if (loc == -1){
            // get uniform location
            stringBuf.setLength(0);
            stringBuf.append(uniform.getName()).append('\0');
            updateNameBuffer();
            loc = glGetUniformLocation(shader.getId(), nameBuf);
            if (loc < 0){
                // uniform is not declared in shader
                //logger.warning("Uniform "+uniform.getName()+" is not declared in shader.");
                return;
            }
            uniform.setLocation(loc);
        }

        FloatBuffer fb;
        switch (uniform.getDataType()){
            case Float:
                Float f = (Float)uniform.getValue();
                glUniform1f(loc, f.floatValue());
                break;
            case Float2:
                Vector2f v2 = (Vector2f)uniform.getValue();
                glUniform2f(loc, v2.getX(), v2.getY());
                break;
            case Float3:
                Vector3f v3 = (Vector3f)uniform.getValue();
                glUniform3f(loc, v3.getX(), v3.getY(), v3.getZ());
                break;
            case Float4:
                ColorRGBA c = (ColorRGBA)uniform.getValue();
                glUniform4f(loc, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
                break;
            case Matrix2:
                fb = (FloatBuffer)uniform.getValue();
                assert fb.remaining() == 4;
                glUniformMatrix2(loc, false, fb);
                break;
            case Matrix3:
                fb = (FloatBuffer)uniform.getValue();
                assert fb.remaining() == 9;
                glUniformMatrix3(loc, true, fb);
                break;
            case Matrix4:
                fb = (FloatBuffer)uniform.getValue();
                assert fb.remaining() == 16;
                glUniformMatrix4(loc, false, fb);
                break;
            case FloatArray:
                fb = (FloatBuffer)uniform.getValue();
                glUniform1(loc, fb);
                break;
            case Float2Array:
                fb = (FloatBuffer)uniform.getValue();
                glUniform2(loc, fb);
                break;
            case Float3Array:
                fb = (FloatBuffer)uniform.getValue();
                glUniform3(loc, fb);
                break;
            case Float4Array:
                fb = (FloatBuffer)uniform.getValue();
                glUniform4(loc, fb);
                break;
            case Int:
                Integer i = (Integer)uniform.getValue();
                glUniform1i(loc, i.intValue());
                break;
        }
    }

    protected void updateShaderUniforms(Shader shader){
        for (Uniform uniform : shader.getUniforms()){
            updateUniform(shader, uniform);
        }
    }

    public void updateLightListUniforms(Shader shader, Geometry g){
        LightList lightList = g.getWorldLightList();
        Uniform lightColor = shader.getUniform("g_LightColor");
        Uniform lightPos = shader.getUniform("g_LightPosition");
        lightColor.setVector4Length(4);
        lightPos.setVector4Length(4);
        for (int i = 0; i < 4; i++){
            if (lightList.size() <= i){
                lightColor.setVector4InArray(0f, 0f, 0f, 0f, i);
                lightPos.setVector4InArray(0f, 0f, 0f, 0f, i);
            }else{
                Light l = lightList.get(i);
                ColorRGBA color = l.getColor();
                lightColor.setVector4InArray(color.getRed(),
                                             color.getGreen(),
                                             color.getBlue(),
                                             l.getType().getId(),
                                             i);

                switch (l.getType()){
                    case Directional:
                        DirectionalLight dl = (DirectionalLight) l;
                        Vector3f dir = dl.getDirection();
                        lightPos.setVector4InArray(dir.getX(), dir.getY(), dir.getZ(), -1, i);
                        break;
                    case Point:
                        PointLight pl = (PointLight) l;
                        Vector3f pos = pl.getPosition();
                        float invRadius = pl.getRadius();
                        if (invRadius != 0){
                            invRadius = 1f / invRadius;
                        }
                        lightPos.setVector4InArray(pos.getX(), pos.getY(), pos.getZ(), invRadius, i);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown type of light: "+l.getType());
                }
            }
        }
    }

    public void updatePredefinedUniforms(Shader shader){
        // assums worldMatrix is properly set.
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projMatrix = camera.getProjectionMatrix();
        Matrix4f viewProjMatrix = camera.getViewProjectionMatrix();

        worldViewProjectionMatrix.loadIdentity();
        worldViewProjectionMatrix.set(viewProjMatrix);
        worldViewProjectionMatrix.multLocal(worldMatrix);

        shader.getUniform("g_WorldMatrix").setMatrix4(worldMatrix);
        shader.getUniform("g_ViewMatrix").setMatrix4(viewMatrix);
        shader.getUniform("g_ProjectionMatrix").setMatrix4(projMatrix);
        shader.getUniform("g_ViewProjectionMatrix").setMatrix4(viewProjMatrix);
        shader.getUniform("g_WorldViewProjectionMatrix").setMatrix4(worldViewProjectionMatrix);

        worldViewProjectionMatrix.loadIdentity();
        worldViewProjectionMatrix.set(viewMatrix);
        worldViewProjectionMatrix.multLocal(worldMatrix);
        shader.getUniform("g_WorldViewMatrix").setMatrix4(worldViewProjectionMatrix);
    }

    public int convertShaderType(ShaderType type){
        switch (type){
            case Fragment:
                return GL_FRAGMENT_SHADER;
            case Vertex:
                return GL_VERTEX_SHADER;
            case Geometry:
                return ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB;
            default:
                throw new RuntimeException("Unknown shader type.");
        }
    }

    public void updateShaderSourceData(ShaderSource source){
        int id = source.getId();
        if (id == -1){
            // create id
            id = glCreateShader(convertShaderType(source.getType()));
            assert id >= 0;
            source.setId(id);
        }

        // upload shader source
        // convert string to bytebuffer
        ByteBuffer sourceBuf = BufferUtils.createByteBuffer(source.getSource());
        glShaderSource(id, sourceBuf);
        glCompileShader(id);

        IntBuffer temp = TempVars.get().intBuffer1;
        glGetShader(id, GL_COMPILE_STATUS, temp);
        boolean compiledOK = temp.get(0) == GL_TRUE;

        // check for errors, etc
        glGetShader(id, GL_INFO_LOG_LENGTH, temp);
        int length = temp.get(0);
        if (length > 0){
            // get infos
            ByteBuffer logBuf = BufferUtils.createByteBuffer(length);
            glGetShaderInfoLog(id, null, logBuf);

            // convert to string, etc
            byte[] logBytes = new byte[length];
            logBuf.get(logBytes, 0, length);
            String infoLog = new String(logBytes);
            if (compiledOK){
                // we dont care much about the info log if all still compiled..
                // send as FINE
                logger.fine(source.getType().name()+" compile success: "+infoLog);
            }else{
                // send as WARNING
                logger.warning(source.getType().name()+" compile error: "+infoLog);
            }
        }else if (!compiledOK){
            logger.warning(source.getType().name()+" compile error: ?");
        }else{
            logger.fine(source.getType().name()+" compile success");
        }

        // only usable if compiled
        source.setUsable(compiledOK);
        if (!compiledOK){
            // make sure to dispose id cause all program's
            // shaders will be cleared later.
            glDeleteShader(id);
        }
    }

    @Override
    public void updateShaderData(Shader shader){
        int id = shader.getId();
        boolean needRegister = false;
        if (id == -1){
            // create program
            id = glCreateProgram();
            assert id >= 0;
            shader.setId(id);
            needRegister = true;
        }

        for (ShaderSource source : shader.getSources()){
            if (source.getId() == -1){
                updateShaderSourceData(source);
                // shader has been compiled here
            }

            if (!source.isUsable()){
                // it's useless.. just forget about everything..
                shader.setUsable(false);
                shader.clearUpdateNeeded();
                return;
            }
            glAttachShader(id, source.getId());
        }
        // link shaders to program
        glLinkProgram(id);

        IntBuffer temp = TempVars.get().intBuffer1;

        glGetProgram(id, GL_LINK_STATUS, temp);
        boolean linkOK = temp.get(0) == GL_TRUE;

        glGetProgram(id, GL_VALIDATE_STATUS, temp);
        boolean validateOK = temp.get(0) == GL_TRUE;

        glGetProgram(id, GL_INFO_LOG_LENGTH, temp);
        int length = temp.get(0);

        if (length > 0){
            // get infos
            ByteBuffer logBuf = BufferUtils.createByteBuffer(length);
            glGetProgramInfoLog(id, null, logBuf);

            // convert to string, etc
            byte[] logBytes = new byte[length];
            logBuf.get(logBytes, 0, length);
            String infoLog = new String(logBytes);

            if (linkOK && validateOK){
                // we dont care much about the info log if all still compiled..
                // send as FINE
                logger.fine("shader link and compile success: "+infoLog);
            }else{
                // send as WARNING
                logger.warning("shader link and compile failure: "+infoLog);
            }
        }else if (validateOK && linkOK){
            logger.fine("shader link and validate success");
        }else if (!validateOK && !linkOK){
            logger.fine("shader link and validate failure");
        }else if (!validateOK){
            logger.fine("shader link success but validate failure");
        }else if (!linkOK){
            logger.fine("shader link failure but validate success");
        }

        if (!linkOK || !validateOK){
            // failure.. forget about everything
            shader.resetSources();
            shader.clearUpdateNeeded();
            shader.setUsable(false);
            deleteShader(shader);
        }else{
            shader.setUsable(true);
            shader.clearUpdateNeeded();
            if (needRegister)
                GLObjectManager.registerForCleanup(this, shader);
        }
    }

    @Override
    public void setShader(Shader shader){
        if (shader == null){
            if (context.boundShaderProgram > 0){
                glUseProgram(0);
                context.boundShaderProgram = 0;
                boundShader = null;
            }
        } else {
            if (shader.isUpdateNeeded())
                updateShaderData(shader);
            
            if (!shader.isUsable())
                return;

            updatePredefinedUniforms(shader);
            updateShaderUniforms(shader);
            if (context.boundShaderProgram != shader.getId()){
                glUseProgram(shader.getId());
                context.boundShaderProgram = shader.getId();
                boundShader = shader;
            }
        }
    }

    public void deleteShader(Shader shader){
        if (shader.getId() == -1){
            logger.warning("Shader is not uploaded to GPU, cannot delete.");
            return;
        }
        for (ShaderSource source : shader.getSources()){
            if (source.getId() != -1){
                glDetachShader(shader.getId(), source.getId());
                glDeleteShader(source.getId());
            }
        }
        glDeleteProgram(shader.getId());
    }

    /*********************************************************************\
    |* Textures                                                          *|
    \*********************************************************************/

    private int convertTextureType(Texture.Type type){
        switch (type){
            case OneDimensional:
                return GL_TEXTURE_1D;
            case TwoDimensional:
                return GL_TEXTURE_2D;
            case ThreeDimensional:
                return GL_TEXTURE_3D;
            case CubeMap:
                return GL_TEXTURE_CUBE_MAP;
            default:
                throw new UnsupportedOperationException("Unknown texture type: "+type);
        }
    }

    private int convertMagFilter(Texture.MagFilter filter){
        switch (filter){
            case Bilinear:
                return GL_LINEAR;
            case Nearest:
                return GL_NEAREST;
            default:
                throw new UnsupportedOperationException("Unknown mag filter: "+filter);
        }
    }

    private int convertMinFilter(Texture.MinFilter filter){
        switch (filter){
            case Trilinear:
                return GL_LINEAR_MIPMAP_LINEAR;
            case BilinearNearestMipMap:
                return GL_LINEAR_MIPMAP_NEAREST;
            case NearestLinearMipMap:
                return GL_NEAREST_MIPMAP_LINEAR;
            case NearestNearestMipMap:
                return GL_NEAREST_MIPMAP_NEAREST;
            case BilinearNoMipMaps:
                return GL_LINEAR;
            case NearestNoMipMaps:
                return GL_NEAREST;
            default:
                throw new UnsupportedOperationException("Unknown min filter: "+filter);
        }
    }

    private int convertWrapMode(Texture.WrapMode mode){
        switch (mode){
            case BorderClamp:
                return GL_CLAMP_TO_BORDER;
            case Clamp:
                return GL_CLAMP;
            case EdgeClamp:
                return GL_CLAMP_TO_EDGE;
            case Repeat:
                return GL_REPEAT;
            case MirroredRepeat:
                return GL_MIRRORED_REPEAT;
            default:
                throw new UnsupportedOperationException("Unknown wrap mode: "+mode);
        }
    }

    public void updateTextureData(Texture tex){
        int texId = tex.getId();
        if (texId == -1){
            // create texture
            glGenTextures(TempVars.get().intBuffer1);
            texId = TempVars.get().intBuffer1.get(0);
            tex.setId(texId);
            GLObjectManager.registerForCleanup(this, tex);
        }

        // bind texture
        int target = convertTextureType(tex.getType());
        if (context.boundTexture != texId){
            glBindTexture(target, texId);
            context.boundTexture = texId;
        }

        // filter things
        int minFilter = convertMinFilter(tex.getMinFilter());
        int magFilter = convertMagFilter(tex.getMagFilter());
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);

        if (tex.getAnisotropicFilter() > 1){
            glTexParameterf(GL_TEXTURE_2D,
                            EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,
                            tex.getAnisotropicFilter());
        }
        // repeat modes
        switch (tex.getType()){
            case ThreeDimensional:
                glTexParameteri(target, GL_TEXTURE_WRAP_R, convertWrapMode(tex.getWrap(WrapAxis.R)));
            case TwoDimensional:
                glTexParameteri(target, GL_TEXTURE_WRAP_T, convertWrapMode(tex.getWrap(WrapAxis.T)));
                // fall down here is intentional..
            case OneDimensional:
                glTexParameteri(target, GL_TEXTURE_WRAP_S, convertWrapMode(tex.getWrap(WrapAxis.S)));
                break;
            default:
                throw new UnsupportedOperationException("Unknown texture type: "+tex.getType());
        }

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        Image img = tex.getImage();
        if (!img.hasMipmaps() && tex.getMinFilter().usesMipMapLevels()){
            // No pregenerated mips available,
            // generate from base level if required
            glTexParameteri(target, GL_GENERATE_MIPMAP, GL_TRUE);
        }
        TextureUtil.uploadTexture(img, target, 0);

        tex.clearUpdateNeeded();
    }

    public void setTexture(int unit, Texture tex){
         if (tex.isUpdateNeeded())
            updateTextureData(tex);

         int texId = tex.getId();
         assert texId != -1;

         if (context.boundTextures[unit] != tex){
             glActiveTexture(GL_TEXTURE0 + unit);
             int type = convertTextureType(tex.getType());
             glEnable(type);
             glBindTexture(type, texId);
             context.boundTextures[unit] = tex;
         }
    }

    public void clearTextureUnits(){
        Texture[] boundTextures = context.boundTextures;
        for (int i = 0; i < boundTextures.length; i++){
            if (boundTextures[i] != null){
                glActiveTexture(GL_TEXTURE0 + i);
                int type = convertTextureType(boundTextures[i].getType());
                glDisable(type);
                glBindTexture(type, 0);
                boundTextures[i] = null;
            }
        }
    }

    public void deleteTexture(Texture tex){
        int texId = tex.getId();
        if (texId != -1){
            IntBuffer temp = TempVars.get().intBuffer1;
            temp.put(0, texId);
            temp.position(0);
            temp.limit(1);
            glDeleteTextures(temp);
            tex.resetObject();
        }
    }

    /*********************************************************************\
    |* Vertex Buffers and Attributes                                     *|
    \*********************************************************************/

    private int convertUsage(Usage usage){
        switch (usage){
            case Static:
                return GL_STATIC_DRAW;
            case Dynamic:
            case DynamicWriteOnly:
                return GL_DYNAMIC_DRAW;
            case Stream:
            case StreamWriteOnly:
                return GL_STREAM_DRAW;
            default:
                throw new RuntimeException("Unknown usage type.");
        }
    }

    private int convertFormat(Format format){
        switch (format){
            case Byte:
                return GL_BYTE;
            case UnsignedByte:
                return GL_UNSIGNED_BYTE;
            case Short:
                return GL_SHORT;
            case UnsignedShort:
                return GL_UNSIGNED_SHORT;
            case Int:
                return GL_INT;
            case UnsignedInt:
                return GL_UNSIGNED_INT;
            case Float:
                return GL_FLOAT;
            case Double:
                return GL_DOUBLE;
            default:
                throw new RuntimeException("Unknown buffer format.");

        }
    }

    @Override
    public void updateBufferData(VertexBuffer vb){
        int bufId = vb.getId();
        if (bufId == -1){
            // create buffer
            glGenBuffers(TempVars.get().intBuffer1);
            bufId = TempVars.get().intBuffer1.get(0);
            vb.setId(bufId);
            GLObjectManager.registerForCleanup(this, vb);
        }

        // bind buffer
        int target;
        if (vb.getBufferType() == VertexBuffer.Type.Index){
            target = GL_ELEMENT_ARRAY_BUFFER;
            if (context.boundElementArrayVBO != bufId){
                glBindBuffer(target, bufId);
                context.boundElementArrayVBO = bufId;
            }
        }else{
            target = GL_ARRAY_BUFFER;
            if (context.boundArrayVBO != bufId){
                glBindBuffer(target, bufId);
                context.boundArrayVBO = bufId;
            }
        }

        int usage = convertUsage(vb.getUsage());
        vb.getData().rewind();
        // upload data based on format
        switch (vb.getFormat()){
            case Byte:
            case UnsignedByte:
                glBufferData(target, (ByteBuffer) vb.getData(), usage);
                break;
            case Short:
            case UnsignedShort:
                glBufferData(target, (ShortBuffer) vb.getData(), usage);
                break;
            case Int:
            case UnsignedInt:
                glBufferData(target, (IntBuffer) vb.getData(), usage);
                break;
            case Float:
                glBufferData(target, (FloatBuffer) vb.getData(), usage);
                break;
            case Double:
                glBufferData(target, (DoubleBuffer) vb.getData(), usage);
                break;
            default:
                throw new RuntimeException("Unknown buffer format.");
        }

        vb.clearUpdateNeeded();
    }

    public void deleteBuffer(VertexBuffer vb) {
        int bufId = vb.getId();
        if (bufId != -1){
            // NOTE: Must unbind the buffer from the state before deleting it!
            if (bufId == context.boundArrayVBO){
                glBindBuffer(GL_ARRAY_BUFFER, 0);
                context.boundArrayVBO = 0;
            }
            if (bufId == context.boundElementArrayVBO){
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                context.boundElementArrayVBO = 0;
            }
            // delete buffer
            IntBuffer temp = TempVars.get().intBuffer1;
            temp.put(0, bufId);
            temp.position(0);
            temp.limit(1);
            glDeleteBuffers(temp);
            vb.resetObject();

            //System.out.println(vb + ": Deleted");
        }
    }

    @Override
    public void clearVertexAttribs(){
        VertexBuffer[] boundAttribs = context.boundAttribs;
        for (int i = 0; i < boundAttribs.length; i++){
            if (boundAttribs[i] != null){
                glDisableVertexAttribArray(i);
                boundAttribs[i] = null;
            }
        }
    }

    @Override
    public void setVertexAttrib(VertexBuffer vb){
        if (vb.getBufferType() == VertexBuffer.Type.Index)
            throw new IllegalArgumentException("Index buffers not allowed to be set to vertex attrib");

        if (vb.isUpdateNeeded())
            updateBufferData(vb);

        int bufId = vb.getId();
        assert bufId != -1;

        if (context.boundArrayVBO != bufId){
            glBindBuffer(GL_ARRAY_BUFFER, bufId);
            context.boundArrayVBO = bufId;
        }

        int programId = context.boundShaderProgram;
        if (programId > 0){
            stringBuf.setLength(0);
            stringBuf.append("in").append(vb.getBufferType().name()).append('\0');
            updateNameBuffer();
            //ByteBuffer attribName = BufferUtils.createByteBuffer("in" + vb.getBufferType().name() + "\0");
            int attribLoc = glGetAttribLocation(programId, nameBuf);
            VertexBuffer[] attribs = context.boundAttribs;
            if (attribLoc >= 0){
                if (attribs[attribLoc] == null){
                    glEnableVertexAttribArray(attribLoc);
                }
                if (attribs[attribLoc] != vb){
                    glVertexAttribPointer(attribLoc, vb.getNumComponents(), convertFormat(vb.getFormat()), false, 0, 0);
                    attribs[attribLoc] = vb;
                }
            }
        }else{
            throw new IllegalStateException("Cannot render mesh without shader bound");
        }
//        int index;
//        if (vb.getType() == VertexBuffer.Type.MiscAttrib){
//            // must alloc a new index from the render context.
//            index = context.allocAttribIndex();
//        }else{
//            index = vb.getType().getIndex();
//        }

        //glVertexAttribPointer(index, vb.getNumComponents(), convertFormat(vb.getFormat()), false, 0, 0);

//        glEnable(GL_VERTEX_ARRAY);
//        if (vb.getType() == Type.Position){
//            glVertexPointer(vb.getNumComponents(), convertFormat(vb.getFormat()), 0, 0);
//        }

        //context.boundAttribs[index] = vb;
    }

    @Override
    public void drawTriangleList(VertexBuffer indexBuf, int count){
        if (indexBuf.getBufferType() != VertexBuffer.Type.Index)
            throw new IllegalArgumentException("Only index buffers are allowed as triangle lists.");

        if (indexBuf.isUpdateNeeded())
            updateBufferData(indexBuf);

        int bufId = indexBuf.getId();
        assert bufId != -1;

        if (context.boundElementArrayVBO != bufId){
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufId);
            context.boundElementArrayVBO = bufId;
        }
        if (count > 1){
            glDrawElementsInstancedARB(GL_TRIANGLES,
                                       indexBuf.getData().capacity(),
                                       convertFormat(indexBuf.getFormat()),
                                       0,
                                       count);
        }else{
            glDrawElements(GL_TRIANGLES,
                           indexBuf.getData().capacity(),
                           convertFormat(indexBuf.getFormat()),
                           0);
        }
        Util.checkGLError();
    }

    public void drawTriangleStrip(int length){
        glDrawArrays(GL_TRIANGLE_STRIP, 0, length);
        Util.checkGLError();
    }

    /*********************************************************************\
    |* Render Calls                                                      *|
    \*********************************************************************/

    @Override
    public void renderMesh(Mesh mesh, int count) {
        VertexBuffer indices = null;
        for (VertexBuffer vb : mesh.getBuffers()){
            if (vb.getBufferType() == Type.Index){
                indices = vb;
            }else{
                setVertexAttrib(vb);
            }
        }
        if (indices == null){
            logger.warning("Index buffer not specified for mesh.");
            return;
        }

        drawTriangleList(indices, count);
        clearVertexAttribs();
    }

    public void renderGeometry(Geometry geom){
        setTransform(geom.getWorldTransform());
        geom.getMaterial().apply(geom, this);
        renderMesh(geom.getMesh(), 1);
    }

//    @Override
//    public void renderMesh(Mesh mesh) {
//        VertexBuffer indices = null;
//        for (VertexBuffer vb : mesh.getBuffers()){
//            if (vb.isUpdateNeeded()){
//                // update buffer
//                updateBufferData(vb);
//                assert vb.getId() >= 0;
//            }
//
//            if (vb.getType() == Type.Index){
//                indices = vb;
//            }else{
//                if (context.boundArrayVBO != vb.getId()){
//                    glBindBuffer(GL_ARRAY_BUFFER, vb.getId());
//                    context.boundArrayVBO = vb.getId();
//                }
//                glEnableClientState(GL_VERTEX_ARRAY);
//                // no stride, buffer offset = 0
//                glVertexPointer(vb.getNumComponents(), convertFormat(vb.getFormat()), 0, 0);
//            }
//        }
//        if (indices == null){
//            logger.warning("Index buffer not specified for mesh.");
//            return;
//        }
//
//        if (context.boundElementArrayVBO != indices.getId()){
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indices.getId());
//            context.boundElementArrayVBO = indices.getId();
//        }
//
//        glDrawElements(GL_TRIANGLES, indices.getData().capacity(), convertFormat(indices.getFormat()), 0);
//        glDisableClientState(GL_VERTEX_ARRAY);
//
//        // check if any GL errors occured.
//        Util.checkGLError();
//    }
}
