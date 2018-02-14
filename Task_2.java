import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task_2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String f = in.nextLine();
        if (f.equals("")) {
            System.out.println("Вы ввели пустую строку");
        }
        // Основная часть программы реализована в методе kalk()
        else kalk(f);
    }

    public static void kalk(String f) {
        // Вычисляем значение выражения
        //
        // Множественные пробелы заменяем одним
        while (f.contains("  ")) {
            f = f.replace("  ", " ");
        }
        // Заменяем все запятые на точки
        f = f.replace(",", ".");
        // Первая проверка
        String err_1 = prov1(f);
        if (err_1.equals("Error")) return;
        // Удаляем все пробелы
        f = f.replace(" ", "");
        if (f.equals("")) {
            System.out.println("Вы ввели только пробелы");
            return;
        }
        // Вторая проверка
        String err_2 = prov2(f);
        if (err_2.equals("Error")) return;
        // Сосчитаем уникальные объекты
        int x = un_obj(f);
        // Если выражение состоит из 1-го числа
        if (x == 1) {
            System.out.println(f);
            return;
        }
        // Составим список элементов массива
        Object[] masiv = spis(f, x);
        // Вычисляем выражение
        while (true) {
            int z = 0;
            // В ходе вычислений освободившиеся ячейки заменяем на пробелы
            // Сдвигаем все пробелы в правую часть выражения
            for (int k = 0; k < 4; k++) {
                for (int n = 0; n < masiv.length; n++) {
                    if (masiv[n].equals(" ")) {
                        for (int j = n; j < masiv.length - 1; j++) {
                            masiv[j] = masiv[j + 1];
                        }
                        masiv[masiv.length - 1] = " ";
                    }
                }
            }
            // Программа завершит вычисления когда 2-ой символ массива будет пробел
            if (masiv[1].equals(" ")) {
                // Ответ
                System.out.println(masiv[0]);
                return;
            }
            // Вычисляем схему ()^2^3=()^6
            // Если пропустить эту схему то может возникнуть ошибка ()^2^3=()^8
            for (int i = 0; i < masiv.length - 3; i++) {
                if (masiv[i].equals("^") && (masiv[i + 1] instanceof BigInteger) && masiv[i + 2].equals("^") && (masiv[i + 3] instanceof BigInteger)) {
                    BigInteger a = (BigInteger) masiv[i+1];
                    BigInteger b = (BigInteger) masiv[i+3];
                    BigInteger num_1 = a.multiply(b);
                    masiv[i + 1] = num_1;
                    masiv[i + 2] = " ";
                    masiv[i + 3] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему 2^3=8
            for (int i = 0; i < masiv.length - 2; i++) {
                if ((masiv[i] instanceof BigDecimal) && masiv[i + 1].equals("^") && (masiv[i + 2] instanceof BigInteger)) {
                    BigDecimal a = (BigDecimal) masiv[i];
                    BigDecimal one = new BigDecimal("1");
                    BigInteger one_i = new BigInteger("1");
                    BigInteger b = (BigInteger) masiv[i + 2];
                    BigInteger zero_i = new BigInteger("0");
                    BigDecimal zero = new BigDecimal("0");
                    // 1 в любой степени = 1
                    if (a.compareTo(one) == 0) {
                        masiv[i] = one;
                        masiv[i + 1] = " ";
                        masiv[i + 2] = " ";
                        z = 1;
                        break;
                    }
                    if (a.compareTo(zero) == 0) {
                        // 0 в любой степени больше нуля = 0
                        if (b.compareTo(zero_i) == 1) {
                            masiv[i] = zero;
                            masiv[i + 1] = " ";
                            masiv[i + 2] = " ";
                            z = 1;
                            break;
                        }
                        if (b.compareTo(zero_i) == -1) {
                            System.out.println("Ошибка: нельзя возводить ноль в отрицательную степень");
                            return;
                        }
                        if (b.compareTo(zero_i) == 0) {
                            // 0^0 - это неопределённость
                            System.out.println("Неопределённость 0^0");
                            return;
                        }
                    }
                    if (b.compareTo(zero_i) < 0) {
                        b = b.negate();
                        BigInteger b_0 = zero_i;
                        BigDecimal num_1 = new BigDecimal("1");
                        while (b.compareTo(b_0) > 0) {
                            num_1 = num_1.multiply(a);
                            b_0 = b_0.add(one_i);
                        }
                        num_1 = one.divide(num_1, 10, BigDecimal.ROUND_HALF_UP);
                        masiv[i] = num_1;
                        masiv[i + 1] = " ";
                        masiv[i + 2] = " ";
                        z = 1;
                        break;
                    } else {
                        BigInteger b_0 = zero_i;
                        BigDecimal num_1 = new BigDecimal("1");
                        while (b.compareTo(b_0) > 0) {
                            num_1 = num_1.multiply(a);
                            b_0 = b_0.add(one_i);
                        }
                        masiv[i] = num_1;
                        masiv[i + 1] = " ";
                        masiv[i + 2] = " ";
                        z = 1;
                        break;
                    }
                }
            }
            if (z == 1) continue;
            // Вычисляем схему (4)=4
            for (int i = 0; i < masiv.length - 2; i++) {
                if (masiv[i].equals("(") && masiv[i + 1] instanceof BigDecimal && masiv[i + 2].equals(")")) {
                    masiv[i] = " ";
                    masiv[i + 2] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему 4*4 = 16
            for (int i = 0; i < masiv.length - 2; i ++) {
                if ((masiv[i] instanceof BigDecimal) && masiv[i + 1].equals("*") && (masiv[i + 2] instanceof BigDecimal)) {
                    BigDecimal a = (BigDecimal) masiv[i];
                    BigDecimal b = (BigDecimal) masiv[i + 2];
                    BigDecimal num_1 = a.multiply(b);
                    masiv[i] = num_1;
                    masiv[i + 1] = " ";
                    masiv[i + 2] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему 4/4 = 16
            for (int i = 0; i < masiv.length - 2; i++) {
                if ((masiv[i] instanceof BigDecimal) && masiv[i + 1].equals("/") &&
                        (masiv[i + 2] instanceof BigDecimal)) {
                    BigDecimal zero = new BigDecimal("0");
                    BigDecimal a = (BigDecimal) masiv[i];
                    BigDecimal b = (BigDecimal) masiv[i+2];
                    if (b.compareTo(zero) == 0) {
                        System.out.println("Ошибка: деление на ноль!");
                        return;
                    }
                    BigDecimal num_1 = a.divide(b, 10, BigDecimal.ROUND_HALF_UP);
                    masiv[i] = num_1;
                    masiv[i + 1] = " ";
                    masiv[i + 2] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему (5+4+ = (9+
            for (int i = 0; i < masiv.length - 4; i++) {
                if (masiv[i].equals("(") && masiv[i+1] instanceof BigDecimal && masiv[i+2].equals("+") &&
                        masiv[i+3] instanceof BigDecimal && (masiv[i+4].equals("+") | masiv[i+4].equals("-"))) {
                    BigDecimal a = (BigDecimal) masiv[i+1];
                    BigDecimal b = (BigDecimal) masiv[i+3];
                    BigDecimal num_1 = a.add(b);
                    masiv[i+1] = num_1;
                    masiv[i+2] = " ";
                    masiv[i+3] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему (5-4+ = (1+
            for (int i = 0; i < masiv.length - 4; i++) {
                if (masiv[i].equals("(") && masiv[i+1] instanceof BigDecimal && masiv[i+2].equals("-") &&
                        masiv[i+3] instanceof BigDecimal && (masiv[i+4].equals("+") | masiv[i+4].equals("-"))) {
                    BigDecimal a = (BigDecimal) masiv[i+1];
                    BigDecimal b = (BigDecimal) masiv[i+3];
                    b = b.negate();
                    BigDecimal num_1 = a.add(b);
                    masiv[i+1] = num_1;
                    masiv[i+2] = " ";
                    masiv[i+3] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему (5+8) = 13
            for (int i = 0; i < masiv.length - 4; i++) {
                if (masiv[i].equals("(") && masiv[i+1] instanceof BigDecimal && masiv[i+2].equals("+") &&
                        masiv[i+3] instanceof BigDecimal && masiv[i+4].equals(")")) {
                    BigDecimal a = (BigDecimal) masiv[i+1];
                    BigDecimal b = (BigDecimal) masiv[i+3];
                    BigDecimal num_1 = a.add(b);
                    masiv[i] = num_1;
                    masiv[i+1] = " ";
                    masiv[i+2] = " ";
                    masiv[i+3] = " ";
                    masiv[i+4] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему (5-8) = -3
            for (int i = 0; i < masiv.length - 4; i++) {
                if (masiv[i].equals("(") && masiv[i+1] instanceof BigDecimal && masiv[i+2].equals("-") &&
                        masiv[i+3] instanceof BigDecimal && masiv[i+4].equals(")")) {
                    BigDecimal a = (BigDecimal) masiv[i+1];
                    BigDecimal b = (BigDecimal) masiv[i+3];
                    b = b.negate();
                    BigDecimal num_1 = a.add(b);
                    masiv[i] = num_1;
                    masiv[i+1] = " ";
                    masiv[i+2] = " ";
                    masiv[i+3] = " ";
                    masiv[i+4] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему 5+8 = 13
            for (int i = 0; i < masiv.length - 2; i++) {
                if ((masiv[i] instanceof BigDecimal) && masiv[i+1].equals("+") && (masiv[i+2] instanceof BigDecimal)) {
                    BigDecimal a = (BigDecimal) masiv[i];
                    BigDecimal b = (BigDecimal) masiv[i+2];
                    BigDecimal num_1 = a.add(b);
                    masiv[i] = num_1;
                    masiv[i+1] = " ";
                    masiv[i+2] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            // Вычисляем схему 5-8 = -3
            for (int i = 0; i < masiv.length - 2; i++) {
                if ((masiv[i] instanceof BigDecimal) && masiv[i+1].equals("-") && (masiv[i+2] instanceof BigDecimal)) {
                    BigDecimal a = (BigDecimal) masiv[i];
                    BigDecimal b = (BigDecimal) masiv[i+2];
                    b = b.negate();
                    BigDecimal num_1 = a.add(b);
                    masiv[i] = num_1;
                    masiv[i+1] = " ";
                    masiv[i+2] = " ";
                    z = 1;
                    break;
                }
            }
            if (z == 1) continue;
            System.out.println("Программа отработала не верно. Что-то пошло не так...");
        }
    }

    public static boolean comp(Pattern p, String f) {
        // Сравнивает строку на соответствие шаблону
        Matcher m = p.matcher(f);
        return m.matches();
    }

    public static String prov1(String f) {
        // Проверяем выражение на правильную расстановку пробелов, скобок и
        // использование только допустимых символов
        //
        //В выражении могут участвовать только символы из набора 0-9+\-*/^() .
        String[] list_f = f.split("");
        Pattern p1 = Pattern.compile(".*[^0-9+*/^(). -]+.*");
        if (comp(p1, f)) {
            System.out.println("В выражении используются не допустимые символы");
            return "Error";
        }
        // Проверка на скобки
        int a = 0;
        for (int i = 0; i < list_f.length; i++) {
            if (list_f[i].equals("(")) a += 1;
            else if (list_f[i].equals(")")) a -= 1;
            if (a < 0) {
                System.out.println("Не правильная расстановка скобок. Скобки закрываются раньше чем открываются");
                return "Error";
            }
        }
        if (a != 0) {
            System.out.println("Не правильная расстановка скобок. Кол '(' != ')'");
            return "Error";
        }
        // Не должно быть сочетания символов " ." и ". "
        if (f.contains(" .") | f.contains(". ")) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Между цифрами не должно быть пробелов
        Pattern p2 = Pattern.compile(".*[0-9]+[ ][0-9]+.*");
        if (comp(p2, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        return "OK";
    }
    public static String prov2(String f) {
        // Проверяем выражение на правильное сочетание символов
        //
        // Не допустимое сочетание символов 2(
        Pattern p1 = Pattern.compile(".*[0-9]+\\(.*");
        if (comp(p1, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не может заканчиваться на символы +*/-^(.
        Pattern p2 = Pattern.compile(".*[+*/^(-]$");
        if (comp(p2, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не может начинаться с символов +*/^).
        Pattern p3 = Pattern.compile("[+*/^].*");
        if (comp(p3, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не допустимое сочетание символов ++
        Pattern p4 = Pattern.compile(".*[+*/-][+*/^).-].*");
        if (comp(p4, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не допустимое сочетание символов ^(
        Pattern p5 = Pattern.compile(".*\\^[+*/^().].*");
        if (comp(p5, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не допустимое сочетание символов (^
        Pattern p6 = Pattern.compile(".*[(][+*/^).].*");
        if (comp(p6, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не допустимое сочетание символов )(
        Pattern p7 = Pattern.compile(".*[)][0-9(.].*");
        if (comp(p7, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не допустимое сочетание символов ..
        Pattern p8 = Pattern.compile(".*[.][^0-9].*");
        if (comp(p8, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Тройные сочетания
        // Не допустимое сочетание символов +00
        Pattern p9 = Pattern.compile(".*[+*^(/-]0[0-9].*");
        if (comp(p9, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не допустимое сочетание символов .5.
        Pattern p10 = Pattern.compile(".*\\.[0-9]*\\..*");
        if (comp(p10, f)) {
            System.out.println("Выражение составлено не корректно");
            return "Error";
        }
        // Не допустимое сочетание символов ^2.3
        Pattern p11 = Pattern.compile(".*\\^-*[0-9]+\\..*");
        if (comp(p11, f)) {
            System.out.println("По условию можно возводить только в целую степень");
            return "Error";
        }
        return "Ok";
    }

    public static int un_obj(String f) {
        // Метод считает уникальные объекты в выражении. (-2) - три объекта '(' '-2' ')'; (1-2) - 5 объектов
        int x = 1;
        String[] list_f = f.split("");
        if (list_f[0].equals("-")) x = 0;
        for (int i = 1; i < list_f.length; i++) {
            Pattern p1 = Pattern.compile("[+*/^()]+");
            if (comp(p1, list_f[i])) {
                x += 1;
            }
            Pattern p2 = Pattern.compile("[0-9.]");
            if (comp(p2, list_f[i]) && !comp(p2, list_f[i - 1])) {
                x += 1;
            }
            if (list_f[i].equals("-") && !list_f[i - 1].equals("^") && !list_f[i - 1].equals("(")) {
                x += 1;
            }
        }
        return x;
    }
    public static Object[] spis(String f, int x) {
        // Составляем список уникальных объектов в выражении
        int r = 0;
        int g = 0;
        int h = 0;
        String[] list_f = f.split("");
        Object[] masiv = new Object[x];
        String str = "";
        for (int j = 0; j < list_f.length; j++) {
            Pattern p1 = Pattern.compile("[+*/)(]+");
            if (comp(p1, list_f[j])) {
                masiv[r] = list_f[j];
                r += 1;
                str = "";
                continue;
            } else if (list_f[j].equals("-")) {
                if (j == 0) {
                    h += 1;
                    str = "";
                    continue;
                }
                if (list_f[j - 1].equals("(") | list_f[j - 1].equals("^")) {
                    h += 1;
                    str = "";
                    continue;
                } else {
                    masiv[r] = list_f[j];
                    r += 1;
                    str = "";
                    continue;
                }
            } else if (list_f[j].equals("^")) {
                masiv[r] = list_f[j];
                r += 1;
                str = "";
                g = 1;
                continue;
            } else {
                str += list_f[j];
                if (j == list_f.length - 1) {
                    if (g == 1) {
                        if (h == 1) {
                            masiv[r] = new BigInteger(str).negate();
                            r += 1;
                            g = 0;
                            h = 0;
                            continue;
                        } else {
                            masiv[r] = new BigInteger(str);
                            r += 1;
                            g = 0;
                            continue;
                        }
                    } else {
                        if (h == 1) {
                            masiv[r] = new BigDecimal("-" + str);
                            r += 1;
                            h = 0;
                            continue;
                        } else {
                            masiv[r] = new BigDecimal(str);
                            r += 1;
                            continue;
                        }
                    }
                } else {
                    Pattern p2 = Pattern.compile("[+*/^()-]");
                    if (comp(p2, list_f[j+1])) {
                        if (g == 1) {
                            if (h == 1) {
                                masiv[r] = new BigInteger(str).negate();
                                r += 1;
                                g = 0;
                                h = 0;
                                continue;
                            } else {
                                masiv[r] = new BigInteger(str);
                                r += 1;
                                g = 0;
                                continue;
                            }
                        } else {
                            if (h == 1) {
                                masiv[r] = new BigDecimal("-" + str);
                                r += 1;
                                h = 0;
                                continue;
                            } else {
                                masiv[r] = new BigDecimal(str);
                                r += 1;
                                continue;
                            }
                        }
                    }
                }
            }
        }
        return masiv;
    }
}