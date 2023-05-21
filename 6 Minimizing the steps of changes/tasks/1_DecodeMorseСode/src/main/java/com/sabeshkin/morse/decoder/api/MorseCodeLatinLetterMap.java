package com.sabeshkin.morse.decoder.api;

/**
 * Мапка соответсвия кода Морзе букве латинского алфавита.
 */
public interface MorseCodeLatinLetterMap {

  /**
   * Вернуть латинский символ соответвующий коду Морзе.
   */
  String get(String morseCode);

  /**
   * Размер мапы.
   */
  int size();

}
