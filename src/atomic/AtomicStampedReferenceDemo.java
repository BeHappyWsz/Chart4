package atomic;

import java.util.concurrent.atomic.AtomicStampedReference;
/**
 * AtomicStampedReference带有时间戳，由此可判断是否被其他线程修改
 * 此案例将只能新增一次数据
 * @author wsz
 * @date 2017年12月21日
 */
public class AtomicStampedReferenceDemo {
	
	static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19, 0);
	
	public static void main(String[] args) {
		
		for(int i = 0; i <3; i++) {//线程进行充值
			final int stamp = money.getStamp();//充值前，final时间戳，后面充值将修改一次时间戳（+1）；
											  //后续操作的期望时间戳仍为第一次设置的值，但时间戳已被修改，无法满足。
			new Thread() {
				public void run() {
					while(true) {
						while(true) {
							Integer m = money.getReference();
							if(m < 20) {
								if(money.compareAndSet(m, m+20, stamp, stamp+1)) {//期望值、写入新值、期望时间戳、新时间戳
									System.out.println("余额小于20，充值成功，余额："+money.getReference());
									break;
								}
							}else {
//								System.out.println("余额大于20");
								break;
							}
						}
					}
				}
			}.start();
		}
		new Thread() {
			public void run() {
				for(int i = 0 ; i < 100 ; i++) {
					while(true) {
						int stamp = money.getStamp();//获取时间戳
						Integer m = money.getReference();//获取值
						if(m > 10) {
							System.out.println("大于10");
							if(money.compareAndSet(m, m-10, stamp, stamp+1)) {
								System.out.println("消费10元，剩余："+money.getReference());
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
