package net.runelite.client.plugins.collectionlog;

import com.google.common.base.Splitter;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import static net.runelite.client.plugins.collectionlog.CollectionLogTabs.*;

@PluginDescriptor(
	name = "Collection Log",
	description = "Show collection log",
	tags = {"collection", "log"},
	enabledByDefault = false
)
@Slf4j
public class CollectionLogPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Inject
	private CollectionLogConfig config;

	private static final Pattern OBTAINED_FORMAT = Pattern.compile("Obtained: <col=.+>([0-9]+)/([0-9]+)</col>");
	private static final Splitter DOT_SPLITTER = Splitter.on(".")
		.trimResults()
		.omitEmptyStrings();

	@Provides
	CollectionLogConfig collectionLogConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CollectionLogConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		log.info("Start collection log percentages");
		ItemContainer items = client.getItemContainer(InventoryID.COLLECTION_LOG);
		if (items != null)
		{
			log.info(items.getItems().length + "");
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Stop collection log percentages");
	}

	@Subscribe
	public void onScriptCallbackEvent(ScriptCallbackEvent event)
	{
		final String eventName = event.getEventName();

		if (!event.getEventName().equals("itemStackSize"))
		{
			return;
		}

		int[] ints = client.getIntStack();
		int latest = ints[client.getIntStackSize() - 1];
		log.info(latest + "");
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{

		ItemContainer items = client.getItemContainer(InventoryID.COLLECTION_LOG);
		if (items != null)
		{
			log.info(items.getItems().length + "");
		}

		int tabIndex = client.getVar(Varbits.COLLECTION_LOG_TAB);
		List<CollectionLogItems> currentTabItems = CollectionLogItems.getItemsFromTabIndex(tabIndex);

		int itemIndex = client.getVar(Varbits.COLLECTION_LOG_ITEM);
		CollectionLogItems item = currentTabItems.get(itemIndex);

		Widget container = client.getWidget(WidgetInfo.COLLECTION_LOG_CONTAINER);
		if (container != null && !container.isHidden())
		{
			Widget progress = client.getWidget(WidgetInfo.COLLECTION_LOG_PROGRESS).getChild(1);
			Matcher m = OBTAINED_FORMAT.matcher(progress.getText());
			if (m.matches())
			{
				float obtained = Float.valueOf(m.group(1));
				float total = Float.valueOf(m.group(2));
				float perc = obtained / total;

				configManager.setConfiguration(CollectionLogConfig.CONFIG_GROUP + "." + client.getLocalPlayer().getName(),
					item.name() + "." + CollectionLogConfig.QUANTITY, total);
				configManager.setConfiguration(CollectionLogConfig.CONFIG_GROUP + "." + client.getLocalPlayer().getName(),
					item.name() + "." + CollectionLogConfig.PROGRESS, perc);

				setPercentageText();
			}
		}
	}

	private void setPercentageText()
	{
		int tabIndex = client.getVar(Varbits.COLLECTION_LOG_TAB);
		List<CollectionLogItems> currentTabItems = CollectionLogItems.getItemsFromTabIndex(tabIndex);
		Widget itemList = itemListWidgetFromTab(tabIndex);

		for (int i = 0; i < currentTabItems.size(); i++)
		{
			CollectionLogItems item = currentTabItems.get(i);
			String strPerc = configManager.getConfiguration(CollectionLogConfig.CONFIG_GROUP + "." + client.getLocalPlayer().getName(), item.getDisplayName());
			float perc = strPerc == null ? 0f : Float.valueOf(strPerc);
//			float perc = itemProgress.getOrDefault(item, 0f);

			if (config.showPercentages())
			{
				itemList.getChild(i).setText("" + item.getDisplayName() + " (" + String.format("%.1f", perc * 100) + "%)");
			}

			if (config.colorItems() && perc >= 1f)
			{
				itemList.getChild(i).setTextColor(new Color(13, 193, 13).getRGB());
			}
		}
	}

	private List<Integer> testVar()
	{
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 7000; i++)
		{
			int var = client.getVarbit(i);
			if (var == 5)
			{
				list.add(i);
			}
		}
		return list;
	}

	private Widget itemListWidgetFromTab(int tab)
	{
		CollectionLogTabs type = fromTabIndex(tab);

		if (type == null)
		{
			return null;
		}

		switch (type)
		{
			case BOSSES:
				return client.getWidget(WidgetInfo.COLLECTION_LOG_ITEM_LIST_BOSSES);
			case RAIDS:
				return client.getWidget(WidgetInfo.COLLECTION_LOG_ITEM_LIST_RAIDS);
			case CLUES:
				return client.getWidget(WidgetInfo.COLLECTION_LOG_ITEM_LIST_CLUES);
			case MINIGAMES:
				return client.getWidget(WidgetInfo.COLLECTION_LOG_ITEM_LIST_MINIGAMES);
			case OTHER:
				return client.getWidget(WidgetInfo.COLLECTION_LOG_ITEM_LIST_OTHER);
			default:
				return null;
		}

	}
}
