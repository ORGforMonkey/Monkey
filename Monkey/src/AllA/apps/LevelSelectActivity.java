package AllA.apps;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.*;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

import android.graphics.Typeface;
import android.util.Log;


public class LevelSelectActivity extends SimpleBaseActivity{
	
	/* constants */
	private static final int FOCUS_NONE = 0;
	private static final int FOCUS_LEVEL_SELECT = 1;
	private static final int FOCUS_LEVEL_SCENE_MOVE = 2;

	private int MAX_LEVEL = StartActivity.MAX_LEVEL;
	
	public static final float CLICK_DISTANCE = (float) 100.0;  /// 거리 최적화 필요

	
	/* variable */
	
	Sprite levelMainSprite[] = new Sprite[MAX_LEVEL+1];
	private Sprite levelSelectBackSprite;
	private Rectangle scrollBar;
	private ScrollLayer levelScrollLayer;
	private ScrollLayer levelScrollLayer2;
	SurfaceScrollDetector mScrollDetector;
	SurfaceScrollDetector mScrollDetector2;
	
	ClickDetector mClickDetector;
	private int selectedLevel;
	
	private int presentFocus = FOCUS_NONE;
	
	
	/* Constructor */
	LevelSelectActivity(int width, int height,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(width,height,pVertexBufferObjectManager);
	}
	
	/* methods */
	
	@Override
	public void loadResources()
	{	
		ResourceManager.loadImage("back3", "back3.png", 1280, 720);
		ResourceManager.loadFont("font1", 256, 256, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 32);
		for(int i=1;i<=MAX_LEVEL;i++)
			ResourceManager.loadImage("level"+i, "level"+i+"/main_level.png", 400, 400);

		super.loadResources();
	}
	
	@Override
	public void loadScene()
	{
//		if(isLoaded())
//			return;
		
		// background
		levelSelectBackSprite = new Sprite(0, 0, ResourceManager.getRegion("back3"), vertexBufferObjectManager) {
			boolean isFocused = false;
			@Override
			public boolean onAreaTouched(
					TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

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

				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		mainLayer.attachChild(levelSelectBackSprite);
		
		
		// components


		levelScrollLayer2 = new ScrollLayer(ScrollLayer.SCROLL_IN_Y, Width, Height){
			@Override
			public void generalEffect() {

				if(getMovedDistance()>0.2f*Height){
					if(SceneManager.isPresentActivity("levelSelectActivity")){

						SceneManager.goBack();
					}
				}
				super.generalEffect();
			}
		};
		mainLayer.attachChild(levelScrollLayer2.getLayer());

		// level을 고르는 버튼들
		
		levelScrollLayer = new ScrollLayer(ScrollLayer.SCROLL_IN_X,Width-200,400);

		for (int i = 1; i <= MAX_LEVEL; i++) {
			final int level = i;
			levelMainSprite[i] = new Sprite(0, 0, ResourceManager.getRegion("level"+i), vertexBufferObjectManager) {
				boolean isFocused = false;
//				boolean chkMove = false;   /// ACTION_MOVE 가 발생하고 난 뒤에는 클릭이 발생하지 않도록 핸들하는 변수
				float startPosX,startPosY;
				@Override
				public boolean onAreaTouched(
						TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
					{
						isFocused = true;
//						chkMove = false;
						startPosX = pSceneTouchEvent.getX();
						startPosY = pSceneTouchEvent.getY();
					}
					if(isFocused){
						presentFocus = FOCUS_LEVEL_SELECT;
						mScrollDetector.onTouchEvent(pSceneTouchEvent);
					}
					
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
						if(!isFocused){
							mScrollDetector2.onTouchEvent(pSceneTouchEvent);
						}
//						chkMove = true;
					}
//					if(isFocused && !chkMove)   //누르고 움직이지 않았을 경우만 클릭으로 인정
					if(isFocused && (pSceneTouchEvent.getX()-startPosX)*(pSceneTouchEvent.getX()-startPosX)+(pSceneTouchEvent.getY()-startPosY)*(pSceneTouchEvent.getY()-startPosY) < CLICK_DISTANCE)
					{
						selectedLevel=level;
						mClickDetector.onManagedTouchEvent(pSceneTouchEvent);
					}
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
						isFocused = false;
						presentFocus = FOCUS_NONE;
			
					}
					
					return true;
				}
				
			};

			levelMainSprite[i].setScale(0.8f);
			
			float levelMainSprite_X = levelMainSprite[i].getWidthScaled() * 1.25f*(i-1);
			float levelMainSprite_Y = 0;

			levelMainSprite[i].setPosition(levelMainSprite_X, levelMainSprite_Y);
			levelScrollLayer.attachChild(levelMainSprite[i]);

			
			
			Text levelText = new Text(0, 0, ResourceManager.getFont("font1"), "level"+i, ("level"+i).length(), vertexBufferObjectManager);
			
			float levelText_X = levelMainSprite_X + (levelMainSprite[i].getWidth() - levelText.getWidth())/2;
			float levelText_Y = levelMainSprite_Y + levelMainSprite[i].getHeight();

			levelText.setPosition(levelText_X, levelText_Y);
			
			Rectangle rect = new Rectangle(0,0,levelMainSprite[i].getWidth(),levelText.getHeight(), vertexBufferObjectManager);
			
			rect.setColor(1, 1, 1);
			rect.setPosition(levelMainSprite_X, levelText_Y);

			levelScrollLayer.attachChild(rect);
			levelScrollLayer.attachChild(levelText);
			
		}

		levelScrollLayer.setPosition(100, (Height-levelMainSprite[1].getHeight())/2);
		
		// scrolling 구현
		mScrollDetector = new SurfaceScrollDetector(new IScrollDetectorListener() {
			
			@Override
			public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
					float pDistanceX, float pDistanceY) {
			}
			
			@Override
			public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
					float pDistanceX, float pDistanceY) {
				presentFocus = FOCUS_NONE;
			}
			
			@Override
			public void onScroll(ScrollDetector pScollDetector, int pPointerID,
					float pDistanceX, float pDistanceY) {
				
				if(SceneManager.isPresentActivity("levelSelectActivity")){
					levelScrollLayer.scroll(pDistanceX*StartActivity.CAMERA_WIDTH/StartActivity.PHONE_WIDTH);
				}
			}
		});

		levelScrollLayer.setPosition(100, (Height-levelScrollLayer.getHeight())/2);

		levelScrollLayer2.attachChild(levelScrollLayer.getLayer());

		scrollBar = new Rectangle(0,0,Width*(levelScrollLayer.getWidth()/levelScrollLayer.getLengthX()),30,vertexBufferObjectManager);
		levelScrollLayer.setScrollBar(scrollBar,0,Height-30,Width,30,new Color(0,0.5f,0.5f));

		levelScrollLayer2.attachChild(scrollBar);
		
		
		// 이전화면으로
		mScrollDetector2 = new SurfaceScrollDetector(new IScrollDetectorListener() {
			
			@Override
			public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
					float pDistanceX, float pDistanceY) {
				
			}
			
			@Override
			public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
					float pDistanceX, float pDistanceY) {
				presentFocus = FOCUS_NONE;
			}
			
			@Override
			public void onScroll(ScrollDetector pScollDetector, int pPointerID,
					float pDistanceX, float pDistanceY) {
				if(SceneManager.isPresentActivity("levelSelectActivity")){
					levelScrollLayer2.scroll(pDistanceY*StartActivity.CAMERA_HEIGHT/StartActivity.PHONE_HEIGHT);
				}
			}
		});

		/***   ClickDetector 부분   **/
		
		mClickDetector = new ClickDetector(new IClickDetectorListener(){
				
			
			@Override
			public void onClick(ClickDetector pClickDetector, int pPointerID,
					float pSceneX, float pSceneY) {
				// TODO Auto-generated method stub
				SimpleBaseActivity nextActivity = SceneManager.getActivity("levelDetailActivity");
				
				int back_out_Effect = SceneManager.EFFECT_MOVE_UP;
				int back_in_Effect  = SceneManager.EFFECT_MOVE_UP;
				nextActivity.setBackActivity(thisActivity,back_out_Effect, back_in_Effect);
				((LevelDetailActivity)nextActivity).setLevel(selectedLevel); //  누른 레벨을 전달
				int out_Effect = SceneManager.EFFECT_MOVE_DOWN;
				int in_Effect  = SceneManager.EFFECT_MOVE_DOWN;
				
				SceneManager.setActivity(nextActivity, out_Effect, in_Effect);
			}


		
		});
		
		mClickDetector.setTriggerClickMaximumMilliseconds(800);   // 너무 빨리 클릭될까봐 누르고 일정 시간 딜레이 후 클릭 되게
		
		
		
		super.loadScene();
	}
	
	@Override
	public void deleteSprites() {
		/*
		for(int i=1;i<=MAX_LEVEL;i++)
			levelMainSprite[i].dispose();
		levelSelectBackSprite.dispose();
		scrollBar.dispose();
*/		super.deleteSprites();
	}
	
	@Override
	public void registerTouchAreatoSceneManager(){

		SceneManager.registerTouchArea(levelSelectBackSprite);
		for(int i=1;i<=MAX_LEVEL;i++)	SceneManager.registerTouchArea(levelMainSprite[i]);
		
		super.registerTouchAreatoSceneManager();
	}
	
	
	@Override
	public void updateActivity(){
		if(presentFocus != FOCUS_LEVEL_SELECT){
			//속도 가속효과
			if(levelScrollLayer != null)
				levelScrollLayer.generalEffect();
		}
		if(presentFocus != FOCUS_LEVEL_SCENE_MOVE){
			if(levelScrollLayer2 != null)
			levelScrollLayer2.generalEffect();
		}
	}
	
	/**/
	
}
