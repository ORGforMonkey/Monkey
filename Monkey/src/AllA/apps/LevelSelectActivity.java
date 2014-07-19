package AllA.apps;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import android.graphics.Typeface;

public class LevelSelectActivity extends SimpleBaseActivity{
	
	/* constants */
	private static final int FOCUS_NONE = 0;
	private static final int FOCUS_LEVEL_SELECT = 1;
	private static final int FOCUS_LEVEL_SCENE_MOVE = 2;

	private static final int MAX_LEVEL = 6;

	
	/* variable */
	
	Sprite levelMainSprite[] = new Sprite[MAX_LEVEL+1];
	private Sprite levelSelectBackSprite;
	private Rectangle scrollBar;
	private ScrollLayer levelScrollLayer;
	private ScrollLayer levelScrollLayer2;
	SurfaceScrollDetector mScrollDetector;
	SurfaceScrollDetector mScrollDetector2;

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
		if(isLoaded())
			return;
		
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

/*						int back_out_Effect = SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_OUT;
						int back_in_Effect  = SceneManager.EFFECT_MOVE_UP|SceneManager.EFFECT_FADE_IN;
						SceneManager.goBack(back_out_Effect,back_in_Effect);
*/
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
			levelMainSprite[i] = new Sprite(0, 0, ResourceManager.getRegion("level"+i), vertexBufferObjectManager) {
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


		super.loadScene();
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
