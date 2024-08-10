package com.klawund.app.scorer;

import com.klawund.app.model.Contact;
import com.klawund.app.model.MatchAccuracy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContactMatchScorerTests
{
	@Test
	void whenMostFieldsAreEqualOrSimilarShouldHaveHighAccuracy()
	{
		var contactMatchScorer = new SimilarityContactMatchScorer();

		var source = new Contact(1L, "C", "F", "mollis.lectus.pede@outlook.net", "", "449-6990 Tellus. Rd.");
		var toMatch = new Contact(2L, "C", "French", "mollis.lectus.pede@outlook.net", "39746", "449-6990 Tellus. Rd.");

		var result = contactMatchScorer.getMatchResult(source, toMatch);

		assertEquals(MatchAccuracy.HIGH, result.accuracy());
	}

	@Test
	void whenMostFieldsAreDifferentShouldHaveLowAccuracy()
	{
		var contactMatchScorer = new SimilarityContactMatchScorer();

		var source = new Contact(1L, "C", "F", "mollis.lectus.pede@outlook.net", "", "449-6990 Tellus. Rd.");
		var toMatch = new Contact(2L, "Ciara", "F", "non.lacinia.at@zoho.ca", "39746", "");

		var result = contactMatchScorer.getMatchResult(source, toMatch);

		assertEquals(MatchAccuracy.LOW, result.accuracy());
	}

	@Test
	void whenMostFieldsAreNullShouldHaveLowAccuracy()
	{
		var contactMatchScorer = new SimilarityContactMatchScorer();

		var source = new Contact(3L, "C", "F", "", "", "");
		var toMatch = new Contact(4L, "C", "F", "", "", "449-6990 Tellus. Rd.");

		var result = contactMatchScorer.getMatchResult(source, toMatch);

		assertEquals(MatchAccuracy.LOW, result.accuracy());
	}
}
