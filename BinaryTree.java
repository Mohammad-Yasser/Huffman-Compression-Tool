package Huffman;


public class BinaryTree {

	protected Node head;
	
	public static class Node {
		Node left = null, right = null, parent = null;
		Character value;

		Node(char ch) {
			value = ch;
		}

		Node() {
			value = null;
		}
	}
	
	
}
