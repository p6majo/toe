package com.p6majo.plot.bennett.coloroverlay;

import com.p6majo.math.utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ColorOverlay {
    static public final byte[] overlayColor = new byte[] { (byte) 0x40,
            (byte) 0xFF, (byte) 0x00, (byte) 0x00 };

    public static void main(String[] args) throws Exception {
        //URL url = new URL("http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png");
        File file = new File("/home/jmartin/workbase/toe/res/pin.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
       }
       catch (IOException ex){
           Utils.errorMsg(ex.getMessage());
       }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        ImageIcon icon = new ImageIcon(
                frame.createImage(new FilteredImageSource(image.getSource(),
                        new RGBAOverlayFilter(overlayColor))));

        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setVisible(true);
    }

    static public class RGBAOverlayFilter extends RGBImageFilter {
        int overlayA;
        int[] overlayRGB;

        public RGBAOverlayFilter(byte[] color) {
            overlayA = color[0] & 0xFF;
            overlayRGB = new int[] { color[1] & 0xFF, color[2] & 0xFF,
                    color[3] & 0xFF };
        }

        @Override
        public int filterRGB(int x, int y, int rgb) {
            int imageA = (rgb >> 24) & 0xFF;
            int[] imageRGB = new int[] { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF,
                    rgb & 0xFF };

            float a = (float) overlayA / 255F;

            for (int i = 0; i < 2; i++) {
                imageRGB[i] = (int) ((1F - a) * imageRGB[i] + a * overlayRGB[i]);
            }

            return (imageA << 24) | (imageRGB[0] << 16) | (imageRGB[1] << 8)
                    | imageRGB[2];
        }
    }
}
