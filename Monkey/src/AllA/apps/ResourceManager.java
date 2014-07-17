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
import android.graphics.Region;
import android.graphics.Typeface;
import android.util.Log;

public class ResourceManager {
	
	HashMap<String, BitmapTextureAtlas> AtlasResources;
	HashMap<String, ITextureRegion> RegionResources;
	HashMap<String, IFont> FontResources;
	TextureManager mTextureManager;
	FontManager mFontManager;
	Context mContext;
	
	ResourceManager(){
		AtlasResources = new HashMap<String, BitmapTextureAtlas>();
		RegionResources = new HashMap<String, ITextureRegion>();
		FontResources = new HashMap<String, IFont>();
	}
	
	ResourceManager(int Capacity){
		AtlasResources = new HashMap<String, BitmapTextureAtlas>(Capacity);
		RegionResources = new HashMap<String, ITextureRegion>(Capacity);
		FontResources = new HashMap<String, IFont>(Capacity);
	}
	
	////////////////////////////////////
	// Setter
	////////////////////////////////////
	public void setTextureManager(TextureManager pTextureManager){
		mTextureManager = pTextureManager;
	}
	
	public void setFontManager(FontManager pFontManager){
		mFontManager = pFontManager;
	}

	public void setAssetBasePath(String pAssetBasePath){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pAssetBasePath);
	}

	public void setContext(Context pContext){
		mContext = pContext;
	}
	
	
	
	public void loadImage(String keys, String pAssetPath, int pWidth, int pHeight){
		BitmapTextureAtlas newTextureAtlas;
		ITextureRegion newTextureRegion;
		
		if(AtlasResources.containsKey(keys))
			return;
		
		newTextureAtlas = new BitmapTextureAtlas(mTextureManager, pWidth, pHeight, TextureOptions.BILINEAR);

		newTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(newTextureAtlas, mContext, pAssetPath, 0, 0);

		newTextureAtlas.load();
		
		AtlasResources.put(keys, newTextureAtlas);
		RegionResources.put(keys, newTextureRegion);
	}
	
	public void loadFont(String keys, int pTextureWidth, int pTextureHeight, Typeface pTypeface, float pSize){
		if(FontResources.containsKey(keys))
			return;
		
		IFont newFont = FontFactory.create(mFontManager, mTextureManager, pTextureWidth, pTextureHeight, pTypeface, pSize);
		newFont.load();
		FontResources.put(keys, newFont);
	}
	
	
	////////////////////////////////////
	// Getter
	////////////////////////////////////
	
	public BitmapTextureAtlas getAtlas(String keys){
		if(AtlasResources.containsKey(keys) == false)
			return null;
		
		return AtlasResources.get(keys);
	}
	
	public ITextureRegion getRegion(String keys){
		if(RegionResources.containsKey(keys) == false)
			return null;
		
		return RegionResources.get(keys);
	}
	
	public IFont getFont(String keys){
		if(FontResources.containsKey(keys) == false)
			return null;
		
		return FontResources.get(keys);
	}

}
