# Choordinates

Text form if you don't want to download resources/choordinates.html

Choordinates 1.0

Overview
Easily identify notes and chords on any string instrument with frets in the Western scale.  You want to know how to play a 6 add 9 chord on an 8-stringed left handed tenor banjolele?  No problem!
Disclaimer
Free as-is fully open source software under GNU GPL general license. Visit https://github.com/obscurestar/choordinates for source files.

Choordinates Main  Window
Menus
Window
Tunings
Chords
Preferences
Help
About
Report Issue
Tuning dropdown
Select a tuning from the tuning dropdown to configure the fretboard display. Notes and favorites already on the screen will be dynamically reconfigured to match the current form.  Tunings may be customized from the Tuning Window in the Window menu.
Search pane
Chord tab
Identify a chord by selecting from the listbox on the left and a root note.
Choose a root note and search to display all notes belonging to the chord on the fretboard display.  ♭b - following the letter name of the note indicate a flat.  Multiple flat characters may be used. ♯ # or + may be used in the same way to denote one or more sharps.  Note names may be upper or lower case.  Some examples C A# bb D##
Notes tab
Identify notes on the fretboard in the notes text field.  Note names are separated one or more spaces and otherwise follow the same naming conventions.
Frets tab
Mashed some strings and it sounded cool but you don’t know what it was?  Set the fret# to adjust the position of the fret entry pane and click on the strings. Search as per usual.
Matches pane
Matches display the name of the chord(s) which best match the search conditions as well as display the note names and intervals of the selected chord. 
Fretboard pane
Displays all the currently selected notes on the fretboard in the tuning. For now, the color of the markings indicates its position in the chord as displayed in the Matches pane.
Black is the root note
White is the nearest positive interval to the root
Yellow the next
Orange
Red
Pink
If you have more than 6 fingers, the colors loop back to black.  Sorry!
Clicking on the fretboard pane will select or deselect a note on that string and mark the background orange. The selected notes may be stored as a favorite via the Add Favorite button.  Selections will be erased when the Tuning or displayed notes are changed.
Favorite Shapes pane
Favorite shapes are tied to the relative tuning of the strings and the interval chord.  Favorites from alternate named chords and tunings will appear in their respective contexts. Examples:
A favorite of an Major chord (1 3 5) will be displayed on EADGBe tuning and DGEFAd as the relative pitches of the strings to one another is consistent.  The same favorite would not appear in drop D (DA DGBe) tuning as the shape would not be valid for the given chord.
Likewise, chords that contain the same absolute intervals will display related favorites.  For example, a minor chord (1 3♭ 5) and an incidental chord ( 1 2♯ 4♯ ) chord will display the the same shapes.
Left-clicking a favorite shape will highlight it on the fretboard display at each place it occurs. 
Right-clicking a favorite shape will pop up a confirm window to delete the shape.
Tuning window
This poorly thought out UI allows you to select tunings to modify, copy or delete them.
Tunings dropdown allows selection from the currently defined tunings and will populate the Tuning Name and Strings fields.
Tuning Names are any valid UTF-8 character string.
Strings define the root notes for each string in ascending order and presently supports 1-8 strings.  Increasing the maximum would be trivial.  File a bug report if you need more or change it yourself in the source.   String names follow the same convention as elsewhere with one exception, Ab, C#, D- and F♯ are all valid note names.
Exception:  Lower case is higher pitch.  EG:  e is 12 semitones above E.   Aside from the fret display this feature is presently unused but in the future may be used to make violin type tunings display more naturally. 
New  is gung ho and will create as many copies of the current tuning as you care to click.  It does not care or provide any feedback. You’ve been warned.
Save will attempt to overwrite the current tuning but will at least pop up a dialog to confirm that this is really what you want to do.
Delete does about what you’d expect, deleting the currently selected tuning after a confirmation dialog.
Close also will offer a confirmation dialog if there are unsaved changes in the entry fields.
Chords window
Behaves similar to the Chords pane with a few small differences.  This is where you can add chord names or correct any mistakes I made entering them.  It has a few additional questionable design elements as well.
Chords listbox:  Why did I use a dropdown in one and a listbox in the other?  It made sense at the time. Unless your guitar has a rubber neck, you’ll likely have more chords than guitars.    Clicking a tuning name in this display should load its details into the fields on the left.
Name field can be whatever you want the full name of this chord to be. 
Symbol likewise can be 0 to many characters and displays as the short name of the chord.  Some examples ‘ ‘ (Major chord) m, minor, m7, minor seventh, ∆ major 7 and so on. 
Aliases are other representations of the same chord eg ‘jazz’, ‘M7’ for the major 7th.  At present they’re only displayed in this pane.  When I get a new shipment of round tuits, they may get more visibility, like the dropdown menus and list boxes.
Intervals define the intervals that comprise this chord.  They follow the same flat/sharp and other naming conventions as letter-named notes with three exceptions.
Exception 1: Interval numbers can be more than 1 digit.  8 is the octave, 16 the second octave, etc, but really how long are your fingers?
Exception 2:  Inverted chords can be denoted with a - preceding the tonic.   If you want your major 7 jazz chord to look even jazzier, you can call it 1 3 5 -2 if you really want to.  Negative interval numbers obey the scale pattern.
Exception 3:  0 is not a valid interval. Those ancient Greeks didn’t know nothing.
New, save, Delete, and Close exhibit similar irksome behavior to their counterparts in the Tuning pane but I swapped the order around to keep things interesting.
Preferences window
Lefty
Never hold a chord book in front of a mirror again.   This will flip the fretboard displays. 
Neck Length
Controls the number of frets in the main fretboard display.  Max is 24 because really it just repeats every 12 frets.  A little math won’t hurt anyone.
Chord Frames
Controls how many frets you will see in the Frets search pane and the favorite shapes displays.  Please be warned that fret shapes are not presently dynamic.  If your chord spans 5 frets and you set this to 2 you’re only going to see two frets of the shape until you display it on the fretboard.
About
About as useful as every other about box.  Think of it as a vestigial organ.
Report Issue
Asks your computer to open a web browser so you lambast me on github.
Choordinates Help
Presumably where you found this document.

