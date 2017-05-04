package com.prathab.api.shopping.utility;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.validator.routines.EmailValidator;

public class Validators {

  private static final PhoneNumberUtil sPhoneNumberUtil = PhoneNumberUtil.getInstance();
  private static final EmailValidator sEmailValidator = EmailValidator.getInstance();

  /**
   * @param number Input number for which international format will be returned
   * @return String - International format of the valid input mobile number null if the input mobile
   * number is not valid
   */
  public static String getInternationalPhoneNumber(String number) {
    Phonenumber.PhoneNumber phoneNumber = null;
    try {
      phoneNumber = sPhoneNumberUtil.parse(number, "IN");
    } catch (NumberParseException e) {
      return null;
    }
    if (phoneNumber != null) {
      return sPhoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }
    return null;
  }

  public static boolean isStringEmpty(String input) {
    return (input == null) || (input.contentEquals(""));
  }

  public static boolean isEmailAddressValid(String email) {
    return sEmailValidator.isValid(email);
  }
}
