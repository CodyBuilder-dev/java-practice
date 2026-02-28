package datatype;
import java.nio.charset.Charset;

public final class Char {
  public static void main(String[] args) {
    // 디버그: JVM과 기본 Charset 출력
    System.out.println("[DEBUG] file.encoding=" + System.getProperty("file.encoding"));
    System.out.println("[DEBUG] java.nio.charset.Charset.defaultCharset()=" + Charset.defaultCharset().name()); // 내부적으로 file.encoding과 동일 값
    System.out.println("[DEBUG] stdout.encoding=" + System.getProperty("stdout.encoding"));
    System.out.println("[DEBUG] stderr.encoding=" + System.getProperty("stderr.encoding"));
    System.out.println("[DEBUG] sun.jnu.encoding=" + System.getProperty("sun.jnu.encoding"));
    System.out.println("[DEBUG] os.name=" + System.getProperty("os.name"));
    System.out.println("[DEBUG] os.version=" + System.getProperty("os.version"));
    System.out.println("[DEBUG] os.arch=" + System.getProperty("os.arch"));


    // === 1. 기본 문자 (한글, 영문, 숫자) ===
    System.out.println("=== 1. 기본 문자 (한글, 영문, 숫자) ===");
    char kor = '가';
    char korLast = '힣';  // 현대 한글 마지막 문자
    char eng = 'A';
    char num = '1';
    char unicodeChar = '\uAC00'; // '가'의 유니코드 표현
    System.out.println("한글 '가': " + kor);
    System.out.println("한글 '가'에 int: " + (int) kor);
    System.out.println("한글 '힣': " + korLast);
    System.out.println("영문 'A': " + eng);
    System.out.println("영문 'a': " + Character.toLowerCase(eng));
    System.out.println("영문 'A'에 int: " + (int) eng);
    System.out.println("숫자 '1': " + num);
    System.out.println("숫자 '1'에 int: " + (int) num);
    System.out.println("유니코드로 표현한 '가': " + unicodeChar);

    // === 2. 특수문자 및 기호 ===
    System.out.println("\n=== 2. 특수문자 및 기호 ===");
    char sym = '!';
    char euro = '€';
    char emoji = '♥'; // BMP 범위 내 단일 문자 이모지
    System.out.println("느낌표: " + sym);
    System.out.println("유로 기호: " + euro);
    System.out.println("하트 기호: " + emoji);

    // === 3. 이스케이프 문자 (제어 문자) ===
    System.out.println("\n=== 3. 이스케이프 문자 (제어 문자) ===");
    char space = ' ';
    char tab = '\t';
    char newline = '\n';
    char backspace = '\b';
    char formfeed = '\f';
    char carriageReturn = '\r';
    char singleQuote = '\'';
    char doubleQuote = '\"';
    char backslash = '\\';
    System.out.println("공백: [" + space + "]");
    System.out.println("탭: [" + tab + "]");
    System.out.println("개행: [" + newline + "]");
    System.out.println("백스페이스: [" + backspace + "]");
    System.out.println("폼피드: [" + formfeed + "]");
    System.out.println("캐리지 리턴: [" + carriageReturn + "]");
    System.out.println("작은따옴표: [" + singleQuote + "]");
    System.out.println("큰따옴표: [" + doubleQuote + "]");
    System.out.println("백슬래시: [" + backslash + "]");

    // === 4. 제어 문자 (Control Characters) - 눈에 안보이거나 이상하게 출력 ===
    System.out.println("\n=== 4. 제어 문자 (Control Characters) - 비가시 문자 ===");
    char nullChar = '\u0000';      // NULL
    char soh = '\u0001';           // Start of Heading
    char bell = '\u0007';          // Bell
    char del = '\u007F';           // Delete
    System.out.println("NULL (\\u0000): [" + nullChar + "]");
    System.out.println("SOH (\\u0001): [" + soh + "]");
    System.out.println("BELL (\\u0007): [" + bell + "]");
    System.out.println("DEL (\\u007F): [" + del + "]");

    // === 5. Zero-width 문자 - 눈에 보이지 않음 ===
    System.out.println("\n=== 5. Zero-width 문자 - 완전히 안보이는 문자 ===");
    char zeroWidthSpace = '\u200B';     // Zero Width Space
    char zeroWidthNonJoiner = '\u200C'; // Zero Width Non-Joiner
    char zeroWidthJoiner = '\u200D';    // Zero Width Joiner
    char zeroWidthNoBreakSpace = '\uFEFF'; // Zero Width No-Break Space (BOM)
    char zeroWidthNoBreakSpace2 = '\u00A0';  // Non-breaking space
    System.out.println("Zero Width Space: [A" + zeroWidthSpace + "B] (A와 B 사이에 있지만 안보임)");
    System.out.println("Zero Width Non-Joiner: [가" + zeroWidthNonJoiner + "나]");
    System.out.println("Zero Width Joiner: [" + zeroWidthJoiner + "]");
    System.out.println("Zero Width No-Break Space (BOM): [A" + zeroWidthNoBreakSpace + "B]");
    System.out.println("Non-breaking space : [A" + zeroWidthNoBreakSpace2 + "B]");

    // === 6. 서로게이트 페어 (Surrogate Pair) - 단독으로 사용하면 깨짐 ===
    System.out.println("\n=== 6. 서로게이트 페어 (Surrogate Pair) - 단독으로 깨짐 ===");
    char highSurrogate = '\uD83D'; // High surrogate
    char lowSurrogate = '\uDE00';  // Low surrogate
    System.out.println("High Surrogate 단독 (\\uD83D): [" + highSurrogate + "] (깨진 문자)");
    System.out.println("Low Surrogate 단독 (\\uDE00): [" + lowSurrogate + "] (깨진 문자)");
    System.out.println("두 개 합쳐서 String으로: [" + highSurrogate + lowSurrogate + "] (😀 이모지)");
    String grinningFace = new String(new char[]{highSurrogate, lowSurrogate});
    System.out.println("String으로 생성: " + grinningFace);

    // === 7. 4바이트 이모지 - char 하나로 표현 불가 ===
    System.out.println("\n=== 7. 4바이트 이모지 - char 하나로 표현 불가 ===");
    String emoji4byte = "😀🎉👍🔥💯";
    System.out.println("4바이트 이모지들: " + emoji4byte);
    System.out.println("이모지 길이 (char 단위): " + emoji4byte.length() + " (각 이모지가 2개의 char)");
    System.out.println("이모지 길이 (실제 문자): " + emoji4byte.codePointCount(0, emoji4byte.length()));

    // === 8. 한글 자모 - char로 표현 가능하지만 조합형으로 사용됨 ===
    System.out.println("\n=== 8. 한글 자모 (Jamo) - 조합형 ===");
    char korJamoG = '\u1100';  // ㄱ (초성)
    char korJamoA = '\u1161';  // ㅏ (중성)
    char korJamoK = '\u11A8';  // ㄱ (종성)
    System.out.println("초성 ㄱ (\\u1100): [" + korJamoG + "]");
    System.out.println("중성 ㅏ (\\u1161): [" + korJamoA + "]");
    System.out.println("종성 ㄱ (\\u11A8): [" + korJamoK + "]");
    System.out.println("조합 (초+중+종): [" + korJamoG + korJamoA + korJamoK + "] (각각 따로 표시됨)");
    System.out.println("완성형 '각': ['각']");

    // === 9. 호환용 한글 자모 - 단독 자음/모음 ===
    System.out.println("\n=== 9. 호환용 한글 자모 - 단독 자음/모음 (ㄱㄴㄷ, ㅏㅑㅓ) ===");
    char compatG = '\u3131';  // ㄱ (호환용)
    char compatA = '\u314F';  // ㅏ (호환용)
    System.out.println("호환용 ㄱ (\\u3131): [" + compatG + "]");
    System.out.println("호환용 ㅏ (\\u314F): [" + compatA + "]");
    System.out.println("호환용을 붙여도 완성형이 안됨: [" + compatG + compatA + "]");

    // === 10. 결합 문자 (Combining Characters) ===
    System.out.println("\n=== 10. 결합 문자 (Combining Characters) - 악센트 결합 ===");
    char baseChar = 'e';
    char combiningAcute = '\u0301';  // Combining acute accent
    System.out.println("기본 문자 e: [" + baseChar + "]");
    System.out.println("결합 악센트만 (\\u0301): [" + combiningAcute + "]");
    System.out.println("e + 악센트: [" + baseChar + combiningAcute + "] (é로 표시됨)");

    // === 11. RTL (Right-to-Left) 문자 - 오른쪽에서 왼쪽으로 읽힘 ===
    System.out.println("\n=== 11. RTL (Right-to-Left) 문자 - 아랍어, 히브리어 ===");
    // 아랍어 "مرحبا" (Marhaba = Hello) - 오른쪽에서 왼쪽으로 읽어야 함
    char arabic1 = '\u0645';  // م (mim)
    char arabic2 = '\u0631';  // ر (ra)
    char arabic3 = '\u062D';  // ح (hha)
    char arabic4 = '\u0628';  // ب (ba)
    char arabic5 = '\u0627';  // ا (alif)
    
    System.out.println("아랍어 문자들 (개별):");
    System.out.println("  م (\\u0645): [" + arabic1 + "]");
    System.out.println("  ر (\\u0631): [" + arabic2 + "]");
    System.out.println("  ح (\\u062D): [" + arabic3 + "]");
    System.out.println("  ب (\\u0628): [" + arabic4 + "]");
    System.out.println("  ا (\\u0627): [" + arabic5 + "]");
    
    System.out.println("\n아랍어 단어 (مرحبا = Marhaba):");
    System.out.println("  순서대로 붙이면: [" + arabic1 + arabic2 + arabic3 + arabic4 + arabic5 + "]");
    System.out.println("  (오른쪽부터 읽어야 함: ا ← ب ← ح ← ر ← م)");
    
    // 히브리어 "שלום" (Shalom = Peace)
    char hebrew1 = '\u05E9';  // ש (shin)
    char hebrew2 = '\u05DC';  // ל (lamed)
    char hebrew3 = '\u05D5';  // ו (vav)
    char hebrew4 = '\u05DD';  // ם (mem sofit)
    System.out.println("\n히브리어 단어 (שלום = Shalom):");
    System.out.println("  순서대로 붙이면: [" + hebrew1 + hebrew2 + hebrew3 + hebrew4 + "]");
    System.out.println("  (오른쪽부터 읽어야 함: ם ← ו ← ל ← ש)");
    
    // 혼합 텍스트
    System.out.println("\n영어와 아랍어 혼합:");
    System.out.println("  [Hello " + arabic1 + arabic2 + arabic3 + arabic4 + arabic5 + " World]");
    System.out.println("  (아랍어 부분은 렌더링 엔진에 따라 오른쪽에서 왼쪽으로 표시됨)");

    // === 12. Bidirectional 제어 문자 - 텍스트 방향 제어 ===
    System.out.println("\n=== 12. Bidirectional 제어 문자 - 텍스트 방향 제어 ===");
    char ltrMark = '\u200E';      // Left-to-Right Mark (약한 힌트)
    char rtlMark = '\u200F';      // Right-to-Left Mark (약한 힌트)
    char ltrOverride = '\u202D';  // Left-to-Right Override (강제)
    char rtlOverride = '\u202E';  // Right-to-Left Override (강제)
    
    // LTR Mark & RTL Mark (약한 힌트 - 주로 아랍어/히브리어 + 숫자에서 효과)
    System.out.println("LTR Mark & RTL Mark (약한 힌트):");
    System.out.println("  아랍어 + 숫자 (원본): [" + arabic1 + arabic2 + "123]");
    System.out.println("  아랍어 + LTR Mark + 숫자: [" + arabic1 + arabic2 + ltrMark + "123]");
    System.out.println("  아랍어 + RTL Mark + 숫자: [" + arabic1 + arabic2 + rtlMark + "123]");
    System.out.println("  (숫자의 배치 순서가 미묘하게 달라질 수 있음)");
    
    System.out.println("\n  영어 + 숫자 (원본): [ABC123]");
    System.out.println("  영어 + LTR Mark + 숫자: [ABC" + ltrMark + "123]");
    System.out.println("  영어 + RTL Mark + 숫자: [ABC" + rtlMark + "123]");
    System.out.println("  (영어는 이미 강한 LTR이라 Mark로는 거의 변화 없음)");
    
    // Override (강제 - 명확한 효과)
    System.out.println("\nLTR Override & RTL Override (강제):");
    
    // 숫자에 RTL Override
    System.out.println("  숫자에 RTL Override:");
    System.out.println("    원본: [12345]");
    System.out.println("    RTL Override: [" + rtlOverride + "12345]");
    System.out.println("    (54321로 거꾸로 출력됨)");
    
    // 아랍어에 RTL Override (원래 방향 강화)
    System.out.println("\n  아랍어에 RTL Override:");
    System.out.println("    원본 아랍어 (مرحبا): [" + arabic1 + arabic2 + arabic3 + arabic4 + arabic5 + "]");
    System.out.println("    RTL Override: [" + rtlOverride + arabic1 + arabic2 + arabic3 + arabic4 + arabic5 + "]");
    System.out.println("    (아랍어는 원래 RTL이라 변화 없거나 더 명확해짐)");
    
    // 아랍어에 LTR Override (역방향 강제!)
    System.out.println("\n  아랍어에 LTR Override (역방향 강제!):");
    System.out.println("    원본 아랍어 (مرحبا): [" + arabic1 + arabic2 + arabic3 + arabic4 + arabic5 + "]");
    System.out.println("    LTR Override: [" + ltrOverride + arabic1 + arabic2 + arabic3 + arabic4 + arabic5 + "]");
    System.out.println("    (아랍어를 강제로 왼쪽→오른쪽으로 출력하여 거꾸로 보임!)");
    
    // 영어 + 아랍어 혼합에 RTL Override
    System.out.println("\n  영어+아랍어 혼합에 RTL Override:");
    System.out.println("    원본: [Hello " + arabic1 + arabic2 + arabic3 + " World]");
    System.out.println("    RTL Override: [" + rtlOverride + "Hello " + arabic1 + arabic2 + arabic3 + " World]");
    System.out.println("    (전체가 거꾸로: dlroW... olleH)");

    // === 13. Variation Selector - 같은 문자를 다르게 렌더링 ===
    System.out.println("\n=== 13. Variation Selector - 텍스트 vs 이모지 스타일 ===");
    char heart = '\u2764';  // Heavy Black Heart
    char textVariation = '\uFE0E';  // Variation Selector-15 (텍스트 스타일)
    char emojiVariation = '\uFE0F';  // Variation Selector-16 (이모지 스타일)
    System.out.println("기본 하트: [" + heart + "]");
    System.out.println("텍스트 스타일: [" + heart + textVariation + "]");
    System.out.println("이모지 스타일: [" + heart + emojiVariation + "]");

    char thumbsUp = '\uD83D';  // High surrogate for 👍
    char thumbsUpLow = '\uDC4D'; // Low surrogate for 👍
    System.out.println("기본 Thumbs Up: [" + thumbsUp + thumbsUpLow + "]");
    System.out.println("텍스트 스타일: [" + thumbsUp + thumbsUpLow + textVariation + "]");
    System.out.println("이모지 스타일: [" + thumbsUp + thumbsUpLow + emojiVariation + "]");

    System.out.println("\n=== 14. Skin Tone Modifier - 이모지 피부색 (char로 불가능) ===");
    // === 14. Skin Tone Modifier - 이모지 피부색 변경 (4바이트 이상) ===
    String thumbsUpString = "👍";
    String skinToneLight = "\uD83C\uDFFB";  // Light Skin Tone
    String skinToneDark = "\uD83C\uDFFF";   // Dark Skin Tone
    System.out.println("기본 thumbs up: " + thumbsUpString);
    System.out.println("밝은 피부톤: " + thumbsUpString + skinToneLight);
    System.out.println("어두운 피부톤: " + thumbsUpString + skinToneDark);
    System.out.println("길이 (char): " + (thumbsUpString + skinToneLight).length() + " (4개의 char 필요)");

    // === 15. char 산술 연산 - char는 숫자처럼 연산 가능 ===
    System.out.println("\n=== 15. char 산술 연산 - char는 정수처럼 계산 가능 ===");
    char ch = 'A';
    System.out.println("원본: " + ch);
    System.out.println("ch + 1: " + (char)(ch + 1));  // 'B'
    System.out.println("ch + 32: " + (char)(ch + 32));  // 'a' (대문자 -> 소문자)
    System.out.println("'Z' - 'A': " + ('Z' - 'A'));  // 25
    
    char digit = '5';
    System.out.println("'5' - '0': " + (digit - '0'));  // 5 (문자를 숫자로)
    
    char korean = '가';
    System.out.println("'가' + 1: " + (char)(korean + 1));  // '각'
    System.out.println("'힣' - '가': " + ('힣' - '가'));  // 11171 (현대 한글 글자 수)

    // === 16. char vs int 변환 - char를 숫자로 다루기 ===
    System.out.println("\n=== 16. char vs int 변환 - char를 숫자로 다루기 ===");
    char a = 'A';
    int asciiValue = a;  // 자동 형변환
    System.out.println("'A'의 ASCII/유니코드 값: " + asciiValue);
    System.out.println("'A'의 16진수 값: 0x" + Integer.toHexString(asciiValue));
    
    char fromInt = (char)65;  // int에서 char로
    System.out.println("65를 char로: " + fromInt);
    
    char unicode = (char)0xAC00;
    System.out.println("0xAC00을 char로: " + unicode);  // '가'

    // === 17. char 범위 초과 테스트 - 오버플로우/언더플로우 ===
    System.out.println("\n=== 17. char 범위 초과 테스트 - 오버플로우/언더플로우 ===");
    
    // 양수 오버플로우
    int largeValue = 999999999;
    char overflowChar = (char)largeValue;
    System.out.println("999999999를 char로 변환:");
    System.out.println("  결과 문자: [" + overflowChar + "]");
    System.out.println("  int 값: " + (int)overflowChar);
    System.out.println("  16진수: 0x" + Integer.toHexString(overflowChar));
    System.out.println("  계산 과정: 999999999 % 65536 = " + (largeValue % 65536));
    
    // 음수 할당 (char는 unsigned이지만 int는 signed)
    int negativeValue = -1;
    char negativeChar = (char)negativeValue;
    System.out.println("\n-1을 char로 변환:");
    System.out.println("  결과 문자: [" + negativeChar + "]");
    System.out.println("  int 값: " + (int)negativeChar);
    System.out.println("  16진수: 0x" + Integer.toHexString(negativeChar));
    System.out.println("  설명: -1은 0xFFFF (65535)가 됨");
    
    int negativeValue2 = -100;
    char negativeChar2 = (char)negativeValue2;
    System.out.println("\n-100을 char로 변환:");
    System.out.println("  결과 문자: [" + negativeChar2 + "]");
    System.out.println("  int 값: " + (int)negativeChar2);
    System.out.println("  16진수: 0x" + Integer.toHexString(negativeChar2));
    
    // 경계값 테스트
    System.out.println("\n경계값 테스트:");
    char maxChar = (char)65535;  // 최대값
    char maxPlus1 = (char)65536;  // 최대값 + 1
    System.out.println("65535 (최대값): [" + maxChar + "] = 0x" + Integer.toHexString(maxChar));
    System.out.println("65536 (최대값+1): [" + maxPlus1 + "] = 0x" + Integer.toHexString(maxPlus1) + " (0으로 wrap)");
    
    char minChar = (char)0;  // 최소값
    char minMinus1 = (char)(-1);  // 최소값 - 1
    System.out.println("0 (최소값): [" + minChar + "] = 0x" + Integer.toHexString(minChar));
    System.out.println("-1 (최소값-1): [" + minMinus1 + "] = 0x" + Integer.toHexString(minMinus1) + " (65535로 wrap)");
    
    // 산술 연산 오버플로우
    System.out.println("\n산술 연산 오버플로우:");
    char z = 'Z';  // 90
    char zPlus100 = (char)(z + 100);  // 190
    char zPlus1000 = (char)(z + 1000);  // 1090
    char zPlus100000 = (char)(z + 100000);  // 100090 -> wrap around
    System.out.println("'Z' (90) + 100 = [" + zPlus100 + "] (" + (int)zPlus100 + ")");
    System.out.println("'Z' (90) + 1000 = [" + zPlus1000 + "] (" + (int)zPlus1000 + ")");
    System.out.println("'Z' (90) + 100000 = [" + zPlus100000 + "] (" + (int)zPlus100000 + ", wrap around됨)");


    // === 17. 유효하지 않은/특수한 유니코드 범위 ===
    System.out.println("\n=== 17. 특수한 유니코드 범위 ===");
    char bom = '\uFEFF';  // Byte Order Mark (BOM) - Zero Width No-Break Space
    char replacementChar = '\uFFFD';  // Replacement Character (잘못된 문자 표시용)
    System.out.println("BOM (\\uFEFF): [" + bom + "] (보이지 않음)");
    System.out.println("Replacement Character (\\uFFFD): [" + replacementChar + "] (� 표시)");
    
    // Private Use Area - 사용자 정의 영역
    char privateUse = '\uE000';
    System.out.println("Private Use Area (\\uE000): [" + privateUse + "] (환경에 따라 다름)");

    // === 18. char vs String 비교 ===
    System.out.println("\n=== 18. char vs String 차이점 ===");
    char charA = 'A';
    String stringA = "A";
    System.out.println("char 'A': " + charA + " (기본 타입, 2바이트)");
    System.out.println("String \"A\": " + stringA + " (참조 타입, 객체)");
    System.out.println("char는 단일 문자만: " + charA);
    System.out.println("String은 여러 문자 가능: " + "ABC");
    System.out.println("char는 산술 연산 가능: " + (char)(charA + 1));
    // System.out.println("String은 산술 연산 불가: " + (stringA + 1)); // 컴파일 에러
    System.out.println("4바이트 이모지는 String만 가능: 😀");

    // === char 범위 및 속성 정보 ===
    System.out.println("\n=== char 범위 및 속성 정보 ===");
    System.out.println("char 최소값: " + (int)Character.MIN_VALUE + " (0x" + Integer.toHexString(Character.MIN_VALUE) + ")");
    System.out.println("char 최대값: " + (int)Character.MAX_VALUE + " (0x" + Integer.toHexString(Character.MAX_VALUE) + ")");
    System.out.println("char 크기: " + Character.SIZE + " bits");
    System.out.println("char 바이트: " + Character.BYTES + " bytes");
    System.out.println("인코딩: UTF-16");
    System.out.println("표현 가능 범위: BMP (Basic Multilingual Plane, U+0000 ~ U+FFFF)");
  }
}
