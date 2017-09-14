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
	
	
	
	public static <K,V> AVLTree <K,V> clone(AVLTree<K,V> tree1)
	{
		AVLTree<K,V> clonedTree = new AVLTree<K,V>();
//		preOrder(tree1, clonedTree,(BTPosition<Entry<K, V>>) tree1.root(), (BTPosition<Entry<K, V>>) clonedTree.root());
		inOrder(tree1,clonedTree,(BTPosition<Entry<K, V>>)tree1.root(),(BTPosition<Entry<K, V>>) clonedTree.root());
		
		
		return clonedTree;
	}


	private static<K,V> void inOrder(AVLTree<K, V> tree1, AVLTree<K, V> clonedTree, BTPosition<Entry<K, V>> tree_node, BTPosition<Entry<K, V>> cloned_node) 
	{
		clonedTree.insertAtExternal(cloned_node, tree_node.element());
		if(!clonedTree.isExternal(tree_node.getLeft()))
		{
			inOrder(tree1,clonedTree,tree_node.getLeft(),cloned_node.getLeft());
		}
		
		if(!clonedTree.isExternal(tree_node.getRight()))
		{
			inOrder(tree1,clonedTree,tree_node.getRight(),cloned_node.getRight());
		}
		clonedTree.setHeight(cloned_node);
				
	}











	// MIAN FUNCTION
//
	
	public static void main(String[] args)
    { 
      String values1[]={"Sydney", "Beijing","Shanghai", "New York", "Tokyo", "Berlin",
     "Athens", "Paris", "London", "Cairo"}; 
      int keys1[]={20, 8, 5, 30, 22, 40, 12, 10, 3, 5};
      String values2[]={"Fox", "Lion", "Dog", "Sheep", "Rabbit", "Fish"}; 
      int keys2[]={40, 7, 5, 32, 20, 30};
         
      /* Create the first AVL tree with an external node as the root and the
     default comparator */ 
         
        AVLTree<Integer, String> tree1=new AVLTree<Integer, String>();

      // Insert 10 nodes into the first tree
         
        for ( int i=0; i<10; i++)
            tree1.insert(keys1[i], values1[i]);
       
      /* Create the second AVL tree with an external node as the root and the
     default comparator */
         
        AVLTree<Integer, String> tree2=new AVLTree<Integer, String>();
       
      // Insert 6 nodes into the tree
         
        for ( int i=0; i<6; i++)
            tree2.insert(keys2[i], values2[i]);
         
//        ExtendedAVLTree.print(tree1);
//        ExtendedAVLTree.print(tree2); 
//        ExtendedAVLTree.clone(tree1);
////      System.out.println(tree1.root().element().getKey());
          ExtendedAVLTree.clone(tree1);
////        System.out.println("-------------");
////        ExtendedAVLTree.print(ExtendedAVLTree.clone(tree1));
////        ExtendedAVLTree.print(ExtendedAVLTree.clone(tree2));
////        ExtendedAVLTree.merge(tree1, tree2);
//        ExtendedAVLTree.print(ExtendedAVLTree.merge(tree1, tree2));
//        ExtendedAVLTree.print(ExtendedAVLTree.merge(ExtendedAVLTree.clone(tree1), ExtendedAVLTree.clone(tree2)));
      }



}








//