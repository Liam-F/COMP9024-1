/***********************************************************************
 * 
 * COMP9024 - Assignment 4
 * Hao Chen - 5102446
 * 
 ***********************************************************************/

import java.util.*;
import java.io.*;


public class CompactCompressedSuffixTrie 
{
	/*You need to define your data structures for the compressed trie  */
	public class CompactCompressedSuffixTrieNode
	{
		private String value;
		private List<CompactCompressedSuffixTrieNode> children_value = 
				new ArrayList<CompactCompressedSuffixTrieNode>();
		private int start_point;
		private boolean leaf;
		private int label;
		
		public CompactCompressedSuffixTrieNode(String value) {this.value = value;}
		
		public String getValue() {return value;}
		public void setValue(String value) {this.value = value;}
		
		public List<CompactCompressedSuffixTrieNode> getChildren() {return children_value;}
		public void setChildren(List<CompactCompressedSuffixTrieNode> children_value) {
			this.children_value = children_value;}
		
		public int getStartPoint() {return start_point;}
		public void setStartPoint(int start_point) {this.start_point = start_point;}
		
		public boolean isLeaf() {return leaf;}
		public void setLeaf(boolean leaf) {this.leaf = leaf;}
	
		public void setLable(int lable) {this.label = lable;}
		public int getLable() {return label;}
		
	}
	
	CompactCompressedSuffixTrieNode root = new CompactCompressedSuffixTrieNode("");
	
	
	/* Constructor
	 * 
	 * Create a compressed suffix trie from file f
	 * throw IOException 
	 * */
	public CompactCompressedSuffixTrie(String f) //create a compact compressed suffix trie from file f
	{
		//read from f, using the buffer reader
		String DNAsequence = "";
		File file = new File(f);
		int input;
		try
		{
			char SingChar;
			FileReader reader = new FileReader(file);
			BufferedReader buf = new BufferedReader(reader);

			while( (input = buf.read()) != -1)
			{
				char tempchar = (char) input;
				if(tempchar == 'A' || tempchar == 'C' || tempchar == 'G' || tempchar == 'T')
				{
					DNAsequence += tempchar;	
				}
			}
			buf.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println(file + " does not exist! ");
			System.exit(0);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
					
		//establish a compact representation of compressed suffix trie	
		insertFirstNode(root, DNAsequence, 0, 1);	
		for(int i=1 ;i<DNAsequence.length();i++)
		{
			processValue(root, DNAsequence.substring(i), i, 1);
		}	
	}

	// insert the first node into the suffix trie
	public void insertFirstNode(CompactCompressedSuffixTrieNode node, String value, int start_point, int label) 
	{
		CompactCompressedSuffixTrieNode newNode = new CompactCompressedSuffixTrieNode(value);
		newNode.setLeaf(true);
		newNode.setStartPoint(start_point);
		newNode.setLable(label);
		node.getChildren().add(newNode);
	}
	
	//process all the values rest and insert them into the suffix trie
	// process all the values and insert them into the suffix trie
	public void processValue(CompactCompressedSuffixTrieNode curNode, String value, int start_point, int label) 
	{
		for (int i = 0; i < curNode.getChildren().size(); i++) 
		{
			CompactCompressedSuffixTrieNode curChild = curNode.getChildren().get(i);
			int length = Math.min(curChild.getValue().length(), value.length());

			// check if there are any same chars that can be compressed
			int same_char = 0;
			
			for (same_char = 0; same_char < length; same_char++) 
			{
				if (curChild.getValue().charAt(same_char) != value.charAt(same_char))
					break;
			}

			// if no same char found, then check the next child or insert before
			// current child
			if (same_char == 0) 
			{
				if (value.charAt(0) < curChild.getValue().charAt(0)) 
				{
					insertAt(curNode, value, start_point, i, label);
					break;
				}
				continue;
			} 
			else if (same_char < length) 
			{ // if partially matched
				String newValue = value.substring(same_char);
				String newChildValue = curChild.getValue().substring(same_char);
				expandChild1(curChild, newChildValue, newValue, start_point,same_char,label);
			} 
			else 
			{ // same_char == length, totally matched
				// process the remaining value
				if (curChild.getValue().length() < value.length()) 
				{ 
					if (curChild.getStartPoint() > start_point)
						curChild.setStartPoint(start_point);
					String remainingValue = value.substring(same_char);
					processValue(curChild, remainingValue, start_point + same_char, label);
				} 
				else if (curChild.getValue().length() == value.length()) 
				{
					if (!curChild.isLeaf())
						curChild.setLeaf(true);
					if (curChild.getStartPoint() > start_point)
						curChild.setStartPoint(start_point);
				} 
				else 
				{ // curChild.getValue().length() > value.length()
					expandChild2(curChild, value, start_point, same_char,label);
				}
			}
		}
	}	
	
	// insert a node at given position of a compressed suffix tire
	public void insertAt(CompactCompressedSuffixTrieNode node, String value, int start_point, int position, int label) 
	{
		CompactCompressedSuffixTrieNode newNode = new CompactCompressedSuffixTrieNode(value);
		newNode.setLeaf(true);
		newNode.setStartPoint(start_point);
		newNode.setLable(label);
		node.getChildren().add(position, newNode);
	}
	
	// separate the current value into the new values
	// and make the same part as a parent node
	// then make the remaining part as a child node
	public void expandChild1(CompactCompressedSuffixTrieNode node, String childValue, String value, int start_point, int offset, int label) 
	{
		CompactCompressedSuffixTrieNode newChild = new CompactCompressedSuffixTrieNode(childValue);
		newChild.setLeaf(node.isLeaf());
		newChild.setChildren(node.getChildren());
		newChild.setStartPoint(node.getStartPoint() + offset);
		newChild.setLable(label);
		node.setValue(node.getValue().substring(0, offset));
		if (node.getStartPoint() > start_point)
			node.setStartPoint(start_point);
		node.setLeaf(false);

		CompactCompressedSuffixTrieNode newNode = new CompactCompressedSuffixTrieNode(value);
		newNode.setLeaf(true);
		newNode.setStartPoint(start_point + offset);
		newNode.setLable(label);
		node.setChildren(new ArrayList<CompactCompressedSuffixTrieNode>());
		if (value.charAt(0) < childValue.charAt(0)) 
		{
			node.getChildren().add(newNode);
			node.getChildren().add(newChild);
		} 
		else 
		{
			node.getChildren().add(newChild);
			node.getChildren().add(newNode);
		}
	}
	// make the value as the new parent node, and cut the remaining part of
	// the former child as the new child node
	public void expandChild2(CompactCompressedSuffixTrieNode node, String value, int start_point, int offset, int label) 
	{
		CompactCompressedSuffixTrieNode newChild = new CompactCompressedSuffixTrieNode(node
				.getValue().substring(offset));
		newChild.setLeaf(node.isLeaf());
		newChild.setChildren(node.getChildren());
		newChild.setStartPoint(node.getStartPoint() + offset);
		newChild.setLable(label);
		node.setLeaf(true);
		node.setValue(value);
		if (node.getStartPoint() > start_point)
			node.setStartPoint(start_point);
		node.setChildren(new ArrayList<CompactCompressedSuffixTrieNode>());
		node.getChildren().add(newChild);
	}

	
	
	/*Method for finding the first occurrence of a pattern s in DNA sequence
	 * 
	 * Time complexity: O(|S|)
	 * */
	public int findString (String s)
	{
		int position = findPattern(root,s);
		return position;
	}

	
	// recursively find the pattern,
	// there are at most 4 children (A,C,G,T) for each node,
	// and at most |S| depths need to be searched,
	// where |S| is the length of the pattern.
	// Thus the time complexity is 4|S|, which is O(|S|).	
	public int findPattern(CompactCompressedSuffixTrieNode curNode, String pattern) 
	{
		for (CompactCompressedSuffixTrieNode curChild : curNode.getChildren()) 
           {
			int length = Math.min(curChild.getValue().length(),
					pattern.length());
			int position = 0;

			// compare current child with the pattern for every single char
			for (position = 0; position < length; position++)
				if (pattern.charAt(position) != curChild.getValue().charAt(
						position))
					break;

			// according to the value of position, process the pattern
			if (position == 0) 
            { // not match current child, check the next child
				if (curChild.getValue().charAt(0) > pattern.charAt(0))
					break;
				continue;
			} 
            else if (position < length) // have no match
				break;
			else if (position == length) 
            { // match the current child, check if there exists remaining pattern
				if (pattern.length() <= curChild.getValue().length())
					return curChild.getStartPoint();
				else 
				{ // check the children of current child to find the remaining pattern
					String remainingPattern = pattern.substring(position);
					int subPosition = findPattern(curChild, remainingPattern);
					if (subPosition == -1)
						break;
					return subPosition - curChild.getValue().length();
				}
			}
		}
		return -1;
	}

	/* method for finding k longest common substrings of two DNA sequence stored in the text files f1 and f2
	 * Every time it find the longest common systring, construct the table like below:
	 * 
	 *    a b c d e
	 * c  0 0 1 0 0  
	 * d  0 0 0 2 0
	 * e  0 0 0 0 3
	 * f  0 0 0 0 0 
	 * 
	 * then the longest common substring should be the highest number.
	 * record the highest number and it's position, and the remove it from the two strings and add the common substring to the
	 * return_List.
	 * The construction of this table is m*n
	 * and construct k times.
	 * 
	 * the time complexity is O(kmn).
	*/
	
	public static void kLongestSubstrings(String f1, String f2, String f3, int k)
	{
		//get the two sequences from the given file
		String DNAsequence_1 = CreateSequence(f1);
		String DNAsequence_2 = CreateSequence(f2);
		
//		System.out.println("DNAsequence_1: " + DNAsequence_1);
//		System.out.println("DNAsequence_2: " + DNAsequence_2);
//		System.out.println("----------------------------------------------------------------------");
		
		//create the ArrayList to store the final result.
		ArrayList<String> return_List = new ArrayList<String>();
		
		//use the findCommenSubstring function recursively to find the final result.
		findCommenSubstring(DNAsequence_1,DNAsequence_2, return_List, k);
		
		// create the final file needed
		CreateFile(f3, return_List);
	}  
	
	//the function to find the common substring.
	 private static void findCommenSubstring(String seq_1, String seq_2, ArrayList<String> re_L, int RecurTime) 
	 {
			char[] a = seq_1.toCharArray();  
			char[] b = seq_2.toCharArray();  
			int a_length = a.length;  
			int b_length = b.length;  
			//create the int array first.
			int[][] lcs = new int[a_length + 1][b_length + 1];  
			
			int current_longest = 0;
			int[] current_position = new int[2];	
			
			//Initialize the int[][] with filling 0.
			for (int i = 0; i <= b_length; i++) 
			{  
				for (int j = 0; j <= a_length; j++) 
				{  
					lcs[j][i] = 0;  
				}  
			}//The time complicity O(m*n)
			
			//Construct the table of 1&0
			for (int i = 1; i <= a_length; i++) 
			{
				for (int j = 1; j <= b_length; j++) 
				{
					if (a[i - 1] == b[j - 1]) 
					{// if the number on the left and the number on the top equal, then this grid should be the diagonal + 1.
						lcs[i][j] = lcs[i - 1][j - 1] + 1; 
						if(lcs[i][j] > current_longest)//record the current longest number and it's position 
						{
							current_longest =lcs[i][j]; 
							current_position[0] = i; // the index of the common substring at sequence 1.
							current_position[1] = j; // the index of the common substring at sequence 2.
						}
		            }  
		            if (a[i - 1] != b[j - 1]) // if the number on the left and the number on the top do not equal, then this grid should be 0.
		            {
		            	lcs[i][j] = 0;
		            }  
		        }  
		    }  //O(m*n) since the length of two sequence is m and n
	        

	        int x = current_position[0];
	        int y = current_position[1];
       
	        
	        String temp = seq_1.substring(x-current_longest, x);
	        re_L.add(temp);
	        
	        //remove the common substring from the two sequences.
	        seq_1 = seq_1.substring(0,x-current_longest) + "$" + seq_1.substring(x);
	        seq_2 = seq_2.substring(0,y-current_longest) + "#" + seq_2.substring(y);
	        
//	        System.out.println("seq_1: " + seq_1);
//	        System.out.println("seq_2: " + seq_2);
//	        System.out.println("-----------------------------");
	        
	        
	        //if the recurtime is 1, means the process is done, then return.
	    	if(RecurTime==1 || current_longest==0)
	    	{return;}
	    	//each time the function run,  RecurTime--.
	    	RecurTime--;
	    	
	    	findCommenSubstring(seq_1,seq_2, re_L, RecurTime); //O(k*m*n) since the recursive time is the k.
	 }

	 //the function to extract the sequence from the given file
	 private static String CreateSequence(String f1) 
	 {
		 String DNAsequence = "";
		 File file_1 = new File(f1);
		 int input;
			try
			{
				FileReader reader = new FileReader(file_1);
				BufferedReader buf = new BufferedReader(reader);
				while( (input = buf.read()) != -1)
				{
					char tempchar = (char) input;
					if(tempchar == 'A' || tempchar == 'C' || tempchar == 'G' || tempchar == 'T')
					{
						DNAsequence += tempchar;	
					}
				}

				buf.close();
			}
			catch(FileNotFoundException e)
			{
				System.out.println(file_1 + " does not exist! ");
				System.exit(0);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		
		return DNAsequence;
	}

	 //the function to create the required file with the list stored the final result.
	private static void CreateFile(String f3, ArrayList<String> List) 
	 {
		 File file3 = new File(f3);
		 try
		 {
			 int length = List.size();
			 FileWriter fos = new FileWriter(file3, false);
			 for(int i=1; i<=length; i++)
			 {
				 fos.write(i + ": ");	 
				 fos.write(List.get(i-1));
				 fos.write("\r");
			 }
			 fos.close();
		 }
		 catch(FileNotFoundException e)
		 {
			 e.printStackTrace();
		 }
		 catch(IOException e)
		 {
			 e.printStackTrace();
		 }
	 }


	 public static void main(String args[]) throws Exception{
	        
		 /** Construct a compact compressed suffix trie named trie1
		  */       
		 CompactCompressedSuffixTrie trie1 = new CompactCompressedSuffixTrie("file1.txt");
		         
		 System.out.println("ACTTCGTAAG is at: " + trie1.findString("ACTTCGTAAG"));

		 System.out.println("AAAACAACTTCG is at: " + trie1.findString("AAAACAACTTCG"));
		         
		 System.out.println("ACTTCGTAAGGTT : " + trie1.findString("ACTTCGTAAGGTT"));
		         
		 CompactCompressedSuffixTrie.kLongestSubstrings("file2.txt", "file3.txt", "file4.txt", 6);
		  }

}

