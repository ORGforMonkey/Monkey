package AllA.apps;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;


public class ScrollLayer{
   // Constants
   static final int SCROLL_IN_X = 0;
   static final int SCROLL_IN_Y = 1;

   // Variables
   Layer scrollLayer;
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
   
   
   // ===========================================================
   // Constructors
   // ===========================================================
   ScrollLayer(int direction){
      scrollLayer = new Layer();
      scroll_direction = direction;
   }

   
   ScrollLayer(int direction, float width, float height){
      scrollLayer = new Layer();
      scroll_direction = direction;

      Width = width;
      Height = height;
      
      Length_X = Width + 1;
      Length_Y = Height + 1;
   }
   
   
   
   // ===========================================================
   // Getter & Setter
   // ===========================================================
   public Layer getLayer(){
      return scrollLayer;
   }
      
   public float getWidth(){
      return Width;
   }
   
   public float getHeight(){
      return Height;
   }
   
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
         return scroll_rate * (Length_X-Width);
      else
         return scroll_rate * (Length_Y-Height);
   }
   
   
   public void setPosition(float pX, float pY){
      this.pX = pX;
      this.pY = pY;
      scrollLayer.setPosition(pX, pY);
   }


   
   // ===========================================================
   // Methods
   // ===========================================================

   
   public void attachChild(Sprite pSprite){
      scrollLayer.attachChild(pSprite);

      // Scroll객체의 크기 갱신 (무조건 (0,0)부터 길이로 측정)
      if(pSprite.getX()+pSprite.getWidth() > Length_X)
         Length_X = pSprite.getX()+pSprite.getWidth();

      if(pSprite.getY()+pSprite.getHeight() > Length_Y)
         Length_Y = pSprite.getY()+pSprite.getHeight();
   }

   public void attachChild(Text pText){
      scrollLayer.attachChild(pText);

      // Scroll객체의 크기 갱신 (무조건 (0,0)부터 길이로 측정)
      if(pText.getX()+pText.getWidth() > Length_X)
         Length_X = pText.getX()+pText.getWidth();

      if(pText.getY()+pText.getHeight() > Length_Y)
         Length_Y = pText.getY()+pText.getHeight();
   }
   
   
   public void attachChild(Rectangle pRect){
      scrollLayer.attachChild(pRect);

      // Scroll객체의 크기 갱신 (무조건 (0,0)부터 길이로 측정)
      if(pRect.getX()+pRect.getWidth() > Length_X)
         Length_X = pRect.getX()+pRect.getWidth();

      if(pRect.getY()+pRect.getHeight() > Length_Y)
         Length_Y = pRect.getY()+pRect.getHeight();
   }
   
   
   public void attachChild(Entity pEntity){
      scrollLayer.attachChild(pEntity);
   }
   
   

   // Scrolling 구현
   public void scroll(float pDistance){
      float term = 0;
      // X와 Y의 구분없이 통합
      if(scroll_direction == SCROLL_IN_X)      term = Length_X - Width;
      if(scroll_direction == SCROLL_IN_Y)      term = Length_Y - Height;
      
      //속도 갱신
      Velocity = pDistance;
      
      
      // Scroll적용
      if(scroll_rate>1){
         // 오른쪽으로 너무 많이 스크롤
         scroll_rate -= (pDistance/3) / term;
      }
      else if(scroll_rate<0){
         // 왼쪽으로 너무 많이 스크롤
         scroll_rate -= (pDistance/3) / term;
      }
      else{
         // 정상적인경우
         scroll_rate -= (pDistance) / term;
      }
            
      updateScroll();
   }
   
   // scroll_rate에 맞춰 Scene을 직접 이동 (위치 갱신)
   public void updateScroll(){
      if(scroll_direction == SCROLL_IN_X)
         scrollLayer.setX(pX - scroll_rate * (Length_X - Width) );
      if(scroll_direction == SCROLL_IN_Y)
         scrollLayer.setY(pY - scroll_rate * (Length_Y - Height) );         

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
      if(scrollBar == null)
         return;
      
      if(scroll_direction == SCROLL_IN_X){
         scrollBar.setX(b_pX+scroll_rate*b_Width*(1-Width/Length_X));
         scrollBar.setY(b_pY);
      }
      if(scroll_direction == SCROLL_IN_Y){
         scrollBar.setX(b_pX);
         scrollBar.setY(b_pY+scroll_rate*b_Height*(1-Height/Length_Y));
      }
   }
   
   public void generalEffect(){
      
      float moveDistance=0;
      if(scroll_direction == SCROLL_IN_X)
         moveDistance = Length_X-Width;
      if(scroll_direction == SCROLL_IN_Y)
         moveDistance = Length_Y-Height;
      
      //scroll위치에 따른 속도 지정
      if(scroll_rate<0){
         Velocity = -(float)Math.sqrt(2*2.0f*(-scroll_rate)*moveDistance);
         if(scroll_rate*moveDistance<-100)
            Velocity = -20.0f;
         else
            Velocity += 2.0f;
      }else if(scroll_rate>1.0f){
         Velocity = (float)Math.sqrt(2*2.0f*(scroll_rate-1)*moveDistance);
         if((scroll_rate-1)*moveDistance>100)
            Velocity = +20.0f;
         else
            Velocity -= 2.0f;
         
      }else{//내부에 있음
         if(Velocity<0)
            Velocity += 0.5f;
         if(Velocity>0)
            Velocity -= 0.5f;
         if(Math.abs(Velocity)<1)
            Velocity = 0;
      }
      
      
      // 스크롤 이동
      scroll_rate -= Velocity/moveDistance;
      
      //끝에 도달하면 정지
      if(Math.abs(scroll_rate)*moveDistance<=Math.abs(Velocity)){
         scroll_rate = 0;
         Velocity = 0;
      }
      if(Math.abs((scroll_rate-1))*moveDistance<=Math.abs(Velocity)){
         scroll_rate = 1;
         Velocity = 0;

      }
      
      updateScroll();

   }


}