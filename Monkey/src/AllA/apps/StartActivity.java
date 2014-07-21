package AllA.apps;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;



import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;

/**
 * (c) 2014 Park Hyung Kee
 * 
 * @since 07.018
 * 
 * 프레지 컨셉 따라하기
 */

public class StartActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int CAMERA_WIDTH = 1280;
	public static final int CAMERA_HEIGHT = 720;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	private static final float FPS = 60;

	public static int PHONE_WIDTH;
	public static int PHONE_HEIGHT;
	// ===========================================================
	// Fields
	// ===========================================================
	// for Splash
	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	private Sprite splash;
	private Scene splashScene;
	
	
	
	// Activities
	private SimpleBaseActivity mainLogoActivity;
	private SimpleBaseActivity mainMenuActivity;
	private SimpleBaseActivity levelSelectActivity;


	// Handler
	private TimerHandler onGameTimer;
	

	// ===========================================================
	// Constructors
	// ===========================================================
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

	}
	
	public void initManager(){
		//화면 정보를 받아온다

		PHONE_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		PHONE_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		
		//SceneManager로 scene들을 관리
		SceneManager.registerEngine(mEngine);
		SceneManager.init();
		SceneManager.setBackground(new Background(0, 0, 0));
				
		//ResourceManager설정
		ResourceManager.setAssetBasePath("gfx/");
		ResourceManager.setContext(this);
		ResourceManager.setFontManager(getFontManager());
		ResourceManager.setTextureManager(getTextureManager());
		
	}
	
	public void initActivity(){

		//mainLogoActivity 초기화
		mainLogoActivity = new MainLogoActivity(this);
		mainLogoActivity.setSize(WIDTH, HEIGHT);
		mainLogoActivity.setVertexBufferObjectManager(getVertexBufferObjectManager());
		SceneManager.registerActivity("mainLogoActivity",mainLogoActivity);

		//mainMenuActivity 초기화
		mainMenuActivity = new MainMenuActivity(this);
		mainMenuActivity.setSize(WIDTH, HEIGHT);
		mainMenuActivity.setVertexBufferObjectManager(getVertexBufferObjectManager());
		SceneManager.registerActivity("mainMenuActivity", mainMenuActivity);

		//levelSelectActivity 초기화
		levelSelectActivity = new LevelSelectActivity(this);
		levelSelectActivity.setSize(WIDTH, HEIGHT);
		levelSelectActivity.setVertexBufferObjectManager(getVertexBufferObjectManager());
		SceneManager.registerActivity("levelSelectActivity", levelSelectActivity);
	
	}

	public void loadResources() {

		mainLogoActivity.loadResources();
		mainMenuActivity.loadResources();
		levelSelectActivity.loadResources();
		
	}
	
public void loadScenes(SimpleBaseActivity nextActivity, int out_Effect, int in_Effect) {

		// 실제로 사용될 scene들을 구성

		this.mEngine.registerUpdateHandler(new FPSLogger());

		SceneManager.setActivity(nextActivity, out_Effect, in_Effect);

	}

	private void initSplashScene() {

		// Loading화면 Scene으로 출력

		splashScene = new Scene();
		splash = new Sprite(0, 0, ResourceManager.getRegion("loading"), mEngine.getVertexBufferObjectManager());
		
		float splash_X = (CAMERA_WIDTH - splash.getWidth()) / 2;
		float splash_Y = (CAMERA_HEIGHT - splash.getHeight()) / 2;
		splash.setPosition(splash_X, splash_Y);

		splashScene.attachChild(splash);
		
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		// Loading화면 이미지 Load
		initManager();
		ResourceManager.loadImage("loading", "loading.png", 1280, 720);

		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {

		initSplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);

	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub

		// 다른 시간대를 갖는 time handler 추가

		// 기본 게임 타이머
		onGameTimer = new TimerHandler(1 / FPS, true, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				updateObject();
			}
		});

		// Loading
		mEngine.registerUpdateHandler(new TimerHandler(0.01f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);

						// 실제 사용할 이미지들 Load
						initActivity();
						loadResources();
						loadScenes(mainLogoActivity, SceneManager.EFFECT_NONE, SceneManager.EFFECT_NONE);

						mEngine.registerUpdateHandler(onGameTimer);
					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	protected void updateObject() {
		
		if(mainLogoActivity != null)
			mainLogoActivity.updateActivity();
		if(mainMenuActivity != null)
			mainMenuActivity.updateActivity();
		if(levelSelectActivity != null)
			levelSelectActivity.updateActivity();

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(SceneManager.presentActivity == levelSelectActivity)
				if(SceneManager.isAnimating() == false)
					loadScenes(mainMenuActivity, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_IN);

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}