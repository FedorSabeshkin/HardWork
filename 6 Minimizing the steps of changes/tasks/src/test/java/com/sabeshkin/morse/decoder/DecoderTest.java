package com.sabeshkin.morse.decoder;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты на декодер из кода Морзе в латиницу.
 */
class DecoderTest {

  @Test
  void testDecode() {
    assertEquals("E", Decoder.decode("."));
  }

}