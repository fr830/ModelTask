package com.calldll.utils;

import java.util.ArrayList;

/**
 * 自定义队列数据结构
 * @author KIO
 *
 * @param <T>
 */
public class MyQueue<T> {
	private ArrayList<T> list;
	public static final int CAPACITY = 40;
	
	public MyQueue() {
		list = new ArrayList<T>();
	}
	
	//获取队列长度
	public int size() {
		return list.size();
	}
	
	//判断队列空
	public boolean isEmpty() {
		return size()==0;
	}
	
	//判断队列满
	public boolean isFull() {
		return size()>=20;
	}
	
	//入队
	public boolean push(T t) {
		if(isFull())return false;
		list.add(t);
		return true;
	}
	
	//出队
	public T pop() {
		if(isEmpty())return null;
		T item = list.get(0);
		list.remove(0);
		return item;
	}
	
	//获取队列头
	public T getFront() {
		if(isEmpty())return null;
		return list.get(0);
	}
	
	//获取队列尾部
	public T getRear() {
		if(isEmpty())return null;
		return list.get(size()-1);
	}
}
