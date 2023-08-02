import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;


/*
   Quick Travel
   P1 = Attack (attack method)
   P2 = Move (sees if if it fits the conditions to move)
   P3 = Amove (Actual command to move)
   P4 = Graphics (Where things get drawn)
   P5 = Click (Everything to do with Click)
*/


public class StrategoServer
{
   //*** Variable Dictionary ******************************
   Board board = new Board(); //Takes in the information from the board class
   int screen = 1;            //Shows you the diffrent screens
   int team = 0;              //Says what team you are on
   int turn = 0;              //Says whos turn it is
   int xclick = 0;            //The x position that you just clicked on
   int yclick = 0;            // The y position that you just clicked on
   int xpre = -1;             //Says the previous x cordinate you clicked
   int ypre = -1;             //Says the previous y cordinate you clicked
   int selectToPlace[] = new int[12];
   int selectAmount[] = new int[12];
   int selectValue = -1;
   boolean gameStart = false;
   int xReg;
   int yReg;
   boolean rwin = false;
   boolean bwin = false;
   boolean rChoose = true;//the reds can choose
   boolean bChoose = false;//the blues can choose
   boolean setVariables = true;
   PrintStream out = null;
   boolean rbool =true;
   boolean remove = false;
   //boolean row = false;
   //******************************************************
   
   JFrame frame = new JFrame("Stratego");
   Drawing draw = new Drawing();
   
   //ImageIcon img = new ImageIcon("Capture.PNG");
            
   

   
   public StrategoServer()throws IOException
   {
      frame.setSize(1100,800);
      frame.setResizable(false);
      frame.add(draw);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
      draw.addMouseListener(new ClickHandler());
      //frame.setIconImage(img.getImage());
      //board.occupied[1][1] = true; 
      multiGame();   
   }

   public static void main(String[] args)throws IOException
   {
      new StrategoServer();
   }
   
   /*** init *********************************************
   * Purpose: Get the pieces to attack
   * Parameters: none                                    
   * Returns: none                                       
   ******************************************************/
   public void attack()//P1
   {
      /*
      All statements bellow are present in both win and loss
      As such there is no need to write code if you lose the battle
      */
      //battle = true;
      if (team == 0 && turn == 0 && board.rteam[xpre][ypre] != 11)
      {
         /*
            Checks if the peices are of the same value
            If they are both pieces are elliminated
         */
         //attacker = board.rteam[xpre][ypre]+1;
         //defender = -board.bteam[xclick][yclick]-1;
         if (board.rteam[xpre][ypre] == board.bteam[xclick][yclick])
         {
            board.shown[xclick][yclick] = false;
            board.shown[xpre][ypre] = false;
            board.occupied[xpre][ypre]= false;
            board.occupied[xclick][yclick] = false;
            board.bteam[xclick][yclick] = -1;
            board.rteam[xpre][ypre] = -1;
         }
         /*
            First Case: A miner attacking a bomb
            Second Case: a spy attacks a level 10
            Third Case: Win by level
         */
         else if (board.rteam[xpre][ypre] == 3 && board.bteam[xclick][yclick] == 11 || board.rteam[xpre][ypre] == 1 
         && board.bteam[xclick][yclick] == 10 || board.rteam[xpre][ypre] > board.bteam[xclick][yclick])
         {
            if (board.bteam[xclick][yclick] == 0)
               screen = 3;
            board.shown[xclick][yclick] = true;
            board.shown[xpre][ypre] = false;
            board.rteam[xclick][yclick] = board.rteam [xpre][ypre];
            board.bteam[xclick][yclick] = -1;
            board.rteam[xpre][ypre] = -1;
            board.occupied[xpre][ypre] = false;
         }
         else
         {
            board.shown[xclick][yclick] = true;
            board.shown[xpre][ypre] = false;
            board.rteam[xpre][ypre] = -1;
            board.occupied[xpre][ypre] = false;
         }
      
      }
      else if (team == 1 && turn == 1  && board.bteam[xpre][ypre] != 11)
      {
         /*
            Checks if the peices are of the same value
            If they are both pieces are elliminated
         */
      //          attacker = board.rteam[xpre][ypre]+1;
      //          defender = -board.bteam[xclick][yclick]-1;
         if (board.bteam[xpre][ypre] == board.rteam[xclick][yclick])
         {
            board.shown[xclick][yclick] = false;
            board.shown[xpre][ypre] = false;
            board.occupied[xpre][ypre]= false;
            board.occupied[xclick][yclick] = false;
            board.rteam[xclick][yclick] = -1;
            board.bteam[xpre][ypre] = -1;
         }
         /*
            First Case: A miner attacking a bomb
            Second Case: a spy attacks a level 10
            Third Case: Win by level
         */
         else if (board.bteam[xpre][ypre] == 3 && board.rteam[xclick][yclick] == 11)
         {
            board.shown[xclick][yclick] = true;
            board.shown[xpre][ypre] = false;
            board.bteam[xclick][yclick] = board.bteam [xpre][ypre];
            board.rteam[xclick][yclick] = -1;
            board.bteam[xpre][ypre] = -1;
            board.occupied[xpre][ypre] = false;
         }
         else if (board.bteam[xpre][ypre] == 1 && board.rteam[xclick][yclick] == 10) 
         {
            board.shown[xclick][yclick] = true;
            board.shown[xpre][ypre] = false;
            board.bteam[xclick][yclick] = board.bteam [xpre][ypre];
            board.rteam[xclick][yclick] = -1;
            board.bteam[xpre][ypre] = -1;
            board.occupied[xpre][ypre] = false;
         
         }
         else if (board.bteam[xpre][ypre] > board.rteam[xclick][yclick])
         {
            if (board.rteam[xclick][yclick] == 0)
               screen = 4;
            board.shown[xclick][yclick] = true;
            board.shown[xpre][ypre] = false;
            board.bteam[xclick][yclick] = board.bteam [xpre][ypre];
            board.rteam[xclick][yclick] = -1;
            board.bteam[xpre][ypre] = -1;
            board.occupied[xpre][ypre] = false;
         }
         else
         {
            board.shown[xclick][yclick] = true;
            board.shown[xpre][ypre] = false;
            board.bteam[xpre][ypre] = -1;
            board.occupied[xpre][ypre] = false;
         }
      
      }
   
      boolean rcount = true;
      boolean bcount = true; 
      for (int i = 0; i < 10; i++)
      {
         for (int j = 0; j < 10; j++)
         {
            if (board.rteam[i][j] != -1 && board.rteam[i][j] != 0 && board.rteam[i][j] != 11)
               rcount = false;
            if (board.bteam[i][j] != -1 && board.bteam[i][j] != 0 && board.bteam[i][j] != 11)
               bcount = false;
         }
      }
      if (rcount && bcount)
         screen = 5; //Tie
      else if (rcount) 
         screen = 4; // Blue wins
      else if (bcount)
         screen = 3; // Red wins
   }
   
   /*** move *********************************************
   * Purpose: To make pieces move                                  
   * Parameters: end - indicates if an action has been made
   * Returns: none                                  
   ******************************************************/
   public void move()//P2
   {
      board.speedcal(xpre,ypre);
      boolean end = false;
      int speed = board.speed[xpre][ypre];
      //Checks to make sure its the red player on the red players turn
      if (team == 0 && turn == 0 && board.rteam[xpre][ypre] != -1 && board.special[xclick][yclick] == false) 
      {
         for (int i = 1; i < speed + 1  && end == false; i++)
         {
            if (xpre + i == xclick && ypre == yclick)
            {
            
               if (board.occupied[xclick][yclick] == false && board.rteam[xclick][yclick] == -1)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre + j <=9)
                     {
                        if (board.occupied[j+xpre][ypre] == true && j + xpre != xclick)
                        {
                           b = true;
                        }
                        else if (j + xpre == xclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               }
               else if (board.rteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre + j <=9)
                     {
                        if (board.occupied[j+xpre][ypre] == true && j + xpre != xclick)
                        {
                           b = true;
                        }
                        else if (j + xpre == xclick)
                        {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
            else if (xpre - i == xclick && ypre == yclick)
            {
                                    
            
               if (board.occupied[xclick][yclick] == false && board.rteam[xclick][yclick] == -1)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre - j >=0)
                     {
                        if (board.occupied[xpre-j][ypre] == true &&  xpre - j != xclick)
                        {
                           b = true;
                        }
                        else if (xpre - j == xclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               }
               else if (board.rteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre - j >=0)
                     {
                        if (board.occupied[xpre - j][ypre] == true && xpre - j != xclick)
                        {
                           b = true;
                        }
                        else if ( xpre - j == xclick)
                        {
                        
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
            else if (xpre == xclick && ypre + i == yclick)
            {
            
            
               if (board.occupied[xclick][yclick] == false && board.rteam[xclick][yclick] == -1)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre + j >=0)
                     {
                        if (board.occupied[xpre][ypre + j] == true && j + ypre != yclick)
                        {
                           b = true;
                        }
                        else if (j + ypre == yclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               
               }
               else if (board.rteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre + j >=0)
                     {
                        if (board.occupied[xpre][ypre + j] == true && ypre + j != yclick)
                        {
                           b = true;
                        }
                        else if (ypre + j == yclick)
                        {
                        
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
            else if (xpre == xclick && ypre - i == yclick)
            {
               
            
               if (board.occupied[xclick][yclick] == false && board.rteam[xclick][yclick] == -1)
               {
                  
               
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre - j >=0)
                     {
                        if (board.occupied[xpre][ypre -j] == true && ypre - j != yclick)
                        {
                           b = true;
                        }
                        else if (ypre - j == yclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.rteam[xclick][yclick] = board.rteam[xpre][ypre];
                           board.rteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               }
               else if (board.rteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre + j >=0)
                     {
                        if (board.occupied[xpre][ypre - j] == true && ypre - j != yclick)
                        {
                           b = true;
                        }
                        else if ( ypre - j == yclick)
                        {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
         }
      }
      else if (team == 1 && turn == 1 &&   board.bteam[xpre][ypre] != -1  && board.special[xclick][yclick] == false) 
      {
         for (int i = 1; i < speed + 1  && end == false; i++)
         {
            if (xpre + i == xclick && ypre == yclick)
            {
            
               if (board.occupied[xclick][yclick] == false && board.bteam[xclick][yclick] == -1)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre + j <=9)
                     {
                        if (board.occupied[j+xpre][ypre] == true && j + xpre != xclick)
                        {
                           b = true;
                        }
                        else if (j + xpre == xclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               }
               else if (board.bteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre + j <=9)
                     {
                        if (board.occupied[j+xpre][ypre] == true && j + xpre != xclick)
                        {
                           b = true;
                        }
                        else if (j + xpre == xclick)
                        {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
            else if (xpre - i == xclick && ypre == yclick)
            {
                                    
            
               if (board.occupied[xclick][yclick] == false && board.bteam[xclick][yclick] == -1)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre - j >=0)
                     {
                        if (board.occupied[xpre-j][ypre] == true &&  xpre - j != xclick)
                        {
                           b = true;
                        }
                        else if (xpre - j == xclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               }
               else if (board.bteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(xpre - j >=0)
                     {
                        if (board.occupied[xpre - j][ypre] == true && xpre - j != xclick)
                        {
                           b = true;
                        }
                        else if ( xpre - j == xclick)
                        {
                        
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
            else if (xpre == xclick && ypre + i == yclick)
            {
            
            
               if (board.occupied[xclick][yclick] == false && board.bteam[xclick][yclick] == -1)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre + j >=0)
                     {
                        if (board.occupied[xpre][ypre + j] == true && j + ypre != yclick)
                        {
                           b = true;
                        }
                        else if (j + ypre == yclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               
               }
               else if (board.bteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre + j >=0)
                     {
                        if (board.occupied[xpre][ypre + j] == true && ypre + j != yclick)
                        {
                           b = true;
                        }
                        else if (ypre + j == yclick)
                        {
                        
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
            else if (xpre == xclick && ypre - i == yclick)
            {
               
            
               if (board.occupied[xclick][yclick] == false && board.bteam[xclick][yclick] == -1)
               {
                  
               
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre - j >=0)
                     {
                        if (board.occupied[xpre][ypre -j] == true && ypre - j != yclick)
                        {
                           b = true;
                        }
                        else if (ypre - j == yclick)
                        {
                           board.occupied [xpre][ypre] = false;
                           board.occupied [xclick][yclick] = true;
                           board.speed[xclick][yclick] = board.speed[xpre][ypre];
                           board.speed[xpre][ypre] = 1;
                           board.bteam[xclick][yclick] = board.bteam[xpre][ypre];
                           board.bteam[xpre][ypre] = -1;;
                           end = true;
                        }
                     }
                  }
               }
               else if (board.bteam[xclick][yclick] == -1 && board.occupied [xclick][yclick] == true)
               {
                  boolean b = false;
                  for (int j = 1; j < speed + 1 && b == false; j++)
                  {
                     if(ypre + j >=0)
                     {
                        if (board.occupied[xpre][ypre - j] == true && ypre - j != yclick)
                        {
                           b = true;
                        }
                        else if ( ypre - j == yclick)
                        {
                           attack();
                           end = true;
                        }
                     }
                  }
               }
            }
         }
      }
   
      //This is to find out
      if (end == true)
      {
         if (turn == 0)
         {
            turn = 1;
         }
         else if (turn == 1)
         {
            turn = 0;
         }
      }
   }


   
   
   class Drawing extends JComponent//P4
   {
      //Importing Images
      ImageIcon logo = new ImageIcon("LOGO.png");
      ImageIcon background = new ImageIcon("Capture.PNG");
      ImageIcon BF = new ImageIcon ("BF.png"); //Blue Flag - Considered Number 0
      ImageIcon BS = new ImageIcon ("BS.png"); // Blue Spy - Considered Number 1
      ImageIcon B2 = new ImageIcon ("B2.png"); // Blue 2
      ImageIcon B3 = new ImageIcon ("B3.png"); // Blue 3
      ImageIcon B4 = new ImageIcon ("B4.png"); // Blue 4
      ImageIcon B5 = new ImageIcon ("B5.png"); // Blue 5
      ImageIcon B6 = new ImageIcon ("B6.png"); // Blue 6
      ImageIcon B7 = new ImageIcon ("B7.png"); // Blue 7
      ImageIcon B8 = new ImageIcon ("B8.png"); // Blue 8
      ImageIcon B9 = new ImageIcon ("B9.png"); // Blue 9
      ImageIcon B10 = new ImageIcon ("B10.png"); // Blue 10
      ImageIcon BB = new ImageIcon("BB.png"); //Blue Bomb - Considered Number 11
      ImageIcon BE = new ImageIcon ("BE.png"); //Blue Empty - No number
      ImageIcon RF = new ImageIcon ("RF.png"); //Blue Flag - Considered Number 0
      ImageIcon RS = new ImageIcon ("RS.png"); // Red Spy - Considered Number 1
      ImageIcon R2 = new ImageIcon ("R2.png"); // Red 2
      ImageIcon R3 = new ImageIcon ("R3.png"); // Red 3
      ImageIcon R4 = new ImageIcon ("R4.png"); // Red 4
      ImageIcon R5 = new ImageIcon ("R5.png"); // Red 5
      ImageIcon R6 = new ImageIcon ("R6.png"); // Red 6
      ImageIcon R7 = new ImageIcon ("R7.png"); // Red 7
      ImageIcon R8 = new ImageIcon ("R8.png"); // Red 8
      ImageIcon R9 = new ImageIcon ("R9.png"); // Red 9
      ImageIcon R10 = new ImageIcon ("R10.png"); // Red 10
      ImageIcon RB = new ImageIcon("RB.png"); //Red Bomb - Considered Number 11
      ImageIcon RE = new ImageIcon ("RE.png"); //Blue Empty - No number
      ImageIcon GF = new ImageIcon ("GF.png"); //Blue Flag - Considered Number 0
      ImageIcon GS = new ImageIcon ("GS.png"); // Red Spy - Considered Number 1
      ImageIcon G2 = new ImageIcon ("G2.png"); // Ged 2
      ImageIcon G3 = new ImageIcon ("G3.png"); // Ged 3
      ImageIcon G4 = new ImageIcon ("G4.png"); // Ged 4
      ImageIcon G5 = new ImageIcon ("G5.png"); // Ged 5
      ImageIcon G6 = new ImageIcon ("G6.png"); // Ged 6
      ImageIcon G7 = new ImageIcon ("G7.png"); // Ged 7
      ImageIcon G8 = new ImageIcon ("G8.png"); // Ged 8
      ImageIcon G9 = new ImageIcon ("G9.png"); // Ged 9
      ImageIcon G10 = new ImageIcon ("G10.png"); // Ged 10
      ImageIcon GB = new ImageIcon("GB.png"); //Ged Bomb - Considered Number 11
      ImageIcon GE = new ImageIcon ("GE.png"); //Blue Empty - No number
      
   
      
      public Drawing()
      {
         repaint();
      }
      
            
      public void paint(Graphics g)
      {
      
         if (setVariables == true){  
            selectToPlace[0] = 0; //Flag
            selectToPlace[1] = 1; //Spy
            selectToPlace[2] = 2;
            selectToPlace[3] = 3;
            selectToPlace[4] = 4;
            selectToPlace[5] = 5;
            selectToPlace[6] = 6;
            selectToPlace[7] = 7;
            selectToPlace[8] = 8;
            selectToPlace[9] = 9;
            selectToPlace[10] = 10;
            selectToPlace[11] = 11; //Bomb
         
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
      
         if (screen == 0)
         {
            g.drawImage(logo.getImage(),115,40,750,250,this);
         }
         else if (screen == 1)
         {
            g.drawImage(background.getImage(),50,50,700,700,this);
         
            if((bChoose||rChoose)&& turn == 0){
               Font MonoFont = new Font("Monospaced",Font.BOLD,30);
               g.setFont(MonoFont);
               g.setColor(Color.RED);
               g.drawString("Red Player must place",50,40); 
            }
            else if((bChoose||rChoose)&& turn == 1){
               Font MonoFont = new Font("Monospaced",Font.BOLD,30);
               g.setFont(MonoFont);
               g.setColor(Color.BLUE);
               g.drawString("Blue Player must place",50,40); 
            }
            else if((bChoose||rChoose)==false && turn == 0){
               Font MonoFont = new Font("Monospaced",Font.BOLD,30);
               g.setFont(MonoFont);
               g.setColor(Color.RED);
               g.drawString("Red Player's Turn",50,40); 
            }
            else{
               Font MonoFont = new Font("Monospaced",Font.BOLD,30);
               g.setFont(MonoFont);
               g.setColor(Color.BLUE);
               g.drawString("Blue Player's turn",50,40); 
            }
         
         
            int doneChoosing = 0;
         
         //do{ //Placing
            if(rChoose == true && team == 0){
               doneChoosing = 0;
               for (int a = 0; a < 12; a++){
                  if(selectAmount[a]==0){
                     doneChoosing += 1;
                  }
               }
               if(doneChoosing == 12){
                                   
                  Font MonoFont = new Font("Monospaced",Font.BOLD,20);
                  g.setFont(MonoFont);
               
                  g.setColor(Color.BLACK);
                  g.fillRect(955,710,100,30);
               
                  g.setColor(Color.WHITE);
                  g.drawString("Finish",965,730);
                  
                  if(xReg >= 955 && xReg <= 1055 && yReg >= 710 && yReg <= 810){  
                  
                     rChoose = false;
                     turn = 1;
                     repaint();
                  
                  }
                  
                  else{
                  
                  }
                  
                  
               }
               else{
                  selectPlace();
                  placePiece();
                  repaint();
               }
               
               //System.out.println(""+doneChoosing);           
            }
         
         //while(rChoose == true);   
         
         
            //System.out.println(""+bChoose);
         
         
            if(bChoose == true && team == 1){               
               doneChoosing = 0;
               for (int a = 0; a < 12; a++){
                  if(selectAmount[a]==0){
                     doneChoosing += 1;
                  }
               }
               if(doneChoosing == 12){
               
                  Font MonoFont = new Font("Monospaced",Font.BOLD,20);
                  g.setFont(MonoFont);
               
                  g.setColor(Color.BLACK);
                  g.fillRect(955,710,100,30);
               
                  g.setColor(Color.WHITE);
                  g.drawString("Finish",965,730);
                  
                  if(xReg >= 955 && xReg <= 1055 && yReg >= 710 && yReg <= 810){      
                     bChoose = false;
                     turn = 0;
                     repaint();
                  }
                  
                  else{
                  }
                  
               }
               else{
                  selectPlace();
                  placePiece();
                  repaint();
               }
            }
            
            if(bChoose || rChoose){
               if(xReg >= 800 && xReg <= 900 && yReg >= 710 && yReg <= 740){
                  remove = true;
               }  
            }
            
            try{
               if((bChoose || rChoose) && remove == true){
                  if(xReg >= 50 && xReg <= 750 && yReg >= 50 && yReg <= 750){
                  
                     if(board.occupied[xclick][yclick] == true && team == 0 && xclick <= 3 && xclick >= 0){
                        selectAmount[board.rteam[xclick][yclick]] += 1;
                        board.rteam[xclick][yclick] = -1;
                     
                        remove = false;
                        repaint();
                     }
                     else if(board.occupied[xclick][yclick] == true && team == 1 && xclick <= 9 && xclick >= 6){
                        selectAmount[board.bteam[xclick][yclick]] += 1;
                        board.bteam[xclick][yclick] = -1;
                     
                        remove = false;
                        repaint();
                     }
                     
                     else if(board.occupied[xclick][yclick] == false){
                        remove = false;
                        repaint();
                     }
                     else{
                     }
                  //repaint();
                  }
               }
            }
            catch(Exception e){
               ;
            }
            
            Font MonoFont = new Font("Monospaced",Font.BOLD,20);
            g.setFont(MonoFont);
            
            g.setColor(Color.BLACK);
            g.fillRect(800,710,100,30);
            
            g.setColor(Color.WHITE);
            g.drawString("Remove",810,730);
            
            if (rChoose == false && bChoose == false){
               remove = false;
               gameStart = true;
            }
            
            //Selecting Pieces to Place
            
            for (int p = 0; p < 12; p++){
            
               //Font MonoFont = new Font("Monospaced",Font.BOLD,20);
               //g.setFont(MonoFont);
               g.setColor(Color.black);
               if((p % 2) == 0){
                  g.fillRect(800,50 +55*p,100,100);
                  g.setColor(Color.black);
                  if(p == 0){               
                     g.drawImage(GF.getImage(), 805, 55+55*p, 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],885,140+55*p);
                     g.setColor(Color.black);
                  }
                  if(p == 2){
                     g.drawImage(G2.getImage(), 805, 55+55*p, 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],885,140+55*p);
                     g.setColor(Color.black);
                  }
                  if(p == 4){
                     g.drawImage(G4.getImage(), 805, 55+55*p, 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],885,140+55*p);
                     g.setColor(Color.black);
                  }
                  if(p == 6){
                     g.drawImage(G6.getImage(), 805, 55+55*p, 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],885,140+55*p);
                     g.setColor(Color.black);
                  }
                  if(p == 8){
                     g.drawImage(G8.getImage(), 805, 55+55*p, 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],885,140+55*p);
                     g.setColor(Color.black);
                  }
                  if(p == 10){
                     g.drawImage(G10.getImage(), 805, 55+55*p, 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],885,140+55*p);
                     g.setColor(Color.black);
                  }
               }
               else{
                  g.fillRect(950,50 +55*(p-1),100,100);
                  g.setColor(Color.black);
                  if(p == 1){
                     g.drawImage(GS.getImage(), 955, 55+55*(p-1), 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],1035,140+55*(p-1));
                     g.setColor(Color.black);
                  }
                  if(p == 3){
                     g.drawImage(G3.getImage(), 955, 55+55*(p-1), 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],1035,140+55*(p-1));
                     g.setColor(Color.black);
                  }
                  if(p == 5){
                     g.drawImage(G5.getImage(), 955, 55+55*(p-1), 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],1035,140+55*(p-1));
                     g.setColor(Color.black);
                  }
                  if(p == 7){
                     g.drawImage(G7.getImage(), 955, 55+55*(p-1), 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],1035,140+55*(p-1));
                     g.setColor(Color.black);
                  }
                  if(p == 9){
                     g.drawImage(G9.getImage(), 955, 55+55*(p-1), 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],1035,140+55*(p-1));
                     g.setColor(Color.black);
                  }
                  if(p == 11){
                     g.drawImage(GB.getImage(), 955, 55+55*(p-1), 90, 90, this);
                     g.setColor(Color.GREEN);
                     g.drawString(""+selectAmount[p],1035,140+55*(p-1));
                     g.setColor(Color.black);
                  }
               }
            }
            
            //System.out.println (xReg +","+ xclick);
            
            //System.out.println(selectAmount[0]);
            
            
         
         
         
         
            boolean end = false;
         
         
            //Says that if where you clicked was not in a square it will not do anything
            if(xclick != -1 || yclick != -1)
            {
               //Says its reds turn
               if (turn == 0 && team == 0)
               {
               //Checks to see if the place they clicked where a peice is
                  if (board.rteam[xclick][yclick] != -1)
                  {
                  /*
                  r < speed + 1 as we need to make it so that the box isn't right on the square you clicked
                  end is to see if we need to end the program early because somethings isnt suppost to be drawn
                  r + xclick < 10 is so that we have no errors of out of bonds
                  */
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end == false &&  xclick + r < 10; r++)
                     {
                        if (board.rteam[xclick+r][yclick] != -1 || board.special [xclick+r][yclick] == true)
                           end = true;
                        else
                        {
                           g.setColor(Color.green);
                           if (board.occupied[r+xclick][yclick] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick+r],board.loc[yclick], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end == false && xclick - r >= 0; r++)
                     {
                        if (board.rteam[xclick-r][yclick] != -1 || board.special [xclick-r][yclick] == true)
                        {
                           end = true;
                        }
                        else
                        {
                        
                           g.setColor(Color.green);
                           if (board.occupied[xclick-r][yclick] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick-r],board.loc[yclick],70,70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end == false && r + yclick < 10; r++)
                     {
                        if (board.rteam[xclick][yclick+r] != -1 || board.special [xclick][yclick+r] == true)
                        {
                           end = true;
                        }
                        else
                        {
                        
                           g.setColor(Color.green);
                           if (board.occupied[xclick][r+yclick] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick],board.loc[yclick+r],70,70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 &&  end == false && yclick - r >= 0; r++)
                     {
                        if (board.rteam[xclick][yclick-r] != -1 || board.special [xclick][yclick-r] == true)
                        {
                           end = true;
                        }
                        else
                        {
                           g.setColor(Color.green);
                           if (board.occupied[xclick][yclick-r] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick],board.loc[yclick-r],70,70);
                        }
                     }
                     end = false;
                  
                  }
               }
               else if (team == 1 && turn == 1)
               {
                  if (board.bteam[xclick][yclick] != -1)
                  {
                     /*
                     r < speed + 1 as we need to make it so that the box isn't right on the square you clicked
                     end is to see if we need to end the program early because somethings isnt suppost to be drawn
                     r + xclick < 10 is so that we have no errors of out of bonds
                     */
                  
                  
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end == false &&  xclick + r < 10; r++)
                     {
                        if (board.special [xclick+r][yclick] == true)
                           end = true;
                        else
                        {
                           g.setColor(Color.green);
                           if (board.occupied[r+xclick][yclick] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick+r],board.loc[yclick], 70, 70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end == false && xclick - r >= 0; r++)
                     {
                        if (board.bteam[xclick-r][yclick] != -1 || board.special [xclick-r][yclick] == true)
                        {
                           end = true;
                        }
                        else
                        {
                        
                           g.setColor(Color.green);
                           if (board.occupied[xclick-r][yclick] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick-r],board.loc[yclick],70,70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end == false && xclick - r <= 0; r++)
                     {
                        if (board.special [xclick-r][yclick]== true)
                           end = true;
                        else
                        {
                           g.setColor (Color.green);
                           if (board.occupied [xclick-r][yclick] == true)
                           {
                              end = true;
                              g.setColor (Color.orange);
                           }
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 && end == false && r + yclick < 10; r++)
                     {
                        if (board.bteam[xclick][yclick+r] != -1 || board.special [xclick][yclick+r] == true)
                        {
                           end = true;
                        }
                        else
                        {
                        
                           g.setColor(Color.green);
                           if (board.occupied[xclick][r+yclick] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick],board.loc[yclick+r],70,70);
                        }
                     }
                     end = false;
                     for (int r = 1; r < board.speed[xclick][yclick] + 1 &&  end == false && yclick - r >= 0; r++)
                     {
                        if (board.bteam[xclick][yclick-r] != -1 || board.special [xclick][yclick-r] == true)
                        {
                           end = true;
                        }
                        else
                        {
                           g.setColor(Color.green);
                           if (board.occupied[xclick][yclick-r] == true)
                           {
                              end = true;
                              g.setColor(Color.orange);
                           }
                           g.fillRect(board.loc[xclick],board.loc[yclick-r],70,70);
                        }
                     }
                     end = false;
                  }
               }
            }
         }
         if(screen == 3){
            frame.getContentPane().setBackground(Color.RED);
            
            Font MonoFont = new Font("Monospaced",Font.BOLD,200);
            g.setFont(MonoFont);
            
            g.setColor(Color.WHITE);
            g.drawString("Red Wins",80,400);        
         }
         if(screen == 4){
            frame.getContentPane().setBackground(Color.BLUE);
            
            Font MonoFont = new Font("Monospaced",Font.BOLD,200);
            g.setFont(MonoFont);
            
            g.setColor(Color.WHITE);
            g.drawString("Blue Wins",10,400);        
         }
         if(screen == 5){
            frame.getContentPane().setBackground(Color.BLACK);
            
            Font MonoFont = new Font("Monospaced",Font.BOLD,200);
            g.setFont(MonoFont);
            
            g.setColor(Color.WHITE);
            g.drawString("Tie",350,400);        
         }
         
         if (screen == 1)
         {
         
         //Used to put the pieces on the board
            for (int i = 0; i < 10; i++)
            {
               for (int j = 0; j < 10; j++)
               {
               //Makes it so that you can see your own pieces and those revealed for the other team
                  if (board.shown [i][j] == true|| team == 0)
                  {
                     if (board.rteam[i][j] == 0)
                        g.drawImage(RF.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 1)
                        g.drawImage(RS.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 2)
                        g.drawImage(R2.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 3)
                        g.drawImage(R3.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 4)
                        g.drawImage(R4.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 5)
                        g.drawImage(R5.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 6)
                        g.drawImage(R6.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 7)
                        g.drawImage(R7.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 8)
                        g.drawImage(R8.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 9)
                        g.drawImage(R9.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 10)
                        g.drawImage(R10.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.rteam[i][j] == 11)
                        g.drawImage(RB.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                  }
                  //Makes it if you are on the other side you can't see the pieces if they are not revealed
                  else if (board.rteam[i][j] != -1)
                     g.drawImage(RE.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
               
               
               //Makes it so that you can see your own pieces and those revealed for the other team
                  if (board.shown [i][j] == true|| team == 1)
                  {   
                     if (board.bteam[i][j] == 0)
                        g.drawImage(BF.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 1)
                        g.drawImage(BS.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 2)
                        g.drawImage(B2.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 3)
                        g.drawImage(B3.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 4)
                        g.drawImage(B4.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);       
                     else if (board.bteam[i][j] == 5)
                        g.drawImage(B5.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 6)
                        g.drawImage(B6.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 7)
                        g.drawImage(B7.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 8)
                        g.drawImage(B8.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 9)
                        g.drawImage(B9.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 10)
                        g.drawImage(B10.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                     else if (board.bteam[i][j] == 11)
                        g.drawImage(BB.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
                  }
                  //Makes it if you are on the other side you can't see the pieces if they are not revealed
                  else if (board.bteam[i][j] != -1)
                     g.drawImage(BE.getImage(), board.loc[i]+ 5, board.loc[j]+ 5, 60, 60, this);
               }
            
            }
         }
      }
   }

   
      
   //P5
   class ClickHandler extends MouseAdapter
   {
      public void mousePressed (MouseEvent e)
      {
      
         int other = 0;
         int x = -1;
         int y = -1;
      //Finds the quardants of where you clicked
         if (turn == 0){
            xReg = e.getX();
            yReg = e.getY();
         
         
            if (screen == 1){
            
            //if (xReg >= 50 && xReg <= 750 && yReg >= 50 && yReg <= 750){
            
               for (int i = 0; i < 10 && other < 2;i++)
               {
                  if (e.getX()> board.loc[i] && e.getX() <= board.loc[i+1])
                  {
                     x = i;
                     other += 1;
                  }
                  if (e.getY()> board.loc[i] && e.getY() <= board.loc[i+1])
                  {         
                     y = i;
                     other +=1;
                  }
               }
            
               boolean b = true;
               
               if ( x == -1)
                  b = false;
               
               if (y == -1)
                  b = true;
               
               if (b){
                  xpre = xclick;
                  ypre = yclick;
                  xclick = x;
                  yclick = y;
                  other = 0;
                  if (gameStart == true){
                     move();
                     //out.println(team);
                     //out.flush();
                  }
               }
            
            //} 
            // else{
            //xclick = -1;
            //yclick = -1;
            //  } 
            }
              
         }
      //          out.println(team);
      //          for (int i = 0; i < 10; i++){
      //             for (int j = 0; i < 10; i++){
      //                out.println(board.rteam[i][j]);
      //                out.println(board.bteam[i][j]);
      //                out.println(board.occupied[i][j]);
      //                out.println(board.special[i][j]);
      //                out.println(board.shown[i][j]);
      //             }
      //          }
      //          out.flush();
         
         draw.repaint();
         
         
      
      }
      
      public void mouseReleased(MouseEvent e){
      
      //          if (rChoose == false && rbool == true)
      //          {
      //             out.println(turn);
      //             for (int i = 0; i < 10; i++){
      //                for (int j = 0; j < 10; j++){
      //                   out.println(board.rteam[i][j]);
      //                   out.println(board.bteam[i][j]);
      //                   out.println(board.occupied[i][j]);
      //                   out.println(board.special[i][j]);
      //                   out.println(board.shown[i][j]);
      //                }
      //             }
      //             out.println(xpre);
      //             out.println(ypre);
      //             out.println(xclick);
      //             out.println(yclick);
      //             out.flush();
      //             rbool = false;
      //          }
            
            
         
         
         out.println(turn);
         for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
               out.println(board.rteam[i][j]);
               out.println(board.bteam[i][j]);
               out.println(board.occupied[i][j]);
               out.println(board.special[i][j]);
               out.println(board.shown[i][j]);
            }
         }
         out.println(xpre);
         out.println(ypre);
         out.println(xclick);
         out.println(yclick);
         out.println(screen);
         out.flush();
            //row = false;
         
      }
   }


   void selectPlace(){ //select a piece to place
      for (int q = 0; q < 12; q++){
         if((q % 2) == 0){
            if(xReg >= 800 && xReg <= 900 && yReg >=(50+(55*q)) && yReg <= ((50+(55*q))+100)){
               selectValue = selectToPlace[q];
            }
                  
                  //800,50 +55*p,100,100);                 
         }
         else{
            if(xReg >= 950 && xReg <= 1050 && yReg >=(50+(55*(q - 1))) && yReg <= ((50+(55*q))+100)){
               selectValue = selectToPlace[q];
            }   //950,50 +55*(p-1),100,100);                  
         }
      }
   }
   
   void placePiece(){
      if (selectValue >= 0 && selectAmount[selectValue] > 0){
         if(xReg >= 50 && xReg <= 750 && yReg >= 50 && yReg <= 750 && board.canPlace(xclick,yclick,team) == true){
            
            try{            
               if(board.occupied[xclick][yclick] == true && team == 0){
                  selectAmount[board.rteam[xclick][yclick]] += 1;
               }
               else if(board.occupied[xclick][yclick] == true && team == 1){
                  selectAmount[board.bteam[xclick][yclick]] += 1;
               }
               else{
               }
            
            }
            catch(ArrayIndexOutOfBoundsException e){
               ;
            }
            board.setPiece(xclick,yclick,team,selectValue);
            selectAmount[selectValue] -= 1;
            selectValue = -1;
         }
      }
      else{
      }
   }
   
   public void multiGame() throws IOException
   {
      System.out.println("Starting server");
      Socket sock;// = new Socket("120.0.0.1",4444);
      ServerSocket servsock = new ServerSocket(4444);
      //sock = servsock.accept();
      System.out.println(servsock.getInetAddress());
      
   
      //String s="";
      
      while (true)
      {
         sock = servsock.accept();
         System.out.println(servsock.getChannel());
         System.out.println("sock = " + sock);
         BufferedReader in= new BufferedReader(new InputStreamReader(sock.getInputStream()));
         out = new PrintStream(sock.getOutputStream());
         
         while(true){         
            if (in.ready()) // && turn == 1)
            {	
               int t = Integer.parseInt(in.readLine());
            //team = t;
               turn = t;
            
               for (int i = 0; i < 10; i++){
                  for (int j = 0; j < 10; j++){
                     board.rteam[i][j] = Integer.parseInt(in.readLine());
                     board.bteam[i][j] = Integer.parseInt(in.readLine());
                     board.occupied[i][j] = Boolean.parseBoolean(in.readLine());
                     board.special[i][j] = Boolean.parseBoolean(in.readLine());
                     board.shown[i][j] = Boolean.parseBoolean(in.readLine());
                  }
               }
               xpre = Integer.parseInt(in.readLine());
               ypre = Integer.parseInt(in.readLine());
               xclick = Integer.parseInt(in.readLine());
               yclick = Integer.parseInt(in.readLine());
               screen = Integer.parseInt(in.readLine());
            
               draw.repaint();
            }
         }
      }
   }
}