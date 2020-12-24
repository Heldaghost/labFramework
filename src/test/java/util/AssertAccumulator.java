package util;


public class AssertAccumulator {
    private boolean assertsPassed;

    private StringBuilder errors;

    public AssertAccumulator() {
        this.errors = new StringBuilder();
        this.assertsPassed = true;
    }

    public String getAccumulatedErrorMessage() {
        return this.errors.toString();
    }

    public boolean isAssertsPassed() {
        return assertsPassed;
    }

    public void release() {
        if (!this.assertsPassed) {
            throw new AssertionError(this.getAccumulatedErrorMessage());
        }
    }

    public void accumulate(Runnable action) {
        try {
            action.run();
        } catch (AssertionError e) {
            this.registerError(e);
        } catch (Exception exception) {
            this.registerError(exception);
        }
    }


    public void accumulate(Runnable action, String key) {
        try {
            action.run();
        } catch (AssertionError e) {
            this.registerError(e);
        } catch (Exception exception) {
            this.registerError(exception);


        }
    }

    private void registerError(Throwable e) {
        this.assertsPassed = false;
        this.errors.append(e.getMessage() + "\n" + stackTraceToString(e));
        this.errors.append(System.lineSeparator());
    }

    private String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
            if (k > 5) break;
            k++;
        }
        return sb.toString();
    }
}