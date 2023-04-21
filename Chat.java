import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.io.*;
public class Chat {
    Socket requestSocket;
    ObjectOutputStream out;         //stream write to the socket
    ObjectInputStream in;
    public void Chat() {}
    public void run(){
        try{
            Scanner obj = new Scanner(System.in);
            System.out.println("enter the port at which it want to connect");
            int port = Integer.parseInt(obj.nextLine());
            requestSocket = new Socket("localhost", port);
            System.out.println("Connected to localhost to "+port);
            //initialize inputStream and outputStream
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            while(true)
            {
                    read();
            }
        }

        catch (ConnectException e) {
            System.err.println("Connection refused. You need to initiate a server first.");

           System.exit(0);
        }

        catch (Exception e) {
            throw new RuntimeException(e);
        }


//
//       finally{
//            //Close connections
//           try{
//               in.close();
//               out.close();
//               //requestSocket.close();
//            }
//            catch(Exception ioException){
//               System.out.println("Invalid Port");
//             // ioException.printStackTrace();
//              run();
//            }
//        }

    }
    void downloadFileFromServer(String downlodedFilePath)
    {
        try {

            FileOutputStream fs = new FileOutputStream(downlodedFilePath);
            byte[] chunks = new byte[1000];
            long sizeOfFile = in.readLong(); //read the file size
            int chunkLength=in.read( chunks, 0 ,(int)Math.min(chunks.length, sizeOfFile)); // send first chunk
            while (sizeOfFile > 0 && (chunkLength!= -1)) {
                fs.write(chunks, 0, chunkLength); // write the file to local
                sizeOfFile =sizeOfFile- chunkLength; // Condition is reading to the end of file
                chunkLength=in.read( chunks, 0 ,(int)Math.min(chunks.length, sizeOfFile)); // send next chunk
            }
            System.out.println("\nFile is Received from the server"); // file is received to the server
            fs.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    //read the message
    void read() throws IOException, ClassNotFoundException {
        String output = (String) in.readObject();

        String[] arguments = output.split(" ");  // store the input in an array by splitting

        if (arguments[0].equals("transfer")) {
            System.out.print("\nRECEIVED MESSAGE"+": "+output+"\n");
            downloadFileFromServer("new" + arguments[1]); // receive file from client
        }
        else {
            System.out.print("\nRECEIVED MESSAGE"+": "+output+"\n");
        }
    }


    public static void main(String args[]) throws IOException, ClassNotFoundException {

          int port= Integer. parseInt(args[0]);
        //A writing thread is created
        System.out.println("My port is  "+port);
        WritingThreadHandler s = new WritingThreadHandler(port);
        s.start();
        Chat c = new Chat();
        c.run();
    }
}

class WritingThreadHandler extends Thread{
    int sPort;
    ObjectOutputStream out;         //stream write to the socket
    ObjectInputStream in;
    public WritingThreadHandler(int port){
        this.sPort=port;

    }
    @Override
    public void run() {
        try {

            ServerSocket sSocket=null;
                //serversocket used to listen on given port number
                sSocket = new ServerSocket(sPort, 10);
                //accept a connection from the client
                Socket connection = sSocket.accept();
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());

           while(true)
           {
                write();

            }

    }
        catch (BindException e)
        {
            System.out.println("Port already in use");
            System.exit(0);
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
//        finally{
//            //Close connections
//            try{
//              in.close();
//               out.close();
//           }
//           catch(Exception ioException){
//                ioException.printStackTrace();
//
//           }
//        }
    }
    void write()
    {
        try {
            Scanner obj = new Scanner(System.in);
            // System.out.print("\nenter the message\n");
            String message = obj.nextLine();

            String[] arguments = message.split(" ");

            if (arguments.length == 2 && arguments[0].equals("transfer") ) {
                //stream write the message
                out.writeObject(message);
                out.flush();
                File file = new File(arguments[1]);
                //if file name exists
                if(file.exists() && !file.isDirectory())  {
                    if( file.length()<=0){System.out.println("file is empty.....");}
                    else {
                        String filename = arguments[1];
                        uploadFiletoServer(filename);

                    }
                }
                else{
                    //if file does not exist in  the path
                    System.out.println("filename does not exist in the path");
                }
            }
            else {
                //stream write the message
                out.writeObject(message);
                out.flush();
            }
           // out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    void uploadFiletoServer(String filename)
    {
        try {
            //create a file object
            File myfileObj = new File(filename);
            FileInputStream fs = new FileInputStream(myfileObj); //create a file input stream object
            if(myfileObj.length()>0) {
                out.writeLong(myfileObj.length()); // Send file size to the server as stream
                out.flush();
                byte[] chunks = new byte[1000];  // Break the file into chunks
                int chunkLength = fs.read(chunks); // read first chunk
                while (chunkLength != -1) {
                    out.write(chunks, 0, chunkLength); // Send the chunk to Server Socket
                    out.flush(); //clear the stream
                    chunkLength = fs.read(chunks);// now move the file pointer to next chunk
                }
            }
            else {System.out.println("file is empty");}
            fs.close();
        }
        catch(FileNotFoundException filenotfound){
            System.err.println("file does not exist in the given path");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }




}