package structure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import object.GameObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import engine.open2d.draw.Plane;
import engine.open2d.texture.AnimatedTexture.Playback;
import junit.framework.Assert;
import android.content.Context;
import android.drm.DrmStore.Action;
import android.graphics.PointF;
import android.util.Log;

public class ActionDataTool {
	private static String NAME = "name";
	private static String HITBOX = "hit_box";
	private static String HURTBOX = "hurt_box";
	private static String LEFT = "left";
	private static String BOTTOM = "bottom";
	private static String RIGHT = "right";
	private static String TOP = "top";
	private static String ACTIVE_FRAME = "active_frame";
	private static String SELECT_BOX = "select";
	
	private static String HIT_TYPE = "hit_type";
	public static String SINGLE_HIT = "single_hit";
	public static String MULTI_HIT = "multi_hit";
	
	private static String X_INIT_SPEED = "x_init_speed";
	private static String Y_INIT_SPEED = "y_init_speed";
	private static String X_ACCEL = "x_accel";
	private static String Y_ACCEL = "y_accel";
	
	private static String HIT_STOP = "hit_stop";
	private static String HIT_STUN = "hit_stun";
	
	private static String PLANE_WIDTH = "width";
	private static String PLANE_HEIGHT = "height";
	private static String PLANE_ROWS = "rows";
	private static String PLANE_COLUMNS = "columns";
	private static String PLANE_PLAY_BACK = "play_back";

	public static String RANDOM_TRIGGER = "random_trigger";
	public static String DISTANCE_TRIGGER = "distance_trigger";
	
	public static String CONT_ANIM = "cont_anim";//continues animation from previous state. current maps to 1 with no value meaning. as long as modifier exists
	
	private static String ACTION_PROPERTIES = "action_properties";
	private static String INTERACTION_PROPERTIES = "interaction_properties";
	
	public static String TRIGGER_PROP_STATE = "state";
	public static String TRIGGER_PROP_VALUE = "value";
	public static String TRIGGER_PROP_STATE_COND = "state_cond";
	public static String TRIGGER_PROP_DEFAULT = "default";
	
	private static String MODIFIERS = "modifiers";
	public static String ACTIVE_AFTER = "active_after";
	public static String ACTIVE_BEFORE = "active_before";
	public static String REVERSE_X = "reverse_x";
	
	public static String FACE_PLAYER = "face_player";//options - track-1, look-2
	public static int FACE_PLAYER_FOLLOW = 1;
	public static int FACE_PLAYER_LOOK = 2;
	
	public static String X_POINT_OFFSET = "x_pt_offset";
	public static String Y_POINT_OFFSET = "y_pt_offset";
	
	public static String CONT_SPEED = "cont_speed";//options - both-1, x-2, y-3
	public static int CONT_SPEED_BOTH_DIR = 1;
	public static int CONT_SPEED_X_DIR    = 2;
	public static int CONT_SPEED_Y_DIR    = 3;
	
	public static String SNAP_TO_FLOOR = "snap_to_floor";

	public static String CANCEL_FRAME = "cancel_frame";

	private static String TRIGGER_INIT_SPEED = "trigger_init_speeds";
	public static String TRIGGER_CHANGE = "trigger_change";
	public static String TRIGGER_CANCEL = "trigger_cancel";
	public static String GROUND_HIT_TRIGGER = "ground_hit_trigger";
	public static String GROUND_HIT_COND_TRIGGER = "ground_hit_cond_trigger";
	public static String AIR_HIT_TRIGGER = "air_hit_trigger";
	public static String AIR_HIT_COND_TRIGGER = "air_hit_cond_trigger";
	public static String WALL_TRIGGER = "wall_trigger";
	public static String ON_HIT_TRIGGER = "on_hit_trigger";
	public static String ON_HIT_COND_TRIGGER = "on_hit_cond_trigger";
	public static String GROUND_TRIGGER = "ground_trigger";
	public static String PLAYED_TRIGGER = "played_trigger";
	public static String STOPPED_X_TRIGGER = "stopped_x_trigger";//triggers when x movement stopped
	public static String STOPPED_Y_TRIGGER = "stopped_y_trigger";//triggers when y movement stopped
	public static String CONTINUOUS_TRIGGER = "continuous_trigger";
	
	public static String SWIPE_F_TRIGGER = "swipe_f_trigger";
	public static String SWIPE_U_TRIGGER = "swipe_u_trigger";
	public static String SWIPE_B_TRIGGER = "swipe_b_trigger";
	public static String SWIPE_D_TRIGGER = "swipe_d_trigger";
	
	public static String TAP_TRIGGER = "tap_trigger";
	
	public static String DTAP_F_TRIGGER = "dtap_f_trigger";
	public static String DTAP_U_TRIGGER = "dtap_u_trigger";
	public static String DTAP_B_TRIGGER = "dtap_b_trigger";
	public static String DTAP_D_TRIGGER = "dtap_d_trigger";
	
	public static String HOLD_PRESS_TRIGGER = "hold_press_trigger";
	public static String HOLD_RELEASE_TRIGGER = "hold_release_trigger";
	
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
				
				if(actionJSON.has(ACTION_PROPERTIES)){
					ActionProperties properties = parseActionProperties(actionJSON.getJSONObject(ACTION_PROPERTIES));
					data.setActionProperties(properties);
				}

				if(actionJSON.has(INTERACTION_PROPERTIES)){
					
					ActionProperties properties = parseActionProperties(actionJSON.getJSONObject(INTERACTION_PROPERTIES));
					InteractionProperties interProperties = parseInteractionProperties(actionJSON.getJSONObject(INTERACTION_PROPERTIES));
					interProperties.copyActionProperties(properties);
					data.setInterProperties(interProperties);
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
	
	public ActionProperties parseActionProperties(JSONObject propertyData) throws JSONException{
		ActionProperties actionProperties = new ActionProperties();
		
		if(propertyData.has(HIT_TYPE)){
			actionProperties.setHitType(propertyData.getString(HIT_TYPE));
		}
		
		if(propertyData.has(X_INIT_SPEED)){
			actionProperties.setxInitSpeed((float)propertyData.getDouble(X_INIT_SPEED));
		}
		
		if(propertyData.has(Y_INIT_SPEED)){
			actionProperties.setyInitSpeed((float)propertyData.getDouble(Y_INIT_SPEED));
		}
		
		if(propertyData.has(X_ACCEL)){
			actionProperties.setxAccel((float)propertyData.getDouble(X_ACCEL));
		}
		
		if(propertyData.has(Y_ACCEL)){
			actionProperties.setyAccel((float)propertyData.getDouble(Y_ACCEL));
		}
		
		if(propertyData.has(X_POINT_OFFSET)){
			actionProperties.setxPtOffset((float)propertyData.getDouble(X_POINT_OFFSET));
		}
		
		if(propertyData.has(Y_POINT_OFFSET)){
			actionProperties.setyPtOffset((float)propertyData.getDouble(Y_POINT_OFFSET));
		}
		
		//trigger parsing
		if(propertyData.has(TRIGGER_CHANGE)){
			JSONObject triggerJSON = propertyData.getJSONObject(TRIGGER_CHANGE);
			if(triggerJSON.has(GROUND_HIT_TRIGGER)){
				String value = triggerJSON.getString(GROUND_HIT_TRIGGER);
				actionProperties.addTriggerChange(GROUND_HIT_TRIGGER, value);
			}
			
			if(triggerJSON.has(GROUND_HIT_COND_TRIGGER)){
				JSONArray propertyArray = triggerJSON.getJSONArray(GROUND_HIT_COND_TRIGGER);
				TriggerProperties triggerProp = parseTriggerCondProperty(propertyArray);
				actionProperties.addTriggerProperties(GROUND_HIT_COND_TRIGGER, triggerProp);
			}
			
			if(triggerJSON.has(AIR_HIT_TRIGGER)){
				String value = triggerJSON.getString(AIR_HIT_TRIGGER);
				actionProperties.addTriggerChange(AIR_HIT_TRIGGER, value);
			}
			
			if(triggerJSON.has(AIR_HIT_COND_TRIGGER)){
				JSONArray propertyArray = triggerJSON.getJSONArray(AIR_HIT_COND_TRIGGER);
				TriggerProperties triggerProp = parseTriggerCondProperty(propertyArray);
				actionProperties.addTriggerProperties(AIR_HIT_COND_TRIGGER, triggerProp);
			}
			
			if(triggerJSON.has(GROUND_TRIGGER)){
				String value = triggerJSON.getString(GROUND_TRIGGER);
				actionProperties.addTriggerChange(GROUND_TRIGGER, value);
			}
			
			if(triggerJSON.has(WALL_TRIGGER)){
				String value = triggerJSON.getString(WALL_TRIGGER);
				actionProperties.addTriggerChange(WALL_TRIGGER, value);
			}
			
			if(triggerJSON.has(ON_HIT_TRIGGER)){
				String value = triggerJSON.getString(ON_HIT_TRIGGER);
				actionProperties.addTriggerChange(ON_HIT_TRIGGER, value);
			}
			
			if(triggerJSON.has(ON_HIT_COND_TRIGGER)){
				JSONArray propertyArray = triggerJSON.getJSONArray(ON_HIT_COND_TRIGGER);
				TriggerProperties triggerProp = parseTriggerCondProperty(propertyArray);
				actionProperties.addTriggerProperties(ON_HIT_COND_TRIGGER, triggerProp);
			}
			
			if(triggerJSON.has(PLAYED_TRIGGER)){
				String value = triggerJSON.getString(PLAYED_TRIGGER);
				actionProperties.addTriggerChange(PLAYED_TRIGGER, value);
			}
			
			if(triggerJSON.has(STOPPED_X_TRIGGER)){
				String value = triggerJSON.getString(STOPPED_X_TRIGGER);
				actionProperties.addTriggerChange(STOPPED_X_TRIGGER, value);
			}
			
			if(triggerJSON.has(STOPPED_Y_TRIGGER)){
				String value = triggerJSON.getString(STOPPED_Y_TRIGGER);
				actionProperties.addTriggerChange(STOPPED_Y_TRIGGER, value);
			}
			
			if(triggerJSON.has(CONTINUOUS_TRIGGER)){
				String value = triggerJSON.getString(CONTINUOUS_TRIGGER);
				actionProperties.addTriggerChange(CONTINUOUS_TRIGGER, value);
			}
			
			if(triggerJSON.has(RANDOM_TRIGGER)){
				JSONArray propertyArray = triggerJSON.getJSONArray(RANDOM_TRIGGER);
				TriggerProperties triggerProp = parseTriggerProperty(propertyArray);
				actionProperties.addTriggerProperties(RANDOM_TRIGGER, triggerProp);
			}
			
			if(triggerJSON.has(DISTANCE_TRIGGER)){
				JSONArray propertyArray = triggerJSON.getJSONArray(DISTANCE_TRIGGER);
				TriggerProperties triggerProp = parseTriggerProperty(propertyArray);
				actionProperties.addTriggerProperties(DISTANCE_TRIGGER, triggerProp);
			}
		}
		
		if(propertyData.has(MODIFIERS)){
			//TODO: consider changing these propertyies to enum array so can just cycle throught when adding
			JSONObject modifiersJSON = propertyData.getJSONObject(MODIFIERS);
			if(modifiersJSON.has(ACTIVE_AFTER)){
				int value = modifiersJSON.getInt(ACTIVE_AFTER);
				actionProperties.addModifier(ACTIVE_AFTER, value);
			}

			if(modifiersJSON.has(ACTIVE_BEFORE)){
				int value = modifiersJSON.getInt(ACTIVE_BEFORE);
				actionProperties.addModifier(ACTIVE_BEFORE, value);
			}

			if(modifiersJSON.has(REVERSE_X)){
				int value = modifiersJSON.getInt(REVERSE_X);
				actionProperties.addModifier(REVERSE_X, value);
			}
			
			if(modifiersJSON.has(CONT_ANIM)){
				int value = modifiersJSON.getInt(CONT_ANIM);
				actionProperties.addModifier(CONT_ANIM, value);
			}
			
			if(modifiersJSON.has(FACE_PLAYER)){
				String value = modifiersJSON.getString(FACE_PLAYER);
				if(value.equals("follow"))
					actionProperties.addModifier(FACE_PLAYER, FACE_PLAYER_FOLLOW);
				else
					actionProperties.addModifier(FACE_PLAYER, FACE_PLAYER_LOOK);
			}
			
			if(modifiersJSON.has(CONT_SPEED)){
				String speed_type = modifiersJSON.getString(CONT_SPEED);
				int value = -1;
				if(speed_type.equals("x"))
					value = CONT_SPEED_X_DIR;
				else if(speed_type.equals("y"))
					value = CONT_SPEED_Y_DIR;
				else
					value = CONT_SPEED_BOTH_DIR;
				actionProperties.addModifier(CONT_SPEED, value);
			}
			
			if(modifiersJSON.has(SNAP_TO_FLOOR)){
				int value = modifiersJSON.getInt(SNAP_TO_FLOOR);
				actionProperties.addModifier(SNAP_TO_FLOOR, value);
			}
		}
		
		if(propertyData.has(TRIGGER_CANCEL)){
			JSONObject cancelJSON = propertyData.getJSONObject(TRIGGER_CANCEL);
			
			if(cancelJSON.has(CANCEL_FRAME)){
				JSONArray value = cancelJSON.getJSONArray(CANCEL_FRAME);
				for(int index = 0; index < value.length(); index++){
					actionProperties.addCancelFrame(value.getInt(index));
				}
			}

			if(cancelJSON.has(TAP_TRIGGER)){
				String value = cancelJSON.getString(TAP_TRIGGER);
				actionProperties.addCancel(TAP_TRIGGER, value);
			}
			
			if(cancelJSON.has(HOLD_PRESS_TRIGGER)){
				String value = cancelJSON.getString(HOLD_PRESS_TRIGGER);
				actionProperties.addCancel(HOLD_PRESS_TRIGGER, value);
			}

			if(cancelJSON.has(HOLD_RELEASE_TRIGGER)){
				String value = cancelJSON.getString(HOLD_RELEASE_TRIGGER);
				actionProperties.addCancel(HOLD_RELEASE_TRIGGER, value);
			}
			
			if(cancelJSON.has(SWIPE_F_TRIGGER)){
				String value = cancelJSON.getString(SWIPE_F_TRIGGER);
				actionProperties.addCancel(SWIPE_F_TRIGGER, value);
			}
			
			if(cancelJSON.has(SWIPE_B_TRIGGER)){
				String value = cancelJSON.getString(SWIPE_B_TRIGGER);
				actionProperties.addCancel(SWIPE_B_TRIGGER, value);
			}
			
			if(cancelJSON.has(SWIPE_U_TRIGGER)){
				String value = cancelJSON.getString(SWIPE_U_TRIGGER);
				actionProperties.addCancel(SWIPE_U_TRIGGER, value);
			}
			
			if(cancelJSON.has(SWIPE_D_TRIGGER)){
				String value = cancelJSON.getString(SWIPE_D_TRIGGER);
				actionProperties.addCancel(SWIPE_D_TRIGGER, value);
			}
			
			if(cancelJSON.has(DTAP_F_TRIGGER)){
				String value = cancelJSON.getString(DTAP_F_TRIGGER);
				actionProperties.addCancel(DTAP_F_TRIGGER, value);
			}
			
			if(cancelJSON.has(DTAP_B_TRIGGER)){
				String value = cancelJSON.getString(DTAP_B_TRIGGER);
				actionProperties.addCancel(DTAP_B_TRIGGER, value);
			}
			
			if(cancelJSON.has(DTAP_U_TRIGGER)){
				String value = cancelJSON.getString(DTAP_U_TRIGGER);
				actionProperties.addCancel(DTAP_U_TRIGGER, value);
			}
			
			if(cancelJSON.has(DTAP_D_TRIGGER)){
				String value = cancelJSON.getString(DTAP_D_TRIGGER);
				actionProperties.addCancel(DTAP_D_TRIGGER, value);
			}
		}
		
		return actionProperties;
	}
	
	public TriggerProperties parseTriggerProperty(JSONArray triggerPropData){
		TriggerProperties prop = new TriggerProperties();
		for(int index = 0; index < triggerPropData.length(); index++){
			JSONObject propEntry;
			try {
				propEntry = triggerPropData.getJSONObject(index);
				String state = propEntry.getString(TRIGGER_PROP_STATE);
				double value = propEntry.getDouble(TRIGGER_PROP_VALUE);
				
				prop.state.add(state);
				prop.value.add(value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return prop;
	}
	
	public TriggerProperties parseTriggerCondProperty(JSONArray triggerPropData){
		TriggerProperties prop = new TriggerProperties();
		
		boolean found_default = false;
		for(int index = 0; index < triggerPropData.length(); index++){
			JSONObject propEntry;
			try {
				propEntry = triggerPropData.getJSONObject(index);
				String cond  = propEntry.getString(TRIGGER_PROP_STATE_COND);
				String state = propEntry.getString(TRIGGER_PROP_STATE);
				
				if(cond.equals(TRIGGER_PROP_DEFAULT))
					found_default = true;
				
				prop.cond_state.put(cond, state);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		Assert.assertTrue(found_default);//Should always have a default go to conditition
		
		return prop;
	}
	
	public InteractionProperties parseInteractionProperties(JSONObject interData) throws JSONException{
		InteractionProperties interProperties = new InteractionProperties();
		
		if(interData.has(HIT_STOP)){
			interProperties.setHitStop(interData.getInt(HIT_STOP));
		}
		
		if(interData.has(HIT_STUN)){
			interProperties.setHitStun(interData.getInt(HIT_STUN));
		}
		
		if(interData.has(TRIGGER_INIT_SPEED)){
			JSONObject triggerInitSpeeds = interData.getJSONObject(TRIGGER_INIT_SPEED);
			if(triggerInitSpeeds.has(GROUND_HIT_TRIGGER)){
				JSONObject value = triggerInitSpeeds.getJSONObject(GROUND_HIT_TRIGGER);
				PointF point = new PointF();
				point.x = (float)value.getDouble(X_INIT_SPEED);
				point.y = (float)value.getDouble(Y_INIT_SPEED);
				interProperties.addTriggerInitSpeed(GROUND_HIT_TRIGGER, point);
			}
			
			if(triggerInitSpeeds.has(GROUND_HIT_COND_TRIGGER)){
				JSONArray speeds = triggerInitSpeeds.getJSONArray(GROUND_HIT_COND_TRIGGER);
				for(int i = 0; i < speeds.length(); ++i){
					JSONObject value = speeds.getJSONObject(i);
					String trigger_state = GROUND_HIT_COND_TRIGGER+"_"+value.getString(TRIGGER_PROP_STATE);
					PointF point = new PointF();
					point.x = (float)value.getDouble(X_INIT_SPEED);
					point.y = (float)value.getDouble(Y_INIT_SPEED);
					
					interProperties.addTriggerInitSpeed(trigger_state, point);
				}
			}
			
			if(triggerInitSpeeds.has(AIR_HIT_TRIGGER)){
				JSONObject value = triggerInitSpeeds.getJSONObject(AIR_HIT_TRIGGER);
				PointF point = new PointF();
				point.x = (float)value.getDouble(X_INIT_SPEED);
				point.y = (float)value.getDouble(Y_INIT_SPEED);
				interProperties.addTriggerInitSpeed(AIR_HIT_TRIGGER, point);
			}
			
			if(triggerInitSpeeds.has(AIR_HIT_COND_TRIGGER)){
				JSONArray speeds = triggerInitSpeeds.getJSONArray(AIR_HIT_COND_TRIGGER);
				for(int i = 0; i < speeds.length(); ++i){
					JSONObject value = speeds.getJSONObject(i);
					String trigger_state = AIR_HIT_COND_TRIGGER+"_"+value.getString(TRIGGER_PROP_STATE);
					PointF point = new PointF();
					point.x = (float)value.getDouble(X_INIT_SPEED);
					point.y = (float)value.getDouble(Y_INIT_SPEED);
					
					interProperties.addTriggerInitSpeed(trigger_state, point);
				}
			}
		}
		
		return interProperties;
	}
}
