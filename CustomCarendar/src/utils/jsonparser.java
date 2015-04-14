package utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.calendarDayBean;
import bean.calendarItemBean;

import android.util.Log;



public class jsonparser {

	//��������������������
	
		public static List<calendarItemBean> parserDayBean(String res){
			List<calendarItemBean> list=new ArrayList<calendarItemBean>();
			try {
				JSONObject jsonobj=new JSONObject(res);
				JSONArray itemarr=jsonobj.getJSONArray("item");
				for(int i=0;i<itemarr.length();i++){
					calendarItemBean cib=new calendarItemBean();
					JSONObject itemobj=itemarr.getJSONObject(i);
					cib.setName(itemobj.getString("name"));
					JSONArray dayarr=itemobj.getJSONArray("days");
					List<calendarDayBean> dlist=new ArrayList<calendarDayBean>();
					for(int n=0;n<dayarr.length();n++){
						calendarDayBean cdb=new calendarDayBean();
					JSONObject dobj=dayarr.getJSONObject(n);
					cdb.setDate(dobj.getString("date"));
					cdb.setSum(dobj.getString("sum"));
					cdb.setDefProjId(dobj.getString("defProjId"));
					dlist.add(cdb);
					}
					cib.setDaylist(dlist);
					list.add(cib);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			
			
			return list;
		}
}
