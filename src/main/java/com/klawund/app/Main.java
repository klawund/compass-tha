package com.klawund.app;

import com.klawund.app.matcher.ContactListMatcher;
import com.klawund.app.provider.ContactListProvider;
import com.klawund.app.provider.CsvContactListProvider;
import com.klawund.app.scorer.ContactMatchScorer;
import com.klawund.app.scorer.SimilarityContactMatchScorer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main
{
	public static void main(String[] args)
	{
		Path csvLocation;

		if (args.length > 0)
		{
			csvLocation = Path.of(args[0]);
		}
		else
		{
			csvLocation = getDefaultCsvLocation();
		}

		ContactListProvider contactListProvider = new CsvContactListProvider(csvLocation);
		ContactMatchScorer contactMatchScorer = new SimilarityContactMatchScorer();

		var matcher = new ContactListMatcher(contactListProvider, contactMatchScorer);

		var result = matcher.getMatches();
		result.forEach(System.out::println);
	}

	private static Path getDefaultCsvLocation()
	{
		try
		{
			return Paths.get(Main.class.getClassLoader().getResource("contact_list.csv").toURI());
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Default CSV file not found");
		}
	}
}