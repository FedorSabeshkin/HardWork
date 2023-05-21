package com.sabeshkin.morse.decoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sabeshkin.morse.decoder.api.Decoder;
import com.sabeshkin.morse.decoder.impl.DecoderImpl;
import org.junit.jupiter.api.Test;

/**
 * Тесты на декодер из кода Морзе в латиницу.
 */
class DecoderTest {

  @Test
  void testDecodePhrase() {
    Decoder decoder = new DecoderImpl();
    assertAll(
        () ->
            assertEquals("E",
                         decoder.decodePhrase("."))
    );
  }

  @Test
  void testDecodeWord() {
    Decoder decoder = new DecoderImpl();
    assertAll(
        () ->
            assertEquals("HE",
                         decoder.decodeWord(".... . ")),
        () ->
            assertEquals("JUDE",
                         decoder.decodeWord(".--- ..- -.. ."))
    );
  }

  @Test
  void testDecodeLetter() {
    Decoder decoder = new DecoderImpl();
    assertAll(
        () ->
            assertEquals("H",
                         decoder.decodeLetter("....")),
        () ->
            assertEquals("E",
                         decoder.decodeLetter("."))
    );
  }


  @Test
  void testBreakToLetterParts() {
    Decoder decoder = new DecoderImpl();
    assertAll(
        () ->
            assertEquals(1,
                         decoder.breakToLetterParts(".").length),
        () ->
            assertEquals(2,
                         decoder.breakToLetterParts(". _").length)
    );
  }

  @Test
  void testBreakToWordParts() {
    Decoder decoder = new DecoderImpl();
    assertAll(
        () ->
            assertEquals(1,
                         decoder.breakToWordParts(".").length),
        () ->
            assertEquals(1,
                         decoder.breakToWordParts(". _").length),
        () ->
            assertEquals(2,
                         decoder.breakToWordParts(".  _").length)
    );
  }

}