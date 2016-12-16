/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author kaushal28
 */
public class receiver {
    
    ServerSocket serverSocket;
    
    public void receiveProcess(){
        try{
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(5004));
            
            //block until some connection request comes
            
            System.out.println("Waiting...");
            
            Socket socket = serverSocket.accept();
            System.out.println("Connection Accepted!!");
            
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            
            //read the number of files from sender or the client.
            int number = dis.readInt();
            ArrayList<File> files = new ArrayList<>();
            System.out.println("Number of files to be received: "+number );
            
            ArrayList<Long> fileSize = new ArrayList<>(number);
            
            //now read each file size to receive it corretly.
            for(int i=0;i<number;i++){
                long eachFileSize = dis.readLong();
                System.out.println("size of file "+(i)+": "+eachFileSize);
                fileSize.add(eachFileSize);
            }
            
            //now read each file names from sender.
            
            for(int i=0;i<number;i++){
                File file = new File(dis.readUTF());
                files.add(file);
            }
            
            int n;
            byte[] buf = new byte[4092];
            
            //Create a directory to save received file(s).
            
            new File("/home/kaushal28/Desktop/File").mkdirs();
            
            //outer loop exe. once for each file.
            for(int i=0;i<files.size();i++){
                
                System.out.println("Receiving file: "+ files.get(i).getName());
                
                //create a new file output for each file.
                FileOutputStream fos = new FileOutputStream("/home/kaushal28/Desktop/File/" + files.get(i).getName());
                
                //now read the file...
                while (fileSize.get(i) > 0 && (n = dis.read(buf, 0, (int)Math.min(buf.length, fileSize.get(i)))) != -1)
                {
                    fos.write(buf,0,n);
                    long x = fileSize.get(i);
                    x = x-n;
                    fileSize.set(i,x);
                }
                fos.close();                
                
                
            }        
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        
        System.out.println("End of receiver.");
        
        try{
            if(!serverSocket.isClosed()){
                serverSocket.close();
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
        }        
    }
}
