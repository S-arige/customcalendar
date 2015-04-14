package bean;

import java.util.List;

public class calendarItemBean {

	private String name;
	private List<calendarDayBean> daylist;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<calendarDayBean> getDaylist() {
		return daylist;
	}
	public void setDaylist(List<calendarDayBean> daylist) {
		this.daylist = daylist;
	}
	
}
