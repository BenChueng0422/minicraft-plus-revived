package minicraft.screen;

import minicraft.entity.Player;
import minicraft.gfx.Font;
import minicraft.gfx.Screen;
import minicraft.sound.Sound;
import minicraft.item.ResourceItem;

public class InventoryMenu extends Menu {
	private Player player;
	private int selected = 0;

	public InventoryMenu(Player player) {
		this.player = player;
		
		if (player.activeItem != null) { // If the player has an active item, then...
			player.inventory.add(0, player.activeItem); // that active item will go into the inventory
			player.activeItem = null; // the player will not have an active item anymore.
		}
	}

	public void tick() {
		if (input.getKey("menu").clicked) game.setMenu(null);

		if (input.getKey("up").clicked) selected--;
		if (input.getKey("down").clicked) selected++;
		if (input.getKey("up").clicked) Sound.pickup.play();
		if (input.getKey("down").clicked) Sound.pickup.play();
		
		int len = player.inventory.invSize();
		if (len == 0) selected = 0;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;
		
		if (input.getKey("attack").clicked && len > 0) { // If your inventory is not empty, and the player presses the "Attack" key...
			player.activeItem = player.inventory.remove(selected); // The item will be removed from the inventory, and placed as the player's active item.
			game.setMenu(null); // the game will go back to the gameplay
		}
	}

	public void render(Screen screen) {
		renderFrame(screen, "inventory", 1, 1, 22, 11); // renders the blue box for the inventory
		renderItemList(screen, 1, 1, 22, 11, player.inventory.getItems(), selected); // renders the icon's and names of all the items in your inventory.
	}
}
