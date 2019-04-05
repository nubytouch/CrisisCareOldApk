package com.nubytouch.crisiscare.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public class UtilsFile {

	public static String readFile(String filePath) {

		File fl = null;
		FileInputStream fin = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			fl = new File(filePath);
            if(fl.exists()) {
                fin = new FileInputStream(fl);

                reader = new BufferedReader(new InputStreamReader(fin, "utf8"));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

            }
		} catch (Exception ex) {
			Logger.getInstance().LogException(ex);
		} finally {

			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
			}

			try {
				if (fin != null) {
					fin.close();
				}
			} catch (Exception ex) {
			}

		}
		return sb.length() > 0 ? sb.toString() : null;
	}

    public static void saveFile(String filepath, String content){
        try {

            FileOutputStream out = new FileOutputStream(filepath,false);
        out.write(content.getBytes());
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void deleteFolder(String path) {
		File fileOrDirectory = new File(path);
		deleteFolderRecursive(fileOrDirectory);
	}

	public static void deleteFolderRecursive(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteFolderRecursive(child);

		fileOrDirectory.delete();

	}

	public static boolean unpackZip(Context context, String zipFilePath, String destinationPath) {
//		InputStream is;
//		ZipInputStream zis;
//        String filename=null;
//		try {
//
//			is = new FileInputStream(zipFilePath);
//			zis = new ZipInputStream(new BufferedInputStream(is));
//			ZipEntry ze;
//			byte[] buffer = new byte[1024];
//			int count;
//
//			File outputDir = new File(destinationPath);
//			if (!outputDir.exists()) {
//				outputDir.mkdirs();
//			}
//
//            if(BuildConfig.DEBUG) {
//                try {
//                    File extStore = Environment.getExternalStorageDirectory();
//                    File dest = new File(extStore.getAbsolutePath() + "/"+Configuration.getContentFilename());
//                    Timber.d("Debug mode : copy zip file : from " + zipFilePath + " to " + dest);
//                    copy(new File(zipFilePath), dest);
//                }catch(Exception e){}
//            }
//
//            Timber.d("Extracting zip file :"+zipFilePath);
//			while ((ze = zis.getNextEntry()) != null) {
//				filename = ze.getName();
//                Timber.d("Reading file "+filename);
//                // Need to create directories if not exists, or
//				// it will generate an Exception...
//				if (ze.isDirectory()) {
//					File fmd = new File(destinationPath + filename);
//					fmd.mkdirs();
//					continue;
//				}
//            File desFile = new File(destinationPath + filename);
//                File destFileDir = desFile.getParentFile();
//                if(!destFileDir.exists()){
//                    destFileDir.mkdirs();
//                }
//                FileOutputStream fout = new FileOutputStream(destinationPath + filename);
//
//				while ((count = zis.read(buffer)) != -1) {
//					fout.write(buffer, 0, count);
//				}
//
//				fout.close();
//				zis.closeEntry();
//			}
//
//			zis.close();
//            Timber.d("Extraction finished");
//		} catch (Exception e) {
//            String msg="";
//
//            if(filename!=null && filename.length()>0)
//            {
//                msg="Error during extraction... Last extracted file is "+filename;
//            }
//			Logger.getInstance().LogException(e,msg);
//            Mint.logException(e);
//            return false;
//		}

		return true;
	}

	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	public static boolean checkFileExist(String path) {
		return new File(path).exists();
	}

	public static boolean renameFolder(String from, String to) {
		File fileFrom = new File(from);
		File fileTo = new File(to);
        if(fileTo.exists()){
            UtilsFile.deleteFolderRecursive(fileTo);
        }
		return fileFrom.renameTo(fileTo);
	}

	public static boolean copy(File src, File dst) {

		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {

			inStream = new FileInputStream(src);

			outStream = new FileOutputStream(dst);
			inChannel = inStream.getChannel();
			outChannel = outStream.getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);

			return true;
		} catch (Exception e) {
			Logger.getInstance().LogException(e);
			return false;
		} finally {

			if (inChannel != null) {
				try {
					inChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (outChannel != null) {
				try {
					outChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
