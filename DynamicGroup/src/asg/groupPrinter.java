package asg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class groupPrinter extends DefaultHandler
{
  List myContacts;
  private String tempVal;
  private String requiredFriend;
  private String requiredContext;
  private Contacts tempCon;

  public groupPrinter()
  {
    this.myContacts = new ArrayList();
  }

  public void runExample(String frindID)
  {
    this.requiredFriend = frindID;
    parseDocument();
  }

  public void runContextSearch(String contextID)
  {
    this.requiredContext = contextID;
    parseDocument();
  }

  private void parseDocument()
  {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    try
    {
      URL url = new URL("http://www.csee.ltu.se/~mjrana/rank1.xml");

      SAXParser sp = spf.newSAXParser();

      sp.parse(new InputSource(url.openStream()), this);
    }
    catch (SAXException se)
    {
      se.printStackTrace();
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  public String printData()
  {
    Iterator it = this.myContacts.iterator();
    StringBuffer sb = new StringBuffer();
    try {
      while (it.hasNext())
      {
        sb.append(it.next().toString());
      }
    }
    catch (Exception e) {
    }
    return sb.toString();
  }

  public List<Contacts> GroupList()
  {
    return this.myContacts;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
  {
    this.tempVal = "";
    try
    {
      if (qName.equalsIgnoreCase("nsname"))
      {
        if (this.requiredContext.compareTo(attributes.getValue("fb_location")) == 0)
        {
          this.tempCon = new Contacts();

          this.tempCon.setName(attributes.getValue("name"));
          this.tempCon.setFbStatus(attributes.getValue("fb_status"));

          this.tempCon.setImage(attributes.getValue("image"));

          this.myContacts.add(this.tempCon);
        }
      }
    }
    catch (Exception e)
    {
    }
  }

  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    this.tempVal = new String(ch, start, length);
  }

  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (qName.equalsIgnoreCase("nsname"));
  }
}