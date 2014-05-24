package structure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import object.GameObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import engine.open2d.draw.Plane;

import android.content.Context;
import android.util.Log;

public class ActionDataTool {
	private static String NAME = "name";
	private static String HITBOX = "hit_box";
	private static String LEFT = "left";
	private static String BOTTOM = "bottom";
	private static String RIGHT = "right";
	private static String TOP = "top";
	
	
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
	
	public List<ActionData> parseFrameData(GameObject pairedObject){
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
				//Plane planeData = parsePlaneData(name,action.getJSONObject("plane_data"));
				List<HitBox> hitBoxes = parseHitBoxData(actionJSON.getJSONArray(HITBOX));
				
				ActionData data = new ActionData(name,pairedObject);
				data.setHitBoxes(hitBoxes);
				actionData.add(data);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return actionData;
	}
	
	/*
	public Plane parsePlaneData(String name, JSONObject planeData){
		float width = planeData.getInt("width");
		float height = planeData.getInt("height");
		int rows = planeData.getInt("rows");
		int columns = planeData.getInt("columns");
		
		return new Plane(name,width,height,rows,columns);
	}
	*/
	
	public List<HitBox> parseHitBoxData(JSONArray hitBoxData) throws JSONException{
		List<HitBox> hitBoxes = new LinkedList<HitBox>();
		
		for(int index = 0; index < hitBoxData.length(); index++){
			JSONObject boundsData = hitBoxData.getJSONObject(index);
			float left = (float) boundsData.getDouble(LEFT);
			float bottom = (float) boundsData.getDouble(BOTTOM);
			float right = (float) boundsData.getDouble(RIGHT);
			float top = (float) boundsData.getDouble(TOP);
			
			HitBox box = new HitBox(left, top, right, bottom);
			hitBoxes.add(box);
		}
		
		return hitBoxes;
	}
}
