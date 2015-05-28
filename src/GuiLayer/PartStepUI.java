package GuiLayer;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import java.awt.FlowLayout;

import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JSplitPane;
import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;
import javax.swing.JDesktopPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JScrollBar;
import javax.swing.JList;

import java.awt.GridLayout;

import CtrLayer.OrderCtr;
import ModelLayer.Customer;
import ModelLayer.Order;
import ModelLayer.PartStep;
import ModelLayer.Step;
import ModelLayer.Town;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.SpringLayout;

import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * 
 * @author Kim Dam Gr�nh�j, Tobias, Bo
 *
 */
public class PartStepUI extends JFrame {
	private OrderCtr orderCtr;
	
	public PartStepUI() {
		orderCtr = new OrderCtr();
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(523, 36, 485, 675);
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.add(panel_2);
		
		JLabel lblNewLabel = new JLabel("Ordreliste");
		lblNewLabel.setBounds(20, 11, 330, 14);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Ordre detaljer");
		lblNewLabel_1.setBounds(524, 11, 363, 14);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(lblNewLabel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBounds(20, 36, 493, 675);
		panel.add(scrollPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(new MigLayout("", "[475px]", "[160px]"));
		
		createOrderItems(panel_1);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(0, 0, 200, 50);
		
		this.setVisible(true);
		this.setResizable(false);
		this.setSize(new Dimension(1024, 768));
	}
	
	private void createOrderItems(JPanel panel1)
	{
		List<Order> orders = new ArrayList<Order>();
		try {
			orders = orderCtr.findAllActiveOrders(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int i = 0;
		for (Order order : orders) {
			Customer customer = order.getCustomer();
			Town town = customer.getTown();
			List<PartStep> partSteps = order.getPartStepList();
			
			// sweep
			Date timeNow = new Date();
			//for (PartStep partStep : )
			
			JPanel panel_7 = new JPanel();
			panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
			panel_7.setLayout(null);
			
			// add panel to rows
			panel1.add(panel_7, "cell 0 " + i + ",grow");

			// items for panel
			JLabel lblNewLabel_2 = new JLabel("Ordre nr:");
			lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblNewLabel_2.setBounds(10, 11, 255, 14);
			panel_7.add(lblNewLabel_2);
			
			JPanel panel_4 = new JPanel();
			panel_4.setBounds(270, 12, 195, 104);
			panel_7.add(panel_4);
			panel_4.setLayout(null);
			
			JList list = new JList();
			list.setBounds(97, 5, 0, 0);
			panel_4.add(list);
			
			JList list_1 = new JList();
			list_1.setModel(new AbstractListModel() {
				String[] values = new String[] {"tst", "testasd"};
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
			list_1.setBounds(0, 0, 185, 104);
			panel_4.add(list_1);
			
			JButton btnNewButton = new JButton("Se detaljer");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			btnNewButton.setBounds(356, 127, 100, 23);
			panel_7.add(btnNewButton);
			
			JLabel lblNavn = new JLabel(customer.getName());
			lblNavn.setBounds(10, 35, 173, 14);
			panel_7.add(lblNavn);
			
			JLabel lblAdresse = new JLabel(customer.getStreet());
			lblAdresse.setBounds(10, 53, 173, 14);
			panel_7.add(lblAdresse);
			
			JLabel lblPostnrBy = new JLabel(town.getZip() + " " + town.getName());
			lblPostnrBy.setBounds(10, 71, 173, 14);
			panel_7.add(lblPostnrBy);
			
			JLabel lblTrinStTrin = new JLabel("Trin: S\u00E6t trin");
			lblTrinStTrin.setBounds(10, 136, 179, 14);
			panel_7.add(lblTrinStTrin);
			
			String orderId = Integer.toString(order.getId());
			
			JLabel lblNewLabel_3 = new JLabel(orderId);
			lblNewLabel_3.setBounds(69, 7, 122, 23);
			panel_7.add(lblNewLabel_3);
			
			i++;
		}
		
		/*groupL.setHorizontalGroup(groupL.createParallelGroup(Alignment.LEADING));
		groupL.setVerticalGroup(groupL.createParallelGroup(Alignment.LEADING));
		
		SequentialGroup ggg = groupL.createSequentialGroup();
		ggg = ggg.addContainerGap();
		
		SequentialGroup aaa = groupL.createSequentialGroup();
		aaa = aaa.addContainerGap();
		
		for (int i = 0; i<10;i++) {
			
			JPanel panel_3 = new JPanel();
			
			ggg = ggg.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 454, GroupLayout.PREFERRED_SIZE);
		
			aaa = aaa.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 105 + (11 * i), GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED);
			

			
			JLabel lblNewLabel_2 = new JLabel("New label");
			lblNewLabel_2.setBounds(10, 11, 46, 14);
			panel_3.add(lblNewLabel_2);
			
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(253, 11, 191, 97);
			panel_3.add(scrollPane_1);
			
			JPanel panel_4 = new JPanel();
			scrollPane_1.setViewportView(panel_4);
		}
		
		ggg = ggg.addContainerGap();
		aaa = aaa.addContainerGap(430, Short.MAX_VALUE);
		
		groupL.setHorizontalGroup(groupL.createParallelGroup(Alignment.LEADING).addGroup(ggg));
		groupL.setVerticalGroup(groupL.createParallelGroup(Alignment.LEADING).addGroup(aaa));*/
	}
}
