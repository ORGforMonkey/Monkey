package AllA.apps;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Typeface;

public class LevelDetailActivity extends SimpleBaseActivity{
	
	/* variable */
	private int MAX_LEVEL = StartActivity.MAX_LEVEL;
	private Layer levelLayer[] = new Layer[MAX_LEVEL+1];
	
	private Sprite backgroundSprite;
	private Sprite levelDetailSprite[] = new Sprite[100];
	
	private int level = 1;
	Text Title;

	/* Constructor */
	LevelDetailActivity(int width, int height,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(width,height,pVertexBufferObjectManager);
	}
	
	/* methods */
	
	@Override
	public void loadResources()
	{	
		ResourceManager.loadImage("back3", "back3.png", 1280, 720);
		for(int i=1;i<=MAX_LEVEL;i++)	ResourceManager.loadImage("leveldetailbutton"+i, "level"+i+"/leveldetailbutton.png", 200, 200);
		ResourceManager.loadFont("font2", 256, 256, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 100);

		super.loadResources();
	}
	
	@Override
	public void loadScene()
	{
		if(isLoaded())
			return;
		
		// background
		backgroundSprite = new Sprite(0, 0, ResourceManager.getRegion("back3"), vertexBufferObjectManager);
		mainLayer.attachChild(backgroundSprite);
		
		// components
		
		Title = new Text(0,0,ResourceManager.getFont("font2"),"level"+level,vertexBufferObjectManager);
		Title.setPosition((Width-Title.getWidth())/2, 25);
		mainLayer.attachChild(Title);
		
		
		for(int i=1;i<=MAX_LEVEL;i++){
			levelLayer[i] = new Layer();
			for(int j=0;j<13;j++){
				levelDetailSprite[j] = new Sprite(0,0,ResourceManager.getRegion("leveldetailbutton"+level),vertexBufferObjectManager){
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						// TODO 게임진입하는 터치부분
						return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
					}
				};
				
				levelDetailSprite[j].setScale(0.95f);
				float X = (j%5)*((Width-levelDetailSprite[j].getWidth())/4);
				float Y = (j/5)*(levelDetailSprite[j].getHeight()+60);
				levelDetailSprite[j].setPosition(X, Y);
				levelLayer[i].attachChild(levelDetailSprite[j]);
				
				Text levelText = new Text(0,0,ResourceManager.getFont("font1"),""+(j+1),vertexBufferObjectManager);
				float TX = X+(levelDetailSprite[j].getWidthScaled()-levelText.getWidth())/2;
				float TY = Y+(levelDetailSprite[j].getHeightScaled()-levelText.getHeight())/2;
				levelText.setPosition(TX, TY);
				levelLayer[i].attachChild(levelText);
			}


			resetPosition(i);
			
			
			mainLayer.attachChild(levelLayer[i]);
		}

		super.loadScene();
	}
	
	
	@Override
	public void deleteSprites() {

/*		backgroundSprite.dispose();
		for(int i=0;i<13;i++)
			levelDetailSprite[i].dispose();

*/		super.deleteSprites();
	}
	
	@Override
	public void registerTouchAreatoSceneManager(){
		
		super.registerTouchAreatoSceneManager();
	}
	
	
	@Override
	public void updateActivity(){
		
		if(Title != null)
			Title.setText("level"+level);
		
		for(int i=1;i<=MAX_LEVEL;i++){
			if(levelLayer[i] == null)	break;

			resetPosition(i);
		}

	}
	
	
	/* new methods */
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int pLevel){
		level = pLevel;
	}
	
	private void resetPosition(int i){
		if(i==level)	levelLayer[i].setScale(0.7f);
		else{
						levelLayer[i].setScale(0.6f);
						SceneManager.setAlphaAll(0.8f, levelLayer[i]);
		}
		
		levelLayer[i].setVisible(true);

		if(i==level+1)
			levelLayer[i].setPosition(Width*(1-0.7f)/2+(Width*0.7f+150), 200);
		else if(i==level)
			levelLayer[i].setPosition(Width*(1-0.7f)/2, 150);
		else if(i==level-1)
			levelLayer[i].setPosition(Width*(1-0.7f)/2-(Width*0.6f+150), 200);
		else
			levelLayer[i].setVisible(false);

	}

}
