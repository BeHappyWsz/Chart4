package threadLocal;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 测试ThreadLocal对性能的影响
 * @author wsz
 * @date 2017年12月20日
 */
public class ThreadLocalEffic {
	public static final int NUM = 2000000;//每个线程要产生的随机数数量
	public static final int SIZE = 5;     //参与工作的线程数量
	static ExecutorService exe = Executors.newFixedThreadPool(SIZE);//固定数量的线程池
	//多线程共享对象
	public static Random rnd = new Random(123);	
	//ThreadLocal对象封装的Random
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
		
		public Random getRandom() {//分类进行返回不同的随机数对象
			if(model == 0) {
				return rnd;
			}else if(model == 1) {
				return trnd.get();
			}else {
				return null;
			}
		}
		
		//消耗的时间
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
		//普通Random
		for (int i = 0; i < SIZE; i++) {
			futs[i] = exe.submit(new ThreadLocalEffic().new RndTest(0));
		}

		int totaltime = 0;
		for(int i =0; i< SIZE; i++) {
			totaltime += futs[i].get();
		}
		System.out.println("多线程访问同一个Random实例："+totaltime+"ms");

		//ThreadLocal的Random
		for (int i = 0; i < SIZE; i++) {
			futs[i] = exe.submit(new ThreadLocalEffic().new RndTest(1));
		}

		totaltime = 0;
		for(int i =0; i< SIZE; i++) {
			totaltime += futs[i].get();
		}
		System.out.println("多线程访问同一个Random实例："+totaltime+"ms");
		exe.shutdown();
	}

}
