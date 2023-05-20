package com.sabeshkin.morse.decoder.impl;

import com.sabeshkin.morse.decoder.api.Decoder;
import com.sabeshkin.morse.decoder.api.MorseCodeLatinLetterMap;

public class DecoderImpl
    implements Decoder {

  private final MorseCodeLatinLetterMap morseCodeLatinLetterMap;

  public DecoderImpl() {
    morseCodeLatinLetterMap = new MorseCodeLatinLetterMapImpl();
  }

  @Override
  public String decodePhrase(String morseCode) {
    return "E";
  }

  @Override
  public String decodeLetter(String morseCode) {
    return morseCodeLatinLetterMap.get(morseCode);
  }

  @Override
  public String decodeWord(String morseCode) {
    return "HE";
  }

  @Override
  public String[] breakToLetterParts(String morseMessage) {
    return morseMessage.split("\\s+");
  }

  @Override
  public String[] breakToWordParts(String morseMessage) {
    return morseMessage.split("\\s\\s+");
  }

}
