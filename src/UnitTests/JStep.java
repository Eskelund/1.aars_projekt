package UnitTests;

import static org.junit.Assert.*;

import java.awt.List;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import CtrLayer.StepCtr;
import DBLayer.IStepDB;
import DBLayer.StepDB;
import ModelLayer.Step;

/**
 * 
 * @author Kim Dam Gr�nh�j
 *
 */
public class JStep {

	/**
	 * This is a positive test, testing getting next steps for a step by ID
	 * @throws SQLException SQL-server can fail
	 */
	@Test
	public void positiveTest_FindOneOrMore() throws SQLException {
		StepCtr sctr = new StepCtr();
		ArrayList<Step> steps = (ArrayList<Step>)sctr.findNextSteps(2);
		
		Boolean foundStepItem = false;
		// Print steps in console
		for (Step s : steps) {
			if (s.getName().equals("Pizza skal hentets")) {
				foundStepItem = true;
			}
		}
		
		assertTrue("item is not found", foundStepItem);
		// Validate there is one or more items
		assertTrue("Should contain one or more Step", steps.size() > 0);
	}
	
	/**
	 * This is a negative test, testing no steps is returned with step id 2222
	 * @throws SQLException SQL-server can fail
	 */
	@Test
	public void negativeTest_NoSteps() throws SQLException {
		StepCtr sctr = new StepCtr();
		ArrayList<Step> steps = (ArrayList<Step>)sctr.findNextSteps(2222);
		
		// Print steps in console
		for (Step s : steps) {
			System.out.println(s.getName());
		}
		
		// Validate there is one or more items
		assertTrue("Should not contain one or more Step", steps.size() == 0);
	}
	
	/**
	 * This is a negative test, testing no steps is returned by inserting IDs under 0
	 * @throws SQLException SQL-server can fail
	 */
	@Test(expected=IllegalArgumentException.class)
	public void negativeTest_NoSteps_WithNegativeNumber() throws SQLException {
		StepCtr sctr = new StepCtr();
		ArrayList<Step> steps = (ArrayList<Step>)sctr.findNextSteps(-1);
	}
	
	/**
	 * This is a negative test, testing no step is returned by ID 0
	 * @throws SQLException SQL-server can fail
	 */
	@Test(expected=IllegalArgumentException.class)
	public void negativeTest_NoSteps_WithZero() throws SQLException {
		StepCtr sctr = new StepCtr();
		ArrayList<Step> steps = (ArrayList<Step>)sctr.findNextSteps(0);
	}
}
