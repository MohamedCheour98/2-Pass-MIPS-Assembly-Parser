import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.lang.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class Parser{

    private static final String[] operations = {"and", "or", "add", "addi", "sll", "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"};
    private static final HashMap<String, Integer> labelsAndAddresses = new HashMap<>();
    private static final HashMap<String, String> registersAndBinary = new HashMap<>();
    private static int lineNumber = -1;
    private static int lineNumber1 = -1;
    private static String a;
    private static String b;
    private static String c;
    private static String d;

    public static void main(String[] args) throws IOException {
    
        if (0 < args.length) {
            String filename = args[0];
            File file = new File(filename);
            
            BufferedReader br = new BufferedReader(new FileReader(file));

        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (isBlank(line)) {
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

        registersAndBinary.put("$zero", "00000");
        registersAndBinary.put("$0", "00000");

        registersAndBinary.put("$v0", "00010");
        registersAndBinary.put("$v1", "00011");

        registersAndBinary.put("$a0", "00100");
        registersAndBinary.put("$a1", "00101");
        registersAndBinary.put("$a2", "00110");
        registersAndBinary.put("$a3", "00111");

        registersAndBinary.put("$t0", "01000");
        registersAndBinary.put("$t1", "01001");
        registersAndBinary.put("$t2", "01010");
        registersAndBinary.put("$t3", "01011");
        registersAndBinary.put("$t4", "01100");
        registersAndBinary.put("$t5", "01101");
        registersAndBinary.put("$t6", "01110");
        registersAndBinary.put("$t7", "01111");

        registersAndBinary.put("$s0", "10000");
        registersAndBinary.put("$s1", "10001");
        registersAndBinary.put("$s2", "10010");
        registersAndBinary.put("$s3", "10011");
        registersAndBinary.put("$s4", "10100");
        registersAndBinary.put("$s5", "10101");
        registersAndBinary.put("$s6", "10110");
        registersAndBinary.put("$s7", "10111");

        registersAndBinary.put("$t8", "11000");
        registersAndBinary.put("$t9", "11001");

        registersAndBinary.put("$sp", "11101");
        registersAndBinary.put("$ra", "11111");


        BufferedReader br1 = new BufferedReader(new FileReader(file));

        for (String line = br1.readLine(); line != null; line = br1.readLine()) {
            outerLoop:
            if (isBlank(line)) {
                if (line.charAt(0) != '#'){
                    for (String op : operations) {
                        if (isContain(line, op)) {
                            lineNumber1++;
                            String[] components = line.split("[ ,\t#:]+");
                            components = containsOpcode(components);
                            System.out.println();
                            break outerLoop;
                        }

                    }

                    for (String op : operations) {
                        /*System.out.println(!line.isBlank());
                        System.out.println(!isContain(line, "#"));
                        System.out.println(!line.contains(":"));*/
                        if (isBlank(line) && !line.contains("#") && !line.contains(":") && !isContain(line, op)){
                            System.out.println("invalid instruction: " + line.substring(0, 4));
                            System.exit(1);
                        }
                    }
                    
                }


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

        if (array[0].equals("")){
            array = slice(array, 1, array.length);
        }


        for (String op : operations){
            if (!array[0].equals(op) && array[0].contains(op) && array[0].length() > 3 ) {
                if (array[0].charAt(3) != 'i') {
                    array[0] = array[0].replace("$", ", $");
                    array = increaseSize(array);
                    break;
                }
            }

        }

        for (int i = 0; i < array.length; i++){

            if (array[i].equals("add")) {
                array = getBinary(array, i);
                System.out.print(b + " " + c + " " + a  + " 00000 100000");
                break;
            }

            else if (array[i].equals("sub")){
                array = getBinary(array, i);
                System.out.print(b + " " + c + " " + a  + " 00000 100010");
                break;
            }

            else if (array[i].equals("and")){
                array = getBinary(array, i);
                System.out.print(b + " " + c + " " + a  + " 00000 100100");
                break;
            }

            else if (array[i].equals("or")){
                array = getBinary(array, i);
                System.out.print(b + " " + c + " " + a  + " 00000 100101");
                break;
            }

            else if (array[i].equals("addi")){
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 4);
                System.out.print("001000 ");

                for (String key : registersAndBinary.keySet()){
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].equals(key)) b = registersAndBinary.get(key);
                }

                int x = Integer.parseInt(array[3]);
                c = Integer.toBinaryString(x);
                while (c.length() != 16){

                    if (c.length() > 16){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 16){
                        if (x < 0){
                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0'){

                            }
                        }
                        else if (x >= 0){
                            c = "0" + c;
                        }

                    }

                }

                System.out.print(a + " " + b + " " + c);
                break;
            }

            else if (array[i].equals("slt")){
                array = getBinary(array, i);
                System.out.print(b + " " + c + " " + a  + " 00000 101010");
                break;
            }

            else if (array[i].equals("sll")){
                array = slice(array, i, i + 4);
                System.out.print("000000 00000 ");

                for (String key : registersAndBinary.keySet()){
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].equals(key)) b = registersAndBinary.get(key);
                }
                int x = Integer.parseInt(array[3]);
                c = Integer.toBinaryString(x);
                while (c.length() != 5){

                    if (c.length() > 5){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 5){
                        if (x < 0){
                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0'){

                            }
                        }
                        else if (x >= 0){
                            c = "0" + c;
                        }
                    }

                }
                System.out.print(b + " " + a + " " + c  + " 000000");
                break;
            }

            else if (array[i].equals("beq")){
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 4);
                System.out.print("000100 ");

                for (String key : registersAndBinary.keySet()){
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].equals(key)) b = registersAndBinary.get(key);

                }

                int x = (-(lineNumber1 + 1) + labelsAndAddresses.get(array[3]));
                c = Integer.toBinaryString(x);


                while (c.length() != 16){

                    if (c.length() > 16){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 16){
                        if (x < 0){
                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0'){

                            }
                        }
                        else if (x >= 0){
                            c = "0" + c;
                        }
                    }

                }
                System.out.print(a + " " + b + " " + c);
                break;
            }

            else if (array[i].equals("bne")){
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 4);
                System.out.print("000101 ");

                for (String key : registersAndBinary.keySet()){
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].equals(key)) b = registersAndBinary.get(key);

                }

                int x = (-(lineNumber1 + 1) + labelsAndAddresses.get(array[3]));
                c = Integer.toBinaryString(x);


                while (c.length() != 16){

                    if (c.length() > 16){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 16){
                        if (x < 0){
                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0'){

                            }
                        }
                        else if (x >= 0){
                            c = "0" + c;
                        }
                    }

                }

                System.out.print(a + " " + b + " " + c);
                break;
            }

            else if (array[i].equals("lw")){
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 3);
                System.out.print("100011 ");

                for (String key : registersAndBinary.keySet()){
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].substring(2,5).equals(key)) b = registersAndBinary.get(key);
                }

                int y = Character.getNumericValue(array[2].charAt(0));
                c = Integer.toBinaryString(y);

                while (c.length() != 16){

                    if (c.length() > 16){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 16){
                        if (y < 0){
                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0'){

                            }
                        }
                        else if (y >= 0){
                            c = "0" + c;
                        }

                    }

                }

                System.out.print(b + " " + a + " " + c);


                break;
            }

            else if (array[i].equals("sw")){
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 3);
                System.out.print("101011 ");

                for (String key : registersAndBinary.keySet()){
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].substring(2,5).equals(key)) b = registersAndBinary.get(key);
                }

                int y = Character.getNumericValue(array[2].charAt(0));
                c = Integer.toBinaryString(y);

                while (c.length() != 16){

                    if (c.length() > 16){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 16){
                        if (y < 0){
                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0'){

                            }
                        }
                        else if (y >= 0){
                            c = "0" + c;
                        }

                    }

                }

                System.out.print(b + " " + a + " " + c);


                break;
            }

            else if (array[i].equals("j")){
                c = null;

                array = slice(array, i, i + 2);
                System.out.print("000010 ");

                int x = labelsAndAddresses.get(array[1]);
                c = Integer.toBinaryString(x);

                while (c.length() != 26){

                    if (c.length() > 26){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 26){
                        if (x < 0){
                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0'){

                            }
                        }
                        else if (x >= 0){
                            c = "0" + c;
                        }
                    }

                }

                System.out.print(c);
                break;
            }

            else if (array[i].equals("jr")){
                a = null;

                array = slice(array, i, i + 2);
                System.out.print("000000 ");

                for (String key : registersAndBinary.keySet()){
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                }

                System.out.print(a + " 000000000000000 001000");
                break;
            }

            else if (array[i].equals("jal")){
                c = null;

                array = slice(array, i, i + 2);
                System.out.print("000011 ");

                int x = labelsAndAddresses.get(array[1]);
                c = Integer.toBinaryString(x);

                while (c.length() != 26){

                    if (c.length() > 26){
                        if (c.charAt(0) == '1'){
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0'){
                            c = c.replace("0", "");
                        }
                    }

                    else if (c.length() < 26){
                        if (x < 0){

                            if (c.charAt(0) == '1'){
                                c = "1" + c;
                            }

                            if (c.charAt(0) == '0'){
                            }
                        }

                        else if (x >= 0){
                            c = "0" + c;
                        }
                    }

                }

                System.out.print(c);
                break;
            }

        }

        return array;
    }

    private static String[] getBinary(String[] array, int i) {
        array = slice(array, i, i + 4);
        System.out.print("000000 ");

        for (String key : registersAndBinary.keySet()){
            if (array[1].equals(key)) a = registersAndBinary.get(key);
            if (array[2].equals(key)) b = registersAndBinary.get(key);
            if (array[3].equals(key)) c = registersAndBinary.get(key);
        }
        return array;
    }


    private static boolean isBlank(final CharSequence cs) {

        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String[] increaseSize(String[] original) {

        String [] temp = new String[original.length + 1];

            temp[0] = original[0].substring(0,3);
            temp[1] = original[0].substring(5, 8);
            temp[2] = original[1];
            temp[3] = original[2];

        original = temp;
        return original;
    }

    private static boolean isContain(String source, String subItem){

        String pattern = "\\b"+subItem+"\\b";
        Pattern p= Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }
    
}

