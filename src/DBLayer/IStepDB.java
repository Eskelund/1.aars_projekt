package DBLayer;

import java.util.List;

import ModelLayer.Step;

/**
 * 
 * @author Kim Dam Gr�nh�j
 * @version 15-05-2015
 *
 */
public interface IStepDB {
	List<Step> findNextSteps(int stepID) throws Exception;
}
