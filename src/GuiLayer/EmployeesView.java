package GuiLayer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import ModelLayer.Employee;

import javax.swing.JCheckBox;

public class EmployeesView extends JPanel {
	private ArrayList<Employee> employees;
	private ArrayList<Employee> selectedEmployees;
	
	public EmployeesView(List<Employee> list, final DetailView dtView, final int id) {
		this.employees = (ArrayList<Employee>) list;
		selectedEmployees = new ArrayList<Employee>();
		this.setBounds(10, 402, 465, 262);
		this.setVisible(false);
		setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(0, 0, 455, 211);
		add(panel_4);
		panel_4.setLayout(new MigLayout("", "[grow][grow]", "[grow][grow]"));
		
		
		
		int colIndex = 0;
		int rowIndex = 0;
		for(int i = 0; i < list.size(); i++)
		{	
			int m = i % 2;
			final Employee employee = list.get(i);
			JPanel panel = new JPanel();
			panel_4.add(panel, "cell " + colIndex + " " + rowIndex + ",grow");
			panel.setLayout(null);
			
			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setBounds(0, 0, 97, 97);
			
			String basePath = new File("images.jpg").getAbsolutePath();			
			lblNewLabel.setIcon(new ImageIcon(basePath));
			panel.add(lblNewLabel);
			
			JLabel lblFlotfyr = new JLabel(employee.getName());
			lblFlotfyr.setBounds(107, 11, 102, 75);
			panel.add(lblFlotfyr);
			
			final JCheckBox chckbxNewCheckBox = new JCheckBox("");
			chckbxNewCheckBox.setBounds(187, 11, 20, 20);
			panel.add(chckbxNewCheckBox, "cell 0 0");
			panel.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					chckbxNewCheckBox.setSelected(!chckbxNewCheckBox.isSelected());
					if(chckbxNewCheckBox.isSelected()){
						selectedEmployees.add(employee);
					}else{
						selectedEmployees.remove(employee);
					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			if(m == 1){
				rowIndex++;
			}
			if(colIndex == 0){
				colIndex = 1;
			}else{
				colIndex = 0;
			}
		}
		final EmployeesView EmpView = this;
		JButton BtnBack = new JButton("Back");
		BtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				EmpView.setVisible(false);
				dtView.showStepsContainer();
			}
		});
		BtnBack.setBounds(0, 222, 224, 23);
		add(BtnBack);
		
		JButton btnNewButton_1 = new JButton("Godkend");
		btnNewButton_1.setBounds(266, 222, 189, 23);
		btnNewButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dtView.setSelectedEmployees(selectedEmployees);
				dtView.saveStep();
				dtView.refresh();
			}
		});
		add(btnNewButton_1);
	}
}
