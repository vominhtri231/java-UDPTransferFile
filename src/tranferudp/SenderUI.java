/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tranferudp;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 *
 * @author ntnam
 */
public class SenderUI extends javax.swing.JFrame {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket senderSocket;
    private InetAddress  receiverAddress;
    
    File file;
    private static int  receivePort;
    /**
     * Creates new form SenderUI
     */
    public SenderUI() {
        initComponents();
    }
    
    
    public Container getPanel() {
    	return this.getContentPane();
    }


    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtReceiverPort = new javax.swing.JTextField();
        txtReceiverAddress = new javax.swing.JTextField();
        chooser = new JFileChooser();
        btSend = new javax.swing.JButton();
        btChoose=new JButton();
        txtSource=new javax.swing.JTextField();;
        jScrollPane1 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("address:");

        jLabel2.setText("port:");

        jLabel3.setText("source:");

        txtSource.setEditable(false);

        btSend.setText("Send");
        btSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btSendActionPerformed(evt);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        btChoose.setText("choose file");
        btChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	int returnValue = chooser.showOpenDialog(null);

        		if (returnValue == JFileChooser.APPROVE_OPTION) {
        			file = chooser.getSelectedFile();
        			txtSource.setText(file.getAbsolutePath());
        		}
            }
        });

        taDescription.setColumns(20);
        taDescription.setRows(5);
        jScrollPane1.setViewportView(taDescription);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtReceiverAddress)
                            .addComponent(txtReceiverPort)
                            .addComponent(btChoose)
                            .addComponent(txtSource, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))))
                			
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btSend)
                        .addGap(22, 22, 22))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(26, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtReceiverAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSend))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtReceiverPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                	.addComponent(btChoose)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }

    private void btSendActionPerformed(java.awt.event.ActionEvent evt) throws UnknownHostException {//GEN-FIRST:event_btSendActionPerformed
    	receiverAddress = InetAddress.getByName(txtReceiverAddress.getText());
        receivePort = Integer.parseInt(txtReceiverPort.getText());
        //File file = txtSource.getSelectedFile();
        connectServer();
        SendFider sendFider=new SendFider();
        sendFider.start();
    }
    
    
    private void connectServer() {
        try {
                senderSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    
    class SendFider extends Thread {
	    public void run() {
	        InetAddress inetAddress;
	        DatagramPacket sendPacket;
	
	        try {
	            if(file==null) return;
	            InputStream inputStream = new FileInputStream(file);
	            BufferedInputStream bis = new BufferedInputStream(inputStream);
	            inetAddress = receiverAddress;
	            byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];
	            
	            // get file size
	            long fileLength = file.length();
	            int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
	            int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);
	
	            // check last bytes of file
	            if (lastByteLength > 0) {
	                piecesOfFile++;
	            }
	
	            // split file into pieces and assign to fileBytess
	            byte[][] fileBytess = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
	            int count = 0;
	            while (bis.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
	                fileBytess[count++] = bytePart;
	                bytePart = new byte[PIECES_OF_FILE_SIZE];
	            }
	
	            // read file info
	            FileInfo fileInfo = new FileInfo();
	            fileInfo.setFilename(file.getName());
	            fileInfo.setFileSize(file.length());
	            fileInfo.setPiecesOfFile(piecesOfFile);
	            fileInfo.setLastByteLength(lastByteLength);
	 
	
	            // send file info
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream oos = new ObjectOutputStream(baos);
	            oos.writeObject(fileInfo);
	            sendPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
	                    inetAddress, receivePort);
	            senderSocket.send(sendPacket);
	
	            // send file content
	
	            taDescription.setText(taDescription.getText().trim()+"\n"+"Sending file...");
	            System.out.println("Sending file...");
	            // send pieces of file
	            int i = 0;
	            jProgressBar1.setMaximum((count-2));
	            jProgressBar1.setMinimum(0);
	            jProgressBar1.setVisible(true);
	            jProgressBar1.setValue(0);  
	            jProgressBar1.setStringPainted(true);
	            for (; i < (count - 1); i++) {
	                sendPacket = new DatagramPacket(fileBytess[i], PIECES_OF_FILE_SIZE,
	                        inetAddress, receivePort);
	                senderSocket.send(sendPacket);
	                jProgressBar1.setValue(i);
	                System.out.println(i);
	                final int temp=i;
	                EventQueue.invokeLater(new Runnable() {
	                	
	                    public void run() {
	                    	jProgressBar1.setValue(temp);
	                    }
	                 });
	                
	                waitMillisecond(100);
	                
	                
	                
	            }
	            // send last bytes of file
	            sendPacket = new DatagramPacket(fileBytess[count - 1], PIECES_OF_FILE_SIZE,
	                    inetAddress, receivePort);
	            senderSocket.send(sendPacket);
	            waitMillisecond(40);
	
	            // close stream
	            bis.close();
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        System.out.println("Sent.");
	        taDescription.setText(taDescription.getText().trim()+"\n"+"Sent.");
	    }
    
    }
    
    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SenderUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SenderUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SenderUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SenderUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SenderUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taDescription;
    private javax.swing.JTextField txtReceiverPort;
    private javax.swing.JTextField txtReceiverAddress;
    private javax.swing.JTextField txtSource;
    private javax.swing.JFileChooser chooser;
    private javax.swing.JButton btChoose;
    // End of variables declaration//GEN-END:variables
}
