package com.kio.entity.output;

import java.util.ArrayList;
import java.util.List;
/**
 * 运行结果行中的一个结果
 * @author lqjlqj
 *
 */
public class DataCell {
	private String name;
	private float time;
	
	private float a;
	private float b;
	private float c;
	
	private float d;
	private float e;
	private float f;
	private float g; 
	private float h;
	private float i;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}
	public float getA() {
		return a;
	}
	public void setA(float a) {
		this.a = a;
	}
	public float getB() {
		return b;
	}
	public void setB(float b) {
		this.b = b;
	}
	public float getC() {
		return c;
	}
	public void setC(float c) {
		this.c = c;
	}
	public float getD() {
		return d;
	}
	public void setD(float d) {
		this.d = d;
	}
	public float getE() {
		return e;
	}
	public void setE(float e) {
		this.e = e;
	}
	public float getF() {
		return f;
	}
	public void setF(float f) {
		this.f = f;
	}
	public float getG() {
		return g;
	}
	public void setG(float g) {
		this.g = g;
	}
	public float getH() {
		return h;
	}
	public void setH(float h) {
		this.h = h;
	}
	public float getI() {
		return i;
	}
	public void setI(float i) {
		this.i = i;
	}
public void setAllData(String[] strings) {
		
		this.setName(strings[0]);
		this.setA(Float.parseFloat(strings[1]));
		this.setB(Float.parseFloat(strings[2]));
		this.setC(Float.parseFloat(strings[3]));
		this.setD(Float.parseFloat(strings[4]));
		
		if (strings.length >= 7) {
			this.setE(Float.parseFloat(strings[5]));
			this.setF(Float.parseFloat(strings[6]));
		}
		
		if (strings.length == 10){
			this.setG(Float.parseFloat(strings[7]));
			this.setH(Float.parseFloat(strings[8]));
			this.setI(Float.parseFloat(strings[9]));
		}
	}
	
	public List<String> getAllData() {
		List<String> list = new ArrayList<>();
		
		list.add(0, this.getName());
    	list.add(1, this.getA()+"");
    	list.add(2, this.getB()+"");
    	list.add(3, this.getC()+"");
    	list.add(4, this.getD()+"");
    	list.add(5, this.getE()+"");
    	list.add(6, this.getF()+"");	    	
    	list.add(7, this.getG()+"");
    	list.add(8, this.getH()+"");
    	list.add(9, this.getI()+"");
    	
    	return list;
	}
	@Override
	public String toString() {
		return "DataCell [name=" + name + ", time=" + time + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e="
				+ e + ", f=" + f + ", g=" + g + ", h=" + h + ", i=" + i + "]";
	}
	
}
