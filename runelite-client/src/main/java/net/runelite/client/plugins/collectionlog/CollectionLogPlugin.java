package net.runelite.client.plugins.collectionlog;

import com.google.common.base.Splitter;
import com.google.inject.Provides;
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
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

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

	private static final String PERC_COLOR = "ffce95";
	private static final Pattern OBTAINED_FORMAT = Pattern.compile("Obtained: <col=.+>([0-9]+)/([0-9]+)</col>");
	private static final Splitter DOT_SPLITTER = Splitter.on(".")
		.trimResults()
		.omitEmptyStrings();

	private static final int PROGRESS_BAR_HEIGHT = 4;

	@Provides
	CollectionLogConfig collectionLogConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CollectionLogConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		log.info("Start collection log percentages");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Stop collection log percentages");
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		ItemContainer items = client.getItemContainer(InventoryID.COLLECTION_LOG);
		if (items != null)
		{
			log.info(items.getItems().length + "");
		}

		// Getting category
		int tabIndex = client.getVar(Varbits.COLLECTION_LOG_TAB);
		List<CollectionLogCategories> currentTabCategories = CollectionLogCategories.fromTabIndex(tabIndex);
		int categoryIndex = client.getVar(Varbits.COLLECTION_LOG_ITEM);
		CollectionLogCategories category = currentTabCategories.get(categoryIndex);

		Widget container = client.getWidget(WidgetInfo.COLLECTION_LOG_CONTAINER);
		if (container != null && !container.isHidden())
		{
			Widget progressWidget = client.getWidget(WidgetInfo.COLLECTION_LOG_PROGRESS).getChild(1);
			Matcher m = OBTAINED_FORMAT.matcher(progressWidget.getText());
			if (m.matches())
			{
				float obtained = Float.valueOf(m.group(1));
				float quantity = Float.valueOf(m.group(2));
				float progress = obtained / quantity;

				setConfigValue(category, ConfigKey.OBTAINED, obtained);
				setConfigValue(category, ConfigKey.QUANTITY, quantity);
				setConfigValue(category, ConfigKey.PROGRESS, progress);

				setPercentageText();
			}
		}
	}

	private void setPercentageText()
	{
		// Current category percentages
		int tabIndex = client.getVar(Varbits.COLLECTION_LOG_TAB);
		List<CollectionLogCategories> currentTabCategories = CollectionLogCategories.fromTabIndex(tabIndex);
		Widget widgetItemList = itemListWidgetFromTab(tabIndex);

		for (int i = 0; i < currentTabCategories.size(); i++)
		{
			CollectionLogCategories category = currentTabCategories.get(i);
			float perc = getCategoryProgress(category);
			setWidgetProgress(widgetItemList.getChild(i), category.getDisplayName(), perc, 0);
		}

		// Tab percentages
		CollectionLogTabs[] tabs = CollectionLogTabs.values();
		for (CollectionLogTabs tab : tabs)
		{
			float progress = getTabProgress(tab);
			Widget tabTitleWidget = client.getWidget(CollectionLogTabs.toTitleWidgetInfo(tab)).getChild(3);
			setWidgetProgress(tabTitleWidget, tab.getDisplayName(), progress, 0);
		}

		// Title percentage
		float collectionLogProgress = getCollectionLogProgress();
		Widget titleWidget = client.getWidget(WidgetInfo.COLLECTION_LOG_ELEMENTS).getChild(1);
		setWidgetProgress(titleWidget, "Collection Log", collectionLogProgress, 1);
	}

	private void setWidgetProgress(Widget widget, String prefixName, float progress, int decimals)
	{
		String widgetText = prefixName;

		if (config.showPercentages())
		{
			String strPerc = progress == -1f ? "-" : String.format("%." + decimals + "f", progress * 100);
			widgetText += " <col=" + PERC_COLOR + ">(" + strPerc + "%)</col>";
		}

		if (config.colorItems() && progress >= 1.0f)
		{
			widgetText = "<col=19f052>" + widgetText + "</col>";
		}

		widget.setText(widgetText);
	}

	private float getTabProgress(CollectionLogTabs tab)
	{
		float totalObtained = getTabStoredTotal(tab, ConfigKey.OBTAINED);
		float totalQuantity = getTabStoredTotal(tab, ConfigKey.QUANTITY);

		if (totalObtained == -1f || totalQuantity == -1f)
		{
			return -1f;
		}

		return totalObtained / totalQuantity;
	}

	private float getTabStoredTotal(CollectionLogTabs tab, ConfigKey configKey)
	{
		List<CollectionLogCategories> categories = CollectionLogCategories.fromTab(tab);
		float total = 0f;
		for (CollectionLogCategories category : categories)
		{
			String value = getStoredValue(category, configKey);
			if (value == null)
			{
				return -1f;
			}
			total += Float.valueOf(value);
		}
		return total;
	}

	private float getCollectionLogProgress()
	{
		CollectionLogTabs[] tabs = CollectionLogTabs.values();

		float totalObtained = 0f;
		float totalQuantity = 0f;

		for (CollectionLogTabs tab : tabs)
		{
			float obtained = getTabStoredTotal(tab, ConfigKey.OBTAINED);
			float quantity = getTabStoredTotal(tab, ConfigKey.QUANTITY);

			if (obtained == -1f || quantity == -1f || Float.valueOf(quantity) == 0)
			{
				return -1f;
			}
			else
			{
				totalObtained += obtained;
				totalQuantity += quantity;
			}
		}

		return totalObtained / totalQuantity;
	}

	private float getCategoryProgress(CollectionLogCategories category)
	{
		String storedObt = getStoredValue(category, ConfigKey.OBTAINED);
		String storedQnty = getStoredValue(category, ConfigKey.QUANTITY);

		if (storedObt == null || storedQnty == null || Float.valueOf(storedQnty) == 0)
		{
			return -1f;
		}

		return Float.valueOf(storedObt) / Float.valueOf(storedQnty);
	}

	private void clearConfig()
	{
		String group = ConfigKey.CONFIG_GROUP + "." + client.getLocalPlayer().getName();
		List<String> configs = configManager.getConfigurationKeys(group);

		for (String config : configs)
		{
			List<String> split = DOT_SPLITTER.splitToList(config);
			configManager.unsetConfiguration(group, split.get(split.size() - 2) + "." + split.get(split.size() - 1));
		}
	}

	private String getStoredValue(CollectionLogCategories category, ConfigKey configKey)
	{
		String group = ConfigKey.CONFIG_GROUP + "." + client.getLocalPlayer().getName();
		String key = category.name() + "." + configKey;

		return configManager.getConfiguration(group, key);
	}

	private void setConfigValue(CollectionLogCategories category, ConfigKey configKey, Object value)
	{
		String group = ConfigKey.CONFIG_GROUP + "." + client.getLocalPlayer().getName();
		String key = category.name() + "." + configKey;

		configManager.setConfiguration(group, key, value);
	}

	private Widget itemListWidgetFromTab(int tab)
	{
		WidgetInfo widgetInfo = CollectionLogTabs.listWidgetInfoFromTabIndex(tab);

		if (widgetInfo == null)
		{
			return null;
		}

		return client.getWidget(widgetInfo);
	}
}
