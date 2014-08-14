package AllA.apps;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Typeface;

public class MainLogoActivity extends SimpleBaseActivity{
	/* variable */
	
	private Sprite mainLogoSprite;
	private Text touchToContinue;
	float sw_touchToContinue = -1;

	/* Constructor */
	MainLogoActivity(int width, int height,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(width,height,pVertexBufferObjectManager);
	}
	
	/* methods */
	

	@Override
	public void loadResources()
	{	
<<<<<<< HEAD
		ResourceManager.loadImage("mainLogo", "mainLogo.png", 1280, 720);
=======
		ResourceManager.loadImage("mainLogo", "mainLogo.jpg", 1280, 720);
>>>>>>> master
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

					SimpleBaseActivity nextActivity = SceneManager.getActivity("mainMenuActivity");

					int out_Effect = SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_OUT;
					int in_Effect  = SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_IN;
					SceneManager.setActivity(nextActivity, out_Effect, in_Effect);

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
