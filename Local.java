import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import helper_functions.FontStyles;
import helper_functions.ImageLoader;

/*
   Quick Travel
   P1 = Attack (attack method)
   P2 = Move (sees if if it fits the conditions to move)
   P3 = Graphics (Where things get drawn)
   P4 = Click (Everything to do with Click)
*/

public class Local {
   // *** Variable Dictionary ******************************
   Board board = new Board(); // Takes in the information from the board class
   int screen = 0; // Shows you the diffrent screens
   int team = 0; // Says what team you are on
   int turn = 0; // Says whos turn it is
   int xclick = 0; // The x position that you just clicked on
   int yclick = 0; // The y position that you just clicked on
   int xpre = -1; // Says the previous x cordinate you clicked
   int ypre = -1; // Says the previous y cordinate you clicked
   int[] selectToPlace = new int[12];
   int[] selectAmount = new int[12];
   int selectValue = -1;
   boolean gameStart = false;
   int xReg;
   int yReg;
   boolean rwin = false;
   boolean bwin = false;
   boolean rChoose = true;// the reds can choose
   boolean bChoose = false;// the blues can choose
   boolean remove = false;
   boolean setVariables = true;
   SelectColor selectColor = new SelectColor();
   // ******************************************************

   JFrame frame = new JFrame("Stratego");
   Drawing draw = new Drawing();

   public Local() {
      frame.setSize(1100, 800);
      frame.setResizable(false);
      frame.getContentPane().setBackground(Color.WHITE);
      frame.add(draw);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      draw.addMouseListener(new ClickHandler());
   }

   public static void main(String[] args) {
      new Local();
   }

   public void winFight(int[][] firstPiece, int[][] secondPiece) {
      if (board.rteam[xclick][yclick] == 0)
         screen = 4;
      else if (board.bteam[xclick][yclick] == 0) {
         screen = 3;
      }
      board.shown[xclick][yclick] = true;
      board.shown[xpre][ypre] = false;
      firstPiece[xclick][yclick] = firstPiece[xpre][ypre];
      secondPiece[xclick][yclick] = -1;
      firstPiece[xpre][ypre] = -1;
      board.occupied[xpre][ypre] = false;
   }

   public void attackHelper(int[][] firstPiece, int[][] secondPiece) {
      if (firstPiece[xpre][ypre] == secondPiece[xclick][yclick]) {
         board.shown[xclick][yclick] = false;
         board.shown[xpre][ypre] = false;
         board.occupied[xpre][ypre] = false;
         board.occupied[xclick][yclick] = false;
         firstPiece[xpre][ypre] = -1;
         secondPiece[xclick][yclick] = -1;
      }
      // Miner attacking a bomb
      else if (firstPiece[xpre][ypre] == 3 && secondPiece[xclick][yclick] == 11) {
         winFight(firstPiece, secondPiece);
      }
      // Spy attacking a level 10
      else if (firstPiece[xpre][ypre] == 1 && secondPiece[xclick][yclick] == 10) {
         winFight(firstPiece, secondPiece);
      } else if (firstPiece[xpre][ypre] > secondPiece[xclick][yclick]) {
         winFight(firstPiece, secondPiece);
      // Loses
      // TESTING FIX
      } else if(firstPiece[xpre][ypre] < secondPiece[xclick][yclick]) {
         board.shown[xclick][yclick] = true;
         board.shown[xpre][ypre] = false;
         secondPiece[xpre][ypre] = -1;
         board.occupied[xpre][ypre] = false;
      }
   }

   /***
    * init *********************************************
    * Purpose: Get the pieces to attack
    * Parameters: none
    * Returns: none
    ******************************************************/
   public void attack()// P1
   {
      /*
       * All statements bellow are present in both win and loss
       * As such there is no need to write code if you lose the battle
       */
      if (team == 0 && turn == 0 && board.rteam[xpre][ypre] != 11) {
         attackHelper(board.rteam, board.bteam);

      } else if (team == 1 && turn == 1 && board.bteam[xpre][ypre] != 11) {
         attackHelper(board.bteam, board.rteam);
      }

      boolean rcount = true;
      boolean bcount = true;
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            if (board.rteam[i][j] != -1 && board.rteam[i][j] != 0 && board.rteam[i][j] != 11)
               rcount = false;
            if (board.bteam[i][j] != -1 && board.bteam[i][j] != 0 && board.bteam[i][j] != 11)
               bcount = false;
         }
      }
      if (rcount && bcount)
         screen = 5; // Tie
      else if (rcount)
         screen = 4; // Blue wins
      else if (bcount)
         screen = 3; // Red wins
   }

   /***
    * move *********************************************
    * Purpose: To make pieces move
    * Parameters: end - indicates if an action has been made
    * Returns: none
    ******************************************************/
   public void move()// P2
   {
      board.speedcal(xpre, ypre);
      boolean end = false;
      int speed = board.speed[xpre][ypre];
      // Checks to make sure its the red player on the red players turn
      if (team == 0 && turn == 0 && board.rteam[xpre][ypre] != -1 && !board.special[xclick][yclick]) {
         for (int i = 1; i < speed + 1 && !end; i++) {
            if (xpre + i == xclick && ypre == yclick) {

               if (!board.occupied[xclick][yclick] && board.rteam[xclick][yclick] == -1) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre + j <= 9) {
                        if (board.occupied[j + xpre][ypre] && j + xpre != xclick) {
                           b = true;
                        } else if (j + xpre == xclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;
                           ;
                           end = true;
                        }
                     }
                  }
               } else if (board.rteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre + j <= 9) {
                        if (board.occupied[j + xpre][ypre] && j + xpre != xclick) {
                           b = true;
                        } else if (j + xpre == xclick) {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            } else if (xpre - i == xclick && ypre == yclick) {
               if (!board.occupied[xclick][yclick] && board.rteam[xclick][yclick] == -1) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre - j >= 0) {
                        if (board.occupied[xpre - j][ypre] && xpre - j != xclick) {
                           b = true;
                        } else if (xpre - j == xclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;
                           ;
                           end = true;
                        }
                     }
                  }
               } else if (board.rteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre - j >= 0) {
                        if (board.occupied[xpre - j][ypre] && xpre - j != xclick) {
                           b = true;
                        } else if (xpre - j == xclick) {

                           attack();
                           end = true;
                        }
                     }
                  }
               }
            } else if (xpre == xclick && ypre + i == yclick) {

               if (!board.occupied[xclick][yclick] && board.rteam[xclick][yclick] == -1) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre + j >= 0) {
                        if (board.occupied[xpre][ypre + j] && j + ypre != yclick) {
                           b = true;
                        } else if (j + ypre == yclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;
                           ;
                           end = true;
                        }
                     }
                  }

               } else if (board.rteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre + j >= 0) {
                        if (board.occupied[xpre][ypre + j] && ypre + j != yclick) {
                           b = true;
                        } else if (ypre + j == yclick) {

                           attack();
                           end = true;
                        }
                     }
                  }
               }
            } else if (xpre == xclick && ypre - i == yclick) {

               if (!board.occupied[xclick][yclick] && board.rteam[xclick][yclick] == -1) {

                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre - j >= 0) {
                        if (board.occupied[xpre][ypre - j] && ypre - j != yclick) {
                           b = true;
                        } else if (ypre - j == yclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;
                           ;
                           end = true;
                        }
                     }
                  }
               } else if (board.rteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre + j >= 0) {
                        if (board.occupied[xpre][ypre - j] && ypre - j != yclick) {
                           b = true;
                        } else if (ypre - j == yclick) {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
         }
      } else if (team == 1 && turn == 1 && board.bteam[xpre][ypre] != -1 && !board.special[xclick][yclick]) {
         for (int i = 1; i < speed + 1 && !end; i++) {
            if (xpre + i == xclick && ypre == yclick) {

               if (!board.occupied[xclick][yclick] && board.bteam[xclick][yclick] == -1) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre + j <= 9) {
                        if (board.occupied[j + xpre][ypre] && j + xpre != xclick) {
                           b = true;
                        } else if (j + xpre == xclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;
                           end = true;
                        }
                     }
                  }
               } else if (board.bteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre + j <= 9) {
                        if (board.occupied[j + xpre][ypre] && j + xpre != xclick) {
                           b = true;
                        } else if (j + xpre == xclick) {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            } else if (xpre - i == xclick && ypre == yclick) {

               if (!board.occupied[xclick][yclick] && board.bteam[xclick][yclick] == -1) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre - j >= 0) {
                        if (board.occupied[xpre - j][ypre] && xpre - j != xclick) {
                           b = true;
                        } else if (xpre - j == xclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;
                           ;
                           end = true;
                        }
                     }
                  }
               } else if (board.bteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (xpre - j >= 0) {
                        if (board.occupied[xpre - j][ypre] && xpre - j != xclick) {
                           b = true;
                        } else if (xpre - j == xclick) {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            } else if (xpre == xclick && ypre + i == yclick) {

               if (!board.occupied[xclick][yclick] && board.bteam[xclick][yclick] == -1) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre + j >= 0) {
                        if (board.occupied[xpre][ypre + j] && j + ypre != yclick) {
                           b = true;
                        } else if (j + ypre == yclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;
                           ;
                           end = true;
                        }
                     }
                  }

               } else if (board.bteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre + j >= 0) {
                        if (board.occupied[xpre][ypre + j] && ypre + j != yclick) {
                           b = true;
                        } else if (ypre + j == yclick) {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            } else if (xpre == xclick && ypre - i == yclick) {

               if (!board.occupied[xclick][yclick] && board.bteam[xclick][yclick] == -1) {

                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre - j >= 0) {
                        if (board.occupied[xpre][ypre - j] && ypre - j != yclick) {
                           b = true;
                        } else if (ypre - j == yclick) {
                           board.occupied[xpre][ypre] = false;
                           board.occupied[xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;
                           ;
                           end = true;
                        }
                     }
                  }
               } else if (board.bteam[xclick][yclick] == -1 && board.occupied[xclick][yclick]) {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && !b; j++) {
                     if (ypre + j >= 0) {
                        if (board.occupied[xpre][ypre - j] && ypre - j != yclick) {
                           b = true;
                        } else if (ypre - j == yclick) {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
         }
      }

      // This is to find out
      if (end) {
         if (turn == 0 && screen < 3) {
            turn = 1;
            team = 1;
            screen = 2;
         } else if (turn == 1 && screen < 3) {
            turn = 0;
            team = 0;
            screen = 2;
         }
      }
   }

   class Drawing extends JComponent// P3
   {

      // Importing Images
      public Drawing() {
         repaint();
      }

      public void paint(Graphics g) {
         frame.getContentPane().setBackground(Color.WHITE);

         if (setVariables) {
            selectToPlace[0] = 0; // Flag
            selectToPlace[1] = 1; // Spy
            selectToPlace[2] = 2;
            selectToPlace[3] = 3;
            selectToPlace[4] = 4;
            selectToPlace[5] = 5;
            selectToPlace[6] = 6;
            selectToPlace[7] = 7;
            selectToPlace[8] = 8;
            selectToPlace[9] = 9;
            selectToPlace[10] = 10;
            selectToPlace[11] = 11; // Bomb

            selectAmount[0] = 1;
            selectAmount[1] = 1;
            selectAmount[2] = 8;
            selectAmount[3] = 5;
            selectAmount[4] = 4;
            selectAmount[5] = 4;
            selectAmount[6] = 4;
            selectAmount[7] = 3;
            selectAmount[8] = 2;
            selectAmount[9] = 1;
            selectAmount[10] = 1;
            selectAmount[11] = 6;

            setVariables = false;
         }

         if (screen == 0) {
            g.setFont(FontStyles.MonoFont70);

            g.drawImage(ImageLoader.getLogo().getImage(), 160, 40, 750, 250, this);

            g.setColor(Color.BLACK);
            g.fillRect(330, 300, 400, 100);
            g.setColor(Color.WHITE);
            g.drawString("Play", 440, 365);

            if (xReg <= 730 && xReg >= 330 && yReg <= 400 && yReg >= 300) {
               screen = 1;
               repaint();
            }

            g.setFont(FontStyles.MonoFont50);

            g.setColor(Color.BLACK);
            g.fillRect(330, 450, 400, 100);
            g.setColor(Color.WHITE);
            g.drawString("Instructions", 350, 515);

            if (xReg <= 730 && xReg >= 330 && yReg <= 550 && yReg >= 450) {
               screen = 9;
               repaint();
            }

         } else if (screen == 9) {
            g.setFont(FontStyles.MonoFont70);

            g.setColor(Color.BLACK);
            g.fillRect(750, 650, 400, 100);
            g.setColor(Color.WHITE);
            g.drawString("Back", 840, 720);

            if (xReg <= 1150 && xReg >= 750 && yReg <= 750 && yReg >= 650) {
               screen = 0;
               repaint();
            }

            g.setFont(FontStyles.MonoFont20);
            g.setColor(Color.BLACK);
            g.drawString("-Game instructions (How to play and rules)can be found in the README Folder.", 50, 50);
            g.drawString(
                  "-In order to place a piece click its picture and then click the tile you want to place it on.", 50,
                  80);
            g.drawString("-Red must place on left side, blue on right", 50, 110);
            g.drawString("-If you want to remove a piece, just place another piece in its", 50, 140);
            g.drawString(" place or click remove and click the piece you want to remove.", 50, 160);
            g.drawString("-Click finish when you are done placing.", 50, 190);
            g.drawString("-When you move or attack, your opponent must click the screen to start their turn", 50, 220);
            g.drawString("-Don't look at your opponent's screen when they move or attack", 50, 250);
            g.drawString("-Have fun", 50, 280);
         } else if (screen == 1)

         {
            g.drawImage(ImageLoader.getBackground().getImage(), 50, 50, 700, 700, this);
            g.setFont(FontStyles.MonoFont30);

            if ((bChoose || rChoose) && turn == 0) {
               g.setFont(FontStyles.MonoFont30);
               g.setColor(Color.RED);
               g.drawString("Red Player must place", 50, 40);
            } else if ((bChoose || rChoose) && turn == 1) {
               g.setFont(FontStyles.MonoFont30);
               g.setColor(Color.BLUE);
               g.drawString("Blue Player must place", 50, 40);
            } else if (!(bChoose || rChoose) && turn == 0) {
               g.setFont(FontStyles.MonoFont30);
               g.setColor(Color.RED);
               g.drawString("Red Player's Turn", 50, 40);
            } else {
               g.setFont(FontStyles.MonoFont30);
               g.setColor(Color.BLUE);
               g.drawString("Blue Player's turn", 50, 40);
            }

            int doneChoosing = 0;

            // do{ //Placing
            if (rChoose && team == 0) {
               // CHANGE BACK TO 0 AFTER TESTING
               doneChoosing = 11;
               for (int a = 0; a < 12; a++) {
                  if (selectAmount[a] == 0) {
                     doneChoosing += 1;
                  }
               }
               if (doneChoosing == 12) {

                  g.setFont(FontStyles.MonoFont20);

                  g.setColor(Color.BLACK);
                  g.fillRect(955, 710, 100, 30);

                  g.setColor(Color.WHITE);
                  g.drawString("Finish", 965, 730);

                  if (xReg >= 955 && xReg <= 1055 && yReg >= 710 && yReg <= 810) {

                     selectAmount[0] = 1;
                     selectAmount[1] = 1;
                     selectAmount[2] = 8;
                     selectAmount[3] = 5;
                     selectAmount[4] = 4;
                     selectAmount[5] = 4;
                     selectAmount[6] = 4;
                     selectAmount[7] = 3;
                     selectAmount[8] = 2;
                     selectAmount[9] = 1;
                     selectAmount[10] = 1;
                     selectAmount[11] = 6;

                     rChoose = false;
                     bChoose = true;
                     team = 1;
                     turn = 1;
                     repaint();

                  }

                  else {

                  }

               } else {
                  selectPlace();
                  placePiece();
                  repaint();
               }

               // System.out.println(""+doneChoosing);

            }
            // while(rChoose );

            if (bChoose && team == 1) {
               doneChoosing = 11;
               for (int a = 0; a < 12; a++) {
                  if (selectAmount[a] == 0) {
                     doneChoosing += 1;
                  }
               }
               if (doneChoosing == 12) {

                  g.setFont(FontStyles.MonoFont20);

                  g.setColor(Color.BLACK);
                  g.fillRect(955, 710, 100, 30);

                  g.setColor(Color.WHITE);
                  g.drawString("Finish", 965, 730);

                  if (xReg >= 955 && xReg <= 1055 && yReg >= 710 && yReg <= 810) {
                     repaint();
                     bChoose = false;
                     team = 0;
                     turn = 0;
                     repaint();
                  }

                  else {
                  }

               } else {
                  selectPlace();
                  placePiece();
                  repaint();
               }
            }

            if (bChoose || rChoose) {
               g.setFont(FontStyles.MonoFont20);

               g.setColor(Color.BLACK);
               g.fillRect(800, 710, 100, 30);

               g.setColor(Color.WHITE);
               g.drawString("Remove", 810, 730);
               if (xReg >= 800 && xReg <= 900 && yReg >= 710 && yReg <= 740) {
                  remove = true;
               }

            }

            try {
               if ((bChoose || rChoose) && remove) {
                  if (xReg >= 50 && xReg <= 750 && yReg >= 50 && yReg <= 750) {

                     if (board.occupied[xclick][yclick] && team == 0 && xclick <= 3 && xclick >= 0) {
                        selectAmount[board.rteam[xclick][yclick]] += 1;
                        board.rteam[xclick][yclick] = -1;

                        remove = false;
                        repaint();
                     } else if (board.occupied[xclick][yclick] && team == 1 && xclick <= 9 && xclick >= 6) {
                        selectAmount[board.bteam[xclick][yclick]] += 1;
                        board.bteam[xclick][yclick] = -1;

                        remove = false;
                        repaint();
                     }

                     else if (!board.occupied[xclick][yclick]) {
                        remove = false;
                        repaint();
                     } else {
                     }
                     // repaint();
                  }
               }
            } catch (Exception e) {
            }

            g.setFont(FontStyles.MonoFont20);

            g.setColor(Color.BLACK);
            g.fillRect(800, 710, 100, 30);

            g.setColor(Color.WHITE);
            g.drawString("Remove", 810, 730);

            if (!rChoose && !bChoose) {
               remove = false;
               gameStart = true;
            }

            // Selecting Pieces to Place
            selectColor.greySelect(g, selectAmount);

            boolean end = false;

            // Says that if where you clicked was not in a square it will not do anything
            if (xclick != -1 || yclick != -1) {
               // Says its reds turn
               if (turn == 0 && team == 0) {
                  // Checks to see if the place they clicked where a peice is
                  if (board.rteam[xclick][yclick] != -1) {
                     /*
                      * r < speed + 1 as we need to make it so that the box isn't right on the square
                      * you clicked
                      * end is to see if we need to end the program early because somethings isnt
                      * suppost to be drawn
                      * r + xclick < 10 is so that we have no errors of out of bonds
                      */
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end && xclick + r < 10; r++) {
                        if (board.rteam[xclick + r][yclick] != -1 || board.special[xclick + r][yclick])
                           end = true;
                        else {
                           g.setColor(Color.green);
                           if (board.occupied[r + xclick][yclick] && board.rteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick + r], board.loc[yclick], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end && xclick - r >= 0; r++) {
                        if (board.rteam[xclick - r][yclick] != -1 || board.special[xclick - r][yclick]) {
                           end = true;
                        } else {

                           g.setColor(Color.green);
                           if (board.occupied[xclick - r][yclick] && board.rteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick - r], board.loc[yclick], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end && r + yclick < 10; r++) {
                        if (board.rteam[xclick][yclick + r] != -1 || board.special[xclick][yclick + r]) {
                           end = true;
                        } else {

                           g.setColor(Color.green);
                           if (board.occupied[xclick][r + yclick] && board.rteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick], board.loc[yclick + r], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end && yclick - r >= 0; r++) {
                        if (board.rteam[xclick][yclick - r] != -1 || board.special[xclick][yclick - r]) {
                           end = true;
                        } else {
                           g.setColor(Color.green);
                           if (board.occupied[xclick][yclick - r] && board.rteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick], board.loc[yclick - r], 70, 70);
                        }
                     }
                     end = false;
                  }
               } else if (team == 1 && turn == 1) {
                  if (board.bteam[xclick][yclick] != -1) {
                     /*
                      * r < speed + 1 as we need to make it so that the box isn't right on the square
                      * you clicked
                      * end is to see if we need to end the program early because somethings isnt
                      * suppost to be drawn
                      * r + xclick < 10 is so that we have no errors of out of bonds
                      */
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end && xclick + r < 10; r++) {
                        if (board.bteam[xclick + r][yclick] != -1 || board.special[xclick + r][yclick])
                           end = true;
                        else {
                           g.setColor(Color.green);
                           if (board.occupied[r + xclick][yclick] && board.bteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick + r], board.loc[yclick], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end && xclick - r >= 0; r++) {
                        if (board.bteam[xclick - r][yclick] != -1 || board.special[xclick - r][yclick]) {
                           end = true;
                        } else {

                           g.setColor(Color.green);
                           if (board.occupied[xclick - r][yclick] && board.bteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick - r], board.loc[yclick], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && !end && r + yclick < 10; r++) {
                        if (board.bteam[xclick][yclick + r] != -1 || board.special[xclick][yclick + r]) {
                           end = true;
                        } else {
                           g.setColor(Color.green);
                           if (board.occupied[xclick - r][yclick]) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && !end && r + yclick < 10; r++) {
                        if (board.bteam[xclick][yclick + r] != -1 || board.special[xclick][yclick + r]) {
                           end = true;
                        } else {

                           g.setColor(Color.green);
                           if (board.occupied[xclick][r + yclick] && board.bteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick], board.loc[yclick + r], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && !end && yclick - r >= 0; r++) {
                        if (board.bteam[xclick][yclick - r] != -1 || board.special[xclick][yclick - r]) {
                           end = true;
                        } else {
                           g.setColor(Color.green);
                           if (board.occupied[xclick][yclick - r] && board.bteam[xclick][yclick] != 11) {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick], board.loc[yclick - r], 70, 70);
                        }
                     }
                     end = false;
                  }
               }
            }
         }

         // Win Screens and Change Turn Screen
         if (screen == 2) {
            frame.getContentPane().setBackground(Color.BLACK);

            g.setFont(FontStyles.MonoFont75);

            g.setColor(Color.WHITE);
            g.drawString("Click Screen Player " + turn, 80, 400);
         }
         if (screen == 3) {
            frame.getContentPane().setBackground(Color.RED);

            g.setFont(FontStyles.MonoFont200);

            g.setColor(Color.WHITE);
            g.drawString("Red Wins", 80, 400);
         }
         if (screen == 4) {
            frame.getContentPane().setBackground(Color.BLUE);

            g.setFont(FontStyles.MonoFont200);

            g.setColor(Color.WHITE);
            g.drawString("Blue Wins", 10, 400);
         }
         if (screen == 5) {
            frame.getContentPane().setBackground(Color.BLACK);

            g.setFont(FontStyles.MonoFont200);

            g.setColor(Color.WHITE);
            g.drawString("Tie", 350, 400);
         }
         if (screen == 1) {

            // Used to put the pieces on the board
            for (int i = 0; i < 10; i++) {
               for (int j = 0; j < 10; j++) {
                  // Makes it so that you can see your own pieces and those revealed for the other
                  // team
                  if (board.shown[i][j] || team == 0) {
                     if (board.rteam[i][j] >= 0 && board.rteam[i][j] <= 11)
                        g.drawImage(ImageLoader.getRed()[board.rteam[i][j]].getImage(), board.loc[i] + 5,
                              board.loc[j] + 5, 60, 60, this);
                  }
                  // Makes it if you are on the other side you can't see the pieces if they are
                  // not revealed
                  else if (board.rteam[i][j] != -1)
                     g.drawImage(ImageLoader.getRed()[12].getImage(), board.loc[i] + 5, board.loc[j] + 5, 60, 60, this);

                  // Makes it so that you can see your own pieces and those revealed for the other
                  // team
                  if (board.shown[i][j] || team == 1) {
                     if (board.bteam[i][j] >= 0 && board.bteam[i][j] <= 11)
                        g.drawImage(ImageLoader.getBlue()[board.bteam[i][j]].getImage(), board.loc[i] + 5,
                              board.loc[j] + 5, 60, 60, this);
                  }
                  // Makes it if you are on the other side you can't see the pieces if they are
                  // not revealed
                  else if (board.bteam[i][j] != -1)
                     g.drawImage(ImageLoader.getBlue()[12].getImage(), board.loc[i] + 5, board.loc[j] + 5, 60, 60,
                           this);
               }

            }
         }
      }
   }

   // P4
   class ClickHandler extends MouseAdapter {
      public void mousePressed(MouseEvent e) {

         int other = 0;
         int x = -1;
         int y = -1;
         // Finds the quardants of where you clicked

         xReg = e.getX();
         yReg = e.getY();

         if (screen == 1) {

            // if (xReg >= 50 && xReg <= 750 && yReg >= 50 && yReg <= 750){

            for (int i = 0; i < 10 && other < 2; i++) {
               if (e.getX() > board.loc[i] && e.getX() <= board.loc[i + 1]) {
                  x = i;
                  other += 1;
               }
               if (e.getY() > board.loc[i] && e.getY() <= board.loc[i + 1]) {
                  y = i;
                  other += 1;
               }
            }

            boolean onScreenClick = true;

            if (x == -1 || y == -1)
               onScreenClick = false;

            if (onScreenClick) {
               xpre = xclick;
               ypre = yclick;
               xclick = x;
               yclick = y;
               other = 0;
               if (gameStart) {
                  move();
               }
            }

         } else if (screen == 2)
            screen = 1;

         draw.repaint();

      }
   }

   void selectPlace() { // select a piece to place
      for (int q = 0; q < 12; q++) {
         if ((q % 2) == 0) {
            if (xReg >= 800 && xReg <= 900 && yReg >= (50 + (55 * q)) && yReg <= ((50 + (55 * q)) + 100)) {
               remove = false;
               selectValue = selectToPlace[q];
            }

         } else {
            if (xReg >= 950 && xReg <= 1050 && yReg >= (50 + (55 * (q - 1))) && yReg <= ((50 + (55 * q)) + 100)) {
               remove = false;
               selectValue = selectToPlace[q];
            }
         }
      }

   }

   // Method to place pieces
   void placePiece() {
      if (selectValue >= 0 && selectAmount[selectValue] > 0) {
         if (xReg >= 50 && xReg <= 750 && yReg >= 50 && yReg <= 750 && board.canPlace(xclick, yclick, team)) {

            try {
               if (board.occupied[xclick][yclick] && team == 0) {
                  selectAmount[board.rteam[xclick][yclick]] += 1;
               } else if (board.occupied[xclick][yclick] && team == 1) {
                  selectAmount[board.bteam[xclick][yclick]] += 1;
               }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            board.setPiece(xclick, yclick, team, selectValue);
            selectAmount[selectValue] -= 1;
            if (selectAmount[selectValue] <= 0) {
               selectValue = -1;
            }
         }
      }
   }
}