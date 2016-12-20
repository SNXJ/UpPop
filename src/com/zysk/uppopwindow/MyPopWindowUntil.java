package com.zysk.uppopwindow;

import java.util.HashMap;


import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

public class MyPopWindowUntil {
	private  Context context;
	private  View view;
	private  TextView textView;
	private String[] itemDescs;
	private  PathPopupWindowDialog pop;
	private int screenHeight;
	public OnActionListener listener;
	public SelectPicListener picListener;

/**
 *  pop选择弹窗  并回显
 * @param context
 * @param view  被点击的View
 * @param textView 需要回显text的TextView
 * @param itemDescs  选择条目
 */
	public MyPopWindowUntil(Context context, View view, TextView textView,
			String[] itemDescs) {
		this.context = context;
		this.view = view;
		this.textView = textView;
		this.itemDescs = itemDescs;
		selectDisplay();
	}
	public MyPopWindowUntil(Context context, View view, TextView textView,
			String[] itemDescs , OnActionListener listener) {
		this.context = context;
		this.view = view;
		this.textView = textView;
		this.itemDescs = itemDescs;
		this.listener = listener;
		selectDisplay(listener);
	}
	public MyPopWindowUntil(Context context, View view, 
			String[] itemDescs , SelectPicListener picListener) {
		this.context = context;
		this.view = view;
		this.itemDescs = itemDescs;
		this.picListener = picListener;
		selectDisplay();
	}
	public MyPopWindowUntil(Context context, View view, TextView textView,
			String[] itemDescs,int screenHeight) {
		this.context = context;
		this.view = view;
		this.textView = textView;
		this.itemDescs = itemDescs;
		this.screenHeight=screenHeight;
		selectDisplay();
	}
	public interface  OnActionListener{
		abstract void onActionListener();
	}
	
	public interface  SelectPicListener{
		abstract void onActionListener(int i);
	}
	
	private  void selectDisplay() {
		pop = new PathPopupWindowDialog(context,screenHeight);
		pop.create(itemDescs,new ResponseListener());
		pop.getPop().showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}
	private  void selectDisplay(OnActionListener listener) {
		pop = new PathPopupWindowDialog(context,screenHeight,listener);
		pop.create(itemDescs,new ResponseListener());
		pop.getPop().showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private class ResponseListener implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int id,
				long position) {
			System.out.println(view + "----");
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map = (HashMap<String, Object>) adapter
					.getItemAtPosition((int) position);
			if(textView != null){
				textView.setText(map.get("text").toString());
			}
			if(null != picListener){
				picListener.onActionListener((int) position);
			}
			pop.dismiss();
		}
	}

}
