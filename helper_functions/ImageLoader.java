package helper_functions;

import javax.swing.ImageIcon;

public class ImageLoader {
    private static final String FOLDER_PATH = "Images/";

    private static ImageIcon loadIconFromFolder(String filename) {
        return new ImageIcon(FOLDER_PATH + filename);
    }

    public static ImageIcon getLogo() {
        return loadIconFromFolder("LOGO.png");
    }

    public static ImageIcon getBackground() {
        return loadIconFromFolder("Capture.PNG");
    }

    public static ImageIcon[] getBlue() {
        String[] blueIcons = {
            "BF.png", "BS.png", "B2.png", "B3.png", "B4.png", "B5.png", "B6.png",
            "B7.png", "B8.png", "B9.png", "B10.png", "BB.png", "BE.png"
        };
        return loadIconsFromFolder(blueIcons);
    }

    public static ImageIcon[] getRed() {
        String[] redIcons = {
            "RF.png", "RS.png", "R2.png", "R3.png", "R4.png", "R5.png", "R6.png",
            "R7.png", "R8.png", "R9.png", "R10.png", "RB.png", "RE.png"
        };
        return loadIconsFromFolder(redIcons);
    }

    public static ImageIcon[] getGrey() {
        String[] greyIcons = {
            "GF.png", "GS.png", "G2.png", "G3.png", "G4.png", "G5.png", "G6.png",
            "G7.png", "G8.png", "G9.png", "G10.png", "GB.png", "GE.png"
        };
        return loadIconsFromFolder(greyIcons);
    }

    private static ImageIcon[] loadIconsFromFolder(String[] filenames) {
        ImageIcon[] icons = new ImageIcon[filenames.length];
        for (int i = 0; i < filenames.length; i++) {
            icons[i] = loadIconFromFolder(filenames[i]);
        }
        return icons;
    }
}
