package goit.hw12;

public class FizzAndBuzz {
    private static NumberSequence numberSequence = new NumberSequence ( 16 );

    public static void main(String[] args) {

        Runnable fizz = new Worker ( numberSequence, "fizz" ) {
            @Override
            public boolean zz ( int number ) {
                return (number % 3 == 0) && (number % 5 != 0);
            }
        };

        Runnable buzz = new Worker ( numberSequence, "buzz" ) {
            @Override
            public boolean zz ( int number ) {
                return (number % 3 != 0) && (number % 5 == 0);
            }
        };
        Runnable fizzBuzz = new Worker ( numberSequence, "fizzbuzz" ) {
            @Override
            public boolean zz ( int number ) {
                return (number % 3 == 0) && (number % 5 == 0);
            }
        };
        Runnable number = new Worker ( numberSequence ) {
            @Override
            public boolean zz ( int number ) {
                return (number % 3 != 0) && (number % 5 != 0);
            }
        };

        new Thread ( fizz ) .start (  );
        new Thread ( buzz ) .start (  );
        new Thread ( fizzBuzz ) .start (  );
        new Thread ( number ) .start (  );
    }
}

class NumberSequence {

    private int limit;
    private int current = 1;

    public NumberSequence ( int limit ) {
        this .limit = limit;
    }

    public boolean isOver (  ) {
        return current > limit;
    }

    public synchronized int getNumber (  ) {
        return current;
    }

    public synchronized void toNext (  ) {
        current++;
    }
}

abstract class Worker implements Runnable {
    private NumberSequence numberSequence;
    private String message;

    public Worker ( NumberSequence numberSequence, String message ) {
        this .numberSequence = numberSequence;
        this .message = message;
    }
    public Worker ( NumberSequence numberSequence ) {
        this .numberSequence = numberSequence;
        this .message = null;
    }

    @Override
    public void run() {
        while ( true ) {
            try {
                synchronized ( numberSequence ) {
                    if ( numberSequence .isOver (  ) ) {
                        return;
                    }
                    if ( zz ( numberSequence .getNumber ( ) ) ) {
                        System.out.println ( message == null ? numberSequence .getNumber ( ) : message );
                        numberSequence .toNext (  );
                    }
                }
            } catch ( Exception e ) {}
        }
    }

    public abstract boolean zz ( int number );
}
