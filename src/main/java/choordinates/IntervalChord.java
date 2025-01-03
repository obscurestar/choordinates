package choordinates;

import java.util.ArrayList;
import java.util.Iterator;

public class IntervalChord extends Chord
{
	private ArrayList<String> mAliases = new ArrayList<String>(); // Aliases for the chord.
	public void addAlias(String name) {
		// I don't care about duplicates. It's your computation time, user.
		mAliases.add(name);
	}

	public void deleteAlias(String alias) {
		// Deletes all alias matching case-insensitive
		// Doesn't care if the alias isn't found.

		Iterator<String> iter = mAliases.iterator();

		while (iter.hasNext()) {
			String item = iter.next();
			if (alias.toUpperCase() == item.toUpperCase()) {
				iter.remove();
			}
		}
	}

	public void setAliasesFromString(String aliasString) {
		// Flush whatever we had. Byeeee!
		mAliases.clear();

		if (aliasString.length() == 0) {
			// Do not allow both name and alias to be blank.
			if (mName == "") {
				return;
			}
		}

		String[] aliases = aliasString.split(",");

		for (String alias : aliases) {
			mAliases.add(alias.trim());
		}
	}

	public ArrayList<String> getAliases() {
		// Return the list of aliases.
		return mAliases;
	}

	public ArrayList<String> getAllChordNames() {
		// Return all names for this chord (preferred first)
		ArrayList<String> notes = getAliases();
		// It's a short list. Who cares about the cost?
		notes.add(0, mName);
		return notes;
	}

}
