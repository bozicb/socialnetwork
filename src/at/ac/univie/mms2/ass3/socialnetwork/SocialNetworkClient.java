package at.ac.univie.mms2.ass2.socialnetwork;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class SocialNetworkClient {

	public static void main(String[] args) {
		WebResource wr = Client.create().resource("http://localhost:8081/socialnetwork");
		wr.path("social_network").path("add_graph").type(MediaType.TEXT_XML).put("social-media-rdf.owl");
		//System.out.println(wr.path("social_network").path("persons").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("persons").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//System.out.println(wr.path("social_network").path("persons").path("Bob").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("persons").path("Bob").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//wr.path("social_network").path("create_person").path("Gerald").path("petanque").path("Glendale").path("62").type(MediaType.TEXT_XML).put();
		//wr.path("social_network").path("update_person").path("Bob").path("programming").path("baseball").path("hasActivity").type(MediaType.TEXT_XML).post();
		//wr.path("social_network").path("delete_person").path("Gerald").type(MediaType.TEXT_XML).delete();
		//System.out.println(wr.path("social_network").path("comments").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("comments").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//System.out.println(wr.path("social_network").path("comments").path("comment1").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//System.out.println(wr.path("social_network").path("comments").path("person").path("Bob").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("comments").path("person").path("Bob").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//System.out.println(wr.path("social_network").path("comments").path("media").path("photo1").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("comments").path("media").path("photo1").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//wr.path("social_network").path("create_comment").path("comment2").path("Beautiful picture!").path("Bob").path("photo1").type(MediaType.TEXT_XML).put();
		//wr.path("social_network").path("update_comment").path("comment1").path("Cool photo ;)").path("Nice!").path("text").type(MediaType.TEXT_XML).post();
		//wr.path("social_network").path("delete_comment").path("comment1").type(MediaType.TEXT_XML).delete();
		//System.out.println(wr.path("social_network").path("media").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("media").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//System.out.println(wr.path("social_network").path("media").path("person").path("Bob").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("media").path("person").path("Bob").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//System.out.println(wr.path("social_network").path("media").path("photo1").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("media").path("photo1").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//wr.path("social_network").path("create_media").path("video2").path("Computer Science Lecture").type(MediaType.TEXT_XML).put();
		//wr.path("social_network").path("update_media").path("video1").path("Talk from International Semantic Web Conference 2012").path("Talk from International Semantic Web Conference 2013").path("title").type(MediaType.TEXT_XML).post();
		//wr.path("social_network").path("delete_media").path("video2").type(MediaType.TEXT_XML).delete();
		//System.out.println(wr.path("social_network").path("likes").path("person").path("Bob").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("likes").path("person").path("Bob").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//wr.path("social_network").path("create_like").path("Bob").path("video1").type(MediaType.TEXT_XML).put();
		//wr.path("social_network").path("delete_like").path("Bob").path("video1").type(MediaType.TEXT_XML).delete();
		//System.out.println(wr.path("social_network").path("list_friends").path("Bob").type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).get(String.class));
		//System.out.println(wr.path("social_network").path("list_friends").path("Bob").type(MediaType.TEXT_HTML).accept(MediaType.TEXT_HTML).get(String.class));
		//wr.path("social_network").path("create_person_link").path("Bob").path("Esther").type(MediaType.TEXT_XML).put();
		//wr.path("social_network").path("delete_person_link").path("Bob").path("Esther").type(MediaType.TEXT_XML).delete();
		//System.out.println(wr.path("social_network").path("retrieve_person").type(MediaType.APPLICATION_JSON).get(String.class));
	}

}
