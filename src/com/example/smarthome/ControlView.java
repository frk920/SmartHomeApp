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
        //为以后将Layout转换为view时用
        mInflater = LayoutInflater.from(mContext);
    }
    private void createView(){
        initView();
    }
    
    //获取界面控件
	private void initView() {
		// TODO Auto-generated method stub
		//设置布局文件
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
                // 发送命令
                sendCmd("10000001");
            }
        });
		mbtnFan2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发送命令
                sendCmd("10000010");
            }
        });
		mbtnFan3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发送命令
                sendCmd("10000011");
            }
        });
		mBtnFanNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发送命令
                sendCmd("10000000");
            }
        });
		mBtnBeepOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发送命令
                sendCmd("10010001");
            }
        });
		mBtnBeepNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发送命令
                sendCmd("10010000");
            }
        });
		mBtnLEDOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发送命令
                sendCmd("10100001");
            }
        });
		mBtnLEDNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发送命令
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
				// 参数一：IP地址
				// 参数二:端口号
				try {
					mScoket = new Socket(ip, Integer.valueOf(port));
					// 获得输入流对象
				} catch (final Exception e) {// 内部类使用外部变量加final
					mContext.runOnUiThread(new Runnable() {
						@Override
						public void run() {
//							Toast.makeText(mContext, "网络连接出错" + e,
//									Toast.LENGTH_SHORT).show();
						}
					});
				}
			    
			}
		}.start();
	}
	
	//获取当前导航栏上方显示对应的View
	public View getView(){
        if (mCurrentView == null){
            createView();
        }
        return mCurrentView;
    }
	
	//显示当前导航栏上方显示对应的View界面
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
                    // 如果没有输出流，则赋值
                    if (mOutputStream == null && mScoket != null)
                        mOutputStream = mScoket.getOutputStream();
                    // 组装命令模拟结构体
                    byte[] cmdStruct = new byte[2];
                    cmdStruct[0] = 0; // type
                    // 把字符串转换为8位无符号整型
                    // 参数1：要转换数字的字符串
                    // 参数2：以几进制进行解析
                    cmdStruct[1] = (byte) Integer.parseInt(cmd, 2);
                    mOutputStream.write(cmdStruct);

                } catch (IOException e) {
                    // 不加错误提示了，你可以加
                    e.printStackTrace();
                }

            }
        }.start();
	}
}