package net.runelite.client.plugins.collectionlog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.widgets.WidgetInfo;

@Getter
@RequiredArgsConstructor
public enum CollectionLogTabs
{
	BOSSES("Bosses"),
	RAIDS("Raids"),
	CLUES("Clues"),
	MINIGAMES("Minigames"),
	OTHER("Other");

	private final String displayName;

	public static CollectionLogTabs fromTabIndex(int tab)
	{
		switch (tab)
		{
			case 0:
				return BOSSES;
			case 1:
				return RAIDS;
			case 2:
				return CLUES;
			case 3:
				return MINIGAMES;
			case 4:
				return OTHER;
		}
		return null;
	}

	public static WidgetInfo toTitleWidgetInfo(CollectionLogTabs tab)
	{
		switch (tab)
		{
			case BOSSES:
				return WidgetInfo.COLLECTION_LOG_BOSSES;
			case RAIDS:
				return WidgetInfo.COLLECTION_LOG_RAIDS;
			case CLUES:
				return WidgetInfo.COLLECTION_LOG_CLUES;
			case MINIGAMES:
				return WidgetInfo.COLLECTION_LOG_MINIGAMES;
			case OTHER:
				return WidgetInfo.COLLECTION_LOG_OTHER;
			default:
				return null;
		}
	}

	public static WidgetInfo toListWidgetInfo(CollectionLogTabs tab)
	{
		switch (tab)
		{
			case BOSSES:
				return WidgetInfo.COLLECTION_LOG_ITEM_LIST_BOSSES;
			case RAIDS:
				return WidgetInfo.COLLECTION_LOG_ITEM_LIST_RAIDS;
			case CLUES:
				return WidgetInfo.COLLECTION_LOG_ITEM_LIST_CLUES;
			case MINIGAMES:
				return WidgetInfo.COLLECTION_LOG_ITEM_LIST_MINIGAMES;
			case OTHER:
				return WidgetInfo.COLLECTION_LOG_ITEM_LIST_OTHER;
			default:
				return null;
		}
	}

	public static WidgetInfo listWidgetInfoFromTabIndex(int tabIndex)
	{

		CollectionLogTabs tab = fromTabIndex(tabIndex);

		if (tab == null)
		{
			return null;
		}

		return toListWidgetInfo(tab);
	}
}
