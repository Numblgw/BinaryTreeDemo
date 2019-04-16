package learning.demo.util;

import java.util.Iterator;

/**
 * @ClassName BinaryTree
 * @Description 二叉树抽象接口，
 * @Author Numblgw
 * @Date 2019/4/14 16:13
 */
public interface BinaryTree<E> extends Iterable<E>{

	/**
	 * 计算二叉树的深度，根节点深度为 1，每向下一层深度 +1，当且仅当树为 null 时返回 0。
	 * @return	该二叉树的深度
	 */
	int depth();

	/**
	 * 计算二叉树中的节点个数
	 * @return	该二叉树中节点个数
	 */
	int size();

	/**
	 * 如果该树为 null 返回 true
	 * @return 该树是否为 null
	 */
	boolean isEmpty();

	/**
	 * 如果该树内包含元素 e 则返回 true 否则返回 false ，对于 null 值的处理应取决于具体实现。
	 * @param o	被查找的元素
	 * @return	是否包含元素 e
	 */
	boolean contains(Object o);

	/**
	 * 向该树中插入一个元素，插入成功返回 true 失败返回 false ,是否可以加入 null 值取决于具体实现
	 * @param e	元素
	 * @return	是否插入成功
	 */
	boolean add(E e);

	/**
	 * 删除元素 e ，支持快速失败 (fail-fast) 机制
	 * @param o	被删除的元素
	 * @return 返回被删除元素，或者null
	 */
	E remove(Object o);

	/**
	 * 将树中的数据清空， 执行完clear操作之后该树将变为空树。
	 */
	void clear();

	/**
	 * 返回一个二叉树迭代器，需要实现类提供迭代器的具体实现。
	 * @return	迭代器实例
	 */
	Iterator<E> iterator();
}
