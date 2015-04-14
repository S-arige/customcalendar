package com.example.customcarendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import utils.jsonparser;


import Adapter.CalendarTitleListviewAdapter;
import Adapter.calendarCalendarAdapter;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import bean.calendarDayBean;
import bean.calendarItemBean;


import com.example.customcarendar.R.color;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;


public class calendarActivity extends Activity {

	@ViewInject(R.id.calendar_title_left_btn)
	private Button btn_top_back;//返回上页
	private String sertime=null;//服务器返回的当前时间
	@ViewInject(R.id.calendar_title_category_tv)
	private Button calendar_title_category_tv;//分类的按钮
	
	@ViewInject(R.id.calendar_title_month_tv)
	private Button calendar_title_month_tv;//年月的按钮
	private Date date;
	private String currentdate;//当前手机时间
	private int currentday;//当前的日期
	@ViewInject(R.id.calendar_calendar_gv)
	private GridView gridView;//gridview
	private List<calendarDayBean> sertimelist=null;//返回值中，包含days的直接list
	private List<calendarItemBean> itemlist;//在接收过程中使用
	
	private List<calendarItemBean> list;//在下载过程中使用
	private String TAG="calendarActivity";
	private HttpUtils httpUtils;//网络工具
	private HttpHandler<String> xhHandler;
	private calendarCalendarAdapter cca;//适配器
	
	private ListView monthlv;//月份的lv
	private ListView categorylv;//种类lv
	private String currentmonth;//当前被选中的月份
	private String currentcategory;//当前被选中的种类
	private int monthcurrentpos=0;//当前月份所在的位置
	private int categorycurrentpos=0;//当前分类所在的位置

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_main);
		ViewUtils.inject(this);
		if(sertime==null){//如果没有给返回
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM"); 
		sertime=sdf.format(new Date(System.currentTimeMillis()));
		//int year=date.getYear();
		Log.i(TAG, sertime+"temp");
		}
		
		
		//从网络下载
		getDateByXutil("http://mapi.damai.cn/proj/Calendar.aspx?CategoryId=0&source=10101&channel_from=360_market&cityId=0&version=50003");
		
		
		
		
		//返回上页的点击事件
		btn_top_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		
		//获取月份选项
		calendar_title_month_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
//				Log.i(TAG, "y月份被点击");
				//准备数据源，数据从网络获取
				//String [] sarr=getResources().getStringArray(R.array.calendar_right_category);
				List<String> slist=new ArrayList<String>();
				if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					String temp=list.get(i).getName();
					String yandm=temp.substring(0, 4)+"年"+temp.substring(5)+"月";
					slist.add(yandm);
				}
				}
				//内部的listview
				CalendarTitleListviewAdapter ctla=new CalendarTitleListviewAdapter(calendarActivity.this, slist,calendar_title_month_tv);
				
				
				
				LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				ViewGroup menuView=(ViewGroup) inflater.inflate(R.layout.calendar_title_popupwindow,null, true);
		
				monthlv=(ListView) menuView.findViewById(R.id.calendar_title_popupListView);
			
				monthlv.setAdapter(ctla);
				final PopupWindow mpop=new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT , true);
				mpop.setContentView(menuView);
				mpop.setWidth(250);
				mpop.setHeight(400);
				mpop.setBackgroundDrawable(new ColorDrawable(0));
				mpop.setOutsideTouchable(true);
				mpop.setFocusable(true);
				mpop.showAsDropDown(calendar_title_month_tv, 0, 0);
				
				
				monthlv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> viewlist, View view,
							int pos, long id) {
						if(monthcurrentpos==pos){
							//搜索
						}else{

							View exview=viewlist.getChildAt(monthcurrentpos);
							TextView extv=(TextView) exview.findViewById(R.id.calendar_title_pop_item_tv);
							ImageView exiv=(ImageView) exview.findViewById(R.id.calendar_title_pop_item_iv);
							exiv.setVisibility(View.INVISIBLE);
							extv.setTextAppearance(calendarActivity.this, R.style.calendar_title_pop_nor);
							TextView tv=(TextView) view.findViewById(R.id.calendar_title_pop_item_tv);
							ImageView iv=(ImageView) view.findViewById(R.id.calendar_title_pop_item_iv);
							iv.setVisibility(View.VISIBLE);
							tv.setTextAppearance(calendarActivity.this, R.style.calendar_title_pop_sel);
							String text=tv.getText().toString();
							calendar_title_month_tv.setText(text);
							Toast.makeText(calendarActivity.this, text, 0).show();
							monthcurrentpos=pos;
							
							sertimelist=list.get(pos).getDaylist();
							String sertime=list.get(pos).getName();
							cca.setSertime(sertime);
							gridView.setAdapter(cca);
						}
						
					}
				});
				
			}
		});
	
		
		
		//获取分类的选项
		calendar_title_category_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(), "BEI DIAN JI",0).show();
				//准备数据源  数据从本地获取
				String [] sarr=getResources().getStringArray(R.array.calendar_right_category);
				List<String> slist=new ArrayList<String>();
				
				for(int i=0;i<sarr.length;i++){
					slist.add(sarr[i]);
				}
				
				//内部的listview
				CalendarTitleListviewAdapter ctla=new CalendarTitleListviewAdapter(calendarActivity.this, slist,calendar_title_category_tv);
				
				
				
				LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				ViewGroup menuView=(ViewGroup) inflater.inflate(R.layout.calendar_title_popupwindow,null, true);
				categorylv=(ListView) menuView.findViewById(R.id.calendar_title_popupListView);
				categorylv.setBackgroundColor(Color.GRAY);//设置的是背景颜色，就是弹出窗口的颜色
				categorylv.setAdapter(ctla);
				final PopupWindow mpop=new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT , true);
				mpop.setContentView(menuView);
				mpop.setWidth(150);
				mpop.setHeight(400);
				mpop.setOutsideTouchable(true);
				mpop.setFocusable(true);
				mpop.showAsDropDown(calendar_title_category_tv, 0, 0);
				
				
				categorylv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> viewlist, View view,
							int pos, long id) {
						Log.i(TAG, "lv   de dianji  shijian  fasheng");
						if(categorycurrentpos==pos){
							//搜索
						}else{
							View exview=viewlist.getChildAt(categorycurrentpos);
							TextView extv=(TextView) exview.findViewById(R.id.calendar_title_pop_item_tv);
							ImageView exiv=(ImageView) exview.findViewById(R.id.calendar_title_pop_item_iv);
							exiv.setVisibility(View.INVISIBLE);
							extv.setTextAppearance(calendarActivity.this, R.style.calendar_title_pop_nor);
							TextView tv=(TextView) view.findViewById(R.id.calendar_title_pop_item_tv);
							ImageView iv=(ImageView) view.findViewById(R.id.calendar_title_pop_item_iv);
							iv.setVisibility(View.VISIBLE);
							tv.setTextAppearance(calendarActivity.this, R.style.calendar_title_pop_sel);
							String text=tv.getText().toString();
							calendar_title_category_tv.setText(text);
							Toast.makeText(calendarActivity.this, text, 0).show();
							categorycurrentpos=pos;
							mpop.dismiss();
						}
						
					}
				});
				
			}
		});

		
	}

	//网络下载数据，并解析。List<calendarItemBean>
	public void getDateByXutil(String path){
		Log.i(TAG, "网络下载方法被执行");
//		list=new ArrayList<calendarItemBean>();
		httpUtils=new HttpUtils();
		
		xhHandler=httpUtils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				cca=new calendarCalendarAdapter(calendarActivity.this,list);
				cca.setSertime(sertime);
				gridView.setAdapter(cca);
				Toast.makeText(getApplication(), "下载失败，请重试", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				String resjson=res.result;
				gridView.removeAllViewsInLayout();
				list=jsonparser.parserDayBean(resjson);
				
				cca=new calendarCalendarAdapter(calendarActivity.this,list);
				sertimelist=list.get(0).getDaylist();
				String temp=list.get(0).getName();
				String yandm=temp.substring(0, 4)+"年"+temp.substring(5)+"月";
				calendar_title_month_tv.setText(yandm);
				cca.setSertime(temp);
				gridView.setAdapter(cca);
			}
		});
		
	

	}
	
	
	
	
	
	
	
	//获取要显示的
	public static List<String> initCalendar(String sertime){
	
		Date monthdate = null;
		List<String> daylist=new ArrayList<String>();//日历的数字排序
		List<String> list=new ArrayList<String>();
		String sy=sertime.substring(0, 4);
		int y=new Integer(sy);
		int m=new Integer(sertime.substring(5));
		//获得当前月份的1号的date
		String datestring=sertime+"-01";
//		Log.i(TAG, datestring+"datestring");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			monthdate=sdf.parse(datestring);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//获得当前月1号是星期几，week_index是0的话，星期日
		Calendar cal = Calendar.getInstance();  
        cal.setTime(monthdate);  
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        Log.i(TAG, week_index+"-----------当前1号是周几 0是周日");
     
        //当前月份的天数
        Calendar cal1 = Calendar.getInstance();  
        cal1.set(Calendar.YEAR, y);  
        cal1.set(Calendar.MONTH, m-1);  
        int days_of_month = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
//        Log.i(TAG, days_of_month+"-----------当前月份的天数");
        //从周日开始
   if(week_index==0){
        	for(int i=1;i<43;i++){
        		if(i<=days_of_month){
        		daylist.add(i+"");
        		}else{
//        			Log.i(TAG, i+"week_index  is  0   i>yuetianshu");
        			daylist.add((i-days_of_month)+"");
        		}
        	}
        }else{//从不是周日开始
        	//上个月份的天数
            Calendar cal2 = Calendar.getInstance();  
            cal2.set(Calendar.YEAR, y);  
            cal2.set(Calendar.MONTH, m-2);  
            int days_of_month2 = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
        	int beforenum=days_of_month2-week_index+1;
        	for(int i=1;i<43;i++){
        		if(beforenum<=days_of_month2){
        			daylist.add(beforenum+"");
//        			Log.i(TAG, beforenum+"beforenum++++++++++++++");
        			beforenum++;
        		}else{
        			
        			int currentmonthday=i-week_index;
        			if(currentmonthday<=days_of_month){
        				daylist.add(currentmonthday+"");
        			}else{//当月后不足，补齐的
        				daylist.add((i-days_of_month-week_index)+"");
        			}
        			
        		}
        	}
        }
   
   list=daylist;
   return list;
		
	}


}
