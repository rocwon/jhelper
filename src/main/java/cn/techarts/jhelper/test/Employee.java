package cn.techarts.jhelper.test;

public class Employee {
	private int id;
	private String name;
	private int gender;
	private int age;
	private String mobile;
	
	public Employee() {}
	
	public Employee(int id) {
		this.id = id;
	}
	
	public Employee(String name,int gender, int age, String mobile) {
		this.setAge(age);
		this.setName(name);
		this.setGender(gender);
		this.setMobile(mobile);		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
