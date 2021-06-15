package com.example.smarthome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteActivity extends Activity{
	
    private Button btn_delete;	//注销按钮
    private EditText de_user_name,de_pwd;
    private String username,pwd,spPwd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_delete);
        init();
        
    }

	private void init() {
		// TODO Auto-generated method stub
		//从main_title_bar.xml页面布局中获取对应的UI控件
        //抽取成员变量ctrl+alt+F

        //从activity_register.xml页面布局中获取对应的UI控件
        btn_delete = (Button) findViewById(R.id.btn_delete);
        de_user_name = (EditText) findViewById(R.id.de_user_name);
        de_pwd = (EditText) findViewById(R.id.de_pwd);
        
        //注销按钮点击事件
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	username=de_user_name.getText().toString().trim();
				pwd=de_pwd.getText().toString().trim();
				spPwd=readPwd(username);
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(DeleteActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(DeleteActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(pwd.equals(spPwd)){
                    Toast.makeText(DeleteActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                    DeteleInfo(username);
                    DeleteActivity.this.finish();//在登录的时候，如果用户还没有注册则注册。注册成功后把注册成功后的用户名返回给前一个页面
                    startActivity(new Intent(DeleteActivity.this, LoginActivity.class));
                    return;
                }else if((!TextUtils.isEmpty(spPwd)&&!pwd.equals(spPwd))){
                	Toast.makeText(DeleteActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                	Toast.makeText(DeleteActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
				}
            }
        });
    }

	private String readPwd(String username){
		SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
	    Cursor cursor=db.rawQuery("select * from user where username = ?",new String[]{username});
	    String pwd = null;
	    while(cursor.moveToNext()){
	    	pwd = cursor.getString(2);
	    }
	    return pwd;
	}
	
	private void DeteleInfo(String username) {
   	 SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
   	 db.execSQL("delete from user where username = ?",new String[]{username});
   	 db.close();
   }

}
