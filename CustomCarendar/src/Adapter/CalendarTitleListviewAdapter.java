package Adapter;

import java.util.List;


import com.example.customcarendar.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarTitleListviewAdapter extends BaseAdapter {
	
	
	private Context context;
	private List<String> list;
	private Button btn;
	private String TAG="CalendarTitleListviewAdapter";
	
	public CalendarTitleListviewAdapter(Context context,List<String> list,Button btn){
		this.context=context;
		this.list=list;
		this.btn=btn;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View con, ViewGroup arg2) {
		ViewHolder vh;
		if(con==null){
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			con=inflater.inflate(R.layout.calendar_title_popupwindow_item,null);
			vh=new ViewHolder();
			ViewUtils.inject(vh, con);
			con.setTag(vh);
		}else{
			vh=(ViewHolder) con.getTag();
		}
		String text=list.get(pos);
		String currenttext=btn.getText().toString();
		if(text.equals(currenttext)){
			
			vh.tv.setText(text);
			vh.tv.setTextAppearance(context, R.style.calendar_title_pop_sel);
			vh.iv.setVisibility(View.VISIBLE);
		}else{
			vh.tv.setText(text);
			vh.tv.setTextAppearance(context, R.style.calendar_title_pop_nor);
			vh.iv.setVisibility(View.INVISIBLE);
		}
		
		return con;
	}

	class ViewHolder{
		@ViewInject(R.id.calendar_title_pop_item_tv)
		TextView tv;
		@ViewInject(R.id.calendar_title_pop_item_iv)
		ImageView iv;
	}
}
