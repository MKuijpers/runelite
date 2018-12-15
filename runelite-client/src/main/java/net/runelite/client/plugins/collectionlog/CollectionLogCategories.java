package net.runelite.client.plugins.collectionlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CollectionLogCategories
{
	ABYSSAL_SIRE(CollectionLogTabs.BOSSES, "Abyssal Sire"),
	BARROWS_CHEST(CollectionLogTabs.BOSSES, "Barrows Chests"),
	BRYOPHYTA(CollectionLogTabs.BOSSES, "Bryophyta"),
	CALLISTO(CollectionLogTabs.BOSSES, "Callisto"),
	CERBERUS(CollectionLogTabs.BOSSES, "Cerberus"),
	CHAOS_ELEMENTAL(CollectionLogTabs.BOSSES, "Chaos Elemental"),
	CHAOS_FANATIC(CollectionLogTabs.BOSSES, "Chaos Fanatic"),
	COMMANDER_ZILYANA(CollectionLogTabs.BOSSES, "Commander Zilyana"),
	CORPOREAL_BEAST(CollectionLogTabs.BOSSES, "Corporeal Beast"),
	CRAZY_ARCHAEOLOGIST(CollectionLogTabs.BOSSES, "Crazy Archaeologist"),
	DAGANNOTH_KINGS(CollectionLogTabs.BOSSES,  "Dagannoth Kings"),
	THE_FIGHT_CAVES(CollectionLogTabs.BOSSES,  "The Fight Caves"),
	GENERAL_GRAARDOR(CollectionLogTabs.BOSSES,  "General Graardor"),
	GIANT_MOLE(CollectionLogTabs.BOSSES,  "Giant Mole"),
	GROTESQUE_GUARDIANS(CollectionLogTabs.BOSSES,  "Grotesque Guardians"),
	THE_INFERNO(CollectionLogTabs.BOSSES,  "The Inferno"),
	KALPHITE_QUEEN(CollectionLogTabs.BOSSES,  "Kalphite Queen"),
	KING_BLACK_DRAGON(CollectionLogTabs.BOSSES,  "King Black Dragon"),
	KRAKEN(CollectionLogTabs.BOSSES,  "Kraken"),
	KREEARRA(CollectionLogTabs.BOSSES,  "Kree'arra"),
	KRIL_TSUTSAROTH(CollectionLogTabs.BOSSES,  "K'ril Tsutsaroth"),
	OBOR(CollectionLogTabs.BOSSES,  "Obor"),
	SCORPIA(CollectionLogTabs.BOSSES,  "Scorpia"),
	SKOTIZO(CollectionLogTabs.BOSSES,  "Skotizo"),
	THERMONUCLEAR_SMOKE_DEVIL(CollectionLogTabs.BOSSES,  "Thermonuclear Smoke Devil"),
	VENENATIS(CollectionLogTabs.BOSSES,  "Venenatis"),
	VETION(CollectionLogTabs.BOSSES,  "Vet'ion"),
	VORKATH(CollectionLogTabs.BOSSES,  "Vorkath"),
	WINTERTODT(CollectionLogTabs.BOSSES,  "Wintertodt"),
	ZULRAH(CollectionLogTabs.BOSSES,  "Zulrah"),

	CHAMBERS_OF_XERIC(CollectionLogTabs.RAIDS, "Chambers of Xeric"),
	THEATRE_OF_BLOOD(CollectionLogTabs.RAIDS, "Theatre of Blood"),

	EASY_TREASURE_TRAILS(CollectionLogTabs.CLUES, "Easy Treasure Trails"),
	MEDIUM_TREASURE_TRAILS(CollectionLogTabs.CLUES, "Medium Treasure Trails"),
	HARD_TREASURE_TRAILS(CollectionLogTabs.CLUES, "Hard Treasure Trails"),
	ELITE_TREASURE_TRAILS(CollectionLogTabs.CLUES, "Elite Treasure Trails"),
	MASTER_TREASURE_TRAILS(CollectionLogTabs.CLUES, "Master Treasure Trails"),
	SHARED_TREASURE_TRAILED_REWARDS(CollectionLogTabs.CLUES, "Shared Treasure Trails Rewards"),

	BARBARIAN_ASSAULT(CollectionLogTabs.MINIGAMES, "Barbarian Assault"),
	CASTLE_WARS(CollectionLogTabs.MINIGAMES, "Castle Wars"),
	FISHING_TRAWLER(CollectionLogTabs.MINIGAMES, "Fishing Trawler"),
	GNOME_RESTAURANT(CollectionLogTabs.MINIGAMES, "Gnome Restaurant"),
	MAGE_TRAINING_ARENA(CollectionLogTabs.MINIGAMES, "Magic Training Arena"),
	PEST_CONTROL(CollectionLogTabs.MINIGAMES, "Pest Control"),
	ROGUES_DEN(CollectionLogTabs.MINIGAMES, "Rogues' Den"),
	SHADES_OF_MORTTON(CollectionLogTabs.MINIGAMES, "Shades of Mort'ton"),
	TEMPLE_TREKKING(CollectionLogTabs.MINIGAMES, "Temple Trekking"),
	TITHE_FARM(CollectionLogTabs.MINIGAMES, "Tithe Farm"),
	TROUBLE_BREWING(CollectionLogTabs.MINIGAMES,  "Trouble Brewing"),

	ALL_PETS(CollectionLogTabs.OTHER, "All Pets"),
	CHAMPIONS_CHALLENGE(CollectionLogTabs.OTHER, "Champion's Challenge"),
	CHAOS_DRUIDS(CollectionLogTabs.OTHER, "Chaos Druids"),
	CHOMPY_BIRD_HUNTING(CollectionLogTabs.OTHER, "Chompy Bird Hunting"),
	CYCLOPES(CollectionLogTabs.OTHER, "Cyclopes"),
	GLOUGHS_EXPERIMENTS(CollectionLogTabs.OTHER, "Glough's Experiments"),
	MOTHERLODE_MINE(CollectionLogTabs.OTHER, "Motherlode Mine"),
	RANDOM_EVENTS(CollectionLogTabs.OTHER, "Random Events"),
	REVENANTS(CollectionLogTabs.OTHER, "Revenants"),
	SHAYZIEN_ARMOUR(CollectionLogTabs.OTHER, "Shayzien Armour"),
	SKILLING_PETS(CollectionLogTabs.OTHER, "Skilling Pets"),
	SLAYER(CollectionLogTabs.OTHER, "Slayer"),
	TZHAAR(CollectionLogTabs.OTHER,  "TzHaar"),
	MISCELLANEOUS(CollectionLogTabs.OTHER,  "Miscellaneous");

	private final CollectionLogTabs type;
	private final String displayName;

	private static final Map<CollectionLogTabs, List<CollectionLogCategories>> ALL_CATEGORIES = new HashMap<>();

	static
	{
		for (CollectionLogCategories category : values())
		{
			List<CollectionLogCategories> list = ALL_CATEGORIES.getOrDefault(category.getType(), new ArrayList<>());
			list.add(category);
			ALL_CATEGORIES.put(category.getType(), list);
		}
	}

	public static List<CollectionLogCategories> fromTab(CollectionLogTabs tab)
	{
		return ALL_CATEGORIES.getOrDefault(tab, new ArrayList<>());
	}

	public static List<CollectionLogCategories> fromTabIndex(int tabIndex)
	{
		return ALL_CATEGORIES.getOrDefault(CollectionLogTabs.fromTabIndex(tabIndex), new ArrayList<>());
	}
}
