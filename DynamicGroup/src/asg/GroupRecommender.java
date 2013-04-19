package asg;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.EventWho;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GroupRecommender
{
  public String recommendGroup(String context)
  {
    String temp = "text";

    SaxPrinter spe = new SaxPrinter();
    try {
      spe.runContextSearch(context);
    }
    catch (Exception e)
    {
    }

    temp = spe.printData();

    return temp;
  }

  public String recommendContacts(String context)
  {
    String temp = "text";

    groupPrinter spe = new groupPrinter();
    try {
      spe.runContextSearch(context);
    }
    catch (Exception e)
    {
    }

    List groupmembers = spe.GroupList();

    temp = spe.printData();

    return temp;
  }

  public String AccessCalendar(String username, String password)throws FileNotFoundException, IOException
  //public static void main(String args[]) throws IOException
  {
    //String username="media.ltu";
    //String password="socialgraph";
    File logFile1 = new File(new StringBuilder().append(username).append("_calendardata.rss").toString());
    if (!logFile1.exists()) {
      logFile1.createNewFile();
   }

    setClear(logFile1, " ");

    String content = "<?xml version= \"1.0\" encoding=\"UTF-8\"?>\n<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n<channel>\n";

    setContents(logFile1, content);

    CalendarService myService = new CalendarService("exampleCo-exampleApp-1.0");
    try
    {
      myService.setUserCredentials(new StringBuilder().append(username).append("@gmail.com").toString(), password);

      URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");

      URL eventURL = new URL(new StringBuilder().append("http://www.google.com/calendar/feeds/").append(username).append("@gmail.com/private/full").toString());

      CalendarFeed resultFeed = (CalendarFeed)myService.getFeed(feedUrl, CalendarFeed.class);

      CalendarEventFeed myFeed = (CalendarEventFeed)myService.getFeed(eventURL, CalendarEventFeed.class);
      CalendarEntry entry;
      for (int i = 0; i < resultFeed.getEntries().size(); i++) {
        entry = (CalendarEntry)resultFeed.getEntries().get(i);
     }

     for (int i = 0; i < myFeed.getEntries().size(); i++)
     {
        CalendarEventEntry entry2 = (CalendarEventEntry)myFeed.getEntries().get(i);
        String event = entry2.getTitle().getPlainText();
        String content1 = new StringBuilder().append("<item>\n<title>").append(event).append("</title>\n").append("<members>").toString();

        setContents(logFile1, content1);
        StringBuilder sb = new StringBuilder();
        List participant = entry2.getParticipants();
        for (int j = 0; j < participant.size(); j++)
        {
          String email_id = ((EventWho)participant.get(j)).getEmail();

          sb.append(new StringBuilder().append(email_id).append(" ").toString());
        }

        content1 = new StringBuilder().append(sb.toString()).append("</members>\n").append("</item>\n").toString();
        setContents(logFile1, content1);
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (ServiceException e) {
      e.printStackTrace();
    }
    content = "</channel>\n</rss>\n";
    setContents(logFile1, content);
    return username;
  }
  
  
// functions that adds location of the user
  
  
    //public static void main(String args[]) throws IOException, TransformerException
  public String addCurrentLocation(String username, String plat, String plon)throws FileNotFoundException, IOException, TransformerException
  	{
	
	  /*String userName="Shabuj";
	  String latitude="kista";
	  String longitude="gala";*/
	  
	  String userName=username;
	  String latitude=plat;
	  String longitude=plon;
	  
	  //local computer
	  //File userFile = new File("/users/mjrana/Sites/notes/data/location/asg_location.xml");              
      
	//server computer
	  //File userFile = new File("/home/juwel/applications/axis2/bin/userdata/notes/data/location/asg_location.xml");
	  File userFile = new File("/home/juwel/applications/data/userdata/notes/data/location/asg_location.xml");
      
	  if (!userFile.exists()) {
          userFile.createNewFile();
          String body="<?xml version=\"1.0\" encoding=\"UTF-8\"?><contact>"
        	  		+"</contact>";
          setContents(userFile, body);
      }
	  
	  try{
			//local computer 
		  	//String filepath = "/users/mjrana/Sites/notes/data/location/asg_location.xml";
		
		  //server computer 
		  	//String filepath = "/home/juwel/applications/axis2/bin/userdata/notes/data/location/asg_location.xml";
		  String filepath = "/home/juwel/applications/data/userdata/notes/data/location/asg_location.xml";
			 
		  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			 Document doc = docBuilder.parse(filepath);
		 
			 //Get the root element
			 Node contact = doc.getFirstChild();		 
		     
			 Node contactNode = doc.getFirstChild(); 
		 
			 NodeList locationList = contactNode.getChildNodes();
			 int k =0;
			 for (k =0; k<locationList.getLength();k++){
		         //Get the staff element by tag name directly
		         Node location = locationList.item(k);
		 
		         NodeList list = location.getChildNodes();
		 
		         int breaker=0;
		         for (int i =0; i<list.getLength();i++){
				 Node node = list.item(i);
				 
				 //get the user element, and update the latitude and longitude parameters...
				 if("name".equals(node.getNodeName())){
					 String nameText= node.getTextContent();
					 if(nameText.equals(userName)){
						 //node.setTextContent("Shabuj");
						 Node latt=list.item(i=i+1);
						 latt.setTextContent(latitude);
						 Node lonn=list.item(i=i+1);
						 lonn.setTextContent(longitude);
						 breaker=1;
						 break;
					 }
					     	 
				 }
				 
			 }
		         if(breaker==1)
					 break;
			 }
			 if(k==locationList.getLength()){
	        	 Element newLocation = doc.createElement("location");
			     
			     Element name = doc.createElement("name");	 
			     name.appendChild(doc.createTextNode(userName));
			     Element lat = doc.createElement("latitude");	 
			     lat.appendChild(doc.createTextNode(latitude));
			     Element lon = doc.createElement("longitude");	 
			     lon.appendChild(doc.createTextNode(longitude));
			     
			     newLocation.appendChild(name);
			     newLocation.appendChild(lat);
			     newLocation.appendChild(lon);
			     contact.appendChild(newLocation);
	         }
			 			 
			//write the content into xml file
		     TransformerFactory transformerFactory = TransformerFactory.newInstance();
		     Transformer transformer = transformerFactory.newTransformer();
			 DOMSource source = new DOMSource(doc);
			 StreamResult result =  new StreamResult(new File(filepath));
		     transformer.transform(source, result);
		     
		 
		   }catch(ParserConfigurationException pce){
			 pce.printStackTrace();
		   }catch(IOException ioe){
			 ioe.printStackTrace();
		   }catch(SAXException sae){
			 sae.printStackTrace();
		   }
	return "shabuj";
		    
	}
  

 
 // call logs
  
  
  //public static void main(String args[]) throws IOException, TransformerException
  public String callLogs(String username, String clog)throws FileNotFoundException, IOException, TransformerException
  	{
	  
	  
	  String userName=username;
	  String callLogs=clog;
	  
	//server computer
	  File userFile = new File("/home/juwel/applications/data/userdata/notes/data/callLog/"+userName+".xml");
      
	  if (!userFile.exists()) {
          userFile.createNewFile();
          
          setContents(userFile, callLogs);
      }else
    	  
	  setContents(userFile, callLogs);
	  return "shabuj";		    
	}
  
  // Register Users
  
  
  //public static void main(String args[]) throws IOException, TransformerException
  public String registerUsers(String username, String password)throws FileNotFoundException, IOException, TransformerException
  	{
	  
	  String userName=username;
	  String userPass=password;
	  
	//server computer
	  File userFile = new File("/home/juwel/applications/data/userdata/notes/data/userList/users.txt");
	  
	  if (!userFile.exists()) {
          userFile.createNewFile();
          
          setContents(userFile, userName+" "+userPass+"\n");
          return "REGISTERED";
      }else
      {  
    	  
    	  try {
              
              FileReader reader = new FileReader(userFile);
              BufferedReader in = new BufferedReader(reader);
              String string;
              String regiteredUser;
              while ((string = in.readLine()) != null) {	                		
                  String[] result = string.split("\\s");
                  regiteredUser=result[0];
              	  
              	  
              	  if(regiteredUser.matches(userName))
              	  {
              		  return "MATCHED"; 
              	  }
              }
              
    	  } catch (IOException e) {
              e.printStackTrace();
          }
    	  setContents(userFile, userName+" "+userPass+"\n");
    	  return "REGISTERED";
  	}		    
	}
  
  
  
  	// functions that eturns neighbourhood friends
  
  public String LocDistance(String plat, String plon)throws FileNotFoundException, IOException, TransformerException
  {
	
	  
    
    String lonText="00";
    String nameText="00";
    String latText="00";
    
    double myLat=Double.parseDouble(plat);
    double myLon=Double.parseDouble(plon);
    
    String[] nearFriends = null;
    StringBuilder nearContacts=new StringBuilder();
	 try{
			 //local computer
		 	 //String filepath = "/users/mjrana/Sites/notes/data/location/asg_location.xml";
		 	//server computer
		 	 //String filepath = "/home/juwel/applications/axis2/bin/userdata/notes/data/location/asg_location.xml";
		     String filepath = "/home/juwel/applications/data/userdata/notes/data/location/asg_location.xml";
			 
		 	 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			 Document doc = docBuilder.parse(filepath);
		 
			 //Get the root element
			 Node contact = doc.getFirstChild();		 
		     
			 Node contactNode = doc.getFirstChild(); 
		 
			 NodeList locationList = contactNode.getChildNodes();
			 int k =0;
			 
			 for (k =0; k<locationList.getLength();k++){
		         //Get the staff element by tag name directly
		         Node location = locationList.item(k);
		 
		         NodeList list = location.getChildNodes();
		 
		         for (int i =0; i<list.getLength();i++){
		        	 Node node = list.item(i);
		        	 if("name".equals(node.getNodeName()))
					 		nameText= node.getTextContent();
					 else if("latitude".equals(node.getNodeName()))
						 latText= node.getTextContent();
					 else if("longitude".equals(node.getNodeName()))
						 lonText= node.getTextContent();
				 
		         }
		         
		         double friendLat=Double.parseDouble(latText);
		         double friendLon=Double.parseDouble(lonText);
		         double distanceFromMe=distance(myLat, myLon, friendLat, friendLon, "M")*1000.0;
		         //double myLon=
		         
		         if(distanceFromMe<100.00)
		        	 nearContacts.append(nameText+"\n");
		         //System.out.println(nameText+" "+latText+" "+lonText+" "+distanceFromMe);
		         
		         
			 }
			 
		     
		 
		   }catch(ParserConfigurationException pce){
			 pce.printStackTrace();
		   }catch(IOException ioe){
			 ioe.printStackTrace();
		   }catch(SAXException sae){
			 sae.printStackTrace();
		   }
		   /*nearFriends= nearContacts.toString().split ("\n");
		     for (int counter=0;counter<nearFriends.length;counter++)
		    	 System.out.print("near friends "+nearFriends[counter]);*/
		   
		   //return nearFriends.toString();
		   return nearContacts.toString();

	}
	

	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
	  double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;
	  if (unit == "K") {
	    dist = dist * 1.609344;
	  } else if (unit == "N") {
	  	dist = dist * 0.8684;
	    }
	  return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
	  return (rad * 180.0 / Math.PI);
	}
  
  
  
  // Functions for adding new meeting.....
  
  public String AddMeeting(String userlist,String owner, String meetingName, String groupKey)throws FileNotFoundException, IOException
  
  {
	  
	  
	  //String str = "one-two-three";
	  
	  String username=null;
	  File userFile;
	  /* delimiter */
	  String delimiter = "-";
	  /* given string will be split by the argument delimiter provided. */
	  String[] temp = userlist.split(delimiter);
	  /* print substrings */
	  
	  for(int i =0; i < temp.length-1 ; i++){
	    username=temp[i];
	    //local computer
	    //userFile = new File("/users/mjrana/Sites/notes/data/users/"+username+".xml");              
	    //server computer
	    //userFile = new File("/home/juwel/applications/axis2/bin/userdata/notes/data/users/"+username+".xml");
	    userFile = new File("/home/juwel/applications/data/userdata/notes/data/users/"+username+".xml");
        
	    if (!userFile.exists()) {
              userFile.createNewFile();
              String body="<?xml version=\"1.0\" encoding=\"UTF-8\"?><channel>"
            	  		+"</channel>";
              setContents(userFile, body);
        }
      //server computer
      //String filepath = "/home/juwel/applications/axis2/bin/userdata/notes/data/users/"+username+".xml";
	    String filepath = "/home/juwel/applications/data/userdata/notes/data/users/"+username+".xml";
      //local computer
      //String filepath = "/users/mjrana/Sites/notes/data/users/"+username+".xml";
      
      userFileWrite(filepath,owner,meetingName,groupKey);
	  		   
	  }
		   //This part is for creating notes resources
		   
		    String folderName=groupKey;
			//local computer path
		    //String folderPath="/Users/mjrana/Sites/notes/";
			//server computer path
		    //String folderPath="/home/juwel/applications/axis2/bin/userdata/notes/";
		    String folderPath="/home/juwel/applications/data/userdata/notes/";
			
		    String resourceName="notes.js";
			dirCreate(folderPath+folderName);
			
			//creatting notes.js
			File notesFile = new File(folderPath+folderName+"/"+resourceName);              
		    if (!notesFile.exists()) {
		              notesFile.createNewFile();                       
		    }
		    //local computer
		    //String content=generateResources("/Users/mjrana/Sites/notes/base/note_header.txt");
		    //server computer
		    //String content=generateResources("/home/juwel/applications/axis2/bin/userdata/notes/base/note_header.txt");
		    String content=generateResources("/home/juwel/applications/data/userdata/notes/base/note_header.txt");
		    
		    setContents(notesFile, content);
		    content="\nlist = memCtrl.get(\"list://"+folderName+"/notes\" + adress);\n";
		    setContents(notesFile, content);
		    //local computer
		    //content=generateResources("/Users/mjrana/Sites/notes/base/notes_footer.txt");
		    //server computer
		    //content=generateResources("/home/juwel/applications/axis2/bin/userdata/notes/base/notes_footer.txt");
		    content=generateResources("/home/juwel/applications/data/userdata/notes/base/notes_footer.txt");
		    
		    setContents(notesFile, content);
		    
		    //creatting notes.html
		    resourceName="notes.html";
		    File notesHtmlFile = new File(folderPath+folderName+"/"+resourceName);              
		    if (!notesHtmlFile.exists()) {
		              notesHtmlFile.createNewFile();                       
		    }
		    //local computer
		    //content=generateResources("/Users/mjrana/Sites/notes/base/notes.html");
		    //server computer
		    //content=generateResources("/home/juwel/applications/axis2/bin/userdata/notes/base/notes.html");
		    content=generateResources("/home/juwel/applications/data/userdata/notes/base/notes.html");
		    
		    setContents(notesHtmlFile, content);
		   
		    //creating cchat.dms
		    resourceName="cchat.html";
		    File cchatHtmlFile = new File(folderPath+folderName+"/"+resourceName);              
		    if (!cchatHtmlFile.exists()) {
		              cchatHtmlFile.createNewFile();                       
		    }
		    //local computer
		    //content=generateResources("/Users/mjrana/Sites/notes/base/chat_header.txt");
		    //server computer
		    //content=generateResources("/home/juwel/applications/axis2/bin/userdata/notes/base/chat_header.txt");
		    content=generateResources("/home/juwel/applications/data/userdata/notes/base/chat_header.txt");
		    
		    setContents(cchatHtmlFile, content);
		    content="dsm_users = memCtrl.get(\"hash://typewriter/"+folderName+"/user\")\n";
		    setContents(cchatHtmlFile, content);
		    //local computer
		    //content=generateResources("/Users/mjrana/Sites/notes/base/chat_middle.txt");
		    //server computer
		    //content=generateResources("/home/juwel/applications/axis2/bin/userdata/notes/base/chat_middle.txt");
		    content=generateResources("/home/juwel/applications/data/userdata/notes/base/chat_middle.txt");
		    
		    setContents(cchatHtmlFile, content);
		    
		    
		    content="dsm_me = memCtrl.get(\"hash://typewriter/"+folderName+"/user/\" + user_name);";
		    setContents(cchatHtmlFile, content);
		    //local computer
		    //content=generateResources("/Users/mjrana/Sites/notes/base/chat_footer.txt");
		    //local computer
		    //content=generateResources("/home/juwel/applications/axis2/bin/userdata/notes/base/chat_footer.txt");
		    content=generateResources("/home/juwel/applications/data/userdata/notes/base/chat_footer.txt");
		    
		    setContents(cchatHtmlFile, content);
		    		    
		    
		   return meetingName;
  }
  
  
  public static void userFileWrite(String filepath, String username, String meetingName, String groupKey)
	{
	  
	  try{
			
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			 Document doc = docBuilder.parse(filepath);
		 
			 //Get the root element
			 Node company = doc.getFirstChild();		 
			  
			 
			 Element newstaff = doc.createElement("meeting");
		     
		     Element title = doc.createElement("title");	 
		     title.appendChild(doc.createTextNode(meetingName));
		     Element owner = doc.createElement("owner");	 
		     owner.appendChild(doc.createTextNode(username));
		     Element meetingid = doc.createElement("meetingid");	 
		     meetingid.appendChild(doc.createTextNode(groupKey));
		     newstaff.appendChild(title);
		     newstaff.appendChild(owner);
		     newstaff.appendChild(meetingid);
		     company.appendChild(newstaff);

		     //write the content into xml file
		     TransformerFactory transformerFactory = TransformerFactory.newInstance();
		     Transformer transformer = transformerFactory.newTransformer();
		     DOMSource source = new DOMSource(doc);
		     StreamResult result =  new StreamResult(new File(filepath));
		     transformer.transform(source, result);
		 
		     //System.out.println("Done");
		 
		   }catch(ParserConfigurationException pce){
			 pce.printStackTrace();
		   }catch(TransformerException tfe){
			 tfe.printStackTrace();
		   }catch(IOException ioe){
			 ioe.printStackTrace();
		   }catch(SAXException sae){
			 sae.printStackTrace();
		   }
	}
  
  public static void dirCreate(String dirName)
	{
		boolean succ = (new File(dirName)).mkdir();

		if (!succ) 
		{
			System.out.println("Creation failed");
		}

		else System.out.println("Created");
	}
	
	public static String generateResources(String path) throws IOException
	{
		FileInputStream input = new FileInputStream(path);

		byte[] fileData = new byte[input.available()];

		input.read(fileData);
		input.close();

		return new String(fileData, "UTF-8");
	}
  
  

  public static void setContents(File aFile, String aContents)
    throws FileNotFoundException, IOException
  {
    if (aFile == null) {
      throw new IllegalArgumentException("File should not be null.");
    }
    if (!aFile.exists()) {
      throw new FileNotFoundException(new StringBuilder().append("File does not exist: ").append(aFile).toString());
    }
    if (!aFile.isFile()) {
      throw new IllegalArgumentException(new StringBuilder().append("Should not be a directory: ").append(aFile).toString());
    }
    if (!aFile.canWrite()) {
      throw new IllegalArgumentException(new StringBuilder().append("File cannot be written: ").append(aFile).toString());
    }

    FileWriter output = new FileWriter(aFile, true);
    try
    {
      output.append(aContents);
    }
    finally {
      output.close();
    }
  }

  public static void setClear(File aFile, String aContents) throws FileNotFoundException, IOException {
    if (aFile == null) {
      throw new IllegalArgumentException("File should not be null.");
    }
    if (!aFile.exists()) {
      throw new FileNotFoundException(new StringBuilder().append("File does not exist: ").append(aFile).toString());
    }
    if (!aFile.isFile()) {
      throw new IllegalArgumentException(new StringBuilder().append("Should not be a directory: ").append(aFile).toString());
    }
    if (!aFile.canWrite()) {
      throw new IllegalArgumentException(new StringBuilder().append("File cannot be written: ").append(aFile).toString());
    }

    FileWriter output = new FileWriter(aFile, false);
    try
    {
      output.flush();
    }
    finally
    {
      output.close();
    }
  }
}