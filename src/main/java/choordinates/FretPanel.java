package choordinates;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import static java.lang.Math.min;

//TODO reduce amount of stuff imported.
//import java.awt.Color;
//import java.awt.Graphics;
import javax.swing.*;

public class FretPanel extends JPanel implements MouseListener, MouseMotionListener {
    //Member constants
    private final int mMaxStrings = 8;
    private final int mMaxFrets = 24;
    
    //Member variables
    private int startX, startY;
    private int endX, endY;
    private int mNumStrings = 6;
    private int mNumFrets = 24;
    private int mFirstFret = -1;
    private boolean mLefty = false;		//Set to true for left-handed chord shapes.
    private String[] mStringNames = new String[mMaxStrings];

    private char[][] mFrets=new char[mMaxStrings][mMaxFrets];  //Let's not be clever.  Just buffer max space.
    private boolean mOrientX = true;  //Set true for horizontal strings, false for vertical

    public void setOrientation(boolean orientation) {
    	//Sets the orientation of the fretboard view in the window.
    	mOrientX = orientation;
    }
    
    public void setNumFrets(int frets)
    {
    	if (frets > 0 && frets <= mMaxFrets)
    	{
    		mNumFrets = frets;
    	}
    	updateTuning();
    	refresh();
    }
    
    public void refresh()
    {
    	 SwingUtilities.invokeLater(() -> {
	            this.repaint();
	        });
    }
    
    public void updateTuning()
    {
    	ChoordData choord_data = ChoordData.getInstance();
    	
    	int tuning_id = choord_data.getCurrentTuning();
    	
    	if (tuning_id == -1)
    	{
    		//No tuning set, default.
    		 mStringNames[0] = "E";
    	     mStringNames[1] = "A";
    	     mStringNames[2] = "D";
    	     mStringNames[3] = "G";
    	     mStringNames[4] = "B";
    	     mStringNames[5] = "e";
    	     mNumStrings = 6;
    	}
    	else
    	{
    		ToneChord tuning = choord_data.getTuning(tuning_id);
    		mNumStrings = tuning.getNumNotes();
    		
    		System.out.print("There are " + tuning.getNumNotes() + " named ");

    		for (int i=0;i<mNumStrings; ++i)
    		{
    			System.out.print(" " + tuning.getNote(i).getName());
    			mStringNames[i] = tuning.getNote(i).getName();
    		}
    		System.out.println("");
    	}	
    }
    
    public FretPanel() {
    	ChoordData.getInstance();
    	
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    private String getRomanNumeral(int num) {
    	String result ="0";
    	
    	if (num == 0)
    	{
    		return result;
    	}
    	result = "";
    	
        int[] values = {10, 9, 5, 4, 1};
        String[] roman = {"X", "IX", "V", "IV", "I"};

        result="";
        
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
            	result += roman[i];
                num -= values[i];
            }
        }

        return result;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //Get the height and width of the panel. 
        int pany = getHeight();
        int panx = getWidth();
        
        //Max size of cells in the space of the pane.
        int max_cell_x, max_cell_y;
        
        updateTuning();
        //TEMPORARY FOR TESTING BEGIN
        //Mocked E-Major chord
        mFrets[0][0] = 'O';
        mFrets[1][2] = 'O';
        mFrets[2][2] = 'O';
        mFrets[3][1] = 'O';
        mFrets[4][0] = 'O';
        mFrets[5][0] = 'O';
        
        //TEMPORARY FOR TESTING END
        
        //Number of cells in x and y.
        int cells_x = mNumFrets;
        int cells_y = mNumFrets;
        
        //Compute cell size based on orientation.
        //Add one to string to have space for drawing fret names.
        //Add one to frets to have space for drawing string names.
        if (mOrientX)
        {
        	max_cell_x = panx / (mNumFrets+1);
        	max_cell_y = pany / (mNumStrings+1);
        	cells_y = mNumStrings;
        }
        else
        {
        	max_cell_x = panx / (mNumStrings+1);
        	max_cell_y = pany / (mNumFrets+1);  
        	cells_x = mNumStrings;
        }
        
        //Several convenience ints.
        final int cell_size = Math.min(max_cell_x, max_cell_y);
        final int cell_half = cell_size/2; 
        final int cell34 = cell_size * 3 / 4;
        final int cell23 = cell_size * 2 / 3;
        final int cell16 = cell_size / 6;
        final int cell14 = cell_size / 4;
        
        int[] labeled_frets = {0, 3, 5, 7, 9, 12, 15, 17, 19, 21, 24};
 
        // Set the color to red
//      g.setColor(Color.RED); 


        if (startX != 0 && startY != 0) {
        	// Set the color to red
        	g.setColor(Color.BLACK); 
        	g.drawLine(startX, startY, endX, endY);
        }
        
        int mouse_x = endX - (cell_size + cell_half);
        int mouse_y = endY - (cell_size);
        if ((mouse_x > 0 && mouse_x < (cells_x - 1) * cell_size)
        	&& (mouse_y > 0 && mouse_y < (cells_y) * cell_size))
        {
        	int x = mouse_x / cell_size;
        	int y = mouse_y / cell_size;
        	g.setColor(Color.CYAN);
        	g.fillRect((x+1) * cell_size + cell_half,  (y+1) * cell_size, cell_size, cell_size);
        }
        g.setColor(Color.BLACK);
      
        //TODO refactor this orientx orienty into a less duplicate-code function some day.
        if (mOrientX)  //Horizontal strings, vertical frets
        {
        	for (int x=0; x<mNumFrets; ++x)
        	{
        		g.drawLine( (x+1) * cell_size + cell_half,  cell_size + cell_half,  (x+1) * cell_size + cell_half, cell_size * mNumStrings + cell_half);
        		if (mFirstFret + x == 0)
        		{
            		g.drawLine( (x+1) * cell_size + cell_half - 2,  cell_size + cell_half,  (x+1) * cell_size + cell_half -2, cell_size * mNumStrings + cell_half);

        		}
        	}
        	for (int y=0; y<mNumStrings; ++y)
        	{
        		g.drawLine(cell_size + cell_half, (y+1) * cell_size + cell_half, cell_size * mNumFrets + cell_half, (y+1) * cell_size + cell_half);
        		if (mLefty)
        		{
        			g.drawString(mStringNames[y], cell_half, (y+1) * cell_size + cell_half);
        		}
        		else
        		{
        			g.drawString(mStringNames[y], cell_half, (mNumStrings - y) * cell_size + cell_half);
        		}
        	}
        	
        	for (int i=0; i<mNumFrets; ++i)
        	{
        		//Draw the string labels
        		int fret_no = mFirstFret+i;
        		if (Arrays.binarySearch(labeled_frets, fret_no) >= 0)
        		{
        			int offset = 0;
        			if (mFirstFret == -1)
        			{
        				offset = cell_size;
        			}
        			String numeral = getRomanNumeral(fret_no);
        			//TODO get graphic width of string and use it to calculate the offset, not this pile of goo.
        			g.drawString(numeral, cell_size * i + offset + cell_half - 5, cell_size);
        		}
        		//Draw the selected frets;
        		for (int j=0; j<mNumStrings; ++j)
        		{
        			if (mFrets[j][i] != 0)
        			{
        				if (mLefty)
        				{
        					g.fillOval( (i+1) * cell_size + cell23, (j+1) * cell_size + cell16, cell34, cell34);
        				}
        				else
        				{
        					g.fillOval( (i+1) * cell_size + cell23, (mNumStrings - j) * cell_size + cell16, cell34, cell34);
        				}
        			}
        		}
        	}
        }
        else
        {				//Horizontal fret, vertical strings.
        	int strnum = 0;

        	for (int x=0; x < mNumStrings; ++x)
        	{
        		g.drawLine( (x+1) * cell_size + cell_half, cell_size + cell_half, (x+1) * cell_size + cell_half, cell_size * mNumFrets + cell_half );
        		if (mLefty)
        		{
        			g.drawString(mStringNames[x], (mNumStrings - x) * cell_size + cell14, cell_size);
        		}
        		else
        		{
        			g.drawString(mStringNames[x], (x+1) * cell_size + cell14, cell_size);
        		}
        	}
        	for (int y=cell_half; y < cell_size * mNumFrets; y += cell_size)
        	{
        		g.drawLine(cell_size + cell_half, cell_size + y, cell_size * mNumStrings + cell_half, cell_size + y);
        	}
        	for (int y=0; y < mNumFrets; ++y)
        	{
        		g.drawLine(cell_size + cell_half, (y+1) * cell_size + cell_half, cell_size * mNumStrings + cell_half, (y+1) * cell_size + cell_half);
        		if (mFirstFret + y == 0)
        		{
            		g.drawLine(cell_size + cell_half, (y+1) * cell_size + cell_half - 2, cell_size * mNumStrings + cell_half, (y+1) * cell_size + cell_half -2);
        		}
        	}
        	for (int i=0; i<mNumFrets; ++i)
        	{
        		int fret_no = mFirstFret+i;
        		if (Arrays.binarySearch(labeled_frets, fret_no) >= 0)
        		{
        			int offset = 0;
        			if (mFirstFret == -1)
        			{
        				offset = cell_size;
        			}
        			String numeral = getRomanNumeral(fret_no);
        			g.drawString(numeral, (cell_size/3), cell_size * i + offset + cell_half + 5);
        		}
        		//Draw the selected frets;
        		for (int j=0; j<mNumStrings; ++j)
        		{
        			if (mFrets[j][i] != 0)
        			{
        				if (mLefty)
        				{
        					g.fillOval( (mNumStrings - j) * cell_size + cell16, (i+1) * cell_size + cell_half+ cell16, cell34, cell34);
        				}
        				else
        				{
        					g.fillOval( (j+1) * cell_size + cell16, (i+1) * cell_size + cell_half + cell16, cell34, cell34);
        				}
        			}
        		}
        	}
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        repaint(); // Redraw the panel to show the line
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        repaint(); // Redraw the panel as the mouse is dragged
    }

    // Unused MouseListener/MouseMotionListener methods
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
