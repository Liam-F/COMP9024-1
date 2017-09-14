/* COMP9024 Assignment3
 * Hao Chen 
 * z5102446
 * */

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.datastructures.*;

/*  This class has 3 parts.
 * 
 *  Part I.   Read from file1 and store in "heap1".
 *  
 *  Part II.  Set time++ and insert the released task into heap_ready (key=dead_line).
 *  		  Create the heap_core and set their available time, insert the task from heap_ready.
 *  		  Record the task into the schedule.
 * 
 *  Part III. Print array "schedule" to file2
 *  
 *  Time complexity: O(n*log(n))
 * */

//HeapEntry is used to store the information of one task, like dead_line, release_time, name and the execution_time
class HeapEntry<D, N> 
{
	protected D dead_line;
	protected D release_time;
	protected D execution_time;
	protected N task_name;
	
	public HeapEntry(D d, D r, D e, N n) 
	{
		dead_line = d;
		release_time = r;
		execution_time = e;
		task_name = n;
	}

	public D getDeadline() 
	{
		return dead_line;
	}
	
	public D getReleaseTime() 
	{
		return release_time;
	}	
	
	public D getExecution() 
	{
		return execution_time;
	}	
	
	public N getTaskName()
	{
		return task_name;
	}
	
}

public class TaskScheduler 
{
	static <N, D> void scheduler(String file1, String file2, int m)
	{
		//Part I.
		// read from file1 and store in "heap1"
		// store the release time as key, and the execution time ,the deadline and task name as a HeapEntry as value
		//
		// Time complexity:
		// the sentences for reading the task name, execution time, release time and deadline has O(n) complexity,
		// and the insert() method of heap1 has the complexity of O(log(n))
		//
		// Time complexity of this part is O(n*log(n))
		File f1 = new File(file1);
		HeapPriorityQueue<Integer, HeapEntry<Integer, String>> heap_1 = 
				new HeapPriorityQueue<Integer, HeapEntry<Integer, String>>();
		
		String task_name;
		int release_time;
		int execution_time;
		int dead_line;
		int task_number = 0;
		String regex = "[0-9a-zA-Z]+";
		Pattern pattern = Pattern.compile(regex);
		try
		{
			Scanner sc = new Scanner(f1);
 			while(sc.hasNext()) 
			{
				task_name = sc.next(); 
				Matcher m_name = pattern.matcher(task_name);
				if(m_name.matches() == false)
				{
					System.out.println("The task attributes of " + file1
							+ "do not match the format.");
					return;
				}
				try
				{
					execution_time = sc.nextInt(); 
					release_time = sc.nextInt();   
					dead_line = sc.nextInt();	   
				}
				catch(InputMismatchException e)
				{
					System.out.println("The task attributes of " + file1
							+ "do not match the format.");
					return;
				}	
				HeapEntry<Integer, String> task_info = new HeapEntry<Integer, String>(dead_line, release_time, execution_time, task_name);				
				heap_1.insert(release_time, task_info); 
				task_number++;
			}//n*log(n)
 			sc.close();
		}
		catch(FileNotFoundException fnf)
		{
			System.out.println(file1 + " does not exist!");
			return;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	

		//Part II.
		//First create the heap_core and insert the core number with key = their available time 
		//Second create the heap_ready to store the released task with key = their dead_line
		//Set the initial time = 0, time++, if the task release time <= time, then release the task 
		//into the heap_ready.
		//Then put the tasks to the heap_core if the core available time <= the task release time,  
		//while recored it into the schedule. at the same time.
		//
		// Time complexity of this part is O(n*log(n))
		HeapPriorityQueue<Integer, HeapEntry<Integer, String>> heap_ready = 
				new HeapPriorityQueue<Integer, HeapEntry<Integer, String>>();
		
		HeapPriorityQueue<Integer, String> heap_core = new HeapPriorityQueue<Integer, String>();
		ArrayList<String> schedule = new ArrayList<String>();
		
		int current_available_core = 1;
		while(true)//m*log(m)
		{
			if(current_available_core > m)
			{break;}
			heap_core.insert(0, "Core"+current_available_core); //log(m)
			current_available_core++;
		}
			
		int time = 0;	
		while(true)
		{			
			while(true)
			{
				if(heap_1.isEmpty())
				{break;}
				if (heap_1.min().getKey() > time)
				{break;}	
				HeapEntry<Integer, String> temp = new HeapEntry<Integer, String>(heap_1.min().getValue().dead_line, heap_1.min().getValue().release_time, heap_1.min().getValue().execution_time, heap_1.min().getValue().task_name);				
				heap_ready.insert(temp.getDeadline(),temp); //log(n)
				heap_1.removeMin();//log(n)
			}//2nlog(n)		
			
 			while(true)
			{
				if(heap_ready.isEmpty())
				{break;}
				
				if(heap_core.min().getKey() > time)
				{break;}
				
				if(heap_core.min().getKey() + heap_ready.min().getValue().getExecution() > heap_ready.min().getValue().dead_line)
				{
					System.out.println("No feasible schedule exists.");
					return;
				}
				schedule.add(heap_ready.min().getValue().getTaskName());
				schedule.add(heap_core.min().getValue());
				schedule.add( String.valueOf(time));
				schedule.add(" ");

				String core_name = heap_core.min().getValue();
				heap_core.removeMin();
				heap_core.insert(time + heap_ready.min().getValue().execution_time,core_name);
				heap_ready.removeMin();	
				task_number--;
			}//n*log(n)+2n*log(m)
 			if(task_number==0)
 			{break;}
			time++;
		}
		

		
		//Part III.
		//print ArrayList schedule to file2
		//
		//Time complexity of this part = O(n)
		File f2 = new File(file2 + ".txt");
		try
		{
			FileWriter fos = new FileWriter(f2,false);
			for(int i=0;i<schedule.size();i++) 
			{
				fos.write(schedule.get(i));
				fos.write(" ");
				if(schedule.get(i) == " ")
				{
					fos.write("\r");
				}
			}//O(n)
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
}

