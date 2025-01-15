package choordinates;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;

//TODO reduce amount of stuff imported.
//import java.awt.Color;
//import java.awt.Graphics;
import javax.swing.*;

public class FretPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
    public enum Select
	{
			NONE,
			VALID,
			ANY
	}
    
    //Member constants
    private final int mMaxStrings = 8;
    private final int mMaxFrets = 26;  //The nut is fret 0.
    
    //Member variables
    private int mClickX, mClickY;
    private int mNumStrings = 6;
    private int mNumFrets = 24;
    private int mNumDrawFrets = mNumFrets+1;
    private int mFirstDrawFret = -1;
    private int mFirstFret = 0;
    private int mSelectionFirstFret = -1;
    private boolean mLefty = false;		//Set to true for left-handed chord shapes.
    private ToneChord mTuning = new ToneChord();
    private int[] mSelections = new int[mMaxStrings];

    private int[][] mFrets=new int[mMaxStrings][mMaxFrets];  //Let's not be clever.  Just buffer max space.
    private boolean[][] mFavFrets = new boolean[mMaxStrings][mMaxFrets];
    private boolean mOrientX = true;  //Set true for horizontal strings, false for vertical

    //Root Note and Search Chord inform the fret display.
    private ToneNote mRootNote;
    private IntervalChord mSearchChord = new IntervalChord();
    private ChordShape mFavChord = new ChordShape();
        
    private Select mSelectMode = Select.NONE;
    
	private FavHandler mFavCallback;

	public void setCallback(FavHandler callback)
	{
		System.out.println("Setting");

		mFavCallback = callback;
	}

    public void selectMode(Select mode)
    {
    	//Determines whether we can select any string/fret 
    	//only populated ones or none at all.
    	mSelectMode = mode;
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
    	else if (frets > mMaxFrets)
    	{
    		frets = mMaxFrets;
    	}
    	mNumFrets = frets;
    	mNumDrawFrets = mNumFrets+1;
    	
    	updateTuning();
    	flushSelections();
    	refresh();
    }
    
    public void setFirstFret(int fretno)
    {
    	int upper_limit = ( mMaxFrets - mNumFrets );

    	mFirstFret = Math.min( Math.max( fretno, 0), upper_limit );
    	mFirstDrawFret = mFirstFret;
    	
    	if ( mFirstFret == 0 )
    	{
    		mFirstDrawFret-=1;
    	}
    }
    
    public void flushSelections()
    {
    	//Deselect all the strings.
    	for (int i=0;i<mMaxStrings;++i)
    	{
    		mSelections[i] = -1;
    	}
    }
    
    public void setSelectionShape(ChordShape chord_shape)
    {
    	mFavChord = chord_shape;
    	markFavorite();
    	flushSelections();
    	refresh();
    }
    
    public ChordShape getSelectionShape()
    {
    	if (mTuning == null
    			|| mRootNote == null
    			|| mSearchChord == null
    			)
    	{
    		//One if to rule them all.
    		throw new IllegalArgumentException("No notes selected.");
    	}
    	
    	mFavChord =  new ChordShape( mTuning, mRootNote, mSearchChord, mFirstFret, mNumStrings, mSelections );
    	markFavorite();
    	flushSelections();
    	refresh();
    	return mFavChord;
    }
    
    public ToneNote getRootNote()
    {
    	return mRootNote;
	}
    
    public IntervalChord getSearchChord()
    {
    	return mSearchChord;
    }
    
    public int getSelectionFret()
    {
    	return mSelectionFirstFret;
    }
    
    public ToneChord getSelections(boolean unique)
    {    	
    	/*SO Lazy!  Rather than implement another function, I'm going to
    	 * just build a string of the note names on the strings, then 
    	 * let ToneChord sort it out.
    	 */
    	String chord_string = "";
    	
    	ArrayList<String> note_names = new ArrayList<String>(); 
    	
    	for ( int i=0;i<mTuning.getNumNotes();++i )
    	{
    		if (mSelections[i] != -1)
    		{
    			ToneNote string_note = new ToneNote(  mTuning.getNote(i), mSelections[i] + mFirstFret);

    			if ( !unique || !note_names.contains( string_note.getName() ) )
    			{
    				note_names.add( string_note.getName() );
    			}
    		}
    	}
    	
    	if (note_names.size() < 1)
    	{
    		return null;
    	}
    	
    	for (String name:note_names)
    	{
    		chord_string = chord_string + name + " ";
    	}
    	
    	return new ToneChord( chord_string );
    }
    
    public void selectFret(int string_num, int fret_num)
    {
    	if (mSelectMode == Select.NONE)
    	{
    		return;
    	}
    	
    	if (mSelectMode==Select.VALID && (mFrets[string_num][fret_num] == -1))
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
    
    public void flushFavorite()
    {
    	for ( int string_num=0; string_num<mMaxStrings; ++string_num )
    	{
    		for ( int fret_num=0; fret_num<mMaxFrets; ++fret_num )
    		{
    			mFavFrets[string_num][fret_num] = false;
    		}
    	}
    }
    
    public void markFavorite()
    {
    	flushFavorite();
    	if (!mFavChord.isValid())
    	{
    		return;
    	}
    	
    	ChoordData choord_data = ChoordData.getInstance();
    	ToneChord tuning = choord_data.getCurrentTuning();

    	int string_num = mFavChord.getLowestString();
    	
    	//Tone the open string is tuned to.
    	ToneNote string_tone = tuning.getNote(string_num);
    	
    	//Tone relative to the root note of the displayed chord.
    	IntervalNote string_interval = new IntervalNote( string_tone.getOctaveSemitone() - mRootNote.getOctaveSemitone() );
    	
    	IntervalNote note= mFavChord.getFirstNote();
    	mSelectionFirstFret =AbstractNote.mod( note.getOctaveSemitone() - string_interval.getOctaveSemitone(), 12) - mFirstFret;
    	int first_fret=mSelectionFirstFret;
    	
    	while (first_fret + mFavChord.getSpan() < mMaxFrets)
    	{
    		for (string_num=0;string_num<mNumStrings;++string_num)
    		{
    			int offset = mFavChord.getString(string_num);
    			if (offset >= 0)
    			{
    				mFavFrets[string_num][first_fret+offset] = true;
    			}
    		}
        	
    		first_fret += 12;
    	}
    }
    
    public void markFrets()
    {
    	ChoordData choord_data = ChoordData.getInstance();
    	
    	if (choord_data.getCurrentTuningID() == -1)
    	{
    		return;
    	}
    	
    	ToneChord tuning = choord_data.getCurrentTuning();
    	
    	int root_tone=0;
    	
    	if (mRootNote != null)
    	{
    		root_tone = mRootNote.getSemitone();
    	}
    	    	
    	flushFrets();   //Floosh
    	
    	//Loop through the strings on the instrument
    	for (int string_num=0; string_num<tuning.getNumNotes(); ++string_num)
    	{
    		ToneNote string_note = tuning.getNote(string_num);
    		    		
    		int string_tone = string_note.getSemitone();
    		
    		//Loop through the notes in the selected chord
    		for (int interval_num=0; interval_num<mSearchChord.getNumNotes(); ++interval_num)
    		{
    			int interval_tone = mSearchChord.getNote(interval_num).getOctaveSemitone();
    			
    			if (mRootNote != null)
    			{
	    			int chord_tone = ( root_tone + interval_tone ) % 12;
	    			for (int fret=0;fret<mMaxFrets; ++fret)
	    			{
	    				if ( ( ( string_tone + fret ) % 12 ) == chord_tone)
	    				{
	    					mFrets[string_num][fret] = interval_num;
	    				}
	    			}
    			}
    		}
    	}
    	
    	refresh();
    }
    
    public void setRootAndChord(ToneNote note, IntervalChord chord)
    {    	
    	if (mRootNote == null 
    			|| !mRootNote.equivalent(note)
    			|| !mSearchChord.equals(chord))
    	{
    		mRootNote = note;
    		mSearchChord = chord;

    		flushSelections();
    		flushFavorite();
    		markFrets();
    	}
    }
    
    public void updateTuning()
    {
    	ChoordData choord_data = ChoordData.getInstance();
    	
    	int tuning_id = choord_data.getCurrentTuningID();
    	
    	if (tuning_id == -1)
    	{
    	     mNumStrings = 6;
    	}
    	else
    	{
    		mTuning = choord_data.getTuning(tuning_id);
    		mNumStrings = mTuning.getNumNotes();
    	}
    	markFrets();
    }
    
    public FretPanel() {    			
    	ChoordData.getInstance();
    	flushFrets();
    	flushSelections();
    	updateTuning();
    	
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
    
    private void handleMouseClick(Cell fret_area, int cell_size)
    {
    	//Handle mouse clicks.
    	int cell_half = cell_size/2;

    	if ( mClickX > fret_area.X[0] - cell_half 
    			&& mClickX < fret_area.X[1] + cell_half
    			&& mClickY > fret_area.Y[0] - cell_half
    			&& mClickY < fret_area.Y[1] + cell_half )
    	{
    		int mouse_x = 0;
    		int mouse_y = 0;
    		
    		mouse_x = Math.min( Math.max ( mClickX, fret_area.X[0]), fret_area.X[1] );
    		mouse_y = Math.min( Math.max ( mClickY, fret_area.Y[0]), fret_area.Y[1] );
    		
    		int cell_x = (mouse_x - fret_area.X[0]) / cell_size;
			int cell_y = (mouse_y - fret_area.Y[0]) / cell_size;
			
    		int string_num, fret_num;
    		if (mOrientX)
    		{
    			cell_y = (mouse_y - fret_area.Y[0] + cell_half) / cell_size;

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
    			cell_x = (mouse_x - fret_area.X[0] + cell_half) / cell_size;
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
    	
    	//Done with mouse position for now.
    	mClickX = -1;
    	mClickY = -1;
    }
    
    private void drawFavorites(Graphics g, Cell fret_area, int cell_size)
    {
    	int cell_half = cell_size/2;
    	
    	if (mSelectMode == Select.NONE)
    	{
    		return;
    	}
    	
    	if (mFavChord.isValid())
    	{
    		for (int string_num=0;string_num<mNumStrings;++string_num)
    		{
    			for (int fret_num=0;fret_num<mMaxFrets;++fret_num)
    			{
    				if (mFavFrets[string_num][fret_num])
    				{
    					Cell cell = new Cell(string_num, fret_num, cell_size, fret_area);
    					
				    	g.setColor(Color.GREEN);

    					if (mOrientX) 
    	    			{
    	    				g.fillRect( cell.X[0], cell.Y[0] - cell_half, cell_size, cell_size);
    	    			}
    	    			else
    	    			{
    	    				g.fillRect( cell.X[0] - cell_half, cell.Y[0], cell_size, cell_size);
    	    			}
    				}
    			}
    		}
    	}
    }
    
    private void drawSelections(Graphics g, Cell fret_area, int cell_size)
    {
    	if ( mSelectMode == Select.NONE )
    	{
    		return;
    	}
    	
    	int cell_half = cell_size/2;
    	int cell34 = cell_size * 3 / 4;
    	//Draw the selections.  Note these will overwrite Favorites. (and that's OK!)
    	if (mSelectMode == Select.ANY)
    	{
    		g.setColor(Color.BLACK); 
    	}
    	else
    	{
    		g.setColor(Color.ORANGE); 
    	}

    	for (int i=0;i<mNumStrings;++i)
    	{
    		if (mSelections[i] != -1)
    		{
    			Cell cell = new Cell(i, mSelections[i], cell_size, fret_area);
    			
    			if (mOrientX) 
    			{
    				if (mSelectMode == Select.ANY)
    				{
    					g.fillOval( cell.X[0] + cell_size/4, cell.Y[0] - cell_size/3, cell34, cell34);
    				}
    				else
    				{
    					g.fillRect( cell.X[0], cell.Y[0] - cell_half, cell_size, cell_size);
    				}
    			}
    			else
    			{
    				if (mSelectMode == Select.ANY)
    				{
    					g.fillOval( cell.X[0] - cell_size/3, cell.Y[0] + cell_size/4, cell34, cell34);
    				}
    				else
    				{
    					g.fillRect( cell.X[0] - cell_half, cell.Y[0], cell_size, cell_size);
    				}

    			}
    		}
    	}
    }
    
    private void drawCells( Graphics g, Cell fret_area, int cell_size, int rows, int cols )
    {
        g.setColor(Color.BLACK);
        
        
    	if (!mOrientX)
    	{
        	int tmp=cols;
        	cols = rows-1;
        	rows = tmp;
        	fret_area.Y[1] -= cell_size;
    	}
    	for (int x=0;x<rows;++x)
    	{
    		int xpos = fret_area.X[0] + x*cell_size;
    		g.drawLine( xpos, fret_area.Y[0], xpos, fret_area.Y[1]);
    		
    		if (mOrientX && (x+mFirstDrawFret) % 12 == 0)
    		{
        		g.drawLine( xpos - 2, fret_area.Y[0], xpos - 2, fret_area.Y[1]);
    		}
    	}
    	
    	for (int y=0;y<cols;++y)
    	{
    		int ypos = fret_area.Y[0] + y*cell_size;
    		g.drawLine( fret_area.X[0], ypos, fret_area.X[1], ypos);
    		
    		if (!mOrientX && (y+mFirstDrawFret) % 12 == 0)
    		{
        		g.drawLine( fret_area.X[0], ypos - 2, fret_area.X[1], ypos - 2 );
    		}
    	}
    }

    private void drawFretNumbers(Graphics g, Cell fret_area, int cell_size)
    {
        int[] labeled_frets = {0, 3, 5, 7, 9, 12, 15, 17, 19, 21, 24};      
        
    	for (int x=0; x<mNumDrawFrets; ++x)
    	{	
       		//Draw the string labels
    		int fret_no = mFirstFret+x;

    		if (Arrays.binarySearch(labeled_frets, fret_no) >= 0)
    		{
    			String numeral = getRomanNumeral(fret_no);
    			
        		Cell cell = new Cell(0, x, cell_size, fret_area);
        		
    			//TODO get graphic width of string , not this mess
    			if (mOrientX)
    			{
    				g.drawString(numeral, cell.X[0] - 5, cell_size);
    			}
    			else
    			{
            		g.drawString(numeral, (cell_size/3), cell.Y[0] + 5);
    			}
    		}
    	}
    }
    
    private void drawStringNames(Graphics g, Cell fret_area, int cell_size)
    {
    	int cell_half = cell_size/2;
	 	for (int y=0; y<mNumStrings; ++y)
	 	{
	 		Cell cell = new Cell(y, 0, cell_size, fret_area);
	    	if (mOrientX)  //Horizontal strings, vertical frets
	    	{	

	    		g.drawString(mTuning.getNote(y).getName(), cell_half, cell.Y[0]);
	    	}
	    	else
	    	{
		 		g.drawString(mTuning.getNote(y).getName(), cell.X[0] - cell_size/4, cell_size);
	    	}
	 	}
    }
       
    private void drawIntervalNotes(Graphics g, Cell fret_area, int cell_size)
    {
    	int rows = mNumDrawFrets-1;
    	int cols = mNumStrings;
    	
    	final int cell_half = cell_size/2;
    	
    	//The bible says PI=3.  Who am I to question mindless dogma?
        final int cell34 = cell_size * 3 / 4;
        final int cell23 = cell_size * 2 / 3;
        final int cell16 = cell_size / 6;
    	
    	if (mOrientX)
    	{
    		int tmp = rows;
    		rows = cols;
    		cols = tmp;
    	}
    	
    	for (int x=0;x<cols;++x)
    	{
    		for (int y=0;y<rows;++y)
    		{
    			Cell cell = new Cell( y, x, cell_size, fret_area );
    			
    			boolean fav=false;
    			int fret_no = x + mFirstFret;
    			
    			if (mOrientX)
    			{
        			if (mFrets[y][fret_no] == -1)
        			{
        				continue;
        			}
    				g.setColor(IntervalNote.getColor(mFrets[y][fret_no]));
    				fav = mFavFrets[y][fret_no];
    				cell.X[0] += cell23;
    			}
    			else
    			{
    				fret_no = y + mFirstFret;
    				cell = new Cell( x, y, cell_size, fret_area );
    				if (mFrets[x][fret_no] == -1)
    				{
    					continue;
    				}
    				g.setColor(IntervalNote.getColor(mFrets[x][fret_no]));
    				
    				fav = mFavFrets[x][fret_no];
    				cell.X[0] += cell16;
    				cell.Y[0] += cell_half;
    			}
    			
    			if (mSelectMode == Select.NONE && !fav)
    			{
    				continue;
    			}

    			cell.X[0] -= cell_half;
    			cell.Y[0] -= cell_size/3;

    			g.fillOval( cell.X[0],  cell.Y[0],  cell34,  cell34);
    			g.setColor(Color.BLACK);
    			g.drawOval( cell.X[0],  cell.Y[0],  cell34,  cell34);
    		}
    	}
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	if (ChoordData.getInstance().getCurrentTuningID() == -1)
    	{
    		return;
    	}
    	
        super.paintComponent(g);
        
        mNumDrawFrets = mNumFrets + 1;
        if (mFirstFret == 0 && mOrientX)
        {
        	mNumDrawFrets++;
        }
        
        //Max size of cells in the space of the pane.
        int max_cell_x, max_cell_y;
                
        //Compute cell size based on orientation.
        //Add one to string to have space for drawing fret names.
        //Add one to frets to have space for drawing string names.
        if (mOrientX)
        {
        	max_cell_x = getWidth() / mNumDrawFrets;
        	max_cell_y = getHeight() / (mNumStrings+1);
        }
        else
        {
        	max_cell_x = getWidth() / (mNumStrings+1);
        	max_cell_y = getHeight() / mNumDrawFrets;  
        }
        
        //Several convenience ints.
        final int cell_size = Math.min(max_cell_x, max_cell_y);
        final int cell_half = cell_size/2; 
        
    	Cell fret_area = new Cell(); //X and Y mins and maxes of fret box.
    	    	
    	fret_area.X[0] = cell_size + cell_half;		//X min
    	fret_area.Y[0] = fret_area.X[0];    //Y min
		fret_area.X[1] =  cell_size * mNumStrings + cell_half;
		fret_area.Y[1] =  cell_size * mNumDrawFrets + cell_half;
    	if (mOrientX)
    	{
    		int tmp = fret_area.X[1];
    		fret_area.X[1] = fret_area.Y[1];
    		fret_area.Y[1] =  tmp;
    	}
    	
    	handleMouseClick( fret_area, cell_size );
    	drawFavorites(g, fret_area, cell_size);
    	drawSelections(g, fret_area, cell_size);   	
        drawCells(g, fret_area, cell_size, mNumDrawFrets, mNumStrings );
        drawFretNumbers(g, fret_area, cell_size);
        drawStringNames(g, fret_area, cell_size);
        drawIntervalNotes(g, fret_area, cell_size); 
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    	if (mSelectMode == Select.NONE 
    			&& mFavCallback != null )
    	{
    		boolean right_click = false;
    		if (e.getButton() == MouseEvent.BUTTON3 )
    		{
    			System.out.println("Delete me senpai!");
    			right_click = true;
    		}
    		mFavCallback.favCallback(mFavChord, right_click);
    		return;
    	}

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
    
    
    private class Cell
    {
    	//Utility class for handling the on-screen dimensions of objects.
    	public int[] X = new int[2];
    	public int[] Y = new int[2];
    	
    	public Cell() {}
    	
    	public Cell(int string_num, int fret_num, int cell_size, Cell draw_area)
    	{
    		int x,y;
    		//i is string num
			if (mOrientX)
			{
				y = string_num;
				if (!mLefty)
				{
					y = mNumStrings - ( string_num + 1 );
				}
				x = fret_num;
			}
			else
			{
				x = string_num;
				if (mLefty)
				{
					x = mNumStrings - ( string_num + 1);
				}
				y = fret_num;
			}
			X[0] = draw_area.X[0] + x*cell_size;
			Y[0] = draw_area.Y[0] + y*cell_size;
			
			X[1] = X[0]+cell_size;
			Y[1] = Y[1]+cell_size;
    	}
    };
    

}

