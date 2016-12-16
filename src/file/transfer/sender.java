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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author kaushal28
 */
public class sender {
  
    ArrayList<File> files;
    String FILE_PATH = "/home/kaushal28/Desktop/date.html";
    Socket socket;
    
    public void sendProcess(String desired){
        
        files = new ArrayList<>();
        
        files.add(new File(FILE_PATH));
        
        //now here you can get IP addresses of available devices.
        //but assuming one divice is available and assuming its IP address.
        
        try{
            socket = new Socket(desired,5004);
            
            System.out.println("Connecting...");
        
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            
            System.out.println("Number of files to be send:" + files.size());
            
            //so write it to let this know the receiver:
            
            dos.writeInt(files.size());
            dos.flush();
            
            //now write each file size to the receiver.
            
            for(int i=0;i<files.size();i++){
                int fileSize = Integer.parseInt(String.valueOf(files.get(i).length()));
                dos.writeLong(fileSize);
                dos.flush();
            }
            
            
            //now write file names...
            
            for(int i=0;i<files.size();i++){
                dos.writeUTF(files.get(i).getName());
                dos.flush();
            }
            
            //buffer for writing...
            int n;
            byte[] buf = new byte[4092];
            
            //outer loop executes once for each file.
            
            for(int i=0;i<files.size();i++){
                System.out.println(files.get(i).getName());
                //create a new fileinputstream for each file.
                FileInputStream fis = new FileInputStream(files.get(i));
                
                //write this file to dos...
                
                while((n = fis.read(buf))!=-1){
                    dos.write(buf,0,n);
                    dos.flush();
                }
                
            }
           
            
            dos.close();
            
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        
        //now connecting to device provided.     
        System.out.println("End of Sender...");
        try{
            if(socket!=null){
                if(!socket.isClosed()){
                    socket.close();
                }                
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
        }   
    }
}
