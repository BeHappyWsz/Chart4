package lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Demo {

//	public synchronized void method1() {
//		method2();
//		method3();
//		method4();
//	}
//	public void method2() {
//		synchronized(this) {
//			//doSomthing
//		}
//	}
//	public void method3() {}
//	public void method4() {}
	
	public void method1() {
		synchronized(this) {
			method2();
		}
		method3();
		method4();
	}
	public void method2() {}
	
	private ReentrantLock lock = new ReentrantLock();
	public void method3() {
		int size = 500;
		synchronized(lock) {
			for(int i = 0; i < size ; i++) {
				//doSomething
			}
		}
	}
	
	public void method4() {
		ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
		map.put("", "");
	}
	
	
}
