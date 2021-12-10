import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ResturantManager extends JPanel implements ActionListener{

	DefaultTableModel model;
	JTable table;
	JScrollPane tableScrollPane;
	
	JButton refreshBtn, processBtn;

	Statement stmt;
	String query;
	ResultSet rs;
	
	

	int colNum;

	String fields[];

	
	Connection conn;

	public ResturantManager(Connection conn) {
		
		setBackground(Color.DARK_GRAY);
		
		this.conn = conn;
		
		initTable();
		
		initLayout();
				
	}
	

	
	void initLayout() {
		refreshBtn = new JButton("REFRESH");
		processBtn = new JButton("PROCESS");
		
		refreshBtn.addActionListener(this);
		processBtn.addActionListener(this);
		
		add(tableScrollPane);
		add(refreshBtn);
		add(processBtn);
	}
	
	void initTable() {

		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		query = "select * from orders";
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResultSetMetaData rsmd = null;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			colNum = rsmd.getColumnCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fields = new String[colNum];

		
		try {
			for (int i = 0; i < colNum; i++) {

				fields[i] = rsmd.getColumnName(i + 1);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model = new DefaultTableModel(fields, 0);
		
		table = new JTable(model);
		
		tableScrollPane = new JScrollPane(table);
		
		
		
		updateModel();
	}
	
	void updateModel() {
		
		model.setRowCount(0);
		
		String colData[];
		colData = new String[colNum];
		
		query = "select * from orders";
		
		
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			while(rs.next()) {
				for(int i=0;i<colNum;i++) {
					colData[i] = rs.getString(i+1);
				}
				
				model.addRow(colData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.fireTableChanged(null);
	}
	

	
	void processOrder(int n) {
		
		query = "insert into processed_orders(orderid, customername, foodid, orderdate)"+
		" values("+
		model.getValueAt(n, 0)+","+
		"\'"+model.getValueAt(n, 1)+"\'"+","+
		model.getValueAt(n, 2)+","+
		"\'"+model.getValueAt(n, 3)+"\'"+");";
				
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		query = "delete from orders where orderid ="+
		model.getValueAt(n, 0) + ";";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updateModel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == refreshBtn) {
			updateModel();
		}
		else if(e.getSource() == processBtn) {
			int n = table.getSelectedRow();
			if(n < 0) return;
			
			processOrder(n);
		}

	}


}
