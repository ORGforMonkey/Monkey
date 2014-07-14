package AllA.apps;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.IBackground;

public class SceneManager {
	
	// Constants
	
	static final int EFFECT_NONE 			= 0x0;
		// MOVEMENT_EFFECT
	static final int EFFECT_MOVE_UP			= 0x1;
	static final int EFFECT_MOVE_DOWN		= 0x10;
	static final int EFFECT_MOVE_LEFT		= 0x100;
	static final int EFFECT_MOVE_RIGHT		= 0x1000;
		// SIZE_EFFECT
	static final int EFFECT_BECOME_SMALL	= 0x10000;
	static final int EFFECT_BECOME_LARGE	= 0x100000;
		// FADE_EFFECT
	static final int EFFECT_FADE_OUT		= 0x1000000;
	static final int EFFECT_FADE_IN			= 0x10000000;
	
	// Variables
	Engine mEngine;
	Scene primaryScene;
	Scene attachedScene;
	Scene nextAttachedScene;
	TimerHandler timer;
	
	float Width = StartActivity.CAMERA_WIDTH;
	float Height = StartActivity.CAMERA_HEIGHT;
	
	float FPS = 60.0f;
	
	
	
	// Methods
	
	SceneManager(Engine pEngine){
		mEngine = pEngine;
		primaryScene = new Scene();
		attachedScene = null;
		mEngine.setScene(primaryScene);
		
		primaryScene.setTouchAreaBindingOnActionDownEnabled(true);

	}

	SceneManager(Engine pEngine, Scene pScene){
		mEngine = pEngine;
		primaryScene = new Scene();
		attachedScene = pScene;
		primaryScene.attachChild(attachedScene);
		mEngine.setScene(primaryScene);
		
		primaryScene.setTouchAreaBindingOnActionDownEnabled(true);

	}

	
	public void clearTouchAreas(){
		primaryScene.clearTouchAreas();
	}
	
	public Scene getScene(){
		return primaryScene;
	}
	
	public void registerTouchArea(ITouchArea pTouchArea){
		primaryScene.registerTouchArea(pTouchArea);
		primaryScene.setTouchAreaBindingOnActionDownEnabled(true);
	}
	
	
	
	public void setScene(Scene pScene){
		mEngine.setScene(primaryScene);

		primaryScene.detachChild(attachedScene);
		attachedScene = pScene;
		primaryScene.attachChild(attachedScene);
	}
	
	
	
	public void setScene(Scene pScene, final int out_Effect, final int in_Effect){
		mEngine.setScene(primaryScene);

		nextAttachedScene = pScene;
		nextAttachedScene.setAlpha(0);
		primaryScene.attachChild(nextAttachedScene);
						
		//그래픽 효과 적용
		timer = new TimerHandler(1 / FPS, true, new ITimerCallback() {
			
			private int counter = 0;

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				counter ++;
				
				//종료 조건
				if(counter>FPS){
					primaryScene.detachChild(attachedScene);
					attachedScene = nextAttachedScene;
					primaryScene.attachChild(attachedScene);
					mEngine.unregisterUpdateHandler(pTimerHandler);
				}

				
				// OUT_EFFECTS
				if(out_Effect == EFFECT_NONE){
				}
				if((out_Effect&EFFECT_MOVE_UP) != 0){
					attachedScene.setY(attachedScene.getY()-Height/FPS);
				}
				if((out_Effect&EFFECT_MOVE_DOWN) != 0){
					attachedScene.setY(attachedScene.getY()+Height/FPS);
				}
				if((out_Effect&EFFECT_MOVE_LEFT) != 0){
					attachedScene.setX(attachedScene.getX()-Width/FPS);
				}
				if((out_Effect&EFFECT_MOVE_RIGHT) != 0){
					attachedScene.setX(attachedScene.getX()+Width/FPS);
				}
				if((out_Effect&EFFECT_BECOME_SMALL) != 0){
					attachedScene.setScale(1-counter/FPS);
				}
				if((out_Effect&EFFECT_BECOME_LARGE) != 0){
					attachedScene.setScale(1+counter/FPS);
				}
				if((out_Effect&EFFECT_FADE_OUT) != 0){
					// 다른효과보단 좀더 빠르다
					if(attachedScene.getAlpha()>=2/FPS)
						attachedScene.setAlpha(1-2*counter/FPS);
				}
				
				
				// IN_EFFECTS
				if(in_Effect == EFFECT_NONE){
					nextAttachedScene.setAlpha(1);
				}
				if((in_Effect&EFFECT_MOVE_UP) != 0){
					if(nextAttachedScene.getY() == 0)
						nextAttachedScene.setY(Height);
					nextAttachedScene.setY(nextAttachedScene.getY()-Height/FPS);
				}
				if((in_Effect&EFFECT_MOVE_DOWN) != 0){
					if(nextAttachedScene.getY() == 0)
						nextAttachedScene.setY(-Height);
					nextAttachedScene.setY(nextAttachedScene.getY()+Height/FPS);
				}
				if((in_Effect&EFFECT_MOVE_LEFT) != 0){
					if(nextAttachedScene.getX() == 0)
						nextAttachedScene.setX(Width);
					nextAttachedScene.setX(nextAttachedScene.getX()-Width/FPS);
				}
				if((in_Effect&EFFECT_MOVE_RIGHT) != 0){
					if(nextAttachedScene.getX() == 0)
						nextAttachedScene.setX(-Width);
					nextAttachedScene.setX(nextAttachedScene.getX()+Width/FPS);
				}
				if((in_Effect&EFFECT_BECOME_SMALL) != 0){
					nextAttachedScene.setScale(2-counter/FPS);
				}
				if((in_Effect&EFFECT_BECOME_LARGE) != 0){
					nextAttachedScene.setScale(counter/FPS);
				}
				if((in_Effect&EFFECT_FADE_IN) != 0){
					// 다른효과보단 좀더 빠르다
					if(nextAttachedScene.getAlpha()<=1-2/FPS)
						nextAttachedScene.setAlpha(2*counter/FPS);
				}

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

}
