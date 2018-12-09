package net.runelite.client.plugins.collectionlog;

public enum CollectionLogTabs
{
	BOSSES,
	RAIDS,
	CLUES,
	MINIGAMES,
	OTHER;

	public static CollectionLogTabs fromTabIndex(int i)
	{
		switch (i)
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
}
