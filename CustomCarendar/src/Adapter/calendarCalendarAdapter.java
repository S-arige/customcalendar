package Adapter;

import java.util.List;

import javax.crypto.spec.IvParameterSpec;


import bean.calendarDayBean;
import bean.calendarItemBean;

import com.example.customcarendar.R;
import com.example.customcarendar.R.color;
import com.example.customcarendar.calendarActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class calendarCalendarAdapter extends BaseAdapter {

	private List<String> sortdaylist;//排序后的天数，即在里面展示的数字的排�?
	private int flag=0;//是否是当前月�?表示不是�?表示是�?
	private List<calendarDayBean> serdaylist;//服务器传过来的当前展示的月份的天的情�?
	private boolean isCon=false;//是否包含在serdaylist�?
	private Context context;
	private String TAG="calendarCalendarAdapter";
	private TextView currenttextview=null;
	private List<calendarItemBean> seritemlist;
	private boolean  backflag;
	public calendarCalendarAdapter(Context context,List<calendarItemBean> seritemlist) {
		this.seritemlist=seritemlist;
		this.context=context;
	}
	
	public void setSertime(String sertime) {
		sortdaylist=calendarActivity.initCalendar(sertime);
		for(int i=0;i<seritemlist.size();i++){
			if(sertime.equals(seritemlist.get(i).getName())){
				serdaylist=seritemlist.get(i).getDaylist();
				backflag=true;
			}
		}
	}



	@Override
	public int getCount() {
		return sortdaylist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return sortdaylist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View con, ViewGroup arg2) {
		ViewHolder vh;
		if(con==null){
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			con=inflater.inflate(R.layout.calendar_calendar_gv_item,null);
			
			vh=new ViewHolder();
			ViewUtils.inject(vh, con);
			con.setTag(vh);
		}else{
			vh=(ViewHolder) con.getTag();
		}
		if("1".equals(sortdaylist.get(position))&&flag==0){
			flag=1;
		}else if("1".equals(sortdaylist.get(position))&&flag==1){
			flag=0;
		}
		if(flag==0){
			vh.tv.setText(sortdaylist.get(position));
			vh.tv.setTextColor(R.color.calendar_week_text_not);
			vh.tv.setFocusableInTouchMode(false);
		}else{
			String text=sortdaylist.get(position);
			int textint=new Integer(text);
			vh.tv.setText(text);
			//是否从服务器获得的数�?
			if(serdaylist!=null&&serdaylist.size()!=0){
				
			for(int i=0;i<serdaylist.size();i++){
				String day=serdaylist.get(i).getDate().substring(8);
				int dayint=new Integer(day);
				if(textint==dayint){
					vh.iv.setVisibility(View.VISIBLE);
					if(backflag){
						vh.tv.setBackgroundResource(R.drawable.calendar_day_circle);
						backflag=false;
						currenttextview=vh.tv;
					}
					isCon=true;
					break;
				}
			}
			}
			
			vh.tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					boolean b=false;
					TextView tv=(TextView) view;
					String text=tv.getText().toString();
					int textint=new Integer(text);
					
					if(serdaylist!=null&&serdaylist.size()!=0){
					for(int i=0;i<serdaylist.size();i++){
						String day=serdaylist.get(i).getDate().substring(8);
						int dayint=new Integer(day);
						if(textint==dayint){
							Toast.makeText(context, text+"dianji de shi",0).show();
							b=true;
							break;
						}
					}
					}
					if(b){
						if(currenttextview==null){
							tv.setBackgroundResource(R.drawable.calendar_day_circle);
							currenttextview=tv;
						}else{
							currenttextview.setBackgroundColor(Color.WHITE);
							tv.setBackgroundResource(R.drawable.calendar_day_circle);
							currenttextview=tv;
						}
						
					}else{
						Toast.makeText(context,"暂无相关内容",0).show();
					}
					
					
				}
			});
		    
		}
		
		
		isCon=false;
		
		
		
		
		
		return con;
	}

	class ViewHolder{
		@ViewInject(R.id.calendar_calendar_gv_item_tv)
		TextView tv;
		@ViewInject(R.id.calendar_calendar_gv_item_iv)
		ImageView iv;
	}
}
