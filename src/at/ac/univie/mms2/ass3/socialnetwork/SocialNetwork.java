package at.ac.univie.mms2.ass2.socialnetwork;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public enum SocialNetwork {
	instance;
	private Model model = ModelFactory.createDefaultModel();
	public Model getModel() {return model;}
}
