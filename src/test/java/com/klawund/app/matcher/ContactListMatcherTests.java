package com.klawund.app.matcher;

import com.klawund.app.model.Contact;
import com.klawund.app.model.ContactMatch;
import com.klawund.app.model.MatchAccuracy;
import com.klawund.app.provider.ContactListProvider;
import com.klawund.app.provider.CsvContactListProvider;
import com.klawund.app.provider.CsvContactListProviderTests;
import com.klawund.app.scorer.ContactMatchScorer;
import com.klawund.app.scorer.SimilarityContactMatchScorer;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContactListMatcherTests
{
	@Test
	void shouldOnlyReturnHighAccuracyForSimilarContacts()
	{
		ContactListProvider contactListProvider = () -> List.of(
				new Contact(1L, "C", "F", "mollis.lectus.pede@outlook.net", "", "449-6990 Tellus. Rd."),
				new Contact(2L, "C", "French", "mollis.lectus.pede@outlook.net", "39746", "449-6990 Tellus. Rd."),
				new Contact(3L, "Ciara", "F", "non.lacinia.at@zoho.ca", "39746", "")
		);

		ContactMatchScorer contactMatchScorer = new SimilarityContactMatchScorer();

		var matcher = new ContactListMatcher(contactListProvider, contactMatchScorer);

		var expectedResult = List.of(
				new ContactMatch(1L, 2L, MatchAccuracy.HIGH),
				new ContactMatch(1L, 3L, MatchAccuracy.LOW)
		);

		assertEquals(expectedResult, matcher.getMatches());
	}

	@Test
	void shouldReturnLowAccuracyForAllNonSimilarContacts()
	{
		ContactListProvider contactListProvider = () -> List.of(
				new Contact(1L, "C", "F", "mollis.lectus.pede@outlook.net", "", "449-6990 Tellus. Rd."),
				new Contact(2L, "C", "French", "mollis.abraham.pede@outlook.net", "29746", "449-6990 Tellus. Rd."),
				new Contact(3L, "Ciara", "F", "non.lacinia.at@zoho.ca", "39746", "")
		);

		ContactMatchScorer contactMatchScorer = new SimilarityContactMatchScorer();

		var matcher = new ContactListMatcher(contactListProvider, contactMatchScorer);

		var expectedResult = List.of(
				new ContactMatch(1L, 2L, MatchAccuracy.LOW),
				new ContactMatch(1L, 3L, MatchAccuracy.LOW)
		);

		assertEquals(expectedResult, matcher.getMatches());
	}

	@Test
	void shouldHandleCsvProvider()
	{
		Path csvLocation = getTestCsvLocation();

		ContactListProvider contactListProvider = new CsvContactListProvider(csvLocation);
		ContactMatchScorer contactMatchScorer = new SimilarityContactMatchScorer();

		var matcher = new ContactListMatcher(contactListProvider, contactMatchScorer);

		var expectedResult = List.of(
				new ContactMatch(1L, 2L, MatchAccuracy.LOW),
				new ContactMatch(1L, 3L, MatchAccuracy.HIGH)
		);

		assertEquals(expectedResult, matcher.getMatches());
	}

	Path getTestCsvLocation()
	{
		try
		{
			return Paths.get(CsvContactListProviderTests.class.getClassLoader().getResource("contact_list.csv").toURI());
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Invalid test CSV identifier");
		}
	}
}
