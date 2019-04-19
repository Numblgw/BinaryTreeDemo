package learning.demo;


import jdk.nashorn.internal.runtime.arrays.IteratorAction;
import learning.demo.util.BalancedBinaryTree;
import learning.demo.util.BinarySearchTree;
import learning.demo.util.BinaryTree;

import javax.xml.soap.Node;
import java.util.Iterator;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        BinaryTree<Integer> binaryTree = new BinarySearchTree<>();
		System.out.println(binaryTree.depth());
		binaryTree.add(0);
		binaryTree.add(1);
		binaryTree.add(6);
		binaryTree.add(10);
		binaryTree.add(4);
		binaryTree.add(3);
		binaryTree.add(5);
		binaryTree.add(2);
		Iterator iterator = binaryTree.iterator();
		Integer i = null;
		while((i = (Integer) iterator.next()) != null) {
			System.out.println(i);
		}
    }
}
