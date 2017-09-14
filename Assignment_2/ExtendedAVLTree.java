package net.datastructures;


//Hao Chen z5102446
// COMP9024 Ass2

import java.awt.*;
import java.util.*;
import javax.swing.*;


public class ExtendedAVLTree<K,V> extends AVLTree<K,V>
{
	/**Q1. This class method creates an identical copy of the AVL tree specified by the
	 *     parameter and returns a reference to the new AVL tree.
	 * 
	 *     The time complexity of this method is O(n).
	 *     
	 * **/
	
	public static <K,V> AVLTree<K,V> clone(AVLTree<K,V> tree)
	{
		// declare new AVLTree object as clonedTree
		AVLTree<K,V> clonedTree = new AVLTree<K,V>();
		//use the clone tree function to insert recursively
		clone_Tree(tree,clonedTree,(BTPosition<Entry<K, V>>) tree.root(), (BTPosition<Entry<K, V>>) clonedTree.root());
		clonedTree.size = tree.size;
		return clonedTree;	
		
	}
	
	private static <K,V > void clone_Tree(AVLTree<K,V> tree, AVLTree<K,V> clonedTree, BTPosition<Entry<K, V>> Node, BTPosition<Entry<K, V>> cloned_Node) 
	{
		clonedTree.insertAtExternal(cloned_Node, Node.element());
		if(clonedTree.isExternal(Node.getLeft())==false)
		{
			clone_Tree(tree,clonedTree,Node.getLeft(),cloned_Node.getLeft());
		}
		if(clonedTree.isExternal(Node.getRight())==false)
		{
			clone_Tree(tree,clonedTree,Node.getRight(),cloned_Node.getRight());
		}
		clonedTree.setHeight(cloned_Node);
	}
	



	/**Q2. This class method merges two AVL trees, tree1 and tree2, into a new tree, and
	 * 	   destroys tree1 and tree2.
	 * 
	 * 	   The time complexity of your merge method must be O(m+n), where m and n are the
	 * 	   numbers of nodes of the two input AVL trees. 
	 *     
	 *     Hint: 1. Create a sorted array list containing all the entries in both AVL trees in O(m+n) time. 
	 *           2. Construct an AVL tree based on the sorted array list in O(m+n) time. 
	 *              Put your running time analysis as
	 *              comments after the code. (4 marks)
	 *              
	 *     The time complexity of your merge method is O(m+n).
	 *            
	 * **/	
	public static <K, V> AVLTree<K, V> merge(AVLTree<K,V> tree1, AVLTree<K,V> tree2 ) 
	{
		NodePositionList<Entry<K, V>> list_tree1 = new NodePositionList<Entry<K, V>>();
		NodePositionList<Entry<K, V>> list_tree2 = new NodePositionList<Entry<K, V>>();
		
		// First part, convert all the nodes into list, and reclaim two original trees, O(m+n)
		inOrder((BTPosition<Entry<K, V>>)tree1.root(), list_tree1);	//O(m)
		inOrder((BTPosition<Entry<K, V>>)tree2.root(), list_tree2);	//O(n)
		tree1 = null;
		tree2 = null;
		
		// Second part, insert elements in list 2 into list 1, complexity of while loop is O(m+n)
		Position<Entry<K, V>> p1 = list_tree1.first();
		Position<Entry<K, V>> p2 = list_tree2.first();
		int length = list_tree2.size();
		while(true) 
		{  
			//if the p2 >= p1, then their order should be p1, p2
			if (Integer.parseInt(p2.element().getKey().toString()) - Integer.parseInt(p1.element().getKey().toString()) >= 0) 
			{
				//p2>p1 add p2 before p1
				list_tree1.addBefore(p1, p2.element());
				//if length > 1, then p2 move to the next Since p2 is already inserted
				if (length > 1)
				{
					p2 = list_tree2.next(p2);
				}
				length--;
			}

			//if the p1<p2 then their order should be p1 p2 
			else 
			{
				//p1<p2 add p2 after p1
				list_tree1.addAfter(p1, p2.element());
				if (length > 1)
				{
					p2 = list_tree2.next(p2);	
				}
				length--;
			}
			
			//there must have a result in the above, so when a p2 is inserted, move p1 to the next
			if(list_tree1.isLast(p1) == false) 
			{
				p1 = list_tree1.next(p1);
			} 
			
			//break when length <= 0 
			if(length <= 0)
			{
				break;
			}
		}
		
		// Third part, convert the list into an array, complexity of for loop is O(m+n)
		Entry<K, V>[] list_array = new Entry[64];
		Position<Entry<K, V>> p = list_tree1.first();
		for (int j = 0; j < list_tree1.size(); j++) 
		{
			list_array[j] = p.element();
			if (j < list_tree1.size()-1)
				p = list_tree1.next(p);
		}
		
		// Fourth part, insert into AVL tree, calculate the height of each node
		// complexity of insertMerge method is O(m+n)
		// complexity of postOrderHeight method is O(m+n)
		AVLTree<K, V> mergedTree = new AVLTree<K,V>();
		insertMerge(mergedTree, (BTPosition<Entry<K, V>>)mergedTree.root(), list_array, 1, list_tree1.size());
		postOrderHeight(mergedTree, (BTPosition<Entry<K, V>>)mergedTree.root());
		return mergedTree;
	}
	
	// This method search the whole tree and put all the elements into a list
	// Time complexity: O(m) for first tree and O(n) for second tree
	public static <K, V> void inOrder(BTPosition<Entry<K, V>> r, NodePositionList<Entry<K, V>> list)
	{
		if(r.getLeft().getLeft() != null)
			inOrder(r.getLeft(), list);
		list.addLast(r.element());
		if(r.getRight().getRight() != null)
			inOrder(r.getRight(), list);
	}
	
	
	// This method is an insert method, the middle element is the root node, 
	// the middle element of the left half is the left child of the root node,
	// and the middle element of the right half is the right child of the root node,
	// and so on until all elements are inserted.
	// The height of each node is inserted directly instead of calculated 
	// in order to reduce time complexity
	// Time complexity: O(m+n)
	public static <K, V> void insertMerge(AVLTree<K, V> mergeAVLT, BTPosition<Entry<K, V>> c, Entry<K, V>[] list_array, int left_point, int right_point) 
	{
		int offset = (int)Math.floor((right_point - left_point)/2);
		int middle_point = left_point + offset;
		mergeAVLT.insertAtExternal(c, list_array[middle_point-1]);
		if (left_point < middle_point)
			insertMerge(mergeAVLT, c.getLeft(), list_array, left_point, middle_point - 1);
		if (middle_point < right_point)
			insertMerge(mergeAVLT, c.getRight(), list_array, middle_point + 1, right_point);
	}
	
	// The height of each node is inserted from the max height of it's children nodes + 1,
    // only the height of two children need to be checked instead of check all the descendants. 
	// The time complexity is O(m+n)
	public static <K, V> void postOrderHeight(AVLTree<K, V> mergeAVLT, BTPosition<Entry<K, V>> r)
	{
		if(mergeAVLT.isExternal(r))
			((AVLNode<K,V>)r).setHeight(0);
		if(r.getLeft().getLeft() != null) 
			postOrderHeight(mergeAVLT, r.getLeft());
		if(r.getRight().getRight() != null) 
			postOrderHeight(mergeAVLT, r.getRight());
		((AVLNode<K,V>)r).setHeight(1 + Math.max( ((AVLNode<K,V>)(r.getLeft())).getHeight(), ((AVLNode<K,V>)(r.getRight())).getHeight()) );
	}


	/**Q3. This class method creates a new window and prints the AVL tree specified by the
	 *     parameter on the new window. 
	 *     
	 *     Each internal node is displayed by a circle containing
	 *     its key and each external node is displayed by a rectangle. 
	 *     You need to choose a proper size for all the circles and a proper size for all the rectangles and make sure
	 *     that this method never prints a tree with crossing edges.
	 *            
	 * **/
	
	public static <K, V> void print(AVLTree<K, V> tree)
	{
		final AVLTree<K, V> t = tree;
		final int TOTAL_HEIGHT = t.height(tree.root());
		final int T_HEIGHT = 80, T_WIDTH = 40, DIAMETER = 32 ,KEY_TO_CENTER = 9, OFFSET = 15;
		final int X_ROOT = (int)((Math.pow(2, TOTAL_HEIGHT) * T_WIDTH)/ 2) , Y_ROOT = 50;
		final BTPosition<Entry<K, V>> root = (BTPosition<Entry<K, V>>)tree.root();
		
		class AVLTreeJPanel extends JPanel 
		{

			private static final long serialVersionUID = -1951270085252137905L;
			public void paintComponent(Graphics g) 
			{
				super.paintComponent(g); 
				// Print the root node first and then print left subtree and then print right subtree.
				// If the input tree is empty, then just print a rectangle.
				if (t.isExternal(root)) 
					g.drawRect(X_ROOT - DIAMETER/2, Y_ROOT - DIAMETER/2 - OFFSET, DIAMETER, DIAMETER);
				else {
					g.drawString(root.element().getKey().toString(), 
							X_ROOT - KEY_TO_CENTER, Y_ROOT - KEY_TO_CENTER);
					g.drawOval(X_ROOT - DIAMETER/2, Y_ROOT - DIAMETER/2 - OFFSET, 
							DIAMETER, DIAMETER);
					printAVLTree(t, root.getLeft(), X_ROOT, Y_ROOT, g, true);
					printAVLTree(t, root.getRight(), X_ROOT, Y_ROOT, g, false);
			}
		}
			public void printAVLTree(AVLTree<K,V> t, BTPosition<Entry<K,V>> r, int x_parent, int y_parent, Graphics g, boolean has_left)
			{
				int x, y;
				int h = t.height(r);
				// Internal nodes: print the key and a circle.
				if(t.isInternal(r))
				{
					if (has_left)
						x = (int)(x_parent - Math.pow(2, h-1) * T_WIDTH);
					else
						x = (int)(x_parent + Math.pow(2, h-1) * T_WIDTH);
					y = y_parent + T_HEIGHT;
					g.drawString(r.element().getKey().toString(), 
							x - KEY_TO_CENTER, y - KEY_TO_CENTER);
					g.drawOval(x - DIAMETER/2, y - DIAMETER/2 - OFFSET, 
							DIAMETER, DIAMETER);
					printAVLTree(t, r.getLeft(), x, y, g, true);
					printAVLTree(t, r.getRight(), x, y, g, false);
				} 
				// External nodes: print a rectangle.
				else 
				{
					if (has_left)
						x = x_parent - T_WIDTH/2 ;
					else
						x = x_parent + T_WIDTH/2 ;
					y = y_parent + T_HEIGHT;
					g.drawRect(x - DIAMETER/2, y - DIAMETER/2 - OFFSET, DIAMETER, DIAMETER);
				}
				// Print the line between two nodes.
				double z = Math.sqrt(Math.pow(x_parent - x, 2) + Math.pow(y_parent - y, 2));
				int lx = (int)(DIAMETER/2 * (x_parent - x) / z);
				int ly = (int)(DIAMETER/2 * (y - y_parent) / z);
				g.drawLine(x_parent - lx , y_parent + ly - OFFSET, x + lx , y - ly - OFFSET);
			}
		}
		JFrame frame = new JFrame("AVL Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		AVLTreeJPanel avltree = new AVLTreeJPanel();
		frame.add(avltree);
		frame.setBackground(Color.WHITE);
		frame.setSize(X_ROOT * 2, (TOTAL_HEIGHT + 2) * T_HEIGHT);
		frame.setVisible(true);	
	}


	
	


    
    
//     // MIAN FUNCTION
// //
	
// 	public static void main(String[] args)
//     { 
//       String values1[]={"Sydney", "Beijing","Shanghai", "New York", "Tokyo", "Berlin",
//      "Athens", "Paris", "London", "Cairo"}; 
//       int keys1[]={20, 8, 5, 30, 22, 40, 12, 10, 3, 5};
//       String values2[]={"Fox", "Lion", "Dog", "Sheep", "Rabbit", "Fish"}; 
//       int keys2[]={40, 7, 5, 32, 20, 30};
         
//       /* Create the first AVL tree with an external node as the root and the
//      default comparator */ 
         
//         AVLTree<Integer, String> tree1=new AVLTree<Integer, String>();

//       // Insert 10 nodes into the first tree
         
//         for ( int i=0; i<10; i++)
//             tree1.insert(keys1[i], values1[i]);
       
//       /* Create the second AVL tree with an external node as the root and the
//      default comparator */
         
//         AVLTree<Integer, String> tree2=new AVLTree<Integer, String>();
       
//       // Insert 6 nodes into the tree
         
//         for ( int i=0; i<6; i++)
//             tree2.insert(keys2[i], values2[i]);
         
//         ExtendedAVLTree.print(tree1);
//         ExtendedAVLTree.print(tree2); 
// //        ExtendedAVLTree.clone(tree1);
// ////      System.out.println(tree1.root().element().getKey());
// ////        ExtendedAVLTree.clone(tree1);
// ////        System.out.println("-------------");
// ////        ExtendedAVLTree.print(ExtendedAVLTree.clone(tree1));
// ////        ExtendedAVLTree.print(ExtendedAVLTree.clone(tree2));
// ////        ExtendedAVLTree.merge(tree1, tree2);
//         ExtendedAVLTree.print(ExtendedAVLTree.merge(tree1, tree2));
// //        ExtendedAVLTree.print(ExtendedAVLTree.merge(ExtendedAVLTree.clone(tree1), ExtendedAVLTree.clone(tree2)));
//       }



}








//