import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SimpleImageSwitcher {
    public static void main(String[] args) {
        // Load the images
        ImageIcon[] images = {
            new ImageIcon("1.png"),
            new ImageIcon("2.png"),
            new ImageIcon("3.png"),
            new ImageIcon("4.png"),
            new ImageIcon("5.png"),
            new ImageIcon("6.png"),
            new ImageIcon("7.png"),
            new ImageIcon("8.png"),
            new ImageIcon("9.png"),
        };

        // Load the sound file
        File soundFile = new File("boom.wav");

        // Create a new JFrame (a window)
        JFrame frame = new JFrame("powodzenia");

        // Get the dimensions of the first image
        int imageWidth = images[0].getIconWidth();
        int imageHeight = images[0].getIconHeight();

        // Set the size of the frame based on the first image's dimensions
        frame.setSize(imageWidth, imageHeight + 30);

        // Specify an action for the close button
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JLabel to display the image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);  // Center the image

        // Initialize an index to track which image is being displayed
        final int[] currentImageIndex = {0};

        // Set the initial image
        imageLabel.setIcon(images[currentImageIndex[0]]);

        // Add the JLabel to the frame
        frame.add(imageLabel, BorderLayout.CENTER);

        // Method to play the sound
        ActionListener playSound = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Create an audio input stream from the sound file
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                    
                    // Get a sound clip resource
                    Clip clip = AudioSystem.getClip();
                    
                    // Open the audio clip and load samples from the audio input stream
                    clip.open(audioInputStream);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-10.0f);
                    // Play the sound clip
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
        };

        // Create a Timer to switch the image every 30 seconds
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the next image
                currentImageIndex[0] = (currentImageIndex[0] + 1) % images.length;
                imageLabel.setIcon(images[currentImageIndex[0]]);
                
                // Update the frame size based on the new image dimensions
                int newImageWidth = images[currentImageIndex[0]].getIconWidth();
                int newImageHeight = images[currentImageIndex[0]].getIconHeight() + 30;
                frame.setSize(newImageWidth, newImageHeight);
                
                // Play the sound
                playSound.actionPerformed(e);
                
                // Randomly relocate the window
                relocateWindowRandomly(frame);
                
                // Bring the window to the front
                bringToFront(frame);
                frame.setVisible(true);
            }
        });
        timer.start();  // Start the timer

        // Pack the frame to fit the JLabel
        frame.pack();

        // Randomly locate the window at startup
        relocateWindowRandomly(frame);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to relocate the window to a random position on the screen
    private static void relocateWindowRandomly(JFrame frame) {
        Random random = new Random();
        
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Calculate the maximum possible x and y coordinates for the window
        int maxX = screenSize.width - frame.getWidth();
        int maxY = screenSize.height - frame.getHeight();
        
        // Generate random x and y coordinates within the screen bounds
        int randomX = random.nextInt(maxX + 1);
        int randomY = random.nextInt(maxY + 1);
        
        // Set the location of the frame to the random coordinates
        frame.setLocation(randomX, randomY);
    }

    // Method to bring the window to the front
    private static void bringToFront(JFrame frame) {
        frame.setAlwaysOnTop(true);  // Set the window always on top
        frame.toFront();             // Bring to the front
        frame.repaint();             // Repaint the window
        frame.requestFocus();        // Request focus
        frame.setAlwaysOnTop(false); // Turn off always on top after bringing to front
    }
}