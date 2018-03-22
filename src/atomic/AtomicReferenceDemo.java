package atomic;

import java.util.concurrent.atomic.AtomicReference;
/**
 * 初始账户19元；当小于20元时，每次充值20元，且只能最多充值一次；多于10元时，每次消费10元。
 * 使用AtomicReference将会被重复充值，不符合实际情况
 * @author wsz
 * @date 2017年12月21日
 */
public class AtomicReferenceDemo {
	//定义账户
	static AtomicReference<Integer> money = new AtomicReference<Integer>();
	
	public static void main(String[] args) {
		//初始账户有19元
		money.set(19);
		
		for(int i = 0; i <20; i++) {//线程进行充值
			new Thread() {
				public void run() {
					while(true) {
						Integer m = money.get();
						if(m < 20) {
							if(money.compareAndSet(m, m+20)) {//每次充值新增20
								System.out.println("少于20，充值成功，目前有："+money.get());
								break;
							}
						}else {
							System.out.println("超过20");
							break;
						}
					}
				}
			}.start();
		}
		new Thread() {
			public void run() {
				for(int i = 0 ; i < 100 ; i++) {
					while(true) {
						Integer m = money.get();
						if(m > 10) {
							System.out.println("多于10");
							if(money.compareAndSet(m, m-10)) {
								System.out.println("消费10元，剩余："+money.get());
								break;
							}
						}else {
							System.out.println("小于10元");
							break;
						}
					}
				}
				
			}
		}.start();
	}

}
