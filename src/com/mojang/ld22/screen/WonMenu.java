package com.mojang.ld22.screen;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class WonMenu extends Menu {
	private int inputDelay = 0; // variable to delay the input of the player, so they won't skip the won menu the first second.
		//except... now they will.
	
	/* WonMenu & DeadMenu are very similar... scratch that, the exact same class with text changes. */
		//i.e, see deadmenu for comments.
	
	public WonMenu() {}

	public void tick() {
		//on menu button press, returns to title screen.
		if (input.getKey("enter").clicked || input.getKey("menu").clicked) {
			game.setMenu(new TitleMenu());
		}
	}

	public void render(Screen screen) {
		Font.renderFrame(screen, "", 1, 3, 21, 9);
		Font.draw("Game Over!", screen, 2 * 8, 4 * 8, Color.get(-1, 555, 555, 555));

		int seconds = game.gameTime / 60;
		int minutes = seconds / 60;
		int hours = minutes / 60;
		minutes %= 60;
		seconds %= 60;

		String timeString = "";
		if (hours > 0) timeString = hours + "h" + (minutes < 10 ? "0" : "") + minutes + "m";
		else timeString = minutes + "m " + (seconds < 10 ? "0" : "") + seconds + "s";

		//Font.draw("Time:", screen, 2 * 8, 5 * 8, Color.get(-1, 555, 555, 555));
		//Font.draw(timeString, screen, (2 + 5) * 8, 5 * 8, Color.get(-1, 550, 550, 550));
		Font.draw("Score:", screen, 2 * 8, 6 * 8, Color.get(-1, 555, 555, 555));
		Font.draw("" + game.player.score, screen, (2 + 6) * 8, 6 * 8, Color.get(-1, 550, 550, 550));
		Font.draw("Press Enter to continue", screen, 2 * 8, 8 * 8, Color.get(-1, 333, 333, 333));
	}
}
