package ru.uzen.android.theme;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.util.Log;

import java.lang.Exception;

public class App extends BaseActivity implements View.OnClickListener{
	private static final String TAG = "APP";
	
	private static final String ACTION_APPLY_ICON_THEME = "com.teslacoilsw.launcher.APPLY_ICON_THEME";
    private static final String NOVA_PACKAGE = "com.teslacoilsw.launcher";
    private static final String EXTRA_ICON_THEME_PACKAGE = "com.teslacoilsw.launcher.extra.ICON_THEME_PACKAGE";
    
    private static final int UPDATE_CONTENT_INTRO = 0x1;
    private static final int UPDATE_CONTENT_CHANGELOG = 0x2;
    private static final int UPDATE_CONTENT_ERROR = 0x3;
    
    private static final int BTN_APPLY_THEME = BUTTONS_LAYOUT_ID;
    private static final int BTN_CHANGELOG   = BUTTONS_LAYOUT_ID + 0x00000001;
    
    private View rootView;
    private Context context;
    
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_CONTENT_INTRO:			
					rootView = (LinearLayout) LinearLayout.inflate(context, R.layout.app_activity, null);
					setContent(rootView);
					break;
				case UPDATE_CONTENT_CHANGELOG:
					Button btn_ch= getFooterButtonId(BTN_CHANGELOG ^ BUTTONS_LAYOUT_ID);

					if(!btn_ch.getText().equals(getString(R.string.changelog))) {
			    			btn_ch.setText(R.string.changelog);
			    			mHandler.sendEmptyMessage(UPDATE_CONTENT_INTRO);
			    			break;
			    	}
			    	btn_ch.setText(R.string.intro);
			    	
					rootView = (LinearLayout) LinearLayout.inflate(context, R.layout.app_changelog, null);
					setContent(rootView);
					break;
				case UPDATE_CONTENT_ERROR:
					Bundle bundle = msg.getData();
					
					rootView = (LinearLayout) LinearLayout.inflate(context, R.layout.app_error, null);
					((TextView) rootView.findViewById(R.id.error_title))
							.setText("Error " + String.valueOf(bundle.getInt("code")));
					((TextView) rootView.findViewById(R.id.error_msg))
							.setText(bundle.getString("error"));
					setContent(rootView);	
					break;
			}			
		};
	};
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		
		try {
	        PackageManager pm = getPackageManager();
	        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
	        setHeaderViewText(getString(R.string.theme_name) + " " + String.valueOf(packageInfo.versionCode));
    	} catch (Exception e) {
    	}
	
    	mHandler.sendEmptyMessage(UPDATE_CONTENT_INTRO);
		Log.d(TAG, "#Create View#");
	}
     
    @Override
    public void initData() {	
    	int n = -1;
    	
    	final int APPLY_THEME = BTN_APPLY_THEME ^ BUTTONS_LAYOUT_ID;
    	final int CHANGELOG = BTN_CHANGELOG ^ BUTTONS_LAYOUT_ID;
    	
    	while(n++ < BUTTONS_MAX_NUM - 1) {
    		Button btn = getFooterButtonId(n);
    		
    		switch(n) {
    			case APPLY_THEME:
    				btn.setText(R.string.apply_icon_theme);
    				btn.setId(BTN_APPLY_THEME);
    				break;
    			case CHANGELOG:
    				btn.setText(R.string.changelog);
    				btn.setId(BTN_CHANGELOG);
    				break;
    		}
    		btn.setOnClickListener(this);
    		Log.d(TAG, "init footer button number=" + n);
    	}
    }
    
    @Override
	public void onClick(View v) {
		switch(v.getId()) {
			case BTN_APPLY_THEME:
				Log.d(TAG, "click button 'APPLY_THEME'");
				showDialogApplyTheme();
				break;
			case BTN_CHANGELOG:
				Log.d(TAG, "click button 'CHANGELOG'");
				sendAction(UPDATE_CONTENT_CHANGELOG);
				break;
		}
	}
    
    private void showDialogApplyTheme() {
    	
    	if(!isAvailablePackage(NOVA_PACKAGE)){
			sendError(0x403, "package '" + NOVA_PACKAGE + "' is not available");
			return;
		}
		
    	try {
	        Intent intent = new Intent(ACTION_APPLY_ICON_THEME);
	        intent.setPackage(NOVA_PACKAGE);
	        intent.putExtra(EXTRA_ICON_THEME_PACKAGE, getPackageName());
	
	        startActivity(intent);
    	} catch (Exception e) {
			sendError(0x404, e.getMessage());
    	}
    }
    
    private void sendError(int code, String msg) {
    	Log.e(TAG, "error code=" + String.valueOf(code) + " message=" + msg);
    	Bundle bundle = new Bundle();
    	bundle.putInt("code", code);
    	bundle.putString("error", msg);
    	sendAction(UPDATE_CONTENT_ERROR, bundle);
    }
    
    private void sendAction(int action) {
        sendAction(action, 0, 0, null);
    }
    
    private void sendAction(int action, Bundle bundle) {
        sendAction(action, 0, 0, bundle);
    }
    
    private void sendAction(final int action, final int arg1, final int arg2, final Bundle bundle) {
    	Log.d(TAG, "Action=" + String.valueOf(action) + 
    			   " arg1=" + String.valueOf(arg1) + 
    			   " arg2=" + String.valueOf(arg2) +  		
    			   " isBundle=" + (bundle != null));
    			   	   
    	if (mHandler != null) {
    		new Thread(new Runnable() {
    			@Override
				public void run() {
		    		Message msg = new Message();
		    		msg.what = action;
		    		msg.arg1 = arg1;
		    		msg.arg2 = arg2;
		    		
		    		if(bundle != null)
		    			msg.setData(bundle);
		    		
		    		mHandler.sendMessage(msg);
		    	}
		    }).start();
    	}
    } 
    
    private boolean isAvailablePackage(String pkg) {
    	return true;
    }
}