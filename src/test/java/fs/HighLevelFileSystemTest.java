package fs;

import fs.exceptions.CanNotOpenFileException;
import fs.exceptions.CanNotReadFileException;
import fs.exceptions.ClosedFileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class HighLevelFileSystemTest {

  private LowLevelFileSystem lowLevelFileSystem;
  private HighLevelFileSystem fileSystem;

  @BeforeEach
  void initFileSystem() {
    lowLevelFileSystem = mock(LowLevelFileSystem.class);
    fileSystem = new HighLevelFileSystem(lowLevelFileSystem);
  }

  @Test
  void sePuedeAbrirUnArchivo() {
    when(lowLevelFileSystem.openFile("unArchivo.txt")).thenReturn(42);
    File file = fileSystem.openFile("unArchivo.txt");
    Assertions.assertEquals(file.getDescriptor(), 42);
  }

  @Test
  void siLaAperturaFallaUnaExcepcionEsLanzada() {
    when(lowLevelFileSystem.openFile("otroArchivo.txt")).thenReturn(-1);
    Assertions.assertThrows(CanNotOpenFileException.class, () -> fileSystem.openFile("otroArchivo.txt"));
  }

  @Test
  void sePuedeLeerSincronicamenteUnArchivoCuandoNoHayNadaParaLeer() {
    Buffer buffer = new Buffer(100);

    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    when(lowLevelFileSystem.syncReadFile(42, buffer.getBytes(), 0, 100)).thenReturn(0);

    File file = fileSystem.openFile("ejemplo.txt");
    file.syncRead(buffer);

    Assertions.assertEquals(0, buffer.getStart());
    Assertions.assertEquals(-1, buffer.getEnd());
    Assertions.assertEquals(0, buffer.getCurrentSize());
  }

  @Test
  void sePuedeLeerSincronicamenteUnArchivoCuandoHayAlgoParaLeer() {
    Buffer buffer = new Buffer(10);

    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    when(lowLevelFileSystem.syncReadFile(42, buffer.getBytes(), 0, 9)).thenAnswer(invocation -> {
      Arrays.fill(buffer.getBytes(), 0, 4, (byte) 3);
      return 4;
    });

    File file = fileSystem.openFile("ejemplo.txt");
    file.syncRead(buffer);

    Assertions.assertEquals(0, buffer.getStart());
    Assertions.assertEquals(3, buffer.getEnd());
    Assertions.assertEquals(4, buffer.getCurrentSize());
    Assertions.assertArrayEquals(buffer.getBytes(), new byte[]{3, 3, 3, 3, 0, 0, 0, 0, 0, 0});
  }

  @Test
  void siLaLecturaSincronicaFallaUnaExcepciónEsLanzada() {
    Buffer buffer = new Buffer(10);

    when(lowLevelFileSystem.openFile("archivoMalito.txt")).thenReturn(13);
    when(lowLevelFileSystem.syncReadFile(anyInt(), any(), anyInt(), anyInt())).thenReturn(-1);

    File file = fileSystem.openFile("archivoMalito.txt");

    Assertions.assertThrows(CanNotReadFileException.class, () -> file.syncRead(buffer));
  }

  @Test
  void sePuedeEscribirSincronicamenteUnArchivoCuandoHayNoHayNadaParaEscribir() {
    Buffer buffer = new Buffer(10);

    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    when(lowLevelFileSystem.syncReadFile(42, buffer.getBytes(), 0, 9)).thenReturn(0);
    File file = fileSystem.openFile("ejemplo.txt");

    Buffer bufferEscribir = new Buffer(10);
    file.syncWrite(bufferEscribir);

    file.syncRead(buffer);

    Assertions.assertEquals(0, buffer.getStart());
    Assertions.assertEquals(-1, buffer.getEnd());
    Assertions.assertEquals(0, buffer.getCurrentSize());
  }

  @Test
  void sePuedeEscribirSincronicamenteUnArchivoCuandoHayAlgoParaEscribir() {
    Buffer buffer = new Buffer(10);

    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    when(lowLevelFileSystem.syncReadFile(42, buffer.getBytes(), 0, 9)).thenAnswer(invocation -> {
      Arrays.fill(buffer.getBytes(), 0, 4, (byte) 3);
      return 4;
    });

    File file = fileSystem.openFile("ejemplo.txt");

    Buffer bufferEscribir = new Buffer(10);
    Arrays.fill(bufferEscribir.getBytes(), 0, 4, (byte) 3);
    file.syncWrite(bufferEscribir);

    file.syncRead(buffer);

    Assertions.assertEquals(0, buffer.getStart());
    Assertions.assertEquals(3, buffer.getEnd());
    Assertions.assertEquals(4, buffer.getCurrentSize());
    Assertions.assertArrayEquals(buffer.getBytes(), bufferEscribir.getBytes());
  }

  @Test
  void sePuedeLeerAsincronicamenteUnArchivo() {

    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    doAnswer(invocation -> {
      Consumer<Integer> callback = invocation.getArgument(4);
      callback.accept(4); // Le marcamos 4 bytes leidos
      return 0;
    }).when(lowLevelFileSystem).asyncReadFile(eq(42), any(byte[].class), anyInt(), anyInt(), any(Consumer.class));

    File file = fileSystem.openFile("ejemplo.txt");
    file.asyncRead(buffer1 -> {
      assertEquals(buffer1.getCurrentSize(), 4);
    }, 6);
  }

  @Test
  void sePuedeEscribirAsincronicamenteUnArchivo() {
    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    doAnswer(invocation -> {
      Runnable callback = invocation.getArgument(4);
      callback.run();
      return 0;
    }).when(lowLevelFileSystem).asyncWriteFile(eq(42), any(byte[].class), anyInt(), anyInt(), any(Runnable.class));

    File file = fileSystem.openFile("ejemplo.txt");
    file.asyncWrite(buffer1 -> {
      assertEquals(3, 3);
    }, new Buffer(10));
  }

  @Test
  void sePuedeCerrarUnArchivo() {
    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    File file = fileSystem.openFile("ejemplo.txt");
    file.close();
    assertTrue(file.isClosed());
  }

  @Test
  void seChequeaSiEstaCerradoPrevioACualqAccion(){
    when(lowLevelFileSystem.openFile("ejemplo.txt")).thenReturn(42);
    File file = fileSystem.openFile("ejemplo.txt");
    file.close();
    Buffer buffer = new Buffer(10);
    Assertions.assertThrows(ClosedFileException.class, () -> file.syncRead(buffer));
  }
}

// Cerrar archivo y chequear si escribe o lee