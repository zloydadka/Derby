package main.java.com.github.view;

import java.util.HashMap;

import main.java.com.github.model.AbstractModel;

public class ViewContext extends HashMap<String,Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4355560277080978779L;
	
	public  AbstractModel makeModel(Class<? extends AbstractModel> clazz) {
		
		AbstractModel model = null;
		try {
			model = clazz.newInstance();
			model.update(this);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return model;
	}

}