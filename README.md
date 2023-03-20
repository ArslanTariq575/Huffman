# Huffman
The Huffman project is a data compression program developed using Java. The program takes an input file and compresses it into an encoded file using a lossless compression technique called Huffman coding. This technique is used to reduce the data size without any loss of information.

The program works by first analyzing the input file to determine the frequency of occurrence of each character. It then uses this information to create a Huffman tree, which is a binary tree where each leaf node represents a character in the input file and each internal node represents the merging of two leaf nodes. The frequency of occurrence of each character determines its position in the Huffman tree. Characters with higher frequency will be closer to the root of the tree and characters with lower frequency will be closer to the leaves.

Once the Huffman tree is constructed, the program generates a code for each character based on its position in the tree. Characters closer to the root of the tree will have shorter codes, while characters closer to the leaves will have longer codes. This is because characters with higher frequency are represented by fewer bits, while characters with lower frequency are represented by more bits.

The program then encodes the input file using the generated codes and saves it in an encoded file. The encoded file will have a reduced size of approximately 50% compared to the original file size, depending on the frequency distribution of the characters in the input file.

To decode the encoded file, the program reads the encoded file and uses the Huffman tree to convert the codes back to their original characters. The decoded file will have the same content as the original file, without any loss of information.

To create the Huffman tree, the program utilizes data structures like queues and trees. A priority queue is used to store the frequency of occurrence of each character, and a binary tree is used to represent the Huffman tree. The program utilizes a bottom-up approach to construct the Huffman tree by repeatedly merging the two nodes with the lowest frequency into a new node until all nodes are merged into a single root node.

Overall, the Huffman project is a useful program for compressing data without losing any information, and it demonstrates the effective use of data structures and algorithms like queues and trees to achieve this goal.
