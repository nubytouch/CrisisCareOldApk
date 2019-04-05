package com.nubytouch.crisiscare.data.model;

public class Topic
{
    public final static String CATEGORY_WEB = "WEB";
    public final static String CATEGORY_WS  = "WS";

    private String  id;
    private String  title;
    private String  link;
    private String  category;
    private int     order;
    private int backgroundColor;
    private String  externalLink;

    public Topic()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public int getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public String getExternalLink()
    {
        return externalLink;
    }

    public void setExternalLink(String externalLink)
    {
        this.externalLink = externalLink;
    }

    public String getLocalLink()
    {
        return link;
    }

    // To move to the new Topic Model class
    public boolean isWebContent()
    {
        return getLink().endsWith(".html");
    }

    public boolean isContacts()
    {
        return getLink().endsWith("contacts");
    }

    public boolean isDocuments()
    {
        return getLink().endsWith("documents");
    }

    public boolean isVideos()
    {
        return getLink().endsWith("videos");
    }

    public boolean isMessages()
    {
        return getLink().endsWith("messages");
    }
    public boolean isNews()
    {
        return getLink().endsWith("news");
    }

    public boolean isAlerts()
    {
        return getLink().endsWith("alerts");
    }

    public boolean isChecklists()
    {
        return getLink().endsWith("checklists");
    }

    public boolean isNotes()
    {
        return getLink().endsWith("notes");
    }

    public boolean isFallbackSites()
    {
        return getLink().equals("map");
    }
}
