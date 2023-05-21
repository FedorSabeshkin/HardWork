package com.sabeshkin.morse.decoder.advanced.impl;

import com.sabeshkin.morse.decoder.advanced.api.DecoderAdvanced;

public class DecoderAdvancedImpl
    implements DecoderAdvanced {

  @Override
  public String decodePhrase(String morseCode) {
    return "E";
  }

}
