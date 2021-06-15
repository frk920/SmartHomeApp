package com.example.smarthome;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity implements OnClickListener{
	// �м�������
    private FrameLayout mBodyLayout;
    private SettingView mSettingView;
    private DataView mDataView;
    private ControlView mControlView;
    //�ײ���ť��
    public LinearLayout mBottomLayout;

	//�ײ���ť�ؼ�
    private View mDataBtn,mControlBtn,mSettingBtn;
    private TextView tv_data,tv_control,tv_setting;
    private ImageView iv_data,iv_control,iv_setting;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//���ô˽���Ϊ����
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initBodyLayout();
        initBottomBar();
        setListener();
        setInitStatus();
	}
	

	//��ȡ�ײ��������ϵĿؼ�
	private void initBottomBar() {
		// TODO Auto-generated method stub
		mBottomLayout = (LinearLayout) findViewById(R.id.main_bottom_bar);
        mDataBtn = findViewById(R.id.bottom_bar_data_btn);
        mControlBtn = findViewById(R.id.bottom_bar_control_btn);
        mSettingBtn = findViewById(R.id.bottom_bar_setting_btn);
        tv_data = (TextView) findViewById(R.id.bottom_bar_text_data);
        tv_control = (TextView) findViewById(R.id.bottom_bar_text_control);
        tv_setting = (TextView) findViewById(R.id.bottom_bar_text_setting);
        iv_data = (ImageView) findViewById(R.id.bottom_bar_image_data);
        iv_control = (ImageView) findViewById(R.id.bottom_bar_image_control);
        iv_setting = (ImageView) findViewById(R.id.bottom_bar_image_setting);
	}

	private void initBodyLayout() {
		// TODO Auto-generated method stub
		mBodyLayout = (FrameLayout) findViewById(R.id.main_body);
	}
	
	//�ؼ��ĵ���¼����������ťʱ������յײ���������״̬��֮����Ӧ��ͼƬ�Ͱ�ť����Ϊѡ��״̬
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        //���ݵĵ���¼�
        case R.id.bottom_bar_data_btn:
            clearBottomImageState();
            selectDisplayView(0);
            break;
        //���Ƶĵ���¼�
        case R.id.bottom_bar_control_btn:
            clearBottomImageState();
            selectDisplayView(1);
            break;
        //���õ���¼�
        case R.id.bottom_bar_setting_btn:
            clearBottomImageState();
            selectDisplayView(2);
            break;
        default:
            break;
		}
	}

	//���õײ�������ť�ĵ�������¼�
	private void setListener() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setOnClickListener(this);
        }
	}

	//����ײ���ť��ѡ��״̬
	private void clearBottomImageState() {
		// TODO Auto-generated method stub
		tv_data.setTextColor(Color.parseColor("#666666"));
        tv_control.setTextColor(Color.parseColor("#666666"));
        tv_setting.setTextColor(Color.parseColor("#666666"));
        iv_data.setImageResource(R.drawable.data_icon);
        iv_control.setImageResource(R.drawable.control_icon);
        iv_setting.setImageResource(R.drawable.setting_icon);
        for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setSelected(false);
        }
	}
	
	//���õײ���ťѡ��״̬
	public void setSelectedStatus(int index) {
        switch (index) {
            case 0:
                mDataBtn.setSelected(true);
                iv_data.setImageResource(R.drawable.data_icon_select);
                tv_data.setTextColor(Color.parseColor("#0097F7"));
                break;
            case 1:
                mControlBtn.setSelected(true);
                iv_control.setImageResource(R.drawable.control_icon_select);
                tv_control.setTextColor(Color.parseColor("#0097F7"));
                break;
            case 2:
                mSettingBtn.setSelected(true);
                iv_setting.setImageResource(R.drawable.setting_icon_select);
                tv_setting.setTextColor(Color.parseColor("#0097F7"));

        }
    }
	
	//�Ƴ�����Ҫ����ͼ
	private void removeAllView() {
        for (int i = 0; i < mBodyLayout.getChildCount(); i++) {
            mBodyLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

	//���ý���view�ĳ�ʼ��״̬
	private void setInitStatus() {
		// TODO Auto-generated method stub
		clearBottomImageState();
        setSelectedStatus(0);
        createView(0);
	}
	
	//��ʾ��Ӧ��ҳ��
	private void selectDisplayView(int index) {
		// TODO Auto-generated method stub
		removeAllView();
        createView(index);
        setSelectedStatus(index);
	}

	//ѡ����ͼ
	private void createView(int viewIndex) {
		// TODO Auto-generated method stub
		switch (viewIndex) {
        case 0:
        	if (mDataView == null) {
        		mDataView = new DataView(this);
                mBodyLayout.addView(mDataView.getView());
            } else {
            	mDataView.getView();
            }
        	mDataView.showView();
            break;
        case 1:
        	if (mControlView == null) {
        		mControlView = new ControlView(this);
                mBodyLayout.addView(mControlView.getView());
            } else {
            	mControlView.getView();
            }
        	mControlView.showView();
            break;
        case 2:
//        	Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
//			startActivity(intent);
        	if (mSettingView == null) {
                mSettingView = new SettingView(this);
                mBodyLayout.addView(mSettingView.getView());
            } else {
                mSettingView.getView();
            }
            mSettingView.showView();
            break;
		}
	}
	
//	protected long exitTime;//��¼��һ�ε��ʱ��ʱ��
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event){
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {//�ڶ��ε��ʱ�����һ��ʱ������������
//                Toast.makeText(MainActivity.this, "�ٰ�һ���˳������ն�",
//                        Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                MainActivity.this.finish();
//                System.exit(0);//�˳��ն�
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//	}

}
