
import java.awt.BorderLayout;
import java.awt.event.*;
//import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;

public class Main extends JFrame{

	ResturantManager rMan;
	CustomerManager cMan;


	static Connection con;

	String driver = "org.sqlite.JDBC";
	String url = "jdbc:sqlite:MySQLiteDB";


	private Statement stmt;
	private ResultSet rs; // DB 연결용 객체 선언

	public Main() {
		super("Order processing program simulator");
		
		conDB();
		layInit();
		
		setVisible(true);

		setBounds(200, 200, 500, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void layInit() {


		rMan = new ResturantManager(con);
		cMan = new CustomerManager(con);


		add("Center", rMan);
		add("South", cMan);


	}

	void conDB(){

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} // DB 연결

		
		System.out.println("DB Connecting......");
		try {
		con = DriverManager.getConnection(url); // DB 연결
		System.out.println(con.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
		stmt = con.createStatement(); // query문 실행할 객체
		}catch (SQLException e) {
			e.printStackTrace();
		}
		

	}

	

	public static void main(String[] args) {
		
		Main main = new Main();


		main.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					con.close();
				} catch (Exception e4) {
				}
				System.out.println("프로그램 완전 종료!");
				System.exit(0);
			}
		});
	}
}