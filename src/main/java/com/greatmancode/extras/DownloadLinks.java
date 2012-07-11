package com.greatmancode.extras;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadLinks
{
	public static void download(File folder)
	{
		URL url; //represents the location of the file we want to dl.
        URLConnection con;  // represents a connection to the url we want to dl.
        DataInputStream dis;  // input stream that will read data from the file.
        byte[] fileData;  //byte aray used to hold data from downloaded file.
        try {
            url = new URL("https://github.com/greatman/OKB/raw/master/forumlistener.zip");
            con = url.openConnection(); // open the url connection.
            dis = new DataInputStream(con.getInputStream()); // get a data stream from the url connection.
            fileData = new byte[con.getContentLength()]; // determine how many byes the file size is and make array big enough to hold the data
            for (int x = 0; x < fileData.length; x++) { // fill byte array with bytes from the data input stream
                fileData[x] = dis.readByte();
            }
            dis.close(); // close the data input stream
            
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(
                new ByteArrayInputStream(fileData));

            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) 
            { 
                //for each entry to be extracted
                String entryName = zipentry.getName();
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(folder.getPath() + entryName);
                String directory = newFile.getParent();
                
                if(directory == null)
                {
                    if(newFile.isDirectory())
                        break;
                }
                
                fileoutputstream = new FileOutputStream(
                		folder.getPath() + File.separator + entryName);             
                byte[] buf = new byte[1024];
                while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
                    fileoutputstream.write(buf, 0, n);

                fileoutputstream.close(); 
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();

            }//while

            zipinputstream.close();
        }
        catch(MalformedURLException m) {
            System.out.println(m);
        }
        catch(IOException io) {
            System.out.println(io);
        }
	}
}
