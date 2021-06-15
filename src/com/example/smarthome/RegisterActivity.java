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

public class RegisterActivity extends Activity{
	
    private Button btn_register;	//注册按钮
    private EditText et_user_name,et_pwd,et_pwd_again;//用户名、密码、再次输入的密码的控件
    private String username,pwd,pwd_again;//用户名、密码、再次输入的密码的控件的获取值
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_register);
        init();
        
    }

	private void init() {
		// TODO Auto-generated method stub
		//从main_title_bar.xml页面布局中获取对应的UI控件
        //抽取成员变量ctrl+alt+F

        //从activity_register.xml页面布局中获取对应的UI控件
        btn_register = (Button) findViewById(R.id.btn_register);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_again = (EditText) findViewById(R.id.et_pwd_again);
        //注册按钮点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击后获取输入在响应控件中的字符串
                getEditstring();
                //判断字符串是否为空
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd_again)){
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!pwd.equals(pwd_again)){
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isExistUserName(username)){
                    Toast.makeText(RegisterActivity.this, "此用户已经存在", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    
                    saveRegisterInfo(username,pwd);
                    
                    //注册成功后通过Intent把用户名传递到LoginActivity.java中
                    Intent data=new Intent();
                    data.putExtra("username",username);
                    setResult(RESULT_OK,data);//setResult为OK，关闭当前页面
                    RegisterActivity.this.finish();//在登录的时候，如果用户还没有注册则注册。注册成功后把注册成功后的用户名返回给前一个页面
                    }
            }
        });
    }

    private void saveRegisterInfo(String username, String pwd) {
    	 SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);

    	 db.execSQL("INSERT INTO user VALUES (NULL, ?, ?)", new Object[]{username,pwd});
    	 db.close();
    }

    private boolean isExistUserName(String username) {
       SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
       Cursor cursor=db.rawQuery("select * from user where username = ?",new String[]{username});
      
       if(cursor.getCount()==0){
    	   cursor.close();
    	   db.close();
    	   return false;
       }else{
    	   cursor.close();
    	   db.close();
    	   return true;
       }
        
    }

    private void getEditstring() {
        username=et_user_name.getText().toString().trim();
        pwd = et_pwd.getText().toString();
        pwd_again = et_pwd_again.getText().toString().trim();
	}
}
