package AllA.apps;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;




import android.graphics.Typeface;
import android.view.KeyEvent;

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
	public static final int CAMERA_WIDTH = 1280;
	public static final int CAMERA_HEIGHT = 720;

	private static final int STATE_SPLASH = 0;
	private static final int STATE_MAIN_LOGO = 1;
	private static final int STATE_MAIN_MENU = 2;
	private static final int STATE_LEVEL_SELECT = 3;
	private static final int STATE_LEVEL_DETAIL = 4;
	
	private static final int FOCUS_NONE = 0;
	private static final int FOCUS_LEVEL_SELECT = 1;
	private static final int FOCUS_LEVEL_SCENE_MOVE = 2;

	private static final int MAX_LEVEL = 6;
	private static final float FPS = 60;
	// ===========================================================
	// Fields
	// ===========================================================
	// Bitmap Atlas
	private BitmapTextureAtlas splashTextureAtlas;
	private BitmapTextureAtlas mainLogoTextureAtlas;
	private BitmapTextureAtlas main_LevelSelectButton_TextureAtlas;
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
	private Rectangle scrollBar;
	Sprite levelMainSprite[] = new Sprite[MAX_LEVEL];
	private ScrollSprite levelScrollSprite;
	private ScrollSprite levelScrollSprite2;
	private Sprite MainLogoSprite;
	private Sprite levelSelectButtonSprite;
	private Sprite levelSelectBackSprite;
	
	
	// Scene
	private Scene splashScene;
	private Entity mainLogoScene;
	private Entity mainMenuScene;
	private Entity levelSelectScene;
	
	// Scene Manager
	private SceneManager sceneManager;


	private int presentState;
	private int presentFocus;

	private Text touchToCountinue;
	private int sw_touchToCountinue = 1; // 1증가 -1감소

	private IFont mBasicFont;

	private TimerHandler onGameTimer;
	
	SurfaceScrollDetector mScrollDetector;
	SurfaceScrollDetector mScrollDetector2;

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
		presentFocus = FOCUS_NONE;

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

	}

	public void loadResources() {
		
		// 실제로 사용될 이미지들 Load

		for (int i = 0; i < 4; i++) {
			this.mBitmapTextureAtlas[i] = new BitmapTextureAtlas(
					this.getTextureManager(), 1920, 1920,
					TextureOptions.BILINEAR);

			this.mBackgroundTextureRegion[i] = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapTextureAtlas[i], this, "back"
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
				getTextureManager(), 400, 200, TextureOptions.BILINEAR);
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
	
	private void loadScenes(int nextState){
		loadScenes(nextState, SceneManager.EFFECT_NONE, SceneManager.EFFECT_NONE);
	}

	private void loadScenes(int nextState, int out_Effect, int in_Effect) {

		// 실제로 사용될 scene들을 구성

		this.mEngine.registerUpdateHandler(new FPSLogger());
		final VertexBufferObjectManager vertexBufferObjectManager = getVertexBufferObjectManager();

		presentState = nextState;

		sceneManager.clearTouchAreas();
		// 씬이 돌아올떄 걱정도 해줘야함


		switch (presentState) {

		case STATE_MAIN_LOGO:

			if (mainLogoScene == null) {

				mainLogoScene = sceneManager.CreateLayer();

				MainLogoSprite = new Sprite(0, 0, mainLogoTextureRegion,
						vertexBufferObjectManager) {
					@Override
					public boolean onAreaTouched(
							final TouchEvent pSceneTouchEvent,
							final float pTouchAreaLocalX,
							final float pTouchAreaLocalY) {
						if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
							loadScenes(STATE_MAIN_MENU, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_IN);

						}
						return true;

					}
				};
				MainLogoSprite.setScale(1);
				mainLogoScene.attachChild(MainLogoSprite);


				touchToCountinue = new Text(0, 0, mBasicFont,
						"Touch Screen to Continue", 25,
						vertexBufferObjectManager);
				touchToCountinue.setPosition(
						(CAMERA_WIDTH - touchToCountinue.getWidth()) / 2,
						CAMERA_HEIGHT - 100);
				touchToCountinue.setAlpha(0);
				mainLogoScene.attachChild(touchToCountinue);

			}
			sceneManager.registerTouchArea(MainLogoSprite);
			sceneManager.setScene(mainLogoScene, out_Effect, in_Effect);

			break;

		case STATE_MAIN_MENU:

			if (mainMenuScene == null) {
				
				mainMenuScene = sceneManager.CreateLayer();

				Sprite MainMenuBackSprite = new Sprite(0, 0,
						mBackgroundTextureRegion[1], vertexBufferObjectManager);
				MainMenuBackSprite.setScale(1);
				mainMenuScene.attachChild(MainMenuBackSprite);

				levelSelectButtonSprite = new Sprite(0, 0,
						main_LevelSelectButton_TextureRegion,
						vertexBufferObjectManager) {
					@Override
					public boolean onAreaTouched(
							final TouchEvent pSceneTouchEvent,
							final float pTouchAreaLocalX,
							final float pTouchAreaLocalY) {
						if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
							loadScenes(STATE_LEVEL_SELECT, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_DOWN|SceneManager.EFFECT_FADE_IN);
						}
						return true;

					}

				};
				

				levelSelectButtonSprite
						.setPosition((CAMERA_WIDTH - levelSelectButtonSprite
								.getWidth()) / 2,
								(CAMERA_HEIGHT - levelSelectButtonSprite
										.getHeight()) / 2);
				mainMenuScene.attachChild(levelSelectButtonSprite);



			}
			sceneManager.registerTouchArea(levelSelectButtonSprite);
			sceneManager.setScene(mainMenuScene,out_Effect,in_Effect);

			break;

		case STATE_LEVEL_SELECT:

			if (levelSelectScene == null) {
				levelSelectScene = sceneManager.CreateLayer();
				
				levelSelectBackSprite = new Sprite(0, 0,
						mBackgroundTextureRegion[2], vertexBufferObjectManager) {
					boolean isFocused = false;
					@Override
					public boolean onAreaTouched(
							TouchEvent pSceneTouchEvent,
							float pTouchAreaLocalX,
							float pTouchAreaLocalY) {
						// TODO Auto-generated method stub
						if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
							isFocused = true;
						if(isFocused){
							presentFocus = FOCUS_LEVEL_SCENE_MOVE;
							mScrollDetector2.onTouchEvent(pSceneTouchEvent);
						}
						if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
							isFocused = false;
							presentFocus = FOCUS_NONE;
						}

						return super
								.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
					}
				};
				levelSelectBackSprite.setScale(1);
				levelSelectScene.attachChild(levelSelectBackSprite);

				/////////////////////////////////////////////////////////////////////////////////////////////////////
				levelScrollSprite2 = new ScrollSprite(ScrollSprite.SCROLL_IN_Y,	 CAMERA_WIDTH, CAMERA_HEIGHT){
					@Override
					public void generalEffect() {
						// TODO Auto-generated method stub
						if(getMovedDistance()>0.2f*CAMERA_HEIGHT){
							if(presentState==STATE_LEVEL_SELECT)
								loadScenes(STATE_MAIN_MENU, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_IN);
						}
						super.generalEffect();
					}
				};
				levelSelectScene.attachChild(levelScrollSprite2.getSprite());

				// level을 고르는 버튼들
				
				levelScrollSprite = new ScrollSprite(ScrollSprite.SCROLL_IN_X,CAMERA_WIDTH-200,400);

				for (int i = 0; i < MAX_LEVEL; i++) {
					levelMainSprite[i] = new Sprite(0, 0,
							levelMainTextureRegion[i],
							vertexBufferObjectManager) {
						boolean isFocused = false;
						@Override
						public boolean onAreaTouched(
								TouchEvent pSceneTouchEvent,
								float pTouchAreaLocalX, float pTouchAreaLocalY) {
							

							if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
								isFocused = true;
							if(isFocused){
								presentFocus = FOCUS_LEVEL_SELECT;
								mScrollDetector.onTouchEvent(pSceneTouchEvent);
							}
							if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
								if(!isFocused){
									mScrollDetector2.onTouchEvent(pSceneTouchEvent);
								}
							}
							if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
								isFocused = false;
								presentFocus = FOCUS_NONE;
							}
							return true;
						}
						
					};

					levelMainSprite[i].setScale(0.8f);
					
					float levelMainSprite_X = levelMainSprite[i].getWidthScaled() * 1.25f*i;
					float levelMainSprite_Y = 0;

					levelMainSprite[i].setPosition(levelMainSprite_X, levelMainSprite_Y);
					levelScrollSprite.attachChild(levelMainSprite[i]);

					
					Text levelText = new Text(0, 0, mBasicFont, "level"+(i+1), ("level"+(i+1)).length(), vertexBufferObjectManager);
					
					float levelText_X = levelMainSprite_X + (levelMainSprite[i].getWidth() - levelText.getWidth())/2;
					float levelText_Y = levelMainSprite_Y + levelMainSprite[i].getHeight();

					levelText.setPosition(levelText_X, levelText_Y);
					
					Rectangle rect = new Rectangle(0,0,levelMainSprite[i].getWidth(),levelText.getHeight(), vertexBufferObjectManager);
					
					rect.setColor(1, 1, 1);
					rect.setPosition(levelMainSprite_X, levelText_Y);

					levelScrollSprite.attachChild(rect);
					levelScrollSprite.attachChild(levelText);
					
										
					
				}

				levelScrollSprite.setPosition(
						100,
						(CAMERA_HEIGHT-levelMainSprite[0].getHeight())/2);
				
				// scrolling 구현
				mScrollDetector = new SurfaceScrollDetector(new IScrollDetectorListener() {
					
					@Override
					public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
							float pDistanceX, float pDistanceY) {
						// TODO Auto-generated method stub						
					}
					
					@Override
					public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
							float pDistanceX, float pDistanceY) {
						// TODO Auto-generated method stub
						presentFocus = FOCUS_NONE;
					}
					
					@Override
					public void onScroll(ScrollDetector pScollDetector, int pPointerID,
							float pDistanceX, float pDistanceY) {
						// TODO Auto-generated method stub
						if(presentState == STATE_LEVEL_SELECT){
							pDistanceX/=1.5;
							levelScrollSprite.scroll(pDistanceX);

						}
					}
				});

				levelScrollSprite.setPosition(100, (CAMERA_HEIGHT-levelScrollSprite.getHeight())/2);

				/////////////////////////////////////////////////////////////////////////////////////////////////////
				levelScrollSprite2.attachChild(levelScrollSprite.getSprite());
//				levelSelectScene.attachChild(levelScrollSprite.getSprite());

				scrollBar = new Rectangle(0,0,CAMERA_WIDTH*(levelScrollSprite.getWidth()/levelScrollSprite.getLengthX()),30,vertexBufferObjectManager);
				levelScrollSprite.setScrollBar(scrollBar,0,CAMERA_HEIGHT-30,CAMERA_WIDTH,30,new Color(0,0.5f,0.5f));
	
/////////////////////////////////////////////////////////////////////////////////////////////////////
				levelScrollSprite2.attachChild(scrollBar);
//				levelSelectScene.attachChild(scrollBar);
				
				
				// 이전화면으로
				mScrollDetector2 = new SurfaceScrollDetector(new IScrollDetectorListener() {
					
					@Override
					public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
							float pDistanceX, float pDistanceY) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
							float pDistanceX, float pDistanceY) {
						// TODO Auto-generated method stub
						presentFocus = FOCUS_NONE;
					}
					
					@Override
					public void onScroll(ScrollDetector pScollDetector, int pPointerID,
							float pDistanceX, float pDistanceY) {
						if(presentState == STATE_LEVEL_SELECT){
							levelScrollSprite2.scroll(pDistanceY);
						}
					}
				});

			}
			for(int i=0;i<MAX_LEVEL;i++)	sceneManager.registerTouchArea(levelMainSprite[i]);
			sceneManager.registerTouchArea(levelSelectBackSprite);
			sceneManager.setScene(levelSelectScene, out_Effect, in_Effect);

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
				1280, 720, TextureOptions.DEFAULT);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTextureAtlas, this, "loading.png", 0, 0);
		splashTextureAtlas.load();

		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {

		//sceneManager로 scene들을 관리
		sceneManager = new SceneManager(mEngine);
		sceneManager.setBackground(new Background(0, 0, 0));
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
			if(levelScrollSprite2!=null)
				levelScrollSprite2.generalEffect();

			break;
			
		case STATE_LEVEL_SELECT:
			if(presentFocus != FOCUS_LEVEL_SELECT){
				//속도 가속효과
				levelScrollSprite.generalEffect();
			}
			if(presentFocus != FOCUS_LEVEL_SCENE_MOVE){
				levelScrollSprite2.generalEffect();
			}
			break;

		}
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			switch(presentState){
			case STATE_LEVEL_SELECT:
				if(sceneManager.isAnimating() == false)
					loadScenes(STATE_MAIN_MENU, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_OUT, SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_IN);
				break;

			case STATE_MAIN_MENU:
				return super.onKeyDown(keyCode, event);
			}
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}