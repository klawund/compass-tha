package com.klawund.app.model;

public record ContactMatch(Long sourceId, Long matchId, MatchAccuracy accuracy)
{
	@Override
	public String toString()
	{
		return "[ContactID Source =" + sourceId + ", ContactID Match =" + matchId + ", Accuracy=" + accuracy + ']';
	}
}
