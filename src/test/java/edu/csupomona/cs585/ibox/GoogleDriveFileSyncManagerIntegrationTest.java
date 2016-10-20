package edu.csupomona.cs585.ibox;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class GoogleDriveFileSyncManagerIntegrationTest {

	public GoogleDriveFileSyncManager fSyncMan;	
	public File localFile;
	public String localFileTitle, localFilePath;
	
	@Before 
	public void setup() throws IOException {
		
		fSyncMan = new GoogleDriveFileSyncManager(GoogleDriveServiceProvider.get().getGoogleDriveClient());
     	localFilePath = "/Users/kennyiraheta/Desktop/CS 585/fun.txt";
     	localFile = new File(localFilePath);
		localFile.createNewFile();
		localFileTitle = localFile.getName();
	}
	
	 @Test
	 public void testAddFile() throws IOException{
		 System.out.println("***Test Add File*** ");
		 try{
			 fSyncMan.addFile(localFile);
			 String checkFileID = fSyncMan.getFileId(localFileTitle);
			 assertNotNull(checkFileID); // asserts that object A isn't null
		 } catch (IOException e) {
				System.out.println("An error occurred. Add File fail: " + e);
		 }
		System.out.format("File %s added successfully\n\n", localFileTitle);
	 }
	 
	 @Test
	 public void testUpdateFile() throws IOException{
		 System.out.println("***Test Update File*** ");
		 try{
			 String ammend = " add to file";
			 BufferedWriter wr = new BufferedWriter(new FileWriter(localFilePath, true));
			 wr.write(ammend);
			 wr.flush();
			 wr.close();
			 fSyncMan.updateFile(localFile);
			 String checkFileTitle = fSyncMan.getFileId(localFileTitle);
			 assertNotEquals(checkFileTitle, localFileTitle); // asserts that two objects are not equal
		 } catch (IOException e) {
				System.out.println("An error occurred. Update File fail: " + e);
		 }
		System.out.format("File %s updated successfully\n\n", localFileTitle);
	 }
	 
	 @Test
	 public void testDeleteFile() throws IOException{
		 System.out.println("***Test Delete File***");
		 try{
			 fSyncMan.deleteFile(localFile);
			 String checkFileTitle = fSyncMan.getFileId(localFileTitle);
			 assertNotEquals(checkFileTitle, localFileTitle); // asserts that two objects are not equal
		 } catch (IOException e) {
				System.out.println("An error occurred. Delete File fail: " + e);
		 }
		System.out.format("File %s deleted successfully\n", localFileTitle);
	 }
}