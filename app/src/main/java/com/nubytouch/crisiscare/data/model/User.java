package com.nubytouch.crisiscare.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.nubytouch.crisiscare.datapackage.DataPackageManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable
{
    private String  id;
    private String  firstname;
    private String  lastname;
    private String  username; // Not longer used, use email instead
    private String  departmentId;
    private String  departmentName;
    private String  siteName;
    private String  jobId;
    private String  jobName;
    private String  mobilePhone;
    private String  phone;
    private String  privatePhone;
    private String  privateMobilePhone;
    private String  privateEmail;
    private boolean isDeputy;
    private boolean isOnCall;
    private String  deputyUsernames;
    private boolean powerUser;
    private boolean appUser;
    private String  email;

    private List<User> deputyList;

    private String  groupIds;
    private boolean hasAcceptedDisclaimer;

    public User()
    {

    }

    protected User(Parcel in)
    {
        id = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        username = in.readString();
        departmentId = in.readString();
        departmentName = in.readString();
        siteName = in.readString();
        jobId = in.readString();
        jobName = in.readString();
        mobilePhone = in.readString();
        phone = in.readString();
        privatePhone = in.readString();
        privateMobilePhone = in.readString();
        privateEmail = in.readString();
        isDeputy = in.readByte() != 0;
        isOnCall = in.readByte() != 0;
        deputyUsernames = in.readString();
        powerUser = in.readByte() != 0;
        appUser = in.readByte() != 0;
        email = in.readString();
        deputyList = in.createTypedArrayList(User.CREATOR);
        groupIds = in.readString();
        hasAcceptedDisclaimer = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(username);
        dest.writeString(departmentId);
        dest.writeString(departmentName);
        dest.writeString(siteName);
        dest.writeString(jobId);
        dest.writeString(jobName);
        dest.writeString(mobilePhone);
        dest.writeString(phone);
        dest.writeString(privatePhone);
        dest.writeString(privateMobilePhone);
        dest.writeString(privateEmail);
        dest.writeByte((byte) (isDeputy ? 1 : 0));
        dest.writeByte((byte) (isOnCall ? 1 : 0));
        dest.writeString(deputyUsernames);
        dest.writeByte((byte) (powerUser ? 1 : 0));
        dest.writeByte((byte) (appUser ? 1 : 0));
        dest.writeString(email);
        dest.writeTypedList(deputyList);
        dest.writeString(groupIds);
        dest.writeByte((byte) (hasAcceptedDisclaimer ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel in)
        {
            return new User(in);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = StringUtils.capitalize(StringUtils.trimToEmpty(firstname));
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = StringUtils.trimToEmpty(lastname);
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(String departmentId)
    {
        this.departmentId = departmentId;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getGroupIds()
    {
        return groupIds;
    }

    public void setGroupIds(String groupIds)
    {
        this.groupIds = groupIds;
    }

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public String getMobilePhone()
    {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPrivatePhone()
    {
        return privatePhone;
    }

    public void setPrivatePhone(String privatePhone)
    {
        this.privatePhone = privatePhone;
    }

    public String getPrivateMobilePhone()
    {
        return privateMobilePhone;
    }

    public void setPrivateMobilePhone(String privateMobilePhone)
    {
        this.privateMobilePhone = privateMobilePhone;
    }

    public String getPrivateEmail()
    {
        return privateEmail;
    }

    public void setPrivateEmail(String privateEmail)
    {
        this.privateEmail = privateEmail;
    }

    public boolean isDeputy()
    {
        return isDeputy;
    }

    public void setDeputy(boolean deputy)
    {
        isDeputy = deputy;
    }

    public boolean isOnCall()
    {
        return isOnCall;
    }

    public void setOnCall(boolean onCall)
    {
        isOnCall = onCall;
    }

    public String getDeputyUsernames()
    {
        return deputyUsernames;
    }

    public void setDeputyUsernames(String deputyUsernames)
    {
        this.deputyUsernames = deputyUsernames;
        deputyList = null;
    }

    public List<User> getDeputies()
    {
        if (deputyList != null)
            return deputyList;

        ArrayList<User> deputies = new ArrayList<>();

        if (deputyUsernames != null && !deputyUsernames.isEmpty())
        {
            String[] usernames = StringUtils.split(deputyUsernames, ",");

            for (User contact : DataPackageManager.getContacts())
            {
                for (String s : usernames)
                {
                    if (contact.email.equals(s))
                    {
                        deputies.add(contact);
                        break;
                    }
                }
            }
        }

        deputyList = deputies;

        return deputyList;
    }

    public boolean isPowerUser()
    {
        return powerUser;
    }

    public void setPowerUser(boolean powerUser)
    {
        this.powerUser = powerUser;
    }

    public boolean isAppUser()
    {
        return appUser;
    }

    public void setAppUser(boolean appUser)
    {
        this.appUser = appUser;
    }

    public String getEmail()
    {
        if (email == null)
            return username; // Quick fix for when username has been deprecated

        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isHasAcceptedDisclaimer()
    {
        return hasAcceptedDisclaimer;
    }

    public void setHasAcceptedDisclaimer(boolean hasAcceptedDisclaimer)
    {
        this.hasAcceptedDisclaimer = hasAcceptedDisclaimer;
    }

    public String getFullName()
    {
        return (lastname.length() > 0 ? lastname + " " : "") + firstname;
    }

    public boolean hasDeputy()
    {
        if (deputyList == null)
            getDeputies();

        return deputyList.size() > 0;
    }

    public User getDeputy()
    {
        if (hasDeputy())
            return deputyList.get(0);

        return null;
    }

    public boolean isInGroup(String groupId)
    {
        return groupIds != null && groupIds.contains(groupId);
    }
}
