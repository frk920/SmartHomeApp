package com.example.smarthome;


import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;




import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DataView{
	
	private LinearLayout mLayout1, mLayout2, mLayout3, mLayout4, mLayout5,mLayout6, mLayout7, mLayout8;
	private TextView mTextWd, mTextSd, mTextGz, mTextDy, mTextDw, mTextx,mTexty, mTextz;
	private Socket mScoket;
	private InputStream mInputStream;
	private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    public  DataView(Activity context){
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
        mCurrentView = mInflater.inflate(R.layout.main_view_data,null);
        mLayout1 = (LinearLayout) mCurrentView.findViewById(R.id.sensor1);
		mLayout2 = (LinearLayout) mCurrentView.findViewById(R.id.sensor2);
		mLayout3 = (LinearLayout) mCurrentView.findViewById(R.id.sensor3);
		mLayout4 = (LinearLayout) mCurrentView.findViewById(R.id.sensor4);
		mLayout5 = (LinearLayout) mCurrentView.findViewById(R.id.sensor5);
		mLayout6 = (LinearLayout) mCurrentView.findViewById(R.id.sensor6);
		mLayout7 = (LinearLayout) mCurrentView.findViewById(R.id.sensor7);
		mLayout8 = (LinearLayout) mCurrentView.findViewById(R.id.sensor8);
        
        mTextWd = (TextView) mCurrentView.findViewById(R.id.text_wendu);
		mTextSd = (TextView) mCurrentView.findViewById(R.id.text_shidu);
		mTextGz = (TextView) mCurrentView.findViewById(R.id.text_guangzhao);
		mTextDy = (TextView) mCurrentView.findViewById(R.id.text_dianya);
		mTextDw = (TextView) mCurrentView.findViewById(R.id.text_dianwei);
		mTextx = (TextView) mCurrentView.findViewById(R.id.text_x);
		mTexty = (TextView) mCurrentView.findViewById(R.id.text_y);
		mTextz = (TextView) mCurrentView.findViewById(R.id.text_z);
		
		mCurrentView.setVisibility(View.VISIBLE);
		connectServer();
        //设置的点击事件
        mLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","1");

            	mContext.startActivity(intent);
            }
        });
        mLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","2");

            	mContext.startActivity(intent);
            }
        });
        mLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","4");
            	mContext.startActivity(intent);
            }
        });
        mLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","4");
            	mContext.startActivity(intent);
            }
        });
        mLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","5");
            	mContext.startActivity(intent);
            }
        });
        mLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","6");
            	mContext.startActivity(intent);
            }
        });
        mLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","7");
            	mContext.startActivity(intent);
            }
        });
        mLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,ChartActivity.class);
                intent.putExtra("getid","8");
            	mContext.startActivity(intent);
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
					mInputStream = mScoket.getInputStream();
					// 使用30个字节的数组接收服务数据
					byte[] b = new byte[30];
					// 一直接收数据
					while (true) {// 可优化：把true改为标志位变量
						// 读取数据
						mInputStream.read(b);
						// 创建结构体类
						ConverEnvInfo cei = new ConverEnvInfo();
						// 把字节数组中的数据设置为cei对象能接受的数据
						// 参数一:byteBuffer(字节数组封装类)
						// 参数二:从哪开始转换
						cei.setByteBuffer(
								ByteBuffer.wrap(b).order(
										ByteOrder.LITTLE_ENDIAN), 0);
						// 取出八个传感器数据
						final float wendu = cei.temperature.get();
						final float shidu = cei.humidity.get();
						final float guangzhao = cei.ill.get();
						final float dianchi = cei.bet.get();
						final float dianwei = cei.adc.get();
						final float x = cei.x.get();
						final float y = cei.y.get();
						final float z = cei.z.get();
						// 切换到主线程更新显示
						// final String message =
						// "温度:"+wendu+"\n湿度:"+shidu+"\n光照:"+guangzhao+"\n电池电量:"+dianchi+"\n电位器:"+dianwei+"\nx:"+x+"\ny:"+y+"\nz:"+z;
					
						mContext.runOnUiThread(new Runnable() {
							@Override
							public void run() {
//								// mText.setText(message);
								mTextWd.setText("" + wendu);
								mTextSd.setText("" + shidu);
								mTextGz.setText("" + guangzhao);
								mTextDy.setText("" + dianchi);
								mTextDw.setText("" + dianwei);
								mTextx.setText("" + x);
								mTexty.setText("" + y);
								mTextz.setText("" + z);
								
							}
						});
					}
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
	
}
