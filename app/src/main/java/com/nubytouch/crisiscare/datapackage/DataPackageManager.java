package com.nubytouch.crisiscare.datapackage;

import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.*;
import com.nubytouch.crisiscare.datapackage.dto.DataResponseDTO;
import com.nubytouch.crisiscare.datapackage.rest.DataService;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPackageManager
{
    private static final String TAG = "DataPackageManager";

    private static List<User> contacts = new ArrayList<>();
    private static List<Document> documents = new ArrayList<>();
    private static List<Group> groups = new ArrayList<>();
    private static List<Topic> topics = new ArrayList<>();
    private static Metadata metadata;

    private DataPackageCallback callback;
    private Call<DataResponseDTO>  call;
    private boolean             cancelled;

    public DataPackageManager()
    {

    }

    public void loadLatestPackage(DataPackageCallback callback)
    {
        this.callback = callback;

        final ServiceBuilder.ProgressListener listener = new ServiceBuilder.ProgressListener()
        {
            @Override
            public void update(long bytesRead, long contentLength, boolean done)
            {
                publishProgress(bytesRead, contentLength, true);
            }
        };

        DataService service = new ServiceBuilder().progressListener(listener).build(DataService.class);
        call = service.getData(new DataService.Params());
        Response<DataResponseDTO> response;

        publishProgress(10, 100, true);

        try
        {
            response = call.execute();

            publishProgress(90, 100, false);

            if (!response.isSuccessful())
            {
                if (callback != null && !cancelled)
                    callback.onNetworkError();
            }
            else
            {
                onDownloaded(response.body());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if (callback != null && !cancelled)
                callback.onNetworkError();
        }

        publishProgress(100, 100, false);


        if (callback != null && !cancelled)
            callback.onDownloadComplete();
    }

    private void onDownloaded(DataResponseDTO data)
    {
        contacts = data.buildContacts();
        documents = data.buildDocuments();
        groups = data.buildGroups();
        topics = data.buildTopics();
        metadata = data.buildMetadata();

        Session.setUser(data.buildUser());
    }


    private void publishProgress(long d, long t, boolean downloading)
    {
        if (callback != null)
        {
            float fraction = (float) d / t;
            int   progress = Math.round(fraction * 100);

            // Download = 75% of process
            // Unzip = 25% of process
            if (downloading)
                progress = (int) Math.round(progress * 0.75);
            else
                progress = (int) (75 + Math.round(progress * 0.25));

//            Timber.d("publishProgress: " + progress + "%");
            callback.onDownloadProgress(progress);
        }
    }

    public void clearData()
    {
        contacts.clear();
        documents.clear();
        groups.clear();
        topics.clear();
        metadata = null;
    }

    public void cancel()
    {
        cancelled = true;

        if (call != null && !call.isExecuted() && !call.isCanceled())
            call.cancel();
    }

    public interface DataPackageCallback
    {
        void onNetworkError();

        void onDownloadProgress(int progress);

        void onDownloadComplete();
    }

    public static List<User> getContacts()
    {
        return contacts;
    }

    public static User getUserByName(String username)
    {
        for (User contact : contacts)
        {
            if (contact.getUsername().equals(username))
                return contact;
        }

        return null;
    }

    public static List<Document> getDocuments()
    {
        return documents;
    }

    public static List<Document> getDocumentsFromFolder(String folder)
    {
        ArrayList<Document> fromFolder = new ArrayList<>();

        for (Document doc : documents)
        {
            if (doc.getFolderName().equals(folder))
                fromFolder.add(doc);
        }

        return fromFolder;
    }

    public static List<Group> getGroups()
    {
        return groups;
    }

    public static List<Topic> getTopics()
    {
        return topics;
    }

    public static Metadata getMetadata()
    {
        return metadata;
    }
}
