package _Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ 
	list.DLinkedListTest.class,
	list.SLinkedListTest.class, 
	list.MyArrayListTest.class,
	stacknqueue.StackTest.class, 
	stacknqueue.QueueTest.class, 
	stacknqueue.PriorityQueueTest.class,
	hash.XHashMapTest.class })
public class AssignmentTestSuite {
 
}

