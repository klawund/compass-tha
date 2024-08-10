package com.klawund.app.scorer;

import com.klawund.app.model.Contact;
import com.klawund.app.model.ContactMatch;

public interface ContactMatchScorer
{
	ContactMatch getMatchResult(Contact source, Contact toMatch);
}
