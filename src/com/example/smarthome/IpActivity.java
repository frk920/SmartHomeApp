package com.example.smarthome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IpActivity extends Activity{
	    private Button btn_connect;
	    private EditText et_ip,et_port;
	    private String ip,port;
	    private Button back;
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.activity_ip);
	    	//设置此界面为竖屏
	    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    	init();
	    	back = (Button) findViewById(R.id.back);
	    	back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(IpActivity.this,MainActivity.class);
	            	startActivity(intent);
				}
			});
	    	
	    }

		private void init() {
			// TODO Auto-generated method stub
			
	        btn_connect = (Button) findViewById(R.id.btn_connect);
	        et_ip = (EditText) findViewById(R.id.et_ip);
	        et_port = (EditText) findViewById(R.id.et_port);
	     
	        //按钮点击事件
	        btn_connect.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ip=et_ip.getText().toString().trim();
			        port = et_port.getText().toString();
					if(TextUtils.isEmpty(ip)){
	                    Toast.makeText(IpActivity.this, "请输入ip地址", Toast.LENGTH_SHORT).show();
	                    return;
	                }else if (TextUtils.isEmpty(port)){
	                    Toast.makeText(IpActivity.this, "请输入端口号", Toast.LENGTH_SHORT).show();
	                    return;
	                }else{
	                	SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
				    	db.execSQL("update ip set ip = ?,port = ? where id = ?", new Object[]{ip,port,1});
				    	db.close();
				    	Toast.makeText(IpActivity.this, "设置连接成功", Toast.LENGTH_SHORT).show();
				    	Intent intent=new Intent(IpActivity.this,MainActivity.class);
						startActivity(intent);
	                }
				}
			});
		}
}
