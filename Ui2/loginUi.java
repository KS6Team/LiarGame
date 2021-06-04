
import com.sun.org.apache.xpath.internal.operations.Bool;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.*;
import java.util.zip.CheckedOutputStream;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

class LoginUi extends JFrame implements ActionListener {

    Client c;
    String id, ip, port;
    JTextField idTf, ipTf, portTf;
    JLabel idLb, ipLb, portLb, imgLb;
    JButton serverBtn, clientBTn, endBtn;
    Container cp;
    JPanel tfP, btnP, radioP, inputP, p1;
    ImageIcon titleIcon, backIcon;
    Thread th = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serverBtn.setVisible(true);
            clientBTn.setVisible(true);
            endBtn.setVisible(true);
            System.out.println("��������");
            interrupted();
        }
    });

    LoginUi() {
        init();
    }

    void init() { //�α���ȭ�� ����
        serverBtn = new JButton("���� �����ϱ�");
        clientBTn = new JButton("���� �����ϱ�");
        endBtn = new JButton("�����ϱ�");
        p1 = new ImagePanel("back2.gif");
        setContentPane(p1);
        p1.add(serverBtn, BorderLayout.EAST);
        p1.add(clientBTn, BorderLayout.EAST);
        p1.add(endBtn, BorderLayout.EAST);
        setUi();
        serverBtn.setBounds(580, 320, 150, 40);
        serverBtn.setVisible(false);
        clientBTn.setBounds(580, 400, 150, 40);
        clientBTn.setVisible(false);
        endBtn.setBounds(580, 480, 150, 40);
        endBtn.setVisible(false);
        action();
        th.start();
    }

    void setUi() {
        setTitle("���̾����");
        setVisible(true);
        setSize(805, 630);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {                                      //�α���ȭ�� ��ư �׼Ǹ�����
        if (e.getSource().equals(serverBtn)) {
            LoginDialog lD = new LoginDialog(this, this, "���� �����ϱ�");
            ip = lD.ipTf.getText();
            port = lD.portTf.getText();

        }
        if (e.getSource().equals(clientBTn)) {
            LoginDialog lD = new LoginDialog(this, this, "���� �����ϱ�");
            ip = lD.ipTf.getText();
            port = lD.portTf.getText();
        }
        if (e.getSource().equals(endBtn)) {
            System.exit(0);
        }
    }

    void action() { //�α���ȭ�� ��ư �׼Ǹ����� �Է�
        serverBtn.addActionListener(this);
        clientBTn.addActionListener(this);
        endBtn.addActionListener(this);
    }

    void reopen() {
        setVisible(true);

    }

    public static void main(String[] args) {
        new LoginUi();
    }
}                                                                                           //LoginUi

class ImagePanel extends JPanel {

    Image image;

    public ImagePanel(String str) {                                                  //�α���ȭ�� ����̹��� ����

        image = Toolkit.getDefaultToolkit().createImage(str);
    }

    @Override
    public void paintComponent(Graphics g) {//�α���ȭ�� ����̹����� �׸���
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

}

class LoginDialog extends JDialog implements ActionListener { //���������ϱ� Ŭ���� �˾��޼���          todo �����μ��� �� ����
    JTextField idTf, ipTf, portTf;
    JLabel idLb, ipLb, portLb;
    JButton okBtn, noBtn, ipBtn;
    JPanel p1;
    JFrame frame;
    LoginUi ui;
    String title;

    LoginDialog(JFrame frame, LoginUi ui, String title) {
        super(frame, title, true);
        this.frame = frame;
        this.ui = ui;
        this.title = title;
        init();
        if (getTitle().equals("���� �����ϱ�")) {
            addS();
            setUiS();
        } else if (getTitle().equals("���� �����ϱ�")) {
            addC();
            setUiC();
        }
    }

    void init() {

        idLb = new JLabel("���̵�");
        idTf = new JTextField(10);
        ipLb = new JLabel("������");
        ipTf = new JTextField(10);
        ipLb = new JLabel("������");
        portLb = new JLabel("��Ʈ");
        portTf = new JTextField(10);
        okBtn = new JButton("Ȯ��");
        noBtn = new JButton("���");
        okBtn.addActionListener(this);
        noBtn.addActionListener(this);
        p1 = new JPanel();
    }

    void setUiS() {
        setLayout(new GridLayout(2, 2));
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void setUiC() {
        setLayout(new GridLayout(4, 2));
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void addS() {
        add(portLb);
        add(portTf);
        add(okBtn);
        add(noBtn);
    }

    void addC() { //
        add(idLb);
        add(idTf);
        add(ipLb);
        add(ipTf);
        add(portLb);
        add(portTf);
        add(okBtn);
        add(noBtn);
    }


    public void actionPerformed(ActionEvent e) {                                            //todo null����ֱ�

        if (e.getSource().equals(okBtn) & title.equals("���� �����ϱ�")) {
            ui.port = portTf.getText().trim();
            Boolean chk = check();
            if (chk = true) {
                dispose();
                frame.dispose();
                new LiarServer(ui,frame);
            }
        } else if (e.getSource().equals(okBtn) & title.equals("���� �����ϱ�")) {
            ui.id = idTf.getText().trim();
            ui.ip = ipTf.getText().trim();
            ui.port = portTf.getText().trim();
            dispose();
            frame.dispose();
            new Client(ui, frame);
        } else if (e.getSource().equals(noBtn)) {
            ipTf.setText("");
            portTf.setText("");
            dispose();
        }
    }

    boolean check() {
        int i;
        if (1 > (i = Integer.parseInt(ui.port)) | (i = Integer.parseInt(ui.port)) > 65535) {
            JOptionPane.showMessageDialog(null, "��Ȯ�� ��Ʈ�� �Է����ּ���.");
            return false;
        }
        return true;
    }

} //�α��δ��̾�α�
