package minicraft.level.tile;

import org.json.JSONObject;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;
import minicraft.screen.AchievementsDisplay;

/// this is all the spikey stuff (except "cloud cactus")
public class OreTile extends Tile {
	private final OreType type;
	
	public enum OreType {
        Iron (Items.get("Iron Ore"), 0),
		Lapis (Items.get("Lapis"), 2),
		Gold (Items.get("Gold Ore"), 4),
		Gem (Items.get("Gem"), 6),
		Cloud (Items.get("Cloud Ore"), 8);
		
		private final Item drop;
		public final int color;
		
		OreType(Item drop, int color) {
			this.drop = drop;
			this.color = color;
		}
		
		private Item getOre() {
			return drop.clone();
		}
    }
	
	protected OreTile(OreType o) {
		super((o == OreTile.OreType.Lapis ? "Lapis" : o == OreType.Cloud ? "Cloud Cactus" : o.name() + " Ore"), new Sprite(22 + o.color, 2, 2, 2, 1));
        this.type = o;
	}

	public void render(Screen screen, Level level, int x, int y) {
		sprite.color = DirtTile.dCol(level.depth);
		sprite.render(screen, x * 16, y * 16);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		hurt(level, x, y, 0);
		return true;
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if(Game.isMode("Creative"))
			return false; // Go directly to hurt method
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Pickaxe) {
				if (player.payStamina(6 - tool.level) && tool.payDurability()) {
					hurt(level, xt, yt, 1);
					return true;
				}
			}
		}
		return false;
	}
	
    public Item getOre() {
        return type.getOre();
    }

	public static JSONObject getDefaultData() {
		JSONObject obj = new JSONObject();
		obj.put("damage", 0);
		return obj;
	}
    
	public void hurt(Level level, int x, int y, int dmg) {
		int damage = level.getData(x, y).getInt("damage") + dmg;
		int oreH = random.nextInt(10) + 3;
		if (Game.isMode("Creative")) dmg = damage = oreH;
		
		level.add(new SmashParticle(x * 16, y * 16));
		Sound.monsterHurt.play();

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (dmg > 0) {
			int count = random.nextInt(2);
			if (damage >= oreH) {
				if (type == OreType.Cloud) {
					level.setTile(x, y, Tiles.get("Cloud"));
				} else {
					level.setTile(x, y, Tiles.get("Dirt"));
				}
				count += 2;
			} else {
				level.setData(x, y, "damage", damage);
			}
			if (type.drop.equals(Items.get("gem"))){
				AchievementsDisplay.setAchievement("minicraft.achievement.find_gem", true);
			}
			level.dropItem(x * 16 + 8, y * 16 + 8, count, type.getOre());
		}
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		/// this was used at one point to hurt the player if they touched the ore; that's probably why the sprite is so spikey-looking.
	}
}
