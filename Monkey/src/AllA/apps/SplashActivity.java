package AllA.apps;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SplashActivity extends SimpleBaseActivity{

	/* variable */
	Sprite splashSprite;
	
	/* Constructor */
	SplashActivity(int width, int height,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(width,height,pVertexBufferObjectManager);
	}
	
	/* methods */
	
	@Override
	public void loadResources() {
		ResourceManager.loadImage("loading", "loading.png", 1280, 720);
		
		super.loadResources();
	}
	
	@Override
	public void loadScene() {
		splashSprite = new Sprite(0, 0, ResourceManager.getRegion("loading"), vertexBufferObjectManager);
		
		float splash_X = (Width - splashSprite.getWidth()) / 2;
		float splash_Y = (Height - splashSprite.getHeight()) / 2;
		splashSprite.setPosition(splash_X, splash_Y);

		mainLayer.attachChild(splashSprite);
		
		super.loadScene();
	}
	
	@Override
	public void registerTouchAreatoSceneManager() {
		// TODO Auto-generated method stub
		super.registerTouchAreatoSceneManager();
	}
	
	@Override
	public void updateActivity() {
		// TODO Auto-generated method stub
		super.updateActivity();
	}
}
