import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board
{
   int[][] rteam  = new int [10][10];
   int[][] bteam  = new int [10][10];
   boolean[][] occupied = new boolean [10][10];
   boolean[][] special = new boolean [10][10];
   boolean[][] shown = new boolean [10][10];
   int[][] speed= new int [10][10];
   int[] loc = new int [11];
   boolean first = true;
   
   public Board ()
   {
      if (first)//if this is the first time he code is run
      {
         int setLoc = 50;//Where the top left corner is
         loc[0] = 50;
         first = false; //Makes it so that this part of the program will not run again
         //Sets where the quardinates for the peices
         for (int i = 1; i < 11; i++)
         {
            setLoc += 70; //The squares are 70 by 70
            loc[i] = setLoc;
         }
         //The next part sets where the pieces are going to be to occupied
         for (int i = 0; i < 4; i++)
         {
            for (int j =0; j < 10; j++)
            {
               occupied [i][j] = true;
               rteam[i][j] = -1;
            }
         }
         for (int i = 6; i < 10; i++)
         {
            for (int j = 0; j < 10; j++)
            {
               occupied [i][j] = true;
               bteam [i][j] = -1;
            }
         }
         //Sets up where the pieces are not going to be
         for (int i = 4; i < 10 ; i++)
         {
            for (int j =0; j < 10; j++)
            {
               rteam[i][j] = -1;
            }
         }
         for (int i = 0; i < 6; i++)
         {
            for (int j = 0; j < 10; j++)
            {
               bteam [i][j] = -1;
            }
         }
         //Makes it so that all pieces have not been shown
         for (int i = 0;i < 10;i++)
            for (int j =0;j<10;j++)
               shown[i][j] = false;
         
         //Blocked off water
         occupied [4][2] = true;
         occupied [5][2] = true;
         occupied [4][3] = true;
         occupied [5][3] = true;
         occupied [4][6] = true;
         occupied [5][6] = true;
         occupied [4][7] = true;
         occupied [5][7] = true;
         special [4][2] = true;
         special [5][2] = true;
         special [4][3] = true;
         special [5][3] = true;
         special [4][6] = true;
         special [5][6] = true;
         special [4][7] = true;
         special [5][7] = true;  
      }
   }
   
   public void speedcal(int x, int y)
   {
      for (int i = 0; i < 10; i++)
         for (int j = 0; j < 10; j++)
         {
            speed[i][j] = 1;
            //Does the special cases of speed
            if (rteam[i][j] == 2 || bteam[i][j] == 2)
               
               speed[i][j] = 10; //Speed of a level 2 is infinite it can go anywhere
               
            else if (rteam[i][j] == 0 || rteam[i][j] == 11 || bteam[i][j] == 0 || bteam[i][j] == 11)
               speed[i][j] = 0; //The speed of the flag and the bomb is zero
         }
   }
   
   
   
   
   void setPiece(int x, int y, int player, int piece){
      if (player == 0){
         rteam[x][y] = piece;
      }
      else if (player == 1){
         bteam[x][y] = piece;
      }
   }
   
   boolean canPlace(int x, int y, int player){
      if (player == 0 && (x <= 3)){
         return true;
      }
      else if( player == 1 && (x >= 6)){
         return true;
      }
      else{
         return false;
      }
   }
}