
package chat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

public class ServerUI extends JFrame {

    JTextArea chatArea;
    JTextField txtMessage, txtSearch;
    JButton btnSend, btnEdit, btnDelete, btnSearch;

    JTable table;
    DefaultTableModel model;

    ServerSocket server;
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public ServerUI() {

        setTitle("Chat Online - HOST SERVER");
        setSize(700,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        model = new DefaultTableModel(new String[]{"ID","Message"},0);
        table = new JTable(model);

        JPanel bottom = new JPanel(new GridLayout(2,1));

        JPanel p1 = new JPanel(new BorderLayout());
        txtMessage = new JTextField();
        btnSend = new JButton("Send");

        p1.add(txtMessage,BorderLayout.CENTER);
        p1.add(btnSend,BorderLayout.EAST);

        JPanel p2 = new JPanel();

        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        txtSearch = new JTextField(15);
        btnSearch = new JButton("Search");

        p2.add(btnEdit);
        p2.add(btnDelete);
        p2.add(txtSearch);
        p2.add(btnSearch);

        bottom.add(p1);
        bottom.add(p2);

        add(new JScrollPane(chatArea),BorderLayout.CENTER);
        add(new JScrollPane(table),BorderLayout.EAST);
        add(bottom,BorderLayout.SOUTH);

        btnSend.addActionListener(e->sendMessage());
        btnEdit.addActionListener(e->editMessage());
        btnDelete.addActionListener(e->deleteMessage());
        btnSearch.addActionListener(e->searchMessage());

        setVisible(true);

        startServer();
    }

    void startServer(){

        new Thread(() -> {

            try{

                server = new ServerSocket(5000);
                chatArea.append("Server started...\n");

                socket = server.accept();
                chatArea.append("Client connected!\n");

                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                out = new PrintWriter(socket.getOutputStream(),true);

                String msg;

                while((msg = in.readLine())!=null){

                    chatArea.append("Client: "+msg+"\n");

                    int id = model.getRowCount()+1;

                    model.addRow(new Object[]{id,msg});

                }

            }catch(Exception e){
                chatArea.append("Error: "+e.getMessage());
            }

        }).start();

    }

    void sendMessage(){

        String msg = txtMessage.getText();

        chatArea.append("Host: "+msg+"\n");

        out.println(msg);

        int id = model.getRowCount()+1;

        model.addRow(new Object[]{id,msg});

        txtMessage.setText("");

    }

    void editMessage(){

        int row = table.getSelectedRow();

        if(row>=0){

            String newMsg = JOptionPane.showInputDialog("Edit message");

            model.setValueAt(newMsg,row,1);

        }

    }

    void deleteMessage(){

        int row = table.getSelectedRow();

        if(row>=0){

            model.removeRow(row);

        }

    }

    void searchMessage(){

        String keyword = txtSearch.getText();

        for(int i=0;i<table.getRowCount();i++){

            String msg = table.getValueAt(i,1).toString();

            if(msg.contains(keyword)){

                table.setRowSelectionInterval(i,i);
                break;

            }

        }

    }

    public static void main(String[] args) {

        new ServerUI();

    }

}
