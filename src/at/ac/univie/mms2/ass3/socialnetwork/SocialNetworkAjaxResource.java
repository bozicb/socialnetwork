package at.ac.univie.mms2.ass2.socialnetwork;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("home")
public class SocialNetworkAjaxResource {
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String socialmedia() throws MalformedURLException, IOException {
		URL url = new URL("file://" + System.getProperty("user.dir") + "/WebContent/index.html");
		URLConnection con = url.openConnection();
		Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
		Matcher m = p.matcher(con.getContentType());
		String charset = m.matches() ? m.group(1) : "ISO-8859-1";
		Reader r = new InputStreamReader(con.getInputStream(), charset);
		StringBuilder buf = new StringBuilder();
		while (true) {
		  int ch = r.read();
		  if (ch < 0)
		    break;
		  buf.append((char) ch);
		}
		return buf.toString();
	}
	
	@Path("friends")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String find_friends() throws MalformedURLException, IOException {
		URL url = new URL("file://" + System.getProperty("user.dir") + "/WebContent/friends.html");
		URLConnection con = url.openConnection();
		Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
		Matcher m = p.matcher(con.getContentType());
		String charset = m.matches() ? m.group(1) : "ISO-8859-1";
		Reader r = new InputStreamReader(con.getInputStream(), charset);
		StringBuilder buf = new StringBuilder();
		while (true) {
		  int ch = r.read();
		  if (ch < 0)
		    break;
		  buf.append((char) ch);
		}
		return buf.toString();
	}
	
}
