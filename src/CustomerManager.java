import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CustomerManager extends JPanel implements ActionListener{
	
	Statement stmt;
	String query;
	ResultSet rs;
	Connection conn;
	
	JButton addCustBtn, delCustBtn;
	
	Random rand;
	
	LinkedList<Customer> customerList;
	
	int foodNoMax;
	
	String peopleNames[]=
			{
					"john",
					"james",
					"shin",
					"delphrado",
					"lawson",
					"lupinek",
					"lee",
					"gatson"
			};
	

	public CustomerManager(Connection conn) {
		
		setBackground(Color.white);
		
		try {
			stmt = conn.createStatement();
			query = "select count(*) from foods;";
			rs = stmt.executeQuery(query);

			rs.next();
			foodNoMax = rs.getInt(1);
			System.out.println("n:"+foodNoMax);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		this.conn=conn;
		
		
		addCustBtn = new JButton("add customer");
		delCustBtn = new JButton("delete customer");
		
		customerList = new LinkedList<Customer>();
		rand = new Random();
		
		
		
		addCustBtn.addActionListener(this);
		delCustBtn.addActionListener(this);

		add(addCustBtn);
		add(delCustBtn);
		
	}
	
	void addCustomer() {
		int peopleNo = peopleNames.length;
		
		String newName = peopleNames[rand.nextInt(peopleNo)];
		
		Customer newCustomer = new Customer(conn, foodNoMax, newName);
		
		customerList.addLast(newCustomer);
		
		newCustomer.start();
	}
	
	void deleteCustomer() {
		if(customerList.size() > 0) {
			customerList.getFirst().makeItDone();
			customerList.removeFirst();
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == addCustBtn) addCustomer();
		else if(e.getSource() == delCustBtn) deleteCustomer();
		
	}



}

class Customer extends Thread{
	
	Statement stmt;
	String query;
	ResultSet rs;
	
	long interval;
	Random rand;
	
	boolean done;
	
	String name;
	int foodNoMax;
	
	void makeItDone() {
		done = true;
	}
	
	
	Customer(Connection conn, int foodNoMax, String name){
		
		this.foodNoMax = foodNoMax;
		this.name = name;
		
		done = false;
		
		rand = new Random(System.currentTimeMillis());
		
		interval = rand.nextInt(3000)+2000;
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	String currentTime() {
		
		LocalDateTime now = LocalDateTime.now();

		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

		return formatedNow;


	}
	
	private void makeOrder() {
		
		query = "insert into orders(customername, foodid, orderdate) values("+
				"\'"+name+"\'"+ ","+
				(rand.nextInt(foodNoMax)+1)+ ","+
				"\'"+currentTime()+"\');";

		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run() {
		
		while(!done) {
			
			makeOrder();
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}