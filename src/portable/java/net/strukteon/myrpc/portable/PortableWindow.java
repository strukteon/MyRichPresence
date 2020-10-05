package net.strukteon.myrpc.portable;/*
    Created by nils on 04.04.2019 at 21:33.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import net.strukteon.myrpc.utils.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class PortableWindow {
    JFrame f;
    PortableWindow(){
        f = new JFrame();//creating instance of JFrame
        JPanel p = new JPanel();
        GroupLayout layout = new GroupLayout(p);
        p.setLayout(layout);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Information info = Information.loadInformation();

        JLabel lb_author = new JLabel("Author: " + info.author);
        JLabel lb_title = new JLabel("Title: " + info.title);
        JLabel lb_credits = new JLabel("Created with My Rich Presence by Strukteon");

        JButton bt_stopPresence = new JButton("Stop");
        JButton bt_startPresence = new JButton("Start Presence");
        JButton bt_download = new JButton("Download here");


        bt_download.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                String url = "http://strukteon.net/";
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URL(url).toURI());
                    } catch (Exception e) {
                        System.out.printf("Could not open url \"%s\"", url);
                        e.printStackTrace();
                    }
                } else
                    System.out.printf("Could not open url \"%s\", opening urls not supported", url);
            }
        });


        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(lb_title)
                                        .addComponent(lb_author)
                                )
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(bt_startPresence)
                                        .addComponent(bt_stopPresence)
                                )
                                .addComponent(lb_credits)
                                .addComponent(bt_download)
                        )
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(lb_title)
                        .addComponent(lb_author)
                        .addGap(12)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(bt_stopPresence)
                                .addComponent(bt_startPresence))
                        .addGap(16)
                        .addComponent(lb_credits)
                        .addComponent(bt_download)
        );

        p.add(bt_stopPresence);
        p.add(bt_startPresence);

        f.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.gridx = 1;
        c.gridy = 1;
        f.add(p, c);
        f.setSize(300,200);//400 width and 500 height
        f.setVisible(true);//making the frame visible
        f.setResizable(false);

        f.setTitle(info.title);
        try {
            f.setIconImage(ImageIO.read(getClass().getResource("/iconn.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        f.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new PortableWindow();
    }
}
