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

		// ��־λ�����ڴ洢��ǰ����ͼ�ǵڼ�����������
		private int mSensorType = 1;
		private Button back;
		private LineChartView mLineChart;
		// x������
		private String[] data = { "9sǰ", "8sǰ", "7sǰ", "6sǰ", "5sǰ", "4ǰ", "3sǰ",
				"2sǰ", "1sǰ", "����" };
		// y������
		float[] score = new float[10];
		// LineChartView���ռ��ϸ�ʽ����
		private ArrayList<AxisValue> mAxisValue = new ArrayList<AxisValue>();
		private ArrayList<PointValue> mPointValue = new ArrayList<PointValue>();
		// �������ֶ���
		private Line mline = new Line();
		// ����x�����
		private Axis mAxis = new Axis();
		// �ߵļ��϶���
		private ArrayList<Line> mLines = new ArrayList<Line>();
		// ����ͼ���ݶ���
		private LineChartData mData = new LineChartData();

		// Socket����
		private Socket mScoket;

		private InputStream mInputStream;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_chart);


			mLineChart = (LineChartView) findViewById(R.id.linechart);
			back = (Button) findViewById(R.id.back);
			
			// ��������
			for (int i = 0; i < data.length; i++) {
				// �������������
				mAxisValue.add(new AxisValue(i).setLabel(data[i]));
				// �����������ֵ
				mPointValue.add(new PointValue(i, score[i]));
			}
			initData();
			connectServer();
			// ������ӵ�
			mline.setValues(mPointValue);
			// �����ߵ���ɫ
			mline.setColor(Color.parseColor("#5699BB"));
			// ���õ����״
			mline.setShape(ValueShape.DIAMOND);
			// ����Բ������
			mline.setCubic(true);
			// �Ƿ�������
			mline.setFilled(true);
			// ����������ʾ
			mline.setHasLabels(true);
			// ���������ʾ
			// mline.setHasLabelsOnlyForSelected(true);
			// ȡ��������ʾ
			// mline.setHasLines(false);

			// ����������ӵ��ߵļ���(����ֻ��һ��)
			mLines.add(mline);
			// ���ߵļ�����ӵ����ݶ�����
			mData.setLines(mLines);

			// ������������
			mAxis.setValues(mAxisValue);
			// �����������Ϊб��
			mAxis.setHasTiltedLabels(true);
			// ���ñ���
			mAxis.setName("SDAUʵѵ����");
			// ��������ɫ
			mAxis.setTextColor(Color.parseColor("#12345B"));
			// ���������С
			// mAxis.setTextSize(15);
			// �����Ƿ�ӵ����ֽ���
			mAxis.setHasLines(true);

			// ���Ἧ����ӵ����ݶ���
			mData.setAxisXBottom(mAxis);

			// �����Ƿ�֧�ֻ���ƽ������
			// mLineChart.setInteractive(false);
			// �������ŵ���
			mLineChart.setZoomType(ZoomType.HORIZONTAL);
			// ����������ű���
			mLineChart.setMaxZoom(2);
			// �����ݶ������õ�����ͼ��
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

		// ���ӷ�����
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
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// mText.setText(message);
									// �жϵ�ǰӦ�ø����ĸ�����������
									switch (mSensorType) {
	                                case 1:
	                                    changeSensor("#00E5EE","�¶�");
	                                    addPoint(wendu);
	                                    break;
	                                case 2:
	                                    changeSensor("#104E8B","ʪ��");
	                                    addPoint(shidu);
	                                    break;
	                                case 3:
	                                    changeSensor("#FFD700","����");
	                                    addPoint(guangzhao);
	                                    break;
	                                case 4:
	                                    changeSensor("#00ff33","��ѹ");
	                                    addPoint(dianchi);
	                                    break;
	                                case 5:
	                                    changeSensor("#660066","��λ��");
	                                    addPoint(dianwei);
	                                    break;
	                                case 6:
	                                    changeSensor("#FF3030","x��");
	                                    addPoint(x);
	                                    break;
	                                case 7:
	                                    changeSensor("#FF00FF","y��");
	                                    addPoint(y);
	                                    break;
	                                case 8:
	                                    changeSensor("#A020F0","z��");
	                                    addPoint(z);
	                                    break;
	                                }
								}
							});
						}
					} catch (final Exception e) {// �ڲ���ʹ���ⲿ������final
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
//								Toast.makeText(ChartActivity.this, "�������ӳ���" + e,
//										Toast.LENGTH_SHORT).show();
								

							}
						});
					}
				}
			}.start();
		}
		//�����ͬ���ʹ��������������ͼ��������ɫ
		private void changeSensor(String color,String title){
			//������ε���Ĵ��������ǵ�ǰ������ʾ�Ĵ��������ͣ���Ҫ����һ�²�ͬ����������������
			//1.ȫ��Ϊ0
			//2.�л����ݿ�
			mAxis.setName(title);
			mline.setColor(Color.parseColor(color));
		}
		
		
		// ��Դ����
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
			// ��������Ϊ11������
			float[] temp = new float[11];
			// ��֮ǰ�����ݷŵ��µ�����temp��ǰʮλ
			for (int i = 0; i < score.length; i++) {
				temp[i] = score[i];
			}
			temp[10] = newData;
			// ��temp�еڶ���ʮһ�����ݸ��ǵ�score������
			for (int i = 0; i < score.length; i++) {
				score[i] = temp[i + 1];
			}
			mPointValue.clear();
			// �滻Ϊ�µ�����
			for (int i = 0; i < data.length; i++) {
				// ʮ��������
				mPointValue.add(new PointValue(i, score[i]));
			}
			mLineChart.setLineChartData(mData);
		}

		
		
		protected void initData(){
			String temp=getIntent().getStringExtra("getid");
			mSensorType = Integer.valueOf(temp);
			
		}
}
