package AllA.apps;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.Typeface;
import android.widget.Toast;

/**
 * (c) 2014 Park Hyung Kee
 * 
 * @since 07.04
 * 
 * 프레지 컨셉 따라하기
 * !
 * !
 * !으어어어
 * !
 * ! v
 * ! |
 * ! v
 * ! O
 * !
 * !
 * (원숭이마을)
 */

public class StartActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 1280;
	private static final int CAMERA_HEIGHT = 720;

	private static final int STATE_SPLASH = 0;
	private static final int STATE_MAIN_LOGO = 1;
	private static final int STATE_MAIN_MENU = 2;
	private static final int STATE_LEVEL_SELECT = 3;
	private static final int STATE_LEVEL_DETAIL = 4;

	private static final int MAX_LEVEL = 6;
	private static final float FPS = 60;
	// ===========================================================
	// Fields
	// ===========================================================
	// Bitmap Atlas
	private BitmapTextureAtlas splashTextureAtlas;
	private BitmapTextureAtlas mainLogoTextureAtlas;
	private BitmapTextureAtlas main_LevelSelectButton_TextureAtlas;
	private BitmapTextureAtlas levelSelectTextureAtlas;
	private BitmapTextureAtlas levelMainTextureAtlas[] = new BitmapTextureAtlas[MAX_LEVEL];
	private BitmapTextureAtlas mBitmapTextureAtlas[] = new BitmapTextureAtlas[10];

	// Region
	private ITextureRegion splashTextureRegion;
	private ITextureRegion mainLogoTextureRegion;
	private ITextureRegion main_LevelSelectButton_TextureRegion;
	private ITextureRegion levelMainTextureRegion[] = new ITextureRegion[MAX_LEVEL];
	private ITextureRegion mBackgroundTextureRegion[] = new ITextureRegion[10];

	// Sprite
	private Sprite splash;

	// Scene
	private Scene splashScene;
	private Scene mainLogoScene;
	private Scene mainMenuScene;

	private Scene levelSelectScene;

	private int presentState;

	private Text touchToCountinue;
	private int sw_touchToCountinue = 1; // 1증가 -1감소

	private IFont mBasicFont;

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

	public void loadResources() {

		// 실제로 사용될 이미지들 Load

		for (int i = 0; i < 5; i++) {
			this.mBitmapTextureAtlas[i] = new BitmapTextureAtlas(
					this.getTextureManager(), 1920, 1920,
					TextureOptions.BILINEAR);

			this.mBackgroundTextureRegion[i] = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapTextureAtlas[i], this, "test"
							+ (i + 1) + ".png", 0, 0);

			this.mBitmapTextureAtlas[i].load();
		}

		for (int i = 0; i < MAX_LEVEL; i++) {
			levelMainTextureAtlas[i] = new BitmapTextureAtlas(
					this.getTextureManager(), 400, 400, TextureOptions.BILINEAR);

			levelMainTextureRegion[i] = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelMainTextureAtlas[i], this, "level"
							+ (i + 1) + "/main_level.png", 0, 0);
			
			levelMainTextureAtlas[i].load();
		}

		mainLogoTextureAtlas = new BitmapTextureAtlas(getTextureManager(),
				1280, 720, TextureOptions.BILINEAR);
		mainLogoTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mainLogoTextureAtlas, this, "StartLogo.png",
						0, 0);
		mainLogoTextureAtlas.load();

		main_LevelSelectButton_TextureAtlas = new BitmapTextureAtlas(
				getTextureManager(), 397, 255, TextureOptions.BILINEAR);
		main_LevelSelectButton_TextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(main_LevelSelectButton_TextureAtlas, this,
						"menu_selectlevel.png", 0, 0);
		main_LevelSelectButton_TextureAtlas.load();

		// 실제 사용할 Font
		mBasicFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 32);
		mBasicFont.load();

	}

	private void loadScenes(int nextState) {

		// 실제로 사용될 scene들을 구성

		this.mEngine.registerUpdateHandler(new FPSLogger());
		final VertexBufferObjectManager vertexBufferObjectManager = getVertexBufferObjectManager();

		presentState = nextState;

		switch (presentState) {

		case STATE_MAIN_LOGO:

			if (mainLogoScene == null) {
				mainLogoScene = new Scene();

				final Sprite MainLogo = new Sprite(0, 0, mainLogoTextureRegion,
						vertexBufferObjectManager) {
					@Override
					public boolean onAreaTouched(
							final TouchEvent pSceneTouchEvent,
							final float pTouchAreaLocalX,
							final float pTouchAreaLocalY) {
						if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
							loadScenes(STATE_MAIN_MENU);
						}
						return true;

					}
				};
				MainLogo.setScale(1);
				mainLogoScene.attachChild(MainLogo);
				mainLogoScene.registerTouchArea(MainLogo);

				touchToCountinue = new Text(0, 0, mBasicFont,
						"Touch Screen to Continue", 25,
						vertexBufferObjectManager);
				touchToCountinue.setPosition(
						(CAMERA_WIDTH - touchToCountinue.getWidth()) / 2,
						CAMERA_HEIGHT - 100);
				touchToCountinue.setAlpha(0);
				mainLogoScene.attachChild(touchToCountinue);

				mainLogoScene.setTouchAreaBindingOnActionDownEnabled(true);

				mEngine.setScene(mainLogoScene);

			}
			break;

		case STATE_MAIN_MENU:

			if (mainMenuScene == null) {
				mainMenuScene = new Scene();

				Sprite MainMenuBackSprite = new Sprite(0, 0,
						mBackgroundTextureRegion[1], vertexBufferObjectManager);
				MainMenuBackSprite.setScale(1);
				mainMenuScene.attachChild(MainMenuBackSprite);

				Sprite levelSelectButtonSprite = new Sprite(0, 0,
						main_LevelSelectButton_TextureRegion,
						vertexBufferObjectManager) {
					@Override
					public boolean onAreaTouched(
							final TouchEvent pSceneTouchEvent,
							final float pTouchAreaLocalX,
							final float pTouchAreaLocalY) {
						if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
							loadScenes(STATE_LEVEL_SELECT);
						}
						return true;

					}

				};

				levelSelectButtonSprite.setScale(1);
				levelSelectButtonSprite
						.setPosition((CAMERA_WIDTH - levelSelectButtonSprite
								.getWidth()) / 2,
								(CAMERA_HEIGHT - levelSelectButtonSprite
										.getHeight()) / 2);
				mainMenuScene.attachChild(levelSelectButtonSprite);
				mainMenuScene.registerTouchArea(levelSelectButtonSprite);

				mainMenuScene.setTouchAreaBindingOnActionDownEnabled(true);

				mEngine.setScene(mainMenuScene);
			}

			break;

		case STATE_LEVEL_SELECT:

			if (levelSelectScene == null) {
				levelSelectScene = new Scene();

				Sprite levelSelectBackSprite = new Sprite(0, 0,
						mBackgroundTextureRegion[2], vertexBufferObjectManager);
				levelSelectBackSprite.setScale(1);
				levelSelectScene.attachChild(levelSelectBackSprite);


				// level을 고르는 버튼들
				
				Sprite levelMainSprite[] = new Sprite[MAX_LEVEL];
				for (int i = 0; i < MAX_LEVEL; i++) {
					levelMainSprite[i] = new Sprite(0, 0,
							levelMainTextureRegion[i],
							vertexBufferObjectManager) {
						@Override
						public boolean onAreaTouched(
								TouchEvent pSceneTouchEvent,
								float pTouchAreaLocalX, float pTouchAreaLocalY) {
							
							return true;
						}
					};

					levelMainSprite[i].setScale(0.8f);
					
					float levelMainSprite_X = 100 + levelMainSprite[i].getWidth()*levelMainSprite[i].getScaleX() * 1.2f* i;
					float levelMainSprite_Y = (CAMERA_HEIGHT - levelMainSprite[i].getHeight()) / 2;

					levelMainSprite[i].setPosition(levelMainSprite_X, levelMainSprite_Y);
					levelSelectScene.attachChild(levelMainSprite[i]);
					
					Text levelText = new Text(0, 0, mBasicFont, "level"+(i+1), ("level"+(i+1)).length(), vertexBufferObjectManager);
					
					float levelText_X = levelMainSprite_X + (levelMainSprite[i].getWidth()*levelMainSprite[i].getScaleX() - levelText.getWidth())/2;
					float levelText_Y = (CAMERA_HEIGHT - levelText.getHeight()) / 2;
					
					levelText.setPosition(levelText_X, levelText_Y);
					levelSelectScene.attachChild(levelText);

					levelSelectScene.registerTouchArea(levelMainSprite[i]);
				}


				levelSelectScene.setTouchAreaBindingOnActionDownEnabled(true);

				mEngine.setScene(levelSelectScene);
			}

			break;

		case STATE_LEVEL_DETAIL:

			// levelSelectScene 활용

		}

	}

	private void initSplashScene() {

		// Loading화면 Scene으로 출력

		splashScene = new Scene();
		splash = new Sprite(0, 0, splashTextureRegion,
				mEngine.getVertexBufferObjectManager())
		// 텍스쳐 불러오기
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither(); // 그라디언트를 사용하기 위해서 dither를 Enable해줌
			}

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		splash.setScale(0.4f);
		splash.setPosition((CAMERA_WIDTH - splash.getWidth()) / 2,
				(CAMERA_HEIGHT - splash.getHeight()) / 2);
		splashScene.attachChild(splash);
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		// Loading화면 이미지 Load

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				2400, 1920, TextureOptions.DEFAULT);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTextureAtlas, this, "loading.png", 0, 0);
		splashTextureAtlas.load();

		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub

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
						loadResources();
						loadScenes(STATE_MAIN_LOGO);

						mEngine.registerUpdateHandler(onGameTimer);
					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	protected void updateObject() {
		switch (presentState) {

		case STATE_MAIN_LOGO:
			// touchToCountinue 글자 효과
			if (touchToCountinue.getAlpha() + sw_touchToCountinue * (1 / FPS) >= 1)
				sw_touchToCountinue = -1;
			if (touchToCountinue.getAlpha() + sw_touchToCountinue * (1 / FPS) <= 0)
				sw_touchToCountinue = +1;
			touchToCountinue.setAlpha(touchToCountinue.getAlpha()
					+ sw_touchToCountinue * (1 / FPS));

			break;

		case STATE_MAIN_MENU:
			break;

		}
		// TODO Auto-generated method stub

	}

	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}