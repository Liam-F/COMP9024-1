//Hao Chen  z5102446
//COMP9024 Ass_1
//2017.3

import java.io.*;
import java.util.*;

public class MyDlist extends DList
{
	/**Q1. The constructor creates an empty doubly linked list**/
	public MyDlist()
	{	
		//This is the default constructor. It calls parent default constructor which creates an empty list and initialize the nodes required.
		super();
	}
	
	/** Q2. Constructor creating DList based on input type */
	public MyDlist(String f)
	{
		if(f.equals("stdin"))
		{
			//if f is stdin then call the readFromStdin method.
			readFromStdin();
		}
		else
		{
			//else call the readFromFile function.
			readFromFile(f);
		}
	}
	
	// This is the private readFromStdin method.
	private void readFromStdin()
	{
		//Define the variables 
		String input = null;
		Scanner sc = new Scanner(System.in);
		//judge whether the line is empty, if yes, then break.
		while( (input=sc.nextLine()).isEmpty() == false )
		{
			//Create a new code with the user input and 
			// and put it at the tail of the new created firstList. 
			DNode Node_new = new DNode(input, null,null);
			this.addLast(Node_new);
		}
		//Close the scanner
		sc.close();
	}
	
	//This is the private readFromFile method.
	private void readFromFile(String file)
	{
		//Define the variables and a BufferedRead
		String[] List_f;
		String input = null;
		BufferedReader inputFile = null;
		//try to get the text file.
		try
		{
			inputFile = new BufferedReader(new FileReader(file));
		}
		//Raise the FileNotFoundException if the file not found.
		catch(FileNotFoundException e)
		{
			
			System.out.println("File cannot be found.");
			return;
		}
		//Try to make the doubly linked list.
		try
		{
			//try to read each line to the last line
			while( (input = inputFile.readLine())!=null )
			{
				//split the contacts into words and put them in an array List_f.
				List_f = input.split(" ");
				for(int i = 0; i<List_f.length; i++)
				{
					//for each word create a new node and put it at the end of the created secondList.
					DNode Node_new = new DNode(List_f[i],null,null);
					this.addLast(Node_new);
				}
			}
		}
		//Raise the IOException if the file cannot be read.
		catch(IOException e)
		{
			System.out.println("Cannot read the file");
			return;
		}
		//Try to close the file
		try
		{
			inputFile.close();
		}
		//Raise the IOException if the file cannot be closed.
		catch(IOException e)
		{
			System.out.println("Cannot close the file");
			return;
		}
	}
	
	/**Q3. Create the printList method which can print all elements of the list.**/
	public static void printList(MyDlist u)
	{
		if(u.isEmpty())
		{
			System.out.println("This list is empty!");
		}
		//Get the head of the list and go through all the nodes in the list and print the element of that node.
		if(u.size > 0)
		{
			DNode node = u.header.next;
			//Break when it go to the end.
			while(node != u.trailer)
			{
				System.out.println(node.element);
				node = node.next;
			}
		}
	}
	
	/**Q4. Create an identical copy of a doubly linked list u and returns the resulting doubly linked list.**/
	//method cloneList time complexity --> O(n)
	public static MyDlist cloneList(MyDlist u)
	{
		MyDlist clonedList = new MyDlist();
		DNode currentNode = u.header.next;
		//go from the first to the last node
		while(true)
		{
			//break when it goes to the last node
			if(currentNode == u.trailer)
			{
				break;
			}
			else
			{
				//add the node to the last of the clonedList
				DNode newNode = new DNode(null,null,null);
				newNode.element = currentNode.element;
				clonedList.addLast(newNode);
				currentNode = currentNode.next;
			}
		}
		return clonedList;
	}
	
	
	/**Q5. The class method computes the union of the two sets u and v, and returns a doubly linked list that stores the union**/
	//there is a cloneList(u), the time complexity is O(n) 
	//two while loop, the time complexity is O(N^2) 
	//method time complexities --> O(N^2)	
	public static MyDlist union(MyDlist	u,MyDlist v)
	{
		//if there is an empty list, then return the other one as the cloned list
		if (u.size == 0 && v.size == 0)
		{
			MyDlist union = new MyDlist();
			return union;
		}
		else if(u.size == 0 && v.size != 0)
		{
			MyDlist union = cloneList(v);
			return union;
		}
		else if (u.size != 0 && v.size == 0)
		{
			MyDlist union = cloneList(u);
			return union;
		}
		else
		{
			//clone the first list 
			MyDlist union = cloneList(u);
			DNode nodeOfv = v.getFirst();
			String elementOfv = nodeOfv.element;
			//for every node in the list, we check every node in the other list
			while(elementOfv!=null)
			{
				int flag = 1;
				DNode nodeOfunion = union.getFirst();
				String elementOfunion = nodeOfunion.element;
				while(elementOfunion != null)
				{
					//if no node match, then we add it; if one match we skip it
					if(elementOfunion.equals(elementOfv))
					{
						flag = 2;
						break;//which means if one element found equally, we switch to the next
					}
					nodeOfunion = nodeOfunion.getNext();
					elementOfunion = nodeOfunion.element;
				}
				if(flag == 1)//which means that this one is not found in the union, so add it
				{
					DNode node_different = new DNode(elementOfv,null,null);
					union.addLast(node_different);
				}
				nodeOfv = nodeOfv.getNext();
				elementOfv = nodeOfv.element;
			}
			return union;
		}
	}

	
	/**Q6. The class method computes the intersection of the two sets u and v, and returns a doubly linked list that stores the intersection**/
	//there are two while loop, the time complexity is O(N^2)
	//method time complexities --> O(N^2)
	public static MyDlist intersection(MyDlist u,MyDlist v)
	{
		//if both lists are empty, then return empty
		if(u.size == 0 || v.size == 0)
		{
			MyDlist intersection = new MyDlist();
			return intersection;
		}
		else
		{
			//for every node in the list, we check every node in the other list
			MyDlist intersection = new MyDlist();
			
			DNode nodeOfv = v.getFirst();
			String elementOfv = nodeOfv.element;
			
			while(elementOfv != null)
			{
				//go through every node in the first list
				int flag = 1;
				DNode nodeOfu = u.getFirst();
				String elementOfu = nodeOfu.element;
				while(elementOfu != null)
				{
					//if one node is found match, then break and add it,
					if(elementOfu.equals(elementOfv))
					{
						flag = 2;
						break;
					}
					//if no node match, then switch to the next node
					nodeOfu = nodeOfu.getNext();
					elementOfu = nodeOfu.element;
				}
				if(flag == 2)
				{
					DNode node_to_added = new DNode(elementOfv,null,null);
					intersection.addLast(node_to_added);					
				}
				nodeOfv = nodeOfv.getNext();
				elementOfv = nodeOfv.element;
			}
			return intersection;
		}
	}
	
	
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("please type some strings, one string each line and an empty line for the end of input:");
		/** Create the first doubly linked list
		    by reading all the strings from the standard input. */
		MyDlist firstList = new MyDlist("stdin");
		    
		/** Print all elememts in firstList */
	    	System.out.println("------------The first List-----------");
		    printList(firstList);
		   
		/** Create the second doubly linked list                         
		by reading all the strings from the file myfile that contains some strings. */
		  
		/** Replace the argument by the full path name of the text file */  
		    MyDlist secondList=new MyDlist("/Users/harvey/Desktop/COMP9024/Assignment_1/myfile.txt");

		/** Print all elements in secondList */ 
	    	System.out.println("------------The second List-----------");
		    printList(secondList);

		/** Clone firstList */
		MyDlist thirdList = cloneList(firstList);

	    /** Print all elements in thirdList. */
    	System.out.println("------------The third List-----------");
	    printList(thirdList);

		/** Clone secondList */
		MyDlist fourthList = cloneList(secondList);

		/** Print all elements in fourthList. */
    	System.out.println("------------The fourth List-----------");
		printList(fourthList);
		    
		/** Compute the union of firstList and secondList */
		MyDlist fifthList = union(firstList, secondList);

		/** Print all elements in thirdList. */ 
    	System.out.println("------------The fifth List-----------");
		printList(fifthList); 

		/** Compute the intersection of thirdList and fourthList */
		MyDlist sixthList = intersection(thirdList, fourthList);

		/** Print all elements in fourthList. */
    	System.out.println("------------The sixth List-----------");
		printList(sixthList);
		}

}
