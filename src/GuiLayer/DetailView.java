package GuiLayer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sun.security.x509.NetscapeCertTypeExtension;
import CtrLayer.PartStepCtr;
import ModelLayer.Customer;
import ModelLayer.Employee;
import ModelLayer.Order;
import ModelLayer.OrderInfoViewModel;
import ModelLayer.PartOrder;
import ModelLayer.PartStep;
import ModelLayer.Product;
import ModelLayer.Step;
import ModelLayer.Town;

public class DetailView extends JPanel {

	PartStepCtr partstepCtr = new PartStepCtr();
	private JList list_1 = new JList(); 
	private JLabel DetailOrdernre = new JLabel("Ordre nr:");
	private JLabel DetailCustomername = new JLabel("");
	private JLabel detailsStreet = new JLabel("");
	private JLabel detailstown = new JLabel("");
	private JLabel detailsorderId = new JLabel("");
	
	private JPanel pastSteps = new JPanel();
	private JLabel currentStepName = new JLabel("");
	private JPanel newSteps = new JPanel();
	private ArrayList<Employee> selectedEmployees;
	OrderInfoViewModel info = null;
	private JPanel stepsContainer = null;
	JPanel panel_3 = new JPanel();
	private PartStepUI partStepUI;
	
	public void showStepsContainer() {
		this.getstepsContainer().setVisible(true);
		this.getstepsContainer().revalidate();
		this.getstepsContainer().repaint();
	}
	
	public DetailView(JPanel panel, PartStepUI partStepUI){
		stepsContainer = new JPanel();
		this.partStepUI = partStepUI;
			panel_3.setBounds(10, 11, 465, 653);
			panel.add(panel_3);
			panel_3.setLayout(null);
							
			
			JPanel panel_4 = new JPanel();
			panel_4.setBounds(260, 10, 195, 270);
			panel_3.add(panel_4);
			panel_4.setLayout(null);				
						
			
			list_1.setBounds(0, 0, 195, 270);
			panel_4.add(list_1);			
			
			
			DetailOrdernre.setFont(new Font("Tahoma", Font.BOLD, 11));
			DetailOrdernre.setBounds(10, 11, 61, 14);
			panel_3.add(DetailOrdernre);			
			
			
			DetailCustomername.setBounds(10, 36, 240, 14);
			panel_3.add(DetailCustomername);			
			
			
			detailsStreet.setBounds(10, 61, 240, 14);
			panel_3.add(detailsStreet);			
			
					
			detailstown.setBounds(10, 86, 240, 14);
			panel_3.add(detailstown);				
				
			
			detailsorderId.setBounds(64, 10, 61, 14);
			panel_3.add(detailsorderId);
			
			
			stepsContainer.setBounds(10, 402, 465, 262);
			panel_3.add(stepsContainer);
			stepsContainer.setLayout(null);
			
			JLabel peterFaarSinVilje = new JLabel("Nuv?rende trin:");
			peterFaarSinVilje.setBounds(180, 0, 200, 100);
			stepsContainer.add(peterFaarSinVilje);
			
			currentStepName.setBounds(180, 15, 200, 100);
			currentStepName.setFont(new Font("Tahoma", Font.BOLD, 13));
			stepsContainer.add(currentStepName);
			
			
			
			pastSteps.setBounds(10, 11, 154, 240);
			stepsContainer.add(pastSteps);
			pastSteps.setLayout(null);
			
			newSteps.setBounds(301, 11, 154, 240);
			stepsContainer.add(newSteps);
			
		}
	
	
	
	public void setDetailsText(int orderid )
	{
		
		JPanel p = this;		try {
			
			info = partstepCtr.findOrderInfo(orderid);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(p, "Database fejl: " + e.getMessage(), "Fejl", JOptionPane.ERROR_MESSAGE);
		}
		if(info != null){
		final Order order = info.getOrder();
		Customer customer = order.getCustomer();
		Town town = customer.getTown();
		
		
		String orderId = Integer.toString(order.getId());	
		
		
		final List<PartOrder> partOrders = order.getPartOrderList();
		
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
		
		
			DetailCustomername.setText(customer.getName()); 
			detailsStreet.setText(customer.getStreet()); 
			detailstown.setText(town.getZip() + " " + town.getName()); 
			String orderIdd = Integer.toString(order.getId());
			detailsorderId.setText(orderIdd);
			
			currentStepName.setText(order.getPartStepList().get(0).getStep().getName());
			pastSteps.removeAll();
			final DetailView DT = this;
			final EmployeesView empView = new EmployeesView(info.getEmployees(),DT,order.getId());
			for(int i = 1; i < order.getPartStepList().size(); i++){
				final PartStep ps = order.getPartStepList().get(i);
				JButton btnNewButton_1 = new JButton(ps.getStep().getName());
				btnNewButton_1.setBounds(1, 40 * i, 153, 23);
				
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {	
						try {
							info = partstepCtr.findOrderInfo(order.getId());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
								
						panel_3.add(empView);
						stepsContainer.setVisible(false);
						empView.setVisible(true);
						Step selectedStep = ps.getStep();
						partstepCtr.setPartStep(selectedStep, order);
					}
				});
				pastSteps.add(btnNewButton_1);
			}
			pastSteps.repaint();
			pastSteps.validate();
			
			newSteps.removeAll();
			for(int i = 0; i < info.getSteps().size(); i++){
				final Step s = info.getSteps().get(i);
				JButton btnNewButton_1 = new JButton(s.getName());
				btnNewButton_1.setBounds(1, 40 * i, 153, 23);
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {	
						try {
							info = partstepCtr.findOrderInfo(order.getId());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						panel_3.add(empView);
						stepsContainer.setVisible(false);
						empView.setVisible(true);
						Step selectedStep = s;
						partstepCtr.setPartStep(selectedStep, order);
					}
				});
				newSteps.add(btnNewButton_1);
			}
			newSteps.repaint();
			newSteps.validate();
		}	
		
	}
	
	public JPanel getstepsContainer(){
		return stepsContainer;
	}
	
	public void repaintStepsContainer(){
		stepsContainer.setVisible(true);
		super.repaint();
		stepsContainer.validate();
		stepsContainer.repaint();
	}

	/**
	 * @return the selectedEmployees
	 */
	public ArrayList<Employee> getSelectedEmployees() {
		return selectedEmployees;
	}

	/**
	 * @param selectedEmployees the selectedEmployees to set
	 */
	public void setSelectedEmployees(ArrayList<Employee> selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}
	
	public void saveStep()
	{
		try {
			partstepCtr.associateEmployees(selectedEmployees);
			partstepCtr.finishStep();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Database fejl: " + e.getMessage(), "Fejl", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void refresh() {
		partStepUI.createOrderItems();
		
	}
}
