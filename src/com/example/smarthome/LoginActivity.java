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

public class LoginActivity extends Activity{
	
    private TextView tv_register,tv_delete;//立即注册、找回密码的控件
    private Button btn_login;	//登录按钮
    private EditText et_user_name,et_pwd;//用户名、密码的控件
    private String username,pwd,spPwd;//用户名、密码的控件的获取值
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_login);
    	//设置此界面为竖屏
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	init();
    }

	private void init() {
		// TODO Auto-generated method stub
		
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
     
        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivityForResult(intent, 1);
			}
		});
        //注销用户点击事件
        tv_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LoginActivity.this,DeleteActivity.class);
				startActivity(intent);
			}
		});
        //登录按钮点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				username=et_user_name.getText().toString().trim();
				pwd=et_pwd.getText().toString().trim();
				spPwd=readPwd(username);
				if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(pwd.equals(spPwd)){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                   
                    LoginActivity.this.finish();//在登录的时候，如果用户还没有注册则注册。注册成功后把注册成功后的用户名返回给前一个页面
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    return;
                }else if((!TextUtils.isEmpty(spPwd)&&!pwd.equals(spPwd))){
                	Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                	Toast.makeText(LoginActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	//根据用户名读取密码
	public String readPwd(String username){
		SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
		
	    Cursor cursor=db.rawQuery("select * from user where username = ?",new String[]{username});
	    String pwd = null;
	    while(cursor.moveToNext()){
	    	pwd = cursor.getString(2);
	    }
	    return pwd;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode,int resultCode,
			Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			//从注册界面传递过来的用户名
			String username=data.getStringExtra("username");
			if(!TextUtils.isEmpty(username)){
				et_user_name.setText(username);
				//设置光标的位置上
				et_user_name.setSelection(username.length());
			}
		}
	}
}
