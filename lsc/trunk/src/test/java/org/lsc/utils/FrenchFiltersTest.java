/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lsc.utils;

import org.junit.Test;
import junit.framework.TestCase;

/**
 *
 * @author rschermesser
 */
public class FrenchFiltersTest extends TestCase {

	public void testRemoveBadChars() {
		String test = "Ç'ést lä lùttè fînâlÉ";
		assertEquals("C'est la lutte finalE", FrenchFilters.removeBadChars(test));	
	}

	public void testFilterPhone() {
		String phone = "01 02 03 04 05";
		assertEquals("33102030405", FrenchFilters.filterPhone(phone));

		phone = "12 3 4 56 78 9-0";
		assertEquals("33234567890", FrenchFilters.filterPhone(phone));
	}

	public void testToUpperCaseAllBeginningNames() {
		String name = "toto titi-tata.tutu_tyty";
		assertEquals("Toto Titi-Tata.tutu_Tyty", FrenchFilters.toUpperCaseAllBeginningNames(name));
	}

	public void testFilterSn() throws CharacterUnacceptedException {
		String sn = "Me MySelf And I";
		assertEquals("Me Myself And I", FrenchFilters.filterSn(sn));
	}

	@Test(expected=CharacterUnacceptedException.class)
	public void testFilterSnException() throws CharacterUnacceptedException {
		String sn = "Me MySelf And Î";
		assertNotNull(FrenchFilters.filterSn(sn));
	}

	public void testFilterUid() {
		String uid = "ABCDEFGHIJKLMNOPQ";
		assertEquals("abcdefghijklmn", FrenchFilters.filterUid(uid));
	}

	public void testFilterShortUid() {
		String uid = "ABCDEFGHIJKLMNOPQ";
		assertEquals("abcdefgh", FrenchFilters.filterShortUid(uid));
	}

	public void testFilterLengthString() {
		assertEquals("identifier", FrenchFilters.filterLengthString("identifier", 12));
		assertEquals("ident", FrenchFilters.filterLengthString("identifier", 5));
		assertEquals("ident", FrenchFilters.filterLengthString("id-entifier", 5));
	}

//	public static String filterStringRegExp(final String value, final String regexp)
//					throws CharacterUnacceptedException {

	/**
	 * Filter a string to match a last name
	 *
	 * @param name the last name to filter
	 * @return the filtered patronimic name
	 * @throws CharacterUnacceptedException thrown if an rejected character
	 * is encountered during analysis
	 */
//	public static String filterLastName(final String name)
//					throws CharacterUnacceptedException {

	/**
	 * Filter a string to match a first name
	 *
	 * @param name the first name to filter
	 * @return the filtered public given name
	 * @throws CharacterUnacceptedException thrown if an rejected character
	 * is encountered during analysis
	 */
//	public static String filterFirstName(final String name)
//					throws CharacterUnacceptedException {

	/**
	 * Filter a string to match a givenName
	 *
	 * @param oldValue the value to filter
	 * @return the filtered givenname
	 * @throws CharacterUnacceptedException thrown if an rejected character
	 * is encountered during analysis
	 */
//	public static String filterGivenName(final String oldValue)
//					throws CharacterUnacceptedException {
	
	/**
	 * Filter all alphanumeric characters.
	 * @param value the original value
	 * @return the filtered string
	 * @throws CharacterUnacceptedException thrown if an rejected character
	 * is encountered during analysis
	 */
//	public static String filterAlpha(final String value)
//					throws CharacterUnacceptedException {

	/**
	 * Generate a 8 chars long password
	 *
	 * @return Le mot de passe
	 */
//	public static String generatePwd() {

	/**
	 * Remove trailing and starting spaces
	 * and replace remaining spaces and dots by dashes
	 *
	 * @param aString the string to filter
	 *
	 * @return the filtered string
	 */
//	public static String filterName(final String aString) {
	
	/**
	 * Filters numerical identifier.
	 *
	 * @param value the string
	 * @return the normalized String
	 * @throws CharacterUnacceptedException launch if and only if the argument
	 *         is not a numerical identifier
	 */
//	public static String filterNumber(final String value)
//					throws CharacterUnacceptedException {
		

	/**
	 * Converts Date into timestamp string.
	 *
	 * @param value A string representation fo a date
	 * @param format The format of Date with representation used by
	 *        SimpleDateFormat
	 *
	 * @return String A string containing correspondant timestamp
	 *
	 * @throws CharacterUnacceptedException thrown if an rejected character
	 * is encountered during analysis
	 */
//	public static String filterDate(final String value, final String format)
//					throws CharacterUnacceptedException {

		
	
}
