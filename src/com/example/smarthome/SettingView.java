package com.example.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class SettingView{
	
    private RelativeLayout rl_ip;
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    public  SettingView(Activity context){
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
        mCurrentView = mInflater.inflate(R.layout.main_view_setting,null);
        rl_ip = (RelativeLayout)mCurrentView.findViewById(R.id.rl_ip);
        mCurrentView.setVisibility(View.VISIBLE);
        
        //���õĵ���¼�
        rl_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,IpActivity.class);
            	mContext.startActivity(intent);
            }
        });
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
