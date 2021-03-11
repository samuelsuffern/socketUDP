
import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;
import java.util.*;


public class plus{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    
    public static void main (String[] args) {
        int correcto =compruebaArgs(args); // correcto = 1, incorrecto = 2
        switch (correcto) {
            case 1:
                
                createRequest(args);
            break;
            case 2:
            default:
                System.out.println("Error en parámetros de entrada. Sintaxis correcta: IP_servidor puerto_destino M timeout");
         }
    }


    private static void createRequest(String[] args) {
        try {
            InetAddress hostServidor = InetAddress.getByName(args[0]);
            int puertoDestino = Integer.parseInt(args[1]);
            int enteroM = Integer.parseInt(args[2]);
            int timeout = Integer.parseInt(args[3]); // leo segundos
            timeout = timeout*1000; // transformo a ms
            String s1= "Envio peticion al host:" + hostServidor + ":" + puertoDestino + ". Mensaje M = "+enteroM;
            System.out.println(s1);
            ByteBuffer bb = ByteBuffer.allocate(4); // capacidad de nuestro buffer es 4 bytes (entero)
            bb.asIntBuffer().put(enteroM);
            //System.out.println("Original ByteBuffer:  "+ Arrays.toString(bb.array())); 
            //System.out.println("Si extraigo del bb : "+ bb.getInt() );
            DatagramSocket socketUDP = new DatagramSocket();
           
            // Construimos un datagrama para enviar el mensaje al servidor
            DatagramPacket peticion =new DatagramPacket(bb.array(), bb.array().length, hostServidor,puertoDestino);
      
            // Enviamos el datagrama
            socketUDP.send(peticion);
            socketUDP.setSoTimeout(timeout);

            //System.out.println("Mensaje enviado : "+ Arrays.toString(bb.array())+ "al servidor : "+hostServidor);
            // Construimos el DatagramPacket que contendrá la respuesta
            byte[] bufer = new byte[6];
            DatagramPacket respuesta =new DatagramPacket(bufer, bufer.length);
           while(true){
               try {
                   
                   socketUDP.receive(respuesta);
                   ByteBuffer bbR = ByteBuffer.wrap(respuesta.getData());
                   int respInt = bbR.getInt();
                   // Enviamos la respuesta del servidor a la salida estandar
                   String s = "Respuseta del servidor (M + N) = "+ respInt;
                   System.out.println(s);
             
                   socketUDP.close();
               } catch (SocketTimeoutException e) {
                    int ts = timeout/1000;
                    System.out.println("No hay respuesta del servidor "+hostServidor+":"+ puertoDestino+" tras "+ts+" segundos");
                    socketUDP.close();
                }    
           }
            // Cerramos el socket
      
          } catch (SocketException e) {
            } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
          }
    }


    private static int compruebaArgs(String[] args) {
        if(args.length == 4){
        return 1;
        }else{
            return 2;
        }
    }

}