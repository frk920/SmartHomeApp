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
        //���õĵ���¼�
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
				// ����һ��IP��ַ
				// ������:�˿ں�
				try {
					mScoket = new Socket(ip, Integer.valueOf(port));
					// �������������
					mInputStream = mScoket.getInputStream();
					// ʹ��30���ֽڵ�������շ�������
					byte[] b = new byte[30];
					// һֱ��������
					while (true) {// ���Ż�����true��Ϊ��־λ����
						// ��ȡ����
						mInputStream.read(b);
						// �����ṹ����
						ConverEnvInfo cei = new ConverEnvInfo();
						// ���ֽ������е���������Ϊcei�����ܽ��ܵ�����
						// ����һ:byteBuffer(�ֽ������װ��)
						// ������:���Ŀ�ʼת��
						cei.setByteBuffer(
								ByteBuffer.wrap(b).order(
										ByteOrder.LITTLE_ENDIAN), 0);
						// ȡ���˸�����������
						final float wendu = cei.temperature.get();
						final float shidu = cei.humidity.get();
						final float guangzhao = cei.ill.get();
						final float dianchi = cei.bet.get();
						final float dianwei = cei.adc.get();
						final float x = cei.x.get();
						final float y = cei.y.get();
						final float z = cei.z.get();
						// �л������̸߳�����ʾ
						// final String message =
						// "�¶�:"+wendu+"\nʪ��:"+shidu+"\n����:"+guangzhao+"\n��ص���:"+dianchi+"\n��λ��:"+dianwei+"\nx:"+x+"\ny:"+y+"\nz:"+z;
					
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
	
}
