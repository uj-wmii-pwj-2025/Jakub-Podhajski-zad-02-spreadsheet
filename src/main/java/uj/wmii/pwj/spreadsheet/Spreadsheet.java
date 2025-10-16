package uj.wmii.pwj.spreadsheet;

public class Spreadsheet {

    public String ref(String[][] input, String target) {
        int column = target.charAt(0) - 'A';
        int row = Integer.parseInt(target.substring(1)) - 1;
        String cell = input[row][column];
        if (cell.startsWith("$")) {
            return ref(input, cell.substring(1));
        }
        
        return cell;
    }

    public String[][] calculate(String[][] input) {
        for (int pass = 0; pass < input.length * input[0].length; pass++) {
            for (int row = 0; row < input.length; row++) {
                for (int col = 0; col < input[row].length; col++) {
                    String target = input[row][col];
                    
                    if (!target.startsWith("$") && !target.startsWith("=")) {
                        continue;
                    }
                    
                    if (target.charAt(0) == '$'){
                        input[row][col] = ref(input, target.substring(1));
                    }
                    else if (target.charAt(0) == '=') {
                        int start = target.indexOf('(');
                        int end = target.indexOf(')');

                        String argsString = target.substring(start + 1, end);
                        String[] parts = argsString.split(",");
                        String a = parts[0].trim();
                        String b = parts[1].trim();

                        if (a.charAt(0) == '$'){
                            String resolved = ref(input, a.substring(1));
                            if (resolved.startsWith("$") || resolved.startsWith("=")) {
                                continue;
                            }
                            a = resolved;
                        }
                        if (b.charAt(0) == '$'){
                            String resolved = ref(input, b.substring(1));
                            if (resolved.startsWith("$") || resolved.startsWith("=")) {
                                continue;
                            }
                            b = resolved;
                        }
                        //Hope it's not a hardcoded solution ;)
                        String operation = target.substring(1,4);
                        if (operation.equals("ADD")){
                            input[row][col] = String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
                        }
                        else if (operation.equals("SUB")){
                            input[row][col] = String.valueOf(Integer.parseInt(a) - Integer.parseInt(b));
                        }
                        else if (operation.equals("MUL")){
                            input[row][col] = String.valueOf(Integer.parseInt(a) * Integer.parseInt(b));
                        }
                        else if (operation.equals("DIV")){
                            input[row][col] = String.valueOf(Integer.parseInt(a) / Integer.parseInt(b));
                        }
                        else if (operation.equals("MOD")){
                            input[row][col] = String.valueOf(Integer.parseInt(a) % Integer.parseInt(b));
                        }
                    }
                }
            }
        }

        return input;
    }

    public static void main(String[] args) {
        Spreadsheet s = new Spreadsheet();
        String[][] input = {
                {"1", "=ADD($A2,3)"},
                {"=ADD(1,2)", "4"},
                {"0", "5"},
                {"$B4", "=ADD($A2,$B1)"}};
        String[][] ans = s.calculate(input);
        for (var row: ans) {
            for (var cell: row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
