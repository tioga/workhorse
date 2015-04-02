package org.tiogasolutions.workhorse.agent.resources;

import org.testng.annotations.Test;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * Created by jacobp on 4/1/2015.
 */
@Test
public class JobResourceV1Test {

  public void testSplitCommand_empty() {
    String expected = JobResourceV1.splitCommand("").toString();
    assertEquals(expected, "[]");
  }

  public void testSplitCommand_oneWord() {
    String expected = JobResourceV1.splitCommand("aaa").toString();
    assertEquals(expected, asList("aaa").toString());
  }

  public void testSplitCommand_twoWords() {
    String expected = JobResourceV1.splitCommand("aaa bbb").toString();
    assertEquals(expected, asList("aaa","bbb").toString());
  }

  public void testSplitCommand_threeWords() {
    String expected = JobResourceV1.splitCommand("aaa bbb ccc").toString();
    assertEquals(expected, asList("aaa","bbb", "ccc").toString());
  }

  public void testSplitCommand_threeWordsFirstQuoted() {
    String expected = JobResourceV1.splitCommand("\"aaa\" bbb ccc").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsSecondQuoted() {
    String expected = JobResourceV1.splitCommand("aaa \"bbb\" ccc").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsLastQuoted() {
    String expected = JobResourceV1.splitCommand("aaa bbb \"ccc\"").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsFirstQuotedWithSpace() {
    String expected = JobResourceV1.splitCommand("\"aaa bbb\" ccc ddd").toString();
    assertEquals(expected, asList("aaa bbb","ccc", "ddd").toString());
  }

  public void testSplitCommand_threeWordsSecondQuotedWithSpace() {
    String expected = JobResourceV1.splitCommand("aaa \"bbb ccc\" ddd").toString();
    assertEquals(expected, asList("aaa","bbb ccc", "ddd").toString());
  }

  public void testSplitCommand_threeWordsLastQuotedWithSpace() {
    String expected = JobResourceV1.splitCommand("aaa bbb \"ccc ddd\"").toString();
    assertEquals(expected, asList("aaa","bbb","ccc ddd").toString());
  }
}