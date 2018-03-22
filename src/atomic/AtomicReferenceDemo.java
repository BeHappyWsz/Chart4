package atomic;

import java.util.concurrent.atomic.AtomicReference;
/**
 * ��ʼ�˻�19Ԫ����С��20Ԫʱ��ÿ�γ�ֵ20Ԫ����ֻ������ֵһ�Σ�����10Ԫʱ��ÿ������10Ԫ��
 * ʹ��AtomicReference���ᱻ�ظ���ֵ��������ʵ�����
 * @author wsz
 * @date 2017��12��21��
 */
public class AtomicReferenceDemo {
	//�����˻�
	static AtomicReference<Integer> money = new AtomicReference<Integer>();
	
	public static void main(String[] args) {
		//��ʼ�˻���19Ԫ
		money.set(19);
		
		for(int i = 0; i <20; i++) {//�߳̽��г�ֵ
			new Thread() {
				public void run() {
					while(true) {
						Integer m = money.get();
						if(m < 20) {
							if(money.compareAndSet(m, m+20)) {//ÿ�γ�ֵ����20
								System.out.println("����20����ֵ�ɹ���Ŀǰ�У�"+money.get());
								break;
							}
						}else {
							System.out.println("����20");
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
							System.out.println("����10");
							if(money.compareAndSet(m, m-10)) {
								System.out.println("����10Ԫ��ʣ�ࣺ"+money.get());
								break;
							}
						}else {
							System.out.println("С��10Ԫ");
							break;
						}
					}
				}
				
			}
		}.start();
	}

}
