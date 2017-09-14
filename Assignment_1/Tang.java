
//import the package the read the file 
import java.io.*;
import java.lang.*;
import java.util.*;

public class MyDlist extends DList {
	//question1
	// no variables function
	
	public MyDlist() {
		super();
	}

	//question2
	// one variable function
	//input from file address or input
	public MyDlist(String f) throws FileNotFoundException{
        //situation 1
		if (f == "stdin") {
			Scanner sc = new Scanner(System.in);
			String kb = sc.nextLine();
			while (!kb.equals("")) {
				kb=kb.trim();
				DNode node = new DNode(kb, null, null);
				this.addLast(node);
				kb = sc.nextLine();
			}
		}
		//situation 2
		else {
			FileInputStream fis = new FileInputStream(f);
			Scanner scanner = new Scanner(fis);
			while (scanner.hasNext()){
				DNode node = new DNode(null, null, null);
				node.element = scanner.next();
				this.addLast(node);
			}
			

			scanner.close();

			}
		   
		}

	
//question3
	//print a list
	public static void printList(MyDlist u) {

		if ((u.size >0)) {
			DNode node = u.getFirst();
			System.out.println(node.element);
			while (node.getNext().element != null) {
				node = node.getNext();
				System.out.println(node.element);
			}
		}
	}

	
	
//question4	
	//method clonelist tume complexity is O(n)
	public static MyDlist cloneList(MyDlist u) {
		MyDlist clonelist = new MyDlist();
		if ((u.size > 0)) {
			DNode node=u.getFirst();
			String element=node.element;
			DNode node1= new DNode(element, null, null);
			clonelist.addLast(node1);
			
			

			while (node.getNext().element != null) {
				node = node.getNext();
			    element=node.element;
			    
			    node1=new DNode(element, null, null);
				clonelist.addLast(node1); 
			}
		}
		return clonelist;
}
	
	
	
	//question5
	//Time complexity of calculation 
	//Before the while-loop, there is cloneList()(MyDlist union = cloneList(u);) whose time complexity is O(n)
	//In the union function, there is two while ,therefore ,the complexity is O(n^2)
	//In conclusion, union's time complexity is O(n^2)
	public static MyDlist union(MyDlist u,MyDlist v){
		if (u.size == 0 & v.size==0){
			MyDlist union = new MyDlist();
			return union;
			
		}
		else if(u.size==0 & v.size>0){
			MyDlist union = cloneList(v);
			return union;
			
		}
		else if(u.size>0 & v.size==0){
			MyDlist union = cloneList(u);
			return union;			
		}
		else {
		MyDlist union = cloneList(u);
		DNode nodeOfv = v.getFirst();
		String elementOfv=nodeOfv.element;
		
		while(elementOfv!=null){
			int sign = 1;
			DNode nodeOfunion = union.getFirst();
			String elementOfunion = nodeOfunion.element;
			while(elementOfunion!=null){
			
				if(elementOfunion.equals(elementOfv)){
					sign=2;
					break;
				}
				nodeOfunion = nodeOfunion.getNext();
				elementOfunion=nodeOfunion.element;
				
				}
			if(sign==1){
				    DNode nodeAdd=new DNode(elementOfv, null, null);
					union.addLast(nodeAdd);
					
				}
			nodeOfv=nodeOfv.getNext();
			elementOfv=nodeOfv.element;
        
			
		}
		return union;
		}
		
		
	}
		
	
	//question6
	//this is the same as question5
	//Time complexity of calculation 
	//Before the while-loop, there is cloneList()(MyDlist union = cloneList(u);) whose time complexity is O(n)
	//In the union function, there is two while ,therefore ,the complexity is O(n^2)
	//In conclusion, union's time complexity is O(n^2)	
	public static MyDlist intersection(MyDlist u,MyDlist v){

		if(u.size>0 & v.size>0){
        MyDlist intersection= new MyDlist();
		
		DNode nodeOfv = v.getFirst();
		String elementOfv=nodeOfv.element;
		
		while(elementOfv!=null){
			int sign = 1;
			DNode nodeOfunion = union.getFirst();
			String elementOfunion = nodeOfunion.element;
			while(elementOfunion!=null){
			
				if(elementOfunion.equals(elementOfv)){
					sign=2;
					break;
				}
				nodeOfunion = nodeOfunion.getNext();
				elementOfunion=nodeOfunion.element;
				
				}
			if(sign==2){
				    DNode nodeAdd=new DNode(elementOfv, null, null);
				    intersection.addLast(nodeAdd);
					
				}
			nodeOfv=nodeOfv.getNext();
			elementOfv=nodeOfv.element;
        
			
		}
		return intersection;}
		else{
			MyDlist intersection =new MyDlist();
			return intersection;
		}
	}		
	
	

	public static void main(String[] args) throws Exception {
		   System.out.println("please type some strings, one string each line and an empty line for the end of input:");
		    /** Create the first doubly linked list
		    by reading all the strings from the standard input. */
		    MyDlist firstList = new MyDlist("stdin");
		    
		   /** Print all elememts in firstList */
		    
		    printList(firstList);
	   
		   /** Create the second doubly linked list                         
		    by reading all the strings from the file myfile that contains some strings. */
		  
	   /** Replace the argument by the full path name of the text file */  
		    MyDlist secondList=new MyDlist("C:/Users/Hui Wu/Documents/NetBeansProjects/MyDlist/myfile.txt");

		   /** Print all elememts in secondList */ 
		    
		    printList(secondList);

		   /** Clone firstList */
		    MyDlist thirdList = cloneList(firstList);
		    
		   /** Print all elements in thirdList. */
		    printList(thirdList);
		    
		  /** Clone secondList */
		    MyDlist fourthList = cloneList(secondList);

		   /** Print all elements in fourthList. */
		    printList(fourthList);
		    
		   /** Compute the union of firstList and secondList */
	    MyDlist fifthList = union(firstList, secondList);
		    
		   /** Print all elements in thirdList. */ 
		    

		   /** Compute the intersection of thirdList and fourthList */
		    MyDlist sixthList = intersection(thirdList, fourthList);
		    
		   /** Print all elements in fourthList. */
		    printList(sixthList);
		    
		

		
	}

}
