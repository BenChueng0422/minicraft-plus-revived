package minicraft.level.tile.farming;

import minicraft.gfx.Screen;
import minicraft.gfx.SpriteLinker;
import minicraft.level.Level;
import minicraft.level.tile.Tiles;

public class HellishBerriesTile extends CropTile {
	private final SpriteLinker.LinkedSprite[] spritStages = new SpriteLinker.LinkedSprite[] {
		new SpriteLinker.LinkedSprite(SpriteLinker.SpriteType.Tile, "hellish_berries_stage0"),
		new SpriteLinker.LinkedSprite(SpriteLinker.SpriteType.Tile, "hellish_berries_stage1"),
		new SpriteLinker.LinkedSprite(SpriteLinker.SpriteType.Tile, "hellish_berries_stage2"),
		new SpriteLinker.LinkedSprite(SpriteLinker.SpriteType.Tile, "hellish_berries_stage3")
	};

	public HellishBerriesTile(String name) {
		super(name, null);
	}

	@Override
	public void render(Screen screen, Level level, int x, int y) {
		int age = (level.getData(x, y) >> 3) & maxStage;
		Tiles.get("Farmland").render(screen, level, x, y);
		int stage;
		if (age < 2) stage = 0;
		else if (age < 4) stage = 1;
		else if (age < 7) stage = 2;
		else stage = 3;
		screen.render(x * 16, y * 16, spritStages[stage]);
	}
}
