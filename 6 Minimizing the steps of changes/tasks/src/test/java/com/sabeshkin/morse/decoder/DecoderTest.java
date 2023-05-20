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
  void testDecode() {
    Decoder decoder = new DecoderImpl();
    assertAll(
        () ->
            assertEquals("E", decoder.decode("."))
    );
  }

}