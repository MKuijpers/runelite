package net.runelite.client.plugins.collectionlog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfigKey
{
	CONFIG_GROUP("collection_log"),
	PROGRESS("PROGRESS"),
	OBTAINED("OBTAINED"),
	QUANTITY("QUANTITY");

	private final String key;

	@Override
	public String toString()
	{
		return getKey();
	}
}
