package com.nubytouch.crisiscare.data.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.DataUtil;
import com.nubytouch.crisiscare.data.model.Document;

public class DocumentDTO
{
    @SerializedName("Guid")
    private String id;
    @SerializedName("Title")
    private String title;
    @SerializedName("Filename")
    private String filename;
    @SerializedName("Size")
    private long filesize;
    @SerializedName("ReadableSize")
    private String readableSize;
    @SerializedName("FolderName")
    private String folderName;

    public void setId(String id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public void setFilesize(long filesize)
    {
        this.filesize = filesize;
    }

    public void setReadableSize(String readableSize)
    {
        this.readableSize = readableSize;
    }

    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }

    public Document buildDocument()
    {
        Document doc = new Document();
        doc.setId(id + DataUtil.generateUUID());
        doc.setTitle(title);
        doc.setFilename(filename);
        doc.setFilesize(filesize);
        doc.setReadableSize(readableSize);
        doc.setFolderName(folderName);
        return doc;
    }
}
