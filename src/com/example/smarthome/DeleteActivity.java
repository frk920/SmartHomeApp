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
	
    private Button btn_delete;	//ע����ť
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
		//��main_title_bar.xmlҳ�沼���л�ȡ��Ӧ��UI�ؼ�
        //��ȡ��Ա����ctrl+alt+F

        //��activity_register.xmlҳ�沼���л�ȡ��Ӧ��UI�ؼ�
        btn_delete = (Button) findViewById(R.id.btn_delete);
        de_user_name = (EditText) findViewById(R.id.de_user_name);
        de_pwd = (EditText) findViewById(R.id.de_pwd);
        
        //ע����ť����¼�
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	username=de_user_name.getText().toString().trim();
				pwd=de_pwd.getText().toString().trim();
				spPwd=readPwd(username);
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(DeleteActivity.this, "�������û���", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(DeleteActivity.this, "����������", Toast.LENGTH_SHORT).show();
                    return;
                }else if(pwd.equals(spPwd)){
                    Toast.makeText(DeleteActivity.this, "ע���ɹ�", Toast.LENGTH_SHORT).show();
                    DeteleInfo(username);
                    DeleteActivity.this.finish();//�ڵ�¼��ʱ������û���û��ע����ע�ᡣע��ɹ����ע��ɹ�����û������ظ�ǰһ��ҳ��
                    startActivity(new Intent(DeleteActivity.this, LoginActivity.class));
                    return;
                }else if((!TextUtils.isEmpty(spPwd)&&!pwd.equals(spPwd))){
                	Toast.makeText(DeleteActivity.this, "�û������������", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                	Toast.makeText(DeleteActivity.this, "���û�������", Toast.LENGTH_SHORT).show();
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
