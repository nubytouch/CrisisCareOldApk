package com.nubytouch.crisiscare.ui.documents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.Document;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.ui.CCActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

public class DocumentsActivity extends CCActivity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_TITLE = "com.nubytouch.crisiscare.ui.documents.title";
    public static final String EXTRA_FOLDER = "com.nubytouch.crisiscare.ui.documents.folder";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_documents);

        if (getIntent().hasExtra(EXTRA_TITLE))
            new ToolbarDelegate(this).setup(false, getIntent().getStringExtra(EXTRA_TITLE));
        if (getIntent().hasExtra(EXTRA_FOLDER))
            new ToolbarDelegate(this).setup(false, getIntent().getStringExtra(EXTRA_FOLDER));
        else
            new ToolbarDelegate(this).setup(false);

        List<Object> data;

        if (getIntent().hasExtra(EXTRA_FOLDER))
        {
            String folder =  getIntent().getStringExtra(EXTRA_FOLDER);
            List<Document> docs = DataPackageManager.getDocumentsFromFolder(folder);

            data = new ArrayList<>();
            data.addAll(docs);
        }
        else
        {
            List<Document> docs = DataPackageManager.getDocuments();

            Collections.sort(docs, new Comparator<Document>() {
                @Override
                public int compare(Document lhs, Document rhs)
                {
                    if (lhs.getTitle() == null)
                        return 1;

                    if (rhs.getTitle() == null)
                        return -1;

                    return lhs.getTitle().compareTo(rhs.getTitle());
                }
            });

            data = new ArrayList<>();

            List<String> folders = new ArrayList<>();

            for (Document doc : docs)
            {
                if (doc.getFolderName() != null && !doc.getFolderName().isEmpty())
                {
                    if (!folders.contains(doc.getFolderName()))
                        folders.add(doc.getFolderName());
                }
                else
                {
                    data.add(doc);
                }
            }

            Collections.sort(folders, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs)
                {
                    if (lhs == null)
                        return 1;

                    if (rhs == null)
                        return -1;

                    return lhs.compareTo(rhs);
                }
            });

            for (int i = folders.size() - 1; i >= 0 ; i--)
            {
                data.add(0, new DocumentFolder(folders.get(i)));
            }
        }

        listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));

        DocumentAdapter adapter = new DocumentAdapter(data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Object object = listView.getItemAtPosition(position);

        if (object instanceof DocumentFolder)
            onFolderSelected((DocumentFolder) object);
        else
            onDocumentSelected((Document) object);
    }

    public void onFolderSelected(DocumentFolder folder)
    {
        Intent intent = new Intent(this, DocumentsActivity.class);
        intent.putExtra(EXTRA_FOLDER, folder.name);
        startActivity(intent);
    }

    public void onDocumentSelected(Document document)
    {
        try
        {
            Timber.d("onItemClick: " + document.getFilenamePath());
            Utils.openPDF(this, document.getTitle(), document.getFilenamePath());
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Unable to open file : " + e.getMessage() + e.getClass().getName(), Toast
                    .LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                onBackPressed();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
