package learning.demo;

import java.util.Iterator;
import java.util.Stack;

/**
 * @ClassName BinarySearchTree
 * @Description java实现二叉搜索树,在插入数据是会自动排序，迭代器遍历结果为升序
 * @Author Numblgw
 * @Date 2019/4/15 10:01
 */
public class BinarySearchTree<E extends Comparable> implements BinaryTree<E> {

	/**
	 * 结构修改次数，用于（fail-fast）
	 */
	private int modCount;

	/**
	 * 根节点
	 */
	private Node root;

	/**
	 * 二叉树中的节点个数
	 */
	private int size;

	@Override
	public int depth() {
		int maxDepth = 0;
		int currentDepth = 0;
		Node node = root;
		Stack<Node> stack = new Stack<>();
		while(node != null) {
			while(node != null) {
				maxDepth = ++currentDepth > maxDepth ? currentDepth : maxDepth;
				stack.push(node);
				node = node.leftChild;
			}
			if(node == null && !stack.empty()) {
				node = stack.pop().rightChild;
				--currentDepth;
			}
		}
		return 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		if(!(o instanceof Comparable)) {
			throw new RuntimeException("the object can not compare, place implements java.lang.Comparable");
		}
		return contains((E)o) != null;
	}

	/**
	 * 查找元素 e，如果存在返回就该节点（node），不存在返回null
	 * @param e
	 * @return
	 */
	private Node contains(E e){
		Node node = root;
		while(node != null) {
			int result = ((Comparable) e).compareTo(node.data);
			if(result < 0) {
				node = node.leftChild;
			}else if(result > 0) {
				node = node.rightChild;
			}else {
				return node;
			}
		}
		return null;
	}

	@Override
	public boolean add(E e) {
		if(e == null) {
			throw new NullPointerException("can not add 'null' to the binary search tree");
		}
		modCount++;
		Node node = root;
		if(node == null) {
			root = new Node(e);
			size = 1;
		}
		while(node != null) {
			int result = e.compareTo(node.data);
			if(result < 0) {
				if(node.leftChild != null) {
					node = node.leftChild;
				}else {
					size++;
					node.leftChild = new Node(node, e);
					break;
				}
			}else if(result > 0) {
				if(node.rightChild != null) {
					node = node.rightChild;
				}else {
					size++;
					node.rightChild = new Node(node, e);
					break;
				}
			}else {
				break;
			}
		}
		return true;
	}

	@Override
	public E remove(Object o) {
		// 需要删除的节点
		Node node = contains((E)o);
		if(node != null) {
			modCount++;
			size--;
			if(node.leftChild != null && node.rightChild != null) {
				Node minNode = minNode(node);
				node.data = minNode.data;
				node = minNode;
			}
			Node childNode = node.leftChild != null ? node.leftChild : node.rightChild;
			if(childNode != null) {
				if(node == node.parent.leftChild) {
					node.parent.leftChild = childNode;
					childNode.parent = node.parent;
				}else if(node == node.parent.rightChild) {
					node.parent.rightChild = childNode;
					childNode.parent = node.parent;
				}
			}else {
				if(node == node.parent.leftChild) {
					node.parent.leftChild = null;
				}else {
					node.parent.rightChild = null;
				}
			}
		}
		return (E)node.data;
	}

	@Override
	public void clear() {
		this.root = null;
		modCount++;
		size = 0;
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	/**
	 * 获得该二叉搜索树中的最小节点
	 * @param root
	 * @return  树中最小元素所在的节点（Node）
	 */
	private Node<E> minNode(Node root) {
		while(root != null && root.leftChild != null) {
			root = root.leftChild;
		}
		return root;
	}

	protected class Node<E extends Comparable> {

		/**
		 * 父节点
		 */
		Node parent;

		/**
		 * 左子树
		 */
		Node leftChild;

		/**
		 * 右子树
		 */
		Node rightChild;

		/**
		 * 数据
		 */
		E data;

		protected Node(E data) {
			this.data = data;
		}

		protected Node(Node parent, E data) {
			this.parent = parent;
			this.data = data;
		}
	}
}
