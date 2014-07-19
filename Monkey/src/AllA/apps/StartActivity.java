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
 * ������ ���� �����ϱ�
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
	private Sprite splash;
	private Scene splashScene;
	
	
	
	// Activities
	private SimpleBaseActivity mainLogoActivity;
	private SimpleBaseActivity mainMenuActivity;
	private SimpleBaseActivity levelSelectActivity;
	private SimpleBaseActivity levelDetailActivity;


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
		//ȭ�� ������ �޾ƿ´�

		PHONE_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		PHONE_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		
		//SceneManager�� scene���� ����
		SceneManager.registerEngine(mEngine);
		SceneManager.init();
		SceneManager.setBackground(new Background(0, 0, 0));
				
		//ResourceManager����
		ResourceManager.setAssetBasePath("gfx/");
		ResourceManager.setContext(this);
		ResourceManager.setFontManager(getFontManager());
		ResourceManager.setTextureManager(getTextureManager());
		
	}
	
	public void initActivity(){

		//mainLogoActivity �ʱ�ȭ
		mainLogoActivity = new MainLogoActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		SceneManager.registerActivity("mainLogoActivity", mainLogoActivity);

		//mainMenuActivity �ʱ�ȭ
		mainMenuActivity = new MainMenuActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		SceneManager.registerActivity("mainMenuActivity", mainMenuActivity);

		//levelSelectActivity �ʱ�ȭ
		levelSelectActivity = new LevelSelectActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		SceneManager.registerActivity("levelSelectActivity", levelSelectActivity);
	
		//levelDetailActivity �ʱ�ȭ
		levelDetailActivity = new LevelSelectActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		SceneManager.registerActivity("levelDetailActivity", levelDetailActivity);
	}

	public void loadResources() {

		mainLogoActivity.loadResources();
		mainMenuActivity.loadResources();
		levelSelectActivity.loadResources();
		levelDetailActivity.loadResources();
		
	}

	private void initSplashScene() {

		// Loadingȭ�� Scene���� ���

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

		// Loadingȭ�� �̹��� Load
		initManager();
		ResourceManager.loadImage("loading", "loading.png", 1280, 720);

		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {

		initSplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);

	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub

		// �ٸ� �ð��븦 ���� time handler �߰�

		// �⺻ ���� Ÿ�̸�
		onGameTimer = new TimerHandler(1 / FPS, true, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				updateObject();
			}
		});

		// Loading
		mEngine.registerUpdateHandler(new FPSLogger());
		mEngine.registerUpdateHandler(new TimerHandler(0.01f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);

						// ���� ����� �̹����� Load
						initActivity();
						loadResources();
						
						SceneManager.setActivity(mainLogoActivity, SceneManager.EFFECT_NONE, SceneManager.EFFECT_NONE);

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
			
			if( SceneManager.presentActivity != null ){
				// ���� ���� ���� ������ back��ư���� ��ȯ�Ѵ�.
				if(SceneManager.presentActivity.getBackActivity() != null){
					// ���� ������ ��ȯ
					SceneManager.goBack();
					// �������� ����
					return true;
				}
			}
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