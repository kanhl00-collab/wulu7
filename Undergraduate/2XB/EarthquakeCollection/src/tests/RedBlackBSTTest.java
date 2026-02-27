package tests;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.search.RedBlackBST;

public class RedBlackBSTTest {
	private RedBlackBST bst;

	@Before
	public void setUp() throws Exception {
		bst = new RedBlackBST<String, Integer>();
	}

	@Test
	public void testSize() {
		assertEquals(bst.size(),0);
	}

	@Test
	public void testIsEmpty() {
		assertTrue(bst.isEmpty());
	}

	@Test
	public void testGet() {
		bst.put("First", 1);
		bst.put("First", 2);
		assertEquals(bst.get("First").size(),2);
	}
	
	@Test
	public void testGet2() {
		bst.put("First", 1);
		bst.put("First", 2);
		bst.put("Second", 2);
		assertEquals(bst.get("Second").size(),1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGet3() {
		bst.put(null, 1);
	}

	@Test
	public void testPut() {
		bst.put("First", 1);
		assertFalse(bst.isEmpty());
	}
	
	@Test
	public void testPut2() {
		bst.put("First", 1);
		bst.put("First", 2);
		assertEquals(bst.size(),1);
	}
	
	@Test
	public void testPut3() {
		bst.put("First", 1);
		bst.put("Second", 2);
		assertEquals(bst.size(),2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPut4() {
		bst.put(null, 1);
	}

	@Test
	public void testMin() {
		bst.put("First", 1);
		bst.put("Second", 2);
		assertEquals(bst.min(),"First");
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testMin2() {
		bst.min();
	}

	@Test
	public void testMax() {
		bst.put("First", 1);
		bst.put("Second", 2);
		assertEquals(bst.max(),"Second");
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testMax2() {
		bst.max();
	}

}
