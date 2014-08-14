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
<<<<<<< HEAD
=======
import org.andengine.input.sensor.acceleration.IAccelerationListener;
>>>>>>> master
import org.andengine.ui.activity.BaseGameActivity;

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
	
	public static int MAX_LEVEL = 6;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private SimpleBaseActivity splashActivity;
	private SimpleBaseActivity mainLogoActivity;
	private SimpleBaseActivity mainMenuActivity;
	private SimpleBaseActivity levelSelectActivity;
	private SimpleBaseActivity levelDetailActivity;
<<<<<<< HEAD

	private TimerHandler onGameTimer;
	
	
=======
	private SimpleBaseActivity gameActivity;

	private TimerHandler onGameTimer;
	
>>>>>>> master
	// ===========================================================
	// Methods
	// ===========================================================
	
	@SuppressWarnings("deprecation")
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
		
<<<<<<< HEAD
=======
		
>>>>>>> master
	}
	
	public void initActivity(){

		//mainLogoActivity �ʱ�ȭ
		mainLogoActivity = new MainLogoActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		mainLogoActivity.loadResources();
		SceneManager.registerActivity("mainLogoActivity", mainLogoActivity);

		//mainMenuActivity �ʱ�ȭ
		mainMenuActivity = new MainMenuActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		mainMenuActivity.loadResources();
		SceneManager.registerActivity("mainMenuActivity", mainMenuActivity);

		//levelSelectActivity �ʱ�ȭ
		levelSelectActivity = new LevelSelectActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		levelSelectActivity.loadResources();
		SceneManager.registerActivity("levelSelectActivity", levelSelectActivity);
	
		//levelDetailActivity �ʱ�ȭ
		levelDetailActivity = new LevelDetailActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		levelDetailActivity.loadResources();
		SceneManager.registerActivity("levelDetailActivity", levelDetailActivity);
<<<<<<< HEAD
=======
		
		
		//GameActivity �ʱ�ȭ
		gameActivity = new GameActivity(WIDTH, HEIGHT, getVertexBufferObjectManager());
		gameActivity.loadResources();
		enableAccelerationSensor((IAccelerationListener) gameActivity);
		SceneManager.registerActivity("gameActivity", gameActivity);
>>>>>>> master
	}
	
	protected void addTimer(){
		
		// �⺻ ���� Ÿ�̸�
		onGameTimer = new TimerHandler(1 / FPS, true, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				updateObject();
			}
		});
		

<<<<<<< HEAD

=======
>>>>>>> master
		mEngine.registerUpdateHandler(new FPSLogger());
		mEngine.registerUpdateHandler(new TimerHandler(0.01f, new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {

						// ����� Activity���� ����
						initActivity();

						SceneManager.setActivity(mainLogoActivity);

						mEngine.registerUpdateHandler(onGameTimer);
<<<<<<< HEAD
						
						mEngine.unregisterUpdateHandler(pTimerHandler);
					}
				}));
		
		
=======
						mEngine.unregisterUpdateHandler(pTimerHandler);
					}
				}));
>>>>>>> master

	}
	
	protected void updateObject() {

		mainLogoActivity.updateActivity();
		mainMenuActivity.updateActivity();
		levelSelectActivity.updateActivity();
		levelDetailActivity.updateActivity();
<<<<<<< HEAD
=======
		gameActivity.updateActivity();
>>>>>>> master

	}
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
<<<<<<< HEAD
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
=======
		final BoundCamera camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
>>>>>>> master
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		// Manager ����
		initManager();

		// Splash ����
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
		// TODO Key�Է� ó��
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
						
			if( SceneManager.presentActivity != null){
<<<<<<< HEAD
				
				if(SceneManager.presentActivity.getBackActivity() != null){
					if(!SceneManager.isAnimating())		SceneManager.goBack();
					return true;
				}
				
			}			
		}

		return super.onKeyDown(keyCode, event);		
=======
				
				if(SceneManager.presentActivity.getBackActivity() != null){
					if(!SceneManager.isAnimating())		SceneManager.goBack();
					return true;
				}
				else{
					//����
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
>>>>>>> master
	}
	
	


<<<<<<< HEAD

=======
>>>>>>> master

}