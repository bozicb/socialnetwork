package at.ac.univie.mms2.ass2.socialnetwork;

import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;

import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

@Path("social_network")
public class SocialNetworkResource {
	private final String sm = "http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#";
	private final String foaf = "http://xmlns.com/foaf/0.1/";
	private final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private final String maont = "http://www.w3.org/ns/ma-ont#";
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String social_media() {
		return "<html>" +
				"	<title>Social Media</title>" +
				"	<body>" +
				"		<h1>Social Media</h1>" +
				"		<ul>" +
				"			<li><a href=\"http://localhost:8081/socialnetwork/social_network/persons\">Persons</a></li>" +
				"			<li><a href=\"http://localhost:8081/socialnetwork/social_network/media\">Media</a></li>" +
				"			<li><a href=\"http://localhost:8081/socialnetwork/social_network/comments\">Comments</a></li>" +
				"		</ul>" +
				"	</body>" +
				"</html>";
	}
	
	@PUT
	@Path("add_graph")
	@Consumes(MediaType.TEXT_XML)
	public void add_graph(String graph) {
		try {
			new URL(graph);
			SocialNetwork.instance.getModel().read(graph);
		}
		catch(MalformedURLException mue) {
			InputStream in = FileManager.get().open(graph);
			SocialNetwork.instance.getModel().read(in, null);
		}
	}
	
	@GET
	@Path("persons")
	@Produces(MediaType.TEXT_XML)
	public String list_persons() {
		String queryString = ""
                + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "SELECT ?person "
                + "WHERE { "
                + "?person rdf:type foaf:Person ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<?xml version=\"1.0\"?>\n<Persons>\n";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<Person>" + solution.getResource("?person").getLocalName() + "</Person>\n";
        }  
        result += "</Persons>";
        return result;
	}
	
	@GET
	@Path("persons")
	@Produces(MediaType.TEXT_HTML)
	public String list_persons_html() {
		String queryString = ""
                + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "SELECT ?person "
                + "WHERE { "
                + "?person rdf:type foaf:Person ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<html><title>List of Persons HTML</title><body><h1>List of Persons</h1><ul>";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/persons/" + solution.getResource("?person").getLocalName() + "\">" + solution.getResource("?person").getLocalName() + "</a></li>";
        }
        result += "</ul></body></html>";
		return result;
	}
	
	@GET
	@Path("persons/{person}")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String get_person(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " 
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "SELECT ?name ?activity ?location ?age "
				+ "WHERE {"
				+ "sm:" + person + " foaf:name ?name. "
				+ "sm:" + person + " sm:hasActivity ?activity ."
				+ "sm:" + person + " sm:location ?location ."
				+ "sm:" + person + " foaf:age ?age ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<?xml version=\"1.0\"?>\n";
		result += "<Person>\n";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<Name>" + solution.getLiteral("?name").getString() + "</Name>\n";
			result += "<Activity>" + solution.getResource("?activity").getLocalName() + "</Activity>\n";
			result += "<Location>" + solution.getResource("?location").getLocalName() + "</Location>\n";
			result += "<Age>" + solution.getLiteral("?age").getString() + "</Age>\n";
		}
		result += "</Person>";
		return result;
	}
	
	@GET
	@Path("persons/{person}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	public String get_person_html(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " 
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "SELECT ?name ?activity ?location ?age "
				+ "WHERE {"
				+ "sm:" + person + " foaf:name ?name. "
				+ "sm:" + person + " sm:hasActivity ?activity ."
				+ "sm:" + person + " sm:location ?location ."
				+ "sm:" + person + " foaf:age ?age ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<html><title>" + person + "</title>" +
				"<script type=\"text/javascript\">" +
				"var req;" +
				"function handover() {" +
				"query = document.forms.formular.elements.query.value;" +
				"req = new XMLHttpRequest();" +
				"req.onreadystatechange = processResults;" +
				"req.open(\"GET\", \"http://localhost:8081/socialnetwork/social_network/retrieve_person/\" + query);" +
				"req.setRequestHeader(\"Content-Type\", \"application/json\");" +
				"req.send(null);" +
				"}" +
				"function processResults() {" +
				"if(req.readyState == 4) {" +
				"var data = req.responseText;" +
				"document.writeln(\"<h2>Potential Friends Found</h2>\");" +
				"document.writeln(\"<ul>\");" +
				"document.writeln(\"<li>\");" +
				"var link = \"http://localhost:8081/socialnetwork/social_network/persons/\";" +
				"var ref = link + data;" +
				"document.writeln(\"<a href=\" + ref + \">\" + data + \"</a>\");" +
				"document.writeln(\"</li>\");" +
				"document.writeln(\"</ul>\");" +
				"}" +
				"}" +
				"function remove() {" +
				"req = new XMLHttpRequest();" +
				"req.onreadystatechange = processRemove;" +
				"req.open(\"DELETE\", \"http://localhost:8081/socialnetwork/social_network/delete_person_link/Bob/" + person + "\");" +
				"req.setRequestHeader(\"Content-Type\", \"application/json\");" +
				"req.send(null);" +
				"}" +
				"function add() {" +
				"req = new XMLHttpRequest();" +
				"req.onreadystatechange = processAdd;" +
				"req.open(\"PUT\", \"http://localhost:8081/socialnetwork/social_network/create_person_link/Bob/" + person + "\");" +
				"req.setRequestHeader(\"Content-Type\", \"application/json\");" +
				"req.send(null);" +
				"}" +
				"function processRemove() {" +
				"if(req.readyState == 4) {" +
				"document.writeln(\"<p>Person deleted from list of friends!</p>\");" +
				"}" +
				"}" +
				"function processAdd() {" +
				"if(req.readyState == 4) {" +
				"document.writeln(\"<p>Person added to list of friends!</p>\");" +
				"}" +
				"}" +
				"</script>" +
				"<body><h1>" + person + "</h1><ul>";
		result += "<Person>\n";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<li><b>Name:</b> " + solution.getLiteral("?name").getString() + "</li>";
			result += "<li><b>Activity:</b> " + solution.getResource("?activity").getLocalName() + "</li>";
			result += "<li><b>Location:</b> " + solution.getResource("?location").getLocalName() + "</li>";
			result += "<li><b>Age:</b> " + solution.getLiteral("?age").getString() + "</li>";
			result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/list_friends/" + person + "\">Friends</a></li>";
			result += "<ul>";
			for(String p : this.list_friends_of_person(person)) {
				result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/persons/" + p + "\">" + p + "</a></li>";
			}
			result += "</ul>";
			result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/likes/person/" + person + "\">Likes</a></li>";
			result += "<ul>";
			for(String l : this.get_likes_of_person(person)) {
				result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/media/" + l + "\">" + l + "</a></li>";
			}
			result += "</ul>";
			result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/media/person/" + person + "\">Uploads</a></li>";
			result += "<ul>";
			for(String m : this.get_media_of_person(person)) {
				result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/media/" + m + "\">" + m + "</a></li>";
			}
			result += "</ul>";
			result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/comments/person/" + person + "\">Comments</a></li>";
			result += "<ul>";
			for(String c : this.get_comments_of_person(person)) {
				result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/comments/" + c + "\">" + c + "</a></li>";
			}
			result += "</ul>";
		}
		result += "</ul>";
		if(person.equals("Bob")) {}
		else if(this.list_friends_of_person("Bob").contains(person)) {
			result += "<p><input id=\"remove\" type=\"button\" value=\"Remove friend\" onclick=\"remove()\"/></p>";
		}
		else {
			result += "<p><input id=\"add\" type=\"button\" value=\"Add friend\" onclick=\"add()\"/></p>";
		}
		result += "Search" +
				"<form name=\"formular\" action=\"\" onsubmit=\"handover();\"> " +
				"<input id=\"query\" type=\"text\"/>" +
				"<input type=\"submit\" value=\"Submit\"/>" +
				"</form>" +
				"</body></html>";
		return result;
	}
	
	@PUT
	@Path("create_person/{name}/{activity}/{location}/{age}")
	@Consumes(MediaType.TEXT_XML)
	public void create_person(@PathParam("name") String name, @PathParam("activity") String activity, @PathParam("location") String location, @PathParam("age") String age) {
		Resource person = SocialNetwork.instance.getModel().createResource(sm+name);
		Property type = SocialNetwork.instance.getModel().getProperty(rdf+"type");
		Resource p = SocialNetwork.instance.getModel().getResource(foaf+"Person");
		SocialNetwork.instance.getModel().add(person, type, p);
		
		Property hasName = SocialNetwork.instance.getModel().getProperty(foaf+"name");
		SocialNetwork.instance.getModel().add(person, hasName, name);
		
		Property hasActivity = SocialNetwork.instance.getModel().getProperty(sm+"hasActivity");
		Resource a = SocialNetwork.instance.getModel().createResource(sm+activity);
		SocialNetwork.instance.getModel().add(person, hasActivity, a);
		
		Resource act = SocialNetwork.instance.getModel().getResource(sm+"Activity");
		SocialNetwork.instance.getModel().add(a, type, act);
		
		Property hasLocation = SocialNetwork.instance.getModel().getProperty(sm+"location");
		Resource l = SocialNetwork.instance.getModel().createResource(sm+location);
		SocialNetwork.instance.getModel().add(person, hasLocation, l);
		
		Resource loc = SocialNetwork.instance.getModel().getResource(maont+"Location");
		SocialNetwork.instance.getModel().add(l, type, loc);
		
		Property hasAge = SocialNetwork.instance.getModel().getProperty(foaf+"age");
		SocialNetwork.instance.getModel().add(person, hasAge, age);
	}
	
	@POST
	@Path("update_person/{person}/{old_value}/{new_value}/{property}")
	@Consumes(MediaType.TEXT_XML)
	public void update_person(@PathParam("person") String name, @PathParam("old_value") String old_value, @PathParam("new_value") String new_value, @PathParam("property") String property) {
		Resource person = SocialNetwork.instance.getModel().getResource(sm+name);
		if(property.equals("name") || property.equals("age")) {
			Property prop = SocialNetwork.instance.getModel().getProperty(foaf+property);
			NodeIterator iter = SocialNetwork.instance.getModel().listObjectsOfProperty(person, prop);
			RDFNode o = iter.next();
			SocialNetwork.instance.getModel().remove(person, prop, o);
			Literal new_o = SocialNetwork.instance.getModel().createLiteral(new_value);
			SocialNetwork.instance.getModel().add(person, prop, new_o);
		}
		else if(property.equals("hasActivity") || property.equals("location")) {
			Property prop = SocialNetwork.instance.getModel().getProperty(sm+property);
			Resource o = SocialNetwork.instance.getModel().getResource(sm+old_value);
			SocialNetwork.instance.getModel().remove(person, prop, o);
			Resource new_o = SocialNetwork.instance.getModel().createResource(sm+new_value);
			SocialNetwork.instance.getModel().add(person, prop, new_o);
		}
	}
	
	@DELETE
	@Path("persons/{person}")
	@Consumes(MediaType.TEXT_XML)
	public void delete_person(@PathParam("person") String name) {
		Resource person = SocialNetwork.instance.getModel().getResource(sm+name);
		SocialNetwork.instance.getModel().removeAll(person, null, null);
	}	
	
	@GET
	@Path("comments")
	@Produces(MediaType.TEXT_XML)
	public String list_comments() {
		String queryString = ""
                + "PREFIX sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "SELECT ?comment "
                + "WHERE { "
                + "?comment rdf:type sm:Comment ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<?xml version=\"1.0\"?>\n<Comments>\n";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<Comment>" + solution.getResource("?comment").getLocalName() + "</Comment>\n";
        }  
        result += "</Comments>";
        return result;
	}
	
	@GET
	@Path("comments")
	@Produces(MediaType.TEXT_HTML)
	public String list_comments_html() {
		String queryString = ""
                + "PREFIX sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "SELECT ?comment "
                + "WHERE { "
                + "?comment rdf:type sm:Comment ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<html><title>Comments</title><body><h1>Comments</h1><ul>";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/comments/" + solution.getResource("?comment").getLocalName() + "\">" + solution.getResource("?comment").getLocalName() + "</a></li>";
        }  
        result += "</ul></body></html>";
        return result;
	}
	
	@GET
	@Path("comments/{comment}")
	@Produces(MediaType.TEXT_HTML)
	public String get_comment_html(@PathParam("comment") String comment) {
		String queryString = ""
                + "PREFIX sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "SELECT ?comment ?text "
                + "WHERE { "
                + "?comment sm:text ?text ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
		String result = "<html><title>" + comment + "</title><body><h1>" + comment + "</h1><ul>";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<li><b>Text:</b> " + solution.getLiteral("?text").getString() + "</li>";
		}
		result += "</ul></body></html>";
		return result;
	}
	
	@GET
	@Path("comments/person/{person}")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String get_person_comments(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "SELECT ?name ?text "
				+ "WHERE {"
				+ "sm:" + person + " sm:write ?name. "
				+ "?name sm:text ?text ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<?xml version=\"1.0\"?>\n";
		result += "<Comment>\n";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<Name>" + solution.getResource("?name").getLocalName() + "</Name>\n";
			result += "<Text>" + solution.getLiteral("?text").getString() + "</Text>\n";
		}
		result += "</Comment>";
		return result;
	}
	
	@GET
	@Path("comments/person/{person}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	public String get_person_comments_html(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "SELECT ?name ?text "
				+ "WHERE {"
				+ "sm:" + person + " sm:write ?name. "
				+ "?name sm:text ?text ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<html><title>" + person + "'s Comments</title><body><h1>" + person + "'s Comments</h1><ul>";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<li><b>Name:</b> " + solution.getResource("?name").getLocalName() + "</li>";
			result += "<li><b>Text:</b> " + solution.getLiteral("?text").getString() + "</li>";
		}
		result += "</ul></body></html>";
		return result;
	}
	
	public ArrayList<String> get_comments_of_person(String person) {
		ArrayList<String> comments = new ArrayList<String>();
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "SELECT ?name ?text "
				+ "WHERE {"
				+ "sm:" + person + " sm:write ?name. "
				+ "?name sm:text ?text ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			comments.add(solution.getResource("?name").getLocalName());
		}
		return comments;
	}
	
	@GET
	@Path("comments/media/{media}")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String get_media_comments(@PathParam("media") String media) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "SELECT ?name ?text "
				+ "WHERE {"
				+ "sm:" + media + " sm:hasComment ?name. "
				+ "?name sm:text ?text ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<?xml version=\"1.0\"?>\n";
		result += "<Comment>\n";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<Name>" + solution.getResource("?name").getLocalName() + "</Name>\n";
			result += "<Text>" + solution.getLiteral("?text").getString() + "</Text>\n";
		}
		result += "</Comment>";
		return result;
	}
	
	@GET
	@Path("comments/media/{media}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	public String get_media_comments_html(@PathParam("media") String media) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "SELECT ?name ?text "
				+ "WHERE {"
				+ "sm:" + media + " sm:hasComment ?name. "
				+ "?name sm:text ?text ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<html><title>Comments for " + media + "</title><body><h1>Comments for " + media + "</h1><ul>";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<li><b>Name:</b> " + solution.getResource("?name").getLocalName() + "</li>";
			result += "<li><b>Text:</b> " + solution.getLiteral("?text").getString() + "</li>";
		}
		result += "</ul></body></html>";
		return result;
	}
	
	@PUT
	@Path("create_comment/{name}/{text}/{person}/{media}")
	@Consumes(MediaType.TEXT_XML)
	public void create_comment(@PathParam("name") String name, @PathParam("text") String text, @PathParam("person") String person, @PathParam("media") String media) {
		Resource comment = SocialNetwork.instance.getModel().createResource(sm+name);
		Property type = SocialNetwork.instance.getModel().getProperty(rdf+"type");
		Resource c = SocialNetwork.instance.getModel().getResource(sm+"Comment");
		SocialNetwork.instance.getModel().add(comment, type, c);
		
		Property txt = SocialNetwork.instance.getModel().getProperty(sm+"text");
		SocialNetwork.instance.getModel().add(comment, txt, text);
		
		Resource p = SocialNetwork.instance.getModel().getResource(sm+person);
		Property write = SocialNetwork.instance.getModel().getProperty(sm+"write");
		SocialNetwork.instance.getModel().add(p, write, comment);

		Resource m = SocialNetwork.instance.getModel().getResource(sm+media);
		Property hasComment = SocialNetwork.instance.getModel().createProperty(sm+"hasComment");
		SocialNetwork.instance.getModel().add(m, hasComment, comment);
	}

	@POST
	@Path("update_comment/{comment}/{old_value}/{new_value}/{property}")
	@Consumes(MediaType.TEXT_XML)
	public void update_comment(@PathParam("comment") String name, @PathParam("old_value") String old_value, @PathParam("new_value") String new_value, @PathParam("property") String property) {
		Resource comment = SocialNetwork.instance.getModel().getResource(sm+name);
		if(property.equals("text")) {
			Property prop = SocialNetwork.instance.getModel().getProperty(sm+property);
			NodeIterator iter = SocialNetwork.instance.getModel().listObjectsOfProperty(comment, prop);
			RDFNode o = iter.next();
			SocialNetwork.instance.getModel().remove(comment, prop, o);
			Literal new_o = SocialNetwork.instance.getModel().createLiteral(new_value);
			SocialNetwork.instance.getModel().add(comment, prop, new_o);
		}
	}
	
	@DELETE
	@Path("delete_comment/{comment}")
	@Consumes(MediaType.TEXT_XML)
	public void delete_comment(@PathParam("comment") String name) {
		Resource comment = SocialNetwork.instance.getModel().getResource(sm+name);
		SocialNetwork.instance.getModel().removeAll(comment, null, null);
	}
	
	@GET
	@Path("media")
	@Produces(MediaType.TEXT_XML)
	public String list_media() {
		String queryString = ""
                + "PREFIX maont: <http://www.w3.org/ns/ma-ont#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?media "
                + "WHERE { "
                + "?media rdf:type maont:MediaResource ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<?xml version=\"1.0\"?>\n<Media>\n";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<MediaObject>" + solution.getResource("?media").getLocalName() + "</MediaObject>\n";
        }  
        result += "</Media>";
        return result;
	}
	
	@GET
	@Path("media")
	@Produces(MediaType.TEXT_HTML)
	public String list_media_html() {
		String queryString = ""
                + "PREFIX maont: <http://www.w3.org/ns/ma-ont#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?media "
                + "WHERE { "
                + "?media rdf:type maont:MediaResource ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<html><title>Media</title><body><h1>Media</h1><ul>";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/media/" + solution.getResource("?media").getLocalName() + "\">" + solution.getResource("?media").getLocalName() + "</a></li>";
        }  
        result += "</ul></body></html>";
        return result;
	}
	
	@GET
	@Path("media/person/{person}")
	@Produces(MediaType.TEXT_XML)
	public String get_person_media(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?media "
                + "WHERE { "
                + "sm:" + person + " sm:upload ?media ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<?xml version=\"1.0\"?>\n<Media>\n";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<MediaObject>" + solution.getResource("?media").getLocalName() + "</MediaObject>\n";
        }  
        result += "</Media>";
        return result;
	}
	
	@GET
	@Path("media/person/{person}")
	@Produces(MediaType.TEXT_HTML)
	public String get_person_media_html(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?media "
                + "WHERE { "
                + "sm:" + person + " sm:upload ?media ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<html><title>" + person + "'s Uploads</title><body><h1>" + person + "'s Uploads</h1><ul>";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/media/" + solution.getResource("?media").getLocalName() + "\">" + solution.getResource("?media").getLocalName() + "</a></li>";
        }  
        result += "</ul></body></html>";
        return result;
	}
	
	public ArrayList<String> get_media_of_person(String person) {
		ArrayList<String> media = new ArrayList<String>();
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?media "
                + "WHERE { "
                + "sm:" + person + " sm:upload ?media ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        while(results.hasNext()) {
        	QuerySolution solution = results.nextSolution();
        	media.add(solution.getResource("?media").getLocalName());
        }
		return media;
	}
	
	@GET
	@Path("media/{media}")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String get_media(@PathParam("media") String media) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "PREFIX maont: <http://www.w3.org/ns/ma-ont#> "
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "SELECT ?date ?title "
				+ "WHERE {"
				+ "sm:" + media + " maont:creationDate ?date . "
				+ "sm:" + media + " foaf:title ?title ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<?xml version=\"1.0\"?>\n";
		result += "<Media>\n";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<Name>" + media + "</Name>\n";
			result += "<CreationDate>" + solution.getLiteral("?date").getString() + "</CreationDate>\n";
			result += "<Title>" + solution.getLiteral("?title").getString() + "</Title>\n";
		}
		result += "</Media>";
		return result;
	}
	
	@GET
	@Path("media/{media}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	public String get_media_html(@PathParam("media") String media) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
				+ "PREFIX maont: <http://www.w3.org/ns/ma-ont#> "
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "SELECT ?date ?title "
				+ "WHERE {"
				+ "sm:" + media + " maont:creationDate ?date . "
				+ "sm:" + media + " foaf:title ?title ."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
		ResultSet results = qexec.execSelect();
		String result = "<html><title>" + media + "</title><body><h1>" + media + "</h1><ul>";
		result += "<Media>\n";
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			result += "<li><b>Name:</b> " + media + "</li>";
			result += "<li><b>Creation Date:</b> " + solution.getLiteral("?date").getString() + "</li>";
			result += "<li><b>Title:</b> " + solution.getLiteral("?title").getString() + "</li>";
			result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/comments/media/" + media + "\">Comments</a></li>";
		}
		result += "</ul></body></html>";
		return result;
	}
	
	@PUT
	@Path("create_media/{name}/{title}")
	@Consumes(MediaType.TEXT_XML)
	public void create_media(@PathParam("name") String name, @PathParam("title") String title) {
		Resource media = SocialNetwork.instance.getModel().createResource(sm+name);
		Property type = SocialNetwork.instance.getModel().getProperty(rdf+"type");
		Resource m = SocialNetwork.instance.getModel().getResource(maont+"MediaResource");
		SocialNetwork.instance.getModel().add(media, type, m);
		
		Property t = SocialNetwork.instance.getModel().getProperty(sm+"title");
		SocialNetwork.instance.getModel().add(media, t, title);
	}
	
	@POST
	@Path("update_media/{media}/{old_value}/{new_value}/{property}")
	@Consumes(MediaType.TEXT_XML)
	public void update_media(@PathParam("media") String name, @PathParam("old_value") String old_value, @PathParam("new_value") String new_value, @PathParam("property") String property) {
		Resource media = SocialNetwork.instance.getModel().getResource(sm+name);
		if(property.equals("creationDate")) {
			Property prop = SocialNetwork.instance.getModel().getProperty(maont+property);
			NodeIterator iter = SocialNetwork.instance.getModel().listObjectsOfProperty(media, prop);
			RDFNode o = iter.next();
			SocialNetwork.instance.getModel().remove(media, prop, o);
			Literal new_o = SocialNetwork.instance.getModel().createLiteral(new_value);
			SocialNetwork.instance.getModel().add(media, prop, new_o);
		}
		else if(property.equals("title")) {
			Property prop = SocialNetwork.instance.getModel().getProperty(foaf+property);
			NodeIterator iter = SocialNetwork.instance.getModel().listObjectsOfProperty(media, prop);
			RDFNode o = iter.next();
			SocialNetwork.instance.getModel().remove(media, prop, o);
			Literal new_o = SocialNetwork.instance.getModel().createLiteral(new_value);
			SocialNetwork.instance.getModel().add(media, prop, new_o);
		}
	}
	
	@DELETE
	@Path("delete_media/{media}")
	@Consumes(MediaType.TEXT_XML)
	public void delete_media(@PathParam("media") String name) {
		Resource media = SocialNetwork.instance.getModel().getResource(sm+name);
		SocialNetwork.instance.getModel().removeAll(media, null, null);
	}
	
	@GET
	@Path("likes/person/{person}")
	@Produces(MediaType.TEXT_XML)
	public String get_person_likes(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?like "
                + "WHERE { "
                + "sm:" + person + " sm:like ?like ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<?xml version=\"1.0\"?>\n<Person>\n";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<likes>" + solution.getResource("?like").getLocalName() + "</likes>\n";
        }  
        result += "</Person>";
        return result;
	}
	
	@GET
	@Path("likes/person/{person}")
	@Produces(MediaType.TEXT_HTML)
	public String get_person_likes_html(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?like "
                + "WHERE { "
                + "sm:" + person + " sm:like ?like ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<html><title>" + person + "'s Likes</title><body><h1>" + person + "'s Likes</h1><ul>";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/media/" + solution.getResource("?like").getLocalName() + "\">" + solution.getResource("?like").getLocalName() + "</a></li>";
        }  
        result += "</ul></body></html>";
        return result;
	}
	
	public ArrayList<String> get_likes_of_person(String person) {
		ArrayList<String> likes = new ArrayList<String>();
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?like "
                + "WHERE { "
                + "sm:" + person + " sm:like ?like ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        while(results.hasNext()) {
        	QuerySolution solution = results.nextSolution();
        	likes.add(solution.getResource("?like").getLocalName());
        }
		return likes;
	}
	
	@PUT
	@Path("create_like/{person}/{object}")
	@Consumes(MediaType.TEXT_XML)
	public void create_like(@PathParam("person") String person, @PathParam("object") String object) {
		Resource p = SocialNetwork.instance.getModel().getResource(sm+person);
		Property like = SocialNetwork.instance.getModel().getProperty(sm+"like");
		Resource o = SocialNetwork.instance.getModel().getResource(sm+object);
		SocialNetwork.instance.getModel().add(p, like, o);
	}
	
	@DELETE
	@Path("delete_like/{person}/{object}")
	@Consumes(MediaType.TEXT_XML)
	public void delete_like(@PathParam("person") String person, @PathParam("object") String object) {
		Resource s = SocialNetwork.instance.getModel().getResource(sm+person);
		Property p = SocialNetwork.instance.getModel().getProperty(sm+"like");
		Resource o = SocialNetwork.instance.getModel().getResource(sm+object);
		SocialNetwork.instance.getModel().remove(s, p, o);
	}
	
	@GET
	@Path("list_friends/{person}")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String list_friends(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?friend "
                + "WHERE { "
                + "sm:" + person + " sm:friend ?friend ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<?xml version=\"1.0\"?>\n<Friends>\n";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<Friend>" + solution.getResource("?friend").getLocalName() + "</Friend>\n";
        }  
        result += "</Friends>";
        return result;
	}
	
	@GET
	@Path("list_friends/{person}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	public String list_friends_html(@PathParam("person") String person) {
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?friend "
                + "WHERE { "
                + "sm:" + person + " sm:friend ?friend ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "<html><title>" + person + "'s Friends</title><body><h1>" + person + "'s Friends</h1><ul>";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            result += "<li><a href=\"http://localhost:8081/socialnetwork/social_network/persons/" + solution.getResource("?friend").getLocalName() + "\">" + solution.getResource("?friend").getLocalName() + "</a></li>";
        }  
        result += "</ul></body></html>";
        return result;
	}
	
	public ArrayList<String> list_friends_of_person(String person) {
		ArrayList<String> friends = new ArrayList<String>();
		String queryString = ""
				+ "PREFIX  sm: <http://www.semanticweb.org/bozicb/ontologies/2013/2/social-media#> "
                + "SELECT ?friend "
                + "WHERE { "
                + "sm:" + person + " sm:friend ?friend ."
                + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        while(results.hasNext()) {
        	QuerySolution solution = results.nextSolution();
        	friends.add(solution.getResource("?friend").getLocalName());
        }
		return friends;
	}
	
	@PUT
	@Path("create_person_link/{person1}/{person2}")
	@Consumes(MediaType.TEXT_XML)
	public void create_person_link(@PathParam("person1") String person1, @PathParam("person2") String person2) {
		Resource s = SocialNetwork.instance.getModel().getResource(sm+person1);
		Property p = SocialNetwork.instance.getModel().getProperty(sm+"friend");
		Resource o = SocialNetwork.instance.getModel().getResource(sm+person2);
		SocialNetwork.instance.getModel().add(s, p, o);
	}
	
	@DELETE
	@Path("delete_person_link/{person1}/{person2}")
	@Consumes(MediaType.TEXT_XML)
	public void delete_person_link(@PathParam("person1") String person1, @PathParam("person2") String person2) {
		Resource s = SocialNetwork.instance.getModel().getResource(sm+person1);
		Property p = SocialNetwork.instance.getModel().getProperty(sm+"friend");
		Resource o = SocialNetwork.instance.getModel().getResource(sm+person2);
		SocialNetwork.instance.getModel().remove(s, p, o);
	}
	
	@GET
	@Path("retrieve_person/{query}")
	@Produces(MediaType.APPLICATION_JSON)
	public String retrieve_person(@PathParam("query") String query) {
		String queryString = ""
                + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "SELECT ?person "
                + "WHERE { "
                + "?person rdf:type foaf:Person ." 
                + "}";
		Query q = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(q, SocialNetwork.instance.getModel());
        ResultSet results = qexec.execSelect();
        String result = "";
        while(results.hasNext()){
            QuerySolution solution = results.nextSolution();
            String sol = solution.getResource("?person").getLocalName();
            if(sol.startsWith(query)) {
            	result += sol;
            }
        }  
        return result;
	}
}
