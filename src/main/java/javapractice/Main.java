package javapractice;

class Parent {

	public static void print() {
		System.out.println("parent");
	}

	public void display() {
		print();
	}
}

class Child extends Parent {
	public static  void print() {
		System.out.println("child");
	}
}

public class Main {

	public static void main(String[] args) {
		Child child = new Child();
		child.display();
	}

}
