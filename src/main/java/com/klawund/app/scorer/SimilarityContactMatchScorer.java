package com.klawund.app.scorer;

import com.klawund.app.model.Contact;
import com.klawund.app.model.ContactMatch;
import com.klawund.app.model.MatchAccuracy;

public class SimilarityContactMatchScorer implements ContactMatchScorer
{
	@Override
	public ContactMatch getMatchResult(Contact source, Contact toMatch)
	{
		double score = 0.0;

		// Each comparison must return a score so that the compound score can be later
		// located in a range of accuracy.

		score += containsPartsSimilarityScore(source.firstName(), toMatch.firstName());
		score += containsPartsSimilarityScore(source.lastName(), toMatch.lastName());

		score += emailSimilarityScore(source.email(), toMatch.email());

		score += baseEqualityScore(source.zipCode(), toMatch.zipCode());

		score += containsPartsSimilarityScore(source.rawAddress(), toMatch.rawAddress());

		var accuracy = scoreToAccuracy(score);

		return new ContactMatch(source.id(), toMatch.id(), accuracy);
	}

	private double containsPartsSimilarityScore(String source, String toMatch)
	{
		if (anyNullOrBlank(source, toMatch))
		{
			return 0.0;
		}

		var lowerCaseSource = source.trim().toLowerCase();
		var lowerCaseToMatch = toMatch.trim().toLowerCase();

		var base = lowerCaseSource.length() > lowerCaseToMatch.length() ? lowerCaseSource : lowerCaseToMatch;
		var working = lowerCaseSource.length() < lowerCaseToMatch.length() ? lowerCaseSource : lowerCaseToMatch;

		if (base.equals(working))
		{
			return 1.0;
		}

		if (base.contains(working))
		{
			return 0.5;
		}

		return 0.0;
	}

	private double emailSimilarityScore(String source, String toMatch)
	{
		if (anyNullOrBlank(source, toMatch))
		{
			return 0.0;
		}

		if (source.equals(toMatch))
		{
			return 1.0;
		}

		var sourceUserNameOnly = source.split("@")[0].toLowerCase();
		var toMatchUserNameOnly = toMatch.split("@")[0].toLowerCase();

		if (sourceUserNameOnly.equals(toMatchUserNameOnly))
		{
			return 0.5;
		}

		return 0.0;
	}

	private double baseEqualityScore(String source, String toMatch)
	{
		if (anyNullOrBlank(source, toMatch))
		{
			return 0.0;
		}

		var lowerCaseSource = source.toLowerCase();
		var lowerCaseToMatch = toMatch.toLowerCase();

		return lowerCaseSource.equals(lowerCaseToMatch) ? 1.0 : 0.0;
	}

	private MatchAccuracy scoreToAccuracy(double score)
	{
		// The score-to-accuracy range must relate to the Contact's model field count directly,
		// or else the compound score may always over/under range.
		return score >= 3.0 ? MatchAccuracy.HIGH : MatchAccuracy.LOW;
	}

	private boolean anyNullOrBlank(String s1, String s2)
	{
		return s1 == null || s2 == null || s1.isBlank() || s2.isBlank();
	}
}
