/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * Your testing strategies for these methods should go here.
     * Make sure you have partitions.
     */
     
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T09:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "charlie", "nothing to see here", d3);
    private static final Tweet tweet4 = new Tweet(4, "bob", "talk is cheap", d4);

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // Ensure assertions are enabled with VM argument: -ea
    }

    
    
    // Test for writtenBy method
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        assertFalse("do not expected list to contain tweet", writtenBy.contains(tweet2));
    }
 
    @Test  
    public void testWrittenByNoResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "bob");
        
        assertTrue("expected empty list", writtenBy.isEmpty());
    }
    
    @Test  
    public void testNoTweet() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "bob");
        
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByCaseInsensitivity() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "Alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    
    
    
    
    // Test for inTimespan method
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2, tweet3)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    @Test 
    public void testInTimespanNoResults() {
        Instant testStart = Instant.parse("2016-02-17T12:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T13:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanExactStartEnd() {
        Instant testStart = Instant.parse("2016-02-17T10:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweet1", inTimespan.contains(tweet1));
        assertTrue("expected list to contain tweet2", inTimespan.contains(tweet2));
    }

    
    
    
    
    
    // Test for containing method
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    @Test
    public void testContainingSingleMatch() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("Obama"));
        
        assertEquals("expected no matches", 0, containing.size());
    }

    @Test
    public void testContainingMultipleMatches() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet4), Arrays.asList("talk", "rivest"));
        
        assertEquals("expected three tweets in result", 3, containing.size());
        assertTrue("expected tweet1 in result", containing.contains(tweet1));
        assertTrue("expected tweet2 in result", containing.contains(tweet2));
        assertTrue("expected tweet4 in result", containing.contains(tweet4));
    }

    @Test
    public void testContainingNoMatches() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet3), Arrays.asList("nonexistent"));
        
        assertEquals("expected no tweets in result", 0, containing.size());
    }

    @Test
    public void testContainingCaseInsensitivity() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("RIVEST"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
        assertTrue("expected list to contain tweet2", containing.contains(tweet2));
    }
    
    @Test
    public void testContainingEmptyWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList());
        assertTrue("expected empty list when no words are provided", containing.isEmpty());
    }
}
