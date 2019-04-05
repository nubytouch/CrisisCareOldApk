package com.nubytouch.crisiscare.ui.contacts.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.data.model.Group;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.job.AbstractJob;
import com.nubytouch.crisiscare.ui.contacts.GroupWrapper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;


public class GenerateContactTreeJob extends AbstractJob
{
    public GenerateContactTreeJob()
    {
        super(new Params(Priority.HIGH.value));
    }

    @Override
    protected void execute() throws Exception
    {
        List<Group>     groups   = DataPackageManager.getGroups();
        ArrayList<User> contacts = (ArrayList<User>) DataPackageManager.getContacts();

        List<GroupWrapper> wrappers;

        if (groups.isEmpty())
            wrappers = oldImplementation(contacts);
        else
            wrappers = newImplementation(groups, contacts);

        postCompleteEvent(new GenerateContactTreeEvent(wrappers));
    }

    private List<GroupWrapper> oldImplementation(ArrayList<User> contacts)
    {
        Timber.d("Old imp");
        List<GroupWrapper>            wrappers = new ArrayList<>();
        HashMap<String, GroupWrapper> groupMap = new HashMap<>();

        GroupWrapper gw;
        for (User contact : contacts)
        {
            if (contact.getDepartmentId() != null && !contact.isDeputy())
            {
                gw = groupMap.get(contact.getDepartmentId());

                if (gw == null)
                {
                    gw = new GroupWrapper(contact.getDepartmentId(), contact.getDepartmentName());
                    wrappers.add(gw);
                    groupMap.put(gw.id, gw);
                }

                gw.contacts.add(contact);

                // init SQL query and store deputies in memory
                // avoid having to doing this for every contact on main thread
                contact.getDeputies();
            }
        }

        Collections.sort(wrappers, new Comparator<GroupWrapper>()
        {
            @Override
            public int compare(GroupWrapper lhs, GroupWrapper rhs)
            {
                if (lhs.name == null)
                    return 1;

                if (rhs.name == null)
                    return -1;

                return lhs.name.compareTo(rhs.name);
            }
        });

        for (GroupWrapper wrapper : wrappers)
            sortContacts(wrapper.contacts);


        return wrappers;
    }

    private List<GroupWrapper> newImplementation(List<Group> groups, ArrayList<User> contacts)
    {
        List<GroupWrapper>            wrappers = new ArrayList<>();
        HashMap<String, GroupWrapper> groupMap = new HashMap<>();

        sortGroups(groups);

        GroupWrapper gw;
        for (Group group : groups)
        {
            gw = new GroupWrapper(group.getId(), group.getName());

            wrappers.add(gw);
            groupMap.put(group.getId(), gw);
        }

        String[] ids;

        for (User contact : contacts)
        {
            // isDeputy contacts should not be visible on the main list
            if (contact.getGroupIds() != null && !contact.getGroupIds().isEmpty() && !contact.isDeputy())
            {
                ids = StringUtils.split(contact.getGroupIds(), ",");

                for (String id : ids)
                {
                    gw = groupMap.get(id);

                    if (gw != null)
                        gw.contacts.add(contact);
                }
            }
        }

        for (GroupWrapper wrapper : wrappers)
            sortContacts(wrapper.contacts);

        return wrappers;
    }

    private void sortGroups(List<Group> groups)
    {
        Collections.sort(groups, new Comparator<Group>()
        {
            @Override
            public int compare(Group lhs, Group rhs)
            {
                if (lhs.getName() == null)
                    return 1;

                if (rhs.getName() == null)
                    return -1;

                return lhs.getName().compareTo(rhs.getName());
            }
        });
    }

    private void sortContacts(List<User> contacts)
    {
        Collections.sort(contacts, new Comparator<User>()
        {
            @Override
            public int compare(User o1, User o2)
            {
                if (o1.getFullName() == null)
                    return 1;

                if (o2.getFullName() == null)
                    return -1;

                return o1.getFullName().compareTo(o2.getFullName());
            }
        });
    }

    @Override
    protected void onRequestError()
    {

    }

    @Override
    public void onAdded()
    {

    }
}
