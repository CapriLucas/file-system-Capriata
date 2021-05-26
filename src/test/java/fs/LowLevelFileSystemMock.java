package fs;

import java.util.function.Consumer;

public class LowLevelFileSystemMock implements LowLevelFileSystem {

   private boolean open = false;
   private String pathAbierto;
   private boolean leido;
   private Object bytesLeidos;

   public boolean seAbrio() {
      return open;
   }
   
   public boolean seCerro() {
      return !open;
   }

   public boolean seLeyo() {
      return leido;
   }
   
   public Object archivoQueAbrio() {
      return pathAbierto;
   }
   
   public Object bytesQueLeiste() {
      return bytesLeidos;
   }

   
   @Override
   public void closeFile(int fd) {
      this.open = false;
   }

   
   
   //========================
   
   @Override
   public int openFile(String path) {
      open = true;
      pathAbierto = path;
      return 0;
   }


   
   @Override
   public int syncReadFile(int fd, byte[] bufferBytes, int bufferStart, int bufferEnd) {
      leido = true;
      bytesLeidos = bufferBytes;
      return 5;
   }

   @Override
   public void syncWriteFile(int fd, byte[] bufferBytes, int bufferStart, int bufferEnd) {
      // TODO Auto-generated method stub

   }

   @Override
   public void asyncReadFile(int fd, byte[] bufferBytes, int bufferStart, int bufferEnd,
         Consumer<Integer> callback) {
      // TODO Auto-generated method stub

   }

   



}
