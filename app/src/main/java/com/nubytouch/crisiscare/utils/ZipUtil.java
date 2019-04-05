package com.nubytouch.crisiscare.utils;

import timber.log.Timber;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil
{
    private static final String TAG = "ZipUtil";

    public static boolean unzipFile(String zipFilePath, String destinationPath)
    {
        InputStream    is;
        ZipInputStream zis;
        String         filename = null;

        try
        {
            is = new FileInputStream(zipFilePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[]   buffer = new byte[1024];
            int      count;

            File outputDir = new File(destinationPath);
            if (!outputDir.exists())
            {
                outputDir.mkdirs();
            }

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();
                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory())
                {
                    File fmd = new File(destinationPath + filename);
                    fmd.mkdirs();
                    continue;
                }

                File desFile     = new File(destinationPath + filename);
                File destFileDir = desFile.getParentFile();
                if (!destFileDir.exists())
                {
                    destFileDir.mkdirs();
                }

                FileOutputStream fout = new FileOutputStream(destinationPath + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
            Timber.d("Extraction finished");
        }
        catch (Exception e)
        {
            String msg = "";

            if (filename != null && filename.length() > 0)
            {
                msg = "Error during extraction... Last extracted file is " + filename;
            }
            Timber.d("unpackZip: " + msg + " " + e);

            return false;
        }

        return true;
    }
}
