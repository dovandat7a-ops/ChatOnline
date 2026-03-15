
package chat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientUI extends JFrame {

    JTextArea chatArea;
    JTextField txtMessage;
    JButton btnSend;

    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public ClientUI(){

        setTitle("Chat Online - CLIENT");
        setSize(500,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        txtMessage = new JTextField();
        btnSend = new JButton("Send");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(txtMessage,BorderLayout.CENTER);
        bottom.add(btnSend,BorderLayout.EAST);

        add(new JScrollPane(chatArea),BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);

        btnSend.addActionListener(e->sendMessage());

        setVisible(true);

        connectServer();

    }

    void connectServer(){

        try{

            socket = new Socket("localhost",5000);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(),true);

            new Thread(() -> {

                try{

                    String msg;

                    while((msg = in.readLine())!=null){

                        chatArea.append("Host: "+msg+"\n");

                    }

                }catch(Exception e){}

            }).start();

        }catch(Exception e){

            chatArea.append("Cannot connect server\n");

        }

    }

    void sendMessage(){

        String msg = txtMessage.getText();

        chatArea.append("Me: "+msg+"\n");

        out.println(msg);

        txtMessage.setText("");

    }

    public static void main(String[] args) {

        new ClientUI();

    }

}
