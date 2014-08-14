package AllA.apps;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

public class MainMenuActivity extends SimpleBaseActivity{
	/* variable */
	Sprite MainMenuBackSprite;
	Sprite levelSelectButtonSprite;
	

	/* Constructor */
	MainMenuActivity(int width, int height,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(width,height,pVertexBufferObjectManager);
	}
	
	/* methods */
	
	@Override
	public void loadResources()
	{
<<<<<<< HEAD
		ResourceManager.loadImage("back2", "back2.png", 1280, 720);
=======
		ResourceManager.loadImage("back2", "back2.jpg", 1280, 720);
>>>>>>> master
		ResourceManager.loadImage("levelButton", "menu_selectlevel.png", 400, 200);

		super.loadResources();
	}
	
	@Override
	public void loadScene()
	{
<<<<<<< HEAD
//		if(isLoaded())
//			return;
=======
		if(isLoaded())
			return;
>>>>>>> master
		
		// background
		
		MainMenuBackSprite = new Sprite(0, 0, ResourceManager.getRegion("back2"), vertexBufferObjectManager);
		mainLayer.attachChild(MainMenuBackSprite);

		
		// components

		levelSelectButtonSprite = new Sprite(0, 0, ResourceManager.getRegion("levelButton"), vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(
					final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					
					SimpleBaseActivity nextActivity = SceneManager.getActivity("levelSelectActivity");
					
					int back_out_Effect = SceneManager.EFFECT_MOVE_UP;
					int back_in_Effect  = SceneManager.EFFECT_MOVE_UP;
					nextActivity.setBackActivity(thisActivity,back_out_Effect, back_in_Effect);

					int out_Effect = SceneManager.EFFECT_MOVE_DOWN;
					int in_Effect  = SceneManager.EFFECT_MOVE_DOWN;

					SceneManager.setActivity(nextActivity, out_Effect, in_Effect);
				}
				return true;

			}

		};
		
<<<<<<< HEAD
		float lSBS_X = (Width-levelSelectButtonSprite.getWidth())/2;
		float lSBS_Y = (Height-levelSelectButtonSprite.getHeight())/2;
		levelSelectButtonSprite.setPosition(lSBS_X, lSBS_Y);
		mainLayer.attachChild(levelSelectButtonSprite);		
=======
		
		float lSBS_X = (Width-levelSelectButtonSprite.getWidth())/2;
		float lSBS_Y = (Height-levelSelectButtonSprite.getHeight())/2;
		levelSelectButtonSprite.setPosition(lSBS_X, lSBS_Y);
		mainLayer.attachChild(levelSelectButtonSprite);
>>>>>>> master

		
		super.loadScene();
	}
	
	@Override
	public void deleteSprites() {
<<<<<<< HEAD
/*		MainMenuBackSprite.dispose();
		levelSelectButtonSprite.dispose();
*/		super.deleteSprites();
=======
		super.deleteSprites();
>>>>>>> master
	}
	
	@Override
	public void registerTouchAreatoSceneManager(){

		SceneManager.registerTouchArea(levelSelectButtonSprite);
		
		super.registerTouchAreatoSceneManager();
	}
	
	
	@Override
	public void updateActivity(){
		
	}
	
	/**/
	
}
