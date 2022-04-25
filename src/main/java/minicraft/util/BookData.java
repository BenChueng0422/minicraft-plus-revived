package minicraft.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

import minicraft.core.Game;
import minicraft.saveload.Load;
import minicraft.saveload.Save;
import minicraft.screen.WorldSelectDisplay;

public class BookData {
	private static Random random = new Random();

	public static final String about = loadStaticBook("about");
	public static final String credits = loadStaticBook("credits");
	public static final String instructions = loadStaticBook("instructions");
	public static final String antVenomBook = loadStaticBook("antidous");
	public static final String storylineGuide = loadStaticBook("story_guide");
	
	private static String loadStaticBook(String bookTitle) {
		String book;
		try {
			book = String.join("\n", Load.loadFile("/resources/books/" + bookTitle + ".txt"));
			book = book.replaceAll("\\\\0", "\0");
		} catch (IOException ex) {
			ex.printStackTrace();
			book = "";
		}
		
		return book;
	}

	public String title;
	public String content;
	public boolean editable;
	public String author;
	public final String id;
	public BookData(String id, String title, String content, boolean editable, String author) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.editable = editable;
		this.author = author;
	}
	public BookData() {
		this(genNewID(), "", "", true, "");
	}
	public BookData(JSONObject data) {
		this(data.getString("id"), data.getString("title"), data.getString("content"), data.getBoolean("editable"), data.getString("author"));
	}

	public static BookData loadBook(String bookID) {
		try {
			return new BookData(new JSONObject(Load.loadFromFile(Game.gameDir + "/saves/" + WorldSelectDisplay.getWorldName() + "/books/" + bookID + ".book", true)));
		} catch (IOException e) {
			e.printStackTrace();
			return new BookData(bookID, "", "", false, "");
		}
	}

	private static String genNewID() {
		File dir = new File(Game.gameDir + "/saves/" + WorldSelectDisplay.getWorldName() + "/books/");
		dir.mkdirs();
		List<String> existIDs = Arrays.asList(dir.list((file, name) -> name.endsWith(".book"))).stream().map(b -> b.substring(0, 8)).collect(Collectors.toList());
		boolean valid = false;
		String id = "";
		while (!valid) {
			id = String.format("%08d", random.nextInt(99999999));
			if (!existIDs.contains(id)) valid = true;
		}
		return id;
	}

	public static void saveBook(BookData book) {
		new File(Game.gameDir + "/saves/" + WorldSelectDisplay.getWorldName() + "/books/").mkdirs();
		JSONObject json = new JSONObject();
		json.put("id", book.id);
		json.put("title", book.title);
		json.put("content", book.content);
		json.put("editable", book.editable);
		json.put("author", book.author);
		try {
			Save.writeJSONToFile(Game.gameDir + "/saves/" + WorldSelectDisplay.getWorldName() + "/books/" + book.id + ".book", json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
