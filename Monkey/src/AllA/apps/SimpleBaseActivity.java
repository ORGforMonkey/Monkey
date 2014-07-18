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
	
	protected Context context;
	
	boolean isLoaded = false;
	boolean isAlwaysUpdated = true;
	
	/* Constructor */
	
	SimpleBaseActivity() {
		mainLayer = new Layer();
	}
	
	SimpleBaseActivity(Context mContext) {
		mainLayer = new Layer();
		context = mContext;
	}
	
	SimpleBaseActivity(VertexBufferObjectManager pVertexBufferObjectManager){
		mainLayer = new Layer();		
		vertexBufferObjectManager = pVertexBufferObjectManager;	
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
	
	
	
	public void setSize(int width, int height){
		Width = width;
		Height = height;
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
	/* animations */
	
}
