package com.xsjiong.data;

import java.lang.reflect.Array;

public class Splay<T extends Comparable> {
	public class Node {
		Node father;
		Node[] son;
		T val;
		int cnt, size;

		Node(T data) {
			val = data;
			cnt = size = 1;
			son = (Node[]) Array.newInstance(Node.class, 2);
		}

		Node getSon(boolean side) {
			return side ? son[1] : son[0];
		}

		void setSon(boolean side, Node son) {
			if (side) this.son[1] = son;
			else this.son[0] = son;
			if (son != null) son.father = this;
		}

		boolean side() {
			if (father == null) return false;
			return father.son[1] == this;
		}

		void update() {
			this.size = cnt;
			if (son[0] != null) size += son[0].size;
			if (son[1] != null) size += son[1].size;
		}

		Node findSon(T v) {
			return v.compareTo(val) > 0 ? son[1] : son[0];
		}

		void rotate() {
			if (father == null) return;
			boolean side = side();
			Node fa = father;
			if (fa.father != null) fa.father.setSon(fa.side(), this);
			else this.father = null;
			fa.setSon(side, getSon(!side));
			setSon(!side, fa);
			this.update();
			fa.update();
		}
	}

	private Node root;

	public Splay() {
	}

	public Node getRoot() {
		return root;
	}

	public void splay(Node cur, Node target) {
		while (cur.father != target) {
			if (cur.father.father != target) (cur.father.side() == cur.side() ? cur.father : cur).rotate();
			cur.rotate();
		}
		if (target == null) root = cur;
	}

	public void makeRoot(Node x) {
		splay(x, null);
	}

	public Node find(T v) {
		Node cur = root;
		Node tmp;
		while (cur.val.compareTo(v) != 0 && (tmp = cur.findSon(v)) != null) cur = tmp;
		return cur;
	}

	Node getPrevious(T v) {
		makeRoot(find(v));
		if (root.val.compareTo(v) < 0) return root;
		Node cur = root.son[0];
		if (cur == null) return root;
		Node tmp;
		while ((tmp = cur.son[1]) != null) cur = tmp;
		return cur;
	}

	Node getSuccedding(T v) {
		makeRoot(find(v));
		if (root.val.compareTo(v) > 0) return root;
		Node cur = root.son[1];
		if (cur == null) return root;
		Node tmp;
		while ((tmp = cur.son[0]) != null) cur = tmp;
		return cur;
	}

	Node insert(T v) {
		if (root == null) return root = new Node(v);
		Node cur = find(v);
		if (cur.val.compareTo(v) == 0) {
			cur.cnt++;
			makeRoot(cur);
			return cur;
		}
		Node n = new Node(v);
		cur.setSon(v.compareTo(cur.val) > 0, n);
		makeRoot(n);
		return n;
	}

	void recycle(Node x) {
		if (x.cnt > 1) {
			x.cnt--;
			makeRoot(x);
		} else {
			if (x == root) root = null;
			else {
				x.father.setSon(x.side(), null);
				x.father.update();
			}
		}
	}

	void delete(T x) {
		Node pre = getPrevious(x);
		Node suc = getSuccedding(x);
		makeRoot(pre);
		splay(suc, pre);
		if (suc.son[0] != null) recycle(suc.son[0]);
	}

	int getRank(T x) {
		makeRoot(find(x));
		return root.son[0].size;
	}

	T getKth(int k) {
		if (root == null) return null;
		if (k > root.size) throw new ArrayIndexOutOfBoundsException();
		Node cur = root;
		Node tmp;
		int lsize;
		while (true) {
			tmp = cur.son[0];
			lsize = (tmp == null ? 0 : tmp.size);
			if (k > lsize + cur.cnt) {
				k -= lsize + cur.cnt;
				cur = cur.son[1];
			} else {
				if (lsize >= k)
					cur = cur.son[0];
				else
					return cur.val;
			}
		}
	}
}
