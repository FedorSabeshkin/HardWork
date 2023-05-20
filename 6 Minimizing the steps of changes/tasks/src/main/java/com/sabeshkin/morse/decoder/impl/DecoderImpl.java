package com.sabeshkin.morse.decoder.impl;

import com.sabeshkin.morse.decoder.api.Decoder;

public class DecoderImpl
    implements Decoder {

  @Override
  public String decode(String morseCode) {
    return "E";
  }

  @Override
  public String[] breakToLetterParts(String morseMessage) {
    return morseMessage.split("\\s+");
  }

  @Override
  public String[] breakToWordParts(String morseMessage) {
    return new String[1];
  }


}
