package AllA.apps;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;

public class SceneManager {
	
	// Constants
		// OUT_EFFECT
	static final int EFFECT_NONE 		= 0x0;
	static final int EFFECT_FADE_IN		= 0x1;
	static final int EFFECT_FADE_OUT	= 0x1;
	
		// IN_EFFECT
	
	// Variables
	Engine mEngine;
	Scene primaryScene;
	TimerHandler timer;
	
	SceneManager(Engine pEngine){
		mEngine = pEngine;
	}

	SceneManager(Engine pEngine, Scene pScene){
		primaryScene = pScene;
		mEngine = pEngine;
		mEngine.setScene(pScene);
	}
	
	
	public void setScene(Scene pScene){
		primaryScene = pScene;
		mEngine.setScene(pScene);
	}
	
	
	public void SceneChange(Scene pScene, int out_Effect, int in_Effect){
	}

}
