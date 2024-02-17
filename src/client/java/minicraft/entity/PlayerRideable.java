package minicraft.entity;

import minicraft.entity.mob.Player;
import minicraft.util.Vector2;

public interface PlayerRideable {
	/** @return {@code true} if the passenger is valid. */
	boolean rideTick(Player passenger, Vector2 vec);

	/** @return {@code true} if the {@code player} successfully rode on the ride. */
	boolean startRiding(Player player);

	/** @return {@code true} if the {@code player} successfully stopped from the ride, or was not on the ride. */
	boolean stopRiding(Player player);
}
