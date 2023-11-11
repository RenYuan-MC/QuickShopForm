package ltd.rymc.form.quickshop.utils;


public class InputUtil {

    public static boolean checkInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean checkInput(String input, String input1){
        return !(
                input != null &&
                !input.trim().isEmpty() &&
                !input.trim().contains(" ") &&
                input1 != null &&
                input.trim().equals(input1.trim())
        );
    }

}
