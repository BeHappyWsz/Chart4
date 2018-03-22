package threadLocal;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ����ThreadLocal�����ܵ�Ӱ��
 * @author wsz
 * @date 2017��12��20��
 */
public class ThreadLocalEffic {
	public static final int NUM = 2000000;//ÿ���߳�Ҫ���������������
	public static final int SIZE = 5;     //���빤�����߳�����
	static ExecutorService exe = Executors.newFixedThreadPool(SIZE);//�̶��������̳߳�
	//���̹߳������
	public static Random rnd = new Random(123);	
	//ThreadLocal�����װ��Random
	public static ThreadLocal<Random> trnd = new ThreadLocal<Random>() {
		@Override
		protected Random initialValue() {
			return new Random(123);
		}
	};
	
	public class RndTest implements Callable<Long>{
		private int model = 0;
		
		public RndTest(int model) {
			this.model = model;
		}
		
		public Random getRandom() {//������з��ز�ͬ�����������
			if(model == 0) {
				return rnd;
			}else if(model == 1) {
				return trnd.get();
			}else {
				return null;
			}
		}
		
		//���ĵ�ʱ��
		@Override
		public Long call() throws Exception {
			long b = System.currentTimeMillis();
			for(int i= 0 ;i < NUM ; i++) {
				getRandom().nextInt();
			}
			long e = System.currentTimeMillis();
			System.out.println(Thread.currentThread().getClass()+" take "+(e-b)+"ms");
			return e-b;
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Future<Long>[] futs = new Future[SIZE];
		//��ͨRandom
		for (int i = 0; i < SIZE; i++) {
			futs[i] = exe.submit(new ThreadLocalEffic().new RndTest(0));
		}

		int totaltime = 0;
		for(int i =0; i< SIZE; i++) {
			totaltime += futs[i].get();
		}
		System.out.println("���̷߳���ͬһ��Randomʵ����"+totaltime+"ms");

		//ThreadLocal��Random
		for (int i = 0; i < SIZE; i++) {
			futs[i] = exe.submit(new ThreadLocalEffic().new RndTest(1));
		}

		totaltime = 0;
		for(int i =0; i< SIZE; i++) {
			totaltime += futs[i].get();
		}
		System.out.println("���̷߳���ͬһ��Randomʵ����"+totaltime+"ms");
		exe.shutdown();
	}

}
