package org.touchirc.activity;

import org.touchirc.R;

import android.os.Bundle;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseSlidingActivity extends SlidingFragmentActivity {

	private int mTitleRes;

	public BaseSlidingActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);
		
		// set the content view
        setContentView(R.layout.conversation_display);
           
        // configure the SlidingMenu
        getSlidingMenu().setMode(SlidingMenu.LEFT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        getSlidingMenu().setShadowWidthRes(R.dimen.sliding_shadow_width);
        getSlidingMenu().setShadowDrawable(R.drawable.shadow);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
        getSlidingMenu().setFadeDegree(0.35f);
        getSlidingMenu().attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		getSlidingMenu().setMenu(R.layout.connected_servers);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	

}
