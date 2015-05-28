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
import CtrLayer.PartStepCtr;
import ModelLayer.Customer;
import ModelLayer.Order;
import ModelLayer.OrderInfoViewModel;
import ModelLayer.PartOrder;
import ModelLayer.PartStep;
import ModelLayer.Product;
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
	private PartStepCtr partstepCtr;
	private List<Order> orders = new ArrayList<Order>();
	JPanel panel_2 = new JPanel();
	public PartStepUI() {
		orderCtr = new OrderCtr();
		partstepCtr = new PartStepCtr();
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
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
		
		
		panel_2.setLayout(null);		
		
		
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(0, 0, 200, 50);
		
		this.setVisible(true);
		this.setResizable(false);
		this.setSize(new Dimension(1024, 768));
	}
	
	private void createOrderItems(JPanel panel1)
	{
		
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
			PartStep latestPartStep = null;
			List<PartOrder> partOrders = order.getPartOrderList();
			
			// sweep
			Date lastSavedTime = null;
			for (PartStep partStep : partSteps)
			{
				if (lastSavedTime == null) {
					lastSavedTime = partStep.getStartDate();
					latestPartStep = partStep;
				}
				
				if (partStep.getStartDate().after(lastSavedTime)) {
					lastSavedTime = partStep.getStartDate();
					latestPartStep = partStep;
				}
			}
			
			Step currentStep = latestPartStep.getStep();
			
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
			
			
			JList list_1 = new JList();
			list_1.setModel(new AbstractListModel() {
				List<String> productNames = null;
				
				public List<String> getProductNames()
				{
					if (productNames == null) {
						productNames = new ArrayList<String>();
						
						int l = 1;
						for (PartOrder partOrder : partOrders) {
							Product product = partOrder.getProduct();
							productNames.add(l + ". " + product.getName());
							
							l++;
						}
					}
					
					return productNames;
				}
				
				public int getSize() {
					return getProductNames().size();
				}
				public Object getElementAt(int index) {
					return getProductNames().get(index);
				}
			});
			list_1.setBounds(0, 0, 185, 104);
			panel_4.add(list_1);
			
			JButton btnNewButton = new JButton("Se detaljer");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {		
					createDetails(panel_2, order.getId());
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
			
			JLabel lblTrinStTrin = new JLabel("Trin: " + currentStep.getName());
			lblTrinStTrin.setBounds(10, 136, 179, 14);
			panel_7.add(lblTrinStTrin);
			
			String orderId = Integer.toString(order.getId());
			
			JLabel lblNewLabel_3 = new JLabel(orderId);
			lblNewLabel_3.setBounds(69, 7, 122, 23);
			panel_7.add(lblNewLabel_3);
			
			i++;
		}		
		
	}
	
	
	private void createDetails(JPanel panel, int orderid){
				OrderInfoViewModel info = null;
				
				try {
					info = partstepCtr.findOrderInfo(orderid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(info != null){
				Order order = info.getOrder();
				Customer customer = order.getCustomer();
				Town town = customer.getTown();
						
				
				JPanel panel_3 = new JPanel();
				panel_3.setBounds(10, 11, 465, 653);
				panel.add(panel_3);
				panel_3.setLayout(null);
								
				
				JPanel panel_4 = new JPanel();
				panel_4.setBounds(260, 10, 195, 270);
				panel_3.add(panel_4);
				panel_4.setLayout(null);				
				
				List<PartOrder> partOrders = order.getPartOrderList();
				JList list_1 = new JList();
				list_1.setModel(new AbstractListModel() {
					List<String> productNames = null;
					
					public List<String> getProductNames()
					{
						if (productNames == null) {
							productNames = new ArrayList<String>();
							
							int l = 1;
							for (PartOrder partOrder : partOrders) {
								Product product = partOrder.getProduct();
								productNames.add(l + ". " + product.getName());
								
								l++;
							}
						}
						
						return productNames;
					}
					
					public int getSize() {
						return getProductNames().size();
					}
					public Object getElementAt(int index) {
						return getProductNames().get(index);
					}
				});
				list_1.setBounds(0, 0, 195, 270);
				panel_4.add(list_1);
				
				
				JLabel label_2 = new JLabel("Ordre nr:");
				label_2.setFont(new Font("Tahoma", Font.BOLD, 11));
				label_2.setBounds(10, 11, 61, 14);
				panel_3.add(label_2);
				
				
				JLabel label = new JLabel(customer.getName());
				label.setBounds(10, 36, 240, 14);
				panel_3.add(label);
				
				
				JLabel lblNewLabel_4 = new JLabel(customer.getStreet());
				lblNewLabel_4.setBounds(10, 61, 240, 14);
				panel_3.add(lblNewLabel_4);
				
				
				JLabel label_3 = new JLabel(town.getZip() + " " + town.getName());		
				label_3.setBounds(10, 86, 240, 14);
				panel_3.add(label_3);
				
				
				String orderId = Integer.toString(order.getId());		
				JLabel label_1 = new JLabel(orderId);
				label_1.setBounds(64, 10, 61, 14);
				panel_3.add(label_1);					
				
			}
	}




	private void setDetailsText(JPanel panel)
	{
		
	}


}


	


