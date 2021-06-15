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
        mCurrentView = mInflater.inflate(R.layout.main_view_setting,null);
        rl_ip = (RelativeLayout)mCurrentView.findViewById(R.id.rl_ip);
        mCurrentView.setVisibility(View.VISIBLE);
        
        //设置的点击事件
        rl_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mContext,IpActivity.class);
            	mContext.startActivity(intent);
            }
        });
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
