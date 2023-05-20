package com.sabeshkin.morse.decoder.api;

/**
 * Класс для декодирования кода Морзе в латиницу.
 */
public interface Decoder {

  /**
   * Декодирует код Морзе в латиницу.
   *
   * @param morseCode строка в коде Морзе, например "___".
   * @return строка из латиницы.
   */
  String decodePhrase(String morseCode);

  /**
   * Декодирует код буквы Морзе в букву латиницы.
   */
  String decodeLetter(String morseCode);

  /**
   * Разбивает принятое сообщение по пробелу на массив букв.
   */
  String[] breakToLetterParts(String morseMessage);

  /**
   * Разбивает принятое сообщение по пробелу на массив слов.
   */
  String[] breakToWordParts(String morseMessage);

}
