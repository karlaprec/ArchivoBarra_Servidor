
package archivoservidorporcentaje;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ArchivoServidorPorcentaje {

    static int nsocket;

    public static void main(String[] args) {
        try {
            nsocket = Integer.parseInt(args[args.length - 1]);
        } catch (Exception e) {
            System.out.println("INGRESE SOCKET DE NUEVO");
            System.exit(0);
        }

        ServerSocket server;
        Socket connection = null;
        try {
            server = new ServerSocket(nsocket);
            while (true) {
                try {
                    connection = server.accept();
                } catch (Exception e) {
                    System.err.println(e);
                }
                BufferedReader lector = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                PrintWriter escritor = new PrintWriter(connection.getOutputStream(), true);
                String entrada = "";
                if ((entrada = lector.readLine()) != null) {
                    System.out.println(entrada);
                    SEND(entrada, connection, escritor);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void SEND(String RUTA, Socket client, PrintWriter escritor) throws FileNotFoundException, IOException {
        final File localFile = new File(RUTA);
        long size = localFile.length();
        escritor.println(size);
        if (localFile.exists()) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(localFile));
            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            byte[] byteArray;
            /* PRIMERO SE ENVIA EL NOMBRE DEL FICHERO */
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(localFile.getName());
            /* LUEGO ENVIAMOS EL FICGERO */
            byteArray = new byte[8192];
            int in;
            while ((in = bis.read(byteArray)) != -1) {
                bos.write(byteArray, 0, in);
            }
            bis.close();
            bos.close();
        } else {
            System.out.println("ARCHIVO NO ENCONTRADO");
        }
    }

}


