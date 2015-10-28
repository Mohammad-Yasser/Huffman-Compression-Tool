package Huffman;

import java.util.Comparator;

public class Pair {
	BinaryTree.Node node;
	int freq;

	Pair(BinaryTree.Node n, int frequency) {
		node = n;
		freq = frequency;
	}

	public static class cmp implements Comparator<Pair> {

		@Override
		public int compare(Pair o1, Pair o2) {
			return o1.freq - o2.freq;
		}

	}

}
