package learning.demo.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @ClassName BalancedBinaryTree
 * @Description java实现平衡二叉树
 * @Author Numblgw
 * @Date 2019/4/16 16:52
 */
public class BalancedBinaryTree<E extends Comparable> implements BinaryTree<E> {

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

	/**
	 * 计算该子树的深度
	 * @param root	树根
	 * @return	深度（int）
	 */
	private int depth(Node root) {
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
				if(node.left != null) {
					queue.add(node.right);
				}
				if(node.right != null) {
					queue.add(node.right);
				}
			}
		}
		return depth;
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
				node = node.left;
			}else if(result > 0) {
				node = node.right;
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
		// 计算插入位置并插入节点
		Node node = addNode(e);
		// 检查并维护平衡
		checkAndAdjustBalance(node);
		return true;
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
	 * 从node节点开始向上查找失去平衡的节点，得到最小不平衡子树的树根，如果平衡则返回 null
	 * @param node	开始查找的节点
	 * @return	该树平衡 return null 否则 return 最小不平衡子树的树根节点
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
				rightRotate(node);
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
		n.right.parent = n.parent;
		if(n.parent != null) {
			if(n.parent.left == n) {
				n.parent.left = n.right;
			}else {
				n.parent.right = n.right;
			}
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

	@Override
	public E remove(Object o) {
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
		return null;
	}

	private class Node<E extends Comparable<E>> {

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
}
