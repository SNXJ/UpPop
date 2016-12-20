package com.zysk.uppopwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zysk.uppopwindow.MyPopWindowUntil.OnActionListener;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

/**
 * path dialog组件 <br/>
 * <br/>
 * <b>示例:<br/>
 * new PathPopupWindowDialog(Context context, int screenHeight, View showBtn)<br/>
 * &nbsp;&nbsp; &nbsp;.create(int itemIconIds[],String itemDescs[],
 * AdapterView.OnItemClickListener listener);<br/>
 * <br/>
 * 参数说明:<br/>
 * context ---- 一般是activity<br/>
 * screenHeight ---- 屏幕高度<br/>
 * showBtn ---- 用来PopupWindow展示，例如Button，点击此button会显示dialog<br/>
 * itemIconIds ---- ListView中每一个item的图标<br/>
 * itemDescs ---- ListView中每一个item的图标对应的说明<br/>
 * listener ---- ListView中每一个item需要响应的操作，需要用户自己实现。<br/>
 * 关于ListView中的值是通过SimpleAdapter设置的，其中SimpleAdapter接收的List<HashMap<String,
 * Object>><br/>
 * map中存放的key是图标是"icon"[ITEM_ICON]和文本是"text"[ITEM_TEXT]，用户在实现listener的时候可能需要用到
 * </b>
 * 
 * 
 */
@SuppressLint("InflateParams")
public class PathPopupWindowDialog {

	public  final String ITEM_TEXT = "text";

	/**
	 * popupWindow
	 */
	private PopupWindow popupWindow;
	/**
	 * 确定dialog需要的context
	 */
	private  Context context;

	/**
	 * 手机屏幕的高度
	 */
	private  int screenHeight=0;

	/**
	 * 确定触发该dialog的view组件，也是创建popupwindow必须的参数
	 */
	private  View parentView;
	
	private OnActionListener listener;
	
	public PathPopupWindowDialog(Context context) {
		super();
		this.context = context;
	}
	public PathPopupWindowDialog(Context context,int screenHeight) {
		super();
		this.context = context;
		this.screenHeight=screenHeight;
	}
	public PathPopupWindowDialog(Context context,int screenHeight,OnActionListener listener) {
		super();
		this.context = context;
		this.screenHeight=screenHeight;
		this.listener = listener;
	}

	/**
	 * 创建PopupWindow
	 * 
	 * @param itemIconIds
	 *            ListView中的图标
	 * @param itemDescs
	 *            ListView中图标对应的描述
	 * @param responseListener
	 *            响应listview中的item click listener 事件
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void create(String itemDescs[],AdapterView.OnItemClickListener responseListener) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View popupView = inflater.inflate(R.layout.p_option_popup, null);
		// 从popu.xml中获得其中的组件
		ListView listView = (ListView) popupView
				.findViewById(R.id.path_popupwindow_listview);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 取消按钮
		Button bt_my_candel = (Button) popupView.findViewById(R.id.bt_my_cancel);

		// 获得popup中最外层的layout
		//RelativeLayout relativeLayout = (RelativeLayout) popupView
			//	.findViewById(R.id.p_popup_layout);
		//this.setBackListener(relativeLayout);

		// 设置ListView中的参数属性
		this.setListViewParamters(listView, itemDescs, responseListener);
	if(screenHeight!=0){
		// 获得屏幕的高度,设置ListView的最大高度,动态设置ListView的高度
		this.setListViewHeightBasedOnChildren(listView, screenHeight);/////////////////////////
	}

		// 生成PopupWindow对象
		popupWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		// 设置PopupWindow对象需要属性参数
	//	popupWindow
			//	.setAnimationStyle(R.style.path_popwindow_anim_enterorout_window);
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);

		ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
		popupWindow.setBackgroundDrawable(dw);
		// 设置popupwindow以外的区域是否可以触摸
		popupWindow.setOutsideTouchable(true);

		// 设置android返回事件，用来隐藏popupwindow
		// 不给LinearLayout设置的原因是因为给其加上后，ListView不能获得焦点
		this.setBackListener(listView);
		
		bt_my_candel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		popupView.setOnTouchListener(new OnTouchListener() {  
             
	            @SuppressLint("ClickableViewAccessibility")
				public boolean onTouch(View v, MotionEvent event) {  
	                int height = popupView.findViewById(R.id.pop_ll_layout).getTop();  
	                int y=(int) event.getY();  
	                if(event.getAction()==MotionEvent.ACTION_UP){  
	                    if(y<height){  
	                        dismiss();  
	                    }  
	                }                 
	                return true;  
	            }  
	        });  
		
	}
	public void dismiss() {
		if(listener != null){
			listener.onActionListener();
		}
		popupWindow.dismiss();
	}

	/**
	 * 设置ListView里面的参数属性
	 * 
	 * @param listView
	 * @param itemDescs
	 *            ListView中图标对应的描述
	 * @param responseListener
	 *            响应listview中的item click listener 事件
	 */
	private void setListViewParamters(ListView listView, String itemDescs[],
			AdapterView.OnItemClickListener responseListener) {
		// 给popu.xml中ListView设置值
		List<HashMap<String, Object>> itemList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < itemDescs.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(ITEM_TEXT, itemDescs[i]);
			itemList.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(context, itemList,
				R.layout.p_option_item, new String[] { "text" },
				new int[] { R.id.path_dynamic_popu_item_textView });
		listView.setFocusable(true);
		listView.requestFocus();
		listView.setAdapter(simpleAdapter);
		listView.setItemsCanFocus(true);
		listView.setDividerHeight(1);

		// 设置ListView中的item点击事件
		listView.setOnItemClickListener(responseListener);
	}

	/**
	 * 给parentView设置onclicklistener事件，用于显示popupwindow
	 * 
	 * 
	 */
	@SuppressWarnings("unused")
	private class ShowPopupWindowListener implements View.OnClickListener {
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			popupWindow.update();
			
		}
	}
public PopupWindow getPop(){
	
	return popupWindow;
}
	/**
	 * 为PopupWindow中的最外层的布局设置onkeylistener事件 用来隐藏弹出的popupwindow
	 * 
	 * @param layout
	 *            为PopupWindow中的最外层的布局
	 * @param pw
	 *            PopupWindow
	 */
	private void setBackListener(View view) {
		view.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
				}
				return false;
			}
		});
	}

	/**
	 * 动态设置listview的高度
	 * 
	 * @param listView
	 *            需要配置的ListView
	 * @param screenHeight
	 *            屏幕的高度
	 * @param imageHeight
	 *            imageHeight
	 * @param viewHeight
	 *            viewHeight
	 */
	@SuppressWarnings("unused")
	private void setListViewHeightBasedOnChildren(ListView listView,
			int screenHeigh) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);

			// 计算子项View 的宽高
			listItem.measure(0, 0);

			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();

		// listView.getDividerHeight()获取子项间分隔符占用的高度
		int listViewHeight = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// 如果总体高度大于屏幕高度，则用屏幕高度
		if (listViewHeight > screenHeight) {
			// 此处的30是实验所得出的结果
			params.height = screenHeight - 75;
		}
		// 否则设置为计算出来的高度
		else {
			params.height = listViewHeight;
		}
		listView.setLayoutParams(params);
	}
}
