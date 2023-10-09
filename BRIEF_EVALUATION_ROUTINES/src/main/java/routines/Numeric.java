// ============================================================================
//
// Copyright (c) 2006-2015, Talend SA
//
// Ce code source a été automatiquement généré par_Talend Open Studio for Data Integration
// / Soumis à la Licence Apache, Version 2.0 (la "Licence") ;
// Votre utilisation de ce fichier doit être en accord avec les termes de la Licence.
// Vous pouvez obtenir une copie de la Licence sur
// http://www.apache.org/licenses/LICENSE-2.0
//
// Sauf lorsqu'explicitement prévu par la loi en vigueur ou accepté par écrit, le logiciel
// distribué sous la Licence est distribué "TEL QUEL", 
// SANS GARANTIE OU CONDITION D'AUCUNE SORTE, expresse ou implicite.
// Consultez la Licence pour connaître la terminologie spécifique régissant les autorisations et
// les limites prévues par la Licence.
//
// ============================================================================
package routines;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import routines.system.RandomUtils;

public class Numeric {

    private static final java.util.Map<String, Integer> seq_Hash = new ConcurrentHashMap<>();

    /**
     * return an incremented numeric id
     *
     * {talendTypes} int | Integer
     *
     * {Category} Numeric
     *
     * {param} string("s1") sequence identifier
     *
     * {param} int(1) start value
     *
     * {param} int(1) step
     *
     * {example} sequence("s1", 1, 1) # 1, 2, 3, ...
     *
     * {example} sequence("s2", 100, -2) # 100, 98, 96, ...
     *
     */
    public static Integer sequence(String seqName, int startValue, int step) {
        return seq_Hash.compute(seqName,
                (String k,  Integer v) -> v == null ? startValue : v + step);
    }

    /**
     * create a sequence if not exists and put a new startValue
     *
     * {Category} Numeric
     *
     * {param} string("s1") sequence identifier
     *
     * {param} int(1) start value
     *
     * {example} sequence("s1", 1)
     *
     */

    public static void resetSequence(String seqName, int startValue) {
        seq_Hash.put(seqName, startValue);
    }

    /**
     * remove a sequence
     *
     * {Category} Numeric
     *
     * {param} string("s1") sequence identifier
     *
     * {example} sequence("s1")
     *
     */

    public static void removeSequence(String seqName) {
        seq_Hash.remove(seqName);
    }

    /**
     * return a random int between min and max
     *
     * {Category} Numeric
     *
     * {talendTypes} int | Integer
     *
     * {param} int(0) min value
     *
     * {param} int(100) max value
     *
     * {example} random(3, 10) # 7, 4, 8, ...
     *
     * {example} random(0, 100) # 93, 12, 83, ...
     *
     * @throws RuntimeException in case when max value is smaller than min value
     */
    public static Integer random(Integer min, Integer max) {
        if (max < min) {
            throw new RuntimeException("Max value should be bigger than min value");
        }
        return ((Long) Math.round(min - 0.5 + (RandomUtils.random() * (max - min + 1)))).intValue();
    }

    /**
     * return numbers using an implied decimal format.
     *
     * {Category} Numeric
     *
     * {talendTypes} float | Float
     *
     * {param} String("9V99") format: float pointing format.
     *
     * {param} String("123") toConvert: read this value.
     *
     * {example} convertImpliedDecimalFormat("9V99", "123") result: 1.23 ...
     *
     */
    public static Float convertImpliedDecimalFormat(String format, String toConvert) {
        BigDecimal decimal = Numeric.convertString2BigDecimal(format,toConvert);
        return new Float(decimal.doubleValue());
    }

    public static BigDecimal convertString2BigDecimal(String format, String toConvert) {
        long decimalPlace = 1;
        int indexOf = format.indexOf('V');
        if (indexOf > -1) {
            boolean isV = false;
            for (int i = 0; i < format.length(); i++) {
                char charAt = format.charAt(i);
                if (charAt == '9' && isV) {
                    decimalPlace = 10 * decimalPlace;
                } else if (charAt == 'V') {
                    isV = true;
                }
            }
        }
        BigDecimal decimal = new BigDecimal(toConvert);
        decimal = decimal.divide(new BigDecimal(decimalPlace));
        return decimal;
    }
}
