package learning.demo;


import jdk.nashorn.internal.runtime.arrays.IteratorAction;
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
        BinaryTree<Integer> binaryTree = new BinarySearchTree();
		System.out.println("isEmpty    " + binaryTree.isEmpty());
        binaryTree.add(6);
		binaryTree.add(3);
		binaryTree.add(1);
		binaryTree.add(2);
		binaryTree.add(5);
		binaryTree.add(4);
		binaryTree.add(9);
		binaryTree.add(7);
		binaryTree.add(8);
		Iterator<Integer> iterator = binaryTree.iterator();
		System.out.println("=======迭代开始=========");
		while(iterator.hasNext()) {
			int data = iterator.next();
			if(data == 6) {
				iterator.remove();
			}
			System.out.println(data);
		}
		System.out.println(binaryTree.contains(6));
    }
}
