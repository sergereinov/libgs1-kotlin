package com.github.sergereinov.libgs1

data class AI(
    val ai: String,
    val descContent: String,
    val descFormat: String,
    val fnc1Required: Boolean,
    val dataTitle: String,
    val fieldLen: Int,
    val maxFieldLenOptional: Int,
    val dataFormat: Int
) {

    fun compareToSequence(chars: CharSequence): Int {
        return if (ai.endsWith('n')) {
            ai.dropLast(1).compareTo(chars.take(ai.length - 1).toString())
        } else {
            ai.compareTo(chars.take(ai.length).toString())
        }
    }


    companion object {

        const val CONTROL_GS = '\u001D'

        const val GS1_STRING: Int = 0
        const val GS1_DATE: Int = 1
        const val GS1_DECIMAL: Int = 2


        fun from(gs1Stream: CharSequence): AI? {

            val index = all.binarySearch {
                it.compareToSequence(gs1Stream)
            }

            return if (index >= 0) all[index] else null
        }



        /*
            Useful refs.

            Meat and Poultry traceability overview
            https://www.gs1.org/docs/traceability/GS1_Global_Meat_and_Poultry_Guideline_Companion_GS1_Made_Easy.pdf

            General specification (GS1 AI - Application Identifiers)
            https://www.gs1.org/docs/barcodes/GS1_General_Specifications.pdf

            DataMatrix guideline
            https://www.gs1.org/docs/barcodes/GS1_DataMatrix_Guideline.pdf

            Databar family
            https://www.gs1.org/standards/barcodes/databar
            https://www.gs1.org/docs/barcodes/databar/GS1_DataBar_Business_Case_Complete.pdf
            https://www.gs1.org/docs/barcodes/GS1_Barcodes_Fact_Sheet-GS1_DataBar_family.pdf
         */

        val all: List<AI> = listOf(
            AI("00", "Serial Shipping Container Code (SSCC)", "N2+N18", false, "SSCC", 2 + 18, 0, GS1_STRING),

            AI("01", "Global Trade Item Number (GTIN)", "N2+N14", false, "GTIN", 2 + 14, 0, GS1_STRING),
            AI("02", "GTIN of contained trade items", "N2+N14", false, "CONTENT", 2 + 14, 0, GS1_STRING),

            AI("10", "Batch or lot number", "N2+X..20", true, "BATCH/LOT", 2 + 1, 2 + 20, GS1_STRING),
            AI("11", "Production date (YYMMDD)", "N2+N6", false, "PROD DATE", 2 + 6, 0, GS1_DATE),
            AI("12", "Due date (YYMMDD)", "N2+N6", false, "DUE DATE", 2 + 6, 0, GS1_DATE),
            AI("13", "Packaging date (YYMMDD)", "N2+N6", false, "PACK DATE", 2 + 6, 0, GS1_DATE),
            AI("15", "Best before date (YYMMDD)", "N2+N6", false, "BEST BEFORE/BEST BY", 2 + 6, 0, GS1_DATE),
            AI("16", "Sell by date (YYMMDD)", "N2+N6", false, "SELL BY", 2 + 6, 0, GS1_DATE),
            AI("17", "Expiration date (YYMMDD)", "N2+N6", false, "USE BY/EXPIRY", 2 + 6, 0, GS1_DATE),

            AI("20", "Internal product variant", "N2+N2", false, "VARIANT", 2 + 2, 0, GS1_STRING),
            AI("21", "Serial number", "N2+X..20", true, "SERIAL", 2 + 1, 2 + 20, GS1_STRING),
            AI("22", "Consumer product variant", "N2+X..20", true, "CPV", 2 + 1, 2 + 20, GS1_STRING),

            AI("240", "Additional item identification", "N3+X..30", true, "ADDITIONAL ID", 3 + 1, 3 + 30, GS1_STRING),
            AI("241", "Customer part number", "N3+X..30", true, "CUST. PART NO.", 3 + 1, 3 + 30, GS1_STRING),
            AI("242", "Made-to-Order variation number", "N3+N..6", true, "MTO VARIANT", 3 + 1, 3 + 6, GS1_STRING),
            AI("243", "Packaging component number", "N3+X..20", true, "PCN", 3 + 1, 3 + 20, GS1_STRING),
            AI("250", "Secondary serial number", "N3+X..30", true, "SECONDARY SERIAL", 3 + 1, 3 + 30, GS1_STRING),
            AI("251", "Reference to source entity", "N3+X..30", true, "REF. TO SOURCE", 3 + 1, 3 + 30, GS1_STRING),
            AI(
                "253",
                "Global Document Type Identifier (GDTI)",
                "N3+N13+X..17",
                true,
                "GDTI",
                3 + 13 + 1,
                3 + 13 + 17,
                GS1_STRING
            ),
            AI(
                "254",
                "GLN extension component",
                "N3+X..20",
                true,
                "GLN EXTENSION COMPONENT",
                3 + 1,
                3 + 20,
                GS1_STRING
            ),
            AI("255", "Global Coupon Number (GCN)", "N3+N13+N..12", true, "GCN", 3 + 13 + 1, 3 + 13 + 12, GS1_STRING),

            AI(
                "30",
                "Variable count of items (variable measure trade item)",
                "N2+N..8",
                true,
                "VAR. COUNT",
                2 + 1,
                2 + 8,
                GS1_STRING
            ),

            //310n Net weight, kilograms (variable measure trade item) N4+N6  (no)  NET WEIGHT (kg)
            //AI("3100", "Net weight, kilograms",	"N4+N6", false, "NET WEIGHT (kg)", 4+6, 0, GS1_STRING), // 000000. kg
            //AI("3101", "Net weight, kilograms",	"N4+N6", false, "NET WEIGHT (kg)", 4+6, 0, GS1_STRING), // 00000.0 kg
            //AI("3102", "Net weight, kilograms",	"N4+N6", false, "NET WEIGHT (kg)", 4+6, 0, GS1_STRING), // 0000.00 kg
            //AI("3103", "Net weight, kilograms",	"N4+N6", false, "NET WEIGHT (kg)", 4+6, 0, GS1_STRING), // 000.000 kg
            //AI("3104", "Net weight, kilograms",	"N4+N6", false, "NET WEIGHT (kg)", 4+6, 0, GS1_STRING), // 00.0000 kg
            //AI("3105", "Net weight, kilograms",	"N4+N6", false, "NET WEIGHT (kg)", 4+6, 0, GS1_STRING), // 0.00000 kg
            //AI("3106", "Net weight, kilograms",	"N4+N6", false, "NET WEIGHT (kg)", 4+6, 0, GS1_STRING), // .000000 kg
            AI("310n", "Net weight, kilograms", "N4+N6", false, "NET WEIGHT (kg)", 4 + 6, 0, GS1_DECIMAL),

            //311n	Length or first dimension, metres (variable measure trade item)	N4+N6  (no)  LENGTH (m)
            AI("311n", "Length or first dimension, metres", "N4+N6", false, "LENGTH (m)", 4 + 6, 0, GS1_DECIMAL),

            //312n Width, diameter, or second dimension, metres (variable measure trade item)  N4+N6  (no)   WIDTH (m)
            AI(
                "312n",
                "Width, diameter, or second dimension, metres",
                "N4+N6",
                false,
                "WIDTH (m)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //313n	Depth, thickness, height, or third dimension, metres (variable measure trade item)	N4+N6  (no)  HEIGHT (m)
            AI(
                "313n",
                "Depth, thickness, height, or third dimension, metres",
                "N4+N6",
                false,
                "HEIGHT (m)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //314n	Area, square metres (variable measure trade item)	N4+N6    AREA (m2)
            AI("314n", "Area, square metres", "N4+N6", false, "AREA (m2)", 4 + 6, 0, GS1_DECIMAL),

            //315n	Net volume, litres (variable measure trade item)  N4+N6    NET VOLUME (l)
            AI("315n", "Net volume, litres", "N4+N6", false, "NET VOLUME (l)", 4 + 6, 0, GS1_DECIMAL),

            //316n	Net volume, cubic metres (variable measure trade item)	N4+N6    NET VOLUME (m3)
            AI("316n", "Net volume, cubic metres", "N4+N6", false, "NET VOLUME (m3)", 4 + 6, 0, GS1_DECIMAL),

            //320n	Net weight, pounds (variable measure trade item)  N4+N6		NET WEIGHT (lb)
            AI("320n", "Net weight, pounds", "N4+N6", false, "NET WEIGHT (lb)", 4 + 6, 0, GS1_DECIMAL),

            //321n	Length or first dimension, inches (variable measure trade item)	N4+N6 LENGTH (i)
            AI("321n", "Length or first dimension, inches", "N4+N6", false, "LENGTH (i)", 4 + 6, 0, GS1_DECIMAL),

            //322n	Length or first dimension, feet (variable measure trade item)	N4+N6	LENGTH (f)
            AI("322n", "Length or first dimension, feet", "N4+N6", false, "LENGTH (f)", 4 + 6, 0, GS1_DECIMAL),

            //323n	Length or first dimension, yards (variable measure trade item)	N4+N6 LENGTH (y)
            AI("323n", "Length or first dimension, yards", "N4+N6", false, "LENGTH (y)", 4 + 6, 0, GS1_DECIMAL),

            //324n	Width, diameter, or second dimension, inches (variable measure trade item)	N4+N6 WIDTH (i)
            AI(
                "324n",
                "Width, diameter, or second dimension, inches",
                "N4+N6",
                false,
                "WIDTH (i)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //325n	Width, diameter, or second dimension, feet (variable measure trade item)	N4+N6	WIDTH (f)
            AI(
                "325n",
                "Width, diameter, or second dimension, feet",
                "N4+N6",
                false,
                "WIDTH (f)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //326n	Width, diameter, or second dimension, yards (variable measure trade item)	N4+N6 WIDTH (y)
            AI(
                "326n",
                "Width, diameter, or second dimension, yards",
                "N4+N6",
                false,
                "WIDTH (y)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //327n	Depth, thickness, height, or third dimension, inches (variable measure trade item)	N4+N6 HEIGHT (i)
            AI(
                "327n",
                "Depth, thickness, height, or third dimension, inches",
                "N4+N6",
                false,
                "HEIGHT (i)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //328n	Depth, thickness, height, or third dimension, feet (variable measure trade item)	N4+N6 HEIGHT (f)
            AI(
                "328n",
                "Depth, thickness, height, or third dimension, feet",
                "N4+N6",
                false,
                "HEIGHT (f)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //329n	Depth, thickness, height, or third dimension, yards (variable measure trade item)	N4+N6	HEIGHT (y)
            AI(
                "329n",
                "Depth, thickness, height, or third dimension, yards",
                "N4+N6",
                false,
                "HEIGHT (y)",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //330n	Logistic weight, kilograms  N4+N6    GROSS WEIGHT (kg)
            AI("330n", "Logistic weight, kilograms", "N4+N6", false, "GROSS WEIGHT (kg)", 4 + 6, 0, GS1_DECIMAL),

            //331n	Length or first dimension, metres  N4+N6    LENGTH (m), log
            AI("331n", "Length or first dimension, metres", "N4+N6", false, "LENGTH (m), log", 4 + 6, 0, GS1_DECIMAL),

            //332n	Width, diameter, or second dimension, metres  N4+N6    WIDTH (m), log
            AI(
                "332n",
                "Width, diameter, or second dimension, metres",
                "N4+N6",
                false,
                "WIDTH (m), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //333n	Depth, thickness, height, or third dimension, metres	N4+N6    HEIGHT (m), log
            AI(
                "333n",
                "Depth, thickness, height, or third dimension, metres",
                "N4+N6",
                false,
                "HEIGHT (m), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //334n	Area, square metres  N4+N6    AREA (m2), log
            AI("334n", "Area, square metres", "N4+N6", false, "AREA (m2), log", 4 + 6, 0, GS1_DECIMAL),

            //335n	Logistic volume, litres  N4+N6    VOLUME (l), log
            AI("335n", "Logistic volume, litres", "N4+N6", false, "VOLUME (l), log", 4 + 6, 0, GS1_DECIMAL),

            //336n	Logistic volume, cubic metres  N4+N6    VOLUME (m3), log
            AI("336n", "Logistic volume, cubic metres", "N4+N6", false, "VOLUME (m3), log", 4 + 6, 0, GS1_DECIMAL),

            //337n	Kilograms per square metre  N4+N6    KG PER m2
            AI("337n", "Kilograms per square metre", "N4+N6", false, "KG PER m2", 4 + 6, 0, GS1_DECIMAL),

            //340n	Logistic weight, pounds  N4+N6    GROSS WEIGHT (lb)
            AI("340n", "Logistic weight, pounds", "N4+N6", false, "GROSS WEIGHT (lb)", 4 + 6, 0, GS1_DECIMAL),

            //341n	Length or first dimension, inches  N4+N6    LENGTH (i), log
            AI("341n", "Length or first dimension, inches", "N4+N6", false, "LENGTH (i), log", 4 + 6, 0, GS1_DECIMAL),

            //342n	Length or first dimension, feet  N4+N6    LENGTH (f), log
            AI("342n", "Length or first dimension, feet", "N4+N6", false, "LENGTH (f), log", 4 + 6, 0, GS1_DECIMAL),

            //343n	Length or first dimension, yards  N4+N6    LENGTH (y), log
            AI("343n", "Length or first dimension, yards", "N4+N6", false, "LENGTH (y), log", 4 + 6, 0, GS1_DECIMAL),

            //344n	Width, diameter, or second dimension, inches  N4+N6    WIDTH (i), log
            AI(
                "344n",
                "Width, diameter, or second dimension, inches",
                "N4+N6",
                false,
                "WIDTH (i), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //345n	Width, diameter, or second dimension, feet  N4+N6    WIDTH (f), log
            AI(
                "345n",
                "Width, diameter, or second dimension, feet",
                "N4+N6",
                false,
                "WIDTH (f), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //346n	Width, diameter, or second dimension, yard  N4+N6    WIDTH (y), log
            AI(
                "346n",
                "Width, diameter, or second dimension, yard",
                "N4+N6",
                false,
                "WIDTH (y), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //347n	Depth, thickness, height, or third dimension, inches	N4+N6    HEIGHT (i), log
            AI(
                "347n",
                "Depth, thickness, height, or third dimension, inches",
                "N4+N6",
                false,
                "HEIGHT (i), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //348n	Depth, thickness, height, or third dimension, feet  N4+N6    HEIGHT (f), log
            AI(
                "348n",
                "Depth, thickness, height, or third dimension, feet",
                "N4+N6",
                false,
                "HEIGHT (f), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //349n	Depth, thickness, height, or third dimension, yards		N4+N6    HEIGHT (y), log
            AI(
                "349n",
                "Depth, thickness, height, or third dimension, yards",
                "N4+N6",
                false,
                "HEIGHT (y), log",
                4 + 6,
                0,
                GS1_DECIMAL
            ),

            //350n	Area, square inches (variable measure trade item)	N4+N6    AREA (i2)
            AI("350n", "Area, square inches", "N4+N6", false, "AREA (i2)", 4 + 6, 0, GS1_DECIMAL),

            //351n	Area, square feet (variable measure trade item)  N4+N6    AREA (f2)
            AI("351n", "Area, square feet", "N4+N6", false, "AREA (f2)", 4 + 6, 0, GS1_DECIMAL),

            //352n	Area, square yards (variable measure trade item)  N4+N6    AREA (y2)
            AI("352n", "Area, square yards", "N4+N6", false, "AREA (y2)", 4 + 6, 0, GS1_DECIMAL),

            //353n	Area, square inches  N4+N6 AREA (i2), log
            AI("353n", "Area, square inches", "N4+N6", false, "AREA (i2), log", 4 + 6, 0, GS1_DECIMAL),

            //354n	Area, square feet  N4+N6    AREA (f2), log
            AI("354n", "Area, square feet", "N4+N6", false, "AREA (f2), log", 4 + 6, 0, GS1_DECIMAL),

            //355n	Area, square yards  N4+N6    AREA (y2), log
            AI("355n", "Area, square yards", "N4+N6", false, "AREA (y2), log", 4 + 6, 0, GS1_DECIMAL),

            //356n	Net weight, troy ounces (variable measure trade item)	N4+N6    NET WEIGHT (t)
            AI("356n", "Net weight, troy ounces", "N4+N6", false, "NET WEIGHT (t)", 4 + 6, 0, GS1_DECIMAL),

            //357n	Net weight (or volume), ounces (variable measure trade item)	N4+N6    NET VOLUME (oz)
            AI("357n", "Net weight (or volume), ounces", "N4+N6", false, "NET VOLUME (oz)", 4 + 6, 0, GS1_DECIMAL),

            //360n	Net volume, quarts (variable measure trade item)  N4+N6    NET VOLUME (q)
            AI("360n", "Net volume, quarts", "N4+N6", false, "NET VOLUME (q)", 4 + 6, 0, GS1_DECIMAL),

            //361n	Net volume, gallons U.S. (variable measure trade item)	N4+N6    NET VOLUME (g)
            AI("361n", "Net volume, gallons U.S.", "N4+N6", false, "NET VOLUME (g)", 4 + 6, 0, GS1_DECIMAL),

            //362n	Logistic volume, quarts  N4+N6    VOLUME (q), log
            AI("362n", "Logistic volume, quarts", "N4+N6", false, "VOLUME (q), log", 4 + 6, 0, GS1_DECIMAL),

            //363n	Logistic volume, gallons U.S.  N4+N6    VOLUME (g), log
            AI("363n", "Logistic volume, gallons U.S.", "N4+N6", false, "VOLUME (g), log", 4 + 6, 0, GS1_DECIMAL),

            //364n	Net volume, cubic inches (variable measure trade item)	N4+N6    VOLUME (i3)
            AI("364n", "Net volume, cubic inches", "N4+N6", false, "VOLUME (i3)", 4 + 6, 0, GS1_DECIMAL),

            //365n	Net volume, cubic feet (variable measure trade item)	N4+N6    VOLUME (f3)
            AI("365n", "Net volume, cubic feet", "N4+N6", false, "VOLUME (f3)", 4 + 6, 0, GS1_DECIMAL),

            //366n	Net volume, cubic yards (variable measure trade item)	N4+N6    VOLUME (y3)
            AI("366n", "Net volume, cubic yards", "N4+N6", false, "VOLUME (y3)", 4 + 6, 0, GS1_DECIMAL),

            //367n	Logistic volume, cubic inches  N4+N6    VOLUME (i3), log
            AI("367n", "Logistic volume, cubic inches", "N4+N6", false, "VOLUME (i3), log", 4 + 6, 0, GS1_DECIMAL),

            //368n	Logistic volume, cubic feet  N4+N6    VOLUME (f3), log
            AI("368n", "Logistic volume, cubic feet", "N4+N6", false, "VOLUME (f3), log", 4 + 6, 0, GS1_DECIMAL),

            //369n	Logistic volume, cubic yards  N4+N6    VOLUME (y3), log
            AI("369n", "Logistic volume, cubic yards", "N4+N6", false, "VOLUME (y3), log", 4 + 6, 0, GS1_DECIMAL),

            AI("37", "Count of trade items", "N2+N..8", true, "COUNT", 2 + 1, 2 + 8, GS1_STRING),

            //390n	Applicable amount payable or Coupon value, local currency	N4+N..15	(FNC1)  AMOUNT
            AI(
                "390n",
                "Applicable amount payable or Coupon value, local currency",
                "N4+N..15",
                true,
                "AMOUNT",
                4 + 1,
                4 + 15,
                GS1_DECIMAL
            ),

            //391n	Applicable amount payable with ISO currency code	N4+N3+N..15	(FNC1)  AMOUNT
            AI(
                "391n",
                "Applicable amount payable with ISO currency code",
                "N4+N3+N..15",
                true,
                "AMOUNT",
                4 + 3 + 1,
                4 + 3 + 15,
                GS1_DECIMAL
            ),

            //392n	Applicable amount payable, single monetary area (variable measure trade item)	N4+N..15	(FNC1)  PRICE
            AI(
                "392n",
                "Applicable amount payable, single monetary area",
                "N4+N..15",
                true,
                "PRICE",
                4 + 1,
                4 + 15,
                GS1_DECIMAL
            ),

            //393n	Applicable amount payable with ISO currency code (variable measure trade item)	N4+N3+N..15	(FNC1)  PRICE
            AI(
                "393n",
                "Applicable amount payable with ISO currency code",
                "N4+N3+N..15",
                true,
                "PRICE",
                4 + 3 + 1,
                4 + 3 + 15,
                GS1_DECIMAL
            ),

            //394n	Percentage discount of a coupon  N4+N4  (FNC1)  PRCNT OFF
            AI("394n", "Percentage discount of a coupon", "N4+N4", true, "PRCNT OFF", 4 + 4, 0, GS1_DECIMAL),

            AI("400", "Customer's purchase order number", "N3+X..30", true, "ORDER NUMBER", 3 + 1, 3 + 30, GS1_STRING),
            AI(
                "401",
                "Global Identification Number for Consignment (GINC)",
                "N3+X..30",
                true,
                "GINC",
                3 + 1,
                3 + 30,
                GS1_STRING
            ),
            AI("402", "Global Shipment Identification Number (GSIN)", "N3+N17", true, "GSIN", 3 + 17, 0, GS1_STRING),
            AI("403", "Routing code", "N3+X..30", true, "ROUTE", 3 + 1, 3 + 30, GS1_STRING),

            AI(
                "410",
                "Ship to - Deliver to Global Location Number",
                "N3+N13",
                false,
                "SHIP TO LOC",
                3 + 13,
                0,
                GS1_STRING
            ),
            AI("411", "Bill to - Invoice to Global Location Number", "N3+N13", false, "BILL TO", 3 + 13, 0, GS1_STRING),
            AI("412", "Purchased from Global Location Number", "N3+N13", false, "PURCHASE FROM", 3 + 13, 0, GS1_STRING),
            AI(
                "413",
                "Ship for - Deliver for - Forward to Global Location Number",
                "N3+N13",
                false,
                "SHIP FOR LOC",
                3 + 13,
                0,
                GS1_STRING
            ),
            AI(
                "414",
                "Identification of a physical location - Global Location Number",
                "N3+N13",
                false,
                "LOC No",
                3 + 13,
                0,
                GS1_STRING
            ),
            AI(
                "415",
                "Global Location Number of the invoicing party",
                "N3+N13",
                false,
                "PAY TO",
                3 + 13,
                0,
                GS1_STRING
            ),
            AI(
                "416",
                "GLN of the production or service location",
                "N3+N13",
                false,
                "PROD/SERV LOC",
                3 + 13,
                0,
                GS1_STRING
            ),


            AI(
                "420",
                "Ship to - Deliver to postal code within a single postal authority",
                "N3+X..20",
                true,
                "SHIP TO POST",
                3 + 1,
                3 + 20,
                GS1_STRING
            ),
            AI(
                "421",
                "Ship to - Deliver to postal code with ISO country code",
                "N3+N3+X..9",
                true,
                "SHIP TO POST",
                3 + 3 + 1,
                3 + 3 + 9,
                GS1_STRING
            ),
            AI("422", "Country of origin of a trade item", "N3+N3", true, "ORIGIN", 3 + 3, 0, GS1_STRING),
            AI(
                "423",
                "Country of initial processing",
                "N3+N3+N..12",
                true,
                "COUNTRY - INITIAL PROCESS",
                3 + 3 + 1,
                3 + 3 + 12,
                GS1_STRING
            ),
            AI("424", "Country of processing", "N3+N3", true, "COUNTRY - PROCESS", 3 + 3, 0, GS1_STRING),
            AI(
                "425",
                "Country of disassembly",
                "N3+N3+N..12",
                true,
                "COUNTRY - DISASSEMBLY",
                3 + 3 + 1,
                3 + 3 + 12,
                GS1_STRING
            ),
            AI(
                "426",
                "Country covering full process chain",
                "N3+N3",
                true,
                "COUNTRY – FULL PROCESS",
                3 + 3,
                0,
                GS1_STRING
            ),
            AI("427", "Country subdivision Of origin", "N3+X..3", true, "ORIGIN SUBDIVISION", 3 + 1, 3 + 3, GS1_STRING),

            AI("7001", "NATO Stock Number (NSN)", "N4+N13", true, "NSN", 4 + 13, 0, GS1_STRING),
            AI(
                "7002",
                "UN/ECE meat carcasses and cuts classification",
                "N4+X..30",
                true,
                "MEAT CUT",
                4 + 1,
                4 + 30,
                GS1_STRING
            ),
            AI("7003", "Expiration date and time", "N4+N10", true, "EXPIRY TIME", 4 + 10, 0, GS1_STRING),
            AI("7004", "Active potency", "N4+N..4", true, "ACTIVE POTENCY", 4 + 1, 4 + 4, GS1_STRING),
            AI("7005", "Catch area", "N4+X..12", true, "CATCH AREA", 4 + 1, 4 + 12, GS1_STRING),
            AI("7006", "First freeze date", "N4+N6", true, "FIRST FREEZE DATE", 4 + 6, 0, GS1_STRING),
            AI("7007", "Harvest date", "N4+N6..12", true, "HARVEST DATE", 4 + 6, 4 + 12, GS1_STRING),
            AI("7008", "Species for fishery purposes", "N4+X..3", true, "AQUATIC SPECIES", 4 + 1, 4 + 3, GS1_STRING),
            AI("7009", "Fishing gear type", "N4+X..10", true, "FISHING GEAR TYPE", 4 + 1, 4 + 10, GS1_STRING),
            AI("7010", "Production method", "N4+X..2", true, "PROD METHOD", 4 + 1, 4 + 2, GS1_STRING),
            AI("7020", "Refurbishment lot ID", "N4+X..20", true, "REFURB LOT", 4 + 1, 4 + 20, GS1_STRING),
            AI("7021", "Functional status", "N4+X..20", true, "FUNC STAT", 4 + 1, 4 + 20, GS1_STRING),
            AI("7022", "Revision status", "N4+X..20", true, "REV STAT", 4 + 1, 4 + 20, GS1_STRING),
            AI(
                "7023",
                "Global Individual Asset Identifier (GIAI) of an assembly",
                "N4+X..30",
                true,
                "GIAI – ASSEMBLY",
                4 + 1,
                4 + 30,
                GS1_STRING
            ),

            //703s  Number of processor with ISO Country Code  N4+N3+X..27  (FNC1)  PROCESSOR # s
            //UNSUPPORTED yet

            AI(
                "710",
                "National Healthcare Reimbursement Number (NHRN) – Germany PZN",
                "N3+X..20",
                true,
                "NHRN PZN",
                3 + 1,
                3 + 20,
                GS1_STRING
            ),
            AI(
                "711",
                "National Healthcare Reimbursement Number (NHRN) – France CIP",
                "N3+X..20",
                true,
                "NHRN CIP",
                3 + 1,
                3 + 20,
                GS1_STRING
            ),
            AI(
                "712",
                "National Healthcare Reimbursement Number (NHRN) – Spain CN",
                "N3+X..20",
                true,
                "NHRN CN",
                3 + 1,
                3 + 20,
                GS1_STRING
            ),
            AI(
                "713",
                "National Healthcare Reimbursement Number (NHRN) – Brasil DRN",
                "N3+X..20",
                true,
                "NHRN DRN",
                3 + 1,
                3 + 20,
                GS1_STRING
            ),

            AI(
                "8001",
                "Roll products (width, length, core diameter, direction, splices)",
                "N4+N14",
                true,
                "DIMENSIONS",
                4 + 14,
                0,
                GS1_STRING
            ),
            AI("8002", "Cellular mobile telephone identifier", "N4+X..20", true, "CMT No", 4 + 1, 4 + 20, GS1_STRING),
            AI(
                "8003",
                "Global Returnable Asset Identifier (GRAI)",
                "N4+N14+X..16",
                true,
                "GRAI",
                4 + 14 + 1,
                4 + 14 + 16,
                GS1_STRING
            ),
            AI(
                "8004",
                "Global Individual Asset Identifier (GIAI)",
                "N4+X..30",
                true,
                "GIAI",
                4 + 1,
                4 + 30,
                GS1_STRING
            ),
            AI("8005", "Price per unit of measure", "N4+N6", true, "PRICE PER UNIT", 4 + 6, 0, GS1_STRING),
            AI(
                "8006",
                "Identification of an individual trade item piece",
                "N4+N14+N2+N2",
                true,
                "ITIP or GCTIN",
                4 + 14 + 2 + 2,
                0,
                GS1_STRING
            ),
            AI("8007", "International Bank Account Number (IBAN)", "N4+X..34", true, "IBAN", 4 + 1, 4 + 34, GS1_STRING),
            AI(
                "8008",
                "Date and time of production",
                "N4+N8+N..4",
                true,
                "PROD TIME",
                4 + 8 + 1,
                4 + 8 + 4,
                GS1_STRING
            ),
            AI("8010", "Component / Part Identifier (CPID)", "N4+X..30", true, "CPID", 4 + 1, 4 + 30, GS1_STRING),
            AI(
                "8011",
                "Component / Part Identifier serial number (CPID SERIAL)",
                "N4+N..12",
                true,
                "CPID SERIAL",
                4 + 1,
                4 + 12,
                GS1_STRING
            ),
            AI("8012", "Software version", "N4+X..20", true, "VERSION", 4 + 1, 4 + 20, GS1_STRING),
            AI(
                "8017",
                "Global Service Relation Number to identify the relationship between an organisation offering services and the provider of services",
                "N4+N18", true, "GSRN - PROVIDER", 4 + 18, 0, GS1_STRING
            ),
            AI(
                "8018",
                "Global Service Relation Number to identify the relationship between an organisation offering services and the recipient of services",
                "N4+N18", true, "GSRN - RECIPIENT", 4 + 18, 0, GS1_STRING
            ),
            AI("8019", "Service Relation Instance Number (SRIN)", "N4+N..10", true, "SRIN", 4 + 1, 4 + 10, GS1_STRING),
            AI("8020", "Payment slip reference number", "N4+X..25", true, "REF No", 4 + 1, 4 + 25, GS1_STRING),
            AI(
                "8110",
                "Coupon code identification for use in North America",
                "N4+X..70",
                true,
                "-",
                4 + 1,
                4 + 70,
                GS1_STRING
            ),
            AI("8111", "Loyalty points of a coupon", "N4+N4", true, "POINTS", 4 + 4, 0, GS1_STRING),
            AI(
                "8112",
                "Paperless coupon code identification for use in North America (AI 8112)",
                "N4+X..70",
                true,
                "-",
                4 + 1,
                4 + 70,
                GS1_STRING
            ),
            AI("8200", "Extended Packaging URL", "N4+X..70", true, "PRODUCT URL", 4 + 1, 4 + 70, GS1_STRING),

            //90  Information mutually agreed between trading partners	N2+X..30  (FNC1)  INTERNAL
            AI(
                "90",
                "Information mutually agreed between trading partners",
                "N2+X..30",
                true,
                "INTERNAL",
                2 + 1,
                2 + 30,
                GS1_STRING
            ),

            //91 to 99  Company internal information  N2+X..90  (FNC1)  INTERNAL
            AI("91", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("92", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("93", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("94", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("95", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("96", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("97", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("98", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING),
            AI("99", "Company internal information", "N2+X..90", true, "INTERNAL", 2 + 1, 2 + 90, GS1_STRING)

            //NOTES:
            //- n   implied decimal point position
            //- N   numeric digit
            //- X  any character in Figure 7.11-1
            //- N3  3 numeric digits, predefined length
            //- N..3  up to 3 numeric digits
            //- X..3  up to 3 characters in Figure 7.11-1
        )
    }
}