package choordinates;

public class Tests {
	Tests()
	{
		//TestToneNoteEqual();
		//TestToneNoteParse();
		//TestToneNote();
		//TestIntervalNote();
		//TestIntervalNoteEqual();
		//TestIntervalNoteReduceSharps();
		//TestIntervalNoteParse();
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
		String tester = "A B♭ B C♭ C D E♭ E F♭ F G♭ G a b♭ b c♭ c d e♭ e f♭ f g♭ g";
		String[] tests = tester.split(" ");
    	
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
		String tester = "-1 1 2♭ 2 3♭ 3 4 5♭ 5 6♭ 6 7♭ 7";
		String[] tests = tester.split(" ");
    	
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