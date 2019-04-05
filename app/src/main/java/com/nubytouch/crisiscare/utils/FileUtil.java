package com.nubytouch.crisiscare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtil
{
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
//			Logger.getInstance().LogException(ex);
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
}
