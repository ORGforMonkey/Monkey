package AllA.apps;


import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;


// 다른 기본 Activity들의 기본 뼈대가 되는 Activity이다. extends해서 이용하면 된다.
public class SimpleBaseActivity{
	/* variable */
	protected Layer mainLayer;
	
	protected VertexBufferObjectManager vertexBufferObjectManager;
	
	protected int Width;
	protected int Height;
	
	protected boolean isLoaded = false;
	protected boolean isAlwaysUpdated = true;
	
	protected SimpleBaseActivity thisActivity;
	protected SimpleBaseActivity backActivity;
	protected int backInEffect;
	protected int backOutEffect;
	
	/* Constructor */
	
	SimpleBaseActivity() {
		mainLayer = new Layer();
		thisActivity = this;
		backActivity = null;
	}

	
	SimpleBaseActivity(int width, int height,VertexBufferObjectManager pVertexBufferObjectManager){
		mainLayer = new Layer();		
		vertexBufferObjectManager = pVertexBufferObjectManager;	
		thisActivity = this;
		backActivity = null;
		Width = width;
		Height = height;
	}

	
	/* methods */
	
	public void loadResources(){
		
	}
	
	public void loadScene(){
		isLoaded = true;
	}
	public void registerTouchAreatoSceneManager(){
		
	}
	
	public void updateActivity(){
		
	}
	
	public void deleteSprites(){
	}
	
	
	
	public void setSize(int width, int height){
		Width = width;
		Height = height;
	}
	
	
	public void setBackActivity(SimpleBaseActivity pActivity, int out_Effect, int in_Effect){
		backActivity = pActivity;
		backOutEffect = out_Effect;
		backInEffect = in_Effect;
	}
	
	public void setBackActivity(SimpleBaseActivity pActivity){
		backActivity = pActivity;
		backOutEffect = SceneManager.EFFECT_NONE;
		backInEffect = SceneManager.EFFECT_NONE;
	}

	public void setBackEffect(int out_Effect, int in_Effect){
		backOutEffect = out_Effect;
		backInEffect = in_Effect;		
	}
	
	public void goBack(){
		if(backActivity != null){
			SceneManager.setActivity(backActivity, backOutEffect, backInEffect);
		}
	}
	
	public void goBack(int out_Effect, int in_Effect){
		if(backActivity != null){
			SceneManager.setActivity(backActivity, out_Effect, in_Effect);
		}
	}
	
	
	public SimpleBaseActivity getBackActivity(){
		return backActivity;
	}

	
	public void setVertexBufferObjectManager (VertexBufferObjectManager pVertexBufferObjectManager)
	{
		vertexBufferObjectManager = pVertexBufferObjectManager;
	}
	
	public void setAlwaysUpdated(boolean pbool){
		isAlwaysUpdated = pbool;
	}
	
	public boolean isAlwaysUpdated(){
		return isAlwaysUpdated;
	}
	
	public boolean isLoaded(){
		return isLoaded;
	}
<<<<<<< HEAD
=======
	
	public boolean isFocused(){
		if(this.equals(SceneManager.presentActivity))
			return true;
		
		return false;
	}
	
	public void onPause(){
	}
	
	public void onResume(){
	}
	
	
>>>>>>> master
	/* animations */
	
}
