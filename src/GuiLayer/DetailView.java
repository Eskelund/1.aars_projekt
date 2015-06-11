package GuiLayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;




import sun.security.x509.NetscapeCertTypeExtension;
import CtrLayer.OrderCtr;
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

	private PartStepCtr partstepCtr = new PartStepCtr();
	private JList list_1;
	private JLabel DetailOrdernre;
	private JLabel DetailCustomername;
	private JLabel detailsStreet;
	private JLabel detailstown;
	private JLabel detailsorderId;
	
	private JPanel pastSteps = new JPanel();
	private JLabel currentStepName = new JLabel("");
	private JPanel newSteps = new JPanel();
	private ArrayList<Employee> selectedEmployees;
	private OrderInfoViewModel info = null;
	private JPanel stepsContainer = null;
	private JPanel panel_3;
	private PartStepUI partStepUI;
	private JPanel panel;
	private JPanel headertext = new JPanel();
	
	public DetailView(JPanel panel, PartStepUI partStepUI){
		
		this.panel = panel;
		this.partStepUI = partStepUI;
		
		initialize();
		}
	
	
	/**
	 * This method creates all the containers this class
	 */
	public void initialize(){
		
			panel.removeAll();
			pastSteps.removeAll();
			newSteps.removeAll();
			currentStepName.setText("");
			stepsContainer = new JPanel();
			
			panel_3 = new JPanel();
			panel_3.setBounds(10, 11, 465, 653);
			panel.add(panel_3);
			panel_3.setLayout(null);
							
			
			JPanel panel_4 = new JPanel();
			panel_4.setBounds(260, 10, 195, 270);
			panel_3.add(panel_4);
			panel_4.setLayout(null);				
						
			list_1 = new JList(); 
			list_1.setBounds(0, 0, 195, 270);
			panel_4.add(list_1);			
			
			DetailOrdernre = new JLabel("Ordre nr:");
			DetailOrdernre.setFont(new Font("Tahoma", Font.BOLD, 11));
			DetailOrdernre.setBounds(10, 11, 61, 14);
			panel_3.add(DetailOrdernre);			
			
			DetailCustomername = new JLabel("");
			DetailCustomername.setBounds(10, 36, 240, 14);
			panel_3.add(DetailCustomername);			
			
			detailsStreet = new JLabel("");
			detailsStreet.setBounds(10, 61, 240, 14);
			panel_3.add(detailsStreet);			
			
			detailstown = new JLabel("");		
			detailstown.setBounds(10, 86, 240, 14);
			panel_3.add(detailstown);				
				
			detailsorderId = new JLabel("");
			detailsorderId.setBounds(64, 10, 61, 14);
			panel_3.add(detailsorderId);
			
			
			stepsContainer.setBounds(10, 402, 465, 262);
			panel_3.add(stepsContainer);
			stepsContainer.setLayout(null);
						
			currentStepName.setBounds(180, 15, 200, 100);
			currentStepName.setFont(new Font("Tahoma", Font.BOLD, 13));
			stepsContainer.add(currentStepName);
			
			
			
			pastSteps.setBounds(0, 11, 180, 240);
			stepsContainer.add(pastSteps);
			//pastSteps.setBorder(BorderFactory.createLineBorder(Color.black));
			pastSteps.setLayout(null);
			
			newSteps.setBounds(250, 11, 200, 240);
			newSteps.setLayout(null);
			
			//newSteps.setBorder(BorderFactory.createLineBorder(Color.black));
			stepsContainer.add(newSteps);
			
			panel.validate();
			panel.repaint();
			
		
	}
	
	
	/**
	 * this method sets all the info in the containers 
	 * @param orderid
	 */
	public void setDetailsText(final int orderid )
	{
		
		JPanel p = this;		
		try {
			
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
			
			pastSteps.removeAll();			
			int count = 0;
			JLabel trin = new JLabel("Nuvaarende trin");
			trin.setBounds(1, 0, 100, 23);
			trin.setFont(new Font("Tahoma", Font.BOLD, 12));
			pastSteps.add(trin);
			final DetailView DT = this;
			final EmployeesView empView = new EmployeesView(info.getEmployees(),DT,order.getId());
			for(int i = 0; i < order.getPartStepList().size(); i++){
				final PartStep ps = order.getPartStepList().get(i);	
				JButton btnNewButton_1 = new JButton(ps.getStep().getName());
				if(i == 0){
					btnNewButton_1.setBounds(1, 30, 179, 23);			
					btnNewButton_1.setEnabled(false);
					btnNewButton_1.setBackground(new Color(97,144,64));
					pastSteps.add(btnNewButton_1);
					JLabel trin1 = new JLabel("tidligere trin");
					trin1.setBounds(1, 60, 100, 23);
					pastSteps.add(trin1);
				}else{							
					btnNewButton_1.setBounds(1, (30 * i)+60, 179, 23);	
					btnNewButton_1.setBackground(new Color(181,213,138));
					btnNewButton_1.setEnabled(false);
					pastSteps.add(btnNewButton_1);
				}
				
				count++;
			}
			
			final JPanel f = this;
			JButton btnNewButton_2 = new JButton("Tilbage");
			btnNewButton_2.setBounds(1, 200, 179, 40);
			if(count == 1){
				btnNewButton_2.setEnabled(false);
			}
			btnNewButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {	
					try {
						OrderCtr or = new OrderCtr();
							or.deletePartSteps(order.getPartStepList().get(0).getId());
							refresh();
							setDetailsText(orderid);
							
							
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(f, "Database fejl: " + e.getMessage(), "Fejl", JOptionPane.ERROR_MESSAGE);
					}								
				}
			});
			pastSteps.add(btnNewButton_2);		
			pastSteps.repaint();
			pastSteps.validate();
			
			
			
			
			newSteps.removeAll();			
			JLabel trin2 = new JLabel("mulige trin");
			trin2.setBounds(1, 0, 199, 23);
			trin2.setFont(new Font("Tahoma", Font.BOLD, 12));
			newSteps.add(trin2);
			
			for(int i = 0; i < info.getSteps().size(); i++){
				final Step s = info.getSteps().get(i);						
				JButton btnNewButton_1 = new JButton(s.getName());
				btnNewButton_1.setBounds(1, (50 * i)+30, 198, 40);
				btnNewButton_1.setBackground(new Color(97,144,64));
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {	
						panel_3.add(empView);
						stepsContainer.setVisible(false);
						empView.setVisible(true);
						Step selectedStep = s;
						partstepCtr.setPartStep(selectedStep, order);
					}
				});
				
				
				btnNewButton_1.getPreferredSize();
				newSteps.add(btnNewButton_1);
			}
			newSteps.repaint();
			newSteps.validate();
		}	
		
	}
	
	
	
	
	/**
	 * returns stepsContainer witch is the container with all the steps
	 * @return
	 */
	public JPanel getstepsContainer(){
		return stepsContainer;
	}
	
	/**
	 * Updates the stepsContainer
	 */
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
	
	
	/**
	 * Calls the function on partstepCtr to save the new partstep
	 */
	public void saveStep()
	{
		try {
			partstepCtr.associateEmployees(selectedEmployees);
			partstepCtr.savePartStep();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Database fejl: " + e.getMessage(), "Fejl", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * updates this panel
	 */
	public void refresh() {
		partStepUI.createOrderItems();
		initialize();
		
	}
	
	/**
	 * showStepsContainers the StepsContainer.
	 */
	public void showStepsContainer() {
		this.getstepsContainer().setVisible(true);
		this.getstepsContainer().revalidate();
		this.getstepsContainer().repaint();
	}
}
