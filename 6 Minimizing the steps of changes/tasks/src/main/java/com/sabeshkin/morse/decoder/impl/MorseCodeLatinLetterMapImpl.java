package com.sabeshkin.morse.decoder.impl;

import com.sabeshkin.morse.decoder.api.MorseCodeLatinLetterMap;
import java.util.HashMap;

/**
 * Класс содержащий мапку соответсвия кода Морзе букве латинского алфавита.
 */
public class MorseCodeLatinLetterMapImpl
    implements MorseCodeLatinLetterMap {

  private static HashMap<String, String> alphabet = new HashMap<>();

  /**
   * Что бы не переделывать сигнатуру всех вызовов заполнения мапки, сделал метод с перевернутой
   * сигнатурой.
   */
  private void put(Character value,
                   String key) {
    alphabet.put(key, value.toString());
  }

  public MorseCodeLatinLetterMapImpl() {
    put('A', ".-");
    put('B', "-...");
    put('C', "-.-.");
    put('D', "-..");
    put('E', ".");
    put('F', "..-.");
    put('G', "--.");
    put('H', "....");
    put('I', "..");
    put('J', ".---");
    put('K', "-.-");
    put('L', ".-..");
    put('M', "--");
    put('N', "-.");
    put('O', "---");
    put('P', ".--.");
    put('Q', "--.-");
    put('R', ".-.");
    put('S', "...");
    put('T', "-");
    put('U', "..-");
    put('V', "...-");
    put('W', ".--");
    put('X', "-..-");
    put('Y', "-.--");
    put('Z', "--..");
  }

  @Override
  public String get(String morseCode) {
    return alphabet.get(morseCode);
  }

  @Override
  public int size() {
    return alphabet.size();
  }

}
