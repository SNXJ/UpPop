package com.zysk.uppopwindow;

import java.util.HashMap;


import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
/**
 * 测试
 * @author sxj
 *
 */
public class TextActivity extends Activity {
	final String OrgTypeDescs[] = new String[] { "有限责任公司", "股份有限公司", "个人独资企业",
	"个体工商户" };
	private  PathPopupWindowDialog pop;
 private TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	final Button bt=(Button) findViewById(R.id.bt_show);
	 tv=(TextView) findViewById(R.id.tv);
	
	bt.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			new MyPopWindowUntil(TextActivity.this, bt, tv, OrgTypeDescs);
		}
	});
		
	}
	
	
}
