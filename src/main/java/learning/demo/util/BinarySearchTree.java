package learning.demo.util;

import java.util.*;

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
		int depth = 0;
		Node node = root;
		// 使用队列存储二叉树的每一层的节点，
		Queue<Node> queue = new LinkedList<>();
		if(node != null) {
			queue.add(node);
		}
		// 使用队列实现二叉树层序遍历，并结算深度
		while(!queue.isEmpty()) {
			depth++;
			int length = queue.size();
			while(length-- > 0) {
				node = queue.poll();
				if(node.leftChild != null) {
					queue.add(node.leftChild);
				}
				if(node.rightChild != null) {
					queue.add(node.rightChild);
				}
			}
		}
		return depth;
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
			throw new RuntimeException("the object can not compare, place implement java.lang.Comparable");
		}
		return contains((E)o) != null;
	}

	/**
	 * 查找元素 e，如果存在返回就该节点（node），不存在返回null
	 * @param e
	 * @return
	 */
	private Node contains(E e) {
		Node node = root;
		while(node != null) {
			int result = e.compareTo(node.data);
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
			removeNode(node);
		}
		return (E)node.data;
	}

	/**
	 * 删除树中的节点，删除后树依然是二叉搜索树
	 * @param node
	 */
	private void removeNode(Node node) {
		modCount++;
		size--;
		if(node.leftChild != null && node.rightChild != null) {
			Node minNode = minNode(node.rightChild);
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

	@Override
	public void clear() {
		this.root = null;
		modCount++;
		size = 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
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

	private class Itr<E extends Comparable> implements Iterator<E> {

		// 迭代时最后访问的元素
		Node<E> lastAccess;
		// fail-fast
		int expectedModCount;
		// 迭代时用栈暂时存储数据
		Stack<Node> stack;

		public Itr() {
			expectedModCount = modCount;
			stack = new Stack<>();
			Node node = BinarySearchTree.this.root;
			while(node != null) {
				stack.push(node);
				node = node.leftChild;
			}
		}

		@Override
		public boolean hasNext() {
			return stack.size() != 0;
		}

		@Override
		public E next() {
			checkModCount();
			if(stack.size() != 0) {
				lastAccess = stack.pop();
				Node node;
				if(lastAccess.rightChild != null) {
					node = lastAccess.rightChild;
					stack.push(node);
					while(node.leftChild != null) {
						node = node.leftChild;
						stack.push(node);
					}
				}
				return lastAccess.data;
			}
			return null;
		}

		@Override
		public void remove() {
			// 调用删除方法前要先访问一个元素，否则抛出异常
			if(lastAccess == null) {
				throw new NullPointerException("the last access element is not exist");
			}
			checkModCount();
			BinarySearchTree.this.removeNode(lastAccess);
			expectedModCount = modCount;
		}

		/**
		 * fail-fast
		 */
		final void checkModCount() {
			if(expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
		}
	}

	private class Node<E extends Comparable> {

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
