package test.com.yatang.xc.xcr.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class User {
	private String userName;

	private Integer grade;

	private Bige bige;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Bige getBige() {
		return bige;
	}

	public void setBige(Bige bige) {
		this.bige = bige;
	}

}

class Bige {

	private String wocalei;

	private String woqu;

	public String getWocalei() {
		return wocalei;
	}

	public void setWocalei(String wocalei) {
		this.wocalei = wocalei;
	}

	public String getWoqu() {
		return woqu;
	}

	public void setWoqu(String woqu) {
		this.woqu = woqu;
	}

}

class aaa {

	private List<User> user;

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

}

 class TestJso {

	/*public static void main(String[] args) {
		User user = new User();
		Bige bige = new Bige();
		bige.setWocalei("WOCAOLEI");
		bige.setWoqu("WOLEGEQU");
		user.setBige(bige);
		user.setGrade(100);
		user.setUserName("ZHONGR");

		User user1 = new User();
		Bige bige1 = new Bige();
		bige1.setWocalei("WOCAOLEI");
		bige1.setWoqu("WOLEGEQU");
		user1.setBige(bige);
		user1.setGrade(100);
		user1.setUserName("ZHONGR");

		User user2 = new User();
		Bige bige2 = new Bige();
		bige2.setWocalei("WOCAOLEI");
		bige2.setWoqu("WOLEGEQU");
		user2.setBige(bige);
		user2.setGrade(100);
		user2.setUserName("ZHONGR");
		aaa a = new aaa();
		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(user1);
		users.add(user2);
		a.setUser(users);

		System.out.println(JSONObject.toJSON(a));

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(simpleDateFormat.format(new Date()).toString());

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());
		System.out.println("===============first:" + first);
		System.out.println(format.format(A.getDateBefore(new Date(), 7)));
	}*/

}

class A {
	static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}
}




public  class TestJson{
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		TestJson threed = new TestJson();
		threed.mnm();
	}
	
	public String getString(){
		String i = "6";
		
		return i;
	}
	
	public void mnm() throws InterruptedException, ExecutionException{
		ExecutorService es=Executors.newFixedThreadPool(5);
		for(int i=0;i<5;i++){
            Callable<String> tc=new Callable<String>() {
				@Override
				public String call() throws Exception {
					String s = getString();
					return s;
				}
			};
			
			
			 Callable<String> td=new Callable<String>() {
					@Override
					public String call() throws Exception {
						String s = getString();
						return s;
					}
				};
				
				
				
				 Callable<String> te=new Callable<String>() {
						@Override
						public String call() throws Exception {
							String s = getString();
							return s;
						}
					};
					
					
					
					 Callable<String> tf=new Callable<String>() {
							@Override
							public String call() throws Exception {
								String s = getString();
								return s;
							}
						};
						
						
						 Callable<String> tg=new Callable<String>() {
								@Override
								public String call() throws Exception {
									String s = getString();
									return s;
								}
							};
			
			
			 Future f2 = es.submit(tc);
			 Future f3 = es.submit(td);
			 Future f4 = es.submit(tf);
			 Future f5 = es.submit(te);
			 Future f6 = es.submit(tg);
			 
			 
			 System.out.println(f2.get().toString()+f3.get().toString()+f4.get().toString()+f5.get().toString()+f6.get().toString());
        }
        es.shutdown();
		
		
		
		
		
		
	}
}