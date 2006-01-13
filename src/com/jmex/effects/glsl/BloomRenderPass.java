package com.jmex.effects.glsl;

import com.jme.image.Texture;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.TextureRenderer;
import com.jme.renderer.pass.Pass;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.*;
import com.jme.system.DisplaySystem;
import com.jme.util.LoggingSystem;

/**
 * GLSL bloom effect pass.
 * - Render supplied source to a texture
 * - Extract intensity
 * - Blur intensity
 * - Blend with first pass
 *
 * @author Rikard Herlitz (MrCoder)
 */
public class BloomRenderPass extends Pass {
	private TextureRenderer tRendererFirst;
	private TextureRenderer tRendererSecond;
	private Texture textureFirst;
	private Texture textureSecond;

	private Quad fullScreenQuad;

	private GLSLShaderObjectsState extractionShader;
	private GLSLShaderObjectsState blurShader;
	private GLSLShaderObjectsState finalShader;

	private int nrBlurPasses;
	private float blurSize;
	private float blurIntensityMultiplier;
	private float exposurePow;
	private float exposureCutoff;
	private boolean supported = true;

	/**
	 * Reset bloom parameters to default
	 */
	public void resetParameters() {
		nrBlurPasses = 2;
		blurSize = 0.02f;
		blurIntensityMultiplier = 1.3f;
		exposurePow = 3.0f;
		exposureCutoff = 0.0f;
	}

	/**
	 * Release pbuffers in TextureRenderer's. Preferably called from user cleanup method.
	 */
	public void cleanup() {
		tRendererFirst.cleanup();
		tRendererSecond.cleanup();
	}

	public boolean isSupported() {
		return supported;
	}
	
	/**
	 * Creates a new bloom renderpass
	 *
	 * @param cam		 Camera used for rendering the bloomsource
	 * @param renderScale Scale of bloom texture
	 */
	public BloomRenderPass(Camera cam, int renderScale) {
		DisplaySystem display = DisplaySystem.getDisplaySystem();

		resetParameters();

		//Create texture renderers and rendertextures(alternating between two not to overwrite pbuffers)
		tRendererFirst = display.createTextureRenderer(
				display.getWidth() / renderScale, display.getHeight() / renderScale, false, true, false, false,
				TextureRenderer.RENDER_TEXTURE_2D, 0);
		tRendererFirst.setBackgroundColor(new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f));
		tRendererFirst.setCamera(cam);

		textureFirst = new Texture();
		textureFirst.setWrap(Texture.WM_CLAMP_S_CLAMP_T);
		textureFirst.setFilter(Texture.FM_LINEAR);
		tRendererFirst.setupTexture(textureFirst);

		tRendererSecond = display.createTextureRenderer(
				display.getWidth() / renderScale, display.getHeight() / renderScale, false, true, false, false,
				TextureRenderer.RENDER_TEXTURE_2D, 0);
		tRendererSecond.setBackgroundColor(new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f));
		tRendererSecond.setCamera(cam);

		textureSecond = new Texture();
		textureSecond.setWrap(Texture.WM_CLAMP_S_CLAMP_T);
		textureSecond.setFilter(Texture.FM_LINEAR);
		tRendererSecond.setupTexture(textureSecond);

		//Create extract intensity shader
		extractionShader = display.getRenderer().createGLSLShaderObjectsState();
		if(!extractionShader.isSupported()) {
			supported = false;
		} else {
			extractionShader.load(BloomRenderPass.class.getClassLoader().getResource("com/jmex/effects/glsl/data/bloom_extract.vert"),
					BloomRenderPass.class.getClassLoader().getResource("com/jmex/effects/glsl/data/bloom_extract.frag"));
			extractionShader.setEnabled(true);
		}

		//Create blur shader
		blurShader = display.getRenderer().createGLSLShaderObjectsState();
		if(!blurShader.isSupported()) {
			supported = false;
		} else {
			blurShader.load(BloomRenderPass.class.getClassLoader().getResource("com/jmex/effects/glsl/data/bloom_blur.vert"),
					BloomRenderPass.class.getClassLoader().getResource("com/jmex/effects/glsl/data/bloom_blur.frag"));
			blurShader.setEnabled(true);
		}

		//Create final shader(basic texturing)
		finalShader = display.getRenderer().createGLSLShaderObjectsState();
		if(!finalShader.isSupported()) {
			supported = false;
		} else {
			finalShader.load(BloomRenderPass.class.getClassLoader().getResource("com/jmex/effects/glsl/data/bloom_final.vert"),
					BloomRenderPass.class.getClassLoader().getResource("com/jmex/effects/glsl/data/bloom_final.frag"));
			finalShader.setEnabled(true);
		}

		//Create fullscreen quad
		fullScreenQuad = new Quad("FullScreenQuad", display.getWidth(), display.getHeight());
		fullScreenQuad.getLocalRotation().set(0, 0, 0, 1);
		fullScreenQuad.getLocalTranslation().set(display.getWidth() / 2, display.getHeight() / 2, 0);
		fullScreenQuad.getLocalScale().set(1, 1, 1);
		fullScreenQuad.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		fullScreenQuad.setCullMode(Spatial.CULL_NEVER);
		fullScreenQuad.setTextureCombineMode(TextureState.REPLACE);
		fullScreenQuad.setLightCombineMode(LightState.OFF);

		TextureState ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		fullScreenQuad.setRenderState(ts);

		AlphaState as = display.getRenderer().createAlphaState();
		as.setBlendEnabled(true);
		as.setSrcFunction(AlphaState.SB_ONE);
		as.setDstFunction(AlphaState.DB_ONE);
		as.setEnabled(true);
		fullScreenQuad.setRenderState(as);

		fullScreenQuad.updateRenderState();
		fullScreenQuad.updateGeometricState(0.0f, true);
	}

	public void doRender(Renderer r) {
		if(spatials.size() != 1) return;

		tRendererFirst.updateCamera();
		tRendererSecond.updateCamera();

		//Render scene to texture
		tRendererFirst.render((Spatial) spatials.get(0), textureFirst);

		TextureState ts = (TextureState) fullScreenQuad.getRenderState(RenderState.RS_TEXTURE);
		AlphaState as = (AlphaState) fullScreenQuad.getRenderState(RenderState.RS_ALPHA);
		as.setEnabled(false);

		//Extract intensity
		extractionShader.clearUniforms();
		extractionShader.setUniform("RT", 0);
		extractionShader.setUniform("exposurePow", getExposurePow());
		extractionShader.setUniform("exposureCutoff", getExposureCutoff());

		ts.setTexture(textureFirst, 0);
		fullScreenQuad.setRenderState(extractionShader);
		fullScreenQuad.updateRenderState();
		tRendererSecond.render(fullScreenQuad, textureSecond);

		//Blur
		blurShader.clearUniforms();
		blurShader.setUniform("RT", 0);
		blurShader.setUniform("sampleDist0", getBlurSize());
		blurShader.setUniform("blurIntensityMultiplier", getBlurIntensityMultiplier());

		ts.setTexture(textureSecond, 0);
		fullScreenQuad.setRenderState(blurShader);
		fullScreenQuad.updateRenderState();
		tRendererFirst.render(fullScreenQuad, textureFirst);

		//Extra blur passes
		for(int i = 0; i < getNrBlurPasses() - 1; i++) {
			ts.setTexture(textureFirst, 0);
			fullScreenQuad.updateRenderState();
			tRendererSecond.render(fullScreenQuad, textureSecond);

			ts.setTexture(textureSecond, 0);
			fullScreenQuad.updateRenderState();
			tRendererFirst.render(fullScreenQuad, textureFirst);
		}

		//Final blend
		ts.setTexture(textureFirst, 0);
		as.setEnabled(true);
		fullScreenQuad.setRenderState(finalShader);
		fullScreenQuad.updateRenderState();
		fullScreenQuad.onDraw(r);
	}

	public float getBlurSize() {
		return blurSize;
	}

	public void setBlurSize(float blurSize) {
		this.blurSize = blurSize;
	}

	public float getExposurePow() {
		return exposurePow;
	}

	public void setExposurePow(float exposurePow) {
		this.exposurePow = exposurePow;
	}

	public float getExposureCutoff() {
		return exposureCutoff;
	}

	public void setExposureCutoff(float exposureCutoff) {
		this.exposureCutoff = exposureCutoff;
	}

	public float getBlurIntensityMultiplier() {
		return blurIntensityMultiplier;
	}

	public void setBlurIntensityMultiplier(float blurIntensityMultiplier) {
		this.blurIntensityMultiplier = blurIntensityMultiplier;
	}

	public int getNrBlurPasses() {
		return nrBlurPasses;
	}

	public void setNrBlurPasses(int nrBlurPasses) {
		this.nrBlurPasses = nrBlurPasses;
	}
}
