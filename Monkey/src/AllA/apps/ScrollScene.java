package AllA.apps;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

public class ScrollScene{
	// Constants
	static final int SCROLL_IN_X = 0;
	static final int SCROLL_IN_Y = 1;

	// Variables
	Scene scrollScene;
	float pX;
	float pY;
	float Width;
	float Height;
	float Length_X;
	float Length_Y;
	float Velocity = 0;
	
	
	
	IEntity scrollBar = null;
	float b_pX;
	float b_pY;
	float b_Width;
	float b_Height;
	
	// States
	int scroll_direction = 0;
	float scroll_rate;
	
	
	ScrollScene(int direction){
		scrollScene = new Scene();
		scrollScene.setBackgroundEnabled(false);
		scroll_direction = direction;
	}

	
	ScrollScene(int direction, float width, float height){
		scrollScene = new Scene();
		scrollScene.setBackgroundEnabled(false);
		scroll_direction = direction;

		Length_X = Width = width;
		Length_Y = Height = height;
	}

	
	// can attach ONLY Sprite and Text
	public void attachChild(Sprite pSprite){
		scrollScene.attachChild(pSprite);

		// Scroll��ü�� ũ�� ���� (������ (0,0)���� ���̷� ����)
		if(pSprite.getX()+pSprite.getWidth() > Length_X)
			Length_X = pSprite.getX()+pSprite.getWidth();

		if(pSprite.getY()+pSprite.getHeight() > Length_Y)
			Length_Y = pSprite.getY()+pSprite.getHeight();
	}

	public void attachChild(Text pText){
		scrollScene.attachChild(pText);

		// Scroll��ü�� ũ�� ���� (������ (0,0)���� ���̷� ����)
		if(pText.getX()+pText.getWidth() > Length_X)
			Length_X = pText.getX()+pText.getWidth();

		if(pText.getY()+pText.getHeight() > Length_Y)
			Length_Y = pText.getY()+pText.getHeight();
	}
	
	
	public Scene getScene(){
		return scrollScene;
	}
	
	public void registerTouchArea(ITouchArea pTouchArea){
		scrollScene.registerTouchArea(pTouchArea);
	}
		
	public void setPosition(float pX, float pY){
		this.pX = pX;
		this.pY = pY;
		scrollScene.setPosition(pX, pY);
	}
	
	public float getWidth(){
		return Width;
	}
	
	public float getHeight(){
		return Height;
	}

	
	public void scroll(float pDistance){
		float term = 0;
		// X�� Y�� ���о��� ����
		if(scroll_direction == SCROLL_IN_X)		term = Length_X - Width;
		if(scroll_direction == SCROLL_IN_Y)		term = Length_Y - Height;
		
		//�ӵ� ����
		Velocity = pDistance;
		
		// Scroll����
		if(scroll_rate>1){
			// ���������� �ʹ� ���� ��ũ��
			scroll_rate -= (pDistance/3) / term;
		}
		else if(scroll_rate<0){
			// �������� �ʹ� ���� ��ũ��
			scroll_rate -= (pDistance/3) / term;
		}
		else{
			// �������ΰ��
			scroll_rate -= (pDistance) / term;
		}
				
		updateScroll();
	}
	
	// scroll_rate�� ���� Scene�� ���� �̵� (��ġ ����)
	public void updateScroll(){
		if(scroll_direction == SCROLL_IN_X)
			scrollScene.setX(pX - scroll_rate * (Length_X - Width) );
		if(scroll_direction == SCROLL_IN_Y)
			scrollScene.setY(pY - scroll_rate * (Length_Y - Height) );
		
		updateScrollBar();
	}
	
	

	public void setScrollBar(IEntity scrollbar, float pX, float pY, float width, float height, Color color){
		scrollBar = scrollbar;
		scrollBar.setColor(color);
		b_pX = pX;
		b_pY = pY;
		b_Width = width;
		b_Height = height;

		updateScrollBar();
	}
	
	public void updateScrollBar(){
		scrollBar.setX(b_pX+scroll_rate*b_Width*(1-Width/Length_X));
		scrollBar.setY(b_pY);
	}
	// new methods for scroll function
	
	public float getRate(){
		return scroll_rate;
	}
	
	public float getLengthX(){
		return Length_X;
	}
	
	public float getLengthY(){
		return Length_Y;
	}
	
	public float getMovedDistance(){
		if(scroll_direction == SCROLL_IN_X)
			return scroll_rate * Width;
		else
			return scroll_rate * Height;
	}
	
	public void generalEffect(){
		
		//scroll��ġ�� ���� �ӵ� ����
		if(scroll_rate<0){
			Velocity = -(float)Math.sqrt(2*2.0f*(-scroll_rate)*(Length_X-Width));
			if(scroll_rate*(Length_X-Width)<-100)
				Velocity = -20.0f;
			else
				Velocity += 2.0f;
		}else if(scroll_rate>1.0f){
			Velocity = (float)Math.sqrt(2*2.0f*(scroll_rate-1)*(Length_X-Width));
			if((scroll_rate-1)*(Length_X-Width)>100)
				Velocity = +20.0f;
			else
				Velocity -= 2.0f;
			
		}else{//���ο� ����
			if(Velocity<0)
				Velocity += 0.5f;
			if(Velocity>0)
				Velocity -= 0.5f;
			if(Math.abs(Velocity)<1)
				Velocity = 0;
		}
		
		
		// ��ũ�� �̵�
		scroll_rate -= Velocity/(Length_X-Width);
		
		//���� �����ϸ� ����
		if(Math.abs(scroll_rate)*(Length_X-Width)<=Math.abs(Velocity)){
			scroll_rate = 0;
			Velocity = 0;
		}
		if(Math.abs((scroll_rate-1))*(Length_X-Width)<=Math.abs(Velocity)){
			scroll_rate = 1;
			Velocity = 0;
		}
		
		updateScroll();

	}


}
