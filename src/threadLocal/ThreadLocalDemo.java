package threadLocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * ThreadLocal:
 * 1.�жϵ�ǰ�߳��Ƿ����ʹ�õĶ���ʵ��
 * 2.�����������new�½�һ�������õ���ǰ�߳��н���ʹ��
 * 3.�ù��ܵ�ʵ����Ҫ��Ӧ�ò�����б�֤
 * @author wsz
 * @date 2017��12��20��
 */

/*
     1.��ȡ��ǰ�߳�
     2.��ȡ�̵߳�ThreadLocalMap
     3.��ֵ��ThreadLocalMap��
     public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
 
 	 1.��ȡ��ǰ�̶߳���
 	 2.��ȡ�̵߳�ThreadLocalMap
 	 3.�߳���Ϊkey��ȡʵ�ʵĶ��������
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

	//SimpleDateFormat,parse()�����̰߳�ȫ��
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>();
	
	public class Demo2 implements Runnable{
		private int i;
		
		public Demo2(int i) {
			this.i = i;
		}
		@Override
		public void run() {
			if(tl.get() == null)//�����ǰ�̲߳����иö���ʵ�������½�һ�������õ���ǰ�߳���
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
