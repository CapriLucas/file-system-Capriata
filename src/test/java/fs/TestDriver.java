package fs;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDriver {
   
   private HighLevelFileSystem highLevelFileSystem;
   private LowLevelFileSystemMock lowLevelFileSystemMock;

   @Test
   public void foo1() {
      lowLevelFileSystemMock = new LowLevelFileSystemMock();
      highLevelFileSystem =
            new HighLevelFileSystem(lowLevelFileSystemMock);
      
      highLevelFileSystem.open("C:/foo/bar");
      
      assertTrue(lowLevelFileSystemMock.seAbrio());
   }
   
   @Test
   public void foo2()  {
      lowLevelFileSystemMock = new LowLevelFileSystemMock();
      highLevelFileSystem =
            new HighLevelFileSystem(lowLevelFileSystemMock);
      
      highLevelFileSystem.open("C:/foo/bar");
      
      assertEquals(lowLevelFileSystemMock.archivoQueAbrio(), 
            "C:/foo/bar");
   }
   
   @Test
   public void foo3()  {
      lowLevelFileSystemMock = new LowLevelFileSystemMock();
      highLevelFileSystem =
            new HighLevelFileSystem(lowLevelFileSystemMock);
      OpenedFile file = highLevelFileSystem.open("C:/foo/bar");
      
      file.close();
      
      assertTrue(lowLevelFileSystemMock.seCerro());
   }
   
   @Test
   public void foo4()  {
      lowLevelFileSystemMock = new LowLevelFileSystemMock();
      highLevelFileSystem =
            new HighLevelFileSystem(lowLevelFileSystemMock);
      OpenedFile file = highLevelFileSystem.open("C:/foo/bar");
      
      Buffer buffer = new Buffer(100);
      file.read(buffer);
      
      assertTrue(lowLevelFileSystemMock.seLeyo());
   }
   
   @Test
   public void foo5()  {
      lowLevelFileSystemMock = new LowLevelFileSystemMock();
      highLevelFileSystem =
            new HighLevelFileSystem(lowLevelFileSystemMock);
      OpenedFile file = highLevelFileSystem.open("C:/foo/bar");
      
      Buffer buffer = new Buffer(100);
      file.read(buffer);
      
      assertEquals(
            lowLevelFileSystemMock.bytesQueLeiste(),
            buffer.getBytes());
   }
   
   @Test
   public void bar6()  {
      lowLevelFileSystemMock = new LowLevelFileSystemMock();
      highLevelFileSystem =
            new HighLevelFileSystem(lowLevelFileSystemMock);
      OpenedFile file = highLevelFileSystem.open("C:/foo/bar");
      
      Buffer buffer = new Buffer(100);
      file.read(buffer);
      
      assertEquals(4, buffer.getEnd());
   }
   

   
   @Test
   public void bar7()  {
      lowLevelFileSystemMock = new LowLevelFileSystemMock();
      highLevelFileSystem =
            new HighLevelFileSystem(lowLevelFileSystemMock);
      OpenedFile file = highLevelFileSystem.open("C:/foo/bar");
      
      file.asyncRead(buffer -> {
         assertEquals(4, buffer.getEnd());   
      });
   }

   
   
}
