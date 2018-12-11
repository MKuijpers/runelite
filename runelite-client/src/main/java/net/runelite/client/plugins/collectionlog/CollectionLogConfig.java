package net.runelite.client.plugins.collectionlog;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("collectionlog")
public interface CollectionLogConfig extends Config
{
	@ConfigItem(
		keyName = "colorItems",
		name = "Color items",
		description = "Color collection log items when completed"
	)
	default boolean colorItems()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPercentages",
		name = "Show percentages",
		description = "Show the progression percentages next to the collection log names."
	)
	default boolean showPercentages()
	{
		return true;
	}
}
