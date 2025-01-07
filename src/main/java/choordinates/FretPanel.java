package choordinates;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

//TODO reduce amount of stuff imported.
//import java.awt.Color;
//import java.awt.Graphics;
import javax.swing.*;

public class FretPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
    //Member constants
    private final int mMaxStrings = 8;
    private final int mMaxFrets = 26;  //The nut is fret 0.
    
    //Member variables
    private int startX, startY;
    private int endX, endY;
    private int mNumStrings = 6;
    private int mNumFrets = 24;
    private int mFirstFret = -1;
    private boolean mLefty = false;		//Set to true for left-handed chord shapes.
    private String[] mStringNames = new String[mMaxStrings];

    private int[][] mFrets=new int[mMaxStrings][mMaxFrets];  //Let's not be clever.  Just buffer max space.
    private boolean mOrientX = true;  //Set true for horizontal strings, false for vertical

    //Root Note and Search Chord inform the fret display.
    private ToneNote mRootNote = new ToneNote();
    private IntervalChord mSearchChord = new IntervalChord();
        
    public void setOrientation(boolean orientation) {
    	//Sets the orientation of the fretboard view in the window.
    	mOrientX = orientation;
    }
    
    public void setNumFrets(int frets)
    {
    	if (frets < 0)
    	{
    		frets = 0;
    	}
    	else if (frets > (mMaxFrets+1))
    	{
    		mNumFrets = mMaxFrets;
    	}
    	mNumFrets = frets+1;
    	
    	updateTuning();
    	refresh();
    }
    
    public void refresh()
    {
    	 SwingUtilities.invokeLater(() -> {
	            this.repaint();
	        });
    }
    
    public void flushFrets()
    {
    	//Array.fill is stupid.  Give me memset() 
    	for (int i=0;i<mMaxStrings; ++i)
    	{
    		for (int j=0;j<mMaxFrets; ++j)
    		{
    			mFrets[i][j] = -1;
    		}
    	}
    }
    
    public void markFrets()
    {
    	ChoordData choord_data = ChoordData.getInstance();
    	ToneChord tuning = choord_data.getTuning( choord_data.getCurrentTuning() );
    	IntervalChord chord = mSearchChord;
    	
    	int root_tone = mRootNote.getSemitone();
    	    	
    	flushFrets();   //Floosh
    	
    	//Loop through the strings on the instrument
    	for (int i=0; i<tuning.getNumNotes(); ++i)
    	{
    		ToneNote string_note = tuning.getNote(i);
    		
    		int string_tone = string_note.getSemitone();
    		
    		//Loop through the notes in the selected chord
    		for (int j=0; j<chord.getNumNotes(); ++j)
    		{
    			int interval_tone = chord.getNote(j).getSemitone();
    			
    			if (interval_tone < 0 )
    			{
    				interval_tone = interval_tone % 12;
    				if (interval_tone < 0)
    				{
    					interval_tone += 12;
    				}
    			}
    			
    			int chord_tone = ( root_tone + interval_tone ) % 12;
    			for (int fret=0;fret<mNumFrets; ++fret)
    			{
    				if ( ( ( string_tone + fret ) % 12 ) == chord_tone)
    				{
    					mFrets[i][fret] = j;
    				}
    			}
    		}
    	}
    	refresh();
    }
    
    public void setRootAndChord(ToneNote note, IntervalChord chord)
    {
    	mRootNote = note;
    	mSearchChord = chord;
    	markFrets();
    }
    
    public void updateTuning()
    {
    	ChoordData choord_data = ChoordData.getInstance();
    	
    	int tuning_id = choord_data.getCurrentTuning();
    	
    	if (tuning_id == -1)
    	{
    	     mNumStrings = 6;
    	}
    	else
    	{
    		ToneChord tuning = choord_data.getTuning(tuning_id);
    		mNumStrings = tuning.getNumNotes();
    		
    		for (int i=0;i<mNumStrings; ++i)
    		{
    			mStringNames[i] = tuning.getNote(i).getName();
    		}
    	}	
    }
    
    public FretPanel() {
    	ChoordData.getInstance();
    	flushFrets();
    	
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
      
        int fret_lines = mNumFrets;
        if (mFirstFret==0)
        {
        	fret_lines ++;
        }
        
        //TODO refactor this orientx orienty into a less duplicate-code function some day.
        if (mOrientX)  //Horizontal strings, vertical frets
        {
        	for (int x=0; x<fret_lines; ++x)
        	{
        		g.drawLine( (x+1) * cell_size + cell_half,  cell_size + cell_half,  (x+1) * cell_size + cell_half, cell_size * mNumStrings + cell_half);
        		if (((mFirstFret + x) % 12) == 0)
        		{
        			//Draw double-lines at 0 12 24
            		g.drawLine( (x+1) * cell_size + cell_half - 2,  cell_size + cell_half,  (x+1) * cell_size + cell_half -2, cell_size * mNumStrings + cell_half);

        		}
        	}
        	for (int y=0; y<mNumStrings; ++y)
        	{
        		g.drawLine(cell_size + cell_half, (y+1) * cell_size + cell_half, cell_size * fret_lines + cell_half, (y+1) * cell_size + cell_half);
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
        		if (i < (mNumFrets-1))
        		{
	        		for (int j=0; j<mNumStrings; ++j)
	        		{
	        			if (mFrets[j][i] != -1)
	        			{
	        				if (mLefty)
	        				{
	        					g.setColor(IntervalNote.getColor(mFrets[j][i]));
	        					g.fillOval( (i+1) * cell_size + cell23, (j+1) * cell_size + cell16, cell34, cell34);
	        					g.setColor(Color.BLACK);
	        					g.drawOval( (i+1) * cell_size + cell23, (j+1) * cell_size + cell16, cell34, cell34);
	        				}
	        				else
	        				{
	        					g.setColor(IntervalNote.getColor(mFrets[j][i]));
	        					g.fillOval( (i+1) * cell_size + cell23, (mNumStrings - j) * cell_size + cell16, cell34, cell34);
	        					g.setColor(Color.BLACK);
	        					g.drawOval( (i+1) * cell_size + cell23, (mNumStrings - j) * cell_size + cell16, cell34, cell34);
	        				}
	        			}
	        		}
        		}
        	}
        }
        else
        {				//Horizontal fret, vertical strings.
        	for (int x=0; x < mNumStrings; ++x)
        	{
        		g.drawLine( (x+1) * cell_size + cell_half, cell_size + cell_half, (x+1) * cell_size + cell_half, cell_size * fret_lines + cell_half );
        		if (mLefty)
        		{
        			g.drawString(mStringNames[x], (mNumStrings - x) * cell_size + cell14, cell_size);
        		}
        		else
        		{
        			g.drawString(mStringNames[x], (x+1) * cell_size + cell14, cell_size);
        		}
        	}
        	for (int y=cell_half; y < cell_size * fret_lines; y += cell_size)
        	{
        		g.drawLine(cell_size + cell_half, cell_size + y, cell_size * mNumStrings + cell_half, cell_size + y);
        	}
        	for (int y=0; y < fret_lines; ++y)
        	{
        		g.drawLine(cell_size + cell_half, (y+1) * cell_size + cell_half, cell_size * mNumStrings + cell_half, (y+1) * cell_size + cell_half);
        		if (((mFirstFret + y) % 12) == 0)
        		{
        			//Draw double-lines at 0 12 24
            		g.drawLine(cell_size + cell_half, (y+1) * cell_size + cell_half - 2, cell_size * mNumStrings + cell_half, (y+1) * cell_size + cell_half -2);
        		}
        	}
        	for (int i=0; i<mNumFrets; ++i)
        	{
        		//Draw fret numbers.
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
        			if (i < (mNumFrets-1))
        			{
	        			if (mFrets[j][i] != -1)
	        			{
	        				if (mLefty)
	        				{
	        					g.setColor(IntervalNote.getColor(mFrets[j][i]));
	        					g.fillOval( (mNumStrings - j) * cell_size + cell16, (i+1) * cell_size + cell_half+ cell16, cell34, cell34);
	        					g.setColor(Color.BLACK);
	        					g.fillOval( (mNumStrings - j) * cell_size + cell16, (i+1) * cell_size + cell_half+ cell16, cell34, cell34);
	        				}
	        				else
	        				{
	        					g.setColor(IntervalNote.getColor(mFrets[j][i]));
	        					g.fillOval( (j+1) * cell_size + cell16, (i+1) * cell_size + cell_half + cell16, cell34, cell34);
	        					g.setColor(Color.BLACK);
	        					g.fillOval( (j+1) * cell_size + cell16, (i+1) * cell_size + cell_half + cell16, cell34, cell34);
	        				}
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
