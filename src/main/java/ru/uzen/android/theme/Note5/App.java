package ru.uzen.android.theme.Note5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.Exception;

public class App extends BaseActivity {
	
	private static final String ACTION_APPLY_ICON_THEME = "com.teslacoilsw.launcher.APPLY_ICON_THEME";
    private static final String NOVA_PACKAGE = "com.teslacoilsw.launcher";
    private static final String EXTRA_ICON_THEME_PACKAGE = "com.teslacoilsw.launcher.extra.ICON_THEME_PACKAGE";
     
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout rootView = new LinearLayout(this);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setLayoutParams(createLinear(MATCH_PARENT, MATCH_PARENT));
		TextView headerText = new TextView(this);
		headerText.setText(R.string.theme_name);
		headerText.setTextSize(dp(18));
		rootView.addView(headerText, createLinear(WRAP_CONTENT, WRAP_CONTENT));

		ImageView iconImage = new ImageView(this);
		iconImage.setImageResource(R.drawable.preview);
		rootView.addView(iconImage, createLinear(MATCH_PARENT, MATCH_PARENT, 1, Gravity.CENTER_HORIZONTAL, 0, 8, 0, 0));
		
    	setContent(rootView);
	}
     
    @Override
    public void initData() {	
    	getFooterButtonId(BTN_EXIT).setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			onClickAppExit();
    		}
    	});
        getFooterButtonId(BTN_APPLY).setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			onClickApplyTheme();
    		}
    	});
    }
    
    private void onClickApplyTheme() {
    	try {
	        Intent intent = new Intent(ACTION_APPLY_ICON_THEME);
	        intent.setPackage(NOVA_PACKAGE);
	        intent.putExtra(EXTRA_ICON_THEME_PACKAGE, getPackageName());
	
	        startActivity(intent);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
        finish();
    }
    
    private void onClickAppExit() {
    	finish();
    }
}