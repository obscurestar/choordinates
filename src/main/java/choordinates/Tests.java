package choordinates;

public class Tests {
	private static final String mIntervalString = "-1 1 2♭ 2 3♭ 3 4 5♭ 5 6♭ 6 7♭ 7";
	private static final String mToneString = "A♭ A B♭ B C D♭ D E♭ E F G♭ G a♭ a b♭ b c d♭ d e♭ e f g♭ g g♭";

	Tests()
	{
		TestToneFromInterval();
		//TestToneChordParse();
		//TestIntervalChordParse();
		//TestToneNoteEqual();
		//TestToneNoteParse();
		//TestToneNote();
		//TestIntervalNote();
		//TestIntervalNoteEqual();
		//TestIntervalNoteReduceSharps();
		//TestIntervalNoteParse();
	}
	
	//TONE CHORD TESTS
	void TestToneFromInterval()
	{
		IntervalChord interval_chord = new IntervalChord( "1 3 5" );
//		IntervalChord interval_chord = new IntervalChord( mIntervalString );
		String[] tones = mToneString.split(" ");
		for (String tone:tones)
		{
			ToneNote note = new ToneNote(tone);
			ToneChord tone_chord = new ToneChord(note, interval_chord);
			System.out.println(tone + " chord created: " + tone_chord.getAllNoteNames());
		}
	}
	
	void TestToneChordParse()
	{
		ToneChord chord = new ToneChord( mToneString );
		System.out.println(chord.getAllNoteNames());
	}
		
	//INTERVAL CHORD TESTS
	void TestIntervalChordParse()
	{
		IntervalChord chord = new IntervalChord( mIntervalString );
		System.out.println(chord.getAllNoteNames());
	}
	
	//TONENOTE TESTS
	void TestToneNoteEqual()
	{

		for (int k=0;k<11;++k)
		{
			ToneNote a = new ToneNote(k);
			for (int i=0;i<7;++i)
			{
				for (int j=0;j<4;++j)
				{
					ToneNote b = new ToneNote();
					b.setID(i);
					b.setSharp(j);
					
					if (a.equivalent(b))
					{
						System.out.println(a.getName() + " = " + b.getName());
					}
				}
			}
		}
	}

	void TestToneNote()
	{
		for (int i=-25;i<25;++i )
		{
			ToneNote note = new ToneNote(i);
			//System.out.print(i + " " + note.getName() + " semi " + note.getOctaveSemitone() + " full " + note.getSemitone() );
			System.out.print( " " + note.getName() );
		}
		System.out.println();
	}
	
	void TestToneNoteParse()
	{
		String[] tests = mToneString.split(" ");
    	
    	System.out.print("String ctor ");
    	for(String test:tests)
    	{
    		ToneNote note = new ToneNote(test);
    		System.out.print(" " + note.getName());
    	}
    	System.out.println();
	}
	
	//INTERVAL NOTE TESTS
	void TestIntervalNoteEqual()
	{
		for (int k=0;k<11;++k)
		{
			IntervalNote a = new IntervalNote(k);
			for (int i=0;i<4;++i)
			{
				for (int j=0;j<4;++j)
				{
					IntervalNote b = new IntervalNote();
					b.setID(i);
					b.setSharp(j);
					
					if (a.equivalent(b))
					{
						System.out.println(a.getName() + " = " + b.getName());
					}
				}
			}
		}
	}
	void TestIntervalNoteReduceSharps()
	{
		for (int i=-13;i<13;++i)
		{
			IntervalNote note = new IntervalNote();
			note.setID(0);
			note.setSharp(i);
			
			System.out.print(note.getNoteName() + " " + i );
			note.reduceSharps();
			System.out.println(" becomes " + note.getName() );
		}
	}
	void TestIntervalNoteParse()
	{
		String[] tests = mIntervalString.split(" ");
    	
    	System.out.print("String ctor ");
    	for(String test:tests)
    	{
    		IntervalNote note = new IntervalNote(test);
    		System.out.print(" " + note.getName());
    	}
    	System.out.println();
	}
	
	void TestIntervalNote()
	{
		for (int i=-13;i<25;++i )
		{
			IntervalNote note = new IntervalNote(i);
			//System.out.println(i + " " + note.getName() + " semi " + note.getOctaveSemitone() + " full " + note.getSemitone() );
			System.out.print( " " + note.getName() );
		}
		System.out.println();
	}
	
}

/*
if (this instanceof IntervalNote)
*/