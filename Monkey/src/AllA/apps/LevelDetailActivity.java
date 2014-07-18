package AllA.apps;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.content.Context;
import android.graphics.Typeface;

public class LevelDetailActivity extends SimpleBaseActivity{
	/* variable */
	
	private Sprite mainLogoSprite;
	private Text touchToContinue;
	float sw_touchToContinue = -1;

	/* Constructor */
	LevelDetailActivity(Context mcontext){
		super(mcontext);
	}
	
	/* methods */
	
	@Override
	public void loadResources()
	{	
//		for(int i=0;i<MAX_LEVEL;i++)	resourceManager.loadImage("leveldetailbutton"+i, "leveldetailbutton.png", 1280, 720);
		ResourceManager.loadFont("font1", 256, 256, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 32);

		super.loadResources();
	}
	
	@Override
	public void loadScene()
	{
		if(isLoaded())
			return;
		
		// background
		
		mainLogoSprite = new Sprite(0, 0, ResourceManager.getRegion("mainLogo"), vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(
					final TouchEvent pSceneTouchEvent,	
					final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {

				}
				return true;
			}
			
		};
		mainLayer.attachChild(mainLogoSprite);
		

		
		// components
		
		touchToContinue = new Text(0, 0, ResourceManager.getFont("font1"), "Touch To Continue", vertexBufferObjectManager);
		float tTC_X = (Width-touchToContinue.getWidth())/2;
		float tTC_Y = 0.8f*Height;
		touchToContinue.setPosition(tTC_X, tTC_Y);
		mainLayer.attachChild(touchToContinue);
		

		super.loadScene();
	}
	
	@Override
	public void registerTouchAreatoSceneManager(){

		SceneManager.registerTouchArea(mainLogoSprite);
		
		super.registerTouchAreatoSceneManager();
	}
	
	
	@Override
	public void updateActivity(){
// touchToCountinue 글자 효과
		
//		if(!isAlwaysUpdated())
		if (touchToContinue.getAlpha() + sw_touchToContinue / 60 >= 1)
			sw_touchToContinue = -1;
		if (touchToContinue.getAlpha() + sw_touchToContinue / 60 <= 0)
			sw_touchToContinue = +1;
		touchToContinue.setAlpha(touchToContinue.getAlpha() + sw_touchToContinue / 60);
	}
	
	
	/**/
	
}
