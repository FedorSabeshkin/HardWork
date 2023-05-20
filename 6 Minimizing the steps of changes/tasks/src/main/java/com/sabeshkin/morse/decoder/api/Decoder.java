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
  String decode(String morseCode);

}
