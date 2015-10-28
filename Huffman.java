package Huffman;

import java.io.*;
import java.util.*;
import Huffman.BinaryTree;
import Huffman.BinaryTree.Node;

public class Huffman {

	static public void compress(String filePath) throws IOException {

		calcFreq(filePath);

		buildTree();

		calcCode();

		outS = new DataOutputStream(new FileOutputStream(filePath + ".huff"));

		for (int i = 0; i < nSymbols; ++i)
			outS.writeInt(freq[i]);

		FileInputStream input = new FileInputStream(filePath);

		int tmp = 0;
		String res = new String();

		while ((tmp = input.read()) != -1) {
			res += code[tmp];
			if (res.length() > maxAvailableBytes / 2) {
				writeBits(res.substring(0, res.length() / 32 * 32 - 1));
				res = res.substring(res.length() / 32 * 32);
			}
		}

		if (res.length() > 0)
			writeBits(res);

		input.close();
		outS.close();
	}

	static public void decompress(String filePath) throws IOException {
		inS = new DataInputStream(new FileInputStream(filePath + ".huff"));

		PrintWriter out = new PrintWriter(filePath + ".unhuff");

		int nChar = 0;

		for (int i = 0; i < nSymbols; ++i) {
			freq[i] = inS.readInt();
			nChar += freq[i];
		}

		char c;

		buildTree();
		calcCode();

		int currInt;
		Node currNode = tree.head;

		while (nChar > 0) {
			currInt = inS.readInt();

			for (short bit = 0; bit < 32; ++bit) {
				if (((currInt >> bit) & 1) == 1) {
					currNode = currNode.right;
				} else {
					currNode = currNode.left;
				}

				if (currNode.value != null) {
					out.print(currNode.value);
					currNode = tree.head;
					if (--nChar == 0)
						break;
				}
			}
		}

		inS.close();
		out.close();
	}

	static private int maxAvailableBytes = (int) 1e8;

	static private int nSymbols = 128;
	static private int[] freq = new int[nSymbols];

	static private BinaryTree tree = new BinaryTree();
	static private String[] code = new String[nSymbols];
	static private BinaryTree.Node[] leaves = new BinaryTree.Node[nSymbols];

	static private DataOutputStream outS;
	static private DataInputStream inS;

	static private void calcFreq(String filePath) throws IOException {

		FileInputStream input = new FileInputStream(filePath);

		int tmp;
		while ((tmp = input.read()) != -1) {
			++freq[tmp];
		}
		input.close();
	}

	static private void buildTree() {
		PriorityQueue<Pair> q = new PriorityQueue<Pair>(100, new Pair.cmp());

		for (int i = 0; i < nSymbols; ++i)
			if (freq[i] > 0)
				q.add(new Pair(leaves[i] = new Node((char) i), freq[i]));

		q.add(new Pair(new Node(), 0));

		while (q.size() > 1) {

			Node parent = new Node();

			Pair tmp1 = q.poll(), tmp2 = q.poll();

			parent.left = tmp1.node;
			parent.right = tmp2.node;
			tmp1.node.parent = tmp2.node.parent = parent;

			q.add(new Pair(parent, tmp1.freq + tmp2.freq));
		}

		tree.head = q.peek().node;
	}

	static private void calcCode() {
		for (int i = 0; i < nSymbols; ++i)
			if (freq[i] > 0) {

				String s = new String();
				BinaryTree.Node curr = leaves[i];

				while (curr.parent != null) {
					s += (char) ('0' + Boolean.compare(curr == curr.parent.right, false));

					curr = curr.parent;
				}

				s = new StringBuffer(s).reverse().toString();

				code[i] = s;

			}
	}

	static private void writeBits(String s) throws IOException {
		int[] bits32 = new int[(31 + s.length()) / 32];

		for (int i = 0; i < s.length(); ++i)
			bits32[i / 32] |= (Boolean.compare((s.charAt(i) == '1'), false) << (i % 32));

		for (int i = 0; i < bits32.length; ++i)
			outS.writeInt(bits32[i]);

		return;
	}

}
