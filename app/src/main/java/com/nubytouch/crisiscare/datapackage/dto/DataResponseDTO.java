package com.nubytouch.crisiscare.datapackage.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.dto.*;
import com.nubytouch.crisiscare.data.model.*;

import java.util.ArrayList;
import java.util.List;

public class DataResponseDTO
{
    @SerializedName("Contacts")
    private List<UserDTO> contacts;
    @SerializedName("Documents")
    private List<DocumentDTO> documents;
    @SerializedName("Groups")
    private List<GroupDTO> groups;
    @SerializedName("Topics")
    private List<TopicDTO> topics;
    @SerializedName("MetaData")
    private MetadataDTO metadata;
    @SerializedName("User")
    private UserDTO user;

    public List<UserDTO> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<UserDTO> contacts)
    {
        this.contacts = contacts;
    }

    public List<DocumentDTO> getDocuments()
    {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents)
    {
        this.documents = documents;
    }

    public List<GroupDTO> getGroups()
    {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups)
    {
        this.groups = groups;
    }

    public List<TopicDTO> getTopics()
    {
        return topics;
    }

    public void setTopics(List<TopicDTO> topics)
    {
        this.topics = topics;
    }

    public MetadataDTO getMetadata()
    {
        return metadata;
    }

    public void setMetadata(MetadataDTO metadata)
    {
        this.metadata = metadata;
    }

    public UserDTO getUser()
    {
        return user;
    }

    public void setUser(UserDTO user)
    {
        this.user = user;
    }

    public List<User> buildContacts()
    {
        ArrayList<User> items = new ArrayList<>();
        for (UserDTO item : contacts)
        {
            items.add(item.buildUser());
        }
        return items;
    }

    public List<Document> buildDocuments()
    {
        ArrayList<Document> items = new ArrayList<>();
        for (DocumentDTO item : documents)
        {
            items.add(item.buildDocument());
        }
        return items;
    }

    public List<Group> buildGroups()
    {
        ArrayList<Group> items = new ArrayList<>();
        for (GroupDTO item : groups)
        {
            items.add(item.buildModel());
        }
        return items;
    }

    public List<Topic> buildTopics()
    {
        ArrayList<Topic> items = new ArrayList<>();
        for (TopicDTO item : topics)
        {
            items.add(item.buildTopic());
        }
        return items;
    }

    public User buildUser()
    {
        return user.buildUser();
    }

    public Metadata buildMetadata()
    {
        return metadata.buildMetadata();
    }
}
