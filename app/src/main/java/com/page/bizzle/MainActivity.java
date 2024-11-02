package com.page.bizzle;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import 
com.page.bizzle.R;

public class MainActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private double s = 0;
	
	private LinearLayout linear1;
	private EditText edittext1;
	private LinearLayout box;
	private TextView TextView1;
	private LinearLayout LinearLayout3;
	private LinearLayout LinearLayout4;
	private SeekBar SeekBar1;
	private TextView TextView2;
	private TextView TextView3;
	
	private SharedPreferences bizzle;
	private TimerTask t;
	private Intent in = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		edittext1 = findViewById(R.id.edittext1);
		box = findViewById(R.id.box);
		TextView1 = findViewById(R.id.TextView1);
		LinearLayout3 = findViewById(R.id.LinearLayout3);
		LinearLayout4 = findViewById(R.id.LinearLayout4);
		SeekBar1 = findViewById(R.id.SeekBar1);
		TextView2 = findViewById(R.id.TextView2);
		TextView3 = findViewById(R.id.TextView3);
		bizzle = getSharedPreferences("last", Activity.MODE_PRIVATE);
		
		edittext1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				bizzle.edit().putString("last", _charSeq).commit();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		SeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				TextView1.setTextSize((float)_progressValue);
				bizzle.edit().putString("size", String.valueOf((long)(_progressValue))).commit();
				s = _progressValue;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
		
		TextView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				box.setVisibility(View.GONE);
				edittext1.setVisibility(View.VISIBLE);
				bizzle.edit().putString("size", "16").commit();
				edittext1.setTextSize((float)16);
			}
		});
		
		TextView3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (s == 0) {
					
				}
				else {
					edittext1.setTextSize((float)s);
				}
				box.setVisibility(View.GONE);
				edittext1.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void initializeLogic() {
		if (bizzle.getString("size", "").equals("")) {
			
		}
		else {
			edittext1.setTextSize((float)Double.parseDouble(bizzle.getString("size", "")));
		}
		if (bizzle.getString("last", "").equals("")) {
			
		}
		else {
			edittext1.setText(bizzle.getString("last", ""));
		}
		box.setVisibility(View.GONE);
		box.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF424242));
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		
		menu.add("Font size");
		menu.add("Copy");
		menu.add("Share");
		menu.add("Source code");
		menu.add("Clear");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(final MenuItem item){
		
		switch(item.getTitle().toString()){
			
			
			case"Font size":
			edittext1.setVisibility(View.GONE);
			box.setVisibility(View.VISIBLE);
			break;
			case"Copy":
			((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", edittext1.getText().toString()));
			break;
			case"Share":
			Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, edittext1.getText().toString()); startActivity(Intent.createChooser(i,"Page"));
			break;
			case"Source code":
			in.setAction(Intent.ACTION_VIEW);
			in.setData(Uri.parse("https://github.com/8izzle/page"));
			startActivity(in);
			break;
			case"Clear":
			edittext1.setText("");
			break;
			 
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if (bizzle.getString("exit", "").equals("true")) {
			finishAffinity();
		}
		else {
			bizzle.edit().putString("exit", "true").commit();
			t = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							bizzle.edit().putString("exit", "").commit();
						}
					});
				}
			};
			_timer.schedule(t, (int)(2000));
			BizzleUtil.showMessage(getApplicationContext(), "Press back again to exit.");
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
