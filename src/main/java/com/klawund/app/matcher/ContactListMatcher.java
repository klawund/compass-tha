package com.klawund.app.matcher;

import com.klawund.app.model.ContactMatch;
import com.klawund.app.provider.ContactListProvider;
import com.klawund.app.scorer.ContactMatchScorer;

import java.util.List;
import java.util.stream.Collectors;

public class ContactListMatcher
{
	private final ContactListProvider contactListProvider;
	private final ContactMatchScorer contactMatchScorer;

	public ContactListMatcher(ContactListProvider contactListProvider, ContactMatchScorer contactMatchScorer)
	{
		this.contactListProvider = contactListProvider;
		this.contactMatchScorer = contactMatchScorer;
	}

	public List<ContactMatch> getMatches()
	{
		var contacts = contactListProvider.getContacts();
		var source = contacts.get(0);

		return contacts.stream()
				.skip(1)
				.map(toMatch -> contactMatchScorer.getMatchResult(source, toMatch))
				.collect(Collectors.toList());
	}
}
