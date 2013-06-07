package org.touchirc.view;

import org.touchirc.R;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

public class MultipleChannelTextView extends MultiAutoCompleteTextView{
	
	public MultipleChannelTextView(Context context) {
		super(context);
		init(context);
	}
	
	public MultipleChannelTextView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	public MultipleChannelTextView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void init(Context context){
		addTextChangedListener(watcher);
	}
	
	private TextWatcher watcher = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {	
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(count >=1){
				if(s.charAt(start) == ' ')
					setChannel(getText().toString().trim().split(" "));
			}
		}
	};
	
	public void setChannel(String[] channels){
		if(getText().toString().contains(" ")){
			SpannableStringBuilder ssb = new SpannableStringBuilder(getText());
			
			int x = 0;
			for (String c : channels){
				LayoutInflater l = (LayoutInflater) getContext().getSystemService(SherlockActivity.LAYOUT_INFLATER_SERVICE);
				TextView tv = (TextView) l.inflate(R.layout.channel_edittext, null);
				tv.setText(c);
				int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
				tv.measure(spec, spec);
				tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
				Bitmap b = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(b);
				canvas.translate(-tv.getScrollX(), -tv.getScrollY());
				tv.draw(canvas);
				tv.setDrawingCacheEnabled(true);
				Bitmap cacheBmp = tv.getDrawingCache();
				Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
				tv.destroyDrawingCache();
				BitmapDrawable bmd = new BitmapDrawable(getResources(), viewBmp);
				bmd.setBounds(0, 0, bmd.getIntrinsicWidth(), bmd.getIntrinsicHeight());
				ssb.setSpan(new ImageSpan(bmd), x, x + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				x = x + c.length() +1;
			}
			
			setText(ssb);
			setSelection(getText().length());
		}
	}
}
