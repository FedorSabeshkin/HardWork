package com.sabeshkin.morse.decoder.advanced.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sabeshkin.morse.decoder.advanced.api.DecoderAdvanced;
import org.junit.jupiter.api.Test;

class DecoderAdvancedImplTest {

  @Test
  void decodePhrase() {
    DecoderAdvanced decoderAdvanced = new DecoderAdvancedImpl();
    assertAll(
        () ->
            assertEquals("E",
                         decoderAdvanced.decodePhrase("."))
    );
  }

}