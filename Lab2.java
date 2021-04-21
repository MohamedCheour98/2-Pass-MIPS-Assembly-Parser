import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.*;
import java.util.Scanner;

public class Lab2{

    static String operations[] = {"and", "or", "add", "addi", "sll", "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"};

    public static void main(String[] args) throws IOException {


        HashMap<String, Integer> labelsAndAddresses = new HashMap<String, Integer>();
        int lineNumber = -1;

        BufferedReader br = new BufferedReader(new FileReader("C:/Users/HP/Desktop/Cal Poly/Classes/CPE 315/Lab21/src/test2.asm"));

        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (!isBlank(line)) {
                if (line.charAt(0) != '#') {
                    for (String op : operations) {
                        if (line.contains(op)) {
                            lineNumber++;
                            break;
                        }

                    }
                    String[] words = line.split("[\\s,#]+");
                    if (words[0].contains(":")) {
                        String[] label = words[0].split(":");
                        if (words.length == 1){
                            labelsAndAddresses.put(label[0], lineNumber+1);
                        }
                        else labelsAndAddresses.put(label[0], lineNumber);

                    }

                }

            }

        }
        System.out.println(labelsAndAddresses);
        System.out.println(lineNumber);

        BufferedReader br1 = new BufferedReader(new FileReader("C:/Users/HP/Desktop/Cal Poly/Classes/CPE 315/Lab21/src/test2.asm"));

        for (String line = br1.readLine(); line != null; line = br1.readLine()) {
            if (!isBlank(line)) {
                if (line.charAt(0) != '#'){
                    for (String op : operations) {
                        if (line.contains(op)) {
                            String[] components = line.split("[:\s,\t]+");
                            components = containsOpcode(components);
                            System.out.println(Arrays.toString(components));
                            break;
                        }
                    }



                    /*if (components[i].equals("add")){
                        if(components[i+1].equals("$zero"))
                    }*/
                }

            }

        }
    }

    public static String[] slice(String[] arr, int start, int end) {

        String[] slice = new  String[end - start];

        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        return slice;
    }

    private static String[] containsOpcode(String[] array){

        outerLoop:
        for (String op : operations){
            for (int i = 0; i < array.length; i++){
                if (array[i].contains(op)){
                    array = slice(array, i, array.length);
                    break outerLoop;
                }

            }

        }
        return array;
    }
    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

