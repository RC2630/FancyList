public class CustomExceptions {

    public static class NoSuchIteratorException extends IllegalStateException {
        // throw this when no such iterator exists
    }

    public static class NotComparableException extends IllegalStateException {
        // throw this when the elements are not comparable with one another
    }

    public static class NotNumberException extends IllegalStateException {
        // throw this when the elements are not numbers (Integers or Doubles)
    }

}