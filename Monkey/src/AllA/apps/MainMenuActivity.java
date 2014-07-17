package AllA.apps;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.content.Context;

public class MainMenuActivity extends SimpleBaseActivity{
	/* variable */
	Sprite MainMenuBackSprite;
	Sprite levelSelectButtonSprite;
	

	/* Constructor */
	MainMenuActivity(Context mcontext){
		super(mcontext);
	}
	
	/* methods */
	
	@Override
	public void loadResources()
	{
		resourceManager.loadImage("back2", "back2.png", 1280, 720);
		resourceManager.loadImage("levelButton", "menu_selectlevel.png", 400, 200);

		super.loadResources();
	}
	
	@Override
	public void loadScene()
	{
		if(isLoaded())
			return;
		
		// background
		
		MainMenuBackSprite = new Sprite(0, 0, resourceManager.getRegion("back2"), vertexBufferObjectManager);
		mainLayer.attachChild(MainMenuBackSprite);

		
		// components

		levelSelectButtonSprite = new Sprite(0, 0, resourceManager.getRegion("levelButton"), vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(
					final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					((StartActivity) context).loadScenes(StartActivity.STATE_LEVEL_SELECT, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_IN);
				}
				return true;

			}

		};
		
		float lSBS_X = (Width-levelSelectButtonSprite.getWidth())/2;
		float lSBS_Y = (Height-levelSelectButtonSprite.getHeight())/2;
		levelSelectButtonSprite.setPosition(lSBS_X, lSBS_Y);
		mainLayer.attachChild(levelSelectButtonSprite);		

		
		super.loadScene();
	}
	
	@Override
	public void registerTouchAreatoSceneManager(SceneManager pSceneManager){

		pSceneManager.registerTouchArea(levelSelectButtonSprite);
		
		super.registerTouchAreatoSceneManager(pSceneManager);
	}
	
	
	@Override
	public void updateActivity(){
		
	}
	
	/**/
	
}
