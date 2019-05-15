/**
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2018  Spotrealms Network
 *
 *    This library is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as
 *    published by the Free Software Foundation, either version 3 of the 
 *    License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Spotrealms Network
 * @website https://spotrealms.com
 * @website https://github.com/spotrealms
 */

package com.spotrealms.javautils.localization_old;

/**
 * An ENUM containing a list of the 69
 * most popular languages by their
 * ISO 639-1 code
 * 
 * @author Spotrealms
 * @website https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
 *
 */
@Deprecated
public enum ISOLang {
	AF("Afrikaans"),
	AR("Arabic"),
	AZ("Azerbaijani"),
	BE("Belarusian"),
	BG("Bulgarian"),
	BN("Bengali"),
	CA("Catalan"),
	CS("Czech"),
	CY("Welsh"),
	DA("Danish"),
	DE("German"),
	EL("Greek"),
	EN("English"),
	EO("Esperanto"),
	ES("Spanish"),
	ET("Estonian"),
	EU("Basque"),
	FA("Persian"),
	FI("Finnish"),
	FR("French"),
	GA("Irish"),
	GL("Galician"),
	HE("Hebrew"),
	HI("Hindi"),
	HR("Croatian"),
	HT("Haitian Creole"),
	HU("Hungarian"),
	HY("Armenian"),
	ID("Indonesian"),
	IS("Icelandic"),
	IT("Italian"),
	JA("Japanese"),
	JV("Javanese"),
	KA("Georgian"),
	KN("Kannada"),
	KO("Korean"),
	LA("Latin"),
	LT("Lithuanian"),
	LV("Latvian"),
	MK("Macedonian"),
	MN("Mongolian"),
	MS("Malay"),
	MT("Maltese"),
	MY("Burmese"),
	NL("Dutch"),
	NO("Norwegian"),
	PL("Polish"),
	PT("Portuguese"),
	RO("Romanian"),
	RU("Russian"),
	SE("Northern Sami"), //For the memes (lul)
	SK("Slovak"),
	SM("Samoan"),
	SL("Slovenian"),
	SQ("Albanian"),
	SR("Serbian"),
	SV("Swedish"),
	SW("Swahili"),
	TA("Tamil"),
	TE("Telugu"),
	TH("Thai"),
	TL("Filipino"),
	TR("Turkish"),
	UK("Ukrainian"),
	UR("Urdu"),
	VI("Vietnamese"),
	YI("Yiddish"),
	ZHCN("Chinese Simplified"),
	ZHTW("Chinese Traditional"),
	
	//For custom languages that aren't in the enum
	NULL("User-defined Language");

	//Utility methods
	private final String isoCode;
	
	//Constructor
	private ISOLang(String isoCode){
		//Assign the class variables from the constructor's parameters
		this.isoCode = isoCode;
	}

	/**
	 * Check if an input ISO code equals one
	 * from this enum
	 * 
	 * @param isoCodeIn - The code to check against the enum codes
	 * @return <b>boolean</b> - The status of whether or not the input {@code String} equals one of the enums
	 */
	public boolean equalsCode(String isoCodeIn){
		//Return true if the ISO code equals the input
		return this.isoCode.equals(isoCodeIn.toLowerCase());
	}
	
	/**
	 * Check if an input language equals a code's 
	 * associated language
	 * 
	 * @param isoCodeIn - The code to check against the enum codes
	 * @return <b>boolean</b> - The status of whether or not the input {@code String} equals one of the enums
	 */
	public boolean equalsLang(String langIn){
		//Return true if the passed language is equal to the name referenced by the enum
		return this.isoCode.toUpperCase().equals(langIn.toUpperCase());
	}

	/**
	 * Returns the language referenced
	 * by the given ISO code
	 * @return <b>String</b> - The full name of the ISO code
	 */
	public String getLang(){
		//Return the ISO code's value as a String
		return this.isoCode;
	}
	
	/**
	 * Returns the language referenced
	 * by the given ISO code
	 * @param codeIn - The ISO code to get the language of
	 * @return <b>String</b> - The full name of the ISO code
	 */
	public static String getLang(String codeIn){
		for(ISOLang curCode : ISOLang.values()){
			//Check if the current code equals one of the enums
			if(codeIn.toUpperCase().equalsIgnoreCase(curCode.toString())){
				//Return the ISO code's value as a String
				return curCode.getLang();
			}
		}
		//Return null because the code wasn't found
		return null;
	}
	
	/**
	 * Get the code of a language name if
	 * one exists in the enum
	 * @param langIn - The language name to check
	 * @return <b>ISOLang</b> - The ISO code of the input language
	 */
	public static ISOLang getCode(String langIn){
		//Loop through all of the available codes
		for(ISOLang curCode : ISOLang.values()){
			//Check if the current code equals the input code
			if(langIn.toUpperCase().equals(getLang(curCode.toString()).toUpperCase())){
				//Return the ISO code
				return curCode;
			}
		}
		
		//Return null because the code wasn't found
		return null;
	}
	
	/**
	 * Gets a corresponding {@code ISOLang} object
	 * if an input language code is valid
	 * @param langIn - The code to get the object for
	 * @return <b>ISOLang</b> - The corresponding {@code ISOLang} object of the input
	 */
	public static ISOLang getCodeObjFromStr(String codeIn){
		//Loop through all of the available codes
		for(ISOLang curCode : ISOLang.values()){
			//Check if the current code equals the input code
			if(codeIn.toUpperCase().equals(String.valueOf(curCode))){
				//Return the ISO code
				return curCode;
			}
		}
		
		//Return null because the code wasn't found
		return null;
	}
	
	/**
	 * Check if a specified language code
	 * exists in the enum
	 * @param langCodeIn - The language code to check
	 * @return <b>boolean</b> - The status of whether or not the specified language code is valid
	 */
	public static boolean isValidLangCode(String langCodeIn){
		//Transform the string to uppercase
		langCodeIn = langCodeIn.toUpperCase();
		
		//Loop through all of the available codes
		for(ISOLang curCode : ISOLang.values()){
			//Check if the code is in the enum
			if(curCode.name().equals(langCodeIn)){
				//Return true because the code is valid
				return true;
			}
		}
		
		//Return false because a valid code wasn't found in the enum
		return false;
	}
}