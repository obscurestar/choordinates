package choordinates;

import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;

public class FavGroup
{
/*
 * FavGroup is a family of favorites which share tuning intervals and chord semitones.
 * EX:  E Standard EADGBe and D standard DGCFAd share the same tuning intervals. 
 * 		A minor chord 1 3b 5 could share the same intervals with a 1 2# 4# chord
 * ChordShapes are stored within FavGroups
 */
	@JsonProperty("favorites")
	HashMap<UUID, ChordShape> mFavorites = new HashMap<UUID, ChordShape>();
	@JsonProperty("uuid")
	private UUID mUUID;

	@JsonCreator
	public FavGroup() { super(); }
	
	@JsonIgnore
	public FavGroup(UUID group_id)
	{
		mUUID = group_id;
	}
	
	@JsonIgnore
	public FavGroup(ToneChord tuning, IntervalChord intervals)
	{
		mUUID = FavGroup.generateUUID(tuning, intervals);
	}
	
	@JsonIgnore
	public boolean hasFavorite( UUID fav_id )
	{
		return mFavorites.containsKey( fav_id );
	}
	
	@JsonIgnore
	public HashMap<UUID, ChordShape> getFavorites()
	{
		return mFavorites;
	}
	
	@JsonIgnore
	public ChordShape getFavorite( UUID fav_id )
	{
		if ( hasFavorite (fav_id) )
		{
			return mFavorites.get(fav_id);
		}
		return null;
	}
	
	@JsonIgnore
	public void deleteFavorite( UUID fav_id )
	{
		if ( hasFavorite (fav_id) )
		{
			mFavorites.remove( fav_id );
		}
	}
	
	@JsonIgnore
	public UUID addFavorite(ChordShape fav)
	{
		
		UUID fav_id = fav.getUUID();
		if ( !hasFavorite( fav_id ) )
		{
			mFavorites.put(fav_id, fav);
		}
		return fav_id;
	}
	
	@JsonIgnore
	public static UUID generateUUID(ToneChord tuning, IntervalChord intervals)
	{
		//NOTE:  tuning and intervals could be be AbstractChord here but it feels wrong.
		
		//This is pretty sketch.  Making a byte array from a couple keywords
		//And the values to make a consistent UUID.

		//SMASH bits!  (They're all < 255, it's okay!
		byte[] id1 = "tuning".getBytes(); 
		byte[] id2 = "intervals".getBytes();
		
		int buffer_len = id1.length + id2.length + tuning.getNumNotes() + intervals.getNumNotes();

		byte[] uuid_bytes = new byte[buffer_len];
		int index = 0;
		
		//First string identifier
		for( int i=0;i<id1.length;++i, ++index )
		{
			uuid_bytes[index] = id1[i];
		}
		
		//The tuning semitones
		for ( int i=0;i<tuning.getNumNotes();++i, ++index )
		{
			//Double casting. Now that's dumb!
			uuid_bytes[index] = (byte)tuning.getNote(i).getOctaveSemitone();
		}
		
		//Second string identifier
		for( int i=0;i<id2.length;++i, ++index )
		{
			uuid_bytes[index] = id2[i];
		}

		//The tuning semitones
		for ( int i=0;i<intervals.getNumNotes();++i, ++index )
		{
			//Double casting. Now that's dumb!
			uuid_bytes[index] = (byte)intervals.getNote(i).getOctaveSemitone();
		}
				
		return UUID.nameUUIDFromBytes( uuid_bytes );
	}
	@JsonIgnore
	public UUID getUUID()
	{
		if ( mUUID == null )
		{
			throw new NullPointerException("UUID must be generated first.");
		}
		return mUUID;
	}
}
