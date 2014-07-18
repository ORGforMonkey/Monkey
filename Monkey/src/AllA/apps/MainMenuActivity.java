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
		ResourceManager.loadImage("back2", "back2.png", 1280, 720);
		ResourceManager.loadImage("levelButton", "menu_selectlevel.png", 400, 200);

		super.loadResources();
	}
	
	@Override
	public void loadScene()
	{
		if(isLoaded())
			return;
		
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

					nextActivity.loadScene();
					nextActivity.registerTouchAreatoSceneManager();
					int out_Effect = SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_OUT;
					int in_Effect  = SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_IN;

					SceneManager.setActivity(nextActivity, out_Effect, in_Effect);
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
	public void registerTouchAreatoSceneManager(){

		SceneManager.registerTouchArea(levelSelectButtonSprite);
		
		super.registerTouchAreatoSceneManager();
	}
	
	
	@Override
	public void updateActivity(){
		
	}
	
	/**/
	
}
