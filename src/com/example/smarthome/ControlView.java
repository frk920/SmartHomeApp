package com.example.smarthome;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ControlView {
	private Button mbtnFan1, mbtnFan2, mbtnFan3, mBtnFanNo, mBtnBeepOk, mBtnBeepNo, mBtnLEDOk, mBtnLEDNo;
	private Socket mScoket;
	private OutputStream mOutputStream;
	private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    public  ControlView(Activity context){
        mContext = context;
        //Ϊ�Ժ�Layoutת��Ϊviewʱ��
        mInflater = LayoutInflater.from(mContext);
    }
    private void createView(){
        initView();
    }
    
    //��ȡ����ؼ�
	private void initView() {
		// TODO Auto-generated method stub
		//���ò����ļ�
        mCurrentView = mInflater.inflate(R.layout.main_view_control,null);
        mbtnFan1 = (Button) mCurrentView.findViewById(R.id.btnFan1);
        mbtnFan2 = (Button) mCurrentView.findViewById(R.id.btnFan2);
        mbtnFan3 = (Button) mCurrentView.findViewById(R.id.btnFan3);
        mBtnFanNo = (Button) mCurrentView.findViewById(R.id.btnFanNo);
        mBtnBeepOk = (Button) mCurrentView.findViewById(R.id.btnBeepOk);
        mBtnBeepNo = (Button) mCurrentView.findViewById(R.id.btnBeepNo);
        mBtnLEDOk = (Button) mCurrentView.findViewById(R.id.btnLEDOk);
        mBtnLEDNo = (Button) mCurrentView.findViewById(R.id.btnLEDNo);
		mCurrentView.setVisibility(View.VISIBLE);
		connectServer();
		
		mbtnFan1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10000001");
            }
        });
		mbtnFan2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10000010");
            }
        });
		mbtnFan3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10000011");
            }
        });
		mBtnFanNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10000000");
            }
        });
		mBtnBeepOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10010001");
            }
        });
		mBtnBeepNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10010000");
            }
        });
		mBtnLEDOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10100001");
            }
        });
		mBtnLEDNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��������
                sendCmd("10100000");
            }
        });
       
	}
	
	private void connectServer() {
	
		new Thread() {
			public void run() {
				SQLiteDatabase db = mContext.openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
			    Cursor cursor=db.rawQuery("select * from ip where id = ?",new String[]{"1"});
			    String ip = "192.168.123.114",port="8887";
			    while(cursor.moveToNext()){
			    	ip = cursor.getString(1);
			    	port = cursor.getString(2);
			    }
				// ����һ��IP��ַ
				// ������:�˿ں�
				try {
					mScoket = new Socket(ip, Integer.valueOf(port));
					// �������������
				} catch (final Exception e) {// �ڲ���ʹ���ⲿ������final
					mContext.runOnUiThread(new Runnable() {
						@Override
						public void run() {
//							Toast.makeText(mContext, "�������ӳ���" + e,
//									Toast.LENGTH_SHORT).show();
						}
					});
				}
			    
			}
		}.start();
	}
	
	//��ȡ��ǰ�������Ϸ���ʾ��Ӧ��View
	public View getView(){
        if (mCurrentView == null){
            createView();
        }
        return mCurrentView;
    }
	
	//��ʾ��ǰ�������Ϸ���ʾ��Ӧ��View����
	public  void showView() {
        if(mCurrentView == null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }
	
	private void sendCmd(final String cmd) {
        new Thread() {
            public void run() {
                try {
                    // ���û�����������ֵ
                    if (mOutputStream == null && mScoket != null)
                        mOutputStream = mScoket.getOutputStream();
                    // ��װ����ģ��ṹ��
                    byte[] cmdStruct = new byte[2];
                    cmdStruct[0] = 0; // type
                    // ���ַ���ת��Ϊ8λ�޷�������
                    // ����1��Ҫת�����ֵ��ַ���
                    // ����2���Լ����ƽ��н���
                    cmdStruct[1] = (byte) Integer.parseInt(cmd, 2);
                    mOutputStream.write(cmdStruct);

                } catch (IOException e) {
                    // ���Ӵ�����ʾ�ˣ�����Լ�
                    e.printStackTrace();
                }

            }
        }.start();
	}
}