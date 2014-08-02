package AllA.apps;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.ui.activity.BaseGameActivity;

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
	
	public static int MAX_LEVEL = 6;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private SimpleBaseActivity splashActivity;
	private SimpleBaseActivity mainLogoActivity;
	private SimpleBaseActivity mainMenuActivity;
	private SimpleBaseActivity levelSelectActivity;
	private SimpleBaseActivity levelDetailActivity;
	private SimpleBaseActivity gameActivity;

	private TimerHandler onGameTimer;
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	@SuppressWarnings("deprecation")
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
		mainLogoActivity = new MainLogoActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		mainLogoActivity.loadResources();
		SceneManager.registerActivity("mainLogoActivity", mainLogoActivity);

		//mainMenuActivity 초기화
		mainMenuActivity = new MainMenuActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		mainMenuActivity.loadResources();
		SceneManager.registerActivity("mainMenuActivity", mainMenuActivity);

		//levelSelectActivity 초기화
		levelSelectActivity = new LevelSelectActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		levelSelectActivity.loadResources();
		SceneManager.registerActivity("levelSelectActivity", levelSelectActivity);
	
		//levelDetailActivity 초기화
		levelDetailActivity = new LevelDetailActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		levelDetailActivity.loadResources();
		SceneManager.registerActivity("levelDetailActivity", levelDetailActivity);
		
		
		//GameActivity 초기화
		gameActivity = new GameActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		gameActivity.loadResources();
		enableAccelerationSensor((IAccelerationListener) gameActivity);
		SceneManager.registerActivity("gameActivity", gameActivity);
	}
	
	protected void addTimer(){
		
		// 기본 게임 타이머
		onGameTimer = new TimerHandler(1 / FPS, true, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				updateObject();
			}
		});
		

		mEngine.registerUpdateHandler(new FPSLogger());
		mEngine.registerUpdateHandler(new TimerHandler(0.01f, new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {

						// 사용한 Activity들을 생성
						initActivity();

						SceneManager.setActivity(mainLogoActivity);

						mEngine.registerUpdateHandler(onGameTimer);
						mEngine.unregisterUpdateHandler(pTimerHandler);
					}
				}));

	}
	
	protected void updateObject() {

		mainLogoActivity.updateActivity();
		mainMenuActivity.updateActivity();
		levelSelectActivity.updateActivity();
		levelDetailActivity.updateActivity();
		gameActivity.updateActivity();

	}
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		final BoundCamera camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		// Manager 설정
		initManager();

		// Splash 생성
		splashActivity = new SplashActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		splashActivity.loadResources();

		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {

		splashActivity.loadScene();
		
		Scene splashScene = new Scene();
		splashScene.attachChild(splashActivity.mainLayer);
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);

	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		
		addTimer();
		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Key입력 처리
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
						
			if( SceneManager.presentActivity != null){
				
				if(SceneManager.presentActivity.getBackActivity() != null){
					if(!SceneManager.isAnimating())		SceneManager.goBack();
					return true;
				}
				else{
					//종료
					finish();
				}
				
			}
		}

		return super.onKeyDown(keyCode, event);		
	}
	
	@Override
	protected void onPause() {
		if(SceneManager.getPresentActivity() != null)
			SceneManager.getPresentActivity().onPause();
		super.onPause();
	}
		
	@Override
	protected synchronized void onResume() {
		if(SceneManager.getPresentActivity() != null)
			SceneManager.getPresentActivity().onResume();
		super.onResume();
	}
	
	



}