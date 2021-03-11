
import java.net.*;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.*;

public class plusd {
    private Socket clientSocket;
    private ServerSocket serverSocket;

    public static void main (String[] args) {
        int correcto =compruebaArgs(args); // correcto = 1, incorrecto = 2
        switch (correcto) {
            case 1:
                
                createServer(args);
            break;
            case 2:
            default:
                System.out.println("Error en parámetros de entrada. Sintaxis correcta: puerto N");
         }
    }

    private static void createServer(String[] args) {
        try{
            int serverPort = Integer.parseInt(args[0]);
            int mensajeN = Integer.parseInt(args[1]);
            DatagramSocket socketUDP = new DatagramSocket(serverPort);
            
            
            while (true) {
                ByteBuffer bb = ByteBuffer.allocate(4);
                // ----------------- RECIBIR peticiones --------------------- //
                DatagramPacket peticion = new DatagramPacket(bb.array(), bb.array().length);

                // ----------------- LEER peticiones --------------------- //
                socketUDP.receive(peticion);
                int mensajeM = bb.getInt();
                System.out.println();
                // ----------------- Utilizo un ByteBuffer para recoger la información  --------------------- //
                 bb.wrap(peticion.getData()); // El tamaño serán 4Bytes
                System.out.println("Recibo peticion del host: " +peticion.getAddress() + ":"+peticion.getPort()+ ". Mensaje M = " + mensajeM);
                int resultado = mensajeM + mensajeN;
                String s = "Respuseta del servidor (N + M): " + mensajeN + " + " + mensajeM + " = " + resultado;
                System.out.println(s);
                // ----------------- ENVIAR DATA DEL CLIENTE --------------------- //
                // para enviar datos, tenemos que construir un paquete, utilizo un bytebuffer para añadir la información al paquete

                ByteBuffer bbS= ByteBuffer.allocate(4);
                bbS.putInt(resultado);
                DatagramPacket respuesta = new DatagramPacket(bbS.array(), bbS.array().length, peticion.getAddress(), peticion.getPort());
                socketUDP.send(respuesta);
            }
        }catch (BufferOverflowException e) { 
  
            System.out.println("buffer's current position "
                               + "is not smaller than its limit"); 
            System.out.println("Exception throws : " + e); 
        }  catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }

    }

    private static int compruebaArgs(String[] args) {
        if(args.length == 2){
            return 1;
            }else{
                return 2;
            }
    }
}
