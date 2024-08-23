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
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private LinearLayout linear1;
	private EditText edittext1;
	
	private SharedPreferences bizzle;
	private TimerTask t;
	
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
	}
	
	private void initializeLogic() {
		if (bizzle.getString("last", "").equals("")) {
			
		}
		else {
			edittext1.setText(bizzle.getString("last", ""));
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add("Share")
		.setIcon(R.drawable.share)
		 .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add("Copy")
		.setIcon(R.drawable.copy)
		 .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(final MenuItem item){
		 switch (item.getTitle().toString()) {
			case "Copy":
			
			((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", edittext1.getText().toString()));
			
			showMessage("Copied");
			
			break;
			case "Share":
			
			Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, edittext1.getText().toString()); startActivity(Intent.createChooser(i,"Page"));
			showMessage("Sharing");
			return true;
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
			SketchwareUtil.showMessage(getApplicationContext(), "Press back again to exit.");
		}
	}
	public void _variableName() {
	}
	private ZoomableLinearLayout zoomLinearLayout;
	{
	}
	
	
	public void _library() {
	}
	public static class ZoomableLinearLayout extends LinearLayout implements ScaleGestureDetector.OnScaleGestureListener {
		
		    private enum Mode {
			        NONE,
			        DRAG,
			        ZOOM
			    }
		
		    private static final float MIN_ZOOM = 1.0f;
		    private static final float MAX_ZOOM = 4.0f;
		
		    private Mode mode = Mode.NONE;
		    private float scale = 1.0f;
		    private float lastScaleFactor = 0f;
		
		    private float startX = 0f;
		    private float startY = 0f;
		
		    private float dx = 0f;
		    private float dy = 0f;
		    private float prevDx = 0f;
		    private float prevDy = 0f;
		
		    public ZoomableLinearLayout(Context context) {
			        super(context);
			        init(context);
			    }
		
		    public void init(Context context) {
			        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
			        this.setOnTouchListener(new OnTouchListener() {
				            @Override
				            public boolean onTouch(View view, MotionEvent motionEvent) {
					                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
						                    case MotionEvent.ACTION_DOWN:
						                        if (scale > MIN_ZOOM) {
							                            mode = Mode.DRAG;
							                            startX = motionEvent.getX() - prevDx;
							                            startY = motionEvent.getY() - prevDy;
							                        }
						                        break;
						                    case MotionEvent.ACTION_MOVE:
						                        if (mode == Mode.DRAG) {
							                            dx = motionEvent.getX() - startX;
							                            dy = motionEvent.getY() - startY;
							                        }
						                        break;
						                    case MotionEvent.ACTION_POINTER_DOWN:
						                        mode = Mode.ZOOM;
						                        break;
						                    case MotionEvent.ACTION_POINTER_UP:
						                        mode = Mode.DRAG;
						                        break;
						                    case MotionEvent.ACTION_UP:
						                        mode = Mode.NONE;
						                        prevDx = dx;
						                        prevDy = dy;
						                        break;
						                }
					                scaleDetector.onTouchEvent(motionEvent);
					
					                if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
						                    getParent().requestDisallowInterceptTouchEvent(true);
						                    float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
						                    float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;
						                    dx = Math.min(Math.max(dx, -maxDx), maxDx);
						                    dy = Math.min(Math.max(dy, -maxDy), maxDy);
						                    applyScaleAndTranslation();
						                }
					
					                return true;
					            }
				        });
			    }
		
		    @Override
		    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
			        return true;
			    }
		
		    @Override
		    public boolean onScale(ScaleGestureDetector scaleDetector) {
			        float scaleFactor = scaleDetector.getScaleFactor();
			        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
				            scale *= scaleFactor;
				            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
				            lastScaleFactor = scaleFactor;
				        } else {
				            lastScaleFactor = 0;
				        }
			        return true;
			    }
		
		    @Override
		    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
			    }
		
		    private void applyScaleAndTranslation() {
			        child().setScaleX(scale);
			        child().setScaleY(scale);
			        child().setTranslationX(dx);
			        child().setTranslationY(dy);
			    }
		
		    private View child() {
			        return getChildAt(0);
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