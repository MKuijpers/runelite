package net.runelite.client.plugins.collectionlog;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

public class CollectionLogOverlay extends Overlay
{
	@Inject
	private Client client;

	@Inject
	private CollectionLogPlugin plugin;

	@Inject
	private CollectionLogConfig config;

	@Provides
	CollectionLogConfig collectionLogConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CollectionLogConfig.class);
	}

	private final TooltipManager tooltipManager;

	private static final int TITLE_BAR_HEIGHT = 3;
	private static final int TAB_BAR_HEIGHT = 2;
	private static final int CATEGORY_BAR_HEIGHT = 1;

	private static final Color GREEN = new Color(56, 157, 74);
	private static final Color RED = new Color(155, 44, 38);

	@Inject
	CollectionLogOverlay(TooltipManager tooltipManager)
	{
		setPreferredPosition(OverlayPosition.TOP_LEFT);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.tooltipManager = tooltipManager;
	}

	@Override
	public Dimension render(Graphics2D g)
	{
		if (client.getWidget(WidgetInfo.COLLECTION_LOG_ROOT) == null ||
			(!config.showProgressBars() && !config.showHoverPercentages()))
		{
			return null;
		}

		// Title progress bar
		Widget titleWidget = client.getWidget(WidgetInfo.COLLECTION_LOG_ROOT).getChild(1);
		if (titleWidget != null)
		{
			float progress = plugin.getCollectionLogProgress();
			if (progress != -1f)
			{
				if (config.showProgressBars())
				{
					Rectangle rect = correctRectCoords(titleWidget.getBounds());
					Rectangle progressBarBounds = rectToProgressBarBounds(rect, TITLE_BAR_HEIGHT);
					drawProgressBar(g, progressBarBounds, progress);
				}

				if (config.showHoverPercentages() && isHovered(titleWidget.getBounds()))
				{
					tooltipManager.add(new Tooltip(generateProgressTooltip(progress, 2)));
				}
			}
		}

		// Tab progress bars
		for (CollectionLogTabs tab : CollectionLogTabs.values())
		{
			WidgetInfo widgetInfo = CollectionLogTabs.toTitleWidgetInfo(tab);
			Widget tabWidget = client.getWidget(widgetInfo);
			float progress = plugin.getTabProgress(tab);

			if (tabWidget != null && progress != -1f)
			{
				if (config.showProgressBars())
				{
					Rectangle rect = correctRectCoords(tabWidget.getBounds());
					Rectangle progressBarBounds = rectToProgressBarBounds(rect, TAB_BAR_HEIGHT);
					drawProgressBar(g, progressBarBounds, progress);
				}

				if (config.showHoverPercentages() && isHovered(tabWidget.getBounds()))
				{
					tooltipManager.add(new Tooltip(generateProgressTooltip(progress, 1)));
				}
			}
		}

		// Category progress bars
		int tabIndex = client.getVar(Varbits.COLLECTION_LOG_TAB);
		CollectionLogTabs tab = CollectionLogTabs.fromTabIndex(tabIndex);

		WidgetInfo listWidgetInfo = CollectionLogTabs.toListWidgetInfo(tab);
		Widget categoryWidgetList = client.getWidget(listWidgetInfo);

		Rectangle listContainerBoundsOrig = client.getWidget(WidgetInfo.COLLECTION_LOG_ITEM_LIST_CONTAINER).getBounds();
		Rectangle listContainerBounds = correctRectCoords(listContainerBoundsOrig);

		List<CollectionLogCategories> currentTabCategories = CollectionLogCategories.fromTabIndex(tabIndex);

		for (int i = 0; i < currentTabCategories.size(); i++)
		{
			Widget widget = categoryWidgetList.getChild(i);
			CollectionLogCategories category = currentTabCategories.get(i);
			float progress = plugin.getCategoryProgress(category);

			if (widget != null && progress != -1f)
			{
				Rectangle rect = correctRectCoords(widget.getBounds());
				Rectangle progressBarBounds = rectToProgressBarBounds(rect, CATEGORY_BAR_HEIGHT);

				if (config.showProgressBars() && listContainerBounds.contains(progressBarBounds))
				{
					drawProgressBar(g, progressBarBounds, progress);
				}

				if (config.showHoverPercentages() && tooltipManager.getTooltips().size() > 0 &&
					(isHovered(widget.getBounds()) && isHovered(listContainerBoundsOrig)))
				{
					tooltipManager.add(new Tooltip(generateProgressTooltip(progress, 1)));
				}
			}
		}

		return null;
	}

	private Rectangle rectToProgressBarBounds(Rectangle rect, int barHeight)
	{
		Rectangle progressBar = (Rectangle) rect.clone();
		progressBar.setLocation((int) progressBar.getX(), (int) (progressBar.getY() + progressBar.getHeight() - barHeight));
		progressBar.setSize((int) progressBar.getWidth(), barHeight);
		return progressBar;
	}

	private void drawProgressBar(Graphics2D g, Rectangle progressBar, float progress)
	{
		g.setColor(GREEN);
		g.fill(progressDone(progressBar, progress));
		g.setColor(RED);
		g.fill(progressLeft(progressBar, progress));
	}

	private Rectangle progressDone(Rectangle rect, float progress)
	{
		Rectangle _rect = (Rectangle) rect.clone();

		double newWidth = Math.floor(rect.getWidth() * progress);
		_rect.setSize((int) newWidth, (int) rect.getHeight());

		return _rect;
	}

	private Rectangle progressLeft(Rectangle rect, double progress)
	{
		Rectangle _rect = (Rectangle) rect.clone();

		double newWidth = Math.ceil(rect.getWidth() * (1.0 - progress));
		double xOffset = Math.floor(rect.getWidth() * progress);
		_rect.setSize((int) newWidth, (int) rect.getHeight());
		_rect.setLocation((int) (rect.getX() + xOffset), (int) rect.getY());

		return _rect;
	}

	private String generateProgressTooltip(float progress, int decimals)
	{
		return "Progress: " + String.format("%." + decimals + "f", progress * 100) + "%";
	}

	private boolean isHovered(Rectangle bounds)
	{
		return bounds.contains(client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY());
	}

	private Rectangle correctRectCoords(Rectangle rect)
	{
		Rectangle corrected = (Rectangle) rect.clone();
		corrected.setLocation((int) (corrected.getX() - 5), (int) corrected.getY() - 20);
		return corrected;
	}
}
