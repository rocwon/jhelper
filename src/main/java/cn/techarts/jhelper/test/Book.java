package cn.techarts.jhelper.test;

public class Book {
	private int id;
	private String isbn;
	private String name;
	private boolean chinese;
	
	public Book() {}
	
	public Book(int id) {
		this.id = id;
	}
	
	public Book(int id, String isbn, String name) {
		this.id = id;
		this.isbn = isbn;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isChinese() {
		return chinese;
	}

	public void setChinese(boolean chinese) {
		this.chinese = chinese;
	}
}
