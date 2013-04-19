package asg;

public class Contacts
{
  private String name;
  private String fb_status;
  private String fb_location;
  private String fb_calendar;
  private String news_header;
  private String blog_title;
  private String social_strength;
  private String image;

  public Contacts()
  {
  }

  public Contacts(String name, String fb_status, String fb_location, String fb_calendar, String news_header, String blog_title, String social_strength, String image)
  {
    this.name = name;
    this.fb_status = fb_status;
    this.fb_location = fb_location;
    this.fb_calendar = fb_calendar;
    this.news_header = news_header;
    this.blog_title = blog_title;
    this.social_strength = social_strength;
    this.image = image;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFbStatus()
  {
    return this.fb_status;
  }

  public void setFbStatus(String fb_Status) {
    this.fb_status = fb_Status;
  }

  public String getLocation()
  {
    return this.fb_location;
  }

  public void setLocation(String fb_location) {
    this.fb_location = fb_location;
  }

  public String getCalendar()
  {
    return this.fb_calendar;
  }

  public void setCalendar(String fb_calendar) {
    this.fb_calendar = fb_calendar;
  }

  public String getBlogs()
  {
    return this.blog_title;
  }

  public void setBlogs(String blog_title) {
    this.blog_title = blog_title;
  }

  public String getNews() {
    return this.news_header;
  }

  public void setNews(String news_header) {
    this.news_header = news_header;
  }

  public String getStrength()
  {
    return this.social_strength;
  }

  public void setStrength(String social_strength) {
    this.social_strength = social_strength;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    if (getName() != null)
    {
      sb.append(" Contact person=\"" + getName() + "\"\n");
    }

    if (getFbStatus() != null)
    {
      sb.append(" Status=\"" + getFbStatus() + "\"\n");
    }

    if (getLocation() != null)
    {
      sb.append(" Location=\"" + getLocation() + "\"\n");
    }

    if (getCalendar() != null)
    {
      sb.append(" calendar=\"" + getCalendar() + "\"\n");
    }

    if (getNews() != null)
    {
      sb.append(" news=\"" + getNews() + "\"\n");
    }

    if (getBlogs() != null)
    {
      sb.append(" blogs=\"" + getBlogs() + "\"\n");
    }

    if (getStrength() != null)
    {
      sb.append(" Social strength=\"" + getStrength() + "\"\n");
    }

    if (getImage() != null)
    {
      sb.append(" Image=\"" + getImage() + "\"\n");
    }
    sb.append("\n");
    return sb.toString();
  }
}