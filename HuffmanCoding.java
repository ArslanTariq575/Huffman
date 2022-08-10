package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.transform.Source;


//  javac -d bin src/huffman/*.java
// java -cp bin huffman.Driver
/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                System.exit(1);
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }
    
    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /**
     * Reads a given text file character by character, and returns an arraylist
     * of CharFreq objects with frequency > 0, sorted by frequency
     * 
     * @param filename The text file to read from
     * @return Arraylist of CharFreq objects, sorted by frequency
     */
    public static ArrayList<CharFreq> makeSortedList(String filename) {
         StdIn.setFile(filename);
        //StdOut.println(filename.length());
        ArrayList<CharFreq> list = new ArrayList<CharFreq>();
        String p = new String ();
        while (StdIn.hasNextChar()) {
            char c = StdIn.readChar();
            p = p + String.valueOf(c);
           
        }
        //StdOut.println(p);
        // Create Array to put ACSII value In an Int array
            
        int [] arr = new int[128];
        int index = 0;
        int count = 0;
        for( int i = 0; i<p.length();i++){
            index = ((int)p.charAt(i));
            
            arr[index] = arr[index] + 1;
            index++;
            
        }
        // Creat A double Array to find Probability of Each Occurrences
         int num = 0;
         double [] d =  new double [128];
        
           while(num<arr.length){
              if(arr[num]> 0 ){
                 d[num] = Double.valueOf(arr[num])/p.length();
                 char  val = (char)num;
                 CharFreq piz = new CharFreq();
                 piz.setCharacter(val);
                 piz.setProbOccurrence(d[num]);
                 list.add(piz);
                if(piz.getProbOccurrence()==1){
                    CharFreq new1 = new CharFreq('b',0);
                    list.add(new1);
                }
                }
                     num++;
             }
             
             
    Collections.sort(list);
    
        return list; 
    }

    /**
     * Uses a given sorted arraylist of CharFreq objects to build a huffman coding tree
     * 
     * @param sortedList The arraylist of CharFreq objects to build the tree from
     * @return A TreeNode representing the root of the huffman coding tree
     */
    public static TreeNode makeTree(ArrayList<CharFreq> sortedList) {
        // Making Two new Queue 
        // One For Source and other for Target
        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();
    
        // For Loop is filling the Value of CharFreq into Source Quence
       for (CharFreq x : sortedList){
            //new TreeNode
            TreeNode new_Node = new TreeNode();
            //setData to x
            new_Node.setData(x);
            //source.enqueue
            source.enqueue(new_Node); 
        }
         TreeNode left = source.dequeue();
         TreeNode right = source.dequeue();
         CharFreq p = new CharFreq(null,left.getData().getProbOccurrence()+right.getData().getProbOccurrence());
         TreeNode g = new TreeNode(p,left,right);
         target.enqueue(g);
         
        while(!source.isEmpty() || target.size() > 1){
            
            TreeNode one = compareNode(source, target);
            //StdOut.println(one.getData().getProbOccurrence());
            TreeNode two = compareNode(source, target);
           //StdOut.println(two.getData().getProbOccurrence());
            CharFreq new1 = new CharFreq(null,one.getData().getProbOccurrence()+
            two.getData().getProbOccurrence());
            TreeNode three = new TreeNode(new1, one, two);
            target.enqueue(three);
        }   
        return target.peek(); // Delete this line
    }

    public static TreeNode compareNode(Queue<TreeNode> one, Queue<TreeNode> two){
        if(one.isEmpty()){
            TreeNode gp = two.dequeue();
            return gp;
        } 
        if(two.isEmpty()){
            TreeNode tt = one.dequeue();
            return tt;
        }
        if(one.peek().getData().getProbOccurrence()==two.peek().getData().getProbOccurrence()){
            TreeNode gpa = one.dequeue();
            return gpa;
        }
        if( one.peek().getData().getProbOccurrence()< two.peek().getData().getProbOccurrence()){
            TreeNode pp = one.dequeue();
            return pp;
            
        }else {
            TreeNode gg = two.dequeue();
            return gg;
        }
    }

    /**
     * Uses a given huffman coding tree to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null
     * 
     * @param root The root of the given huffman coding tree
     * @return Array of strings containing only 1's and 0's representing character encodings
     */
    public static String[] makeEncodings(TreeNode root) {
        
        String [] output = new String [128];
        String s = "";
        makeEncodingsmaker(output, root, s);
        
        return output; 
    }
    public static void makeEncodingsmaker(String[] output,TreeNode root, String s){
    
        if (root.getLeft() ==null && root.getRight() ==null){ 
            char indexC = root.getData().getCharacter();
            int index = (int) indexC;
            output[index] = s; 
            return;
        }
        makeEncodingsmaker(output,root.getLeft(),s+ "0");
        makeEncodingsmaker(output,root.getRight(),s+ "1");
    } 
    /**
     * Using a given string array of encodings, a given text file, and a file name to encode into,
     * this method makes use of the writeBitString method to write the final encoding of 1's and
     * 0's to the encoded file.
     * 
     * @param encodings The array containing binary string encodings for each ASCII character
     * @param textFile The text file which is to be encoded
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public static void encodeFromArray(String[] encodings, String textFile, String encodedFile) {
        StdIn.setFile(textFile);
        String p = new String();
        while (StdIn.hasNextChar()){
            char tt = StdIn.readChar();
            int index = (int) tt;
            
            p = p+ encodings[index];
        }
        //StdOut.println(p);
        //StdOut.println(p.length());
        writeBitString(encodedFile,p);
    }
  
    /**
     * Using a given encoded file name and a huffman coding tree, this method makes use of the 
     * readBitString method to convert the file into a bit string, then decodes the bit string
     * using the tree, and writes it to a file.
     * 
     * @param encodedFile The file which contains the encoded text we want to decode
     * @param root The root of your Huffman Coding tree
     * @param decodedFile The file which you want to decode into
     */
    public static void decode(String encodedFile, TreeNode root, String decodedFile) {
        StdOut.setFile(decodedFile);
        String read = readBitString(encodedFile);
        if (root == null) {
         return;
     }
     char[] arr = read.toCharArray();
     int index = 0;
     String new1 = new String();
     while (index < arr.length) {
         TreeNode node = root;
         while (node != null) {
             if (node.getLeft() == null && node.getRight() == null) {
                 new1 = new1 + node.getData().getCharacter();
                 break;//break inner while
             } else {
                 char c = arr[index];
                 if (c == '0') {
                     node = node.getLeft();
                 } else {
                     node = node.getRight();
                 }
                 index++;
             }
         }
     }
     StdOut.print(new1);
     
     
        
    }
      
}
