package AllA.apps;

import java.util.HashMap;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.content.Context;
import android.graphics.Typeface;

public class ResourceManager {
	
	static HashMap<String, BitmapTextureAtlas>	AtlasResources	= new HashMap<String, BitmapTextureAtlas>();
	static HashMap<String, ITextureRegion>		RegionResources	= new HashMap<String, ITextureRegion>();
	static HashMap<String, IFont> 				FontResources	= new HashMap<String, IFont>();

	static TextureManager	mTextureManager;
	static FontManager		mFontManager;

	static Context mContext;
	
	////////////////////////////////////
	// Setter
	////////////////////////////////////
	static public void setTextureManager(TextureManager pTextureManager){
		mTextureManager = pTextureManager;
	}
	
	static public void setFontManager(FontManager pFontManager){
		mFontManager = pFontManager;
	}

	static public void setAssetBasePath(String pAssetBasePath){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pAssetBasePath);
	}

	static public void setContext(Context pContext){
		mContext = pContext;
	}
	
	
	
	static public void loadImage(String keys, String pAssetPath, int pWidth, int pHeight){
		BitmapTextureAtlas newTextureAtlas;
		ITextureRegion newTextureRegion;
		
		if(AtlasResources.containsKey(keys))
			return;
		
<<<<<<< HEAD
		newTextureAtlas = new BitmapTextureAtlas(mTextureManager, pWidth, pHeight, TextureOptions.DEFAULT);
=======
		newTextureAtlas = new BitmapTextureAtlas(mTextureManager, pWidth, pHeight, TextureOptions.BILINEAR);
>>>>>>> master

		newTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(newTextureAtlas, mContext, pAssetPath, 0, 0);

		newTextureAtlas.load();
<<<<<<< HEAD
		
=======
				
>>>>>>> master
		AtlasResources.put(keys, newTextureAtlas);
		RegionResources.put(keys, newTextureRegion);
	}
	
	static public void loadFont(String keys, int pTextureWidth, int pTextureHeight, Typeface pTypeface, float pSize){
		if(FontResources.containsKey(keys))
			return;
		
		IFont newFont = FontFactory.create(mFontManager, mTextureManager, pTextureWidth, pTextureHeight, pTypeface, pSize);
		newFont.load();
		FontResources.put(keys, newFont);
	}
	
	
	////////////////////////////////////
	// Getter
	////////////////////////////////////
	
	static public BitmapTextureAtlas getAtlas(String keys){
		if(AtlasResources.containsKey(keys) == false)
			return null;
		
		return AtlasResources.get(keys);
	}
	
	static public ITextureRegion getRegion(String keys){
		if(RegionResources.containsKey(keys) == false)
			return null;
		
		return RegionResources.get(keys);
	}
	
	static public IFont getFont(String keys){
		if(FontResources.containsKey(keys) == false)
			return null;
		
		return FontResources.get(keys);
	}

}
