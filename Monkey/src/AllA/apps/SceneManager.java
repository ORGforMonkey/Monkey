package AllA.apps;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.IBackground;

import android.util.Log;

public class SceneManager {
	
	// Constants

	static final int EFFECT_NONE 			= Integer.valueOf("0", 2);
		// MOVEMENT_EFFECT
	static final int EFFECT_MOVE_UP			= Integer.valueOf("1", 2);
	static final int EFFECT_MOVE_DOWN		= Integer.valueOf("10", 2);
	static final int EFFECT_MOVE_LEFT		= Integer.valueOf("100", 2);
	static final int EFFECT_MOVE_RIGHT		= Integer.valueOf("1000", 2);
		// SIZE_EFFECT
	static final int EFFECT_BECOME_SMALL	= Integer.valueOf("10000", 2);
	static final int EFFECT_BECOME_LARGE	= Integer.valueOf("100000", 2);
		// FADE_EFFECT
	static final int EFFECT_FADE_OUT		= Integer.valueOf("1000000", 2);
	static final int EFFECT_FADE_IN			= Integer.valueOf("10000000", 2);
	static final int EFFECT_FADE_UP			= Integer.valueOf("100000000", 2);
	static final int EFFECT_FADE_DOWN		= Integer.valueOf("1000000000", 2);
	static final int EFFECT_FADE_LEFT		= Integer.valueOf("10000000000", 2);
	static final int EFFECT_FADE_RIGHT		= Integer.valueOf("100000000000", 2);
	
	// Entities
//	private ArrayList<Entity> mLayers = new ArrayList<Entity>();
	
	// Variables
	Engine mEngine;
	Scene primaryScene;
	Entity attachedLayer;
	Entity nextAttachedLayer;
	TimerHandler timer;
	
	float Width = StartActivity.CAMERA_WIDTH;
	float Height = StartActivity.CAMERA_HEIGHT;
	
	float FPS = 60.0f;
	
	
	boolean isAnimating = false;
	
	
	
	// ===========================================================
	// Constructors
	// ===========================================================	
	SceneManager(Engine pEngine){
		mEngine = pEngine;
		init(null);
	}

	SceneManager(Engine pEngine, Entity pLayer){
		mEngine = pEngine;
		init(pLayer);
	}
	
	void init(Entity pLayer){
		primaryScene = new Scene();
		attachedLayer = pLayer;
		if(pLayer!=null)	primaryScene.attachChild(pLayer);
		primaryScene.setTouchAreaBindingOnActionDownEnabled(true);
				
		mEngine.setScene(primaryScene);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Scene getScene(){
		return primaryScene;
	}

	public void setAlphaAll(float alpha, IEntity pIEntity){
		if(pIEntity==null)
			return;
		pIEntity.setAlpha(alpha);
		if(pIEntity.getChildCount()==0)
			return;
		for(int i=0;i<pIEntity.getChildCount();i++){
			setAlphaAll(alpha, pIEntity.getChildByIndex(i));
		}
	}
	
	public void setScene(Entity pLayer){
		setScene(pLayer,EFFECT_NONE,EFFECT_NONE);
	}
	
	public void setScene(Entity pLayer, final int out_Effect, final int in_Effect){
		mEngine.setScene(primaryScene);
		
		if(attachedLayer==null){
			primaryScene.detachChildren();
			attachedLayer = pLayer;
			primaryScene.attachChild(pLayer);
			return;
		}
		

		nextAttachedLayer = pLayer;
		nextAttachedLayer.setVisible(false);
		primaryScene.attachChild(nextAttachedLayer);

		//그래픽 효과 적용
		timer = new TimerHandler(1 / FPS, true, new ITimerCallback() {
			
			private float time = 0;
			private float sw = 0;
			/*지정해야 하는 부분*/
			private float outtime = 0.3f;
			private float intime = 0.3f;
			private float intime_offset = 0;

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				
				float outrate = time/outtime;
				float inrate = (time-intime_offset)/intime;
				
				isAnimating = true;
				
				// OUT_EFFECTS
				if(0<=outrate && outrate<=1){
					if(out_Effect == EFFECT_NONE){
					}
					if((out_Effect&EFFECT_MOVE_UP) != 0){
						attachedLayer.setY(-Height*outrate);
					}
					if((out_Effect&EFFECT_MOVE_DOWN) != 0){
						attachedLayer.setY(Height*outrate);
					}
					if((out_Effect&EFFECT_MOVE_LEFT) != 0){
						attachedLayer.setX(-Width*outrate);
					}
					if((out_Effect&EFFECT_MOVE_RIGHT) != 0){
						attachedLayer.setX(Width*outrate);
					}
					if((out_Effect&EFFECT_BECOME_SMALL) != 0){
						attachedLayer.setScale(1-outrate);
					}
					if((out_Effect&EFFECT_BECOME_LARGE) != 0){
						attachedLayer.setScale(1+outrate);
					}
					if((out_Effect&EFFECT_FADE_OUT) != 0){

						setAlphaAll(1-outrate,attachedLayer);
					}
					
				}
				
				// IN_EFFECTS
				if(0<=inrate && inrate<=1){
					nextAttachedLayer.setVisible(true);
					if(in_Effect == EFFECT_NONE){
						nextAttachedLayer.setAlpha(1);
					}
					if((in_Effect&EFFECT_MOVE_UP) != 0){
						if(sw == 0)
							nextAttachedLayer.setY(Height);
						nextAttachedLayer.setY(Height*(1-outrate));
					}
					if((in_Effect&EFFECT_MOVE_DOWN) != 0){
						if(sw == 0)
							nextAttachedLayer.setY(-Height);
						nextAttachedLayer.setY(-Height*(1-outrate));
					}
					if((in_Effect&EFFECT_MOVE_LEFT) != 0){
						if(sw == 0)
							nextAttachedLayer.setX(Width);
						nextAttachedLayer.setX(Width*(1-outrate));
					}
					if((in_Effect&EFFECT_MOVE_RIGHT) != 0){
						if(sw == 0)
							nextAttachedLayer.setX(-Width);
						nextAttachedLayer.setX(-Width*(1-outrate));
					}
					if((in_Effect&EFFECT_BECOME_SMALL) != 0){
						nextAttachedLayer.setScale(2-inrate);
					}
					if((in_Effect&EFFECT_BECOME_LARGE) != 0){
						nextAttachedLayer.setScale(inrate);
					}
					if((in_Effect&EFFECT_FADE_IN) != 0){
						if(sw == 0)
							setAlphaAll(0,nextAttachedLayer);

						if(nextAttachedLayer.getAlpha()<=1-2/FPS)
							setAlphaAll(inrate,nextAttachedLayer);
					}else{
						nextAttachedLayer.setAlpha(1);
					}
				}
								
				outrate = (out_Effect==EFFECT_NONE)?1:outrate;
				inrate = (in_Effect==EFFECT_NONE)?1:inrate;
				
				//종료 조건
				if((inrate>outrate?inrate:outrate) >= 1){
					primaryScene.detachChild(attachedLayer);
					attachedLayer = nextAttachedLayer;
					attachedLayer.setPosition(0, 0);
					setAlphaAll(1,attachedLayer);

					isAnimating = false;
					
					mEngine.unregisterUpdateHandler(pTimerHandler);					
				}
				
				//유지 관리
				time += 1/FPS;
				sw = 1;

			}
		});
		
		mEngine.registerUpdateHandler(timer);
	}
	
	
	public void setBackground(IBackground pBackground){
		primaryScene.setBackground(pBackground);
	}
	
	public void setBackgroundEnabled(boolean pEnabled){
		primaryScene.setBackgroundEnabled(pEnabled);
	}
	
	
	// ===========================================================
	// Methods
	// ===========================================================
	Layer CreateLayer(){
		Layer newLayer = new Layer();
		return newLayer;
	}

	public void clearTouchAreas(){
		primaryScene.clearTouchAreas();
	}
		
	public void registerTouchArea(ITouchArea pTouchArea){
		primaryScene.registerTouchArea(pTouchArea);
		primaryScene.setTouchAreaBindingOnActionDownEnabled(true);
	}
	
	public boolean isAnimating(){
		return isAnimating;
	}

}
