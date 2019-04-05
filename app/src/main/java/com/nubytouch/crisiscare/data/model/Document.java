package com.nubytouch.crisiscare.data.model;

public class Document
{
	private String id;
	private String title;
	private String filename;
	private long   filesize;
	private String readableSize;
	private String folderName;

	public Document()
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

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public long getFilesize()
	{
		return filesize;
	}

	public void setFilesize(long filesize)
	{
		this.filesize = filesize;
	}

	public String getReadableSize()
	{
		return readableSize;
	}

	public void setReadableSize(String readableSize)
	{
		this.readableSize = readableSize;
	}

	public String getFilenamePath()
	{
		return /*"file://" + */filename;
	}

	public String getFolderName()
	{
		return folderName;
	}

	public void setFolderName(String folderName)
	{
		this.folderName = folderName;
	}
}
