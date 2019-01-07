import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.*;
//import java.util.*;

public class ColorChanger2 implements ChangeListener{
    JFrame frame;
    BufferedImage processed;
    BufferedImage raw;

    public Main()throws IOException{
        System.out.println("Hello World!");
        frame = new JFrame("Main Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //add stuff to frame
        raw = ImageIO.read(new File("flower.png"));

        int width = raw.getWidth();
        int height = raw.getHeight();

        processed = new BufferedImage(width, height, raw.getType());
        processed.getGraphics().drawImage(raw,0,0,null);

        JLabel image= new JLabel(new ImageIcon(processed));
        frame.add(image, BorderLayout.CENTER);

        JSlider slider = new JSlider();
        frame.add(slider, BorderLayout.SOUTH);
        slider.addChangeListener(this);
        //pack and make visible
        frame.pack();
        frame.setVisible(true);

        frame.repaint();
    }

    public void stateChanged(ChangeEvent e) {
        int value =((JSlider)e.getSource()).getValue();
        float hue = value / 100f;
        int width = raw.getWidth();
        int height = raw.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //this is how we grab the RGB value of a pixel at x, y coordinates in the image
                int rgb = raw.getRGB(x, y);

                //extract the red value
                int red = (rgb >> 16) & 0xFF;

                //extract the green value
                int green = (rgb >> 8) & 0xFF;

                //extract the blue value
                int blue = (rgb) & 0xFF;

                //use Color.RGBtoHSB() method to convert RGB value to HSB
                float hsb[] = Color.RGBtoHSB(red, green, blue, null);

                //can either set the hue with
                //hsb[0] = hue;
                //or we can offset the hue by adding the hardcoded hue to the existing hue
                //hsb[0] = hsb[0] + hue;
                //the latter will leave some of the original color in the image
                hsb[0] = hsb[0] + hue;

                //since hue should range from 0 to 1, if adding hue results in a number greater than 1,
                //resulting hue should loop back around the spectrum
                //so a simple -1 to resulting hue should work
                if (hsb[0] > 1) {
                    hsb[0] = hsb[0] - 1;
                }

                //then use Color.HSBtoRGB() method to convert the HSB value to a new RGB value
                int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

                //set the new RGB value to a pixel at x, y coordinates in the image
                processed.setRGB(x, y, newRGB);
            }
        }
        frame.repaint();
    }


    public static void main(String[] args)throws IOException {
      new Main();
    }

}
