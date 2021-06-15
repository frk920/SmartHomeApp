package com.example.smarthome;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// 设置此界面为竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
		Cursor c=db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='user'", null);
		c.moveToFirst();
		//		int i=0;
//		if(c==null){
//			Toast.makeText(SplashActivity.this,i,
//	                Toast.LENGTH_SHORT).show();
//		}else{
//			Toast.makeText(SplashActivity.this,i+1,
//	                Toast.LENGTH_SHORT).show();
//		}
//		while(c.moveToNext()){
//	    	i++;
//	    }
		
		if (c.getInt(0)==0) {
			db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR UNIQUE, passaword VARCHAR)");
			db.execSQL("INSERT INTO user VALUES (NULL, ?, ?)", new Object[]{"admin","123456"});
		}
		c=db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='ip'", null);
		c.moveToFirst();
		if (c.getInt(0)==0) {
			db.execSQL("CREATE TABLE ip (id INTEGER PRIMARY KEY AUTOINCREMENT, ip VARCHAR, port VARCHAR)");
			db.execSQL("INSERT INTO ip VALUES (NULL, ?, ?)", new Object[]{"192.168.123.114","8887"});
		}
		c.close();  
		db.close();
		
		TextView tv_version=(TextView)findViewById(R.id.tv_version);
		tv_version.setText("智能家居控制终端");
		// 利用timer让此界面延迟3秒后跳转，timer有一个线程，这个线程不断执行task
		Timer timer = new Timer();// Timer类是JDK中提供的一个定时器功能，使用时会在主线程之外开启一个单独的线程执行指定任务，任务可以执行一次或者多次
		// TimerTask实现runnable接口，TimerTask类表示在一个指定时间内执行的task
		TimerTask task = new TimerTask() {
			@Override
			public void run() {// 跳转主界面的任务代码写在TimerTask的run()方法中
				Intent intent = new Intent(SplashActivity.this,
						LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		};
		timer.schedule(task, 3000);// timer.schedule用于开启TimerTask类
									// 传递两个参数，第一个参数为TimerTask的对象，第二个参数为TimerTask和run()之间的时间差为3秒。
		// 设置这个task在延迟3秒后自动执行
	}

}
