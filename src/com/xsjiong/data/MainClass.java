package com.xsjiong.data;

public class MainClass {
	public static void main(String[] args) {
		IntSplay s = new IntSplay();
		s.insert(23);
		s.insert(55);
		s.insert(24);
		System.out.println(s.getKth(2));

	}
}
