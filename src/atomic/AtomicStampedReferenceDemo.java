package atomic;

import java.util.concurrent.atomic.AtomicStampedReference;
/**
 * AtomicStampedReference����ʱ������ɴ˿��ж��Ƿ������߳��޸�
 * �˰�����ֻ������һ������
 * @author wsz
 * @date 2017��12��21��
 */
public class AtomicStampedReferenceDemo {
	
	static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19, 0);
	
	public static void main(String[] args) {
		
		for(int i = 0; i <3; i++) {//�߳̽��г�ֵ
			final int stamp = money.getStamp();//��ֵǰ��finalʱ����������ֵ���޸�һ��ʱ�����+1����
											  //��������������ʱ�����Ϊ��һ�����õ�ֵ����ʱ����ѱ��޸ģ��޷����㡣
			new Thread() {
				public void run() {
					while(true) {
						while(true) {
							Integer m = money.getReference();
							if(m < 20) {
								if(money.compareAndSet(m, m+20, stamp, stamp+1)) {//����ֵ��д����ֵ������ʱ�������ʱ���
									System.out.println("���С��20����ֵ�ɹ�����"+money.getReference());
									break;
								}
							}else {
//								System.out.println("������20");
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
						int stamp = money.getStamp();//��ȡʱ���
						Integer m = money.getReference();//��ȡֵ
						if(m > 10) {
							System.out.println("����10");
							if(money.compareAndSet(m, m-10, stamp, stamp+1)) {
								System.out.println("����10Ԫ��ʣ�ࣺ"+money.getReference());
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
