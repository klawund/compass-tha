package com.klawund.app.model;

public enum MatchAccuracy
{
	LOW("Low"),
	HIGH("High");

	private final String label;

	MatchAccuracy(String label)
	{
		this.label = label;
	}

	public String label()
	{
		return this.label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
