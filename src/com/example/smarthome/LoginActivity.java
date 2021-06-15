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
	
    private TextView tv_register,tv_delete;//����ע�ᡢ�һ�����Ŀؼ�
    private Button btn_login;	//��¼��ť
    private EditText et_user_name,et_pwd;//�û���������Ŀؼ�
    private String username,pwd,spPwd;//�û���������Ŀؼ��Ļ�ȡֵ
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_login);
    	//���ô˽���Ϊ����
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
     
        //����ע��ؼ��ĵ���¼�
        tv_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivityForResult(intent, 1);
			}
		});
        //ע���û�����¼�
        tv_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LoginActivity.this,DeleteActivity.class);
				startActivity(intent);
			}
		});
        //��¼��ť����¼�
        btn_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				username=et_user_name.getText().toString().trim();
				pwd=et_pwd.getText().toString().trim();
				spPwd=readPwd(username);
				if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this, "�������û���", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(LoginActivity.this, "����������", Toast.LENGTH_SHORT).show();
                    return;
                }else if(pwd.equals(spPwd)){
                    Toast.makeText(LoginActivity.this, "��¼�ɹ�", Toast.LENGTH_SHORT).show();
                   
                    LoginActivity.this.finish();//�ڵ�¼��ʱ������û���û��ע����ע�ᡣע��ɹ����ע��ɹ�����û������ظ�ǰһ��ҳ��
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    return;
                }else if((!TextUtils.isEmpty(spPwd)&&!pwd.equals(spPwd))){
                	Toast.makeText(LoginActivity.this, "�û������������", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                	Toast.makeText(LoginActivity.this, "���û�������", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	//�����û�����ȡ����
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
			//��ע����洫�ݹ������û���
			String username=data.getStringExtra("username");
			if(!TextUtils.isEmpty(username)){
				et_user_name.setText(username);
				//���ù���λ����
				et_user_name.setSelection(username.length());
			}
		}
	}
}
