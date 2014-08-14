package AllA.apps;

import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.IBackground;
<<<<<<< HEAD
=======

import android.util.Log;
>>>>>>> master

public class SceneManager {
	
	// Constants
	
	static final int EFFECT_NONE 			= Integer.valueOf("1", 2);
		// MOVEMENT_EFFECT
	static final int EFFECT_MOVE_UP			= Integer.valueOf("10", 2);
	static final int EFFECT_MOVE_DOWN		= Integer.valueOf("100", 2);
	static final int EFFECT_MOVE_LEFT		= Integer.valueOf("1000", 2);
	static final int EFFECT_MOVE_RIGHT		= Integer.valueOf("10000", 2);
		// SIZE_EFFECT
	static final int EFFECT_BECOME_SMALL	= Integer.valueOf("100000", 2);
	static final int EFFECT_BECOME_LARGE	= Integer.valueOf("1000000", 2);
		// FADE_EFFECT
	static final int EFFECT_FADE_OUT		= Integer.valueOf("10000000", 2);
	static final int EFFECT_FADE_IN			= Integer.valueOf("100000000", 2);
	static final int EFFECT_FADE_UP			= Integer.valueOf("1000000000", 2);
	static final int EFFECT_FADE_DOWN		= Integer.valueOf("10000000000", 2);
	static final int EFFECT_FADE_LEFT		= Integer.valueOf("100000000000", 2);
	static final int EFFECT_FADE_RIGHT		= Integer.valueOf("1000000000000", 2);
	
<<<<<<< HEAD
	// Entities
//	private ArrayList<Entity> mLayers = new ArrayList<Entity>();
=======
>>>>>>> master
	
	// Variables
	static Engine mEngine;
	static Scene primaryScene;
	static Entity attachedLayer;
	static Entity nextAttachedLayer;
	static TimerHandler timer;
	
	static float Width = StartActivity.CAMERA_WIDTH;
	static float Height = StartActivity.CAMERA_HEIGHT;
	
	static float FPS = 60.0f;
	
	static SimpleBaseActivity presentActivity;
	static HashMap<String, SimpleBaseActivity>	registeredActivities	= new HashMap<String, SimpleBaseActivity>();

	static boolean isAnimating = false;
	
	
	
	// ===========================================================
	// Constructors
	// ===========================================================	
	
	static void init(){
		primaryScene = new Scene();
		primaryScene.setTouchAreaBindingOnActionDownEnabled(true);
				
		mEngine.setScene(primaryScene);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	static public void registerEngine(Engine pEngine){
		mEngine = pEngine;
	}
	
	static public void registerActivity(String keys, SimpleBaseActivity pActivity){
		registeredActivities.put(keys, pActivity);
	}
	
	static public SimpleBaseActivity getActivity(String keys){
		if(!registeredActivities.containsKey(keys))
			return null;
		
		return registeredActivities.get(keys);
	}
	
	static public Scene getScene(){
		return primaryScene;
	}

	static public void setAlphaAll(float alpha, IEntity pIEntity){
		if(pIEntity==null)
			return;
		pIEntity.setAlpha(alpha);
		if(pIEntity.getChildCount()==0)
			return;
		for(int i=0;i<pIEntity.getChildCount();i++){
			setAlphaAll(alpha, pIEntity.getChildByIndex(i));
		}
	}
	
	static public void goBack(){
		if(presentActivity == null)	return;
		SimpleBaseActivity backActivity = presentActivity.getBackActivity();
		int back_out_Effect = presentActivity.backOutEffect;
		int back_in_Effect = presentActivity.backInEffect;

		if(backActivity == null)	return;
		setActivity(backActivity, back_out_Effect, back_in_Effect);
	}

	static public void goBack(final int out_Effect, final int in_Effect){
		if(presentActivity == null)	return;
		SimpleBaseActivity backActivity = presentActivity.getBackActivity();

		if(backActivity == null)	return;
		setActivity(backActivity, out_Effect, in_Effect);
	}
	
	static public void setActivity(SimpleBaseActivity pActivity){

		setActivity(pActivity, EFFECT_NONE, EFFECT_NONE);
	}	

	static public void setActivity(SimpleBaseActivity pActivity, final int out_Effect, final int in_Effect){
<<<<<<< HEAD

		clearTouchAreas();
		if(presentActivity!=null)	presentActivity.deleteSprites();
		presentActivity = pActivity;
=======
		clearTouchAreas();
		if(presentActivity!=null){
			presentActivity.onPause();
			presentActivity.deleteSprites();
		}
		presentActivity = pActivity;
		presentActivity.onResume();
>>>>>>> master
		presentActivity.loadScene();
		presentActivity.registerTouchAreatoSceneManager();
		
		setScene(pActivity.mainLayer, out_Effect, in_Effect);
	}	
	
	static public void setScene(Entity pLayer){
		setScene(pLayer,EFFECT_NONE,EFFECT_NONE);
<<<<<<< HEAD
	}
	
	static public void setScene(Entity pLayer, final int out_Effect, final int in_Effect){
		mEngine.setScene(primaryScene);

		if(attachedLayer==null){
			primaryScene.detachChildren();
			attachedLayer = pLayer;
			primaryScene.attachChild(pLayer);
			return;
		}

		nextAttachedLayer = pLayer;
		setAlphaAll(1, nextAttachedLayer);
		nextAttachedLayer.setPosition(0, 0);
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
				if(0<=outrate && outrate<1+(1/FPS)/outtime){
					if(out_Effect == EFFECT_NONE){
						outrate = 1;
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
				if(0<=inrate && inrate<1+(1/FPS)/intime){
					nextAttachedLayer.setVisible(true);
					if(in_Effect == EFFECT_NONE){
						inrate = 1;
					}
					if((in_Effect&EFFECT_MOVE_UP) != 0){
						if(sw == 0)
							nextAttachedLayer.setY(Height);
						nextAttachedLayer.setY(Height*(1-inrate));
					}
					if((in_Effect&EFFECT_MOVE_DOWN) != 0){
						if(sw == 0)
							nextAttachedLayer.setY(-Height);
						nextAttachedLayer.setY(-Height*(1-inrate));
					}
					if((in_Effect&EFFECT_MOVE_LEFT) != 0){
						if(sw == 0)
							nextAttachedLayer.setX(Width);
						nextAttachedLayer.setX(Width*(1-inrate));
					}
					if((in_Effect&EFFECT_MOVE_RIGHT) != 0){
						if(sw == 0)
							nextAttachedLayer.setX(-Width);
						nextAttachedLayer.setX(-Width*(1-inrate));
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
					}
				}

				//종료 조건
				if((inrate<outrate?inrate:outrate) >= 1){
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
=======
>>>>>>> master
	}
	
	static public void setScene(Entity pLayer, final int out_Effect, final int in_Effect){

		
		mEngine.setScene(primaryScene);

		if(attachedLayer==null){
			primaryScene.detachChildren();
			attachedLayer = pLayer;
			primaryScene.attachChild(pLayer);
			return;
		}

		nextAttachedLayer = pLayer;
		setAlphaAll(1, nextAttachedLayer);
		nextAttachedLayer.setPosition(0, 0);
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
				if(0<=outrate && outrate<1+(1/FPS)/outtime){
					if(out_Effect == EFFECT_NONE){
						outrate = 1;
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
				if(0<=inrate && inrate<1+(1/FPS)/intime){
					nextAttachedLayer.setVisible(true);
					if(in_Effect == EFFECT_NONE){
						inrate = 1;
					}
					if((in_Effect&EFFECT_MOVE_UP) != 0){
						if(sw == 0)
							nextAttachedLayer.setY(Height);
						nextAttachedLayer.setY(Height*(1-inrate));
					}
					if((in_Effect&EFFECT_MOVE_DOWN) != 0){
						if(sw == 0)
							nextAttachedLayer.setY(-Height);
						nextAttachedLayer.setY(-Height*(1-inrate));
					}
					if((in_Effect&EFFECT_MOVE_LEFT) != 0){
						if(sw == 0)
							nextAttachedLayer.setX(Width);
						nextAttachedLayer.setX(Width*(1-inrate));
					}
					if((in_Effect&EFFECT_MOVE_RIGHT) != 0){
						if(sw == 0)
							nextAttachedLayer.setX(-Width);
						nextAttachedLayer.setX(-Width*(1-inrate));
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
					}
				}

				//종료 조건
				if((inrate<outrate?inrate:outrate) >= 1){
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
	
	
	static public void setBackground(IBackground pBackground){
		primaryScene.setBackground(pBackground);
	}
	
<<<<<<< HEAD
	static public void setBackground(IBackground pBackground){
		primaryScene.setBackground(pBackground);
	}
	
	static public void setBackgroundEnabled(boolean pEnabled){
		primaryScene.setBackgroundEnabled(pEnabled);
	}
	
=======
	static public void setBackgroundEnabled(boolean pEnabled){
		primaryScene.setBackgroundEnabled(pEnabled);
	}
	
>>>>>>> master
	
	// ===========================================================
	// Methods
	// ===========================================================

	static public void clearTouchAreas(){
		primaryScene.clearTouchAreas();
	}
		
	static public void registerTouchArea(ITouchArea pTouchArea){
		primaryScene.registerTouchArea(pTouchArea);
		primaryScene.setTouchAreaBindingOnActionDownEnabled(true);
	}
	
	static public boolean isAnimating(){
		return isAnimating;
	}
	
	static public SimpleBaseActivity getPresentActivity(){
		return presentActivity;
	}
	
	static public boolean isPresentActivity(SimpleBaseActivity pActivity){
		if(pActivity.equals(presentActivity))
			return true;
		
		return false;
	}
	
	static public boolean isPresentActivity(String keys){
		if(!registeredActivities.containsKey(keys))
			return false;
		
		if(registeredActivities.get(keys).equals(presentActivity))
			return true;
		
		return false;
	}
	

}
