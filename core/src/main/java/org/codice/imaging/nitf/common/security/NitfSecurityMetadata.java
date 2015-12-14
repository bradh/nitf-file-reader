package org.codice.imaging.nitf.common.security;

/**
 Security metadata for a NITF file header or segment subheader.
 */
public interface NitfSecurityMetadata {
    /**
     Return the security classification.

     @return security classification
     */
    NitfSecurityClassification getSecurityClassification();

    /**
     Return the security classification system.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall contain valid values indicating the national or
     multinational security system used to classify the file. Country Codes per FIPS PUB 10-4 shall be used to
     indicate national security systems. The designator "XN" is for classified data generated by a component using
     NATO security system marking guidance. This code is outside the FIPS 10-4 document listing, and was
     selected to not duplicate that document's existing codes."
     <p>
     So system means "which country specified it". This field can be empty indicating no security classification
     system applied.

     @return security classification system
     */
    String getSecurityClassificationSystem();

    /**
     Return the security codewords.
     <p>
     "This field shall contain a valid indicator of the security compartments associated with
     the file. Values include one or more of the digraphs found table A-4. Multiple entries shall be separated by a
     single ECS space (0x20): The selection of a relevant set of codewords is application specific."
     <p>
     Note that the list in MIL-STD-2500C table A-4 includes digraphs that are no longer used. Consult current guidance.
     <p>
     This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

     @return security codewords or an empty string if no codewords apply.
     */
    String getCodewords();

    /**
     Return the security control and handling code instructions.
     <p>
     "This field shall contain valid additional security control and/or handling instructions
     (caveats) associated with the file. Values include digraphs found in table A-4. The digraph may indicate
     single or multiple caveats. The selection of a relevant caveat(s) is application specific."
     <p>
     Note that the list in MIL-STD-2500C table A-4 includes digraphs that are no longer used. Consult current guidance.
     <p>
     This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

     @return security control and handling codes, or an empty string if no codes apply.
     */
    String getControlAndHandling();

    /**
     Return the release instructions.
     <p>
     "This field shall contain a valid list of country and/or multilateral entity codes to
     which countries and/or multilateral entities the file is authorized for release. Valid items in
     the list are one or more country codes as found in FIPS PUB 10-4 separated by a single ECS space (0x20)."
     <p>
     So the release instructions are the countries that this is "REL TO".
     <p>
     This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

     @return release instructions, or an empty string if no release instructions apply.
     */
    String getReleaseInstructions();

    /**
     Return the security declassification type.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall contain a valid indicator of the type of security declassification or
     downgrading instructions which apply to the file. Valid values are DD (=declassify on a specific date), DE
     (=declassify upon occurrence of an event), GD (=downgrade to a specified level on a specific date), GE
     (=downgrade to a specified level upon occurrence of an event), O (=OADR), and X (= exempt from automatic
     declassification)."

     @return the declassification type, or an empty string if no declassification instructions apply
     */
    String getDeclassificationType();

    /**
     Return the declassification date.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall indicate the date on which a file is to be declassified if the value in
     File Declassification Type is DD."
     <p>
     An empty string means that no declassification date applies.

     @return the declassification date (format CCYYMMDD), or an empty string.
     */
    String getDeclassificationDate();

    /**
     Return the declassification exemption.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall indicate the reason the file is exempt from automatic
     declassification if the value in Declassification Type is X. Valid values are X1 to X8 and X251 to
     X259. X1 to X8 correspond to the declassification exemptions found in DOD 5200.1-R, paragraphs 4-202b(1) to (8)
     for material exempt from the 10-year rule. X251 to X259 correspond to the declassification exemptions found
     in DOD 5200.1-R, paragraphs 4-301a(1) to (9) for permanently valuable material exempt from the 25-year
     declassification system."
     <p>
     An empty string means that no declassification exemption applies.

     @return the declassification exemption, or an empty string to indicate no declassification exemption applies.
     */
    String getDeclassificationExemption();

    /**
     Return the security downgrade.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall indicate the classification level to which a file is to be downgraded if
     the values in Declassification Type are GD or GE. Valid values are S (=Secret), C (=Confidential), R (=
     Restricted)."
     <p>
     An empty string indicates that security downgrading does not apply.

     @return the downgrade classification level, or an empty string.
     */
    String getDowngrade();

    /**
     Return the downgrade date.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall indicate the date on which a file is to be downgraded if the value in
     Declassification Type is GD."
     <p>
     An empty string indicates that a security downgrading date does not apply.

     @return the downgrade date (format CCYYMMDD), an empty string if downgrading does not apply, null for NITF 2.0 files.
     */
    String getDowngradeDate();

    /**
     Return the downgrade date or special case for this file.
     <p>
     This field is only valid for NITF 2.0 files.
     <p>
     The valid values are:
     (1) the calendar date in the format YYMMDD
     (2) the code "999999" when the originating agency's determination is required (OADR)
     (3) the code "999998" when a specific event determines at what point declassification or downgrading is to take place.
     <p>
     If the third case (999998) occurs, use getDowngradeEvent() to determine the downgrade event.

     @return the downgrade date or special case flag value
     */
    String getDowngradeDateOrSpecialCase();

    /**
     Get the specific downgrade event for this file.
     <p>
     This field is only valid for NITF 2.0 files.
     <p>
     This is only valid if getDowngradeDateOrSpecialCase() is equal to 999998.

     @return the downgrade event
     */
    String getDowngradeEvent();

    /**
     Return the classification text.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall be used to provide additional information about file classification
     to include identification of a declassification or downgrading event if the values in Declassification
     Type are DE or GE. It may also be used to identify multiple classification sources and/or any other special
     handling rules. Values are user defined free text."
     <p>
     An empty string indicates that  additional information about file classification does not apply.

     @return the classification text or an empty string.
     */
    String getClassificationText();

    /**
     Return the classification authority type.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.
     <p>
     "This field shall indicate the type of authority used to classify the file. Valid values are
     O (= original classification authority),
     D (= derivative from a single source), and
     M (= derivative from multiple sources)."
     <p>
     An empty string indicates that classification authority type does not apply.

     @return classification authority type, or an empty string.
     */
    String getClassificationAuthorityType();

    /**
     Return the classification security authority.
     <p>
     "This field shall identify the classification authority for the file dependent upon
     the value in Classification Authority Type. Values are user defined free text which should contain the
     following information: original classification authority name and position or personal identifier if the value in
     Classification Authority Type is O; title of the document or security classification guide used to classify
     the file if the value in Classification Authority Type is D; and Derive-Multiple if the file classification was
     derived from multiple sources and the value of the Classification Authority Type field is M.
     In the latter case, the file originator will maintain a record of the sources used in accordance
     with existing security directives. One of the multiple sources may also be identified in Classification Text
     if desired."
     <p>
     An empty string indicates that no file classification authority applies.
     <p>
     This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

     @return classification authority or an empty string.
     */
    String getClassificationAuthority();

    /**
     Return the classification reason.
     <p>
     "This field shall contain values indicating the reason for classifying the file.
     Valid values are A to G. These correspond to the reasons for original classification per E.O. 12958,
     Section 1.5.(a) to (g)."
     <p>
     An empty string indicates that no file classification reason applies.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.

     @return the classification reason (1 character), or an empty string.
     */
    String getClassificationReason();

    /**
     Return the security source date.
     <p>
     "This field shall indicate the date of the source used to derive the classification of the
     file. In the case of multiple sources, the date of the most recent source shall be used."
     <p>
     An empty string indicates that a security source date does not apply.
     <p>
     This field is only valid for NITF 2.1 / NSIF 1.0 files.

     @return the security source date (format CCYYMMDD), or an empty string.
     */
    String getSecuritySourceDate();

    /**
     Return the security control number.
     <p>
     "This field shall contain a valid security control number associated with the file.
     The format of the security control number shall be in accordance with the regulations governing the
     appropriate security channel(s)."
     <p>
     An empty string indicates that no file security control number applies.
     <p>
     This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

     @return the security control number, or an empty string.
     */
    String getSecurityControlNumber();
}
