package com.sabeshkin.morse.decoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sabeshkin.morse.decoder.api.MorseCodeLatinLetterMap;
import com.sabeshkin.morse.decoder.impl.MorseCodeLatinLetterMapImpl;
import org.junit.jupiter.api.Test;

/**
 * Тесты на класс содержащий мапку соответсвия кода Морзе букве латинского алфавита.
 */
class MorseCodeLatinLetterMapTest {

  @Test
  void testSize() {
    MorseCodeLatinLetterMap morseCodeLatinLetterMap = new MorseCodeLatinLetterMapImpl();
    assertEquals(28, morseCodeLatinLetterMap.size());
  }

  @Test
  void testGet() {
    MorseCodeLatinLetterMap morseCodeLatinLetterMap = new MorseCodeLatinLetterMapImpl();
    assertEquals("E", morseCodeLatinLetterMap.get("."));
  }

}