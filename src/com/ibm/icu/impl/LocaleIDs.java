/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleIDs
/*     */ {
/*     */   private static String[] _languages;
/*     */   private static String[] _replacementLanguages;
/*     */   private static String[] _obsoleteLanguages;
/*     */   private static String[] _languages3;
/*     */   private static String[] _obsoleteLanguages3;
/*     */   private static String[] _countries;
/*     */   private static String[] _deprecatedCountries;
/*     */   private static String[] _replacementCountries;
/*     */   private static String[] _obsoleteCountries;
/*     */   private static String[] _countries3;
/*     */   private static String[] _obsoleteCountries3;
/*     */   
/*     */   public static String[] getISOCountries()
/*     */   {
/*  26 */     initCountryTables();
/*  27 */     return (String[])_countries.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getISOLanguages()
/*     */   {
/*  39 */     initLanguageTables();
/*  40 */     return (String[])_languages.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getISO3Country(String country)
/*     */   {
/*  52 */     initCountryTables();
/*     */     
/*  54 */     int offset = findIndex(_countries, country);
/*  55 */     if (offset >= 0) {
/*  56 */       return _countries3[offset];
/*     */     }
/*  58 */     offset = findIndex(_obsoleteCountries, country);
/*  59 */     if (offset >= 0) {
/*  60 */       return _obsoleteCountries3[offset];
/*     */     }
/*     */     
/*  63 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getISO3Language(String language)
/*     */   {
/*  76 */     initLanguageTables();
/*     */     
/*  78 */     int offset = findIndex(_languages, language);
/*  79 */     if (offset >= 0) {
/*  80 */       return _languages3[offset];
/*     */     }
/*  82 */     offset = findIndex(_obsoleteLanguages, language);
/*  83 */     if (offset >= 0) {
/*  84 */       return _obsoleteLanguages3[offset];
/*     */     }
/*     */     
/*  87 */     return "";
/*     */   }
/*     */   
/*     */   public static String threeToTwoLetterLanguage(String lang) {
/*  91 */     initLanguageTables();
/*     */     
/*     */ 
/*  94 */     int offset = findIndex(_languages3, lang);
/*  95 */     if (offset >= 0) {
/*  96 */       return _languages[offset];
/*     */     }
/*     */     
/*  99 */     offset = findIndex(_obsoleteLanguages3, lang);
/* 100 */     if (offset >= 0) {
/* 101 */       return _obsoleteLanguages[offset];
/*     */     }
/*     */     
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   public static String threeToTwoLetterRegion(String region) {
/* 108 */     initCountryTables();
/*     */     
/*     */ 
/* 111 */     int offset = findIndex(_countries3, region);
/* 112 */     if (offset >= 0) {
/* 113 */       return _countries[offset];
/*     */     }
/*     */     
/* 116 */     offset = findIndex(_obsoleteCountries3, region);
/* 117 */     if (offset >= 0) {
/* 118 */       return _obsoleteCountries[offset];
/*     */     }
/*     */     
/* 121 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findIndex(String[] array, String target)
/*     */   {
/* 129 */     for (int i = 0; i < array.length; i++) {
/* 130 */       if (target.equals(array[i])) {
/* 131 */         return i;
/*     */       }
/*     */     }
/* 134 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void initLanguageTables()
/*     */   {
/* 159 */     if (_languages == null)
/*     */     {
/*     */ 
/*     */ 
/* 163 */       String[] tempLanguages = { "aa", "ab", "ace", "ach", "ada", "ady", "ae", "af", "afa", "afh", "ak", "akk", "ale", "alg", "am", "an", "ang", "apa", "ar", "arc", "arn", "arp", "art", "arw", "as", "ast", "ath", "aus", "av", "awa", "ay", "az", "ba", "bad", "bai", "bal", "ban", "bas", "bat", "be", "bej", "bem", "ber", "bg", "bh", "bho", "bi", "bik", "bin", "bla", "bm", "bn", "bnt", "bo", "br", "bra", "bs", "btk", "bua", "bug", "byn", "ca", "cad", "cai", "car", "cau", "ce", "ceb", "cel", "ch", "chb", "chg", "chk", "chm", "chn", "cho", "chp", "chr", "chy", "cmc", "co", "cop", "cpe", "cpf", "cpp", "cr", "crh", "crp", "cs", "csb", "cu", "cus", "cv", "cy", "da", "dak", "dar", "day", "de", "del", "den", "dgr", "din", "doi", "dra", "dsb", "dua", "dum", "dv", "dyu", "dz", "ee", "efi", "egy", "eka", "el", "elx", "en", "enm", "eo", "es", "et", "eu", "ewo", "fa", "fan", "fat", "ff", "fi", "fiu", "fj", "fo", "fon", "fr", "frm", "fro", "fur", "fy", "ga", "gaa", "gay", "gba", "gd", "gem", "gez", "gil", "gl", "gmh", "gn", "goh", "gon", "gor", "got", "grb", "grc", "gu", "gv", "gwi", "ha", "hai", "haw", "he", "hi", "hil", "him", "hit", "hmn", "ho", "hr", "hsb", "ht", "hu", "hup", "hy", "hz", "ia", "iba", "id", "ie", "ig", "ii", "ijo", "ik", "ilo", "inc", "ine", "inh", "io", "ira", "iro", "is", "it", "iu", "ja", "jbo", "jpr", "jrb", "jv", "ka", "kaa", "kab", "kac", "kam", "kar", "kaw", "kbd", "kg", "kha", "khi", "kho", "ki", "kj", "kk", "kl", "km", "kmb", "kn", "ko", "kok", "kos", "kpe", "kr", "krc", "kro", "kru", "ks", "ku", "kum", "kut", "kv", "kw", "ky", "la", "lad", "lah", "lam", "lb", "lez", "lg", "li", "ln", "lo", "lol", "loz", "lt", "lu", "lua", "lui", "lun", "luo", "lus", "lv", "mad", "mag", "mai", "mak", "man", "map", "mas", "mdf", "mdr", "men", "mg", "mga", "mh", "mi", "mic", "min", "mis", "mk", "mkh", "ml", "mn", "mnc", "mni", "mno", "mo", "moh", "mos", "mr", "ms", "mt", "mul", "mun", "mus", "mwr", "my", "myn", "myv", "na", "nah", "nai", "nap", "nb", "nd", "nds", "ne", "new", "ng", "nia", "nic", "niu", "nl", "nn", "no", "nog", "non", "nr", "nso", "nub", "nv", "nwc", "ny", "nym", "nyn", "nyo", "nzi", "oc", "oj", "om", "or", "os", "osa", "ota", "oto", "pa", "paa", "pag", "pal", "pam", "pap", "pau", "peo", "phi", "phn", "pi", "pl", "pon", "pra", "pro", "ps", "pt", "qu", "raj", "rap", "rar", "rm", "rn", "ro", "roa", "rom", "ru", "rup", "rw", "sa", "sad", "sah", "sai", "sal", "sam", "sas", "sat", "sc", "sco", "sd", "se", "sel", "sem", "sg", "sga", "sgn", "shn", "si", "sid", "sio", "sit", "sk", "sl", "sla", "sm", "sma", "smi", "smj", "smn", "sms", "sn", "snk", "so", "sog", "son", "sq", "sr", "srr", "ss", "ssa", "st", "su", "suk", "sus", "sux", "sv", "sw", "syr", "ta", "tai", "te", "tem", "ter", "tet", "tg", "th", "ti", "tig", "tiv", "tk", "tkl", "tl", "tlh", "tli", "tmh", "tn", "to", "tog", "tpi", "tr", "ts", "tsi", "tt", "tum", "tup", "tut", "tvl", "tw", "ty", "tyv", "udm", "ug", "uga", "uk", "umb", "und", "ur", "uz", "vai", "ve", "vi", "vo", "vot", "wa", "wak", "wal", "war", "was", "wen", "wo", "xal", "xh", "yao", "yap", "yi", "yo", "ypk", "za", "zap", "zen", "zh", "znd", "zu", "zun" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */       String[] tempReplacementLanguages = { "id", "he", "yi", "jv", "sr", "nb" };
/*     */       
/*     */ 
/*     */ 
/* 227 */       String[] tempObsoleteLanguages = { "in", "iw", "ji", "jw", "sh", "no" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 234 */       String[] tempLanguages3 = { "aar", "abk", "ace", "ach", "ada", "ady", "ave", "afr", "afa", "afh", "aka", "akk", "ale", "alg", "amh", "arg", "ang", "apa", "ara", "arc", "arn", "arp", "art", "arw", "asm", "ast", "ath", "aus", "ava", "awa", "aym", "aze", "bak", "bad", "bai", "bal", "ban", "bas", "bat", "bel", "bej", "bem", "ber", "bul", "bih", "bho", "bis", "bik", "bin", "bla", "bam", "ben", "bnt", "bod", "bre", "bra", "bos", "btk", "bua", "bug", "byn", "cat", "cad", "cai", "car", "cau", "che", "ceb", "cel", "cha", "chb", "chg", "chk", "chm", "chn", "cho", "chp", "chr", "chy", "cmc", "cos", "cop", "cpe", "cpf", "cpp", "cre", "crh", "crp", "ces", "csb", "chu", "cus", "chv", "cym", "dan", "dak", "dar", "day", "deu", "del", "den", "dgr", "din", "doi", "dra", "dsb", "dua", "dum", "div", "dyu", "dzo", "ewe", "efi", "egy", "eka", "ell", "elx", "eng", "enm", "epo", "spa", "est", "eus", "ewo", "fas", "fan", "fat", "ful", "fin", "fiu", "fij", "fao", "fon", "fra", "frm", "fro", "fur", "fry", "gle", "gaa", "gay", "gba", "gla", "gem", "gez", "gil", "glg", "gmh", "grn", "goh", "gon", "gor", "got", "grb", "grc", "guj", "glv", "gwi", "hau", "hai", "haw", "heb", "hin", "hil", "him", "hit", "hmn", "hmo", "hrv", "hsb", "hat", "hun", "hup", "hye", "her", "ina", "iba", "ind", "ile", "ibo", "iii", "ijo", "ipk", "ilo", "inc", "ine", "inh", "ido", "ira", "iro", "isl", "ita", "iku", "jpn", "jbo", "jpr", "jrb", "jaw", "kat", "kaa", "kab", "kac", "kam", "kar", "kaw", "kbd", "kon", "kha", "khi", "kho", "kik", "kua", "kaz", "kal", "khm", "kmb", "kan", "kor", "kok", "kos", "kpe", "kau", "krc", "kro", "kru", "kas", "kur", "kum", "kut", "kom", "cor", "kir", "lat", "lad", "lah", "lam", "ltz", "lez", "lug", "lim", "lin", "lao", "lol", "loz", "lit", "lub", "lua", "lui", "lun", "luo", "lus", "lav", "mad", "mag", "mai", "mak", "man", "map", "mas", "mdf", "mdr", "men", "mlg", "mga", "mah", "mri", "mic", "min", "mis", "mkd", "mkh", "mal", "mon", "mnc", "mni", "mno", "mol", "moh", "mos", "mar", "msa", "mlt", "mul", "mun", "mus", "mwr", "mya", "myn", "myv", "nau", "nah", "nai", "nap", "nob", "nde", "nds", "nep", "new", "ndo", "nia", "nic", "niu", "nld", "nno", "nor", "nog", "non", "nbl", "nso", "nub", "nav", "nwc", "nya", "nym", "nyn", "nyo", "nzi", "oci", "oji", "orm", "ori", "oss", "osa", "ota", "oto", "pan", "paa", "pag", "pal", "pam", "pap", "pau", "peo", "phi", "phn", "pli", "pol", "pon", "pra", "pro", "pus", "por", "que", "raj", "rap", "rar", "roh", "run", "ron", "roa", "rom", "rus", "rup", "kin", "san", "sad", "sah", "sai", "sal", "sam", "sas", "sat", "srd", "sco", "snd", "sme", "sel", "sem", "sag", "sga", "sgn", "shn", "sin", "sid", "sio", "sit", "slk", "slv", "sla", "smo", "sma", "smi", "smj", "smn", "sms", "sna", "snk", "som", "sog", "son", "sqi", "srp", "srr", "ssw", "ssa", "sot", "sun", "suk", "sus", "sux", "swe", "swa", "syr", "tam", "tai", "tel", "tem", "ter", "tet", "tgk", "tha", "tir", "tig", "tiv", "tuk", "tkl", "tgl", "tlh", "tli", "tmh", "tsn", "ton", "tog", "tpi", "tur", "tso", "tsi", "tat", "tum", "tup", "tut", "tvl", "twi", "tah", "tyv", "udm", "uig", "uga", "ukr", "umb", "und", "urd", "uzb", "vai", "ven", "vie", "vol", "vot", "wln", "wak", "wal", "war", "was", "wen", "wol", "xal", "xho", "yao", "yap", "yid", "yor", "ypk", "zha", "zap", "zen", "zho", "znd", "zul", "zun" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 351 */       String[] tempObsoleteLanguages3 = { "ind", "heb", "yid", "jaw", "srp" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 356 */       synchronized (ULocale.class) {
/* 357 */         if (_languages == null) {
/* 358 */           _languages = tempLanguages;
/* 359 */           _replacementLanguages = tempReplacementLanguages;
/* 360 */           _obsoleteLanguages = tempObsoleteLanguages;
/* 361 */           _languages3 = tempLanguages3;
/* 362 */           _obsoleteLanguages3 = tempObsoleteLanguages3;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void initCountryTables()
/*     */   {
/* 377 */     if (_countries == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 388 */       String[] tempCountries = { "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AN", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AX", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BL", "BM", "BN", "BO", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "GA", "GB", "GD", "GE", "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "ST", "SV", "SY", "SZ", "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE", "YT", "ZA", "ZM", "ZW" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 422 */       String[] tempObsoleteCountries = { "FX", "CS", "RO", "TP", "YU", "ZR" };
/*     */       
/*     */ 
/*     */ 
/* 426 */       String[] tempDeprecatedCountries = { "BU", "CS", "DY", "FX", "HV", "NH", "RH", "TP", "YU", "ZR" };
/*     */       
/*     */ 
/* 429 */       String[] tempReplacementCountries = { "MM", "RS", "BJ", "FR", "BF", "VU", "ZW", "TL", "RS", "CD" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 436 */       String[] tempCountries3 = { "AND", "ARE", "AFG", "ATG", "AIA", "ALB", "ARM", "ANT", "AGO", "ATA", "ARG", "ASM", "AUT", "AUS", "ABW", "ALA", "AZE", "BIH", "BRB", "BGD", "BEL", "BFA", "BGR", "BHR", "BDI", "BEN", "BLM", "BMU", "BRN", "BOL", "BRA", "BHS", "BTN", "BVT", "BWA", "BLR", "BLZ", "CAN", "CCK", "COD", "CAF", "COG", "CHE", "CIV", "COK", "CHL", "CMR", "CHN", "COL", "CRI", "CUB", "CPV", "CXR", "CYP", "CZE", "DEU", "DJI", "DNK", "DMA", "DOM", "DZA", "ECU", "EST", "EGY", "ESH", "ERI", "ESP", "ETH", "FIN", "FJI", "FLK", "FSM", "FRO", "FRA", "GAB", "GBR", "GRD", "GEO", "GUF", "GGY", "GHA", "GIB", "GRL", "GMB", "GIN", "GLP", "GNQ", "GRC", "SGS", "GTM", "GUM", "GNB", "GUY", "HKG", "HMD", "HND", "HRV", "HTI", "HUN", "IDN", "IRL", "ISR", "IMN", "IND", "IOT", "IRQ", "IRN", "ISL", "ITA", "JEY", "JAM", "JOR", "JPN", "KEN", "KGZ", "KHM", "KIR", "COM", "KNA", "PRK", "KOR", "KWT", "CYM", "KAZ", "LAO", "LBN", "LCA", "LIE", "LKA", "LBR", "LSO", "LTU", "LUX", "LVA", "LBY", "MAR", "MCO", "MDA", "MNE", "MAF", "MDG", "MHL", "MKD", "MLI", "MMR", "MNG", "MAC", "MNP", "MTQ", "MRT", "MSR", "MLT", "MUS", "MDV", "MWI", "MEX", "MYS", "MOZ", "NAM", "NCL", "NER", "NFK", "NGA", "NIC", "NLD", "NOR", "NPL", "NRU", "NIU", "NZL", "OMN", "PAN", "PER", "PYF", "PNG", "PHL", "PAK", "POL", "SPM", "PCN", "PRI", "PSE", "PRT", "PLW", "PRY", "QAT", "REU", "ROU", "SRB", "RUS", "RWA", "SAU", "SLB", "SYC", "SDN", "SWE", "SGP", "SHN", "SVN", "SJM", "SVK", "SLE", "SMR", "SEN", "SOM", "SUR", "STP", "SLV", "SYR", "SWZ", "TCA", "TCD", "ATF", "TGO", "THA", "TJK", "TKL", "TLS", "TKM", "TUN", "TON", "TUR", "TTO", "TUV", "TWN", "TZA", "UKR", "UGA", "UMI", "USA", "URY", "UZB", "VAT", "VCT", "VEN", "VGB", "VIR", "VNM", "VUT", "WLF", "WSM", "YEM", "MYT", "ZAF", "ZMB", "ZWE" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 499 */       String[] tempObsoleteCountries3 = { "FXX", "SCG", "ROM", "TMP", "YUG", "ZAR" };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 504 */       synchronized (ULocale.class) {
/* 505 */         if (_countries == null) {
/* 506 */           _countries = tempCountries;
/* 507 */           _deprecatedCountries = tempDeprecatedCountries;
/* 508 */           _replacementCountries = tempReplacementCountries;
/* 509 */           _obsoleteCountries = tempObsoleteCountries;
/* 510 */           _countries3 = tempCountries3;
/* 511 */           _obsoleteCountries3 = tempObsoleteCountries3;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getCurrentCountryID(String oldID) {
/* 518 */     initCountryTables();
/* 519 */     int offset = findIndex(_deprecatedCountries, oldID);
/* 520 */     if (offset >= 0) {
/* 521 */       return _replacementCountries[offset];
/*     */     }
/* 523 */     return oldID;
/*     */   }
/*     */   
/*     */   public static String getCurrentLanguageID(String oldID) {
/* 527 */     initLanguageTables();
/* 528 */     int offset = findIndex(_obsoleteLanguages, oldID);
/* 529 */     if (offset >= 0) {
/* 530 */       return _replacementLanguages[offset];
/*     */     }
/* 532 */     return oldID;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\LocaleIDs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */