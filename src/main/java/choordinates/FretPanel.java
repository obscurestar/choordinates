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
    private int mClickX, mClickY;
    private int mNumStrings = 6;
    private int mNumFrets = 24;
    private int mFirstFret = -1;
    private boolean mLefty = false;		//Set to true for left-handed chord shapes.
    private ToneChord mTuning = new ToneChord();
    private int[] mSelections = new int[mMaxStrings];

    private int[][] mFrets=new int[mMaxStrings][mMaxFrets];  //Let's not be clever.  Just buffer max space.
    private boolean mOrientX = true;  //Set true for horizontal strings, false for vertical

    //Root Note and Search Chord inform the fret display.
    private ToneNote mRootNote;
    private IntervalChord mSearchChord = new IntervalChord();
        
    private boolean mSelectAny = true;
    
    public void selectAny(boolean any)
    {
    	//Determines whether we can select any string/fret 
    	//or only populated ones.
    	mSelectAny = any;
    }
    
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
    	flushSelections();
    	refresh();
    }
    
    public void setFirstFret(int fretno)
    {
    	int upper_limit = ( mMaxFrets - mNumFrets );
    	
    	mFirstFret = Math.min( Math.max( fretno, 0), upper_limit );
    	mFirstFret -= 1;
    }
    
    public void flushSelections()
    {
    	//Deselect all the strings.
    	for (int i=0;i<mMaxStrings;++i)
    	{
    		mSelections[i] = -1;
    	}
    }
    
    public ToneChord getSelections()
    {    	
    	/*SO Lazy!  Rather than implement another function, I'm going to
    	 * just build a string of the note names on the strings, then 
    	 * let ToneChord sort it out.
    	 */
    	String chord_string = "";
    	
    	for (int i=0;i<mTuning.getNumNotes();++i )
    	{
    		if (mSelections[i] != -1)
    		{
    			ToneNote string_note = new ToneNote(  mTuning.getNote(i), mSelections[i] );
    			chord_string = chord_string + " " + string_note.getName();
    		}
    	}
    	
    	return new ToneChord( chord_string );
    }
    
    public void selectFret(int string_num, int fret_num)
    {
    	System.out.println("String: " + string_num + " " + fret_num );
    	if (!mSelectAny && (mFrets[string_num][fret_num] == -1))
    	{
    		return;
    	}
    	
    	if ( mSelections[string_num] == fret_num )
    	{
    		//This fret was already selected.  Deselect.
    		mSelections[string_num] = -1;
    	}
    	else
    	{
    		mSelections[string_num] = fret_num;
    	}
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
    	
    	int root_tone=0;
    	
    	if (mRootNote != null)
    	{
    		root_tone = mRootNote.getSemitone();
    	}
    	    	
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
    			
    			if (mRootNote != null)
    			{
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
    	}
    	refresh();
    }
    
    public void setRootAndChord(ToneNote note, IntervalChord chord)
    {
    	mRootNote = note;
    	mSearchChord = chord;
    	flushSelections();
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
    		mTuning = choord_data.getTuning(tuning_id);
    		mNumStrings = mTuning.getNumNotes();
    	}
    	//flushSelections();
    	markFrets();
    }
    
    public FretPanel() {
    	ChoordData.getInstance();
    	flushFrets();
    	flushSelections();
    	
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
    	//Hideous boutique graphic panel with trial and error layout.
    	//TODO Refactor into function blocks some day.
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
        
    	int fret_area[][] = new int[2][2]; //X and Y mins and maxes of fret box.
    	
    	if (mOrientX)
    	{
    		fret_area[0][0] = cell_size + cell_half;		//X min
    		fret_area[1][0] = cell_size * mNumFrets + cell_half;	//X max
    		
    		fret_area[0][1] =  cell_size + cell_half;
    		fret_area[1][1] =  cell_size * mNumStrings + cell_half;
    	}
    	else
    	{
    		fret_area[0][0] = cell_size + cell_half;
    		fret_area[1][0] =  cell_size * mNumStrings + cell_half;
    		
    		fret_area[0][1] = cell_size + cell_half;
    		fret_area[1][1] =  cell_size * mNumFrets + cell_half;
    	}

    	//Handle mouse clicks.
    	if ( mClickX > fret_area[0][0] - cell_half 
    			&& mClickX < fret_area[1][0] + cell_half
    			&& mClickY > fret_area[0][1] - cell_half
    			&& mClickY < fret_area[1][1] + cell_half )
    	{
    		System.out.println("Clicky " + mClickX + " " + mClickY);
    		int mouse_x = 0;
    		int mouse_y = 0;
    		
    		mouse_x = Math.min( Math.max ( mClickX, fret_area[0][0]), fret_area[1][0] );
    		mouse_y = Math.min( Math.max ( mClickY, fret_area[0][1]), fret_area[1][1] );
    		
    		int cell_x = (mouse_x - fret_area[0][0]) / cell_size;
			int cell_y = (mouse_y - fret_area[0][1]) / cell_size;
			
    		int string_num, fret_num;
    		if (mOrientX)
    		{
    			cell_y = (mouse_y - fret_area[0][1] + cell_half) / cell_size;

    			fret_num = cell_x;
    			if (mLefty)
    			{
    				string_num = cell_y;
    			}
    			else
    			{
    				string_num = mNumStrings - (cell_y +1);
    			}
    		}
    		else
    		{
    			cell_x = (mouse_x - fret_area[0][0] + cell_half) / cell_size;
    			fret_num = cell_y;
    			if (mLefty)
    			{
    				string_num = mNumStrings - (cell_x + 1);
    			}
    			else
    			{
    				string_num = cell_x;	
    			}
    		}
    		    		
    		selectFret(string_num, fret_num);
    	}
    	
    	//Draw the selections:
    	if (mSelectAny)
    	{
    		g.setColor(Color.BLACK); 
    	}
    	else
    	{
    		g.setColor(Color.CYAN); 
    	}
    	for (int i=0;i<mNumStrings;++i)
    	{
    		if (mSelections[i] != -1)
    		{
    			int x,y;
    			if (mOrientX)
    			{
    				y = i;
    				if (!mLefty)
    				{
    					y = mNumStrings - ( i + 1 );
    				}
    				x = mSelections[i];
    				if (mSelectAny)
    				{
    					g.fillOval( fret_area[0][0] + x*cell_size + cell_size/4, fret_area[0][1] + y*cell_size - cell_size/3, cell34, cell34);
    				}
    				else
    				{
    					g.fillRect(fret_area[0][0] + x*cell_size, fret_area[0][1] + y*cell_size - cell_half, cell_size, cell_size);
    				}
    			}
    			else
    			{
    				x = i;
    				if (mLefty)
    				{
    					x = mNumStrings - ( i + 1);
    				}
    				y = mSelections[i];
    				if (mSelectAny)
    				{
    					g.fillOval( fret_area[0][0] + x*cell_size - cell_size/3, fret_area[0][1] + y*cell_size + cell_size/4, cell34, cell34);
    				}
    				else
    				{
    					g.fillRect(fret_area[0][0] + x*cell_size - cell_half, fret_area[0][1] + y*cell_size, cell_size, cell_size);
    				}

    			}
    		}
    	}
    	
    	//Done with mouse position for now.
    	mClickX = -1;
    	mClickY = -1;
    	
        g.setColor(Color.BLACK);
        
        //TODO refactor this orientx orienty into a less duplicate-code function some day.
        if (mOrientX)  //Horizontal strings, vertical frets
        {
        	for (int x=0; x<mNumFrets; ++x)
        	{
        		g.drawLine( (x+1) * cell_size + cell_half,  fret_area[0][1],  (x+1) * cell_size + cell_half, fret_area[1][1]);
        		if (((mFirstFret + x) % 12) == 0)
        		{
        			//Draw double-lines at 0 12 24
            		g.drawLine( (x+1) * cell_size + cell_half - 2,  fret_area[0][1],  (x+1) * cell_size + cell_half -2, fret_area[1][1]);

        		}
        	}
        	for (int y=0; y<mNumStrings; ++y)
        	{
        		g.drawLine(fret_area[0][0], (y+1) * cell_size + cell_half, fret_area[1][0], (y+1) * cell_size + cell_half);
        		if (mLefty)
        		{
        			g.drawString(mTuning.getNote(y).getName(), cell_half, (y+1) * cell_size + cell_half);
        		}
        		else
        		{
        			g.drawString(mTuning.getNote(y).getName(), cell_half, (mNumStrings - y) * cell_size + cell_half);
        		}
        	}
        	
        	for (int i=0; i<mNumFrets; ++i)
        	{
        		//Draw the string labels
        		int fret_no = mFirstFret+i;
        		if (Arrays.binarySearch(labeled_frets, fret_no) >= 0)
        		{
        			int offset = 0;
        			if (mFirstFret < 1)
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
        		g.drawLine( (x+1) * cell_size + cell_half, fret_area[0][1], (x+1) * cell_size + cell_half, fret_area[1][1]);
        		if (mLefty)
        		{
        			g.drawString(mTuning.getNote(x).getName(), (mNumStrings - x) * cell_size + cell14, cell_size);
        		}
        		else
        		{
        			g.drawString(mTuning.getNote(x).getName(), (x+1) * cell_size + cell14, cell_size);
        		}
        	}

        	for (int y=0; y < mNumFrets; ++y)
        	{
        		g.drawLine(fret_area[0][1], (y+1) * cell_size + cell_half, fret_area[1][0], (y+1) * cell_size + cell_half);
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
        			if (mFirstFret < 1)
        			{
        				offset = cell_size;
        			}
        			if (i>0)
        			{
        				String numeral = getRomanNumeral(fret_no);
        				g.drawString(numeral, (cell_size/3), cell_size * i + offset + cell_half + 5);
        			}
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
        mClickX = e.getX();
        mClickY = e.getY();
        repaint();   
    }

    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
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
