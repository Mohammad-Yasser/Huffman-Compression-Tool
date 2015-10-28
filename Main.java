package Huffman;

import java.io.*;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) throws IOException {

		int choice = Integer.parseInt(JOptionPane.showInputDialog("Select your choice:\n1-Compress\n2-Decompress"));


		if (choice == 1) {
			Huffman.compress("input.txt");
		} else {
			Huffman.decompress("compressed.huff");
		}

	}

}
