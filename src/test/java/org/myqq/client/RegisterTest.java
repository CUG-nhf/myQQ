package org.myqq.client;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterTest {

    private Register register;

    @Before
    public void setUp() {
        register = new Register("", 0);
    }

    @Test
    public void verifyPassword_NullPassword_ShouldReturnFalse() {
        assertFalse(register.verifyPassword(null));
    }

    @Test
    public void verifyPassword_EmptyPassword_ShouldReturnFalse() {
        assertFalse(register.verifyPassword(""));
    }

    @Test
    public void verifyPassword_LengthLessThan8_ShouldReturnFalse() {
        assertFalse(register.verifyPassword("1234567"));
    }

    @Test
    public void verifyPassword_LengthGreaterThan16_ShouldReturnFalse() {
        assertFalse(register.verifyPassword("12345678901234567"));
    }

    @Test
    public void verifyPassword_ContainsSpace_ShouldReturnFalse() {
        assertFalse(register.verifyPassword("1234567 "));
    }

    @Test
    public void verifyPassword_OnlyLetters_ShouldReturnFalse() {
        assertFalse(register.verifyPassword("abdseocb"));
    }

    @Test
    public void verifyPassword_OnlyNumbers_ShouldReturnFalse() {
        assertFalse(register.verifyPassword("897407534"));
    }

    @Test
    public void verifyPassword_OnlySpecialCharacters_ShouldReturnFalse() {
        assertFalse(register.verifyPassword("!@#$%^&*"));
    }

    @Test
    public void verifyPassword_LettersAndNumbers_ShouldReturnTrue() {
        assertTrue(register.verifyPassword("acde1234"));
    }

    @Test
    public void verifyPassword_LettersAndSpecialCharacters_ShouldReturnTrue() {
        assertTrue(register.verifyPassword("abcyr!@#"));
    }

    @Test
    public void verifyPassword_NumbersAndSpecialCharacters_ShouldReturnTrue() {
        assertTrue(register.verifyPassword("19923!@#"));
    }

    @Test
    public void verifyPassword_LettersNumbersAndSpecialCharacters_ShouldReturnTrue() {
        assertTrue(register.verifyPassword("abc123!@#"));
    }

    @Test
    public void verifyPassword_ContainsConsecutiveRepeatingCharacters_ShouldReturnFalse() {
        assertFalse(register.verifyPassword("11111111"));
        assertFalse(register.verifyPassword("abcdefgh"));
        assertFalse(register.verifyPassword("12abcdefgh34"));
        assertFalse(register.verifyPassword("17811111111ad"));
    }

    @Test
    public void verifyPassword_ValidPassword_ShouldReturnTrue() {
        assertTrue(register.verifyPassword("Abc123!@#"));
    }
}
