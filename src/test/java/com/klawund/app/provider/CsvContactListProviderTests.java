package com.klawund.app.provider;

import com.klawund.app.model.Contact;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvContactListProviderTests
{
	@Test
	void shouldRejectInvalidCsvHeader()
	{
		var csvLocation = getTestCsvLocation("unexpected_header.csv");
		var provider = new CsvContactListProvider(csvLocation);

		assertThrows(IllegalStateException.class, provider::getContacts);
	}

	@Test
	void shouldHandleUnevenLines()
	{
		var csvLocation = getTestCsvLocation("uneven_contact_list.csv");
		var provider = new CsvContactListProvider(csvLocation);

		var expected = List.of(
				new Contact(1L, "Ciara", "French", "mollis.lectus.pede@outlook.net", "39746", "449-6990 Tellus. Rd."),
				new Contact(2L, "Ciara", "F", "mollis.lectus.pede@outlook.net", "39746", "")
		);

		assertEquals(expected, provider.getContacts());
	}

	@Test
	void shouldHandleBaseCase()
	{
		var csvLocation = getTestCsvLocation("contact_list.csv");
		var provider = new CsvContactListProvider(csvLocation);

		var expected = List.of(
				new Contact(1L, "Ciara", "French", "mollis.lectus.pede@outlook.net", "39746", "449-6990 Tellus. Rd."),
				new Contact(2L, "Charles", "Pacheco", "nulla.eget@protonmail.couk", "76837", "Ap #312-8611 Lacus. Ave"),
				new Contact(3L, "Ciara", "F", "mollis.lectus.pede@outlook.net", "39746", "Ap #315-9877 Sec. Ave")
		);

		assertEquals(expected, provider.getContacts());
	}

	Path getTestCsvLocation(String identifier)
	{
		try
		{
			return Paths.get(CsvContactListProviderTests.class.getClassLoader().getResource(identifier).toURI());
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Invalid test CSV identifier");
		}
	}
}
