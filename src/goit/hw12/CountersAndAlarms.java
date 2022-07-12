package goit.hw12;

import java.util.Date;

import static java.lang.Thread.sleep;

public class CountersAndAlarms {

    private static Object monitor = new Object (  );

    public static void main(String[] args) {

        Runnable secondCounter = new Runnable() {
            public static final int INTERVAL = 1000;
            private Date start;

            @Override
            public void run() {
                start = new Date (  );
                while ( true ) {
                    try {
                        sleep ( INTERVAL );
                        synchronized ( monitor ) { monitor .notifyAll (  ); }
                    } catch (InterruptedException ie) {
                    }
                    System.out.format ( "С начала сессии прошло %d сек%n", calcIntervalInSeconds (  ) );
                }
            }

            private long calcIntervalInSeconds (  ) {
                return ( new Date (  ) .getTime (  ) - start .getTime (  ) ) / 1000L;
            }
        };

        Runnable alarm = new Runnable() {
            public static final int ALARM_INTERVAL = 5000;
            Date previousAlarm;

            @Override
            public void run() {
                previousAlarm = new Date (  );
                while ( true ) {
                    try {
                        synchronized ( monitor ) { monitor .wait (  ); }
                        if ( isIntervalEllapsed (  ) ) {
                            System.out.println("Прошло 5 секунд");
                            updatePreviousAlarm (  );
                        }
                        // В задаче сказано "предусмотреть возможность оповещения..." Я правильно понял
                        // или надо было просто сделать sleep(5000) ?
                    } catch (InterruptedException ie) {
                    }
                }
            }

            private boolean isIntervalEllapsed (  ) {
                return new Date (  ) .getTime (  ) - previousAlarm .getTime (  ) >= ALARM_INTERVAL;
            }

            private void updatePreviousAlarm (  ) {
                previousAlarm = new Date (  );
            }
        };

        new Thread ( secondCounter ) .start (  );
        new Thread ( alarm ) .start (  );
    }
}
