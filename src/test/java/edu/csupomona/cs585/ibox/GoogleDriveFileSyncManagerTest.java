package edu.csupomona.cs585.ibox;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;

import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.*;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.http.FileContent;


public class GoogleDriveFileSyncManagerTest {
	
	public Drive mockedService;
	public GoogleDriveFileSyncManager fSyncMan;
	public File file;
	public Files files;
	public FileList fileList;
	public Files.Insert mockInsert;
	public Files.Update mockUpdate;
	public Files.Delete mockDelete;
	public List request;
	public java.io.File mockedLocalFile;
	public java.util.List<File> ListF;
	public String fName = "testFile", idName = "2099";	
	
	@Before // setup method creates objects & mock data needed for tests & runs before test methods execute
	public void setup() throws IOException {
		mockedService = mock(Drive.class); // mocked Drive object
		fSyncMan = new GoogleDriveFileSyncManager(mockedService);
		
		// set mock data 
		mockedLocalFile = mock(java.io.File.class);
		mockInsert = mock(Files.Insert.class);
		mockUpdate = mock(Files.Update.class);
		mockDelete = mock(Delete.class);
		request = mock(List.class);	
		files = mock(Files.class); 
		
		file = new File();
		file.setTitle(fName);
		file.setId(idName);
		fileList = new FileList();
		ListF = new java.util.ArrayList<File>();
		ListF.add(file);
		fileList.setItems(ListF);
		
		// stubbing -- mock for getFileId() for update and delete
		when(mockedLocalFile.getName()).thenReturn(fName);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
	}
	
	@Test
	public void testAddFile() throws IOException{
		//Insert a file
		// stubbing -- mock for addFile()
		when(mockedService.files()).thenReturn(files);
		when(files.insert(Mockito.isA(File.class), Mockito.isA(FileContent.class))).thenReturn(mockInsert); 
		when(mockInsert.execute()).thenReturn(file);	
		fSyncMan.addFile(mockedLocalFile);
		
		// verify method is called then assert
		verify(mockInsert).execute();
		assertEquals(idName, file.getId());	
	}

	@Test
	public void testUpdateFile() throws IOException{
		// stubbing -- mock for updateFile()
		when(mockedService.files()).thenReturn(files);
		when(files.update(isA(String.class), isA(File.class), isA(FileContent.class))).thenReturn(mockUpdate);
		when(mockUpdate.execute()).thenReturn(file);	
		fSyncMan.updateFile(mockedLocalFile);

		// verify method is called then assert
		verify(mockUpdate).execute();
		assertEquals(idName, file.getId());	
	}
	
	@Test
	public void testDeleteFile() throws IOException {
		// stubbing -- mock for deleteFile()
		when(mockedService.files()).thenReturn(files);
		when(files.delete(isA(String.class))).thenReturn(mockDelete);
		when(mockDelete.execute()).thenReturn(null);
		fSyncMan.deleteFile(mockedLocalFile);
		
		// verify method is called
		verify(mockDelete).execute();
	}
	
	@Test
	public void testGetFileId() throws IOException{
		// stubbing -- mock for getFileId()
		when(mockedService.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		// assert
		assertEquals(idName, fSyncMan.getFileId(fName));	
	}
}