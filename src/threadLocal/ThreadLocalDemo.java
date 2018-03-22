package threadLocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * ThreadLocal:
 * 1.判断当前线程是否持有使用的对象实例
 * 2.如果不持有则new新建一个并设置到当前线程中进行使用
 * 3.该功能的实现需要在应用层面进行保证
 * @author wsz
 * @date 2017年12月20日
 */

/*
     1.获取当前线程
     2.获取线程的ThreadLocalMap
     3.设值到ThreadLocalMap中
     public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
 
 	 1.获取当前线程对象
 	 2.获取线程的ThreadLocalMap
 	 3.线程作为key获取实际的对象等数据
     public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }

	
 */
public class ThreadLocalDemo {

	//SimpleDateFormat,parse()不是线程安全的
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>();
	
	public class Demo2 implements Runnable{
		private int i;
		
		public Demo2(int i) {
			this.i = i;
		}
		@Override
		public void run() {
			if(tl.get() == null)//如果当前线程不持有该对象实例，则新建一个并设置到当前线程中
				tl.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			try {
				Date date = tl.get().parse("2017-12-20 20:12:"+i%60);
				System.out.println(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class Demo1 implements Runnable{
		private int i;
		public Demo1(int i) {
			this.i = i;
		}
		@Override
		public void run() {
			try {
				Date date = sdf.parse("2017-12-20 20:12:"+i%60);
				System.out.println(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		ExecutorService ftp = Executors.newFixedThreadPool(10);
		for(int i = 0 ; i< 5000 ; i++) {
//			ftp.execute(new ThreadLocalDemo().new Demo1(i));
			ftp.execute(new ThreadLocalDemo().new Demo2(i));
		}
	}
}
