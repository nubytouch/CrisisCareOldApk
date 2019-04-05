package com.nubytouch.crisiscare.data.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.DataUtil;
import com.nubytouch.crisiscare.data.model.User;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UserDTO
{
    @SerializedName("Id")
    private String id;
    @SerializedName("Firstname")
    private String firstname;
    @SerializedName("Name")
    private String lastname;
    @SerializedName("Username")
    private String username;
    @SerializedName("Password")
    private String password;
    @SerializedName("DepartmentId")
    private String departmentId;
    @SerializedName("Department")
    private String department;
    @SerializedName("GroupIds")
    private String[] groupIds;
    @SerializedName("SiteId")
    private String siteName;
    @SerializedName("JobTitleId")
    private String jobTitleId;
    @SerializedName("JobTitle")
    private String jobTitle;
    @SerializedName("Position")
    private String position;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("Extension")
    private String extension;
    @SerializedName("Home")
    private String home;
    @SerializedName("Blackberry")
    private String blackberry;
    @SerializedName("PrivateEmail")
    private String privateEmail;
    @SerializedName("IsDeputy")
    private boolean isDeputy;
    @SerializedName("IsOncall")
    private boolean isOnCall;
    @SerializedName("DeputyUsernames")
    private List<String> deputies;
    @SerializedName("IsPowerUser")
    private boolean isPowerUser;
    @SerializedName("IsAppUser")
    private boolean isAppUser;
    @SerializedName("HasAcceptedDisclaimer")
    private boolean hasAcceptedDisclaimer;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setDepartmentId(String departmentId)
    {
        this.departmentId = departmentId;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public void setGroupIds(String[] groupIds)
    {
        this.groupIds = groupIds;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public void setJobTitleId(String jobTitleId)
    {
        this.jobTitleId = jobTitleId;
    }

    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public void setHome(String home)
    {
        this.home = home;
    }

    public void setBlackberry(String blackberry)
    {
        this.blackberry = blackberry;
    }

    public void setPrivateEmail(String privateEmail)
    {
        this.privateEmail = privateEmail;
    }

    public void setDeputy(boolean deputy)
    {
        isDeputy = deputy;
    }

    public void setOnCall(boolean onCall)
    {
        isOnCall = onCall;
    }

    public void setDeputies(List<String> deputies)
    {
        this.deputies = deputies;
    }

    public void setPowerUser(boolean powerUser)
    {
        isPowerUser = powerUser;
    }

    public void setAppUser(boolean appUser)
    {
        isAppUser = appUser;
    }

    public User buildUser()
    {
        User user = new User();
        user.setId(id + DataUtil.generateUUID()); // Prevent BDD to reject insert because of Primary Key
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username  + DataUtil.generateUUID()); // Prevent BDD to reject insert because of Unique constraint
        user.setDepartmentId(departmentId);
        user.setDepartmentName(department);
        user.setSiteName(siteName);
        user.setJobId(jobTitleId);
        user.setJobName(position != null && !position.isEmpty() ? position : jobTitle);
        user.setMobilePhone(mobile);
        user.setPhone(extension);
        user.setPrivatePhone(home);
        user.setPrivateMobilePhone(blackberry);
        user.setPrivateEmail(privateEmail);
        user.setDeputy(isDeputy);
        user.setOnCall(isOnCall);
        user.setPowerUser(isPowerUser);
        user.setAppUser(isAppUser);
        user.setEmail(username);
        user.setEmail(username);
        user.setHasAcceptedDisclaimer(hasAcceptedDisclaimer);

        String groupIds = "";
        if (this.groupIds != null)
            groupIds = StringUtils.join(this.groupIds, ",");

        user.setGroupIds(groupIds);

        String deputies = "";
        if (this.deputies != null)
            deputies = StringUtils.join(this.deputies, ",");

        user.setDeputyUsernames(deputies);

        return user;
    }
}
