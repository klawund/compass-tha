package com.klawund.app.provider;

import com.klawund.app.model.Contact;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class CsvContactListProvider implements ContactListProvider
{
	private final Path csvLocation;

	public CsvContactListProvider(Path csvLocation)
	{
		if (!Files.exists(csvLocation))
		{
			throw new IllegalArgumentException("Invalid CSV contact list file");
		}

		this.csvLocation = csvLocation;
	}

	@Override
	public List<Contact> getContacts()
	{
		List<Contact> contacts = new LinkedList<>();

		try (var br = Files.newBufferedReader(csvLocation))
		{
			assertHeaderIsValid(br.readLine());

			String line;
			while ((line = br.readLine()) != null)
			{
				contacts.add(contactFromLine(line));
			}
		}
		catch (IllegalStateException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			// todo log
		}

		return contacts;
	}

	private void assertHeaderIsValid(String headerLine)
	{
		String[] columns = headerLine.split(",");

		if (columns.length != 6 || !"contactID".equals(columns[0]) || !"name".equals(columns[1]) || !"name1".equals(columns[2]) || !"email".equals(columns[3]) || !"postalZip".equals(columns[4]) || !"address".equals(columns[5]))
		{
			throw new IllegalStateException("Must use a CSV file with a valid header");
		}

	}

	private Contact contactFromLine(String csvLine)
	{
		String[] columns = csvLine.split(",");

		var id = safeLong(columns[0]);
		var firstName = columns[1];
		var lastName = columns[2];
		var email = columns[3];
		var zipCode = columns[4];

		// Lines without an address will result in an array with 5 positions
		var rawAddress = columns.length == 6 ? columns[5] : "";

		return new Contact(id, firstName, lastName, email, zipCode, rawAddress);
	}

	private long safeLong(String l)
	{
		try
		{
			return Long.parseLong(l);
		}
		catch (Exception e)
		{
			return 0L;
		}
	}
}
