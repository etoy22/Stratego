
import java.awt.*;

import helper_functions.FontStyles;
import helper_functions.ImageLoader;


public class SelectColor {

    // Selecting Pieces to Place
    void greySelect(Graphics g, int[] selectAmount) {
        g.setFont(FontStyles.MonoFont20);
        for (int p = 0; p < 12; p++) {
            int cordX = 0;
            int cordY = 0;
            g.setColor(Color.black);
            if (p % 2 == 0) {
                cordX = 800;
                cordY = 50 + 55 * p;
            } else {
                cordX = 950;
                cordY = 50 + 55 * (p - 1);
            }
            g.fillRect(cordX, cordY, 100, 100);
            g.setColor(Color.black);
            g.drawImage(ImageLoader.getGrey()[p].getImage(), cordX + 5, cordY, 90, 90, null);
            g.setColor(Color.GREEN);
            g.drawString("" + selectAmount[p], cordX + 85, cordY + 90);
            g.setColor(Color.black);
        }
    }
}
