/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
     
	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T09:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype @ali", d2);
    private static final Tweet tweet3 = new Tweet(3, "bob", "Hello @Charlie, how are you? Hello @alice.", d3);
    private static final Tweet tweet4 = new Tweet(4, "alice", "@bob is here!", d4);
    private static final Tweet tweet5 = new Tweet(5, "eve", "No mentions here! Contact abc@email.com", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    
    
    // cases for time span
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanMultipleTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanAllInOrder() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet4, tweet1, tweet2, tweet3));
        assertEquals("expected start", d4, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start and end to be the same", d1, timespan.getStart());
        assertEquals("expected start and end to be the same", d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanEmptyList() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList());
        assertEquals("expected start and end to be the same", timespan.getStart(), timespan.getEnd());
    }

    
    
    // cases for mentions
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersWithMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet2, tweet5));
        assertFalse("expected a set", mentionedUsers.isEmpty());
        assertTrue("expected ali to be mentioned", mentionedUsers.contains("ali"));
        assertFalse("did not expect email.com", mentionedUsers.contains("email.com"));
    }

    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet2, tweet3));
        assertTrue("expected ali to be mentioned", mentionedUsers.contains("ali"));
        assertTrue("expected charlie to be mentioned", mentionedUsers.contains("charlie"));
        assertTrue("expected alice to be mentioned", mentionedUsers.contains("alice"));
    }
    
    @Test
    public void testGetMentionedUsersCaseInsensitivity() {
        Tweet tweetWithDifferentCase = new Tweet(6, "user", "Hey @Ali, what's up?", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithDifferentCase));
        assertTrue("expected ali to be mentioned", mentionedUsers.contains("ali"));
    }

    @Test
    public void testGetMentionedUsersNoAdjacentCharacters() {
        Tweet tweetWithAdjacentChars = new Tweet(7, "user", "Check@ali123 and not @ali! @ali", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithAdjacentChars));
        assertTrue("expected ali to be mentioned", mentionedUsers.contains("ali"));
        assertFalse("ali123 should not be mentioned", mentionedUsers.contains("ali123"));
    }

    @Test
    public void testGetMentionedUsersEmptyList() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList());
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
