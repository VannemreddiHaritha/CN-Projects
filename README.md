# CN-Projects
  Problem Statement:
 We need to implement a Chat application where two or more people can chat over internet. Here both client and server code should be included in one program as every person should be able to read the text sent by other person and write the text and send to the other person.
Implementation:
There is one program called chat.java. In this program, two threads are present. One is main thread and other is writing thread. In writing thread, the server code is implemented, which is same as code of server in project2. As we start the terminal, the server runs on a specific port and waits for the connection. Next, the main thread executes which has client code in it and asks for the port it wants to connect. If we start another terminal and give the port to which it wants to connect in both terminal, connection gets established between two terminals. Here, both the terminals act as client and server. Later read () and write() function runs in a loop so that continues messages are transferred between these two. Even files can be transferred between the two terminals.
Steps to execute:
1) Compile the program by giving below command. javac Chat.java
2) Run the program by passing port as a command line argument.
   3) Open the other terminal and execute it by passing another port.

 4) Now enter the port of one server in another as shown below.
  5) Now write the message in any of the terminal and that message will be reached by another terminal.
  6) Now we can even transfer files by passing the transfer "filename"

  References:
https://www.geeksforgeeks.org/multithreaded-servers-in-java/ Project 1 and Project 2 are used
