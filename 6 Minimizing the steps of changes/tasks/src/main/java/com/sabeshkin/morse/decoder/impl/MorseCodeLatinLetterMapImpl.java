package com.sabeshkin.morse.decoder.impl;

import com.sabeshkin.morse.decoder.api.MorseCodeLatinLetterMap;

/**
 * Класс содержащий мапку соответсвия кода Морзе букве латинского алфавита.
 */
public class MorseCodeLatinLetterMapImpl
    implements MorseCodeLatinLetterMap {

  @Override
  public int size() {
    return 28;
  }

}
