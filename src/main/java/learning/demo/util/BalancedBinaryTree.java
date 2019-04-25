package learning.demo.util;

import java.util.*;

/**
 * @ClassName BalancedBinaryTree
 * @Description java实现平衡二叉树
 * @Author Numblgw
 * @Date 2019/4/16 16:52
 */
public class BalancedBinaryTree<E extends Comparable> implements BinaryTree<E> {

	/**
	 * 左旋
	 *
	 * 		第一种情况
	 *
	 * 	 1
	 * 	  \                     2
	 * 	   2        --->      /  \
	 * 	   	\                1    3
	 * 	   	 3
	 *
	 * 		第二种情况
	 *
	 *     2
	 *    / \                    4
	 *   1   4                 /  \
	 *      / \     --->      2    5
	 *     3   5            /  \    \
	 *          \          1   3     6
	 *           6
	 *
	 * 先右旋再左旋
	 *
	 *   1             1
	 *    \             \                 2
	 *     3   --->      2     --->      / \
	 *    /               \             1   3
	 *   2                 3
	 *
	 *
	 *
	 */

	/**
	 * 修改次数，用以实现 fail-fast机制
	 */
	private int modCount;

	/**
	 * 树根
	 */
	private Node root;

	/**
	 * 树中节点个数
	 */
	private int size;

	@Override
	public int depth() {
		return depth(this.root);
	}


	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		if(!(o instanceof Comparable)) {
			throw new RuntimeException("this object can not compare, place implement java.lang.Comparable");
		}
		return contains((E)o) != null;
	}

	@Override
	public boolean add(E e) {
		if(e == null) {
			throw new NullPointerException("can not add 'null' to the binary search tree");
		}
		modCount++;
		// 计算插入位置并插入节点
		Node node = addNode(e);
		// 检查并维护平衡
		checkAndAdjustBalance(node);
		return true;
	}

	@Override
	public E remove(Object o) {
		Node node = contains((E) o);
		if(node != null) {
			removeNode(node);
			return (E)node.data;
		}
		return null;
	}

	@Override
	public void clear() {
		this.root = null;
		modCount++;
		size = 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr<>();
	}

	/**
	 * 计算该子树的深度
	 * @param root	树根
	 * @return	深度（int）
	 */
	private int depth(Node root) {
		int depth = 0;
		// 使用队列存储二叉树的每一层的节点，
		Queue<Node> queue = new LinkedList<>();
		if(root != null) {
			queue.add(root);
		}
		// 使用队列实现二叉树层序遍历，并结算深度
		while(!queue.isEmpty()) {
			depth++;
			int length = queue.size();
			while(length-- > 0) {
				root = queue.poll();
				if(root.left != null) {
					queue.add(root.left);
				}
				if(root.right != null) {
					queue.add(root.right);
				}
			}
		}
		return depth;
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
				node = node.left;
			}else if(result > 0) {
				node = node.right;
			}else {
				return node;
			}
		}
		return null;
	}

	/**
	 * 计算插入位置并插入节点
	 * @param e	节点中的数据
	 * @return	返回被插入的节点，用来从下往上检查平衡性
	 */
	private Node addNode(E e) {
		Node node = root;
		// 树为空时直接插入根节点
		if(node == null) {
			root = new Node(e);
			size = 1;
			return root;
		}
		while(node != null) {
			int result = e.compareTo(node.data);
			if(result < 0) {
				if(node.left != null) {
					node = node.left;
				}else {
					size++;
					node.left = new Node(node, e);
					node = node.left;
					break;
				}
			}else if(result > 0) {
				if(node.right != null) {
					node = node.right;
				}else {
					size++;
					node.right = new Node(node, e);
					node = node.right;
					break;
				}
				// 插入节点已存在时不做插入操作，但是 modCount 会自增
			}else {
				break;
			}
		}
		return node;
	}

	/**
	 * 从node节点开始 向上查找失去平衡的节点，得到最小不平衡子树的树根，并调整平衡
	 * @param node	开始查找的节点
	 */
	private void checkAndAdjustBalance(Node node) {
		// 平衡因子
		int balanceFactor = 0;
		while(node.parent != null) {
			node = node.parent;
			balanceFactor = calculateBalanceFactor(node);
			if(balanceFactor == -2 || balanceFactor == 2) {
				break;
			}
		}
		if(balanceFactor == 2 || balanceFactor == -2) {
			// 左旋或者右旋维持平衡
			if(balanceFactor == 2) {
				if(calculateBalanceFactor(node.left) == -1) {
					// 先左旋
					leftRotate(node.left);
				}
				// 右旋
				rightRotate(node);
			}else {
				if(calculateBalanceFactor(node.right) == 1) {
					// 先右旋
					rightRotate(node.right);
				}
				// 左旋
				leftRotate(node);
			}
			// 检查root 是否指向根节点
			checkRoot();
		}
	}

	/**
	 * 计算该树的平衡因子，平衡因子为 左子树深度 减 右子树深度
	 * @param root	需要计算平衡因子的树的树根
	 * @return 平衡因子
	 */
	private int calculateBalanceFactor(Node root) {
		int leftDepth = depth(root.left);
		int rightDepth = depth(root.right);
		return leftDepth - rightDepth;
	}

	/**
	 * 左旋
	 * @param n
	 */
	private void leftRotate(Node n) {
		// 先把
		n.right.parent = n.parent;
		if(n.parent != null) {
			if(n.parent.left == n) {
				n.parent.left = n.right;
			}else {
				n.parent.right = n.right;
			}
		}else {
			this.root = n.right;
		}
		n.parent = n.right;
		n.right = n.parent.left;
		if(n.right != null) {
			n.right.parent = n;
		}
		n.parent.left = n;
	}

	/**
	 * 右旋
	 * @param n
	 */
	private void rightRotate(Node n) {
		n.left.parent = n.parent;
		if(n.parent != null) {
			if(n.parent.left == n) {
				n.parent.left = n.left;
			}else {
				n.parent.right = n.left;
			}
		}
		n.parent = n.left;
		n.left = n.parent.right;
		if(n.left != null) {
			n.left.parent = n;
		}
		n.parent.right = n;
	}

	/**
	 * 当进行完旋转之后 root 可能并不是指向 树根，需要调用该方法检查并修正
	 */
	private void checkRoot() {
		Node node = this.root;
		while(node.parent != null) {
			node = node.parent;
		}
		this.root = node;
	}

	/**
	 * 删除node节点，要分别处理 node节点有 0,1,2 个子节点的情况
	 * @param node
	 */
	private void removeNode(Node node) {
		modCount++;
		size--;
		// 当被删除节点存在两个子节点时，那被删除节点和右子树中最小节点互换，或者和左子树中最大节点互换
		if(node.left != null && node.right != null) {
			Node minNode = minNode(node.right);
			node.data = minNode.data;
			node = minNode;
		}
		Node childNode = node.left != null ? node.left : node.right;
		// 当被删除节点存在一个子节点时
		if(childNode != null) {
			node.data = childNode.data;
			node = childNode;
		}
		if(node == node.parent.left) {
			node.parent.left = null;
		}else {
			node.parent.right = null;
		}
		checkAndAdjustBalance(node.parent);
	}

	/**
	 * 从 node 向下查找最小节点，即找到 以 node 为 根 的树中的最小节点
	 * @param node	查找的起始节点（子树的根）
	 * @return	node以及node子节点中的最小节点，
	 */
	private Node minNode(Node node) {
		while(node != null && node.left != null) {
			node = node.left;
		}
		return node;
	}

	private class Node<E> {

		Node parent;

		Node left;

		Node right;

		E data;

		Node(E data) {
			this.data = data;
		}

		Node(Node parent, E data) {
			this.parent = parent;
			this.data = data;
		}
	}

	private class Itr<E extends Comparable> implements Iterator<E>{

		// 迭代时最后访问的元素
		Node<E> lastAccess;
		// fail-fast
		int expectedModCount;
		// 迭代时用栈暂时存储数据
		Stack<Node> stack;

		public Itr() {
			expectedModCount = BalancedBinaryTree.this.modCount;
			stack = new Stack<>();
			Node node = BalancedBinaryTree.this.root;
			while(node != null) {
				stack.push(node);
				node = node.left;
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
				if(lastAccess.right != null) {
					node = lastAccess.right;
					stack.push(node);
					while(node.left != null) {
						node = node.left;
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
			BalancedBinaryTree.this.removeNode(lastAccess);
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
}
