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
        BinaryTree<Integer> binaryTree = new BalancedBinaryTree<>();
		System.out.println(binaryTree.depth());
    }
}
