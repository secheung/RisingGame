package structure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import object.GameObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import engine.open2d.draw.Plane;
import engine.open2d.texture.AnimatedTexture.Playback;

import android.content.Context;
import android.util.Log;

public class ActionDataTool {
	private static String NAME = "name";
	private static String HITBOX = "hit_box";
	private static String HURTBOX = "hurt_box";
	private static String HITSTOP = "hit_stop";
	private static String LEFT = "left";
	private static String BOTTOM = "bottom";
	private static String RIGHT = "right";
	private static String TOP = "top";
	private static String ACTIVE_FRAME = "active_frame";
	private static String SELECT_BOX = "select";
	
	private static String PLANE_WIDTH = "width";
	private static String PLANE_HEIGHT = "height";
	private static String PLANE_ROWS = "rows";
	private static String PLANE_COLUMNS = "columns";
	private static String PLANE_PLAY_BACK = "play_back";

	Context context;
	String currentFile;
	public ActionDataTool(Context context){
		this.context = context;
	}
	
	public void readFile(int resourceId){
		InputStream inputStream = context.getResources().openRawResource(resourceId);

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;
		StringBuilder text = new StringBuilder();

		try {
			while (( line = buffreader.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			Log.e("STREAM ERROR", e.getMessage());
			return;
		}

		this.currentFile = text.toString();

	}
	
	public List<ActionData> parseFrameData(){
		if(this.currentFile == null){
			Log.w("JSON PARSE WARNING", "NO FILE LOADED");
			return null;
		}
		
		List<ActionData> actionData = new LinkedList<ActionData>();
		
		try {
			JSONObject parser = new JSONObject(this.currentFile);
			Iterator<?> keys = parser.keys();
			while(keys.hasNext()){
				String key = (String)keys.next();
				JSONObject actionJSON = parser.getJSONObject(key);

				String name = actionJSON.getString(NAME);
				ActionData data = new ActionData(name);
				
				if(actionJSON.has(HITBOX)){
					List<HitBox> hitBoxes = parseHitBoxData(actionJSON.getJSONArray(HITBOX));
					data.setHitBoxes(hitBoxes);
				}

				if(actionJSON.has(HURTBOX)){
					List<HurtBox> hurtBoxes = parseHurtBoxData(actionJSON.getJSONArray(HURTBOX));
					data.setHurtBoxes(hurtBoxes);
				}
				
				if(actionJSON.has(HITSTOP)){
					int hitstop = actionJSON.getInt(HITSTOP);
					data.setHitstop(hitstop);
					
				}
				
				PlaneData planeData = parsePlaneData(actionJSON.getJSONObject("plane_data"));
				data.setPlaneData(planeData);
				
				actionData.add(data);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return actionData;
	}
	
	public PlaneData parsePlaneData(JSONObject planeData) throws JSONException{
		float width = (float) planeData.getDouble(PLANE_WIDTH);
		float height = (float) planeData.getDouble(PLANE_HEIGHT);
		int rows = planeData.getInt(PLANE_ROWS);
		int columns = planeData.getInt(PLANE_COLUMNS);
		
		Playback playback = Playback.PLAY;
		if(planeData.has(PLANE_PLAY_BACK)){
			String name = planeData.getString(PLANE_PLAY_BACK);
			playback = Playback.getPlaybackFromName(name);
		}

		return new PlaneData(width,height,rows,columns,playback);
	}

	
	public List<HitBox> parseHitBoxData(JSONArray hitBoxData) throws JSONException{
		List<HitBox> hitBoxes = new LinkedList<HitBox>();
		
		for(int index = 0; index < hitBoxData.length(); index++){
			JSONObject boundsData = hitBoxData.getJSONObject(index);
			float left = (float) boundsData.getDouble(LEFT);
			float bottom = (float) boundsData.getDouble(BOTTOM);
			float right = (float) boundsData.getDouble(RIGHT);
			float top = (float) boundsData.getDouble(TOP);
			HashSet<Integer> activeFrames;
			if(boundsData.has(ACTIVE_FRAME)){
				activeFrames = new HashSet<Integer>();
				JSONArray actives = boundsData.getJSONArray(ACTIVE_FRAME);
				for(int i = 0; i < actives.length(); i++){
					activeFrames.add(actives.getInt(i));
				}
			} else {
				activeFrames = new HashSet<Integer>();
			}
			HitBox box = new HitBox(left, top, right, bottom, activeFrames);
			if(boundsData.has(SELECT_BOX)){
				box.setSelected((float)boundsData.getDouble(SELECT_BOX));
			}
			hitBoxes.add(box);
		}
		
		return hitBoxes;
	}
	
	public List<HurtBox> parseHurtBoxData(JSONArray hitBoxData) throws JSONException{
		List<HurtBox> hurtBoxes = new LinkedList<HurtBox>();
		
		for(int index = 0; index < hitBoxData.length(); index++){
			JSONObject boundsData = hitBoxData.getJSONObject(index);
			float left = (float) boundsData.getDouble(LEFT);
			float bottom = (float) boundsData.getDouble(BOTTOM);
			float right = (float) boundsData.getDouble(RIGHT);
			float top = (float) boundsData.getDouble(TOP);
			HashSet<Integer> activeFrames;
			if(boundsData.has(ACTIVE_FRAME)){
				activeFrames = new HashSet<Integer>();
				JSONArray actives = boundsData.getJSONArray(ACTIVE_FRAME);
				for(int i = 0; i < actives.length(); i++){
					activeFrames.add(actives.getInt(i));
				}
			} else {
				activeFrames = new HashSet<Integer>();
			}
			HurtBox box = new HurtBox(left, top, right, bottom, activeFrames);
			if(boundsData.has(SELECT_BOX)){
				box.setSelected((float)boundsData.getDouble(SELECT_BOX));
			}
			hurtBoxes.add(box);
		}
		
		return hurtBoxes;
	}
}
