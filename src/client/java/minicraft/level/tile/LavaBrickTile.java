package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.gfx.SpriteAnimation;
import minicraft.gfx.SpriteLinker.SpriteType;
import minicraft.item.Item;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;
import minicraft.util.AdvancementElement;
import minicraft.util.DamageSource;
import org.jetbrains.annotations.Nullable;

public class LavaBrickTile extends Tile {
	protected LavaBrickTile(String name) {
		super(name, new SpriteAnimation(SpriteType.Tile, "missing_tile"));
	}

	@Override
	protected void handleDamage(Level level, int x, int y, Entity source, @Nullable Item item, int dmg) {}

	@Override
	public boolean hurt(Level level, int x, int y, Entity source, @Nullable Item item, Direction attackDir, int damage) {
		if (item instanceof ToolItem && source instanceof Player) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Pickaxe) {
				if (((Player) source).payStamina(4 - tool.level) && tool.payDurability()) {
					int data = level.getData(x, y);
					level.setTile(x, y, Tiles.get("Lava"));
					Sound.play("monsterhurt");
					AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.INSTANCE.trigger(
						new AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.ItemUsedOnTileTriggerConditionHandler.ItemUsedOnTileTriggerConditions(
							item, this, data, x, y, level.depth));
					return true;
				}
			}
		}
		return false;
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		if (entity instanceof Mob)
			entity.hurt(new DamageSource(DamageSource.DamageType.HOT_FLOOR, level, (x << 4) + 8, (y << 4) + 8, this),
				Direction.NONE, 3);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return e.canWool();
	}
}
