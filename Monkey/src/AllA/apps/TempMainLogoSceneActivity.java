package AllA.apps;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class TempMainLogoSceneActivity{
	/* variable */
	private Sprite MainLogoSprite;
	private BitmapTextureAtlas mainLogoTextureAtlas;
	private ITextureRegion mainLogoTextureRegion;
	private Entity TempMainLogoScene;
	private ResourceManager resourceManager;
	VertexBufferObjectManager vertexBufferObjectManager;
	/* methods */
	
	void loadResources()
	{	
		Log.e("»ÑÀ×", "test");
		resourceManager.loadImage("StartLogo", "StartLogo.png", 1280, 720);

		Log.e("»ÑÀ×", "test");

	}
	void tempmethod(SceneManager sceneManager, final int nextState, int out_Effect, int in_Effect)
	{

		MainLogoSprite = new Sprite(0, 0, resourceManager.getRegion("StartLogo"),
				vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(
					final TouchEvent pSceneTouchEvent,	
					final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
//					StartActivity.loadScenes(nextState, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_IN);

				}
				return true;
			}
		};
		
		TempMainLogoScene.attachChild(MainLogoSprite);
		sceneManager.registerTouchArea(MainLogoSprite);
		sceneManager.setScene(TempMainLogoScene, out_Effect, in_Effect);
	}
	
	
	public void setResourceManager (ResourceManager pResourceManager)
	{
		resourceManager = pResourceManager;
	}

	public void setVertexBufferObjectManager (VertexBufferObjectManager pVertexBufferObjectManager)
	{
		vertexBufferObjectManager = pVertexBufferObjectManager;
	}
	
	/* animations */
	
}
