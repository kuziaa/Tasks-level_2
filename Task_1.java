import java.util.Scanner;
import java.util.regex.*;

public class Task_1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);               // Считываем строку с аргументами
        String f_line = in.nextLine();                     // и заносим все аргументы в массив
        String[] f_line_mass = f_line.split(" ");
        String s_line = new String();
        while(true) {
            if(f_line.length() == 0) {
                System.out.println("Первая строка не должна быть пустой");
                break;
            }
            int x = 0;
            String n_line = in.nextLine();
            if(n_line.length() == 0) {                     // После ввода пустой строки программа заканчивает работу
                break;
            }
            String[] n_line_mass = n_line.split(" ");
            for(int i = 0; i < f_line_mass.length; i++) {
                for(int j = 0; j < n_line_mass.length; j++) {
                    Pattern p = Pattern.compile(f_line_mass[i]);
                    Matcher m = p.matcher(n_line_mass[j]);
                    if(m.matches() == true) {
                        System.out.println(n_line);
                        x = 1;                             // Если соответствие найдено, переходим к следующей строке
                        break;
                    }
                }
                if(x == 1) break;
            }
        }
    }
}
