package ch.ethz.coss.diasclient.listenerTests;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ListenerUnitTest {

	HashMapTest hashMapTest = new HashMapTest();

	HashMap<String,Integer> testHashMap (HashMap<String, Integer> a, HashMap<String,Integer> b) {
		a.put("bashA", 272);
		b=new HashMap<String, Integer>();
		b.put("cashA", 273);
		return b;
	}

	@Test
	public void addition_isCorrect() throws Exception {
		assertEquals(4, 2 + 2);
		System.out.println("A" + hashMapTest.a);
		System.out.println("B" + hashMapTest.b);
		System.out.println("B" + testHashMap(hashMapTest.a,hashMapTest.b));
		System.out.println("A" + hashMapTest.a);
		System.out.println("B" + hashMapTest.b);


	}



}

class HashMapTest {
	public HashMap<String, Integer> a = new HashMap<String, Integer>();
	public HashMap<String, Integer> b = new HashMap<String, Integer>();

	public HashMapTest(){
		a.put("hashA", 271);
		b.put("hashB", 172);
	}
}