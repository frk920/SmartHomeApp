package com.example.smarthome;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChartActivity extends Activity{

		// 标志位，用于存储当前折线图是第几个传感器的
		private int mSensorType = 1;
		private Button back;
		private LineChartView mLineChart;
		// x轴数据
		private String[] data = { "9s前", "8s前", "7s前", "6s前", "5s前", "4前", "3s前",
				"2s前", "1s前", "现在" };
		// y轴数据
		float[] score = new float[10];
		// LineChartView接收集合格式数据
		private ArrayList<AxisValue> mAxisValue = new ArrayList<AxisValue>();
		private ArrayList<PointValue> mPointValue = new ArrayList<PointValue>();
		// 创建折现对象
		private Line mline = new Line();
		// 创建x轴对象
		private Axis mAxis = new Axis();
		// 线的集合对象
		private ArrayList<Line> mLines = new ArrayList<Line>();
		// 折线图数据对象
		private LineChartData mData = new LineChartData();

		// Socket对象
		private Socket mScoket;

		private InputStream mInputStream;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_chart);


			mLineChart = (LineChartView) findViewById(R.id.linechart);
			back = (Button) findViewById(R.id.back);
			
			// 遍历数组
			for (int i = 0; i < data.length; i++) {
				// 横坐标添加文字
				mAxisValue.add(new AxisValue(i).setLabel(data[i]));
				// 纵坐标添加数值
				mPointValue.add(new PointValue(i, score[i]));
			}
			initData();
			connectServer();
			// 给线添加点
			mline.setValues(mPointValue);
			// 设置线的颜色
			mline.setColor(Color.parseColor("#5699BB"));
			// 设置点的形状
			mline.setShape(ValueShape.DIAMOND);
			// 设置圆滑折线
			mline.setCubic(true);
			// 是否填充面积
			mline.setFilled(true);
			// 设置坐标显示
			mline.setHasLabels(true);
			// 点击坐标显示
			// mline.setHasLabelsOnlyForSelected(true);
			// 取消线条显示
			// mline.setHasLines(false);

			// 把所有线添加到线的集合(这里只有一条)
			mLines.add(mline);
			// 把线的集合添加到数据对象中
			mData.setLines(mLines);

			// 给轴设置数据
			mAxis.setValues(mAxisValue);
			// 设置轴的字体为斜体
			mAxis.setHasTiltedLabels(true);
			// 设置表名
			mAxis.setName("SDAU实训测试");
			// 设置轴颜色
			mAxis.setTextColor(Color.parseColor("#12345B"));
			// 设置字体大小
			// mAxis.setTextSize(15);
			// 设置是否拥有轴分界线
			mAxis.setHasLines(true);

			// 把轴集合添加到数据对象
			mData.setAxisXBottom(mAxis);

			// 设置是否支持滑动平移缩放
			// mLineChart.setInteractive(false);
			// 设置缩放的轴
			mLineChart.setZoomType(ZoomType.HORIZONTAL);
			// 设置最大缩放倍数
			mLineChart.setMaxZoom(2);
			// 把数据对象设置到折现图中
			mLineChart.setLineChartData(mData);
			
			back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ChartActivity.this,MainActivity.class);
	            	startActivity(intent);
				}
			});

		}

		// 连接服务器
		private void connectServer() {
			new Thread() {
				public void run() {
					SQLiteDatabase db = openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
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
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// mText.setText(message);
									// 判断当前应该更新哪个传感器折线
									switch (mSensorType) {
	                                case 1:
	                                    changeSensor("#00E5EE","温度");
	                                    addPoint(wendu);
	                                    break;
	                                case 2:
	                                    changeSensor("#104E8B","湿度");
	                                    addPoint(shidu);
	                                    break;
	                                case 3:
	                                    changeSensor("#FFD700","光照");
	                                    addPoint(guangzhao);
	                                    break;
	                                case 4:
	                                    changeSensor("#00ff33","电压");
	                                    addPoint(dianchi);
	                                    break;
	                                case 5:
	                                    changeSensor("#660066","电位器");
	                                    addPoint(dianwei);
	                                    break;
	                                case 6:
	                                    changeSensor("#FF3030","x轴");
	                                    addPoint(x);
	                                    break;
	                                case 7:
	                                    changeSensor("#FF00FF","y轴");
	                                    addPoint(y);
	                                    break;
	                                case 8:
	                                    changeSensor("#A020F0","z轴");
	                                    addPoint(z);
	                                    break;
	                                }
								}
							});
						}
					} catch (final Exception e) {// 内部类使用外部变量加final
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
//								Toast.makeText(ChartActivity.this, "网络连接出错" + e,
//										Toast.LENGTH_SHORT).show();
								

							}
						});
					}
				}
			}.start();
		}
		//点击不同类型传感器后更改折线图标题与颜色
		private void changeSensor(String color,String title){
			//如果本次点击的传感器不是当前正在显示的传感器类型，需要处理一下不同传感器的数据问题
			//1.全置为0
			//2.切换数据库
			mAxis.setName(title);
			mline.setColor(Color.parseColor(color));
		}
		
		
		// 资源回收
		@Override
		public void onDestroy() {
			super.onDestroy();
			try {
				mScoket.close();
				mInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void addPoint(Float newData) {
			// 创建长度为11的数组
			float[] temp = new float[11];
			// 把之前的数据放到新的数组temp的前十位
			for (int i = 0; i < score.length; i++) {
				temp[i] = score[i];
			}
			temp[10] = newData;
			// 把temp中第二到十一个数据覆盖到score数组中
			for (int i = 0; i < score.length; i++) {
				score[i] = temp[i + 1];
			}
			mPointValue.clear();
			// 替换为新的数据
			for (int i = 0; i < data.length; i++) {
				// 十个点数据
				mPointValue.add(new PointValue(i, score[i]));
			}
			mLineChart.setLineChartData(mData);
		}

		
		
		protected void initData(){
			String temp=getIntent().getStringExtra("getid");
			mSensorType = Integer.valueOf(temp);
			
		}
}
