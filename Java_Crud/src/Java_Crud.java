import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Java_Crud {

	private JFrame frame;
	private JTextField txtbname;
	private JTextField txtadd;
	private JTextField txtprice;
	private JTable table;
	private JTextField txtbookid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Java_Crud window = new Java_Crud();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Java_Crud() {
		initialize();
		Connect();
		table_load();
	}

	Connection con;
	PreparedStatement pst;
	ResultSet rs;

	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/java_crud", "root", "");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void table_load() {
		try {
			pst = con.prepareStatement("select * from book");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 759, 459);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Book Shop");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(294, 27, 178, 37);
		frame.getContentPane().add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(31, 90, 355, 223);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(10, 28, 82, 22);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Addition");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(10, 72, 82, 22);
		panel.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Price");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_2.setBounds(10, 115, 82, 22);
		panel.add(lblNewLabel_1_2);

		txtbname = new JTextField();
		txtbname.setBounds(120, 32, 195, 19);
		panel.add(txtbname);
		txtbname.setColumns(10);

		txtadd = new JTextField();
		txtadd.setColumns(10);
		txtadd.setBounds(120, 76, 195, 19);
		panel.add(txtadd);

		txtprice = new JTextField();
		txtprice.setColumns(10);
		txtprice.setBounds(120, 119, 195, 19);
		panel.add(txtprice);

		JButton btnsave = new JButton("Save");
		btnsave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bname, addition, price;

				bname = txtbname.getText();
				addition = txtadd.getText();
				price = txtprice.getText();

				try {
					pst = con.prepareStatement("insert into book(name,addition,price)values(?,?,?)");
					pst.setString(1, bname);
					pst.setString(2, addition);
					pst.setString(3, price);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Addedd!!!");
					table_load();

					txtbname.setText("");
					txtadd.setText("");
					txtprice.setText("");
					txtbname.requestFocus();

				} catch (SQLException e2) {
					e2.printStackTrace();
				}

			}
		});
		btnsave.setBounds(7, 165, 85, 35);
		panel.add(btnsave);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(120, 165, 85, 35);
		panel.add(btnExit);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtbname.setText("");
				txtadd.setText("");
				txtprice.setText("");
				txtbname.requestFocus();
			}
		});
		btnClear.setBounds(230, 165, 85, 35);
		panel.add(btnClear);

		JScrollPane table_1 = new JScrollPane();
		table_1.setBounds(396, 90, 315, 223);
		frame.getContentPane().add(table_1);

		table = new JTable();
		table_1.setViewportView(table);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(31, 337, 355, 61);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblbookid = new JLabel("Book ID");
		lblbookid.setBounds(10, 19, 82, 20);
		lblbookid.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblbookid);

		txtbookid = new JTextField();
		txtbookid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					String id = txtbookid.getText();

					pst = con.prepareStatement("select name,addition,price from book where id = ?");
					pst.setString(1, id);
					ResultSet rs = pst.executeQuery();

					if (rs.next() == true) {
						String name = rs.getString(1);
						String addition = rs.getString(2);
						String price = rs.getString(3);

						txtbname.setText(name);
						txtadd.setText(addition);
						txtprice.setText(price);
					} else {
						txtbname.setText("");
						txtadd.setText("");
						txtprice.setText("");
					}

				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		});
		txtbookid.setBounds(116, 20, 195, 20);
		txtbookid.setColumns(10);
		panel_1.add(txtbookid);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bname, addition, price, bid;

				bname = txtbname.getText();
				addition = txtadd.getText();
				price = txtprice.getText();
				bid = txtbookid.getText();

				try {
					pst = con.prepareStatement("update book set name= ?,addition=?,price=? where id =?");
					pst.setString(1, bname);
					pst.setString(2, addition);
					pst.setString(3, price);
					pst.setString(4, bid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Addedd!!!");
					table_load();

					txtbname.setText("");
					txtadd.setText("");
					txtprice.setText("");
					txtbname.requestFocus();

				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(457, 350, 85, 35);
		frame.getContentPane().add(btnUpdate);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bid;
				bid = txtbookid.getText();
				try {
					pst = con.prepareStatement("delete from book where id =?");
					pst.setString(1, bid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Delete!!!");
					table_load();

					txtbname.setText("");
					txtadd.setText("");
					txtprice.setText("");
					txtbname.requestFocus();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnDelete.setBounds(590, 350, 85, 35);
		frame.getContentPane().add(btnDelete);
	}
}
