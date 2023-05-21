package com.sabeshkin.morse.decoder.impl;

import com.sabeshkin.morse.decoder.api.Decoder;
import com.sabeshkin.morse.decoder.api.MorseCodeLatinLetterMap;
import java.util.Arrays;

public class DecoderImpl
    implements Decoder {

  private final MorseCodeLatinLetterMap morseCodeLatinLetterMap;

  private final static String SPACE = " ";

  public DecoderImpl() {
    morseCodeLatinLetterMap = new MorseCodeLatinLetterMapImpl();
  }

  @Override
  public String decodePhrase(String morseMessage) {
    StringBuilder sbLatinPhrase = new StringBuilder();
    String[] morseWords = breakToWordParts(morseMessage);
    Arrays.stream(morseWords)
          .forEach(morseWord -> {
                     String latinWord = decodeWord(morseWord);
                     sbLatinPhrase.append(latinWord)
                                  .append(SPACE);
                   }
          );
    String latinPhrase = sbLatinPhrase.toString();
    return latinPhrase.strip();
  }

  @Override
  public String decodeLetter(String morseCode) {
    return morseCodeLatinLetterMap.get(morseCode);
  }

  @Override
  public String decodeWord(String morseWord) {
    StringBuilder sbLatinWord = new StringBuilder();
    String[] morseLetters = breakToLetterParts(morseWord);
    Arrays.stream(morseLetters)
          .forEach(morseLetter -> {
                     String latinLetter = decodeLetter(morseLetter);
                     sbLatinWord.append(latinLetter);
                   }
          );
    return sbLatinWord.toString();
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
