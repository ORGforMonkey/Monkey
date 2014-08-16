package AllA.apps;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class GameActivity extends SimpleBaseActivity implements IAccelerationListener, IPinchZoomDetectorListener, IOnSceneTouchListener{
	
	/* variable */
	private PhysicsWorld mPhysicsWorld;
	private Sprite BackSprite;
	Line line1;
	Sprite obj1;
	Sprite obj2;
	Sprite obj3;
	Rectangle ground;
	Rectangle left;
	Rectangle right;
	Body body1;
	Body body2;
	Body body3;
	Body g_wall;
	Body l_wall;
	Body r_wall;
    PhysicsEditorShapeLibrary physicsEditorShapeLibrary;
    private PinchZoomDetector mPinchZoomDetector;
    private float mPinchZoomStartedCameraZoomFactor;
    
    ZoomCamera mCamera;
	HUD mHUD;
	
	int g_Width;
	int g_Height;

	/* constructor */
	GameActivity(int width, int height,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(width,height,pVertexBufferObjectManager);

		
		g_Width = 1280;
		g_Height = 3000;
		
		mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		physicsEditorShapeLibrary = new PhysicsEditorShapeLibrary();
		
		mPinchZoomDetector = new PinchZoomDetector(this);

	}
	
	@Override
	public void onResume() {
		mCamera = (ZoomCamera) SceneManager.mEngine.getCamera();
		mCamera.setBoundsEnabled(true);
		mCamera.setBounds(0, 0, g_Width, g_Height);
		if(mHUD!=null)	mHUD.setOnSceneTouchListener(this);
		if(obj1!=null)	mCamera.setChaseEntity(obj1);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		mHUD.setOnSceneTouchListener(null);
		mCamera.setChaseEntity(null);
		mCamera.set(0, 0, Width, Height);
		mCamera.setZoomFactor(1);
		super.onPause();
	}
	
	/* methods */
	
	@Override
	public void loadResources() {
		ResourceManager.loadImage("onGameBack", "onGameBack.jpg", 1280, 3000);
		ResourceManager.loadImage("obj1", "obj1.png", 100, 100);

		super.loadResources();
	}
	
	@Override
	public void loadScene() {
		
		if(isLoaded())
			return;
		
		// background
		BackSprite = new Sprite(0, 0, ResourceManager.getRegion("onGameBack"), vertexBufferObjectManager);
		mainLayer.attachChild(BackSprite);
		

		// wall
		ground = new Rectangle(0,g_Height-100, g_Width, 100, vertexBufferObjectManager);
		left = new Rectangle(-10, 0, 10, g_Height, vertexBufferObjectManager);
		right = new Rectangle(g_Width, 0, 10, g_Height, vertexBufferObjectManager);
		ground.setColor(new Color(0.53f, 0.33f, 0.16f));
		
		FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.2f, 0.0f);//마찰이 있으면 수직항력이 커지면 정지해버림
		FixtureDef groundFixtureDef = PhysicsFactory.createFixtureDef(0, 0.1f, 5f);
		g_wall = PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody, groundFixtureDef);
		l_wall = PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		r_wall = PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		mainLayer.attachChild(ground);
		mainLayer.attachChild(left);
		mainLayer.attachChild(right);
		
		
		// objects
		obj1 = new Sprite(g_Width/2-50,100,ResourceManager.getRegion("obj1"),vertexBufferObjectManager);
		obj2 = new Sprite(g_Width/2-100,200,ResourceManager.getRegion("obj1"),vertexBufferObjectManager);
		obj3 = new Sprite(g_Width/2+100,200,ResourceManager.getRegion("obj1"),vertexBufferObjectManager);
//		obj1.setColor(new Color(0, 0, 0));
//		obj2.setColor(new Color(0, 1, 0));
//		obj3.setColor(new Color(0, 0, 1));
		
		FixtureDef FixtureDef = PhysicsFactory.createFixtureDef(0.01f, 0.2f, 0.0f);

		physicsEditorShapeLibrary.open(ResourceManager.getContext(), "shape/obj1.xml");
		body1 = physicsEditorShapeLibrary.createBody("obj1", obj1, this.mPhysicsWorld);
		body2 = physicsEditorShapeLibrary.createBody("obj1", obj2, this.mPhysicsWorld);
		body3 = physicsEditorShapeLibrary.createBody("obj1", obj3, this.mPhysicsWorld);

//		body2 = PhysicsFactory.createBoxBody(mPhysicsWorld, obj2, BodyType.DynamicBody, FixtureDef);
//		body3 = PhysicsFactory.createBoxBody(mPhysicsWorld, obj3, BodyType.DynamicBody, FixtureDef);
//		body1.setLinearDamping(1);//공기 저항
		
		

		mainLayer.attachChild(obj1);
		mainLayer.attachChild(obj2);
		mainLayer.attachChild(obj3);


		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(obj1, body1, true, false));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(obj2, body2, true, false));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(obj3, body3, true, false));


		{
			RevoluteJointDef chainLinkDef1 = new RevoluteJointDef();
			chainLinkDef1.collideConnected = false;
			chainLinkDef1.initialize(body1, body2, body1.getWorldCenter());
			
			chainLinkDef1.localAnchorA.set(0, 0.0f);
			chainLinkDef1.localAnchorB.set(-5.0f, -7.0f);
			chainLinkDef1.enableMotor = false;
			chainLinkDef1.motorSpeed = 0;
			chainLinkDef1.enableLimit = false;
			
			Joint chainLink1 = mPhysicsWorld.createJoint(chainLinkDef1);
		}
		
		{
			RevoluteJointDef chainLinkDef2 = new RevoluteJointDef();
			chainLinkDef2.collideConnected = false;
			chainLinkDef2.initialize(body1, body3, body1.getWorldCenter());
			
			chainLinkDef2.localAnchorA.set(0, 0.0f);
			chainLinkDef2.localAnchorB.set(5.0f, -7.0f);
			chainLinkDef2.enableMotor = false;
			chainLinkDef2.motorSpeed = 0;
			chainLinkDef2.enableLimit = false;
			
			Joint chainLink2 = mPhysicsWorld.createJoint(chainLinkDef2);
		}
		
		
		
		mainLayer.registerUpdateHandler(mPhysicsWorld);
		
		mCamera.setChaseEntity(obj1);
		
		//touch
		mHUD = new HUD();
		mCamera.setHUD(mHUD);
		
		mHUD.setOnSceneTouchListener(this);
		

		super.loadScene();
	}

	
	@Override
	public void registerTouchAreatoSceneManager() {

		super.registerTouchAreatoSceneManager();
	}
	
	@Override
	public void deleteSprites() {
		
		mHUD.clearTouchAreas();
		super.deleteSprites();
	}
	
	@Override
	public void updateActivity() {
		
		super.updateActivity();
	}

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		if(isFocused()){
/*			final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX()*4, 4.9f);
			this.mPhysicsWorld.setGravity(gravity);
			Vector2Pool.recycle(gravity);
*/		}
	}
	
	public void setAcceleration(float accelX){
		if(isFocused()){
			final Vector2 gravity = Vector2Pool.obtain(accelX, 4.9f);
			this.mPhysicsWorld.setGravity(gravity);
			Vector2Pool.recycle(gravity);
		}
	}
	
	private void move(TouchEvent pSceneTouchEvent){
		if(pSceneTouchEvent.getX()<Width/2){
			setAcceleration(-5.0f);			
		}else{
			setAcceleration(5.0f);
		}
	}
	
	private void stop(){
		setAcceleration(0);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		if (this.mPinchZoomDetector != null) {
			this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if (this.mPinchZoomDetector.isZooming()) {
			} else {
				if(pSceneTouchEvent.getMotionEvent().getPointerCount()==1){
					if (pSceneTouchEvent.isActionDown()) {
						move(pSceneTouchEvent);
					}
					if (pSceneTouchEvent.isActionUp()) {
						stop();
					}
				}else{
					stop();
				}
			}
		}
		return true;
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {
		mPinchZoomStartedCameraZoomFactor = mCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		if(mCamera.getZoomFactor()>=1){
			if(mPinchZoomStartedCameraZoomFactor * pZoomFactor<1)
				mCamera.setZoomFactor(1);
			else
				mCamera.setZoomFactor(mPinchZoomStartedCameraZoomFactor * pZoomFactor);
		}
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		
	}

}
