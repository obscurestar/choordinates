package com.obscurestar.choordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Locale;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.FileOutputStream;

public class ChoordData {
	public static boolean CLOSING = false;
	// TODO: Probably a better filepath.
	@JsonIgnore
	private static String mFilePath = "";
	@JsonIgnore
	private static String mJsonFile = "choordinates.json";
	@JsonIgnore
	private static String mHelpFile = "choordinates.html";
	@JsonIgnore
	private static ChoordData mInstance;

	@JsonProperty("tunings")
	private ArrayList<ToneChord> mTunings = new ArrayList<ToneChord>();
	@JsonProperty("current_tuning")
	private int mCurrentTuning;
	@JsonProperty("chords")
	private ArrayList<IntervalChord> mChords = new ArrayList<IntervalChord>();
	@JsonProperty("current_chord")
	private int mCurrentChord;
	@JsonProperty("favorites_groups")
	private HashMap<UUID, FavGroup> mFavGroups = new HashMap<UUID, FavGroup>();
	@JsonProperty("preferences")
	private Preferences mPreferences = new Preferences();

	private ChoordData() {
		mCurrentTuning = -1;
		mCurrentChord = -1;
	}

	@JsonIgnore
	public String getHelpFile() {
		return mHelpFile;
	}

	@JsonIgnore
	public static ChoordData getInstance() {
		if (mInstance == null) {
			mInstance = new ChoordData();
			mInstance.installFiles(); // This is only ever run once.
			mInstance.mPreferences.loadIcon();
		}
		return mInstance;
	}

	@JsonIgnore
	public boolean hasFavoriteGroup(UUID group_id) {
		return mFavGroups.containsKey(group_id);
	}

	@JsonIgnore
	public boolean hasFavoriteGroup(IntervalChord interval) {
		// Uses the current tuning to see if group exists.
		UUID group_id = FavGroup.generateUUID(getCurrentTuning(), interval);

		return hasFavoriteGroup(group_id);
	}

	@JsonIgnore
	public FavGroup getFavoriteGroup(UUID group_id) {
		// gets a favorite group. Creates new group if doesn't exist, returns group
		if (hasFavoriteGroup(group_id)) {
			return mFavGroups.get(group_id);
		}
		FavGroup favs = new FavGroup(group_id);
		mFavGroups.put(group_id, favs);

		return favs;
	}

	@JsonIgnore
	public FavGroup getFavoriteGroup(ToneChord tuning, IntervalChord interval) {
		// gets a favorite group. Creates new group if doesn't exist, returns group
		UUID group_id = FavGroup.generateUUID(tuning, interval);

		return getFavoriteGroup(group_id);
	}

	@JsonIgnore
	public FavGroup getFavoriteGroup(IntervalChord interval) {
		return getFavoriteGroup(getCurrentTuning(), interval);
	}

	@JsonIgnore
	public UUID addFavorite(ChordShape fav, UUID group_id) {
		// Creates group if it does not exist, returns UUID of favorite.
		FavGroup group = getFavoriteGroup(group_id);
		return group.addFavorite(fav);
	}

	@JsonIgnore
	public UUID addFavorite(ChordShape fav, ToneChord tuning, IntervalChord interval) {
		return getFavoriteGroup(tuning, interval).addFavorite(fav);
	}

	@JsonIgnore
	public UUID addFavorite(ChordShape fav, IntervalChord interval) {
		ToneChord tuning = getCurrentTuning();
		return addFavorite(fav, tuning, interval);
	}

	// Functions related to chords.
	@JsonIgnore
	public int getNumChords() {
		return mChords.size();
	}

	@JsonIgnore
	public boolean validChord(int id) {
		if (id >= 0 && id < mChords.size()) {
			return true;
		}

		return false;
	}

	@JsonProperty("current_chord")
	public void setCurrentChord(int id) {
		// Sets current chord to the passed ID.
		// TODO maybe make a better validator some day.
		if (validChord(id)) {
			mCurrentChord = id;
		} else {
			if (mChords.size() > 0) {
				mCurrentChord = 0;
			} else {
				mCurrentChord = -1;
			}
		}
	}

	@JsonIgnore
	public int getCurrentChord() {
		return mCurrentChord;
	}

	@JsonIgnore
	public IntervalChord getChord(int id) {
		if (!validChord(id)) {
			throw new ArrayIndexOutOfBoundsException(id + " out of bounds. Only " + getNumChords() + " avaliable.");
		}
		return mChords.get(id);
	}

	public int addChord(IntervalChord chord) {
		mChords.add(chord);
		return mChords.size() - 1;
	}

	public void deleteChord(int id) {
		// Removes a tuning from the list.
		if (validChord(id)) {
			mChords.remove(id);
		}
	}

	public void updateChord(int id, IntervalChord chord) {
		if (validChord(id)) {
			mChords.set(id, chord);
		}
	}

	// Functions pertaining to tunings.
	@JsonIgnore
	public int getNumTunings() {
		return mTunings.size();
	}

	public boolean validTuning(int id) {
		if (id >= 0 && id < mTunings.size()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void setCurrentTuning(int id) {
		// Sets current tuning to the passed ID.
		// TODO maybe make a better validator some day.
		if (validTuning(id)) {
			mCurrentTuning = id;
		} else {
			if (mTunings.size() > 0) {
				mCurrentTuning = 0;
			} else {
				mCurrentTuning = -1;
			}
		}
	}

	@JsonIgnore
	public int getCurrentTuningID() {
		return mCurrentTuning;
	}

	@JsonIgnore
	public ToneChord getTuning(int id) {
		if (!validTuning(id)) {
			throw new ArrayIndexOutOfBoundsException(id + " out of bounds. Only " + getNumTunings() + " avaliable.");
		}
		return mTunings.get(id);
	}

	@JsonIgnore
	public ToneChord getCurrentTuning() {
		return getTuning(getCurrentTuningID());
	}

	public int addTuning(ToneChord tuning) {
		mTunings.add(tuning);
		return mTunings.size() - 1;
	}

	public void deleteTuning(int id) {
		// Removes a tuning from the list.
		if (validTuning(id)) {
			mTunings.remove(id);
		}
	}

	public void updateTuning(int id, ToneChord tuning) {
		if (validTuning(id)) {
			mTunings.set(id, tuning);
		}
	}

	public ArrayList<Integer> findIntervalChords(IntervalChord chord) {
		/*
		 * Search the list of interval chords, return list of chords with the same notes
		 * in them.
		 */

		/*
		 * TODO THIS IS TERRIBLE AND DOES NOT SCALE. Just doing a linear search to
		 * compare the chords. Could actually do something smart like map the chord IDs
		 * into a longlong, sort it and use a binary search. However, that's a bit of
		 * work and the list of practical chord intervals is at most a few dozen. For
		 * now, just eat some computation time.
		 */
		ArrayList<Integer> matches = new ArrayList<>();

		return matches;
	}

	public ArrayList<Integer> findIntervalChord(ToneChord chord) {
		/* Returns list of chords matching this chord */

		/* TODO THIS IS TERRIBLE AND DOES NOT SCALE. See above. */
		ArrayList<Integer> matches = new ArrayList<>();

		return matches;
	}

	@JsonIgnore
	public Preferences getPreferences() {
		return mPreferences;
	}

	// Data serialization and deserialization routines.
	public void write() {
		ObjectMapper objectMapper = new ObjectMapper();

		try {

			objectMapper.writeValue(new File(mJsonFile), this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Tasty static initializer
	// now with yummy singleton flavor.
	public static ChoordData read() {
		// Read data into a ChoordData object
		// If the JSON fails to load for some reason,
		// will return the default empty container.
		ChoordData.getInstance();

		try {
			// Create an ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			File jsonFile = new File(mJsonFile);

			// Don't care if doesn't exist.
			if (jsonFile.exists()) {
				// Read the JSON file and map it to a Java object
				mInstance = objectMapper.readValue(jsonFile, ChoordData.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mInstance;
	}

	@JsonIgnore
	private String copyFileFromJar(String input_filename) {
		String filename = mFilePath + input_filename;

		File f = new File(filename);
		if (f.exists() && !f.isDirectory()) {
			if (f.length() == 0)
			{
				f.delete();
			}
			else
			{
				return filename; // Good exit
			}
		}

		ClassLoader classloader = ClassLoader.getSystemClassLoader();

		// Get the file in the jar file to an input stream.
		InputStream input_stream;
		try {
			input_stream = classloader.getResourceAsStream(input_filename);
		} catch (NullPointerException e) {
			Choordinates.alert("Error",
					"Couldn't extract " + input_filename + " from jar.\nYou'll have to create your own or fix permissions.");
			return filename; // Can still run, will just have to create all the data yourself.
		}

		// Open the destination file.
		File file = null;
		try {
			file = new File(filename);
		} catch (NullPointerException e) {
			Choordinates.alert("ERROR!", "Couldn't write to " + filename + " Aborting!");
			System.exit(0); // Can't write to directory. this is fatal.
		}

		FileOutputStream output_stream = null;

		try {
			output_stream = new FileOutputStream(file, false);
		} catch (IOException e) {
			Choordinates.alert("ERROR!", "Unable to create " + filename);
			System.exit(0); // Also fatal.
		}

		int read;
		byte[] bytes = new byte[4096];

		try {
			while ((read = input_stream.read(bytes)) != -1) {
				output_stream.write(bytes, 0, read);
			}

			output_stream.close();
			input_stream.close();
		} catch (IOException e) {
			Choordinates.alert("ERROR!", "Error copying " + filename + " from jar.");
			System.exit(0);
		}
		return filename;
	}

	@JsonIgnore
	public void installFiles() {
		String osName = System.getProperty("os.name").toLowerCase(Locale.getDefault());

		if (osName.contains("win")) {
			mFilePath = System.getenv("APPDATA");
			if (mFilePath == null) {
				mFilePath = System.getenv("LOCALAPPDATA");
			}
		} else if (osName.contains("mac")) {
			mFilePath = Paths.get(System.getProperty("user.home"), "Library", "Application Support").toString();
		} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
			mFilePath = System.getenv("XDG_DATA_HOME");
			if (mFilePath == null || mFilePath.isEmpty()) {
				mFilePath = Paths.get(System.getProperty("user.home"), ".local", "share").toString();
			}
		} else {
			Choordinates.alert("OwO What dis?", osName + " is unfamiliar.  Using local directory.");
		}

		mFilePath += "/com.obscurestar.choordinates/";

		try {
			Path path = Paths.get(mFilePath);
			Files.createDirectories(path);
		} catch (IOException e) {
		}

		mJsonFile = copyFileFromJar(mJsonFile);
		mHelpFile = copyFileFromJar(mHelpFile);
	}
}
