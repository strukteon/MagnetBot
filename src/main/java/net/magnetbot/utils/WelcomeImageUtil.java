package net.magnetbot.utils;
/*
    Created by nils on 02.05.2018 at 21:52.
    
    (c) nils 2018
*/

import ij.ImageJ;
import ij.ImageJApplet;
import net.magnetbot.core.command.ParsedCommandEvent;
import org.apache.poi.util.TempFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WelcomeImageUtil {

    private BufferedImage background;
    private BufferedImage avatar;

    private BufferedImage avatarCircle;

    public WelcomeImageUtil(String avatarUrl, String username, String discriminator){

        try {
            background = ImageIO.read(new File("C:\\Users\\nilss\\Desktop\\cool-bg.jpg"));//new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
            avatarCircle = /*ImageIO.read(new File("C:\\Users\\nilss\\Desktop\\cool-bg.jpg"));*/new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
            avatar = ImageIO.read(new URL(avatarUrl));//ImageIO.read(new File("C:\\Users\\nilss\\Desktop\\magnet-logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = "Welcome, "+username+"#"+discriminator;
        Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        g.setFont(new Font("TimesRoman", Font.BOLD, 72));
        int width = g.getFontMetrics().stringWidth(text);

        background = scale(background, Math.max(1024, width+128), 1024);
        avatar = scale(avatar, 384, 384);

        Graphics2D avatarGraph = background.createGraphics();
        double ellh = avatar.getHeight()*1.05;
        double ellw = avatar.getWidth()*1.05;
        avatarGraph.fill(new Ellipse2D.Double(background.getWidth()/2 - ellw/2, background.getHeight()/2-avatar.getHeight()*1.025, ellw, ellh));



        avatarGraph.setClip(new Ellipse2D.Float(background.getWidth()/2-avatar.getWidth()/2, background.getHeight()/2-avatar.getHeight(), avatar.getHeight(), avatar.getWidth()));
        avatarGraph.drawImage(avatar, background.getWidth()/2-avatar.getWidth()/2, background.getHeight()/2-avatar.getHeight(), null);

        avatarGraph.setClip(0, 0, background.getWidth(), background.getHeight());

        BufferedImage textField = new BufferedImage(background.getWidth(), 1024/4, BufferedImage.TYPE_INT_ARGB);
        Graphics2D txtfield = textField.createGraphics();
        float opacity = 0.7f;
        txtfield.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        txtfield.setPaint(new Color(0));
        txtfield.fillRect(0, 0, background.getWidth(), 1024/4);
        txtfield.setPaint(new Color(255, 255, 255));
        txtfield.setFont(new Font("TimesRoman", Font.BOLD, 72));


        txtfield.drawString(text, 64, 1024/7);

        avatarGraph.drawImage(textField, 0, 1024/5*3, null);
        //Graphics g = sc.createGraphics();

    }

    public File getFile(){
        File temp = null;
        try {
             temp = File.createTempFile("welcome-", ".png");
             ImageIO.write(background, "JPEG", temp);
        } catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }

    private BufferedImage scale(BufferedImage source, int width, int height){
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance((double)width/source.getWidth(),(double)height/source.getHeight());
        g.drawRenderedImage(source, at);
        return scaled;
    }
}
