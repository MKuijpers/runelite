package net.runelite.client.plugins.collectionlog;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("collectionlog")
public interface CollectionLogConfig extends Config
{
	@ConfigItem(
		keyName = "colorCompletedItems",
		name = "Color completed items",
		description = "Color collection log items when completed"
	)
	default boolean colorCompletedItems()
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

	@ConfigItem(
		keyName = "showHoverPercentages",
		name = "Show hover percentages",
		description = "Show the progression percentages on hover."
	)
	default boolean showHoverPercentages()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showProgressBars",
		name = "Show progress bars",
		description = "Show progression progress bars."
	)
	default boolean showProgressBars()
	{
		return true;
	}
}
