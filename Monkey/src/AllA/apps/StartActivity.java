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
	
	public static final int STATE_SPLASH = 0;
	public static final int STATE_MAIN_LOGO = 1;
	public static final int STATE_MAIN_MENU = 2;
	public static final int STATE_LEVEL_SELECT = 3;
	public static final int STATE_LEVEL_DETAIL = 4;

	private static final float FPS = 60;
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
		
	// Manager
	private SceneManager 	sceneManager;
	private ResourceManager resourceManager;

	// States
	static public int presentState;

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
		
		presentState = STATE_SPLASH;

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

	}
	
	public void initActivity(){
		//sceneManager�� scene���� ����
		sceneManager = new SceneManager(mEngine);
		sceneManager.setBackground(new Background(0, 0, 0));
		
		//ResourceManager����
		resourceManager = new ResourceManager();
		resourceManager.setAssetBasePath("gfx/");
		resourceManager.setContext(this);
		resourceManager.setFontManager(getFontManager());
		resourceManager.setTextureManager(getTextureManager());

		//mainLogoActivity �ʱ�ȭ
		mainLogoActivity = new MainLogoActivity(this);
		mainLogoActivity.setSize(WIDTH, HEIGHT);
		mainLogoActivity.setResourceManager(resourceManager);
		mainLogoActivity.setVertexBufferObjectManager(getVertexBufferObjectManager());

		//mainMenuActivity �ʱ�ȭ
		mainMenuActivity = new MainMenuActivity(this);
		mainMenuActivity.setSize(WIDTH, HEIGHT);
		mainMenuActivity.setResourceManager(resourceManager);
		mainMenuActivity.setVertexBufferObjectManager(getVertexBufferObjectManager());

		//levelSelectActivity �ʱ�ȭ
		levelSelectActivity = new LevelSelectActivity(this);
		levelSelectActivity.setSize(WIDTH, HEIGHT);
		levelSelectActivity.setResourceManager(resourceManager);
		levelSelectActivity.setVertexBufferObjectManager(getVertexBufferObjectManager());
		
		
	}

	public void loadResources() {
		

		mainLogoActivity.loadResources();
		mainMenuActivity.loadResources();
		levelSelectActivity.loadResources();
		
	}
	

	public void loadScenes(int nextState, int out_Effect, int in_Effect) {

		// ������ ���� scene���� ����

		this.mEngine.registerUpdateHandler(new FPSLogger());

		presentState = nextState;

		sceneManager.clearTouchAreas();
		// ���� ���ƿË� ������ �������


		switch (presentState) {

		case STATE_MAIN_LOGO:
			
			mainLogoActivity.loadScene();
			mainLogoActivity.registerTouchAreatoSceneManager(sceneManager);
			sceneManager.setActivity(mainLogoActivity, out_Effect, in_Effect);

			break;

		case STATE_MAIN_MENU:

			mainMenuActivity.loadScene();
			mainMenuActivity.registerTouchAreatoSceneManager(sceneManager);
			sceneManager.setActivity(mainMenuActivity, out_Effect, in_Effect);

			break;

		case STATE_LEVEL_SELECT:

			levelSelectActivity.loadScene();
			levelSelectActivity.registerTouchAreatoSceneManager(sceneManager);
			sceneManager.setActivity(levelSelectActivity, out_Effect, in_Effect);

			break;

		case STATE_LEVEL_DETAIL:

			// levelSelectScene Ȱ��

		}

	}

	private void initSplashScene() {

		// Loadingȭ�� Scene���� ���

		splashScene = new Scene();
		splash = new Sprite(0, 0, splashTextureRegion,
				mEngine.getVertexBufferObjectManager())
		// �ؽ��� �ҷ�����
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither(); // �׶���Ʈ�� ����ϱ� ���ؼ� dither�� Enable����
			}

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				return true;
			}
		};

		
		splash.setPosition((CAMERA_WIDTH - splash.getWidth()) / 2,
				(CAMERA_HEIGHT - splash.getHeight()) / 2);
		splashScene.attachChild(splash);
		
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		// Loadingȭ�� �̹��� Load

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				1280, 720, TextureOptions.DEFAULT);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTextureAtlas, this, "loading.png", 0, 0);
		splashTextureAtlas.load();

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

		// �ٸ� �ð��븦 ���� time handler �߰�

		// �⺻ ���� Ÿ�̸�
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

						// ���� ����� �̹����� Load

						initActivity();
						loadResources();
						loadScenes(STATE_MAIN_LOGO, SceneManager.EFFECT_NONE, SceneManager.EFFECT_NONE);

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
			switch(presentState){
			case STATE_LEVEL_SELECT:
				if(sceneManager.isAnimating() == false)
					loadScenes(STATE_MAIN_MENU, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_IN);

				return true;

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