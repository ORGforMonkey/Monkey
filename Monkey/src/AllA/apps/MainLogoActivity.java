package AllA.apps;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.content.Context;
import android.graphics.Typeface;

public class MainLogoActivity extends SimpleBaseActivity{
	/* variable */
	
	private Sprite mainLogoSprite;
	private Text touchToContinue;
	float sw_touchToContinue = -1;

	/* Constructor */
	MainLogoActivity(Context mcontext){
		super(mcontext);
	}
	
	/* methods */
	
	@Override
	public void loadResources()
	{	
		resourceManager.loadImage("mainLogo", "mainLogo.png", 1280, 720);
		resourceManager.loadFont("font1", 256, 256, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 32);

		super.loadResources();
	}
	
	@Override
	public void loadScene()
	{
		if(isLoaded())
			return;
		
		// background
		
		mainLogoSprite = new Sprite(0, 0, resourceManager.getRegion("mainLogo"), vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(
					final TouchEvent pSceneTouchEvent,	
					final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					((StartActivity) context).loadScenes(StartActivity.STATE_MAIN_MENU, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_IN);

				}
				return true;
			}
			
		};
		mainLayer.attachChild(mainLogoSprite);
		

		
		// components
		
		touchToContinue = new Text(0, 0, resourceManager.getFont("font1"), "Touch To Continue", vertexBufferObjectManager);
		float tTC_X = (Width-touchToContinue.getWidth())/2;
		float tTC_Y = 0.8f*Height;
		touchToContinue.setPosition(tTC_X, tTC_Y);
		mainLayer.attachChild(touchToContinue);
		

		super.loadScene();
	}
	
	@Override
	public void registerTouchAreatoSceneManager(SceneManager pSceneManager){

		pSceneManager.registerTouchArea(mainLogoSprite);
		
		super.registerTouchAreatoSceneManager(pSceneManager);
	}
	
	
	@Override
	public void updateActivity(){
// touchToCountinue ���� ȿ��
		
//		if(!isAlwaysUpdated())
		if (touchToContinue.getAlpha() + sw_touchToContinue / 60 >= 1)
			sw_touchToContinue = -1;
		if (touchToContinue.getAlpha() + sw_touchToContinue / 60 <= 0)
			sw_touchToContinue = +1;
		touchToContinue.setAlpha(touchToContinue.getAlpha() + sw_touchToContinue / 60);
	}
	
	/**/
	
}
