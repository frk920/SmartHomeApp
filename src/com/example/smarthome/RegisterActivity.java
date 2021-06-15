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
	
    private Button btn_register;	//ע�ᰴť
    private EditText et_user_name,et_pwd,et_pwd_again;//�û��������롢�ٴ����������Ŀؼ�
    private String username,pwd,pwd_again;//�û��������롢�ٴ����������Ŀؼ��Ļ�ȡֵ
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_register);
        init();
        
    }

	private void init() {
		// TODO Auto-generated method stub
		//��main_title_bar.xmlҳ�沼���л�ȡ��Ӧ��UI�ؼ�
        //��ȡ��Ա����ctrl+alt+F

        //��activity_register.xmlҳ�沼���л�ȡ��Ӧ��UI�ؼ�
        btn_register = (Button) findViewById(R.id.btn_register);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_again = (EditText) findViewById(R.id.et_pwd_again);
        //ע�ᰴť����¼�
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //������ȡ��������Ӧ�ؼ��е��ַ���
                getEditstring();
                //�ж��ַ����Ƿ�Ϊ��
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "�������û���", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(RegisterActivity.this, "����������", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd_again)){
                    Toast.makeText(RegisterActivity.this, "���ٴ���������", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!pwd.equals(pwd_again)){
                    Toast.makeText(RegisterActivity.this, "������������벻һ��", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isExistUserName(username)){
                    Toast.makeText(RegisterActivity.this, "���û��Ѿ�����", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
                    
                    saveRegisterInfo(username,pwd);
                    
                    //ע��ɹ���ͨ��Intent���û������ݵ�LoginActivity.java��
                    Intent data=new Intent();
                    data.putExtra("username",username);
                    setResult(RESULT_OK,data);//setResultΪOK���رյ�ǰҳ��
                    RegisterActivity.this.finish();//�ڵ�¼��ʱ������û���û��ע����ע�ᡣע��ɹ����ע��ɹ�����û������ظ�ǰһ��ҳ��
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
