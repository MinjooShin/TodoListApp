package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		
		String title="", desc="", category="", due_date="";
		Scanner sc = new Scanner(System.in);

		System.out.println("\n"
				+ "========== Create item Section\n"
				+ "enter the title > ");
		title=sc.next();
		sc.nextLine();//제목 뒤에 enter없애주기 위함.
		if (list.isDuplicate(title)) {
			System.out.println("title can't be duplicate");
			return;
		}
		
		System.out.println("enter the category > ");
		category = sc.next();
		sc.nextLine();//제목 뒤에 enter없애주기 위함.
		
		System.out.println("enter the description > ");
		desc = sc.nextLine().trim();
		
		System.out.println("enter the due_date > ");
		due_date = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(title, category, desc, due_date);
		
		if(list.addItem(t)>0) System.out.println("item added!");
		
	}

	public static void deleteItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("\n"
				+ "========== Delete Item Section\n"
				+ "enter the number of item to remove\n"
				+ "\n");
		int num = sc.nextInt();
		if(l.deleteItem(num)>0) System.out.println("item deleted!");
		sc.close();
	}


	public static void updateItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("\n"
				+ "========== Edit Item Section\n"
				+ "enter the number of the item you want to update\n"
				+ "\n");
		int num = sc.nextInt();
		sc.nextLine(); //엔터키 제거
		
		System.out.println("enter the new title ");
		String new_title = sc.nextLine().trim();
		
		System.out.println("enter the new category of the item");
		String new_category = sc.next();
		sc.nextLine(); //엔터키 제거

		System.out.println("enter the new description ");
		String new_desc = sc.nextLine().trim();

		System.out.println("enter the due_date");
		String new_due_date = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(new_title, new_desc, new_category, new_due_date);
		t.setId(num);
		if(l.updateItem(t)>0) System.out.println("item updated!");
		
	}
	
	public static void saveList(TodoList l, String filename) {
	//list내용을 filename에 저장.
		try {
			Writer w = new FileWriter(filename, true); 
			for(TodoItem item: l.getList()) {
				w.write(item.toSaveString());
			}
			w.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadList(TodoList l, String filename) {
		//파일 내용 읽기
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			String oneline;
			while((oneline=br.readLine())!=null) {
				StringTokenizer st = new StringTokenizer(oneline, "-");
				int id=Integer.parseInt(st.nextToken());
				String category=st.nextToken();
				String title=st.nextToken();
				String desc=st.nextToken();
				String current_date=st.nextToken();
				String due_date=st.nextToken();
				
				TodoItem item = new TodoItem(title, category, desc, due_date);
				item.setId(id);
				System.out.println(item.toString());
			}
			br.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void findItem(TodoList l, String choice) {
		String find_str="";
		find_str=choice.substring(5);
		int count=0;
		int cnt_sum=0;
		for(TodoItem item: l.getListforfind(find_str)) {
			count++;
			if(item.getCategory().contains(find_str)||item.getTitle().contains(find_str)||item.getDesc().contains(find_str)) {
				System.out.println(item.toString());
				cnt_sum++;
			}
		}
		System.out.println("총 "+cnt_sum+"개의 항목을 찾았습니다.");
	}

	public static void findCate(TodoList l, String choice) {
		String find_str="";
		find_str=choice.substring(10);
		int count=0;
		for(TodoItem item : l.getListCategory(find_str)) {
			System.out.println(item.toString());
			count++;
		}
		System.out.printf("\n총 %d개의 항목을 찾았습니다.\n", count);
	}

	public static void listCate(TodoList l) {
		int count=0;
		for(String item: l.getCategories()) {
			System.out.print(item+" ");
			count++;
		}
		System.out.printf("\n총 %d개의 카테고리 등록되어 있습니다.\n", count);
	}

	public static void listAll(TodoList l, String orderby, int ordering) {
		int count=0;
		System.out.printf("[전체 목록, 총 %d개]\n", l.getCount());
		for(TodoItem item:l.getOrderedList(orderby, ordering)) {
			count++;
			System.out.println(item.toString());
		}
	}
	
	public static void completeItem(TodoList l, String choice) {
		int count=0;
		if(choice.equals("ls_completed")) {
			for(TodoItem item : l.getListcomp()) {
				count++;
				System.out.println(item.toString());
			}
			System.out.printf("총 %d개의 항목이 완료되었습니다.", count);
		}	
		else {
			String find_str="";
			find_str=choice.substring(5);
			int com_idx =Integer.parseInt(find_str);
			if(l.checkComp(com_idx)>0) System.out.println("check completed!");	
			}
		}
	}
	